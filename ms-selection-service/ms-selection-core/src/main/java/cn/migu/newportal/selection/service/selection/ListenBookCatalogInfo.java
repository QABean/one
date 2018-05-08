package cn.migu.newportal.selection.service.selection;

import java.util.ArrayList;
import java.util.List;

import cn.migu.newportal.cache.util.CommonHttpUtil;
import cn.migu.newportal.cache.util.JsonFormatUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.ArrayUtils;

import cn.migu.newportal.cache.bean.book.AuthorInfo;
import cn.migu.newportal.cache.bean.book.BookItem;
import cn.migu.newportal.cache.bean.book.PortalChapterInfo;
import cn.migu.newportal.cache.cache.service.PortalAuthorInfoCacheService;
import cn.migu.newportal.cache.cache.service.PortalBookCoverCacheService;
import cn.migu.newportal.cache.cache.service.PortalChapterInfoCacheService;
import cn.migu.newportal.cache.cache.service.PortalContentInfoCacheService;
import cn.migu.newportal.cache.cache.service.PortalContentInfoExCacheService;
import cn.migu.newportal.cache.util.PropertiesConfig;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.date.DateTools;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.ListenBookCatalogResponseOuterClass.ListenBookCatalogData;
import cn.migu.selection.proto.base.ListenBookCatalogResponseOuterClass.ListenBookCatalogResponse;
import cn.migu.selection.proto.base.ListenBookCatalogResponseOuterClass.ListenChapter;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 听书详情页目录展示
 *
 * @author fengjiangtao
 * @version C10 2017年7月4日
 * @since SDP V300R003C10
 */
public class ListenBookCatalogInfo extends ServiceMethodImpl<ListenBookCatalogResponse, ComponentRequest>
{
    // 日志对象
    private static Logger logger = LoggerFactory.getLogger(ListenBookCatalogInfo.class.getName());
    
    private static final String METHOD_NAME = "getListenBookCatalogInfo";
    
    // 完本图书显示的章节行数
    private static final String SHOWLINENUM = "showLineNum";
    
    // 完本图书默认展示的章节行数的key值
    private static final String LDP_BC_D_N = "listenDetailPage_bookCatalog_datault_num";
    
    // 听书的图书标识符为5
    private static final String LISTEN_BOOK_CONTENTTYPE = "5";
    
    public ListenBookCatalogInfo()
    {
        super(METHOD_NAME);
    }
    
    @ImplementMethod
    public InvokeResult<ListenBookCatalogResponse> v1(ServiceController controller, ComponentRequest request)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug( "enter ListenBookCatalogInfo-v1，identityId:{} ComponentRequest :{}"
                , CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(request));
        }
        
        ListenBookCatalogResponse.Builder builder = ListenBookCatalogResponse.newBuilder();
        builder.setPluginCode(StringTools.nvl(request.getParamMapMap().get(ParamConstants.PLUGINCODE)));
        builder.setIsvisable(ParamConstants.NOT_VISABLE);
        builder.setStatus(MSResultCode.ErrorCodeAndDesc.CONNECTION_FAILED.getCode());
        builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.CONNECTION_FAILED.getDesc());
        
        try
        {
            // 获取请求参数bid
            String bid = request.getParamMapMap().get(ParamConstants.BID);
            // 图书id为空，返回错误码BOOK_ID_EMPTY
            if (StringTools.isEmpty(bid))
            {
                builder.setStatus(MSResultCode.ErrorCodeAndDesc.BOOK_ID_EMPTY.getCode());
                builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.BOOK_ID_EMPTY.getDesc());
                if (logger.isDebugEnabled())
                {
                    logger.debug("exit ListenBookCatalogInfo.v1,bookId is empty.identityId:{},response={}",
                        CommonHttpUtil.getIdentity(),
                        JsonFormatUtil.printToString(builder));
                }
                return new InvokeResult<ListenBookCatalogResponse>(builder.build());
            }
            int showLineNum = NumberUtils.toInt(request.getParamMapMap().get(SHOWLINENUM), -1);
            if (showLineNum < 0)
                showLineNum = PropertiesConfig.getInt(LDP_BC_D_N, 3);
            // 排序传入的不为2就默认为1
            int sortType = NumberUtils.toInt(request.getParamMapMap().get(ParamConstants.SORTTYPE), 1) != 2 ? 1 : 2;
            ListenBookCatalogData.Builder dataBuilder = ListenBookCatalogData.newBuilder();
            dataBuilder.setTotalChapterNum(ParamConstants.TOTALCHAPTERNUM);
            BookItem bookItem = PortalContentInfoCacheService.getInstance().getBookItem(bid);
            // 图书为空或者图书已下架，返回错误码
            if (bookItem == null || ParamConstants.BOOK_SHELF_STATUS.equals(bookItem.getStatus())
                || !LISTEN_BOOK_CONTENTTYPE.equals(bookItem.getItemType()))
            {
                builder.setStatus(MSResultCode.ErrorCodeAndDesc.BOOK_EMPTY_OR_SHELF.getCode());
                builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.BOOK_EMPTY_OR_SHELF.getDesc());
                if (logger.isDebugEnabled())
                {
                    logger.debug("exit ListenBookCatalogInfo.v1,bookItem is empty or off-shelf.identityId:{},response:{}",
                        CommonHttpUtil.getIdentity(),
                        JsonFormatUtil.printToString(builder));
                }
                return new InvokeResult<ListenBookCatalogResponse>(builder.build());
            }
            String[] chapterIds = (String[])PortalContentInfoExCacheService.getInstance().getChapterIds(bid);
            if (ArrayUtils.isNotEmpty(chapterIds))
            {
                PortalChapterInfo chapterInfo = null;
                ListenChapter.Builder lcBuilder = ListenChapter.newBuilder();
                List<ListenChapter> chapters = new ArrayList<>();
                dataBuilder.setTotalChapterNum(String.valueOf(chapterIds.length));
                int minNum = chapterIds.length > showLineNum ? showLineNum : chapterIds.length;
                String bookStatus =
                    StringUtils.isBlank(bookItem.getIsFinish()) ? ParamConstants.YESNUM : bookItem.getIsFinish();
                if (ParamConstants.YESNUM.equals(bookStatus))
                {
                    if (sortType == 1)
                    {
                        for (int i = 0; i < minNum; i++)
                        {
                            chapterInfo =
                                PortalChapterInfoCacheService.getInstance().getChapterItem(bid, chapterIds[i]);
                            lcBuilder.setChapterId(StringTools.nvl(chapterInfo.getChapterId()));
                            lcBuilder.setChapterName(StringTools.nvl(chapterInfo.getChapterName()));
                            lcBuilder.setFeeStatus(StringTools.nvl(chapterInfo.getFeeStatus()));
                            chapters.add(lcBuilder.build());
                        }
                    }
                    else
                    {
                        for (int i = chapterIds.length - 1; i >= chapterIds.length - minNum; i--)
                        {
                            chapterInfo =
                                PortalChapterInfoCacheService.getInstance().getChapterItem(bid, chapterIds[i]);
                            lcBuilder.setChapterId(StringTools.nvl(chapterInfo.getChapterId()));
                            lcBuilder.setChapterName(StringTools.nvl(chapterInfo.getChapterName()));
                            lcBuilder.setFeeStatus(StringTools.nvl(chapterInfo.getFeeStatus()));
                            chapters.add(lcBuilder.build());
                        }
                    }
                    dataBuilder.setIsFinish(ParamConstants.ISFINISH_ONE);
                    dataBuilder.setSortType(String.valueOf(sortType));
                }
                else
                {
                    dataBuilder.setIsFinish(ParamConstants.ISFINISH_ZERO);
                    if (chapterIds != null && chapterIds.length > 0)
                    {
                        chapterInfo = PortalChapterInfoCacheService.getInstance().getChapterItem(bid,
                            chapterIds[chapterIds.length - 1]);
                        lcBuilder.setChapterId(StringTools.nvl(chapterInfo.getChapterId()));
                        lcBuilder.setChapterName(StringTools.nvl(chapterInfo.getChapterName()));
                        lcBuilder.setFeeStatus(StringTools.nvl(chapterInfo.getFeeStatus()));
                        chapters.add(lcBuilder.build());
                    }
                    String time = DateTools.timeTransform(bookItem.getLastSerialTime(),
                        DateTools.DATE_FORMAT_14,
                        DateTools.DATE_FORMAT_10);
                    dataBuilder.setUpdateTime(StringTools.nvl(time));
                }
                
                dataBuilder.addAllChapters(chapters);
                String isShowLine = request.getParamMapMap().get(ParamConstants.ISSHOWLINE);
                String isMarginTop = request.getParamMapMap().get(ParamConstants.ISMARGINTOP);
                String isMarginBottom = request.getParamMapMap().get(ParamConstants.ISMARGINBOTTOM);
                String isPaddingTop = request.getParamMapMap().get(ParamConstants.ISPADDINGTOP);
                // 是否显示页面底部横线
                dataBuilder.setIsShowLine(
                    StringTools.isEq(isShowLine, ParamConstants.TRUE) ? ParamConstants.TRUE : ParamConstants.FALSE);
                
                // 是否显示页面顶部边框
                dataBuilder.setIsMarginTop(
                    StringTools.isEq(isMarginTop, ParamConstants.TRUE) ? ParamConstants.TRUE : ParamConstants.FALSE);
                
                // 是否显示页面底部边框
                dataBuilder.setIsMarginBottom(
                    StringTools.isEq(isMarginBottom, ParamConstants.TRUE) ? ParamConstants.TRUE : ParamConstants.FALSE);
                
                // 是否显示内边框
                dataBuilder.setIsPaddingTop(
                    StringTools.isEq(isPaddingTop, ParamConstants.TRUE) ? ParamConstants.TRUE : ParamConstants.FALSE);
                dataBuilder.setContentType(StringTools.nvl(bookItem.getItemType()));
                dataBuilder.setBookId(bid);
                dataBuilder.setBookName(StringTools.nvl(bookItem.getBookName()));
                dataBuilder.setBookCover(StringTools.nvl(PortalBookCoverCacheService.getInstance().getBookCover(bid,
                    PropertiesConfig.getProperty("cdnUrl"))));
                AuthorInfo author = PortalAuthorInfoCacheService.getInstance().getAuthorInfo(bookItem.getAuthorId());
                if (author != null)
                {
                    dataBuilder.setAuthorName(StringTools.nvl(author.getAuthorPenName()));
                }
                dataBuilder.setChargeMode(StringTools.nvl(bookItem.getChargeMode()));
                dataBuilder.setBookLevel(StringTools.nvl(bookItem.getBookLevel()));
                // 判断是否ajax请求
                String isAjax = request.getParamMapMap().get(ParamConstants.ISAJAX);
                if (StringUtils.isNotEmpty(isAjax) && ParamConstants.YESNUM.equals(isAjax))
                {
                    ComponentRequest.Builder paramMapBuilder = request.toBuilder();
                    paramMapBuilder.removeParamMap(ParamConstants.ISAJAX);
                    dataBuilder.putAllCtag(paramMapBuilder.getParamMapMap());
                }
                // 设置组件可见
                builder.setData(dataBuilder);
                builder.setStatus(MSResultCode.ErrorCodeAndDesc.SUCCESS.getCode());
                builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.SUCCESS.getDesc());
                builder.setIsvisable(ParamConstants.IS_VISABLE);
            }
        }
        catch (Exception e)
        {
            logger.error("ListenBookCatalogInfo method has error,identityId:{}, request:{},e:{}",
                CommonHttpUtil.getIdentity(),
                JsonFormatUtil.printToString(request),
                e);
        }
        
        if (logger.isDebugEnabled())
        {
            logger.debug("exit ListenBookCatalogInfo.v1,identityId:{},response={}",
                CommonHttpUtil.getIdentity(),
                JsonFormatUtil.printToString(builder));
        }
        return new InvokeResult<ListenBookCatalogResponse>(builder.build());
    }
    
}
