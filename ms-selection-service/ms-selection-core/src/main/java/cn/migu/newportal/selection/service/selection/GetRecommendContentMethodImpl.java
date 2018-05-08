package cn.migu.newportal.selection.service.selection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import cn.migu.compositeservice.otherservice.GetUserPreSubBookInfo.GetUserPreSubBookInfoRequest;
import cn.migu.compositeservice.otherservice.GetUserPreSubBookInfo.GetUserPreSubBookInfoResponse;
import cn.migu.newportal.cache.bean.book.BookItem;
import cn.migu.newportal.cache.business.user.UserManager;
import cn.migu.newportal.cache.cache.service.*;
import cn.migu.newportal.cache.util.*;
import cn.migu.newportal.selection.bean.local.request.RecommendContentRequest;
import cn.migu.newportal.selection.engine.ContentServiceEngine;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.constants.SystemConstants;
import cn.migu.newportal.util.constants.Types;
import cn.migu.newportal.util.http.UrlTools;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode.ErrorCodeAndDesc;
import cn.migu.newportal.util.tools.ProtoUtil;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.RecommendContentResponseOuterClass.RecommendContentData;
import cn.migu.selection.proto.base.RecommendContentResponseOuterClass.RecommendContentResponse;
import cn.migu.selection.proto.base.RecommendContentResponseOuterClass.ShareInfo;
import cn.migu.userservice.GetMyBookScore.GetMyBookScoreReq;
import cn.migu.userservice.GetMyBookScore.GetMyBookScoreResponse;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 阅读末页 推荐文本
 * 
 * @pluginCode：recommend_content
 * @author wanghao
 * @version C10 2017年11月13日
 * @since SDP V300R003C10
 */
public class GetRecommendContentMethodImpl extends ServiceMethodImpl<RecommendContentResponse, ComponentRequest>
{
    // 日志信息
    private static Logger LOGGER = LoggerFactory.getLogger(GetRecommendContentMethodImpl.class.getName());
    
    // 方法名称
    private static final String METHOD_NAME = "getRecommendContent";
    
    // 我的评分默认为0 default
    private static final int MY_DEFAULT_SCORE = 0;
    
    public GetRecommendContentMethodImpl()
    {
        super(METHOD_NAME);
    }
    
    /**
     * 推荐文本 方法实现
     * 
     * @author wanghao
     * @date 2017年10月30日
     * @param controller
     * @param request
     * @return
     */
    @ImplementMethod
    public InvokeResult<RecommendContentResponse> getRecommendContent(ServiceController controller,
        ComponentRequest request)
    {
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("Enter GetRecommendContentMethodImpl.getRecommendContent !identityId:{}, Request :{}",
                CommonHttpUtil.getIdentity(),JsonFormatUtil.printToString(request));
        }
        RecommendContentRequest requestBean = new RecommendContentRequest(request.getParamMapMap());
        RecommendContentResponse.Builder builder = RecommendContentResponse.newBuilder();
        RecommendContentData.Builder builderData = RecommendContentData.newBuilder();
        builder.setPluginCode(StringTools.nvl(requestBean.getPluginCode()));

        String bid = requestBean.getBid();
        if (StringTools.isEmpty(bid))
        {
            LOGGER.error("Error GetRecommendContentMethodImpl.getRecommendContent Error ! identityId:{},bid:{}",
                CommonHttpUtil.getIdentity(),
                bid);
            BeanMergeUtils.setCommonResponseField(builder, ErrorCodeAndDesc.PARAMNAME_EROOR, ParamConstants.FALSE);
            return new InvokeResult<RecommendContentResponse>(builder.build());
        }

        // 获取图书黑名单，如果图书在黑名单中，则不展示此组件
        List<String> blackBookIdList = BlackBookListService.getInstance().getBlackBookIdList();
        if (Util.isNotEmpty(blackBookIdList) && blackBookIdList.contains(bid))
        {
            LOGGER.error(
                "GetRecommendContentMethodImpl.getRecommendContent, book is on the blacklist, bid={}, identityId={}",
                bid,
                CommonHttpUtil.getIdentity());
            ProtoUtil.buildResultCode(builder, ErrorCodeAndDesc.BOOK_ON_BLACKLIST, ParamConstants.NOT_VISABLE);
            return new InvokeResult<RecommendContentResponse>(builder.build());
        }

        // 从缓存中获取图书信息
        BookItem bookItem = PortalContentInfoCacheService.getInstance().getBookItem(bid);
        // 图书如果不存在或者图书已经下架则返回错误码
        if (Util.isEmpty(bookItem) || StringTools.isEq(ParamConstants.BOOK_SHELF_STATUS, bookItem.getStatus()))
        {
            LOGGER.error(
                "GetRecommendContentMethodImpl.getRecommendContent, bookItem is empty or book has been laid down, bid={}, identityId={}",
                bid,
                CommonHttpUtil.getIdentity());

            ProtoUtil.buildResultCode(builder, ErrorCodeAndDesc.BOOK_EMPTY_OR_SHELF, ParamConstants.NOT_VISABLE);
            return new InvokeResult<RecommendContentResponse>(builder.build());
        }
        //封装推荐文本数据
        setRecommendContentData(requestBean, builderData, bid, bookItem);
        //封装shareInfo
        builderShareInfo(builderData, requestBean);

        // 封装公共参数isShowLine，isMarginTop，isMarginBottom，isPaddingTop
        ProtoUtil.buildCommonData(builderData, request.getParamMapMap());
        //BeanMergeUtils.setCommonResponseField(builder, ErrorCodeAndDesc.SUCCESS, ParamConstants.IS_VISABLE);
        // 设置返回数据是否可见、状态码、描述
        ProtoUtil.buildResultCode(builder, ErrorCodeAndDesc.SUCCESS, ParamConstants.IS_VISABLE);
        builder.setData(builderData.build());

        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("Exit GetRecommendContentMethodImpl.getRecommendContent !identityId:{}, Response :{}",
                CommonHttpUtil.getIdentity(),JsonFormatUtil.printToString(builder));
        }
        return new InvokeResult<RecommendContentResponse>(builder.build());

    }

    /**
     * 封装shareInfo
     */
    private void builderShareInfo(RecommendContentData.Builder dataBuilder, RecommendContentRequest requestBean)
    {
        ShareInfo.Builder shareInfo = ShareInfo.newBuilder();
        // 1:安卓 2：IOS
        String uaType = CommonHttpUtil.getUAType();
        //判断当前版本是否支持shareContentEx()方法1支持0不支持
        String isSupportshareContentEx = "0";
        //是否支持QQ分享1支持0不支持
        String isSupportQQ = "0";
        if (StringTools.isEq(ParamConstants.ANDROID_TYPE, uaType))
        {
            isSupportshareContentEx = isSupportAbility("and_shareContentEx");
            isSupportQQ = isSupportAbility("and_shareQQ");
        }
        //分享方式能力支持
        else if (StringTools.isEq(ParamConstants.IOS_TYPE, uaType))
        {
            isSupportshareContentEx = isSupportAbility("ios_shareContentEx");
            isSupportQQ = isSupportAbility("ios_shareQQ");
        }
        shareInfo.setShareChange(StringTools.nvl(requestBean.getShareChange()));
        shareInfo.setIsSupportshareContentEx(StringTools.nvl(isSupportshareContentEx));
        shareInfo.setIsSupportQQ(StringTools.nvl(isSupportQQ));
        //是否支持 更多分享 1支持0不支持
        String iszhichiMore = isSupportAbility("and_moreFetion");
        shareInfo.setIszhichiMore(StringTools.nvl(iszhichiMore));
        //是否支持和飞信分享 1支持0不支持
        String iszhichiHFX = isSupportAbility("and_newfetionShare");
        shareInfo.setIszhichiHFX(StringTools.nvl(iszhichiHFX));
        //点击分享者的mbid
        String mbid = CommonHttpUtil.getIdentity();
        shareInfo.setMbid(StringTools.nvl(mbid));
        //分享成功是否跳转页面 1:是;2:否; 默认2
        shareInfo.setShareSuccessLink(requestBean.getShareSuccessLink());
        //分享成功跳转地址
        String successLink =
            UrlTools.getRelativelyUrl(requestBean.getSuccessLink(), PropertiesConfig.getUrlPrefix("defaultConfig"));
        shareInfo.setSuccessLink(StringTools.nvl(successLink));
        //跳转周期设置 : 1:总共一次;2:一天一次;3:不限次数; 默认1
        shareInfo.setLinkCycle(StringTools.nvl(requestBean.getLinkCycle()));
        dataBuilder.setShareInfo(shareInfo);
    }

    /**
     * 根据能力关键字判断当前版本能力
     * @param capacityKey
     * @return
     */
    public String isSupportAbility(String capacityKey)
    {
        if (StringTools.isEmpty(capacityKey))
        {
            return null;
        }
        return PortalCapacitySupportCacheService.getInstance().isSupportAbility(capacityKey);
    }
    
    /**
     * 封装推荐文本数据
     * 
     * @author wanghao
     * @date 2017年10月30日
     * @param requestBean
     * @param bid
     */
    private void setRecommendContentData(RecommendContentRequest requestBean, RecommendContentData.Builder dataBuilder, String bid, BookItem bookItem)
    {
        Map<String, String> params = UesServiceUtils.buildPublicParaMap(null, null);
        params.put(ParamConstants.BID, requestBean.getBid());
        // 打赏页
        String rewardUrl = "";
        if (StringTools.isEq(ParamConstants.IOS_TYPE, CommonHttpUtil.getUAType()) &&
            StringTools.isEq(Types.ZERO, isSupportAbility("ios_restore")))
        {
            rewardUrl = UrlTools.processForView(UrlTools.getRelativelyUrl(requestBean.getGoRewardUrl(),
                SystemConstants.DEFAULT_DOMAIN), params);
        }
        else if(StringTools.isEq(ParamConstants.ANDROID_TYPE, CommonHttpUtil.getUAType()))
        {
            rewardUrl = UrlTools.processForView(UrlTools.getRelativelyUrl(requestBean.getGoRewardUrl(),
                SystemConstants.DEFAULT_DOMAIN), params);
        }

        // 评论页
        String commentUrl = UrlTools.processForView(UrlTools.getRelativelyUrl(requestBean.getGoCommentUrl(),
            SystemConstants.DEFAULT_DOMAIN), params);
        // 书本名称
        String bookName = bookItem.getBookName();
        // 书本logo
        String bookCoverLogo = BookUtils.getBookCover(bookItem);
        boolean isReserve = false;
        String myScore = StringTools.nvl(MY_DEFAULT_SCORE);
        String msisdn = CommonHttpUtil.getIdentity();
        if (!UserManager.isGuestUser(msisdn))
        {
            // 是否预定
            isReserve = isReserveBook(bookItem, msisdn);
            // 我的评分
            GetMyBookScoreReq.Builder getMyBookScoreRequest = GetMyBookScoreReq.newBuilder();
            getMyBookScoreRequest.setMsisdn(StringTools.nvl(msisdn));
            getMyBookScoreRequest.setBookId(StringTools.nvl(bid));
            GetMyBookScoreResponse myScoreResponse =
                ContentServiceEngine.getInstance().getMyBookScore(getMyBookScoreRequest.build());
            if (Util.isNotEmpty(myScoreResponse))
            {
                myScore = myScoreResponse.getScore();
            }
        }
        // 书本状态 1 完本；2 连载
        String bookStatus = StringTools.isEq(ParamConstants.OVER_BOOK, bookItem.getIsFinish()) ? ParamConstants.OVER_BOOK
            : ParamConstants.SERIAL_BOOK;
        // 预订action地址
        String bookReserveAction = null;

        // 如果书本状态为连载的话设置action地址
        if (StringTools.isEq(ParamConstants.SERIAL_BOOK, bookStatus))
        {
            // 如果已经预订则取消预订，如果没有预订则预订
            String operationType = isReserve ? ParamConstants.BOOK_UNRESERVE : ParamConstants.BOOK_RESERVE;
            bookReserveAction = ActionUrlUtils.getReserveUpdateActionUrl(bid, operationType);
            dataBuilder.setBookReserveAction(bookReserveAction);
        }

        dataBuilder.setMyScore(StringTools.toInt(myScore, MY_DEFAULT_SCORE));
        dataBuilder.setAddBookScoreUrl(ActionUrlUtils.getAddPostScoreUrl(bid));
        String barId = PortalPostInfoCacheService.getInstance().getBarForumId(ParamConstants.BOOK_BAR, bid);
        dataBuilder.setAddBookPostUrl(ActionUrlUtils.getAddPostUrl(bid, barId, null));
        
        dataBuilder.setRewardUrl(StringTools.nvl(rewardUrl));
        dataBuilder.setCommentUrl(StringTools.nvl(commentUrl));
        // 月票链接地址
        String monthticUrlPrefix = UesServerServiceUtils.getPresetPage(UesPrePageKeyConstants.GEND_MONTHLY_TICKET_DETAIL_URL);
        Map<String, String> monthParams = new HashMap<>();
        monthParams.put(ParamConstants.BID, bid);
        monthticUrlPrefix = UesServiceUtils.getUESUrl(monthticUrlPrefix, monthParams);
        dataBuilder.setMonthTicketUrl(StringTools.nvl(monthticUrlPrefix));

        dataBuilder.setBookStatus(StringTools.nvl(bookStatus));
        dataBuilder.setBookName(StringTools.nvl(bookName));
        dataBuilder.setBid(bid);
        dataBuilder.setBookCoverLogo(StringTools.nvl(bookCoverLogo));
        dataBuilder.setIsReserve(isReserve);
        dataBuilder.setShareDesc(StringTools.nvl(requestBean.getShareDesc()));
        dataBuilder.setShareTitle(StringTools.nvl(requestBean.getShareTitle()));
        String richContent =
            UesServerServiceUtils.getUesRichContentInfo(requestBean.getInstanceId());
        dataBuilder.setRichContent(StringTools.nvl(richContent));

    }
    
    /**
     * 判断用户是否预订这本书
     * 
     * @author wanghao
     * @date 2017年10月31日
     * @param bookItem
     * @param msisdn
     * @return
     */
    private boolean isReserveBook(BookItem bookItem, String msisdn)
    {
        boolean isReserve = false;
        String bookItemType = bookItem.getItemType();
        if (Util.isNotEmpty(bookItemType) && Types.RESERVE_BOOK_TYPE.contains(bookItemType))
        {
            String reserveIds = GetUserReserveBooksCacheService.getInstance().getUserReserveBooks(msisdn,
                ParamConstants.PORTAL_TYPE_WAP,
                Types.RESERVE_BOOK_TYPE);
            if (Util.isNotEmpty(reserveIds) && reserveIds.contains(bookItem.getBookId()))
            {
                isReserve = true;
            }
        }
        else
        {
            GetUserPreSubBookInfoRequest.Builder getUserPreSubBookInfoRequest =
                GetUserPreSubBookInfoRequest.newBuilder();
            getUserPreSubBookInfoRequest.setMobile(msisdn);
            getUserPreSubBookInfoRequest.setBookId(bookItem.getBookId());
            getUserPreSubBookInfoRequest.setContentType(bookItem.getItemType());
            GetUserPreSubBookInfoResponse getUserPreSubBookInfoResponse =
                ContentServiceEngine.getInstance().isReserve(getUserPreSubBookInfoRequest.build());
            // 本书用户预定状态 1:预定状态 ；0：未预定状态
            isReserve = StringTools.toInt(ParamConstants.BOOK_UNRESERVE_STATE, 1) == getUserPreSubBookInfoResponse
                .getReserveStatus();
        }
        return isReserve;
    }
    
}
