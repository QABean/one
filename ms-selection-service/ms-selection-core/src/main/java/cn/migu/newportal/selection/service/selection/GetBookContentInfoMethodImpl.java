package cn.migu.newportal.selection.service.selection;

import cn.migu.compositeservice.bookproductservice.GetBookFeeDesc.GetBookFeeDescResponse;
import cn.migu.compositeservice.usersnsservice.QueryEreadPresentBooks.PresentBookInfo;
import cn.migu.newportal.cache.bean.book.AuthorInfo;
import cn.migu.newportal.cache.bean.book.BookItem;
import cn.migu.newportal.cache.bean.bookContent.BookStat_Portal;
import cn.migu.newportal.cache.business.user.UserManager;
import cn.migu.newportal.cache.cache.service.*;
import cn.migu.newportal.cache.util.*;
import cn.migu.newportal.selection.engine.ContentServiceEngine;
import cn.migu.newportal.selection.service.BookContentServiceImpl;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.constants.Types;
import cn.migu.newportal.util.date.DateTools;
import cn.migu.newportal.util.exception.PortalException;
import cn.migu.newportal.util.http.HttpUtil;
import cn.migu.newportal.util.http.UrlTools;
import cn.migu.newportal.util.other.NumberTools;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode;
import cn.migu.newportal.util.tools.RequestCheckUtil;
import cn.migu.selection.proto.base.BookContentResponseOuterClass.BookContentData;
import cn.migu.selection.proto.base.BookContentResponseOuterClass.BookContentResponse;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 图书详情页消费者
 * 
 * @author wulinfeng
 * @version C10 2017年5月31日
 * @since SDP V300R003C10
 */
public class GetBookContentInfoMethodImpl extends ServiceMethodImpl<BookContentResponse, ComponentRequest>
{
    // 日志对象
    private static Logger logger = LoggerFactory.getLogger(GetBookContentInfoMethodImpl.class);
    
    private static final String METHOD_NAME = "getBookContentInfo";
    
    private BookContentServiceImpl serviceImpl;
    
    /** 图书类型-图书 */
    private static final String CONTENTTYPE_BOOK = "1";
    
    /** 图书类型-听书 */
    private static final String CONTENTTYPE_LISTEN = "5";
    
    public GetBookContentInfoMethodImpl()
    {
        super(METHOD_NAME);
    }
    
    /**
     * 获取图书内容信息
     * 
     * @author hushanqing
     * @param controller
     * @param request
     * @return
     */
    @ImplementMethod
    public InvokeResult<BookContentResponse> getBookContentInfo(ServiceController controller, ComponentRequest request)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("enter GetBookContentInfoMethodImpl.getBookContentInfo,identityId:{} request:{}"
                ,CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(request));
        }
        
        BookContentResponse.Builder builder = BookContentResponse.newBuilder();
        builder.setPluginCode(request.getParamMapMap().get(ParamConstants.PLUGINCODE));
        builder.setIsvisable(ParamConstants.NOT_VISABLE);
        builder.setStatus(MSResultCode.ErrorCodeAndDesc.CONNECTION_FAILED.getCode());
        builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.CONNECTION_FAILED.getDesc());
        
        BookContentData.Builder dataBuilder = BookContentData.newBuilder();
        
        // 获取请求参数
        String bid = request.getParamMapMap().get(ParamConstants.BID);
        
        // 验证图书id是否存在
        if (!checkBookId(builder, bid))
        {
            if (logger.isDebugEnabled())
            {
                logger.debug(
                    "exit GetBookContentInfoMethodImpl.getBookContentInfo bookId is empty!,identityId:{} bookId:{}",
                    CommonHttpUtil.getIdentity(),
                    bid);
            }
            return new InvokeResult<BookContentResponse>(builder.build());
        }
        
        // 设置通用数据
        setCommonData(request, dataBuilder);
        // 调用缓存获取图书详情
        
        // 设置响应数据
        buildResponse(request, builder, dataBuilder, bid);
        
        if (logger.isDebugEnabled())
        {
            logger.debug("exit GetBookContentInfoMethodImpl,identityId:{} reponse :{} ",
                CommonHttpUtil.getIdentity(),
                JsonFormatUtil.printToString(builder));
        }
        
        return new InvokeResult<BookContentResponse>(builder.build());
    }
    
    /**
     * 验证图书id是否存在
     * 
     * @author hushanqing
     * @param builder
     * @param bid
     * @return
     */
    private boolean checkBookId(BookContentResponse.Builder builder, String bid)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("enter GetBookContentInfoMethodImpl.checkBookId(),bookId:{}", bid);
        }
        if (StringUtils.isEmpty(bid))
        {
            builder.setIsvisable(ParamConstants.NOT_VISABLE);
            builder.setStatus(MSResultCode.ErrorCodeAndDesc.BOOK_ID_EMPTY.getCode());
            builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.BOOK_ID_EMPTY.getDesc());
            
            return false;
        }
        return true;
    }
    
    /**
     * 设置通用数据
     * 
     * @author hushanqing
     * @param request
     * @param dataBuilder
     */
    private void setCommonData(ComponentRequest request, BookContentData.Builder dataBuilder)
    {
        String isShowLine = request.getParamMapMap().get(ParamConstants.ISSHOWLINE);
        String isMarginTop = request.getParamMapMap().get(ParamConstants.ISMARGINTOP);
        String isMarginBottom = request.getParamMapMap().get(ParamConstants.ISMARGINBOTTOM);
        String isPaddingTop = request.getParamMapMap().get(ParamConstants.ISPADDINGTOP);
        
        dataBuilder.setIsShowLine(RequestCheckUtil.getCSSType(isShowLine));
        dataBuilder.setIsPaddingTop(RequestCheckUtil.getCSSType(isPaddingTop));
        dataBuilder.setIsMarginTop(RequestCheckUtil.getCSSType(isMarginTop));
        dataBuilder.setIsMarginBottom(RequestCheckUtil.getCSSType(isMarginBottom));
    }
    
    /**
     * 设置响应数据
     * 
     * @author hushanqing
     * @param request
     * @param builder
     * @param dataBuilder
     * @param bid
     */
    private void buildResponse(ComponentRequest request, BookContentResponse.Builder builder,
        BookContentData.Builder dataBuilder, String bid)
    {
        BookItem book = PortalContentInfoCacheService.getInstance().getBookItem(bid);
        
        if (Util.isEmpty(book) || StringTools.isEq(ParamConstants.BOOK_SHELF_STATUS, book.getStatus()))
        {
            builder.setStatus(MSResultCode.ErrorCodeAndDesc.BOOK_EMPTY_OR_SHELF.getCode());
            builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.BOOK_EMPTY_OR_SHELF.getDesc());
            return;
        }
        
        dataBuilder.setContentType(StringUtils.isBlank(book.getItemType()) ? CONTENTTYPE_BOOK : book.getItemType());
        
        // 获取用户身份id
        String mobile = HttpUtil.getHeader(ParamConstants.X_Identity_ID);
        // 获取用户会员类别
        String isMember = UserManager.isMember(mobile) ? ParamConstants.TRUE : ParamConstants.FALSE;
        
        dataBuilder.setIsMember(StringTools.nvl2(isMember, ParamConstants.IS_NOT_MEMBER));
        
        String bookCover = "";
        try
        {
            bookCover =
                PortalBookCoverCacheService.getInstance().getBookCover(bid, PropertiesConfig.getProperty("cdnUrl"));
        }
        catch (Exception e)
        {
            logger.error("PortalBookCoverCacheService.getBookCover failed, identityId:{} bookId:{} e:{}",
                CommonHttpUtil.getIdentity(),
                bid,
                e);
        }
        dataBuilder.setBookCover(StringTools.nvl(bookCover));
        
        // 获取作者信息
        String authorId = book.getAuthorId();
        String authorUrl = "";
        String authorName = PropertiesConfig.getProperty("author_pen_name");
        if (StringTools.isNotEmpty(authorId))
        {
            // 获取配置项
            String authorUrlPrefix = UesServerServiceUtils.getPresetPage(UesPrePageKeyConstants.AUTHOR_SPACE_URL);
            Map<String, String> authorParams = new HashMap<>();
            authorParams.put(ParamConstants.AID, authorId);
            authorUrl = UesServiceUtils.getUESUrl(authorUrlPrefix, authorParams);
            
            // 从缓存取作者名称
            AuthorInfo author = null;
            try
            {
                author = PortalAuthorInfoCacheService.getInstance().getAuthorInfo(authorId);
            }
            catch (Exception e)
            {
                logger.error("PortalAuthorInfoCacheService.getAuthorInfo failed.identityId:{} authorId:{} e:{}",
                    CommonHttpUtil.getIdentity(),
                    authorId,
                    e);
            }
            if (author != null)
            {
                authorName = author.getAuthorPenName();
            }
        }
        dataBuilder.setAuthorUrl(StringTools.nvl(authorUrl));
        dataBuilder.setBookAuthor(StringTools.nvl(authorName));
        dataBuilder.setBookName(StringTools.nvl(book.getBookName()));
        
        // 获取评分信息
        String score = "0";
        try
        {
            BookStat_Portal bookStat = PotalBookStatMemCacheService.getInstance().getBatchBookStat(bid);
            if (Util.isNotEmpty(bookStat) && Util.isNotEmpty(bookStat.getBookScore()))
            {
                score = bookStat.getBookScore().getAvgScore();
            }
        }
        catch (Exception e)
        {
            logger.error("PotalBookStatMemCacheService.getBookScore failed.identityId:{} bookId:{} e:{} ",
                CommonHttpUtil.getIdentity(),
                bid,
                e);
        }
        
        dataBuilder.setBookStar(StringTools.nvl(score));
        
        // 主播名
        String speakerName = "";
        // 时长
        String audioDuration = "";
        // 主播详情地址地址
        String speakrUrl = "";
        // 分类名称
        String bookCate = "";
        String isUgc = "0";
        String ugcLink = "";
        String ugcDesc = "";
        String ugcTime = "";
        int bookNum = 0;
        boolean bookFinish = false;
        // 听书
        if (StringTools.isEq(CONTENTTYPE_LISTEN, book.getItemType()))
        {
            speakerName = book.getReaderName();
            
            long totalDuration = book.getTotalDuration();
            if (totalDuration >= 0)
            {
                audioDuration = DateTools.secondConvert(totalDuration);
            }
            
            // 主播ID
            String readId = book.getReaderid();
            if (StringUtils.isNotBlank(readId))
            {
                speakrUrl = UesServerServiceUtils.getPresetPage(UesPrePageKeyConstants.SPEAKER_DETAIL_URL);
                Map<String, String> speakerParam = new HashMap<>();
                speakerParam.put(ParamConstants.AID, readId);
                
                speakrUrl = UesServiceUtils.getUESUrl(speakrUrl, speakerParam);
            }
        }
        else
        {
            bookCate = book.getBookClassName();
            
            // 是否用户自上传图书
            isUgc = StringTools.isEmpty(book.getIsUgc()) ? Types.FALSE : book.getIsUgc();
            
            if (StringTools.isEq(Types.TRUE, isUgc))
            {
                // 获取配置项
                ugcLink = request.getParamMapMap().get(ParamConstants.LINKURL);
                Map<String, String> ugcLinkParam = new HashMap<>();
                ugcLinkParam.put(ParamConstants.BID, bid);
                ugcLink = UesServiceUtils.getUESUrl(ugcLink, ugcLinkParam);
                
                ugcDesc = request.getParamMapMap().get(ParamConstants.LINKDESC);
                ugcTime = DateTools.timeTransform(book.getCreateTime(), DateTools.DATE_FORMAT_24HOUR_19);
            }
            
            // 图书字数
            bookNum = NumberTools.toInt(book.getCount(), 0);
            
            String bookFinish_ = StringTools.isEmpty(book.getIsFinish()) ? Types.FALSE : book.getIsFinish();
            bookFinish = StringTools.isEq(bookFinish_, Types.TRUE);
        }
        
        // 资费接口地址
        Map<String, String> bookFeeParams = UesServiceUtils.buildPublicParaMap(null, null);
        bookFeeParams.put(ParamConstants.BID, bid);
        String ajaxBookFeeUrl = UrlTools.processForView(ActionUrlConstants.BOOK_FEE_URL, bookFeeParams);
        
        dataBuilder.setSpeakerName(StringTools.nvl(speakerName));
        dataBuilder.setAudioDuration(StringTools.nvl(audioDuration));
        dataBuilder.setSpeakerUrl(StringTools.nvl(speakrUrl));
        dataBuilder.setBookCate(StringTools.nvl(bookCate));
        dataBuilder.setIsUgc(StringTools.nvl(isUgc));
        
        dataBuilder.setUgcLink(StringTools.nvl(ugcLink));
        dataBuilder.setUgcDesc(StringTools.nvl(ugcDesc));
        dataBuilder.setUgcTime(StringTools.nvl(ugcTime));
        
        dataBuilder.setWordCount(bookNum);
        dataBuilder.setBookFinished(bookFinish);
        
        dataBuilder.setAjaxBookFee(ajaxBookFeeUrl);
        
        // 判断是否ajax请求
        String isAjax = request.getParamMapMap().get(ParamConstants.ISAJAX);
        if (StringTools.isNotEmpty(isAjax) && StringTools.isEq(ParamConstants.IS_AJAX, isAjax))
        {
            Map<String, String> params = new HashMap<>();
            params.put(ParamConstants.PLUGINCODE, request.getParamMapMap().get(ParamConstants.PLUGINCODE));
            params.put(ParamConstants.BID, request.getParamMapMap().get(ParamConstants.BID));
            dataBuilder.putAllCtag(params);
        }
        //新增赠书数据
        buildPresentBookData(book, dataBuilder, request.getParamMapMap());
        
        builder.setData(dataBuilder);
        builder.setIsvisable(ParamConstants.IS_VISABLE);
        
        // 设置成功返回码
        builder.setStatus(MSResultCode.ErrorCodeAndDesc.SUCCESS.getCode());
        builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.SUCCESS.getDesc());
    }
    
    public BookContentServiceImpl getBookContentServiceImpl()
    {
        return serviceImpl;
    }
    
    /**
     * 设置赠书活动相关数据
     * 
     * @author hushanqing
     * @param bookItem
     * @param dataBuilder
     */
    private void buildPresentBookData(BookItem bookItem, BookContentData.Builder dataBuilder,
        Map<String, String> paramMap)
    {
        String bookId = bookItem.getBookId();
        PresentBookInfo presentBookInfo =
            PresentBooksCacheService.getInstance().getPresentBookInfo(bookId);
        
        if (Util.isEmpty(presentBookInfo))
        {
            dataBuilder.setIsActivity(Types.FALSE);
            return;
        }
        String campaignState =
            getCampaignState(presentBookInfo.getEffectTime(), presentBookInfo.getExpriceTime(), bookId);
        if (StringTools.isEq(campaignState, Types.TRUE))
        {
            dataBuilder.setIsActivity(Types.TRUE);
            dataBuilder.setPresentType(StringTools.nvl(presentBookInfo.getPresentType()));
            // 赠书预制页
            String bookGivePrePage = UesServerServiceUtils.getPresetPage(UesPrePageKeyConstants.BOOK_GIVEAWAYS_PAGEURL);
            Map<String, String> params = UesServiceUtils.buildPublicParaMap(null, null);
            params.put(ParamConstants.BID, bookId);
            String bookGiveUrl = UesServiceUtils.getUESUrl(bookGivePrePage, params);
            dataBuilder.setBookGiveUrl(StringTools.nvl(bookGiveUrl));
            
            // 是否通过赠书获得该图书
            String isReceived = Types.FALSE;
            // 获取用户身份id
            String identityId = HttpUtil.getHeader(ParamConstants.X_Identity_ID);
            if (UserManager.isGuestUser(identityId))
            {
                dataBuilder.setIsReceived(isReceived);
                return;
            }
            
            // 赠送章节数
            String presentedChapterNum = "0";
            try
            {
                GetBookFeeDescResponse response =
                    ContentServiceEngine.getInstance().getBookFeeDesc(identityId, bookItem, Types.TRUE);
                if (Util.isNotEmpty(response))
                {
                    presentedChapterNum = response.getChapterNum();
                    isReceived = response.getIsPresented();
                }
            }
            catch (PortalException e)
            {
                logger.error(
                    " call ContentServiceEngine.getBookFeeDesc error!identityId:{},msisdn:{},bookItem:{},isPresentQuery:{},e:{}",
                    CommonHttpUtil.getIdentity(),
                    identityId,
                    bookItem,
                    Types.TRUE,
                    e);
            }
            
            dataBuilder.setIsReceived(isReceived);
            
            // 是赠书活动获得
            if (StringTools.isEq(isReceived, Types.TRUE))
            {
                String chargeMode =
                    Util.isEmpty(bookItem.getChargeMode()) ? Types.CHARGE_MODE_FREE : bookItem.getChargeMode();
                String receivedContext = null;
                // 按本提示语
                if (StringTools.isEq(chargeMode, Types.CHARGE_MODE_FREE)
                    || StringTools.isEq(chargeMode, Types.CHARGE_MODE_BOOK))
                {
                    receivedContext = paramMap.get(ParamConstants.BOOKDESC);
                }
                //按章提示语
                else if (StringTools.isEq(chargeMode, Types.CHARGE_MODE_CHAPTER))
                {
                    receivedContext = paramMap.get(ParamConstants.CHAPTERDESC);
                    if (StringTools.isNotEmpty(receivedContext))
                    {
                        receivedContext = StringTools.replace(receivedContext, "{presentedChapterNum}", presentedChapterNum);
                    }
                }
                
                dataBuilder.setReceivedContext(StringTools.nvl(receivedContext));
            }
        }
    }
    
    /**
     * 获取赠书活动状态 1:有 0：无
     * 
     * @author hushanqing
     * @param effectTime
     * @param expriceTime
     * @param bookId
     * @return
     */
    private String getCampaignState(long effectTime, long expriceTime, String bookId)
    {
        // 获取当前时间
        long currentDate = DateTools.getCurrentTimeLong();
        // 比较活动生效时间，失效时间
        if (currentDate < effectTime)
        { // 活动未开始
            return Types.FALSE;
        }
        if (currentDate > expriceTime)
        { // 活动已结束
            PresentBooksCacheService.getInstance().removePresentBookInfoByBookId(bookId);
            return Types.FALSE;
        }
        // 活动进行中
        return Types.TRUE;
    }
}
