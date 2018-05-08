package cn.migu.newportal.selection.service.selection;

import java.util.HashMap;
import java.util.Map;

import cn.migu.newportal.cache.util.*;
import org.apache.commons.lang.StringUtils;

import cn.migu.compositeservice.campaignservice.GetDrawNumber.DrawNumberResponse;
import cn.migu.newportal.cache.bean.book.AuthorInfo;
import cn.migu.newportal.cache.bean.book.BookItem;
import cn.migu.newportal.cache.bean.book.PortalChapterInfo;
import cn.migu.newportal.cache.business.user.UserManager;
import cn.migu.newportal.cache.cache.service.PortalAuthorInfoCacheService;
import cn.migu.newportal.cache.cache.service.PortalBookCoverCacheService;
import cn.migu.newportal.cache.cache.service.PortalCapacitySupportCacheService;
import cn.migu.newportal.cache.cache.service.PortalChapterInfoCacheService;
import cn.migu.newportal.cache.cache.service.PortalContentInfoCacheService;
import cn.migu.newportal.cache.cache.service.PortalContentInfoExCacheService;
import cn.migu.newportal.selection.engine.ContentServiceEngine;
import cn.migu.newportal.selection.service.PortalBookLinkServiceImpl;
import cn.migu.newportal.selection.util.SelectionConstants;
import cn.migu.newportal.util.constants.BookContants;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.constants.States;
import cn.migu.newportal.util.constants.SystemConstants;
import cn.migu.newportal.util.constants.Types;
import cn.migu.newportal.util.exception.PortalException;
import cn.migu.newportal.util.http.HttpUtil;
import cn.migu.newportal.util.http.UrlTools;
import cn.migu.newportal.util.other.NumberTools;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode;
import cn.migu.newportal.util.tools.RequestCheckUtil;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.PortalBookLinkResponseOuterClass.PortalBookLinkData;
import cn.migu.selection.proto.base.PortalBookLinkResponseOuterClass.PortalBookLinkResponse;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 图书内容链接
 * 
 * @author hushanqing
 * @version C10 2017年10月9日
 * @since SDP V300R003C10
 */
public class GetPortalBookLinkMethodImpl extends ServiceMethodImpl<PortalBookLinkResponse, ComponentRequest>
{
    // 日志对象
    private static Logger logger = LoggerFactory.getLogger(GetPortalBookLinkMethodImpl.class.getName());
    
    private static final String METHOD_NAME = "getPortalBookLink";
    
    /** 摇奖 */
    public static final String SHAKE_LOTTERY = "2";
    
    private PortalBookLinkServiceImpl service;
    
    public GetPortalBookLinkMethodImpl()
    {
        super(METHOD_NAME);
    }
    
    /**
     * 获取图书链接信息
     * 
     * @author hushanqing
     * @param controller
     * @param request
     * @return
     */
    @ImplementMethod
    public InvokeResult<PortalBookLinkResponse> getBookLinkInfo(ServiceController controller, ComponentRequest request)
    {
        if (logger.isDebugEnabled())
        {
            logger.info("enter PortalBookLinkInfo.getBookLinkInfo,identityId:{},request:{}" ,
                CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(request));
        }
        
        PortalBookLinkResponse.Builder portalBookLinkBuilder = PortalBookLinkResponse.newBuilder();
        
        portalBookLinkBuilder.setPluginCode(request.getParamMapMap().get(ParamConstants.PLUGINCODE));
        portalBookLinkBuilder.setIsvisable(ParamConstants.NOT_VISABLE);
        
        PortalBookLinkData.Builder bookLinkBuilder = PortalBookLinkData.newBuilder();
        
        // 设置通用数据
        setCommonData(request, bookLinkBuilder);
        
        // 获取请求参数
        String bookId = request.getParamMapMap().get(ParamConstants.BID);
        // 获取图书详情
        BookItem book = PortalContentInfoCacheService.getInstance().getBookItem(bookId);
        
        if (Util.isEmpty(book) || StringTools.isEq(ParamConstants.BOOK_SHELF_STATUS, book.getStatus()))
        {
            portalBookLinkBuilder.setStatus(MSResultCode.ErrorCodeAndDesc.BOOK_EMPTY_OR_SHELF.getCode());
            portalBookLinkBuilder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.BOOK_EMPTY_OR_SHELF.getDesc());
            return new InvokeResult<PortalBookLinkResponse>(portalBookLinkBuilder.build());
        }
        
        // 设置响应数据
        buildResponse(controller, request, portalBookLinkBuilder, bookLinkBuilder, bookId, book);
        
        if (logger.isDebugEnabled())
        {
            logger.debug("exit PortalBookLinkInfo,identityId:{} response:{} ",CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(portalBookLinkBuilder));
        }
        
        return new InvokeResult<PortalBookLinkResponse>(portalBookLinkBuilder.build());
    }
    
    /**
     * 设置响应数据
     * 
     * @author hushanqing
     * @param controller
     * @param request
     * @param portalBookLinkBuilder
     * @param bookLinkBuilder
     * @param bookId
     * @param book
     */
    private void buildResponse(ServiceController controller, ComponentRequest request,
        PortalBookLinkResponse.Builder portalBookLinkBuilder, PortalBookLinkData.Builder bookLinkBuilder, String bookId,
        BookItem book)
    {
        
        bookLinkBuilder.setIsUgc(StringTools.isEmpty(book.getIsUgc()) ? Types.FALSE : book.getIsUgc());
        bookLinkBuilder.setIsSupportDownload(getIsSupportDownload(book));
        bookLinkBuilder.setBookid(bookId);
        bookLinkBuilder.setBookLevel(StringTools.nvl(book.getBookLevel()));
        
        String bookFeeFasciculeDesc = "";
        if (StringTools.isEq(SelectionConstants.FASCICULESTATUS_TRUE, book.getFasciculeStatus()))
        {
            bookFeeFasciculeDesc = PropertiesConfig.getProperty("bath_order_purcharse_desc");
        }
        bookLinkBuilder.setBookFeeFasciculeDesc(bookFeeFasciculeDesc);
        
        bookLinkBuilder.setBookName(StringTools.nvl(book.getBookName()));
        bookLinkBuilder.setBookShareTitle(StringTools.nvl(book.getBookName()));
        bookLinkBuilder.setBookShareDesc(StringTools.nvl(book.getLongDescription()));
        bookLinkBuilder
            .setChargeMode(StringTools.isEmpty(book.getChargeMode()) ? Types.CHARGE_MODE_FREE : book.getChargeMode());
        bookLinkBuilder
            .setContentType(StringTools.isEmpty(book.getItemType()) ? BookContants.BOOKTYPE_EBOOK : book.getItemType());
        bookLinkBuilder.setIsPrePackFinished(StringTools.nvl(book.getIsPrePackFinished()));
        bookLinkBuilder.setIsSerial(StringTools.nvl(book.getIsSerial()));
        
        // 获取作者信息
        if (null != book.getAuthorId())
        {
            AuthorInfo authorInfo = null;
            
            authorInfo = PortalAuthorInfoCacheService.getInstance().getAuthorInfo(book.getAuthorId());
            
            if (authorInfo != null)
            {
                String authorName = authorInfo.getAuthorPenName();
                bookLinkBuilder.setAuthorName(StringTools.nvl(authorName));
            }
        }
        
        // 获取图书封面
        String bookCover =
            PortalBookCoverCacheService.getInstance().getBookCover(bookId, PropertiesConfig.getProperty("cdnUrl"));
        bookLinkBuilder.setBookCover(bookCover);
        
        // 获取章节信息
        String[] chapterIds = (String[])PortalContentInfoExCacheService.getInstance().getChapterIds(bookId);
        String firstChapterId = "";
        if (chapterIds != null && chapterIds.length > 0)
        {
            firstChapterId = chapterIds[0];
        }
        PortalChapterInfo chapterInfo =
            PortalChapterInfoCacheService.getInstance().getChapterItem(bookId, firstChapterId);
        
        if (chapterInfo != null)
        {
            String firstChapterName = chapterInfo.getChapterName();
            String firstChapterUrl = UesServiceUtils.getUESUrl(chapterInfo.getChapterContentUrl(), null);
            bookLinkBuilder.setFirstChapterName(StringTools.nvl(firstChapterName));
            bookLinkBuilder.setFirstChapterUrl(StringTools.nvl(firstChapterUrl));
        }
        
        bookLinkBuilder.setFirstChapterId(StringTools.nvl(firstChapterId));
        
        // 获取下载次数
        try
        {
            String downLoadCount =
                ContentServiceEngine.getInstance().getSingleNumAboutBookCounts(controller.getContext(), bookId);
            
            bookLinkBuilder.setDownloadNum(NumberTools.toInt(downLoadCount, 0));
        }
        catch (cn.migu.newportal.util.exception.PortalException e)
        {
            logger.error("call ContentServiceEngine.getSingleNumAboutBookCounts() failed, identityId:{},bookId:{},e:{}"
                ,CommonHttpUtil.getIdentity(), bookId, e);
        }
        
        // 月票链接地址
        String monthticUrlPrefix = UesServerServiceUtils.getPresetPage(UesPrePageKeyConstants.GEND_MONTHLY_TICKET_DETAIL_URL);
        String nodeId = request.getParamMapMap().get(ParamConstants.NID);
        Map<String, String> monthParams = new HashMap<>();
        monthParams.put(ParamConstants.BID, bookId);
        monthParams.put(ParamConstants.NID, nodeId);
        monthticUrlPrefix = UesServiceUtils.getUESUrl(monthticUrlPrefix, monthParams);
        
        bookLinkBuilder.setMonthticUrl(StringTools.nvl(monthticUrlPrefix));
        
        // 打赏链接
        String rewardUrl = UesServerServiceUtils.getPresetPage(UesPrePageKeyConstants.BOOK_REWARD_PAGE_URL);
        Map<String, String> rewardParams = new HashMap<>();
        rewardParams.put(ParamConstants.BID, bookId);
        rewardUrl = UesServiceUtils.getUESUrl(rewardUrl, rewardParams);
        bookLinkBuilder.setRewardUrl(StringTools.nvl(rewardUrl));
        
        // 是否支持下载
        String isSupportDownload = getIsSupportDownload(book);
        bookLinkBuilder.setIsSupportDownload(isSupportDownload);
        
        // 是否分享书券
        
        /** 用户手机号 */
        String identity = HttpUtil.getHeader(ParamConstants.X_Identity_ID);
        /** 抽奖活动id */
        String lotteryid = request.getParamMapMap().get(ParamConstants.LOTTERYID);
        boolean raffleBinded = getRaffleBinded(lotteryid, identity);
        bookLinkBuilder.setRaffleBinded(raffleBinded);
        
        // 抽奖接口
        Map<String, String> raffleParam = UesServiceUtils.buildPublicParaMap(null, null);
        raffleParam.put(ParamConstants.LOTTERYID, lotteryid);
        raffleParam.put(ParamConstants.SHAKE_LOTTERY_KEY, SHAKE_LOTTERY);
        String ajaxRaffle = UrlTools.processForView(ActionUrlConstants.USER_RAFFLE_URL, raffleParam);
        bookLinkBuilder.setAjaxRaffle(StringTools.nvl(ajaxRaffle));
        
        // 判断是否ajax请求
        String isAjax = request.getParamMapMap().get(ParamConstants.ISAJAX);
        if (StringUtils.isNotEmpty(isAjax) && ParamConstants.IS_AJAX.equals(isAjax))
        {
            ComponentRequest.Builder paramMapBuilder = request.toBuilder();
            paramMapBuilder.removeParamMap(ParamConstants.ISAJAX);
            bookLinkBuilder.putAllCtag(paramMapBuilder.getParamMapMap());
        }
        
        portalBookLinkBuilder.setData(bookLinkBuilder.build());
        portalBookLinkBuilder.setIsvisable(ParamConstants.IS_VISABLE);
        portalBookLinkBuilder.setStatus(MSResultCode.ErrorCodeAndDesc.SUCCESS.getCode());
        portalBookLinkBuilder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.SUCCESS.getDesc());
    }
    
    /**
     * 设置通用数据
     * 
     * @author hushanqing
     * @param request
     * @param bookLinkBuilder
     */
    private void setCommonData(ComponentRequest request, PortalBookLinkData.Builder bookLinkBuilder)
    {
        String isShowLine = request.getParamMapMap().get(ParamConstants.ISSHOWLINE);
        String isMarginTop = request.getParamMapMap().get(ParamConstants.ISMARGINTOP);
        String isMarginBottom = request.getParamMapMap().get(ParamConstants.ISMARGINBOTTOM);
        String isPaddingTop = request.getParamMapMap().get(ParamConstants.ISPADDINGTOP);
        
        bookLinkBuilder.setIsShowLine(RequestCheckUtil.getCSSType(isShowLine));
        bookLinkBuilder.setIsMarginTop(RequestCheckUtil.getCSSType(isMarginTop));
        bookLinkBuilder.setIsMarginBottom(RequestCheckUtil.getCSSType(isMarginBottom));
        bookLinkBuilder.setIsPaddingTop(RequestCheckUtil.getCSSType(isPaddingTop));
    }
    
    public PortalBookLinkServiceImpl getPortalBookLinkServiceImpl()
    {
        return service;
    }
    
    /**
     * 是否支持下载
     * 
     * @author hushanqing
     * @param book
     * @return
     */
    private String getIsSupportDownload(BookItem book)
    {
        String ua = HttpUtil.getHeader(ParamConstants.User_Agent);
        String andriodSupport =
            PortalCapacitySupportCacheService.getInstance().isSupportAbility("and_serialDownload", ua);
        String iosSupport = PortalCapacitySupportCacheService.getInstance().isSupportAbility("ios_serialDownload", ua);
        
        if (StringTools.isEq(book.getIsFinish(), Types.TRUE)
            || (StringTools.isEq(book.getIsFinish(), Types.FALSE)
                && StringTools.isEq(book.getFasciculeStatus(), States.FASCICULE_EXIST))
            || (StringTools.isEq(book.getIsPrePackFinished(), Types.TRUE))
                && StringTools.isEq(andriodSupport, Types.TRUE)
            || (StringTools.isEq(book.getIsPrePackFinished(), Types.TRUE)) && StringTools.isEq(iosSupport, Types.TRUE))
        {
            return Types.TRUE;
        }
        
        return Types.FALSE;
    }
    
    /**
     * 是否分享送书券
     * 
     * @author hushanqing
     * @param lotterid
     * @param identity
     * @return
     */
    private boolean getRaffleBinded(String lotterid, String identity)
    {
        if (StringTools.isEmpty(lotterid))
        {
            return false;
        }
        String tokenId = HttpUtil.getHeader(ParamConstants.X_Auth_Token);
        String loginType = UserManager.getUserLoginType(tokenId, identity);
        if (StringTools.isEq(Types.FALSE, loginType))
        {
            return true;
        }
        
        // 用户渠道代码
        String channelId = HttpUtil.getParameter(ParamConstants.CHANNEID);
        // ua
        String userAgent = HttpUtil.getParameter(ParamConstants.User_Agent);
        // 客户端版本
        String version = HttpUtil.getParameter(ParamConstants.CLIENT_VERSION);
        // 任务id
        String missionId = HttpUtil.getParameter(ParamConstants.MISSIONID);
        
        DrawNumberResponse response = null;
        try
        {
            response = ContentServiceEngine.getInstance().getDrawNumber(lotterid,
                identity,
                SystemConstants.PORTAL_TYPE,
                loginType,
                channelId,
                version,
                userAgent,
                missionId);
        }
        catch (PortalException e)
        {
            logger.error("call ContentServiceEngine.getDrawNumber failed,identityId:{},lotterid:{},portalType:1,e:{}",
                CommonHttpUtil.getIdentity(),
                lotterid,
                e);
        }
        
        if (Util.isEmpty(response))
        {
            return false;
        }
        // 免费次数
        int freeCounts = NumberTools.toInt(response.getDayFree(), 0);
        if (freeCounts > 0)
        {
            return true;
        }
        
        // 任务次数
        int missionCounts = 0;
        if (StringTools.isEmpty(missionId))
        {
            missionCounts = NumberTools.toInt(response.getReceiveNum(), 0);
        }
        else
        {
            missionCounts = NumberTools.toInt(response.getMissionTotalAmount(), 0)
                - NumberTools.toInt(response.getMissionUsedAmount(), 0);
        }
        if (missionCounts > 0)
        {
            return true;
        }
        return false;
    }
    
}
