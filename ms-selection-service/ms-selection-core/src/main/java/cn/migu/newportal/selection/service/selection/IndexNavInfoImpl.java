package cn.migu.newportal.selection.service.selection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.migu.newportal.cache.util.CommonHttpUtil;
import cn.migu.newportal.cache.util.JsonFormatUtil;
import org.apache.commons.lang.StringUtils;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import cn.migu.newportal.uesserver.api.Struct.ConfigRequest;
import cn.migu.newportal.uesserver.api.Struct.LinkInfo;
import cn.migu.newportal.uesserver.api.Struct.LinkRequest;

import cn.migu.newportal.cache.util.UesServerServiceUtils;
import cn.migu.newportal.cache.util.UesServiceUtils;
import cn.migu.newportal.selection.engine.ContentServiceEngine;
import cn.migu.newportal.selection.util.SelectionConstants;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.http.UrlTools;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.IndexNavResponseOuterClass.IndexNavContent;
import cn.migu.selection.proto.base.IndexNavResponseOuterClass.IndexNavIcon;
import cn.migu.selection.proto.base.IndexNavResponseOuterClass.IndexNavResponse;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeContext;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexNavInfoImpl extends ServiceMethodImpl<IndexNavResponse, ComponentRequest>
{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexNavInfoImpl.class);
    
    private static final String METHOD_NAME = "getLinkInfoList";
    
    
    public IndexNavInfoImpl()
    {
        super(METHOD_NAME);
    }
    
    
    @ImplementMethod
    public InvokeResult<IndexNavResponse> indexNavInfo(ServiceController controller, ComponentRequest request)
        throws JsonParseException, JsonMappingException, IOException
    {

        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug( "enter IndexNavInfoImpl-indexNavInfo，identityId:{} ComponentRequest :{}"
                , CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(request));
        }

        IndexNavResponse.Builder buliders = IndexNavResponse.newBuilder();
        InvokeContext context = controller.getContext();
      //  String domainUrl = StringUtils.EMPTY;
        Map<String, String> map = request.getParamMapMap();
        UesServiceUtils.setPublicParamToRequest(map);
        LinkRequest.Builder linkBulider = LinkRequest.newBuilder();
        linkBulider.setLinkIds(map.get(ParamConstants.LINKIDS));
        ConfigRequest.Builder configBuilder = ConfigRequest.newBuilder();
        configBuilder.setSiteId(SelectionConstants.DEFAULT_SITEID);
        configBuilder.setConfigkey(SelectionConstants.DOMAIN_PREFIX);
        
        List<LinkInfo> list = UesServerServiceUtils.getLinkInfoList(map.get(ParamConstants.LINKIDS));
        if (list != null && list.size() > 0)
        {
            IndexNavContent.Builder indeNavContent = IndexNavContent.newBuilder();
            
            indeNavContent.setIsShowLine(StringUtils.defaultString(request.getParamMapMap().get(ParamConstants.ISSHOWLINE)));
            indeNavContent.setIsMarginTop(StringUtils.defaultString(request.getParamMapMap().get(ParamConstants.ISMARGINTOP)));
            indeNavContent.setIsMarginBottom(StringUtils.defaultString(request.getParamMapMap().get(ParamConstants.ISMARGINBOTTOM)));
            
            List<IndexNavIcon> listContent = new ArrayList<IndexNavIcon>();
            IndexNavIcon.Builder indeNavBuilder = IndexNavIcon.newBuilder();
            for (LinkInfo info : list)
            {
                Map<String, String> paramap =  UesServiceUtils.buildPublicParaMap(null, String.valueOf(info.getSortvalue()));
                indeNavBuilder.setPicurl(StringTools.nvl(UesServiceUtils.getUESUrl(info.getPicurl(), null)));
                indeNavBuilder.setIconname(info.getLinkName());
                indeNavBuilder.setUrl(StringTools.nvl(UrlTools.processForView(info.getLinkurl(),paramap)));
                listContent.add(indeNavBuilder.build());
            }
            indeNavContent.addAllIcon(listContent);
            buliders.setData(indeNavContent);
            buliders.setIsvisable(ParamConstants.IS_VISABLE);
            buliders.setPluginCode(map.get(ParamConstants.PLUGINCODE));
            buliders.setStatus(MSResultCode.ErrorCodeAndDesc.SUCCESS.getCode());
            buliders.setMessageDesc(MSResultCode.ErrorCodeAndDesc.SUCCESS.getDesc());
        }
        else
        {
            buliders.setIsvisable(ParamConstants.NOT_VISABLE);
            buliders.setPluginCode(map.get(ParamConstants.PLUGINCODE));
            buliders.setStatus(MSResultCode.ErrorCodeAndDesc.CONNECTION_FAILED.getCode());
            buliders.setMessageDesc(MSResultCode.ErrorCodeAndDesc.CONNECTION_FAILED.getDesc());
        }
        return new InvokeResult<IndexNavResponse>(buliders.build());
    }
    
}
