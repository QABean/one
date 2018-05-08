package cn.migu.newportal.selection.service.selection;

import cn.migu.newportal.cache.bean.RecommendContentBean;
import cn.migu.newportal.cache.bean.SpeakerInfo;
import cn.migu.newportal.cache.bean.book.BookItem;
import cn.migu.newportal.cache.bean.bookContent.Book;
import cn.migu.newportal.cache.cache.service.*;
import cn.migu.newportal.cache.util.*;
import cn.migu.newportal.selection.bean.local.request.ContentRecommendInfoRequest;
import cn.migu.newportal.util.bean.page.PaginTools;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode.ErrorCodeAndDesc;
import cn.migu.newportal.util.tools.PortalUtils;
import cn.migu.newportal.util.tools.ProtoUtil;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.ContentRecommendResponseOuterClass.BiData;
import cn.migu.selection.proto.base.ContentRecommendResponseOuterClass.BookList;
import cn.migu.selection.proto.base.ContentRecommendResponseOuterClass.ContentRecommendData;
import cn.migu.selection.proto.base.ContentRecommendResponseOuterClass.ContentRecommendResponse;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 同类推荐
 * @author xfhe bookList_same
 */
public class ContentRecommendInfo extends ServiceMethodImpl<ContentRecommendResponse, ComponentRequest>
{
    
    // 日志对象
    private static Logger logger = LoggerFactory.getLogger(ContentRecommendInfo.class);
    
    private static final String METHOD_NAME = "getContentRecommendInfo";
    
    private static final String ONE = "1"; // UES读过还读
    
    private static final String TWO = "2";// 同系列推荐
    
    private static final String THREE = "3";// UES买过还买过
    
    private static final String FOUR = "4";// 表示UES同作者
    
    private static final String FIVE = "5";// 图书阅读还阅读关联推荐
    
    private static final String SIX = "6";// 图书订购还订购关联推荐
    
    private static final String SEVEN = "7";// 图书浏览还浏览关联推荐
    
    private static final String EIGHT = "8";// 听书详情页同类推荐
    
    private static final String PRTYPEEIGHT = "8";
    
    private static final String PRTYPENINE = "9";
    
    private static final String PRTYPETEN = "10";
    
    public ContentRecommendInfo()
    {
        super(METHOD_NAME);
        
    }
    
    /**
     * 
     * @author hexingfei
     * @description: 同类推荐
     * @param controller
     * @param request
     * @return
     */
    @ImplementMethod
    public InvokeResult<ContentRecommendResponse> getContentRecommendInfo(ServiceController controller,
        ComponentRequest request)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug( "enter ContentRecommendInfo-getContentRecommendInfo，identityId:{} ComponentRequest :{}"
                , CommonHttpUtil.getIdentity(),JsonFormatUtil.printToString(request));
        }
        ContentRecommendInfoRequest requestBean = new ContentRecommendInfoRequest(request.getParamMapMap());

        ContentRecommendResponse.Builder builder = ContentRecommendResponse.newBuilder();
        ContentRecommendData.Builder builderData = ContentRecommendData.newBuilder();

        builder.setPluginCode(StringTools.nvl(requestBean.getPluginCode()));

        //展示样式: 默认:3;枚举值:1:一行书名+读过人数;2:一行书名+作者;3:两行书名
        builderData.setStyle(StringTools.nvl(requestBean.getType()));
        //标题内容，默认：同类推荐
        String title = requestBean.getTitle();
        builderData.setTitle(StringTools.nvl(title));
        // 设置样式
        BeanMergeUtils.setComponentStyle(builderData, request.getParamMapMap());
        String bookId = requestBean.getBid();
        if (StringUtils.isEmpty(bookId))
        {// 如果书本信息没有则直接返回
            logger.error("ContentRecommendInfo.getContentRecommendInfo bid:{} is empty,identityId:{}",
                bookId,
                CommonHttpUtil.getIdentity());
            ProtoUtil.buildResultCode(builder, ErrorCodeAndDesc.BOOK_ID_EMPTY, ParamConstants.NOT_VISABLE);
            return new InvokeResult<ContentRecommendResponse>(builder.build());
        }

        String dataFrom = requestBean.getDataFrom();
        if (StringUtils.isEmpty(dataFrom))
        {
            logger.error("ContentRecommendInfo.getContentRecommendInfo dataFrom:{} is empty,identityId:{}",
                dataFrom,
                CommonHttpUtil.getIdentity());
            ProtoUtil.buildResultCode(builder, ErrorCodeAndDesc.PARAMNAME_EROOR, ParamConstants.NOT_VISABLE);
            return new InvokeResult<ContentRecommendResponse>(builder.build());
        }
        builderData.setDataFrom(StringTools.nvl(dataFrom));

        BookItem bookItem = PortalContentInfoCacheService.getInstance().getBookItem(bookId);
        // 图书不存在
        if (Util.isEmpty(bookItem))
        {
            logger.error(
                "ContentRecommendInfo.getContentRecommendInfo, bookItem is empty, bid={}, identityId={}",
                bookId,
                CommonHttpUtil.getIdentity());
            ProtoUtil.buildResultCode(builder, ErrorCodeAndDesc.BOOK_EMPTY_OR_SHELF, ParamConstants.NOT_VISABLE);
            return new InvokeResult<ContentRecommendResponse>(builder.build());
        }

        String authorId = bookItem.getAuthorId();

        //页码
        String pageNo = StringTools.nvl(RequestParamCheckUtils.paginationParamCheck(requestBean.getPageNo(), 1));
        //每页显示条数
        String pageSize = StringTools.nvl(RequestParamCheckUtils.paginationParamCheck(requestBean.getPageSize(), 10));

        RecommendContentBean contentBean = new RecommendContentBean();// 构造请求参数
        contentBean.setChannelId(CommonHttpUtil.getChannalID());
        contentBean.setBookId(bookId);
        contentBean.setAuthorId(authorId);
        contentBean.setStart(pageNo);
        contentBean.setNum(pageSize);
        contentBean.setPortalType(ParamConstants.PORTAL_TYPE_WAP);
        contentBean.setRanDateType(requestBean.getRankDateType());
        contentBean.setRecommendGrade(requestBean.getRecommendLevel());
        contentBean.setRankType(requestBean.getRankStandard());
        contentBean.setRecommendType(dataFrom);
        contentBean.setCount(pageSize);

        List<BookList> bookListList = new ArrayList<>();
        switch (dataFrom)
        {
            case ONE:// UES读过还读
            case TWO:// 同系列推荐
            case THREE:// UES买过还买过
                bookListList = getBookInfoFromCache(contentBean, bookItem);
                break;
            case FOUR:// 表示UES同作者
                if (StringUtils.isEmpty(authorId))
                {// 如果作者信息没有则直接返回
                    logger.error("ContentRecommendInfo.getContentRecommendInfo authorId :{} is empty,identityId:{}",
                        authorId,
                        CommonHttpUtil.getIdentity());
                    ProtoUtil.buildResultCode(builder, ErrorCodeAndDesc.AUTHORID_IS_EMPTY, ParamConstants.NOT_VISABLE);
                    return new InvokeResult<ContentRecommendResponse>(builder.build());
                }
                bookListList = getBookInfoFromCache(contentBean, bookItem);
                break;
            case FIVE:// 图书阅读还阅读关联推荐
            case SIX:// 图书订购还订购关联推荐
            case SEVEN: // 图书浏览还浏览关联推荐
                getBookInfoFromBI(requestBean, pageNo, pageSize, bookId, builderData);
                break;
            case EIGHT:// 听书详情页同类推荐
                bookListList = transfromBookInfo(bookId, pageNo, pageSize);
                break;
        }
        
        builderData.addAllBookList(bookListList);

        // 封装公共参数isShowLine，isMarginTop，isMarginBottom，isPaddingTop
        ProtoUtil.buildCommonData(builderData, request.getParamMapMap());
        // 设置返回数据是否可见、状态码、描述
        ProtoUtil.buildResultCode(builder, ErrorCodeAndDesc.SUCCESS, ParamConstants.IS_VISABLE);
        builder.setData(builderData);

        if (logger.isDebugEnabled())
        {
            logger.debug( "Exit ContentRecommendInfo-getContentRecommendInfo，identityId:{} response :{}"
                , CommonHttpUtil.getIdentity(),JsonFormatUtil.printToString(builder));
        }
        return new InvokeResult<ContentRecommendResponse>(builder.build());
    }
    
    /**
     * 同类推荐调用缓存数据
     * 
     * @param contentBean
     * @return
     */
    private List<BookList> getBookInfoFromCache( RecommendContentBean contentBean, BookItem bookItem)
    {
        List<Book> recommendMap = PortalContentRecommendCacheService.getInstance().getRecommendContentInfo(contentBean, bookItem);
        List<BookList> bookListLis = transfromBookInfo(recommendMap);
        
        return bookListLis;
    }
    
    /**
     * 转换
     * 
     * @param recommendMap
     * @return
     */
    private List<BookList> transfromBookInfo(List<Book> recommendMap)
    {
        List<BookList> bookListLis = new ArrayList<>();
        if (recommendMap != null && recommendMap.size() > 0)
        {
            String cdnUrl = PropertiesConfig.getProperty("cdnUrl");
            Map<String, String> bookDetailMap = new HashMap<>();
            bookDetailMap.put(ParamConstants.NID, "");
            for (Book book : recommendMap)
            {
                if (book == null)
                {
                    continue;
                }
                
                BookList.Builder booklist = BookList.newBuilder();
                String authoId = book.getAuthorId();
                cn.migu.newportal.cache.bean.book.AuthorInfo authorInfo =
                    PortalAuthorInfoCacheService.getInstance().getAuthorInfo(authoId);
                if (authorInfo != null)
                {
                    booklist.setAuthorName(StringTools.nvl(authorInfo.getAuthorName()));
                }
                String bookCover = PortalBookCoverCacheService.getInstance().getBookCover(book.getBookId(), cdnUrl);
                booklist.setBookCover(StringTools.nvl(bookCover));
                booklist.setBookName(StringTools.nvl(book.getBookName()));
                bookDetailMap.put(ParamConstants.BID, book.getBookId());
                booklist.setBookUrl(PresetPageUtils.getBookDetailPage(bookDetailMap));
                booklist.setContentType(StringTools.nvl(book.getContentType()));
                booklist.setCoPercent(getCoPrecent(book.getSmartRecommendValue()));
                bookListLis.add(booklist.build());
            }
        }
        
        return bookListLis;
    }
    
    /**
     * 转换 听书详情页同类推荐
     * 
     * @param bookId
     * @return
     */
    private List<BookList> transfromBookInfo(String bookId, String pageNo, String pageSize)
    {
        BookItem bookItem = PortalContentInfoCacheService.getInstance().getBookItem(bookId);
        SpeakerInfo speakerInfo = null;
        if (Util.isNotEmpty(bookItem))
        {
            speakerInfo = SpeakerInfoCacheService.getInstance().getSpeakerInfo(bookItem.getReaderid());
        }
        
        List<BookList> bookListLis = new ArrayList<>();
        if (Util.isNotEmpty(speakerInfo))
        {
            String[] bookIds = speakerInfo.getBookIds();
            String[] bookIdsPage =
                PaginTools.getPagedList(bookIds, Integer.parseInt(pageNo), Integer.parseInt(pageSize));
            String cdnUrl = PropertiesConfig.getProperty("cdnUrl");
            Map<String, String> bookDetailMap = new HashMap<>();
            bookDetailMap.put(ParamConstants.NID, "");
            for (int i = 0; i < bookIdsPage.length; i++)
            {
                if (Util.isEmpty(bookIdsPage[i]))
                {
                    continue;
                }
                BookItem speakerBookItem = PortalContentInfoCacheService.getInstance().getBookItem(bookIdsPage[i]);
                if (Util.isEmpty(speakerBookItem))
                {
                    continue;
                }
                BookList.Builder booklist = BookList.newBuilder();
                String authoId = speakerBookItem.getAuthorId();
                cn.migu.newportal.cache.bean.book.AuthorInfo authorInfo =
                    PortalAuthorInfoCacheService.getInstance().getAuthorInfo(authoId);
                booklist.setAuthorName(ParamConstants.DEFAULTAUTHORNAME);
                if (authorInfo != null)
                {
                    booklist.setAuthorName(StringTools.nvl(authorInfo.getAuthorPenName()));
                }
                String bookCover = PortalBookCoverCacheService.getInstance().getBookCover(bookIdsPage[i], cdnUrl);
                booklist.setBookCover(StringTools.nvl(bookCover));
                booklist.setBookName(StringTools.nvl(speakerBookItem.getBookName()));
                bookDetailMap.put(ParamConstants.BID, bookIdsPage[i]);
                booklist.setBookUrl(StringTools.nvl(PresetPageUtils.getBookDetailPage(bookDetailMap)));
                if (Util.isNotEmpty(speakerBookItem.getProductInfo()))
                {
                    booklist.setContentType(StringTools.nvl(speakerBookItem.getProductInfo().getContentType()));
                }

                bookListLis.add(booklist.build());
            }
        }
        
        return bookListLis;
    }
    
    /**
     * BI数据接口
     * 
     * @param requestBean
     */
    private void getBookInfoFromBI(ContentRecommendInfoRequest requestBean, String pageNo, String pageSize, String bid,
        ContentRecommendData.Builder recommendData)
    {
        BiData.Builder biData = BiData.newBuilder();
        biData.setMsisdn(StringTools.nvl(CommonHttpUtil.getIdentity()));
        biData.setBid(StringTools.nvl(bid));
        biData.setPageNo(StringTools.nvl(pageNo));
        biData.setShowNum(StringTools.nvl(pageSize));
        String clientVersion = PortalUtils.getUAVersion();
        //客户端版本号;请求头client_version中获取，默认"android"
        clientVersion = StringTools.isEmpty(clientVersion) ? "android" : clientVersion;
        biData.setClientVersion(StringTools.nvl(clientVersion));
        biData.setInstanceId(StringTools.nvl(requestBean.getInstanceId()));

        recommendData.setBiData(biData);
    }

    private String getCoPrecent(String smartRecommendValue)
    {
        if (StringTools.isEmpty(smartRecommendValue))
        {
            smartRecommendValue = (Util.getRandom(50) + 20) + "%";
        }
        smartRecommendValue = smartRecommendValue.split("\\.")[0] + "%";
        return smartRecommendValue;
    }
}
