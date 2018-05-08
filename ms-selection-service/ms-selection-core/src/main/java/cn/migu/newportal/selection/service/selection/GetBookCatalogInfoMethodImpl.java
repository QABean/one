package cn.migu.newportal.selection.service.selection;

import cn.migu.newportal.cache.util.CommonHttpUtil;
import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.migu.newportal.cache.bean.book.AuthorInfo;
import cn.migu.newportal.cache.bean.book.BookItem;
import cn.migu.newportal.cache.bean.book.PortalChapterInfo;
import cn.migu.newportal.cache.cache.service.PortalAuthorInfoCacheService;
import cn.migu.newportal.cache.cache.service.PortalBookCoverCacheService;
import cn.migu.newportal.cache.cache.service.PortalChapterInfoCacheService;
import cn.migu.newportal.cache.cache.service.PortalContentInfoCacheService;
import cn.migu.newportal.cache.cache.service.PortalContentInfoExCacheService;
import cn.migu.newportal.cache.util.JsonFormatUtil;
import cn.migu.newportal.cache.util.PropertiesConfig;
import cn.migu.newportal.selection.service.BookCatalogServiceImpl;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.constants.Types;
import cn.migu.newportal.util.date.DateTools;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode;
import cn.migu.newportal.util.tools.RequestCheckUtil;
import cn.migu.selection.proto.base.BookCatalogResponseOuterClass.BookCatalogData;
import cn.migu.selection.proto.base.BookCatalogResponseOuterClass.BookCatalogResponse;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;

/**
 * 图书详情页目录展示
 *
 * @author fengjiangtao
 * @version C10 2017年7月4日
 * @since SDP V300R003C10
 */
public class GetBookCatalogInfoMethodImpl extends ServiceMethodImpl<BookCatalogResponse, ComponentRequest>
{
    // 日志对象
    private static Logger logger = LoggerFactory.getLogger(GetBookCatalogInfoMethodImpl.class);
    
    private static final String METHOD_NAME = "getBookCatalogInfo";
    
    // 默认图书等级
    private static final String DEFAULT_BOOKLEVEL = "4";
    
    public GetBookCatalogInfoMethodImpl()
    {
        super(METHOD_NAME);
    }
    
    /**
     * 获取内容目录信息
     * 
     * @author hushanqing
     * @param controller
     * @param request
     * @return
     */
    @ImplementMethod
    public InvokeResult<BookCatalogResponse> getBookCatalogInfo(ServiceController controller, ComponentRequest request)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("enter getBookCatalogInfoMethodImpl.getBookCatalogInfo,identityId:{},requst:{}",
                CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(request));
        }
        
        BookCatalogResponse.Builder builder = BookCatalogResponse.newBuilder();
        builder.setPluginCode(request.getParamMapMap().get(ParamConstants.PLUGINCODE));
        
        BookCatalogData.Builder dataBuilder = BookCatalogData.newBuilder();
        // 获取请求参数
        String bid = request.getParamMapMap().get(ParamConstants.BID);
        // 设置通用及透传参数
        setCommonData(request, dataBuilder);
        // 设置内容目录数据
        builCatalogData(dataBuilder, bid, request, builder);
        
        if (logger.isDebugEnabled())
        {
            logger.debug("exit getBookCatalogInfoMethodImpl.getBookCatalogInfo,identityId:{} response :{} ",
                CommonHttpUtil.getIdentity(),JsonFormatUtil.printToString(builder));
        }
        
        return new InvokeResult<BookCatalogResponse>(builder.build());
    }
    
    /**
     * 设置通用数据
     * 
     * @author hushanqing
     * @param request
     * @param dataBuilder
     */
    private void setCommonData(ComponentRequest request, BookCatalogData.Builder dataBuilder)
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
     * 设置内容目录数据
     * 
     * @author hushanqing
     * @param dataBuilder
     * @param bid
     */
    private void builCatalogData(BookCatalogData.Builder dataBuilder, String bid, ComponentRequest request,
        BookCatalogResponse.Builder builder)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("enter getBookCatalogInfoMethodImpl.builCatalogData(),identityId:{} ,bookId:{}",
                CommonHttpUtil.getIdentity(),
                bid);
        }
        BookItem bookItem = PortalContentInfoCacheService.getInstance().getBookItem(bid);
        if (Util.isEmpty(bookItem) || StringTools.isEq(ParamConstants.BOOK_SHELF_STATUS, bookItem.getStatus()))
        {
            builder.setIsvisable(ParamConstants.NOT_VISABLE);
            builder.setStatus(MSResultCode.ErrorCodeAndDesc.BOOK_EMPTY_OR_SHELF.getCode());
            builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.BOOK_EMPTY_OR_SHELF.getDesc());
            logger.warn("book:{} not exist or off shelf!",bid);
            return;
        }
        String[] chapterIds = PortalContentInfoExCacheService.getInstance().getChapterIds(bid);
        String bookStatus = StringTools.isEmpty(bookItem.getIsFinish()) ? Types.TRUE : bookItem.getIsFinish();
        PortalChapterInfo chapterInfo = null;
        if (StringTools.isEq(Types.TRUE, bookStatus))
        {
            dataBuilder.setBookFinished(true);
            
            if (chapterIds != null && chapterIds.length > 0)
            {
                chapterInfo = PortalChapterInfoCacheService.getInstance().getChapterItem(bid, chapterIds[0]);
            }
        }
        else if (StringTools.isEq(Types.FALSE, bookStatus))
        {
            dataBuilder.setBookFinished(false);
            if (chapterIds != null && chapterIds.length > 0)
            {
                chapterInfo =
                    PortalChapterInfoCacheService.getInstance().getChapterItem(bid, chapterIds[chapterIds.length - 1]);
            }
        }
        
        if (Util.isNotEmpty(chapterInfo))
        {
            dataBuilder.setChapterName(StringTools.nvl(chapterInfo.getChapterName()));
        }
        
        String time = DateTools.timeTransform(bookItem.getLastSerialTime(),
            DateTools.DATE_FORMAT_14,
            DateTools.DATE_FORMAT_24HOUR_19);
        dataBuilder.setUpdateTime(StringTools.nvl(time));
        dataBuilder.setContentType(bookItem.getItemType());
        dataBuilder.setBookId(bid);
        dataBuilder.setBookName(bookItem.getBookName());
        dataBuilder.setBookCover(StringTools
            .nvl(PortalBookCoverCacheService.getInstance().getBookCover(bid, PropertiesConfig.getProperty("cdnUrl"))));
        AuthorInfo author = PortalAuthorInfoCacheService.getInstance().getAuthorInfo(bookItem.getAuthorId());
        if (Util.isNotEmpty(author))
        {
            dataBuilder.setAuthorName(StringTools.nvl(author.getAuthorPenName()));
        }
        
        dataBuilder.setChargeMode(
            StringTools.isEmpty(bookItem.getChargeMode()) ? Types.CHARGE_MODE_FREE : bookItem.getChargeMode());
        // 图书等级
        String bookLevel = StringTools.isEmpty(bookItem.getBookLevel()) ? DEFAULT_BOOKLEVEL : bookItem.getBookLevel();
        dataBuilder.setBookLevel(bookLevel);
        // 系统当前时间
        dataBuilder.setNowTime(DateTools.getCurrentDate(DateTools.DATE_FORMAT_24HOUR_19));
        
        // 判断是否ajax请求
        String isAjax = request.getParamMapMap().get(ParamConstants.ISAJAX);
        if (StringUtils.isNotEmpty(isAjax) && ParamConstants.IS_AJAX.equals(isAjax))
        {
            ComponentRequest.Builder paramMapBuilder = request.toBuilder();
            paramMapBuilder.removeParamMap(ParamConstants.ISAJAX);
            dataBuilder.putAllCtag(paramMapBuilder.getParamMapMap());
        }
        builder.setData(dataBuilder);
        builder.setIsvisable(ParamConstants.IS_VISABLE);
        builder.setStatus(MSResultCode.ErrorCodeAndDesc.SUCCESS.getCode());
        builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.SUCCESS.getDesc());

        if (logger.isDebugEnabled())
        {
            logger.debug("exit getBookCatalogInfoMethodImpl.builCatalogData(),identityId:{} ,builder:{}",
                CommonHttpUtil.getIdentity(),
                JsonFormatUtil.printToString(builder));
        }
    }
    
}
