package cn.migu.newportal.selection.service.selection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.migu.newportal.cache.util.*;
import org.apache.commons.lang.StringUtils;


import cn.migu.newportal.uesserver.api.Struct.ConfigRequest;
import cn.migu.newportal.uesserver.api.Struct.LinkInfo;
import cn.migu.newportal.uesserver.api.Struct.LinkRequest;

import cn.migu.newportal.selection.engine.ContentServiceEngine;
import cn.migu.newportal.selection.util.SelectionConstants;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.http.UrlTools;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode;
import cn.migu.selection.proto.base.ActivityFlexLinkResponseOuterClass.ActivityFlexLinkResponse;
import cn.migu.selection.proto.base.ActivityFlexLinkResponseOuterClass.FlexLinkData;
import cn.migu.selection.proto.base.ActivityFlexLinkResponseOuterClass.Links;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeContext;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivityFlexLinkInfoImpl extends ServiceMethodImpl<ActivityFlexLinkResponse, ComponentRequest>
{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityFlexLinkInfoImpl.class);
    
    private static final String METHOD_NAME = "getActivityFlexLinkInfo";
    
    public ActivityFlexLinkInfoImpl()
    {
        
        super(METHOD_NAME);
    }
    
    @ImplementMethod
    public InvokeResult<ActivityFlexLinkResponse> activityFlexLinkInfo(ServiceController controller,
        ComponentRequest req)
    {
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("enter ActivityFlexLinkInfoImpl.activityFlexLinkInfo,identityId:{}, ComponentRequest :{}",
                CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(req));
        }
        ActivityFlexLinkResponse.Builder builder = ActivityFlexLinkResponse.newBuilder();
        
        builder.setPluginCode(req.getParamMapMap().get(ParamConstants.PLUGINCODE));
        builder.setIsvisable(ParamConstants.NOT_VISABLE);
        builder.setStatus(MSResultCode.ErrorCodeAndDesc.CONNECTION_FAILED.getCode());
        builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.CONNECTION_FAILED.getDesc());
        UesServiceUtils.setPublicParamToRequest(req.getParamMapMap());
        Map<String, String> map = req.getParamMapMap();

        String domainUrl = StringUtils.EMPTY;
        
        List<LinkInfo> list = null;
        list = UesServerServiceUtils.getLinkInfoList(map.get(ParamConstants.LINKIDS));
        domainUrl = UesServerServiceUtils.getUesParamConfig(SelectionConstants.DOMAIN_PREFIX, "");
        FlexLinkData.Builder flexLinkBuild = FlexLinkData.newBuilder();
        if (list != null && list.size() > 0)
        {
            if (list.size() > 3)
            {
                list = list.subList(0, 3);
            }
            flexLinkBuild.setTotalNum(String.valueOf(list.size()));
            flexLinkBuild.setPicUrl(StringTools
                .nvl(UrlTools.getRelativelyUrl(map.get("picUrl"), PropertiesConfig.getProperty("defaultConfig"))));
            flexLinkBuild.setIsShowLine(map.get("isShowLine") == null ? "1" : map.get(ParamConstants.ISSHOWLINE));
            
            flexLinkBuild
                .setIsMarginTop(StringUtils.defaultString(req.getParamMapMap().get(ParamConstants.ISMARGINTOP)));
            flexLinkBuild
                .setIsMarginBottom(StringUtils.defaultString(req.getParamMapMap().get(ParamConstants.ISMARGINBOTTOM)));
            
            List<Links> linkList = new ArrayList<Links>();
            for (LinkInfo link : list)
            {
                
                Links.Builder linkBuild = Links.newBuilder();
                Map<String, String> params =
                    UesServiceUtils.buildPublicParaMap(null, String.valueOf(link.getSortvalue()));
                linkBuild.setUrl(StringTools.nvl(UrlTools.processForView(
                    UrlTools.getRelativelyUrl(link.getLinkurl(), PropertiesConfig.getProperty("defaultConfig")),
                    params)));
                linkList.add(linkBuild.build());
            }
            flexLinkBuild.addAllLinks(linkList);
            
            String isAjax = map.get(ParamConstants.ISAJAX);
            if (StringUtils.isNotEmpty(isAjax) && "1".equals(isAjax))
            {
                ComponentRequest.Builder paramMapBuilder = req.toBuilder();
                paramMapBuilder.removeParamMap(ParamConstants.ISAJAX);
                flexLinkBuild.putAllCtag(paramMapBuilder.getParamMapMap());
            }
            builder.setData(flexLinkBuild);
            builder.setIsvisable(ParamConstants.IS_VISABLE);
            builder.setStatus(MSResultCode.ErrorCodeAndDesc.SUCCESS.getCode());
            builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.SUCCESS.getDesc());
            builder.setPluginCode(map.get(ParamConstants.PLUGINCODE));
            
        }
        
        return new InvokeResult<ActivityFlexLinkResponse>(builder.build());
        
    }
    
}
