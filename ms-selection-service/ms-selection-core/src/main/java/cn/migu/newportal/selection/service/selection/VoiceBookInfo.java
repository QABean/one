package cn.migu.newportal.selection.service.selection;

import cn.migu.newportal.cache.bean.book.AuthorInfo;
import cn.migu.newportal.cache.bean.book.BookItem;
import cn.migu.newportal.cache.bean.book.PortalChapterInfo;
import cn.migu.newportal.cache.bean.book.PortalContentExItem;
import cn.migu.newportal.cache.bean.node.NodeItem;
import cn.migu.newportal.cache.cache.service.*;
import cn.migu.newportal.cache.util.*;
import cn.migu.newportal.selection.bean.LimitNodeItem;
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
public class VoiceBookInfo extends ServiceMethodImpl<VoiceBookResponse, ComponentRequest>
{

    // 日志对象

    private static Logger logger = LoggerFactory.getLogger(VoiceBookInfo.class.getName());

    private static final String METHOD_NAME = "VoiceBookService";

    /** 书库轮循和精品轮循标签不轮循排序方式 2-图书上节点时间 */
    public static final String BOOK_ON_NODE = "2";

    public VoiceBookInfo()
    {
        super(METHOD_NAME);
    }

    /**
     * 听书业务逻辑
     *
     * @author zhangmm
     * @param controller
     * @param request
     * @return
     */
    @ImplementMethod
    public InvokeResult<VoiceBookResponse> getVoiceBook(ServiceController controller, ComponentRequest request)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug( "enter VoiceBookInfo-getVoiceBook，identityId:{} ComponentRequest :{}"
                , CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(request));
        }
        String pluginCode = request.getParamMapMap().get(ParamConstants.PLUGINCODE);
        pluginCode = ParamUtil.getPluginCode(pluginCode);

        // 数据来源，（1.自配，2.专区）
        String dataSrc = request.getParamMapMap().get(ParamConstants.DATASRC);
        dataSrc = ParamUtil.checkDataSrc(dataSrc);
        // 是否播放最新章节（1.播放第一章节，2.播放最新章节,）
        String isplayNew = request.getParamMapMap().get(ParamConstants.ISPLAYNEW);
        isplayNew = ParamUtil.checkShowType(isplayNew);

        // 角标展示（1:免费;2:限免;3:会员;4:完本;5:名家;6:上传）,可多选
        String cornerShowType = request.getParamMapMap().get(ParamConstants.CORNERSHOWTYPE);

        // 展示样式（1:书名+点击量;2:书名+主播，默认1）
        String showType = request.getParamMapMap().get(ParamConstants.SHOWTYPE);
        showType = ParamUtil.checkShowType(showType);
        // 图书UV放大倍数
        String largeSize = request.getParamMapMap().get(ParamConstants.LARGESIZE);

        // 专区下图书列表显示数
        String showNum = request.getParamMapMap().get(ParamConstants.SHOWNUM);

        // 图书名称显示类型:1：图书书名,2：长推荐语,4：短推荐语,5：WAP2.0推荐语,6：适配推荐语
        String nameShowType = request.getParamMapMap().get(ParamConstants.NAMESHOWTYPE);

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
        // 专区id
        String nid = request.getParamMapMap().get(ParamConstants.NID);
        // 获取显示个数
        int num = ParamUtil.getShowNum(showNum, ParamConstants.NODESHOWNUM);
        VoiceBookResponse.Builder builder = VoiceBookResponse.newBuilder();
        BooksData.Builder booksData = BooksData.newBuilder();
        builder.setPluginCode(request.getParamMapMap().get(ParamConstants.PLUGINCODE));
        List<VoiceBook> booksList = new ArrayList<>();
        List<BookItem> bookItems = new ArrayList<>();
        // 自配模式
        if (ParamConstants.NUMONE.equals(dataSrc))
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("enter VoiceBookInfo.getVoiceBook ,identityId:{} dataSrc = {}",
                    CommonHttpUtil.getIdentity(),
                    dataSrc);
            }
            bookItems.add(PortalContentInfoCacheService.getInstance().getBookItem(bookId1));
            bookItems.add(PortalContentInfoCacheService.getInstance().getBookItem(bookId2));
            bookItems.add(PortalContentInfoCacheService.getInstance().getBookItem(bookId3));
            // 封装展示状态
            ProtoUtil.buildCommonData(booksData, request.getParamMapMap());
            if (bookItems != null)
            {
                for (BookItem bookItem : bookItems)
                {
                    bookEvaluate(
                        bookItem,
                        cornerShowType,
                        nameShowType,
                        isplayNew,
                        largeSize,
                        dataSrc,
                        params,
                        request,
                        booksList);
                }
                booksData.addAllBooks(booksList);
            }
            booksData.setShowStyleType(showType);
            builder.setData(booksData.build());
            // 设置成功返回码
            ProtoUtil.buildResultCode(builder, ErrorCodeAndDesc.SUCCESS, ParamConstants.IS_VISABLE);
            return new InvokeResult<VoiceBookResponse>(builder.build());
        }

        // 专区模式
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
        if (Util.isNotEmpty(bookIds) && num > bookIds.length && totalCnt > 0 && bookIds.length != totalCnt)
        {
            bookIds = BookManager.overflowProcess(1, num, nid, cityId, provinceId);
        }
        if (Util.isEmpty(bookIds))
        {
            logger.error("Exit VoiceBookInfo.getVoiceBook,bookIds is null,identityId:{}",CommonHttpUtil.getIdentity());
            ProtoUtil.buildResultCode(builder, ErrorCodeAndDesc.NODE_NOT_EXIST, ParamConstants.NOT_VISABLE);
            return new InvokeResult<VoiceBookResponse>(builder.build());
        }
        for(String bookid:bookIds)
        {
            if(StringTools.isEmpty(bookid))
            {
                continue;
            }
            BookItem bookItem = PortalContentInfoCacheService.getInstance().getBookItem(bookid);
            bookEvaluate(bookItem,
                cornerShowType,
                nameShowType,
                isplayNew,
                largeSize,
                dataSrc,
                params,
                request,
                booksList);
            if(booksList.size() == num)
            {
                break;
            }
        }
        booksData.addAllBooks(booksList);
        booksData.setShowStyleType(showType);
        builder.setData(booksData.build());
        // 设置成功返回码
        ProtoUtil.buildResultCode(builder, ErrorCodeAndDesc.SUCCESS, ParamConstants.IS_VISABLE);
        // 封装展示状态
        ProtoUtil.buildCommonData(booksData, request.getParamMapMap());
        if (logger.isDebugEnabled())
        {
            logger.debug("Exit VoiceBookInfo.getVoiceBook ,identityId:{},response:{}",
                CommonHttpUtil.getIdentity(),
                JsonFormatUtil.printToString(builder));
        }
        return new InvokeResult<VoiceBookResponse>(builder.build());

    }

    /**
     * 对返回参数进行封装
     *
     *
     * @author zhagnmm
     * @param booksList
     * @param bookItem
     * @param cornerShowType
     * @param nameShowType
     * @param isplayNew
     * @param largeSize
     * @param dataSrc
     * @param param
     */
    private void bookEvaluate(BookItem bookItem , String cornerShowType,
        String nameShowType, String isplayNew, String largeSize, String dataSrc,
        Map<String, String> param, ComponentRequest request, List<VoiceBook> booksList)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("Enter VoiceBookInfo.bookEvaluate");
        }
        if (Util.isEmpty(bookItem))
        {
            if (logger.isErrorEnabled())
            {
                logger.error("book:{} not exist,identityId:{}", CommonHttpUtil.getIdentity());
            }
            return;
        }
        VoiceBook.Builder books = VoiceBook.newBuilder();
        books.setBookShowName(BookDataUtil.getShowName(bookItem.getBookId(), nameShowType));
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
        Map<String, String> paramMap = UesServiceUtils.buildPublicParaMap(null, null);
        paramMap.put(ParamConstants.BID, bookItem.getBookId());
        paramMap.put(ParamConstants.NID, request.getParamMapMap().get(ParamConstants.NID));

        // 拼接代码详情页
        Map<String, String> buildPublicParam = UesServiceUtils.buildPublicParaMap(null, null);
        buildPublicParam.put(ParamConstants.BID, bookItem.getBookId());
        buildPublicParam.put(ParamConstants.NID, request.getParamMapMap().get(ParamConstants.NID));
        // 图书详情页地址
        String detailUrl = PresetPageUtils.getBookDetailPage(buildPublicParam);
        books.setBookDetailUrl(StringTools.nvl(detailUrl));
        books.setChargeMode(ParamUtil.checkResponse(bookItem.getChargeMode()));
        books.setBookLevel(bookItem.getBookLevel());
        // 设置图书点击量
        String click = BookDataUtil.getInstance().getClickNumDesc(bookItem.getBookId(), largeSize);
        books.setBookClickDesc(ParamUtil.checkResponse(click));
        PortalContentExItem contentExItem =
            PortalContentInfoExCacheService.getInstance().getContentExItem(bookItem.getBookId());
        if (StringTools.isEq(isplayNew, ParamConstants.NUMONE))
        {
            if (Util.isNotEmpty(contentExItem))
            {
                books.setChapterId(ParamUtil.checkResponse(contentExItem.getFirstChapterId()));
            }
        }
        else if (StringTools.isEq(isplayNew, ParamConstants.NUMTWO))
        {
            if (Util.isNotEmpty(contentExItem))
            {
                books.setChapterId(ParamUtil.checkResponse(contentExItem.getLatestChapterId()));
            }
        }
        books.setBookId(ParamUtil.checkResponse(bookItem.getBookId()));
        // 角标获取
        String cornerScene = BookDataUtil.getConer(bookItem, cornerShowType);
        books.setCornerShowType(ParamUtil.checkResponse(cornerScene));
        // 获取主播信息
        Speaker speakerInfo = new Speaker(bookItem.getReaderid(), bookItem.getReaderName());
        if (speakerInfo != null)
        {
            books.setSpeakerName(ParamUtil.checkResponse(speakerInfo.getName()));
            if(Util.isEmpty(speakerInfo.getName()))
            {
                books.setSpeakerName("佚名");
            }
        }
        if (bookItem.getProductInfo() != null)
        {
            books.setContentType(ParamUtil.checkResponse(bookItem.getProductInfo().getContentType()));
        }
        books.setOffset(SystemConstants.offset);
        books.setRecentlyTime(" ");
        books.setIsCompare("false");

        // 获取作者信息
        AuthorInfo authorInfo = PortalAuthorInfoCacheService.getInstance().getAuthorInfo(bookItem.getAuthorId());
        if (!Util.isEmpty(authorInfo))
        {
            books.setAuthorName(ParamUtil.checkResponse(authorInfo.getAuthorName()));
            if(Util.isEmpty(Util.isEmpty(authorInfo.getAuthorName())))
            {
                books.setAuthorName("佚名");
            }
        }
        PortalChapterInfo chapterInfo = null;
        if (StringTools.isEq(isplayNew, ParamConstants.NUMONE))
        {
            if(Util.isNotEmpty(contentExItem))
            {
                chapterInfo = PortalChapterInfoCacheService.getInstance()
                        .getChapterItem(bookItem.getBookId(), contentExItem.getFirstChapterId());
            }
        }
        else if (StringTools.isEq(isplayNew, ParamConstants.NUMTWO))
        {
            if (Util.isNotEmpty(contentExItem))
            {
                chapterInfo = PortalChapterInfoCacheService.getInstance().getChapterItem(bookItem.getBookId(),
                    contentExItem.getLatestChapterId());
            }
        }
        if (Util.isNotEmpty(chapterInfo))
        {
            books.setChapterName(ParamUtil.checkResponse(chapterInfo.getChapterName()));
        }
        books.setIsDownload(ParamUtil.checkResponse(bookItem.getCanDownload()));
        books.setItemType(ParamUtil.checkResponse(bookItem.getItemType()));
        booksList.add(books.build());
        
        if (logger.isDebugEnabled())
        {
            logger.info("Exit VoiceBookInfo.bookEvaluate , ");
        }
    }
}
