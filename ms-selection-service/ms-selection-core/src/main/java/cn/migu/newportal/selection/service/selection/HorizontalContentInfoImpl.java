package cn.migu.newportal.selection.service.selection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import cn.migu.newportal.cache.bean.CycleTypes;
import cn.migu.newportal.cache.bean.book.AuthorInfo;
import cn.migu.newportal.cache.bean.book.BookItem;
import cn.migu.newportal.cache.bean.node.NodeItem;
import cn.migu.newportal.cache.cache.manager.ContentSortManager;
import cn.migu.newportal.cache.cache.service.NewCycleDataCacheService;
import cn.migu.newportal.cache.cache.service.PortalAuthorInfoCacheService;
import cn.migu.newportal.cache.cache.service.PortalBookCoverCacheService;
import cn.migu.newportal.cache.cache.service.PortalContentInfoCacheService;
import cn.migu.newportal.cache.cache.service.PortalNodeInfoCacheService;
import cn.migu.newportal.cache.util.CommonHttpUtil;
import cn.migu.newportal.cache.util.JsonFormatUtil;
import cn.migu.newportal.cache.util.PropertiesConfig;
import cn.migu.newportal.selection.Exception.PortalException;
import cn.migu.newportal.selection.bean.local.request.HorizontalContenRequest;
import cn.migu.newportal.selection.util.BookDataUtil;
import cn.migu.newportal.selection.util.ContentUtil;
import cn.migu.newportal.selection.util.ParamUtil;
import cn.migu.newportal.selection.util.SelectionConstants;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode;
import cn.migu.newportal.util.tools.ProtoUtil;
import cn.migu.newportal.util.tools.MSResultCode.ErrorCodeAndDesc;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.HorizontalContentResponseOuterClass.HorizontalContentBooks;
import cn.migu.selection.proto.base.HorizontalContentResponseOuterClass.HorizontalContentData;
import cn.migu.selection.proto.base.HorizontalContentResponseOuterClass.HorizontalContentResponse;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 横向内容列表处理
 * 
 * @author yejiaxu
 * @version C10 2017年7月3日
 * @since SDP V300R003C10
 */
public class HorizontalContentInfoImpl extends ServiceMethodImpl<HorizontalContentResponse, ComponentRequest>
{
    
    // 日志对象
    private static final Logger LOG = LoggerFactory.getLogger(HorizontalContentInfoImpl.class.getName());
    
    private static final String METHOD_NAME = "getHorizontalContentInfo";
    
    /**
     * 页面展示样式 1:展示为样式一
     */
    private static final String TYPE_ONE = "1";
    
    /**
     * 页面展示样式 2:展示为样式二
     */
    private static final String TYPE_TWO = "2";
    
    
    public HorizontalContentInfoImpl()
    {
        super(METHOD_NAME);
    }
    
    
    /**
     * 
     * 横向内容列表
     * 
     * @author yejiaxu
     * @param controller
     * @param request
     * @return
     * @throws PortalException
     */
    @ImplementMethod
    public InvokeResult<HorizontalContentResponse> getHorizontalContentInfo(ServiceController controller,
        ComponentRequest request)
            throws PortalException
    {
        if (LOG.isDebugEnabled())
        {
            LOG.debug( "enter HorizontalContentInfoImpl-getHorizontalContentInfo，identityId:{} ComponentRequest :{}"
                , CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(request));
        }
        // 获取请求对象Bean
        HorizontalContenRequest horizontalContenRequest = new HorizontalContenRequest(request);
        HorizontalContentResponse.Builder builder = HorizontalContentResponse.newBuilder();
        
        HorizontalContentData.Builder dataBuilder = HorizontalContentData.newBuilder();
        builder.setPluginCode(request.getParamMapMap().get(ParamConstants.PLUGINCODE));
        builder.setIsvisable(ParamConstants.NOT_VISABLE);
        builder.setStatus(MSResultCode.ErrorCodeAndDesc.CONNECTION_FAILED.getCode());
        builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.CONNECTION_FAILED.getDesc());
        // 展示样式，1则为样式一，2则为样式二
        String[] typeValue = new String[] {TYPE_ONE, TYPE_TWO};
        dataBuilder.setType(ParamUtil.checkParamter(typeValue, horizontalContenRequest.getType(), TYPE_ONE));
        // 展示总的图书
        int totalNum = 3;
        if (StringTools.isNotEmpty(horizontalContenRequest.getShowBookNum()))
        {
            totalNum = ParamUtil.getShowNum(horizontalContenRequest.getShowBookNum(), 3);
        }
        else if (StringTools.isNotEmpty(horizontalContenRequest.getShowNum())
            && StringTools.isNotEmpty(horizontalContenRequest.getLineNum()))
        {
            totalNum = ParamUtil.getShowNum(horizontalContenRequest.getShowNum(), 3)
                * ParamUtil.getShowNum(horizontalContenRequest.getLineNum(), 1);
        }
        
        dataBuilder.setTotalNum(totalNum);
        // 是否显示页面底部横线
        dataBuilder.setIsShowLine(ParamUtil.getIsShowLine(horizontalContenRequest.getIsShowLine(), "0"));
        // 展示类型:1、书名，2、书名+作者名，3、书名+点击量
        dataBuilder.setShowType(ParamUtil.getShowType(horizontalContenRequest.getShowType(), "1"));
        // 展示腰封类型（单选框）:1、无，2、蓝色，3、红色，4、绿色
        dataBuilder.setIsShowYF(ParamUtil.getIsShowYF(horizontalContenRequest.getIsShowYF(), "1"));
        // 展示上边框1:是;0:否,默认0
        String[] isPaddingValue =
            new String[] {SelectionConstants.IS_MARGINORPADDINGTYPE, SelectionConstants.IS_NOT_MARGINORPADDINGTYPE};
        String isPaddingTop = ParamUtil.checkParamter(isPaddingValue,
            horizontalContenRequest.getIsPaddingTop(),
            SelectionConstants.IS_NOT_MARGINORPADDINGTYPE);
        dataBuilder.setIsPaddingTop(isPaddingTop);
        dataBuilder.setIsMarginBottom(horizontalContenRequest.getIsMarginBootom());
        dataBuilder.setIsMarginTop(horizontalContenRequest.getIsMarginTop());
        // 专区Id
        if (StringTools.isEmpty(horizontalContenRequest.getNodeId_list()))
        {
            LOG.error("book_horizontalContent nodeId_list is empty;identityId:{},node_id_list:{}",
                CommonHttpUtil.getIdentity(),
                horizontalContenRequest.getNodeId_list());
            builder.setStatus(MSResultCode.ERRORCODE_PARAMVALUE_IS_NULL);
            builder.setMessageDesc("nodeId_list :" + MSResultCode.ERRORMSG_PARAMVALUE_IS_NULL);
            return new InvokeResult<HorizontalContentResponse>(builder.build());
        }
        String[] nodeIds = horizontalContenRequest.getNodeId_list().split(",");
        // 获取组件实例ID
        String instanceId = request.getParamMapMap().get(ParamConstants.INSTANCEID);
        if (StringTools.isEmpty(horizontalContenRequest.getInstanceId()))
        {
            LOG.error("book_horizontalContent instanceId is empty,identityId:{},instanceId:{}",
                CommonHttpUtil.getIdentity(),
                horizontalContenRequest.getInstanceId());
            builder.setStatus(MSResultCode.ERRORCODE_PARAMVALUE_IS_NULL);
            builder.setMessageDesc("instanceId :" + MSResultCode.ERRORMSG_PARAMVALUE_IS_NULL);
            return new InvokeResult<HorizontalContentResponse>(builder.build());
        }
        // 获取专区
        CycleTypes cycle = new CycleTypes(request.getParamMapMap());
        String isCycle = ParamUtil.getIsCycle(horizontalContenRequest.getIsCycle(), "0");
        // 轮循
        if (SelectionConstants.IS_CYCLE.equals(isCycle))
        {
            nodeIds = NewCycleDataCacheService.getInstance().getCycleData(instanceId, cycle, nodeIds);
        }
        // 获取图书信息
        List<HorizontalContentBooks> booksList =
            getBooks(nodeIds, cycle, isCycle, horizontalContenRequest, controller, totalNum);
            
        if (Util.isEmpty(booksList))
        {
            LOG.error(
                "book_horizontalContent NodeItem is empty or NodeItem.books is empty;identityId:{},node_id_list:{}"
                ,
                CommonHttpUtil.getIdentity(),
                horizontalContenRequest.getNodeId_list());
            builder.setStatus(MSResultCode.ErrorCodeAndDesc.NODE_NOT_EXIST.getCode());
            builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.NODE_NOT_EXIST.getDesc());
            return new InvokeResult<HorizontalContentResponse>(builder.build());
        }
        dataBuilder.addAllBooks(booksList);
        // 添加组件成功返回数据信息
        builder.setData(dataBuilder);
        // 设置成功返回码
        ProtoUtil.buildResultCode(builder, ErrorCodeAndDesc.SUCCESS, ParamConstants.IS_VISABLE);
        // 封装展示状态
        ProtoUtil.buildCommonData(dataBuilder, request.getParamMapMap());
        
        if (LOG.isDebugEnabled())
        {
            LOG.debug("eixt HorizontalContentInfo-getHorizontalContentInfo,identityId:{} response :{} ",
                CommonHttpUtil.getIdentity(),
                JsonFormatUtil.printToString(builder));
        }
        return new InvokeResult<HorizontalContentResponse>(builder.build());
        
    }
    
    /**
     * 获取专区下所有图书ID
     *
     * @author yejiaxu
     * @param nodeIds
     * @param nodeShowNum
     * @param cycle
     * @param sortType
     * @param isCycle
     * @param instanceId
     * @return
     */
    private List<String> getBookIds(String[] nodeIds, int nodeShowNum, CycleTypes cycle, String sortType,
        String isCycle, String instanceId)
    {
        if (Util.isNotEmpty(nodeIds) && nodeIds.length <= nodeShowNum)
        {
            nodeShowNum = nodeIds.length;
        }
        List<String> bookIdResult = new ArrayList<String>();
        for (int i = 0; i < nodeShowNum; i++)
        {
            NodeItem listNodIds = PortalNodeInfoCacheService.getInstance().getNodeItem(nodeIds[i]);
            if (Util.isEmpty(listNodIds))
            {
                LOG.error("NodeItem is null ,identityId:{},nodeId :{},nodeShowNum :{}",
                    CommonHttpUtil.getIdentity(),
                    nodeIds[i],
                    nodeShowNum);
                continue;
            }
            // 判断节点是否有书籍
            String[] contentIds = listNodIds.getBookIDs();
            if (Util.isEmpty(contentIds))
            {
                LOG.error("NodeItem is not have books, identityId:{} nodeId : {}" ,CommonHttpUtil.getIdentity(), nodeIds[i]);
                continue;
            }
            else
            {
                // 获取排序或轮循后的全部书籍ID，不分页
                String[] bookIDs = ContentSortManager.getInstance().getBookListForNodes(cycle,
                    nodeIds[i],
                    sortType,
                    isCycle,
                    instanceId);
                List<String> bookIds = new ArrayList<String>();
                if(Util.isNotEmpty(bookIDs))
                {
                    bookIds = Arrays.asList(bookIDs);
                    bookIdResult.addAll(bookIds);
                }

            }
        }
        return bookIdResult;
    }
    
    /**
     * 
     * 获取图书信息
     * 
     * @author yejiaxu
     * @param nodeIds
     * @param cycle
     * @param isCycle
     * @param horizontalContenRequest
     * @param controller
     * @param totalNum
     * @return
     */
    private List<HorizontalContentBooks> getBooks(String[] nodeIds, CycleTypes cycle, String isCycle,
        HorizontalContenRequest horizontalContenRequest, ServiceController controller, int totalNum)
    {
        
        // 排序参数处理
        String sortType = ParamUtil.getSortType(horizontalContenRequest.getSortType(), "1");
        // 专区展示个数
        int nodeShowNum = ParamUtil.getShowNum(horizontalContenRequest.getShowNodeNum(), 1);
        // 获取图书id
        List<String> bookIdResult =
            getBookIds(nodeIds, nodeShowNum, cycle, sortType, isCycle, horizontalContenRequest.getInstanceId());
        List<HorizontalContentBooks> booksList = new ArrayList<HorizontalContentBooks>();
        if (bookIdResult != null && bookIdResult.size() > 0)
        {
            String bookUrlPrefix = ContentUtil.getBookUrlPrefix(controller);
            int bookSize = 0;
            for (int i = 0; i < bookIdResult.size(); i++)
            {
                if (bookSize == totalNum)
                {
                    break;
                }
                // 获取图书信息
                BookItem bookItem = PortalContentInfoCacheService.getInstance().getBookItem(bookIdResult.get(i));
                if (bookItem == null)
                {
                    LOG.debug("bookItem i null; bookId :{}" , bookIdResult.get(i));
                    continue;
                }
                HorizontalContentBooks.Builder contentBooks = HorizontalContentBooks.newBuilder();
                // 获取作者信息
                AuthorInfo authorInfo =
                    PortalAuthorInfoCacheService.getInstance().getAuthorInfo(bookItem.getAuthorId());
                if (Util.isNotEmpty(authorInfo))
                {
                    // 作者名
                    contentBooks.setAuthorName(ParamUtil.checkResponse(authorInfo.getAuthorPenName()));
                }
                else
                {
                    contentBooks.setAuthorName(" ");
                }
                // 获取书名
                contentBooks.setBookShowName(ParamUtil.checkResponse(
                    BookDataUtil.getShowName(bookIdResult.get(i), horizontalContenRequest.getNameShowType())));
                // 获取图书封面地址
                String bookCover = PortalBookCoverCacheService.getInstance().getBookCover(bookIdResult.get(i),
                    PropertiesConfig.getProperty("cdnUrl"));
                contentBooks.setBookCoverLogo(ParamUtil.checkResponse(bookCover));
                // 图书详情页地址
                contentBooks.setBookDetailUrl(bookUrlPrefix + "?bid=" + bookIdResult.get(i));
                // 获取阅读人数
                String bookUV = BookDataUtil.getInstance().getClickNumDesc(bookIdResult.get(i),
                    horizontalContenRequest.getLargeSize());
                if (StringTools.isEmpty(StringTools.nvl(bookUV)))
                {
                    bookUV = ParamConstants.CLICKNUM;
                }
                contentBooks.setBookClickDesc(ParamUtil.checkResponse(bookUV));
                // 获取图书角标信息
                String cornerScene = BookDataUtil.getConer(bookItem, horizontalContenRequest.getCornerShowType());
                contentBooks.setCornerShowType(ParamUtil.checkResponse(cornerScene));
                booksList.add(contentBooks.build());
                bookSize++;
            }
        }
        else
        {
            return null;
        }
        return booksList;
    }
}
