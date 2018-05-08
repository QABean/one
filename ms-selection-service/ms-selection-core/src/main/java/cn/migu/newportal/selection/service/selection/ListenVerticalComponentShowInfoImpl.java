package cn.migu.newportal.selection.service.selection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import cn.migu.newportal.cache.bean.book.AuthorInfo;
import cn.migu.newportal.cache.bean.book.BookItem;
import cn.migu.newportal.cache.bean.book.PortalContentExItem;
import cn.migu.newportal.cache.bean.bookContent.Book;
import cn.migu.newportal.cache.bean.node.NodeItem;
import cn.migu.newportal.cache.cache.service.PortalAuthorInfoCacheService;
import cn.migu.newportal.cache.cache.service.PortalBookCoverCacheService;
import cn.migu.newportal.cache.cache.service.PortalContentInfoCacheService;
import cn.migu.newportal.cache.cache.service.PortalContentInfoExCacheService;
import cn.migu.newportal.cache.cache.service.PortalNodeInfoCacheService;
import cn.migu.newportal.cache.cache.service.PortalPostInfoCacheService;
import cn.migu.newportal.cache.util.CommonHttpUtil;
import cn.migu.newportal.cache.util.JsonFormatUtil;
import cn.migu.newportal.cache.util.PropertiesConfig;
import cn.migu.newportal.cache.util.UesServiceUtils;
import cn.migu.newportal.selection.bean.PagedContent;
import cn.migu.newportal.selection.bean.local.request.ListenVerticalRequest;
import cn.migu.newportal.selection.engine.Speaker;
import cn.migu.newportal.selection.manager.BookManager;
import cn.migu.newportal.selection.util.BookDataUtil;
import cn.migu.newportal.selection.util.ParamUtil;
import cn.migu.newportal.selection.util.SelectionConstants;
import cn.migu.newportal.util.cache.util.PortalConfig;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.constants.WapConst;
import cn.migu.newportal.util.exception.PortalException;
import cn.migu.newportal.util.http.UrlTools;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.ListenVerticalComponentResponseOuterClass.ListenVerticalBooks;
import cn.migu.selection.proto.base.ListenVerticalComponentResponseOuterClass.ListenVerticalComponentResponse;
import cn.migu.selection.proto.base.ListenVerticalComponentResponseOuterClass.ListenVerticalData;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 听书纵向内容
 *
 * @author yejiaxu
 * @version C10 2017年7月17日
 * @since SDP V300R003C10
 */
public class ListenVerticalComponentShowInfoImpl
    extends ServiceMethodImpl<ListenVerticalComponentResponse, ComponentRequest>
{
    
    // 日志对象
    private static final Logger LOG = LoggerFactory.getLogger(ListenVerticalComponentShowInfoImpl.class);
    
    private static final String METHOD_NAME = "getListenVerticalComponentShowInfo";
    
    public ListenVerticalComponentShowInfoImpl()
    {
        super(METHOD_NAME);
    }
    
    /**
     * 听书纵向内容处理
     *
     * @author yejiaxu
     * @param controller
     * @param request
     * @return
     */
    @ImplementMethod
    public InvokeResult<ListenVerticalComponentResponse> getListenVertical(ServiceController controller,
        ComponentRequest request)
    {
        if (LOG.isDebugEnabled())
        {
            LOG.debug("enter ListenVerticalComponentShowInfoImpl.getListenVertical,identityId:{}, request = {}",
                CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(request));
        }
        // 获取请求参数对象
        ListenVerticalRequest listenVerticalRequest = new ListenVerticalRequest(request);
        
        ListenVerticalComponentResponse.Builder builder = ListenVerticalComponentResponse.newBuilder();
        ListenVerticalData.Builder dataBuilder = ListenVerticalData.newBuilder();
        builder.setPluginCode(request.getParamMapMap().get(ParamConstants.PLUGINCODE));
        builder.setIsvisable(ParamConstants.NOT_VISABLE);
        builder.setStatus(MSResultCode.ErrorCodeAndDesc.CONNECTION_FAILED.getCode());
        builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.CONNECTION_FAILED.getDesc());
        // 入参非空校验
        try
        {
            checkResquestParam(listenVerticalRequest);
        }
        catch (PortalException e1)
        {

            LOG.error("listenVerticalComponentShowInfo param is null,identityId:{},ListenVerticalRequest:{}, e:{}",
                CommonHttpUtil.getIdentity(),
                listenVerticalRequest,
                e1.getMessage());
            builder.setStatus(e1.getExceptionCode());
            builder.setMessageDesc(e1.getMessage());
            return new InvokeResult<ListenVerticalComponentResponse>(builder.build());
            
        }
        
        // 根据专区id获取专区信息：
        NodeItem nodeItem = PortalNodeInfoCacheService.getInstance().getNodeItem(listenVerticalRequest.getNid());
        if (Util.isEmpty(nodeItem))
        {
            LOG.error(" listenVerticalComponentShowInfo nodeItem is null ,identityId:{},nid:{}",
                CommonHttpUtil.getIdentity(),
                listenVerticalRequest.getNid());
            builder.setStatus(MSResultCode.ErrorCodeAndDesc.NODE_NOT_EXIST.getCode());
            builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.NODE_NOT_EXIST.getDesc());
            return new InvokeResult<ListenVerticalComponentResponse>(builder.build());
        }
        // 根据专区信息获取图书ID
        String[] bookIds = nodeItem.getBookIDs();
        if (Util.isEmpty(bookIds))
        {
            LOG.error(
                " listenVerticalComponentShowInfo nodeItem.bookIds is null ,identityId:{}, nid:{}",
                CommonHttpUtil.getIdentity(),
                listenVerticalRequest.getNid());
            setNotVisable(builder);
            return new InvokeResult<ListenVerticalComponentResponse>(builder.build());
        }
        // 获取分页的图书信息
        PagedContent<List<Book>> pagedContent = null;
        try
        {
            pagedContent = BookManager.getPagedContent(null,
                listenVerticalRequest.getNid(),
                listenVerticalRequest.getPageNo(),
                listenVerticalRequest.getShowNum(),
                null);
        }
        catch (PortalException e)
        {
            LOG.error(
                " listenVerticalComponentShowInfo getPageContent error ,identityId:{}, nid:{}",
                CommonHttpUtil.getIdentity(),
                listenVerticalRequest.getNid());
            setNotVisable(builder);
            return new InvokeResult<ListenVerticalComponentResponse>(builder.build());
        }
        // 获取图书信息
        List<ListenVerticalBooks> listenVerticalBooks = getListVerticalBooks(pagedContent,
            listenVerticalRequest.getIsPlayNew(),
            listenVerticalRequest.getCornerShowType(),
            listenVerticalRequest.getNid(),
            controller);
        // 封装数据
        itemData(request, listenVerticalRequest, builder, dataBuilder, listenVerticalBooks);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("end ListenVerticalComponentShowInfoImpl.getListenVertical,identityId:{}, response = {}",
                CommonHttpUtil.getIdentity(),JsonFormatUtil.printToString(builder));
        }
        return new InvokeResult<ListenVerticalComponentResponse>(builder.build());
        
    }
    
    /**
     * 数据封装
     *
     * @author yejiaxu
     * @param request
     * @param listenVerticalRequest
     * @param builder
     * @param dataBuilder
     * @param listenVerticalBooks
     */
    private void itemData(ComponentRequest request, ListenVerticalRequest listenVerticalRequest,
        ListenVerticalComponentResponse.Builder builder, ListenVerticalData.Builder dataBuilder,
        List<ListenVerticalBooks> listenVerticalBooks)
    {
        if (Util.isEmpty(listenVerticalBooks))
        {
            listenVerticalBooks = new ArrayList<ListenVerticalBooks>();
            // ListenVerticalBooks.Builder listenVertical = ListenVerticalBooks.newBuilder();
            // listenVerticalBooks.add(listenVertical.build());
        }
        dataBuilder.addAllBooks(listenVerticalBooks);
        dataBuilder.setIsMarginBottom(listenVerticalRequest.getIsMarginBootom());
        dataBuilder.setIsMarginTop(listenVerticalRequest.getIsMarginTop());
        dataBuilder.setIsPaddingTop(listenVerticalRequest.getIsPaddingTop());
        dataBuilder.setIsShowLine(listenVerticalRequest.getIsShowLine());
        dataBuilder.setIsAjax(ParamConstants.IS_AJAX);
        ComponentRequest.Builder paramMapBuilder = request.toBuilder();
        paramMapBuilder.removeParamMap(ParamConstants.ISAJAX);
        dataBuilder.putAllCtag(paramMapBuilder.getParamMapMap());
        // 添加组件成功返回数据信息
        builder.setIsvisable(ParamConstants.IS_VISABLE);
        builder.setData(dataBuilder);
        builder.setStatus(MSResultCode.ErrorCodeAndDesc.SUCCESS.getCode());
        builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.SUCCESS.getDesc());
    }
    
    /**
     * 入参非空校验
     *
     * @author yejiaxu
     * @param listenVerticalRequest
     * @throws PortalException
     */
    private void checkResquestParam(ListenVerticalRequest listenVerticalRequest)
        throws PortalException
    {
        if (StringTools.isEmpty(listenVerticalRequest.getNid()))
        {
            throw new PortalException(MSResultCode.ERRORCODE_PARAMVALUE_IS_NULL,
                "nodeId:" + MSResultCode.ERRORMSG_PARAMVALUE_IS_NULL);
        }
        
    }
    
    /**
     * 设置组件不可见性
     *
     * @author yejiaxu
     * @param builder
     */
    private void setNotVisable(ListenVerticalComponentResponse.Builder builder)
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
     * 获取图书列表
     * 
     * @author yejiaxu
     * @param pagedContent
     * @return
     */
    private List<ListenVerticalBooks> getListVerticalBooks(PagedContent<List<Book>> pagedContent, String isPlayNew,
        String cornerShowType, String nodeId, ServiceController controller)
    {
        List<ListenVerticalBooks> listenVerticalBooks = null;
        if (Util.isNotEmpty(pagedContent))
        {
            if (Util.isNotEmpty(pagedContent.getContent()))
            {
                listenVerticalBooks = new ArrayList<>();
                for (Book book : pagedContent.getContent())
                {
                    if (Util.isNotEmpty(book))
                    {
                        ListenVerticalBooks.Builder listenBuilder = ListenVerticalBooks.newBuilder();
                        listenBuilder =
                            getListenVerticalBookInfo(book.getBookId(), isPlayNew, cornerShowType, nodeId, controller);
                        listenVerticalBooks.add(listenBuilder.build());
                    }
                }
            }
        }
        return listenVerticalBooks;
        
    }
    
    /**
     * 
     * 设置图书信息
     * 
     * @author yejiaxu
     * @param bookId
     * @return
     */
    private ListenVerticalBooks.Builder getListenVerticalBookInfo(String bookId, String isPlayNew,
        String cornerShowType, String nodeId, ServiceController controller)
    {
        ListenVerticalBooks.Builder listenBuilder = ListenVerticalBooks.newBuilder();
        BookItem bookItem = PortalContentInfoCacheService.getInstance().getBookItem(bookId);
        if (bookItem == null)
        {
            return null;
        }
        String speakerPreUrl = Speaker.getSpeakerUrlPrefix(controller);
        if (LOG.isDebugEnabled())
        {
            LOG.debug(
                "ListenVerticalComponentShowInfoImpl.getListenVerticalBookInfo  bookItem :{},bookId:{},isPlayNew:{},cornerShowType:{},nodeId:{}",
                bookItem,
                bookId,
                isPlayNew,
                cornerShowType,
                nodeId);
        }
        // 图书ID
        listenBuilder.setBookId(bookId);
        // 图书书名
        String bookName = bookItem.getBookName();
        listenBuilder.setBookShowName(ParamUtil.checkResponse(bookName));
        // 图书内容类型
        if (bookItem.getProductInfo() != null)
        {
            listenBuilder.setContentType(ParamUtil.checkResponse(bookItem.getProductInfo().getContentType()));
        }
        else
        {
            listenBuilder.setContentType(ParamUtil.checkResponse(null));
        }
        // 获取作者信息
        AuthorInfo authorInfo = PortalAuthorInfoCacheService.getInstance().getAuthorInfo(bookItem.getAuthorId());
        if (Util.isNotEmpty(authorInfo))
        {
            // 作者名
            listenBuilder.setAuthorName(ParamUtil.checkResponse(authorInfo.getAuthorPenName()));
            String authorDetailUrl = getAuthorUrl(authorInfo, nodeId, bookId, bookItem.getAuthorId());
            listenBuilder.setAuthorDetailUrl(ParamUtil.checkResponse(authorDetailUrl));
        }
        else
        {
            listenBuilder.setAuthorName(ParamUtil.checkResponse(null));
            listenBuilder.setAuthorDetailUrl(ParamUtil.checkResponse(null));
        }
        // 获取图书封面地址
        String bookCover = PortalBookCoverCacheService.getInstance().getBookCover(bookItem.getBookId(),
            PropertiesConfig.getProperty("cdnUrl"));
        listenBuilder.setBookCoverLogo(ParamUtil.checkResponse(bookCover));
        // 图书简介
        listenBuilder.setBookShortDesc(ParamUtil.checkResponse(bookItem.getShortDescription()));
        // 章节ID
        PortalContentExItem contentExItem =
            PortalContentInfoExCacheService.getInstance().getContentExItem(bookItem.getBookId());
        if (StringTools.isEq(isPlayNew, ParamConstants.NUMONE))
        {
            listenBuilder.setChapterId(ParamUtil.checkResponse(contentExItem.getFirstChapterId()));
        }
        else if (StringTools.isEq(isPlayNew, ParamConstants.NUMTWO))
        {
            listenBuilder.setChapterId(ParamUtil.checkResponse(contentExItem.getLatestChapterId()));
        }
        // 角标获取
        String cornerScene = BookDataUtil.getConer(bookItem, cornerShowType);
        listenBuilder.setCornerShowType(ParamUtil.checkResponse(cornerScene));
        // 获取主播信息
        Speaker speakerInfo = new Speaker(bookItem.getReaderid(), bookItem.getReaderName());
        if (speakerInfo != null)
        {
            listenBuilder.setSpeakerName((ParamUtil.checkResponse(speakerInfo.getName())));
            Map<String, String> params = UesServiceUtils.buildPublicParaMap(null, null);
            params.put(SelectionConstants.speakerId, speakerInfo.getId());
            String speakerUrl = UrlTools.processForView(speakerPreUrl, params);
            listenBuilder.setSpeakerDetailUrl(ParamUtil.checkResponse(StringTools.replaceSpecialChar(speakerUrl)));
        }
        // 图书状态
        listenBuilder.setBookStatus(ParamUtil.checkResponse(bookItem.getIsFinish()));
        return listenBuilder;
        
    }
    
    /**
     * 
     * 作者详情页地址
     * 
     * @author
     * @param authorInfo
     * @param nodeId
     * @param bookId
     * @return
     */
    private String getAuthorUrl(AuthorInfo authorInfo, String nodeId, String bookId, String aid)
    {
        // 请求参数
        Map<String, String> params = UesServiceUtils.buildPublicParaMap(null, null);
        String authorUrl = "";
        
        if (null != authorInfo && "0".equals(authorInfo.getAuthentication()))
        {
            authorUrl = UrlTools.replaceUrlHttpToEmptyWhenSchemeIsHttps(PortalConfig.get("forum_jump_url", ""));
            String barId =
                PortalPostInfoCacheService.getInstance().getBarForumId(WapConst.AUTHOR_BAR, authorInfo.getAuthorId());
            if (StringTools.isEmpty(barId))
            {
                return "";
            }
            params.put(SelectionConstants.forumId, barId);
        }
        else
        {
            // 跳往作家详情页地址
            authorUrl = UrlTools.replaceUrlHttpToHttpsWhenSchemeIsHttps(PortalConfig.get("author_detail_url", ""));
            // 如果没有配置，则默认跳新互动详情页
            if (StringTools.isEmpty(authorUrl))
            {
                authorUrl = SelectionConstants.authorUrl;
            }
            params.put(SelectionConstants.nodeId, nodeId);
            params.put(SelectionConstants.bookId, bookId);
        }
        params.put(SelectionConstants.authorId, aid);
        String url = UrlTools.processForView(authorUrl, params);
        return StringTools.replaceSpecialChar(url);
    }
    
}
