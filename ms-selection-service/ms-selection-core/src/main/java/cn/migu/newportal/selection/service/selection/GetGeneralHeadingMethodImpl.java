package cn.migu.newportal.selection.service.selection;

import java.util.Map;


import cn.migu.newportal.cache.bean.node.NodeItem;
import cn.migu.newportal.cache.cache.service.PortalNodeInfoCacheService;
import cn.migu.newportal.cache.manager.node.NodeManager;
import cn.migu.newportal.cache.util.CommonHttpUtil;
import cn.migu.newportal.cache.util.JsonFormatUtil;
import cn.migu.newportal.cache.util.PropertiesConfig;
import cn.migu.newportal.cache.util.UesServiceUtils;
import cn.migu.newportal.selection.bean.local.request.GetGeneralHeadingRequest;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.constants.Types;
import cn.migu.newportal.util.date.DateTools;
import cn.migu.newportal.util.http.UrlTools;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode;
import cn.migu.newportal.util.tools.ProtoUtil;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.GeneralHeadingResponseOuterClass.GeneralHeading;
import cn.migu.selection.proto.base.GeneralHeadingResponseOuterClass.GeneralHeadingResponse;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通用标题处理
 * 
 * @author hushanqing
 * @version C10 2017年8月30日
 * @since SDP V300R003C10
 */
public class GetGeneralHeadingMethodImpl extends ServiceMethodImpl<GeneralHeadingResponse, ComponentRequest>
{
    private static final Logger logger = LoggerFactory.getLogger(GetGeneralHeadingMethodImpl.class);
    
    private static final String METHOD_NAME = "getGeneralHeading";
    
    /** 是否是跳转专区类型-是 */
    private static final String IS_NODEURL = "1";
    
    public GetGeneralHeadingMethodImpl()
    {
        super(METHOD_NAME);
    }
    
    
    /**
     * 获取标题信息内容
     * 
     * @author hushanqing
     * @param controller
     * @param request
     * @return
     */
    @ImplementMethod
    public InvokeResult<GeneralHeadingResponse> getGeneralHeading(ServiceController controller,
        ComponentRequest request)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("Enter GetGeneralHeadingMethodImpl.getGeneralHeading(),identityId:{},request = "
                , CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(request));
        }
        // 封装请求参数
        Map<String, String> params = request.getParamMapMap();
        GetGeneralHeadingRequest headRequest = new GetGeneralHeadingRequest(params);
        
        GeneralHeadingResponse.Builder builder = GeneralHeadingResponse.newBuilder();
        builder.setPluginCode(StringTools.nvl(headRequest.getPluginCode()));
        
        // 封装响应
        buildResponse(controller, headRequest, builder);
        if (logger.isDebugEnabled())
        {
            logger.debug("exit GetGeneralHeadingMethodImpl.getGeneralHeading() ,identityId:{} ,response :{} "
                ,CommonHttpUtil.getIdentity(),JsonFormatUtil.printToString(builder));
        }
        return new InvokeResult<GeneralHeadingResponse>(builder.build());
    }
    
    /**
     * 封装响应参数
     * 
     * @author hushanqing
     * @param controller
     * @param headRequest
     * @param builder
     */
    private void buildResponse(ServiceController controller, GetGeneralHeadingRequest headRequest,
        GeneralHeadingResponse.Builder builder)
    {
        // 倒计时类型
        String countType = headRequest.getCountType();
        // 当前时间
        String currentTime = DateTools.getCurrentDate(DateTools.DATE_FORMAT_24HOUR_19);
        if (StringTools.isEq(countType, Types.COUNTTYPE_CURRENT))
        {
            headRequest.setStartTime(currentTime);
            headRequest.setEndTime(DateTools.getCurrentDate(DateTools.DATE_FORMAT_10) + " 23:59:59");
        }
        
        // 是否跳转专区
        String isNodeUrl = headRequest.getIsNodeUrl();
        String url = null;
        //是跳转专区
        if (StringTools.isEq(isNodeUrl, IS_NODEURL))
        {
            url = getNodeUrl(headRequest, controller);
        }
        //不是跳转专区
        else
        {
            url = UesServiceUtils.getUESUrl(headRequest.getUrl(), null);
        }
        
        GeneralHeading.Builder dataBuilder = GeneralHeading.newBuilder();
        ProtoUtil.buildCommonData(dataBuilder,headRequest);
        
        dataBuilder.setTitle(StringTools.nvl(headRequest.getTitle()));
        dataBuilder.setHuiyuan(StringTools.nvl(headRequest.getHuiyuan()));
        dataBuilder.setLabel(StringTools.nvl(headRequest.getLabel()));
        dataBuilder.setCountType(StringTools.nvl(headRequest.getCountType()));
        dataBuilder.setContent(StringTools.nvl(headRequest.getContent()));
        dataBuilder.setTextmin(StringTools.nvl((headRequest.getTextmin())));
        dataBuilder.setSuffix(StringTools.nvl(headRequest.getSuffix()));
        dataBuilder.setNowTime(currentTime);
        dataBuilder.setStartTime(StringTools.nvl(headRequest.getStartTime()));
        dataBuilder.setEndTime(StringTools.nvl(headRequest.getEndTime()));
        dataBuilder.setDesc(StringTools.nvl(headRequest.getUrlDesc()));
        dataBuilder.setUrl(StringTools.nvl(url));
        dataBuilder.setIsShowReminder(StringTools.nvl(headRequest.getIsShowReminder()));
        
        builder.setData(dataBuilder);
        ProtoUtil.buildResultCode(builder,MSResultCode.ErrorCodeAndDesc.SUCCESS,ParamConstants.IS_VISABLE);
    }
    
    /**
     * 获取跳转专区的url并拼接参数
     * 
     * @author hushanqing
     * @param headRequest
     * @return
     */
    private String getNodeUrl(GetGeneralHeadingRequest headRequest, ServiceController controller)
    {
        // 专区id
        String nid = headRequest.getNid();
        // 专区名称
        String nodeName = headRequest.getAlias();
        if (StringTools.isEmpty(nodeName))
        {
            NodeItem nodeItem = PortalNodeInfoCacheService.getInstance().getNodeItem(nid);
            if (Util.isNotEmpty(nodeItem))
            {
                nodeName = nodeItem.getName();
            }
        }

        String nodeUrl = NodeManager.getNodeDetailUrl(nid);
        
        Map<String, String> paramMap = UesServiceUtils.buildPublicParaMap(null, null);
        paramMap.put(ParamConstants.NODENAME, nodeName);

        return UesServiceUtils.getUESUrl(nodeUrl,paramMap);
    }
    
}
