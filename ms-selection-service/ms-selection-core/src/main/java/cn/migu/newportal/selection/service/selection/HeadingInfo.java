package cn.migu.newportal.selection.service.selection;

import java.util.List;
import java.util.Map;

import cn.migu.newportal.cache.util.CommonHttpUtil;
import cn.migu.newportal.cache.util.JsonFormatUtil;
import org.apache.commons.lang.StringUtils;


import cn.migu.newportal.uesserver.api.Struct.LinkInfo;
import cn.migu.newportal.uesserver.api.Struct.LinkRequest;

import cn.migu.newportal.cache.util.UesServerServiceUtils;
import cn.migu.newportal.cache.util.UesServiceUtils;
import cn.migu.newportal.selection.util.JsonUtil;
import cn.migu.newportal.selection.util.SelectionConstants;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.tools.MSResultCode;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.HeadingResponseOuterClass.HeadingData;
import cn.migu.selection.proto.base.HeadingResponseOuterClass.HeadingResponse;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeContext;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeadingInfo extends ServiceMethodImpl<HeadingResponse, ComponentRequest>
{
    private static final Logger logger = LoggerFactory.getLogger(HeadingInfo.class);
    
    private static final String METHOD_NAME = "getHeadingInfo";
    
    public HeadingInfo()
    {
        super(METHOD_NAME);
    }
    
    
    @ImplementMethod
    public InvokeResult<HeadingResponse> getHeadingInfo(ServiceController controller, ComponentRequest request)
    {
        InvokeContext context = controller.getContext();

        if (logger.isDebugEnabled())
        {
            logger.debug( "enter HeadingInfo-getHeadingInfo，identityId:{} ComponentRequest :{}"
                , CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(request));
        }
        Map<String, String> reqMap = request.getParamMapMap();
        UesServiceUtils.setPublicParamToRequest(reqMap);
        String linkId = reqMap.get(ParamConstants.LINKID);
        
        LinkRequest.Builder linkRequestBuilder = LinkRequest.newBuilder();
        linkRequestBuilder.setLinkIds(linkId);
        List<LinkInfo> list = null;
        list = UesServerServiceUtils.getLinkInfoList(linkId);
        HeadingResponse.Builder headingRspBuilder = HeadingResponse.newBuilder();
        HeadingData.Builder headingData = HeadingData.newBuilder();
        if (list != null && list.size() != 0)
        {
            String style = reqMap.get(ParamConstants.STYLE);
            String isShowLine = reqMap.get(ParamConstants.ISSHOWLINE);
            String isMarginTop = reqMap.get(ParamConstants.ISMARGINTOP);
            String isMarginBottom = reqMap.get(ParamConstants.ISMARGINBOTTOM);
            String isMember = reqMap.get(ParamConstants.ISMEMBER);
            String startTime = reqMap.get(ParamConstants.STARTTIME);
            String endTime = reqMap.get(ParamConstants.ENDTIME);
            LinkInfo linkInfo = list.get(0);
            
            headingData.setIsShowLine(StringUtils.defaultString(isShowLine));
            headingData.setIsMarginTop(StringUtils.defaultString(isMarginTop));
            headingData.setIsMarginBottom(StringUtils.defaultString(isMarginBottom));
            headingData.setIsMember(StringUtils.defaultString(isMember));
            headingData.setStyle(StringUtils.defaultString(style));
            headingData.setTitle(linkInfo.getLinkName());
            headingData.setTitleDesc(linkInfo.getDescription());
            headingData.setMore(linkInfo.getLinksuffix());
            headingData.setStartTime(StringUtils.defaultString(startTime));
            headingData.setEndTime(StringUtils.defaultString(endTime));
            
            String pgPrefix = null;
            pgPrefix =  UesServerServiceUtils.getUesParamConfig(SelectionConstants.DOMAIN_PREFIX, "");
            pgPrefix = StringUtils.trimToEmpty(pgPrefix);
            pgPrefix = pgPrefix.endsWith("/") ? pgPrefix : pgPrefix + "/";
            headingData.setMoreUrl(UesServiceUtils.getUESUrl(linkInfo.getLinkurl(),
                UesServiceUtils.buildPublicParaMap(null, Util.nvl(linkInfo.getSortvalue()))));
            
        }
        else
        {
            headingRspBuilder.setIsvisable(ParamConstants.NOT_VISABLE);
            headingRspBuilder.setStatus(MSResultCode.ErrorCodeAndDesc.CONNECTION_FAILED.getCode());
            headingRspBuilder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.CONNECTION_FAILED.getDesc());
        }
        headingRspBuilder.setData(headingData);
        headingRspBuilder.setIsvisable(ParamConstants.IS_VISABLE);
        headingRspBuilder.setStatus(MSResultCode.ErrorCodeAndDesc.SUCCESS.getCode());
        headingRspBuilder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.SUCCESS.getDesc());
        headingRspBuilder.setPluginCode(StringUtils.defaultString(reqMap.get(ParamConstants.PLUGINCODE)));
        
        logger.debug("Exit HeadingInfo.getHeadingInfo Response :{} ",JsonUtil.objToJson(headingRspBuilder));
        
        return new InvokeResult<HeadingResponse>(headingRspBuilder.build());
    }
}
