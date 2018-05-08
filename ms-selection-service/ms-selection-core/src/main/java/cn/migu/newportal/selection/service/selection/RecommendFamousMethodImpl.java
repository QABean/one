package cn.migu.newportal.selection.service.selection;

import java.util.List;
import java.util.Map;

import cn.migu.newportal.cache.util.CommonHttpUtil;
import cn.migu.newportal.cache.util.JsonFormatUtil;
import cn.migu.newportal.uesserver.api.Struct.LinkInfo;
import cn.migu.newportal.uesserver.api.Struct.LinkRequest;

import cn.migu.newportal.cache.util.UesServerServiceUtils;
import cn.migu.newportal.cache.util.UesServiceUtils;
import cn.migu.newportal.selection.bean.local.request.GetRecommendAuthorRequest;
import cn.migu.newportal.selection.engine.ContentServiceEngine;
import cn.migu.newportal.selection.util.UrlUtil;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.constants.SystemConstants;
import cn.migu.newportal.util.exception.PortalException;
import cn.migu.newportal.util.http.UrlTools;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.RecommendFamousResponseOuterClass.RecommendFamousData;
import cn.migu.selection.proto.base.RecommendFamousResponseOuterClass.RecommendFamousResponse;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeContext;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 名家推介
 *
 * @author wangwenjuan
 * @version C10 2017年7月19日
 * @since SDP V300R003C10
 */
public class RecommendFamousMethodImpl extends ServiceMethodImpl<RecommendFamousResponse, ComponentRequest>
{
    // 日志对象
    private static final Logger LOG = LoggerFactory.getLogger(RecommendFamousMethodImpl.class);
    
    private static final String METHOD_NAME = "getRecommendFamousInfo";
    
    public RecommendFamousMethodImpl()
    {
        super(METHOD_NAME);
    }
    
    /**
     * 
     * 名家推介
     *
     * @param controller
     * @param request
     * @return
     * @throws PortalException
     */
    @ImplementMethod
    public InvokeResult<RecommendFamousResponse> getRecommendFamous(ServiceController controller,
        ComponentRequest request)
        throws PortalException
    {
        if (LOG.isDebugEnabled())
        {
            LOG.debug( "enter RecommendFamousMethodImpl-getRecommendFamous，identityId:{} ComponentRequest :{}"
                , CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(request));
        }
        GetRecommendAuthorRequest tempRequest = new GetRecommendAuthorRequest(request);
        UesServiceUtils.setPublicParamToRequest(request.getParamMapMap());
        RecommendFamousResponse.Builder builder = RecommendFamousResponse.newBuilder();
        RecommendFamousData.Builder builderData = RecommendFamousData.newBuilder();
        // 设置组件名称：
        builder.setPluginCode(tempRequest.getPluginCode());
        // 参数校验
        if (!checkParams(tempRequest, builder))
        {
            return new InvokeResult<RecommendFamousResponse>(builder.build());
        }
        
        // 获取配置链接ID字符串集合，以“,”隔开
        String linkIdList = tempRequest.getLinkIdList();
        // 获取链接列表,上面已经判空，此处不会越界，无需判断；已经和张赛确认过
        List<LinkInfo> linkInfoList = getLinkList(linkIdList.split(SystemConstants.COMMA_SEPARATOR)[0], controller);
        
        buildResponseData(controller, tempRequest, builder, builderData, linkIdList, linkInfoList);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("exit RecommendFamousMethod.getRecommendFamous,identityId:{} RecommendFamousResponse:{} "
                ,CommonHttpUtil.getIdentity(),JsonFormatUtil.printToString(builder));
        }
        return new InvokeResult<RecommendFamousResponse>(builder.build());
    }
    
    /**
     * 
     * 
     *
     * @author zhaoxinwei
     * @param controller
     * @param tempRequest
     * @param builder
     * @param builderData
     * @param linkIdList
     * @param linkInfoList
     */
    private void buildResponseData(ServiceController controller, GetRecommendAuthorRequest tempRequest,
        RecommendFamousResponse.Builder builder, RecommendFamousData.Builder builderData, String linkIdList,
        List<LinkInfo> linkInfoList)
    {
        if (Util.isEmpty(linkInfoList))
        {
            LOG.error("get RecommendFamousMethod.getRecommendFamous linkInfoList is null, identityId:{},linkIdList:{}",
                CommonHttpUtil.getIdentity(),
                linkIdList);
            builder.setIsvisable(ParamConstants.NOT_VISABLE);
            builder.setStatus(MSResultCode.ErrorCodeAndDesc.LINK_NOT_EXIST.getCode());
            builder.setMessageDesc(MSResultCode.ERRORMSG_LINKINFO_IS_NULL);
            return;
        }
        for (LinkInfo linkInfo : linkInfoList)
        {
            if (Util.isEmpty(linkInfo))
            {
                continue;
            }
            
            Map<String, String> paramMap =
                    UesServiceUtils.buildPublicParaMap(null, StringTools.nvl(linkInfo.getSortvalue()));
            
            // 获取配置的连接地址
            String linkUrl = linkInfo.getLinkurl();
            linkUrl = UesServiceUtils.getUESUrl(linkUrl, paramMap);
            
            // 获取配置的图片地址
            String picUrl = linkInfo.getPicurl();
            picUrl = UrlTools.getRelativelyUrl(picUrl, UesServiceUtils.DEFAULT_DOMAIN);
            
            String linkDesc = linkInfo.getDescription();
            String linkPrefix = linkInfo.getLinkprefix();
            
            builderData.setLinkPrefix(StringTools.nvl(linkPrefix));
            builderData.setLinkUrl(StringTools.nvl(linkUrl));
            builderData.setPicUrl(StringTools.nvl(picUrl) );
            builderData.setLinkDesc(StringTools.nvl(linkDesc));
            
            builderData.setShowType(tempRequest.getShowType());
            builderData.setIsMarginBottom(tempRequest.getIsMarginBottom());
            builderData.setIsMarginTop(tempRequest.getIsMarginTop());
            builderData.setIsPaddingTop(tempRequest.getIsPaddingTop());
            builderData.setIsShowLine(tempRequest.getIsShowLine());
            
            setSuccessfulResp(builder);
            builder.setData(builderData);
            break;
        }
    }
    
    /**
     * 构造成功的响应
     * 
     *
     * @author zhaoxinwei
     * @param builder
     */
    private void setSuccessfulResp(RecommendFamousResponse.Builder builder)
    {
        if (Util.isNotEmpty(builder))
        {
            builder.setIsvisable(ParamConstants.IS_VISABLE);
            builder.setStatus(MSResultCode.ErrorCodeAndDesc.SUCCESS.getCode());
            builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.SUCCESS.getDesc());
        }
    }
    
    /**
     * 
     * 用于参数校验，如果参数校验不通过返回false，通过返回true
     *
     * @author zhaoxinwei
     * @param tempRequest
     * @param builder
     */
    private boolean checkParams(GetRecommendAuthorRequest tempRequest, RecommendFamousResponse.Builder builder)
    {
        if (StringTools.isEmpty(tempRequest.getLinkIdList()))
        {
            builder.setIsvisable(ParamConstants.NOT_VISABLE);
            builder.setStatus(MSResultCode.ERRORCODE_PARAMVALUE_IS_NULL);
            builder.setMessageDesc("linkIdList" + MSResultCode.ERRORMSG_PARAMVALUE_IS_NULL);
            return false;
        }
        return true;
    }
    
    /**
     * 获取配置链接列表
     * 
     * @author
     * @param controller
     * @param linkIdList
     * @return
     */
    private List<LinkInfo> getLinkList(String linkIdList, ServiceController controller)
    {
        if (LOG.isDebugEnabled())
        {
            LOG.debug("enter  RecommendFamousMethod.getLinkList  link_id_list = {}" , linkIdList);
        }
        List<LinkInfo> list = null;
        // 调用ues微服务获取链接信息
        list = UesServerServiceUtils.getLinkInfoList(linkIdList);
        LOG.debug("end RecommendFamousMethod getLinklist  list = " + list);
        return list;
    }
    
}
