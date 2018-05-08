package cn.migu.newportal.selection.service.selection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.migu.newportal.cache.util.CommonHttpUtil;
import cn.migu.newportal.cache.util.JsonFormatUtil;
import org.apache.commons.lang.StringUtils;


import cn.migu.newportal.uesserver.api.Struct.LinkInfo;

import cn.migu.newportal.cache.util.UesServerServiceUtils;
import cn.migu.newportal.cache.util.UesServiceUtils;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.http.UrlTools;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.RecommendResponseOuterClass.Recommend;
import cn.migu.selection.proto.base.RecommendResponseOuterClass.RecommendData;
import cn.migu.selection.proto.base.RecommendResponseOuterClass.RecommendResponse;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecommendInfoImpl extends ServiceMethodImpl<RecommendResponse, ComponentRequest>
{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RecommendInfoImpl.class.getName());
    
    private static final String METHOD_NAME = "getRecommendInfo";
    
    private com.google.protobuf.Service service;
    
    public RecommendInfoImpl()
    {
        super(METHOD_NAME);
    }
    
    public void initialize(com.google.protobuf.Service service)
    {
        this.service = service;
        super.initialize(RecommendInfoImpl.class);
        
    }
    
    public com.google.protobuf.Service getService()
    {
        return service;
    }
    
    public void setService(com.google.protobuf.Service service)
    {
        this.service = service;
    }
    
    @ImplementMethod
    public InvokeResult<RecommendResponse> indexNavInfo(ServiceController controller, ComponentRequest req)
        throws IOException
    {
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug( "enter RecommendInfoImpl-indexNavInfo，identityId:{} ComponentRequest :{}"
                , CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(req));
        }

        Map<String, String> map = req.getParamMapMap();
        UesServiceUtils.setPublicParamToRequest(map);
        RecommendResponse.Builder builder = RecommendResponse.newBuilder();

        
        List<LinkInfo> list = null;
        list = UesServerServiceUtils.getLinkInfoList(map.get(ParamConstants.LINKIDS));
        if (list != null && list.size() > 0)
        {
            Set<LinkInfo> set = new HashSet<LinkInfo>();
            if (list.size() > 5)// 判断数据是否大于5 若大于则随机给5个
            {
                set = getRandomNum(list, 5);
            }
            else // 小于5则全给
            {
                set.addAll(list);
            }
            Recommend.Builder recomment = Recommend.newBuilder();
            
            recomment.setIsMarginTop(StringUtils.defaultString(req.getParamMapMap().get(ParamConstants.ISMARGINTOP)));
            recomment.setIsMarginBottom(StringUtils.defaultString(req.getParamMapMap().get(ParamConstants.ISMARGINBOTTOM)));
            
            List<RecommendData> recoList = new ArrayList<RecommendData>();
            for (LinkInfo info : set)
            {
                RecommendData.Builder recoBuild = RecommendData.newBuilder();
                recoBuild.setName(info.getLinkName());
                Map<String, String> param = UesServiceUtils.buildPublicParaMap(null, String.valueOf(info.getSortvalue()));
                recoBuild.setUrl(StringTools.nvl(UrlTools.processForView(info.getLinkurl(),param)));
                recoList.add(recoBuild.build());
            }
            
            recomment.addAllButtons(recoList);
            String isAjax = map.get(ParamConstants.ISAJAX);
            if (StringUtils.isNotEmpty(isAjax) && "1".equals(isAjax))
            {
                ComponentRequest.Builder paramMapBuilder = req.toBuilder();
                paramMapBuilder.removeParamMap(ParamConstants.ISAJAX);
                recomment.putAllCtag(paramMapBuilder.getParamMapMap());
            }
            builder.setData(recomment);
            builder.setIsvisable(ParamConstants.IS_VISABLE);
            builder.setStatus(MSResultCode.ErrorCodeAndDesc.SUCCESS.getCode());
            builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.SUCCESS.getDesc());
            builder.setPluginCode(map.get(ParamConstants.PLUGINCODE));
            String isShowLine = map.get(ParamConstants.ISSHOWLINE);
            if (StringUtils.isNotEmpty(isShowLine))
            {
                builder.setIsShowLine(isShowLine);
            }
            
        }
        else
        {
            builder.setIsvisable(ParamConstants.NOT_VISABLE);
            builder.setPluginCode(map.get(ParamConstants.PLUGINCODE ));
            builder.setStatus(MSResultCode.ErrorCodeAndDesc.CONNECTION_FAILED.getCode());
            builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.CONNECTION_FAILED.getDesc());
        }
        
        // builder.setStatus(String.valueOf(result.getCode()));
        return new InvokeResult<RecommendResponse>(builder.build());
    }
    
    /**
     * 产生随机的数
     * 
     * @param list 总数
     * @param count 需要随机产生的个数
     * @return
     */
    private Set<LinkInfo> getRandomNum(List<LinkInfo> list, int count)
    {
        
        Set<LinkInfo> set = new HashSet<LinkInfo>();
        while (true)
        {
            int a = (int)Math.round(Math.random() * (list.size() - 1));
            set.add(list.get(a));
            if (set.size() >= count)
            {
                break;
            }
        }
        
        return set;
    }
    
    /*
     * public static void main(String[] args) { List<Integer> list=new ArrayList<Integer>(); for(int i=0;i<8;i++){
     * list.add(i); } Set<Integer> lits=new HashSet<Integer>(); while(true){ int a=
     * (int)Math.round(Math.random()*(list.size()-1)); lits.add(list.get(a)); if(lits.size() >=5){ break; } }
     * 
     * }
     */
}
