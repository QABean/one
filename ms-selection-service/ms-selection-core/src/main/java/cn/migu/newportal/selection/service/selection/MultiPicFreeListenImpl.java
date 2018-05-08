package cn.migu.newportal.selection.service.selection;

import cn.migu.compositeservice.contservice.BookmarkInfoOuterClass.BookmarkInfo;
import cn.migu.newportal.cache.bean.book.AuthorInfo;
import cn.migu.newportal.cache.bean.book.BookItem;
import cn.migu.newportal.cache.bean.book.PortalChapterInfo;
import cn.migu.newportal.cache.bean.book.PortalContentExItem;
import cn.migu.newportal.cache.bean.booksheet.BookMarkBean;
import cn.migu.newportal.cache.bean.node.NodeItem;
import cn.migu.newportal.cache.business.user.UserManager;
import cn.migu.newportal.cache.cache.service.*;
import cn.migu.newportal.cache.util.*;
import cn.migu.newportal.selection.bean.LimitNodeItem;
import cn.migu.newportal.selection.bean.local.request.MultiPicFreeListenRequest;
import cn.migu.newportal.selection.engine.Speaker;
import cn.migu.newportal.selection.manager.BookManager;
import cn.migu.newportal.selection.util.BookDataUtil;
import cn.migu.newportal.selection.util.ParamUtil;
import cn.migu.newportal.selection.util.UserUtils;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.constants.SystemConstants;
import cn.migu.newportal.util.http.HttpUtil;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode.ErrorCodeAndDesc;
import cn.migu.newportal.util.tools.ProtoUtil;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.VoiceBookResponseOuterClass.BooksData;
import cn.migu.selection.proto.base.VoiceBookResponseOuterClass.VoiceBook;
import cn.migu.selection.proto.base.VoiceBookResponseOuterClass.VoiceBookResponse;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 听书
 *
 * @author zhangmm
 * @version C10 2017年7月5日
 * @since SDP V300R003C10
 */
public class MultiPicFreeListenImpl extends ServiceMethodImpl<VoiceBookResponse, ComponentRequest>
{

    // 日志对象

    private static Logger logger = LoggerFactory.getLogger(MultiPicFreeListenImpl.class);

    private static final String METHOD_NAME = "multiPicFreeListen";

    private final static String ISSHELF = "1";

    public MultiPicFreeListenImpl()
    {
        super(METHOD_NAME);
    }

    /**
     * 听书业务逻辑
     *
     * @param controller
     * @param request
     * @return
     * @author zhangmm
     */
    @ImplementMethod
    public InvokeResult<VoiceBookResponse> multiPicFreeListen(ServiceController controller, ComponentRequest request)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("enter MultiPicFreeListenImpl-multiPicFreeListen，identityId:{} ComponentRequest :{}"
                , CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(request));
        }

        MultiPicFreeListenRequest req = new MultiPicFreeListenRequest(request.getParamMapMap());

        // 省ID
        String provinId = UserUtils.getProvinceID(HttpUtil.getHeader(ParamConstants.X_Identity_ID));
        // 市ID
        String citId = UserUtils.getCityID(HttpUtil.getHeader(ParamConstants.X_Identity_ID));

        String bookId1 = request.getParamMapMap().get(ParamConstants.BOOKID1);
        String bookId2 = request.getParamMapMap().get(ParamConstants.BOOKID2);
        String bookId3 = request.getParamMapMap().get(ParamConstants.BOOKID3);

        String coverlogo1 = request.getParamMapMap().get(ParamConstants.COVERLOGO1);
        String coverlogo2 = request.getParamMapMap().get(ParamConstants.COVERLOGO2);
        String coverlogo3 = request.getParamMapMap().get(ParamConstants.COVERLOGO3);
        Map<String, String> params = new HashMap<String, String>();
        params.put(bookId1, coverlogo1);
        params.put(bookId2, coverlogo2);
        params.put(bookId3, coverlogo3);

        // 获取显示个数
        int showNum = ParamUtil.getShowNum(req.getShowNum(), ParamConstants.NODESHOWNUM);
        VoiceBookResponse.Builder builder = VoiceBookResponse.newBuilder();
        BooksData.Builder booksData = BooksData.newBuilder();
        // 组件code
        builder.setPluginCode(req.getPluginCode());
        // 封装展示状态
        ProtoUtil.buildCommonData(booksData,request.getParamMapMap());
        List<VoiceBook> bookList = new ArrayList<>();
        List<BookItem> bookItems = new ArrayList<>();

        String dataSrc = req.getDataSrc();
        // 自配模式
        if (ParamConstants.NUMONE.equals(dataSrc))
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("enter MultiPicFreeListenImpl-multiPicFreeListen ,identityId:{} dataSrc = {}",
                    CommonHttpUtil.getIdentity(),
                    dataSrc);
            }
            bookItems.add(PortalContentInfoCacheService.getInstance().getBookItem(bookId1));
            bookItems.add(PortalContentInfoCacheService.getInstance().getBookItem(bookId2));
            bookItems.add(PortalContentInfoCacheService.getInstance().getBookItem(bookId3));
            for (BookItem bookItem : bookItems)
            {
                VoiceBook.Builder voiceBookbuilder = bookEvaluate(
                    bookItem,
                    req,
                    dataSrc,
                    params,
                    request);
                bookList.add(voiceBookbuilder.build());
            }
            booksData.addAllBooks(bookList);
            booksData.setShowStyleType(req.getShowType());
            builder.setData(booksData);
            // 设置成功返回码
            ProtoUtil.buildResultCode(builder, ErrorCodeAndDesc.SUCCESS, ParamConstants.IS_VISABLE);
            if (logger.isDebugEnabled())
            {
                logger.debug(
                    "Exit MultiPicFreeListenImpl-multiPicFreeListen success , identityId:{}, Response:{}",
                    CommonHttpUtil.getIdentity(),
                    JsonFormatUtil.printToString(builder.build()));
            }
            return new InvokeResult<VoiceBookResponse>(builder.build());
        }

        // 专区模式  专区id
        String nid = req.getNid();
        NodeItem nodeItems = PortalNodeInfoCacheService.getInstance().getNodeItem(nid);
        String provinceId = UserUtils.getProvinceID(provinId);
        String cityId = UserUtils.getCityID(citId);
        String[] bookIds = null;
        int totalCnt = 0;
        if (Util.isNotEmpty(nodeItems) && Util.isNotEmpty(nodeItems.getBookIDs()))
        {
            bookIds = nodeItems.getBookIDs();
            totalCnt = bookIds.length;
        }
        // 包月收费模式
        if (Util.isNotEmpty(nodeItems) && Util.isNotEmpty(nodeItems.getProductInfo())
            && StringTools.isEq(SystemConstants.CHARGE_MODE_MONTHLY, nodeItems.getProductInfo().getChargeMode()))
        {
            LimitNodeItem limitNodeItem = BookManager.filterCityAndProvinceBookIds(nodeItems, cityId, provinceId);
            totalCnt = limitNodeItem.getTotalCnt();
            bookIds = limitNodeItem.getBookIds();
        }
        // 如果用户请求的个数超过缓存的图书个数,直接调getPagedBooksWithPart返回原图书列表
        if (Util.isNotEmpty(bookIds) && showNum > bookIds.length && totalCnt > 0 && bookIds.length != totalCnt)
        {
            bookIds = BookManager.overflowProcess(1, showNum, nid, cityId, provinceId);
        }
        if (Util.isEmpty(bookIds))
        {
            logger.error("Exit VoiceBookInfo.getVoiceBook,bookIds is null,identityId:{}", CommonHttpUtil.getIdentity());
            ProtoUtil.buildResultCode(builder, ErrorCodeAndDesc.NODE_NOT_EXIST, ParamConstants.NOT_VISABLE);
            return new InvokeResult<VoiceBookResponse>(builder.build());
        }
        for (String bookid : bookIds)
        {
            if (StringTools.isEmpty(bookid))
            {
                continue;
            }
            BookItem bookItem = PortalContentInfoCacheService.getInstance().getBookItem(bookid);
            if (bookList.size() == showNum)
            {
                break;
            }
            VoiceBook.Builder voiceBookbuilder = bookEvaluate(bookItem,
                req,
                dataSrc,
                params,
                request);
            bookList.add(voiceBookbuilder.build());
        }
        booksData.addAllBooks(bookList);
        booksData.setShowStyleType(req.getShowType());
        builder.setData(booksData);
        // 设置成功返回码
        ProtoUtil.buildResultCode(builder, ErrorCodeAndDesc.SUCCESS, ParamConstants.IS_VISABLE);
        if (logger.isDebugEnabled())
        {
            logger.debug("Exit MultiPicFreeListenImpl-multiPicFreeListen ,identityId:{},response:{}",
                CommonHttpUtil.getIdentity(),
                JsonFormatUtil.printToString(builder));
        }
        return new InvokeResult<VoiceBookResponse>(builder.build());

    }

    /**
     * 对返回参数进行封装
     *
     * @param bookItem
     * @param dataSrc
     * @param param
     * @author zhagnmm
     */
    private VoiceBook.Builder bookEvaluate(BookItem bookItem, MultiPicFreeListenRequest req, String dataSrc,
        Map<String, String> param, ComponentRequest request)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("Enter VoiceBookInfo.bookEvaluate");
        }
        if (Util.isEmpty(bookItem))
        {
            logger.error("book:{} not exist,identityId:{}", CommonHttpUtil.getIdentity());
            return null;
        }
        VoiceBook.Builder books = VoiceBook.newBuilder();
        books.setBookShowName(BookDataUtil.getShowName(bookItem, req.getNameShowType()));
        books.setBookDesc(ParamUtil.checkResponse(bookItem.getShortDescription()));
        // 获取封面信息
        String bookCover = PortalBookCoverCacheService.getInstance().getBookCover(bookItem.getBookId(),
            PropertiesConfig.getProperty("cdnUrl"));
        if (StringTools.isEq(dataSrc, ParamConstants.NUMONE))
        {
            books.setBookCoverLogo(ParamUtil.checkResponse(param.get(bookItem.getBookId())));
        }
        else
        {
            books.setBookCoverLogo(ParamUtil.checkResponse(bookCover));
        }
        // 拼接图书详情页
        Map<String, String> buildPublicParam = UesServiceUtils.buildPublicParaMap(null, null);
        buildPublicParam.put(ParamConstants.BID, bookItem.getBookId());
        buildPublicParam.put(ParamConstants.NID, request.getParamMapMap().get(ParamConstants.NID));
        // 图书详情页地址
        String detailUrl = PresetPageUtils.getBookDetailPage(buildPublicParam);
        books.setBookDetailUrl(StringTools.nvl(detailUrl));
        books.setChargeMode(ParamUtil.checkResponse(bookItem.getChargeMode()));
        books.setBookLevel(bookItem.getBookLevel());
        // 设置图书点击量
        String click = BookDataUtil.getInstance().getClickNumDesc(bookItem.getBookId(), req.getLargeSize());
        books.setBookClickDesc(ParamUtil.checkResponse(click));
        PortalContentExItem contentExItem =
            PortalContentInfoExCacheService.getInstance().getContentExItem(bookItem.getBookId());
        // 获取相对章首的偏移量(offset)和chapterId
        BookmarkInfo bookmark = getBookmark(bookItem);
        if (StringTools.isEq(req.getStartFrom(), ParamConstants.NUMONE))
        {
            if (Util.isNotEmpty(contentExItem))
            {
                books.setChapterId(ParamUtil.checkResponse(contentExItem.getFirstChapterId()));
            }
        }
        else if (StringTools.isEq(req.getStartFrom(), ParamConstants.NUMTWO))
        {
            if (Util.isNotEmpty(contentExItem))
            {
                books.setChapterId(ParamUtil.checkResponse(contentExItem.getLatestChapterId()));
            }
        }
        // 用户书签 设置chapterId
        else if (StringTools.isEq(req.getStartFrom(), ParamConstants.NUMTHREE))
        {
            // 设置第一章节
            PortalChapterInfo chapterInfo = PortalChapterInfoCacheService.getInstance().getFirstChapter(bookItem);
            if (Util.isEmpty(bookmark) && Util.isNotEmpty(chapterInfo))
            {
                books.setChapterId(Util.nvl(chapterInfo.getChapterId()));
            }
            else
            {
                books.setChapterId(Util.nvl(bookmark.getChapterId()));
            }
            books.setIsCompare("true");
        }

        // 设置偏移量offset
        // 游客设置offset为0
        if (UserManager.isGuestUser() || Util.isEmpty(bookmark))
        {
            books.setOffset(SystemConstants.offset);
        }
        else
        {
            books.setOffset(StringTools.nvl2(bookmark.getPosition(), SystemConstants.offset));
        }

        books.setBookId(ParamUtil.checkResponse(bookItem.getBookId()));
        // 角标获取
        String cornerScene = BookDataUtil.getConer(bookItem, req.getCornerShowType());
        books.setCornerShowType(ParamUtil.checkResponse(cornerScene));
        // 获取主播信息
        Speaker speakerInfo = new Speaker(bookItem.getReaderid(), bookItem.getReaderName());
        if (speakerInfo != null)
        {
            books.setSpeakerName(ParamUtil.checkResponse(speakerInfo.getName()));
        }
        if (bookItem.getProductInfo() != null)
        {
            books.setContentType(ParamUtil.checkResponse(bookItem.getProductInfo().getContentType()));
        }

        books.setRecentlyTime(" ");

        // 获取作者信息
        AuthorInfo authorInfo = PortalAuthorInfoCacheService.getInstance().getAuthorInfo(bookItem.getAuthorId());
        if (!Util.isEmpty(authorInfo))
        {
            books.setAuthorName(ParamUtil.checkResponse(authorInfo.getAuthorName()));
        }
        PortalChapterInfo chapterInfo = null;
        if (StringTools.isEq(req.getStartFrom(), ParamConstants.NUMONE))
        {
            if (Util.isNotEmpty(contentExItem))
            {
                chapterInfo = PortalChapterInfoCacheService.getInstance()
                    .getChapterItem(bookItem.getBookId(), contentExItem.getFirstChapterId());
            }
        }
        else if (StringTools.isEq(req.getStartFrom(), ParamConstants.NUMTWO))
        {
            if (Util.isNotEmpty(contentExItem))
            {
                chapterInfo = PortalChapterInfoCacheService.getInstance().getChapterItem(bookItem.getBookId(),
                    contentExItem.getLatestChapterId());
            }
        }
        else if (StringTools.isEq(req.getStartFrom(), ParamConstants.NUMTHREE))
        {
            if (Util.isNotEmpty(bookItem))
            {
                chapterInfo = PortalChapterInfoCacheService.getInstance().getChapterItem(bookItem.getBookId(),
                    books.getChapterId());
            }
        }
        if (Util.isNotEmpty(chapterInfo))
        {
            books.setChapterName(ParamUtil.checkResponse(chapterInfo.getChapterName()));
        }

        books.setIsDownload(ParamUtil.checkResponse(bookItem.getCanDownload()));
        books.setItemType(ParamUtil.checkResponse(bookItem.getItemType()));
        return books;
    }

    /**
     * 封装相对首章的阅读偏移量及chapterId
     *
     * @param bookItem @return
     * @author zhangmm
     */
    private BookmarkInfo getBookmark(BookItem bookItem)
    {
        BookMarkBean markBean = new BookMarkBean();
        markBean.setMsisdn(CommonHttpUtil.getIdentity());
        markBean.setBookId(bookItem.getBookId());
        markBean.setBookMarkType(SystemConstants.IS_SYSTEM_BOOKMARK);
        markBean.setContentType("0");
        markBean.setIsShelf(ISSHELF);// 1 上架图书SSS

        List<BookmarkInfo> bookmarkInfoList =
            PortalBookMarkInfoCacheService.getInstance().getSystemMarkListProtoType(markBean);

        BookmarkInfo bookmark1 = null;
        if (!Util.isEmpty(bookmarkInfoList))
        {
            for (BookmarkInfo bookmark : bookmarkInfoList)
            {
                if (bookmark != null && StringTools.isEq(bookmark.getBookId(), bookItem.getBookId()))
                {
                    bookmark1 = bookmark;
                    break;
                }
            }
        }
        return bookmark1;
    }
}
