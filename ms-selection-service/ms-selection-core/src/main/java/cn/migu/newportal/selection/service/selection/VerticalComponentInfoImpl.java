package cn.migu.newportal.selection.service.selection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.migu.newportal.cache.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;


import com.huawei.iread.server.constant.Types;

import cn.migu.compositeservice.contservice.Common.RankInfo;
import cn.migu.compositeservice.contservice.GetRank.GetRankResponse;
import cn.migu.newportal.cache.bean.book.AuthorInfo;
import cn.migu.newportal.cache.bean.book.BookItem;
import cn.migu.newportal.cache.bean.bookContent.Book;
import cn.migu.newportal.cache.bean.node.NodeItem;
import cn.migu.newportal.cache.cache.service.BookScoreDetailListCacheService;
import cn.migu.newportal.cache.cache.service.PortalAuthorInfoCacheService;
import cn.migu.newportal.cache.cache.service.PortalBookCoverCacheService;
import cn.migu.newportal.cache.cache.service.PortalContentInfoCacheService;
import cn.migu.newportal.cache.cache.service.PortalNodeInfoCacheService;
import cn.migu.newportal.cache.manager.node.NodeManager;
import cn.migu.newportal.selection.bean.LimitNodeItem;
import cn.migu.newportal.selection.bean.PagedContent;
import cn.migu.newportal.selection.engine.ContentServiceEngine;
import cn.migu.newportal.selection.manager.BookClassInfoCacheManager;
import cn.migu.newportal.selection.manager.BookManager;
import cn.migu.newportal.selection.util.BookDataUtil;
import cn.migu.newportal.selection.util.ContentUtil;
import cn.migu.newportal.selection.util.ParamUtil;
import cn.migu.newportal.selection.util.RankType;
import cn.migu.newportal.selection.util.UserUtils;
import cn.migu.newportal.util.bean.page.PaginTools;
import cn.migu.newportal.util.constants.DatasrcSourceScene;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.constants.SystemConstants;
import cn.migu.newportal.util.exception.PortalException;
import cn.migu.newportal.util.http.HttpUtil;
import cn.migu.newportal.util.http.UrlTools;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequestOrBuilder;
import cn.migu.selection.proto.base.VerticalComponentResponseOuterClass.BookVerticalData;
import cn.migu.selection.proto.base.VerticalComponentResponseOuterClass.VerticalBookes;
import cn.migu.selection.proto.base.VerticalComponentResponseOuterClass.VerticalComponentResponse;
import cn.migu.userservice.GetAvgScoreAndTotalPersons.BookMarkInfoResponse;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 纵向列表组件
 *
 * @author 王文娟
 * @version C10 2017年7月19日
 * @since SDP V300R003C10
 */
public class VerticalComponentInfoImpl extends ServiceMethodImpl<VerticalComponentResponse, ComponentRequest>
{
    // 日志对象
    private static final Logger LOG = LoggerFactory.getLogger(VerticalComponentInfoImpl.class);
    
    private static final String METHOD_NAME = "getVerticalComponentInfo";
    
    /** 点击榜排行类型 */
    protected final static String CLICKRANKTYPE = "2";
    
    /** 上架时间排行 */
    protected final static String ONSHELFRANKTYPE = "1";
    
    public VerticalComponentInfoImpl()
    {
        
        super(METHOD_NAME);
    }
    
    /**
     * 
     * 
     *
     * @param controller
     * @param request
     * @return
     */
    @ImplementMethod
    public InvokeResult<VerticalComponentResponse> getVerticalPagedCatlogBooks(ServiceController controller,
        ComponentRequest request)
    {
        if (LOG.isDebugEnabled())
        {
            LOG.debug("enter VerticalComponentInfoImpl.getVerticalPagedCatlogBooks ,identityId:{},requestParamMap :{}" ,
                CommonHttpUtil.getIdentity(), request.getParamMapMap());
        }
        VerticalComponentResponse.Builder builder = VerticalComponentResponse.newBuilder();
        // 设置组件名称
        builder.setPluginCode(request.getParamMapMap().get(ParamConstants.PLUGINCODE));
        // 获取专区id,pg要做的逻辑是，优先取组件配置nid，如果为空，取请求参数nid
        String nid = StringUtils.defaultString(request.getParamMapMap().get(ParamConstants.NID));
        if (StringTools.isEmpty(nid))
        {
            LOG.debug("Exit buildResponseBuilder nid is NUll");
            builder.setIsvisable(ParamConstants.NOT_VISABLE);
            builder.setStatus(MSResultCode.ERRORCODE_PARAMVALUE_IS_NULL);
            builder.setMessageDesc("nid" + MSResultCode.ERRORMSG_PARAMVALUE_IS_NULL);
            return new InvokeResult<VerticalComponentResponse>(builder.build());
        }
        else
        {
            // 根据专区id获取图书信息：
            NodeItem nodeItem = PortalNodeInfoCacheService.getInstance().getNodeItem(nid);
            if (nodeItem == null)
            {
                LOG.debug("exit  nodeItem is NUll");
                builder.setIsvisable(ParamConstants.NOT_VISABLE);
                builder.setStatus(MSResultCode.ErrorCodeAndDesc.NODE_NOT_EXIST.getCode());
                builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.NODE_NOT_EXIST.getDesc());
                return new InvokeResult<VerticalComponentResponse>(builder.build());
            }
        }
        
        // 页码
        String page = request.getParamMapMap().get(ParamConstants.PAGENO);
        if (NumberUtils.toInt(page, 1) < 1)
        {
            page = PropertiesConfig.getProperty("detailPage_bookVertical_default_pageNo", "1");
        }
        int pageNo = NumberUtils.toInt(page);
        
        // 每页展示数量
        String showPg = request.getParamMapMap().get(ParamConstants.SHOWPAGE);// 获取展示条数
        if (NumberUtils.toInt(showPg, -1) < 1)
        {
            showPg = PropertiesConfig.getProperty("detailPage_verticalComponentInfo_default_showPage", "5");
        }
        int showPage = NumberUtils.toInt(showPg);
        // 省ID
        String provinceId = UserUtils.getProvinceID(HttpUtil.getHeader(ParamConstants.X_Identity_ID));
        // 市ID
        String cityId = UserUtils.getCityID(HttpUtil.getHeader(ParamConstants.X_Identity_ID));
        // 尾标
        String endLogo = StringUtils.defaultString(request.getParamMapMap().get(ParamConstants.ENDLOG));
        String[] endLogoValue =
            new String[] {ParamConstants.ISONE_ENDLOGO, ParamConstants.ISTWO_ENDLOGO, ParamConstants.ISTHREE_ENDLOGO};
        endLogo = ParamUtil.checkParamter(endLogoValue, endLogo, ParamConstants.ISONE_ENDLOGO);
        // 点击量放大倍数
        String largeSize = StringUtils.defaultString(request.getParamMapMap().get(ParamConstants.LARGESIZE));
//        if (NumberUtils.toInt(largeSize, 1) < 0)
//        {
//            largeSize = PropertiesConfig.getProperty("detailPage_bookVertical_default_largeSize", "1");
//        }
//        if (largeSize.isEmpty())
//        {
//            largeSize = "1";
//        }
        // 展示类型
        String showType = StringUtils.defaultString(request.getParamMapMap().get(ParamConstants.NAMESHOWTYPE));
        // 角标类型
        String cornerShowType = StringUtils.defaultString(request.getParamMapMap().get(ParamConstants.CORNERSHOWTYPE));

        PagedContent<List<Book>> pagedContent = null;
        try
        {
            pagedContent = getPagedContent(request, builder, nid, pageNo, showPage, provinceId, cityId, showType);
        }
        catch (PortalException e)
        {
            LOG.error(
                " VerticalComponentInfoImpl.getPagedContent error,identityId:{}, nid:{},pageNo:{},showPage:{},showType:{},e:{}",
                CommonHttpUtil.getIdentity(),
                nid,
                pageNo,
                showPage,
                showType);
            setNotVisable(builder);
            return new InvokeResult<VerticalComponentResponse>(builder.build());
        }
        List<VerticalBookes> verticalBookes =
            buildVerticalBooks(endLogo, largeSize, cornerShowType, pagedContent, showType, request);
        // if (Util.isEmpty(verticalBookes))
        // {
        // VerticalBookes.Builder bookes = VerticalBookes.newBuilder();
        // verticalBookes.add(bookes.build());
        // }
        // 获取请求参数
        String isShowLine = StringUtils.defaultString(request.getParamMapMap().get(ParamConstants.ISSHOWLINE));
        String[] isShowLineValue = new String[] {ParamConstants.IS_ISSHOWLINE, ParamConstants.NOT_ISSHOWLINE};
        isShowLine = ParamUtil.checkParamter(isShowLineValue, isShowLine, ParamConstants.NOT_ISSHOWLINE);
        buildResponseBuilder(request, builder, verticalBookes, isShowLine);
        return new InvokeResult<VerticalComponentResponse>(builder.build());
    }
    
    /**
     * 
     * 
     *
     * @author
     * @param request
     * @param builder
     * @param verticalBookes
     * @param isShowLine
     */
    private void buildResponseBuilder(ComponentRequest request, VerticalComponentResponse.Builder builder,
        List<VerticalBookes> verticalBookes, String isShowLine)
    {
        BookVerticalData.Builder verticaldata = BookVerticalData.newBuilder();
        
        // 是否显示页面底部横
        if (StringTools.isNotEmpty(isShowLine))
        {
            verticaldata.setIsShowLine(isShowLine);
        }
        
        // 控制下边框：
        String isMarginBottom = request.getParamMapMap().get(ParamConstants.ISMARGINBOTTOM);
        String[] isMarginBottomValue = new String[] {ParamConstants.IS_MARGINBOTTOM, ParamConstants.NOT_MARGINBOTTOM};
        isMarginBottom = ParamUtil.checkParamter(isMarginBottomValue, isMarginBottom, ParamConstants.NOT_MARGINBOTTOM);
        
        String isPaddingTop = request.getParamMapMap().get(ParamConstants.ISPADDINGTOP);
        String[] isPaddingTopValue = new String[] {ParamConstants.IS_PADDINGTOP, ParamConstants.NOT_PADDINGTOP};
        isPaddingTop = ParamUtil.checkParamter(isPaddingTopValue, isPaddingTop, ParamConstants.NOT_PADDINGTOP);
        
        // 是否显示上划线：
        String isMarginTop = StringUtils.defaultString(request.getParamMapMap().get(ParamConstants.ISMARGINTOP));
        String[] isMarginTopValue = new String[] {ParamConstants.IS_MARGINTOP, ParamConstants.NOT_MARGINTOP};
        isMarginTop = ParamUtil.checkParamter(isMarginTopValue, isMarginTop, ParamConstants.NOT_MARGINTOP);
        
        verticaldata.setIsMarginTop(ParamUtil.checkResponse(isMarginTop));
        verticaldata.setIsMarginBottom(isMarginBottom);
        verticaldata.setIsPaddingTop(isPaddingTop);
        
        // 是否上拉加载
        String isLaLoding = StringUtils.defaultString(request.getParamMapMap().get(ParamConstants.ISLALODING));
        // 支持上拉加载即要ajax请求
        if (StringTools.isEq(ParamConstants.IS_AJAX, isLaLoding))
        {
            verticaldata.setIsAjax(ParamUtil.checkResponse(ParamConstants.IS_AJAX));
        }
        else
        {
            verticaldata.setIsAjax(ParamUtil.checkResponse(ParamConstants.NOT_AJAX));
        }
        
        // 每页展示数量
        String showPg = request.getParamMapMap().get(ParamConstants.SHOWPAGE);
        if (NumberUtils.toInt(showPg, -1) < 1)
        {
            showPg = PropertiesConfig.getProperty("detailPage_verticalComponentInfo_default_showPage", "5");
        }
        verticaldata.setShowPage(showPg);
        if (verticalBookes != null && verticalBookes.size() != 0)
        {
            verticaldata.addAllBooks(verticalBookes);
        }
        else
        {
            verticalBookes = new ArrayList<VerticalBookes>();
            // VerticalBookes.Builder verticalBook = VerticalBookes.newBuilder();
            // verticalBookes.add(verticalBook.build());
            verticaldata.addAllBooks(verticalBookes);
        }
        
        // 判断是否ajax请求
        if (StringTools.isNotEmpty(isLaLoding) && ParamConstants.IS_AJAX.equals(isLaLoding))
        {
            ComponentRequest.Builder paramMapBuilder = request.toBuilder();
            paramMapBuilder.removeParamMap(ParamConstants.ISAJAX);
            verticaldata.putAllCtag(paramMapBuilder.getParamMapMap());
            builder.setData(verticaldata);
        }
        else
        {
            builder.setData(verticaldata);
        }
        
        builder.setIsvisable(ParamConstants.IS_VISABLE);
        builder.setStatus(MSResultCode.ErrorCodeAndDesc.SUCCESS.getCode());
        builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.SUCCESS.getDesc());
        if (LOG.isDebugEnabled())
        {
            LOG.debug("exit VerticalComponentInfoImpl-buildResponse，identityId:{}, response :{}",
                CommonHttpUtil.getIdentity(),JsonFormatUtil.printToString(builder));
        }
    }
    
    /**
     * 
     * 
     *
     * @author
     * @param request
     * @param builder
     * @param nid
     * @param pageNo
     * @param showPage
     * @param provinceId
     * @param cityId
     * @param showType
     * @return
     * @throws PortalException
     */
    private PagedContent<List<Book>> getPagedContent(ComponentRequest request,
        VerticalComponentResponse.Builder builder, String nid, int pageNo, int showPage, String provinceId,
        String cityId, String showType)
        throws PortalException
    {
        // 排行榜类型，请求URL参数传到PG，然后由PG转发过来
        String rankType = getCurrRankType(request);
        // 如果是点击榜的则获取专区点击榜排序数据
        if (CLICKRANKTYPE.equals(rankType))
        {
            return getClickBookList(nid, String.valueOf(SystemConstants.SHOW_TYPE_BOOK_NAME), pageNo, showPage);
        }
        else
        {
            // 按上架时间排序
            return getPagedBooks(nid, showType, cityId, provinceId, pageNo, showPage);
        }
    }
    
    /**
     * 
     * 
     *
     * @author
     * @param endLogo
     * @param largeSize
     * @param cornerShowType
     * @param pagedContent
     * @return
     */
    private List<VerticalBookes> buildVerticalBooks(String endLogo, String largeSize, String cornerShowType,
        PagedContent<List<Book>> pagedContent, String showType, ComponentRequest request)
    {
        List<VerticalBookes> verticalBookes = null;
        
        if (pagedContent != null && !Util.isEmpty(pagedContent.getContent()))
        {
            verticalBookes = new ArrayList<>();
            for (Book book : pagedContent.getContent())
            {
                if (null != book)
                {
                    VerticalBookes.Builder verticalbuild = VerticalBookes.newBuilder();
                    verticalbuild = getBookInfo(largeSize,
                        book.getBookId(),
                        cornerShowType,
                        endLogo,
                        showType,
                        request);
                    verticalBookes.add(verticalbuild.build());
                }
            }
            
        }
        return verticalBookes;
    }
    
    /**
     * 设置组件不可见代码
     *
     *
     * @author
     * @param builder
     */
    private void setNotVisable(VerticalComponentResponse.Builder builder)
    {
        if (null != builder)
        {
            builder.setIsvisable(ParamConstants.NOT_VISABLE);
            builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.CONNECTION_FAILED.getDesc());
            builder.setStatus(MSResultCode.ErrorCodeAndDesc.CONNECTION_FAILED.getCode());
        }
    }

    /**
     * 
     * 
     *
     * @param bookId
     * @param cornerShowType
     * @param endLogo
     * @return
     */
    private VerticalBookes.Builder getBookInfo(String largeSize, String bookId, String cornerShowType, String endLogo,
        String showType, ComponentRequest request)
    {
        
        VerticalBookes.Builder verticalbuild = VerticalBookes.newBuilder();
        // 获取图书信息：
        BookItem bookItem = PortalContentInfoCacheService.getInstance().getBookItem(bookId);
        
        if (bookItem == null)
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("book:{} not exist", bookId);
            }
            return null;
        }

        // 通用方法获取 根据nameShowType参数，bookItem
        String bookShowName = BookDataUtil.getShowName(bookId, showType);
        verticalbuild.setBookShowName(ParamUtil.checkResponse(bookShowName));
        // authorid判空
        // 获取作者信息：
        AuthorInfo authorInfo = PortalAuthorInfoCacheService.getInstance().getAuthorInfo(bookItem.getAuthorId());
        
        if (authorInfo != null)
        {
            verticalbuild
                .setAuthorName(StringUtils.defaultString(ParamUtil.checkResponse(authorInfo.getAuthorPenName())));
        }
        // 添加点击量
        verticalbuild.setBookClickDesc(BookDataUtil.getInstance().getClickNumDesc(bookId, largeSize));
        // 获取角标
        String conce = StringUtils.defaultString(BookDataUtil.getConer(bookItem, cornerShowType));
        verticalbuild.setCornerShowType(conce);
        verticalbuild
            .setBookShortDesc(StringUtils.defaultString(ParamUtil.checkResponse(bookItem.getShortDescription())));
        String bookCover = PortalBookCoverCacheService.getInstance().getBookCover(bookId,
            PropertiesConfig.getProperty(ParamConstants.CDNURL));
        verticalbuild.setBookCoverLogo(bookCover);
        
        Map<String, String> param = UesServiceUtils.buildPublicParaMap(null, null);
        param.put(ParamConstants.BID, bookId);
        param.put(ParamConstants.NID, request.getParamMapMap().get(ParamConstants.NID));
        String bookUrl = PresetPageUtils.getBookDetailPage(param);
        verticalbuild.setBookDetailUrl(StringTools.nvl(bookUrl));
        // 尾标（直接从请求参数获取）
        verticalbuild.setEndLogo(endLogo);
        BookMarkInfoResponse bookMarkInfoResponse =
            BookScoreDetailListCacheService.getInstance().getBookScoreDetailList(bookItem.getBookId());
        // 设置图书平均平分
        if (Util.isNotEmpty(bookMarkInfoResponse))
        {
            verticalbuild.setBookScore(bookMarkInfoResponse.getAvgScore());
        }
        else
        {
            verticalbuild.setBookScore(" ");
        }
        
        NodeItem nodeItem = getCateName(bookItem);
        if (nodeItem==null) {
			return verticalbuild;
		}
        String cateNode = nodeItem.getName();
        // 设置图书分类名称
        verticalbuild.setCateNode(StringTools.nvl(cateNode));
        
        // 到对应图书分类的专区详情页
        String cateNodeUrl = NodeManager.getNodeUrl(nodeItem.getId());
        
        Map<String, String> paramMap =  new HashMap<>();
        
        paramMap.put(ParamConstants.NODENAME, cateNode);
        cateNodeUrl = UrlTools.processForView(UrlTools.getRelativelyUrl(cateNodeUrl, UesServiceUtils.DEFAULT_DOMAIN), paramMap);
        
        verticalbuild.setCateNodeUrl(StringTools.nvl(cateNodeUrl));
        return verticalbuild;
    }
    
    /**
     * 
     * 获取图书分类名，其实是获取分类整合专区的专区名称 TODO 缺少UES专区列表里面设置的专区别名，后续开发专区列表组件时补上
     * 
     * @author zhaoxinwei
     * @param bookItem
     * @return
     */
    private NodeItem getCateName(BookItem bookItem)
    {
        if (null == bookItem)
        {
            return null;
        }
        String classId = bookItem.getBookClassId();
        if (StringTools.isEmpty(classId)) {
			return null;
		}
        cn.migu.newportal.cache.bean.book.BookClassInfo bookClassInfo =
            BookClassInfoCacheManager.getInstance().getBookClassInfo(classId);
        if (null == bookClassInfo)
        {
            return null;
        }
        String uniteId = bookClassInfo.getBookUniteClassId();
        if (StringUtils.isEmpty(uniteId))
        {
            uniteId = bookClassInfo.getDefaultNodeId();
        }
        NodeItem nodeItem = PortalNodeInfoCacheService.getInstance().getNodeItem(uniteId);
        return nodeItem;
    }
    
    /**
     * 获取图书点击榜数据
     * 
     * @param nodeId 专区Id
     * @param showType 显示方式
     * @param pageNo 当前页码
     * @param pageSize 每页显示记录数
     * @return [参数说明]
     * @return PagedContent<List<Book>> [返回类型说明]
     * @throws PortalException
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public PagedContent<List<Book>> getClickBookList(String nodeId, String showType, int pageNo, int pageSize)
        throws PortalException
    {
        // 分页调用getRank接口
        List<String> bookList = new ArrayList<String>();
        GetRankResponse rankResponse = ContentServiceEngine.getInstance().getRank(nodeId,
            RankType.RANK_STANDARD_CLICK,
            RankType.RANK_TYPE_TOTAL,
            pageSize,
            pageNo);
        if (Util.isEmpty(bookList))
        {
            return null;
        }
        
        List<RankInfo> infoList = rankResponse.getRankListList();
        if (Util.isEmpty(infoList))
        {
            return null;
        }
        for (int i = 0, len = infoList.size(); i < len; i++)
        {
            if (Util.isEmpty(infoList.get(i)))
            {
                continue;
            }
            bookList.add(infoList.get(i).getBookId());
        }
        
        String[] bookIds = bookList.toArray(new String[bookList.size()]);
        
        String[] pagedBookIds = PaginTools.getPagedList(bookIds, pageNo, pageSize);
        
        List<Book> bookInfoList = new ArrayList<Book>();
        for (String bid : pagedBookIds)
        {
            Book book = new Book();
            book.setBookId(bid);
            bookInfoList.add(book);
        }
        
        PagedContent<List<Book>> pagedBooks = new PagedContent<List<Book>>(pageSize, pageNo, bookIds.length);
        pagedBooks.setContent(bookInfoList);
        return pagedBooks;
    }
    
    /**
     * 获取当前排行榜类型
     * 
     * @return [参数说明]
     * @return String [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static String getCurrRankType(ComponentRequestOrBuilder request)
    {
        String currRankType = request.getParamMapMap().get("rankType");
        if (CLICKRANKTYPE.equals(currRankType))
        {
            return currRankType;
        }
        else
        {
            return ONSHELFRANKTYPE;
        }
    }
    
    /**
     * 分页查询专区下的内容列表
     * 
     * @param nodeId
     * @param pageNo
     * @param pageSize
     * @return
     * @see [类、类#方法、类#成员]
     */
    public PagedContent<List<Book>> getPagedBooks(String nodeId, String showType, String cityId, String provinceId,
        int pageNo, int pageSize)
    {
        // 从缓存中获取专区信息
        NodeItem nodeItem = PortalNodeInfoCacheService.getInstance().getNodeItem(nodeId);
        if (null == nodeItem)
        {
            return null;
        }
        
        String[] bookIds = nodeItem.getBookIDs();
        if (null == bookIds || 0 == bookIds.length)
        {
            return null;
        }
        int totalCnt = bookIds.length;
        int requestCnt = pageNo * pageSize;
        LimitNodeItem books = new LimitNodeItem();
        
        cityId = StringTools.isEmpty(cityId) ? Types.COUNTRY : cityId;
        provinceId = StringTools.isEmpty(provinceId) ? SystemConstants.CHINA_ID : provinceId;
        
        // 包月收费模式   
        if (null != nodeItem.getProductInfo()
            && StringTools.isEq(SystemConstants.CHARGE_MODE_MONTHLY, nodeItem.getProductInfo().getChargeMode()))
        {
            books = BookManager.filterCityAndProvinceBookIds(nodeItem, cityId, provinceId);
            bookIds = books.getBookIds();
            totalCnt = books.getTotalCnt();
            if (null == bookIds || 0 == bookIds.length)
            {
                return null;
            }
        }
        boolean hasPaged = false;
        // 如果用户请求的个数没有超过缓存的图书个数, 直接返回原图书列表
        if (requestCnt >= bookIds.length)
        {
            bookIds = BookManager.overflowProcess(pageNo,
                pageSize,
                nodeId,
                books.getAdaptCityId(),
                books.getAdaptProvinceId());
            hasPaged = true;
        }
        return getPageBookList(nodeId,
            bookIds,
            showType,
            pageNo,
            pageSize,
            DatasrcSourceScene.fromNode,
            totalCnt,
            hasPaged);
    }
    
    /**
     * 分页图书数据源
     * 
     * @param bookIds
     * @param pageNo
     * @param pageSize
     * @return
     * @see [类、类#方法、类#成员]
     */
    public PagedContent<List<Book>> getPageBookList(String nodeId, String[] bookIds, String showType, int pageNo,
        int pageSize, String DSCreateSource, int totalCnt, boolean hasPaged)
    {
        
        String[] pagedBookIds = null;
        // 是否已经分过页了。
        if (hasPaged)
        {
            pagedBookIds = bookIds;
        }
        else
        {
            pagedBookIds = PaginTools.getPagedList(bookIds, pageNo, pageSize);
        }
        if (null == pagedBookIds)
        {
            pagedBookIds = new String[] {};
        }
        List<Book> bookList = new ArrayList<Book>();
        for (String bid : pagedBookIds)
        {
            Book book = new Book();
            book.setBookId(bid);
            bookList.add(book);
        }
        
        PagedContent<List<Book>> pagedBooks = new PagedContent<List<Book>>(pageSize, pageNo, totalCnt);
        pagedBooks.setContent(bookList);
        return pagedBooks;
    }
    
}
