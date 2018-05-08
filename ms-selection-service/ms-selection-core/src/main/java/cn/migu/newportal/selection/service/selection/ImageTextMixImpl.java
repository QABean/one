package cn.migu.newportal.selection.service.selection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import cn.migu.newportal.cache.bean.CycleTypes;
import cn.migu.newportal.cache.bean.book.AuthorInfo;
import cn.migu.newportal.cache.bean.book.BookItem;
import cn.migu.newportal.cache.cache.service.PortalAuthorInfoCacheService;
import cn.migu.newportal.cache.cache.service.PortalBookCoverCacheService;
import cn.migu.newportal.cache.cache.service.PortalContentInfoCacheService;
import cn.migu.newportal.cache.util.*;
import cn.migu.newportal.selection.Exception.PortalException;
import cn.migu.newportal.selection.bean.local.request.ImageTextMixRequest;
import cn.migu.newportal.selection.manager.CycleTypesManager;
import cn.migu.newportal.selection.util.BookDataUtil;
import cn.migu.newportal.selection.util.ContentUtil;
import cn.migu.newportal.selection.util.ParamUtil;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode;
import cn.migu.selection.proto.base.BookDateOuterClass.BookDate;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.ComponentShowResponseOuterClass.ComponentShowData;
import cn.migu.selection.proto.base.ComponentShowResponseOuterClass.ComponentShowResponse;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 图文混排
 * 
 *
 * @author zhangmm
 * @version C10 2017年7月25日
 * @since SDP V300R003C10
 */
public class ImageTextMixImpl extends ServiceMethodImpl<ComponentShowResponse, ComponentRequest>
{
    
    // 日志对象
    private static final Logger LOG = LoggerFactory.getLogger(ImageTextMixImpl.class.getName());
    
    private static final String METHOD_NAME = "getComponentShow";
    
    public ImageTextMixImpl()
    {
        super(METHOD_NAME);
    }
    
    /**
     * 
     * 
     *
     * @author zhangmm
     * @param controller
     * @param request
     * @return
     * @throws PortalException
     */
    @ImplementMethod
    public InvokeResult<ComponentShowResponse> getComponentShow(ServiceController controller, ComponentRequest request)
        throws PortalException
    {
        // 入口日志
        if (LOG.isDebugEnabled())
        {
            LOG.debug( "enter ImageTextMixImpl-getComponentShow，identityId:{} ComponentRequest :{}"
                , CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(request));
        }
        // 关键字判断
        ComponentShowResponse.Builder response = ComponentShowResponse.newBuilder();
        ComponentShowData.Builder compShowData = ComponentShowData.newBuilder();
        response.setPluginCode(request.getParamMapMap().get(ParamConstants.PLUGINCODE));
        response.setStatus(MSResultCode.ErrorCodeAndDesc.CONNECTION_FAILED.getCode());
        response.setIsvisable(ParamConstants.NOT_VISABLE);
        if (StringTools.isEmpty(request.getParamMapMap().get(ParamConstants.INSTANCEID)))
        {
            LOG.error("booktext_mix_list instanceId is empty,identityId:{},instanceId:{}",
                CommonHttpUtil.getIdentity(),
                request.getParamMapMap().get(ParamConstants.INSTANCEID));
            response.setStatus(MSResultCode.ERRORCODE_PARAMVALUE_IS_NULL);
            response.setMessageDesc("instanceId :" + MSResultCode.ERRORMSG_PARAMVALUE_IS_NULL);
            return new InvokeResult<ComponentShowResponse>(response.build());
        }
        if (StringTools.isEmpty(request.getParamMapMap().get(ParamConstants.NODE_ID_LIST)))
        {
            LOG.error("booktext_mix_list node_id_list is empty,identityId:{},node_id_list:{}",
                CommonHttpUtil.getIdentity(),
                request.getParamMapMap().get(ParamConstants.NODE_ID_LIST));
            response.setStatus(MSResultCode.ERRORCODE_PARAMVALUE_IS_NULL);
            response.setMessageDesc("node_id_list :" + MSResultCode.ERRORMSG_PARAMVALUE_IS_NULL);
            return new InvokeResult<ComponentShowResponse>(response.build());
        }
        // request参数封装
        CycleTypes cycleTypes = new CycleTypes(request.getParamMapMap());
        ImageTextMixRequest imageTextMix = new ImageTextMixRequest(request.getParamMapMap());
        // 调用接口获取响应数据
        List<BookDate> bookList =
            getBooksInfo(imageTextMix, cycleTypes, controller, ParamConstants.SHOWBOOKNUM, request);
        
        compShowData.setIsMarginTop(imageTextMix.getIsmarginTop());
        compShowData.setIsMarginBottom(imageTextMix.getIsMarginBottom());
        compShowData.setIsPaddingTop(imageTextMix.getIsPaddingTop());
        compShowData.setIsShowLine(imageTextMix.getIsShowLine());
        compShowData.setEndLogo(imageTextMix.getEndLogo());
        if (Util.isEmpty(bookList))
        {
            LOG.error(
                "booktext_mix_list bookList is empty bookList is null,identityId:{},node_id_list:{},sortType:{},errorCode:{}"
                ,
                CommonHttpUtil.getIdentity(),
                imageTextMix.getNode_id_list(),
                imageTextMix.getSortType(),
                MSResultCode.ErrorCodeAndDesc.NODE_NOT_EXIST.getCode());
            response.setStatus(MSResultCode.ErrorCodeAndDesc.NODEBOOK_NOT_EXIST.getCode());
            response.setMessageDesc(MSResultCode.ErrorCodeAndDesc.NODEBOOK_NOT_EXIST.getDesc());
            return new InvokeResult<ComponentShowResponse>(response.build());
        }
        compShowData.addAllBooks(bookList);
        response.setStatus(ParamConstants.SUCCESS_CODE);
        response.setMessageDesc(MSResultCode.ErrorCodeAndDesc.SUCCESS.getDesc());
        response.setIsvisable(ParamConstants.IS_VISABLE);
        response.setData(compShowData);
        // 出口日志
        if (LOG.isDebugEnabled())
        {
            LOG.debug("Exit ImageTextMixImpl.getComponentShow,identityId:{},response:{}",
                CommonHttpUtil.getIdentity(),
                JsonFormatUtil.printToString(response));
        }
        return new InvokeResult<ComponentShowResponse>(response.build());
    }
    
    /**
     * 获取轮询后的封装图书信息
     * 
     *
     * @author zhangmm
     * @param imageTextMix
     * @param cycle
     * @param controller
     * @param totalNum
     * @return
     */
    private List<BookDate> getBooksInfo(ImageTextMixRequest imageTextMix, CycleTypes cycle,
        ServiceController controller, int totalNum, ComponentRequest request)
    {
        if (LOG.isDebugEnabled())
        {
            LOG.debug("Enter ImageTextMixImpl-getBooksInfo totalNum :{} " , totalNum);
        }
        List<BookDate> booksList = new ArrayList<BookDate>();
        
        // 获取专区id
        String[] nodeIds = imageTextMix.getNode_id_list().split(",");
        List<String> bookList = CycleTypesManager.getBookListByNodeid(cycle,
            nodeIds,
            imageTextMix.getSortType(),
            imageTextMix.getInstanceId());
        int bookSize = 0;
        BookDate.Builder bookDate = BookDate.newBuilder();
        if (Util.isNotEmpty(bookList))
        {
            for (String bookId : bookList)
            {
                if (bookSize == totalNum)
                {
                    break;
                }
                BookItem bookItem = PortalContentInfoCacheService.getInstance().getBookItem(bookId);
                if (Util.isEmpty(bookItem))
                {
                    totalNum++;
                    continue;
                }
                bookDate.setBookShowName(BookDataUtil.getShowName(bookId, imageTextMix.getNameShowType()));
                // 获取作者信息
                AuthorInfo authorInfo =
                    PortalAuthorInfoCacheService.getInstance().getAuthorInfo(bookItem.getAuthorId());
                if (Util.isNotEmpty(authorInfo))
                {
                    // 作者名
                    bookDate.setAuthorName(ParamUtil.checkResponse(authorInfo.getAuthorPenName()));
                }
                else
                {
                    bookDate.setAuthorName(" ");
                }
                bookDate.setBookBrief(ParamUtil.checkResponse(bookItem.getShortDescription()));
                // 获取图书封面地址
                String bookCover = PortalBookCoverCacheService.getInstance().getBookCover(bookId,
                    PropertiesConfig.getProperty(ParamConstants.CDNURL));
                bookDate.setBookCoverLogo(ParamUtil.checkResponse(bookCover));
                Map<String, String> param = UesServiceUtils.buildPublicParaMap(null, null);
                param.put(ParamConstants.BID, bookId);
                param.put(ParamConstants.NID, request.getParamMapMap().get(ParamConstants.NID));
                //图书详情页地址
                String bookUrlPrefix = PresetPageUtils.getBookDetailPage(param);
                bookDate.setBookDetailUrl(UesServiceUtils.getUESUrl(bookUrlPrefix, param));
                String click = BookDataUtil.getInstance().getClickNumDesc(bookId, imageTextMix.getLargeSize());
                bookDate.setBookClickDesc(ParamUtil.checkResponse(click));
                bookDate.setBookScore(ParamUtil.checkResponse(BookDataUtil.getScore(bookId)));
                bookDate.setCateNode(ParamUtil.checkResponse(BookDataUtil.getNodeName(bookId, bookItem)));
                booksList.add(bookDate.build());
                bookSize++;
            }
        }
        if (LOG.isDebugEnabled())
        {
            LOG.debug("Exit ImageTextMixImpl-lgetBooksInfo,booksList:{} ",booksList);
        }
        return booksList;
    }
}
