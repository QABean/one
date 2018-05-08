package cn.migu.newportal.selection.service.selection;

import java.util.Map;

import cn.migu.newportal.cache.util.CommonHttpUtil;
import com.googlecode.protobuf.format.JsonFormat;

import cn.migu.newportal.cache.cache.service.GetUserPreferenceService;
import cn.migu.newportal.cache.util.BeanMergeUtils;
import cn.migu.newportal.cache.util.JsonFormatUtil;
import cn.migu.newportal.cache.util.UesServiceUtils;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.constants.SystemConstants;
import cn.migu.newportal.util.http.UrlTools;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode.ErrorCodeAndDesc;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.PreferenceLinksResponseOuterClass.PreferenceLinksData;
import cn.migu.selection.proto.base.PreferenceLinksResponseOuterClass.PreferenceLinksResponse;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 偏好排序展示 preference_ranklist
 * 
 *
 * @author liuchuanzhuang
 * @version C10 2017年12月13日
 * @since SDP V300R003C10
 */
public class PreferenceLinksMethodImpl extends ServiceMethodImpl<PreferenceLinksResponse, ComponentRequest>
{
    private static Logger LOGGER = LoggerFactory.getLogger(PreferenceLinksMethodImpl.class);
    
    private static final String METHOD_NAME = "preferenceLinks";
    
    /** 1:按偏好排列 */
    private static final String ARRAY = "1";
    
    /** 2:按偏好展示 */
    private static final String SHOW = "2";
    
    public PreferenceLinksMethodImpl()
    {
        super(METHOD_NAME);
    }
    
    /**
     * 
     * preference_ranklist 组件逻辑
     *
     * @author liuchuanzhuang
     * @param controller
     * @param request
     * @return
     */
    @ImplementMethod
    public InvokeResult<PreferenceLinksResponse> preferenceLinks(ServiceController controller, ComponentRequest request)
    {
        
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("Enter PreferenceLinksMethodImpl.preferenceLinks(),identityId:{}, ComponentRequest :{}"
                , CommonHttpUtil.getIdentity(), JsonFormat.printToString(request));
        }
        
        Map<String, String> paramMap = request.getParamMapMap();
        UesServiceUtils.setPublicParamToRequest(paramMap);
        PreferenceLinksResponse.Builder builder = PreferenceLinksResponse.newBuilder();
        // 设置组件名称
        builder.setPluginCode(StringTools.nvl(paramMap.get(ParamConstants.PLUGINCODE)));
        // 不用校验游客
        // 排序规则，1:按偏好排列，2:按偏好展示 ，ues配置 ,非必需 ,默认 1
        String rule = StringTools.nvl(paramMap.get(ParamConstants.RULE));
        rule = (SHOW.equals(rule)) ? rule : ARRAY;
        
        PreferenceLinksData.Builder data = PreferenceLinksData.newBuilder();
        // data封裝
        try
        {
            String preference = GetUserPreferenceService.getInstance().getUserPreference();
            // DataLoader没有数据返回异常 -1 设置返回值为空 GetUserPreferenceDataLoader
            data.setPrefers((SystemConstants.USER_PREFERENCE_IS_NULL_OR_ERROR.equals(preference)) ? "" : StringTools.nvl(preference));
            data.setRule(rule);
            
            // 拼接
            Map<String, String> params = UesServiceUtils.buildPublicParaMap(null, null);
            
            data.setNanUrl(StringTools.nvl(UrlTools.processForView(
                UrlTools.getRelativelyUrl(paramMap.get(ParamConstants.NAN_URL), UesServiceUtils.DEFAULT_DOMAIN),
                params)));
            data.setNvUrl(StringTools.nvl(UrlTools.processForView(
                UrlTools.getRelativelyUrl(paramMap.get(ParamConstants.NV_URL), UesServiceUtils.DEFAULT_DOMAIN),
                params)));
            data.setCbUrl(StringTools.nvl(UrlTools.processForView(
                UrlTools.getRelativelyUrl(paramMap.get(ParamConstants.CB_URL), UesServiceUtils.DEFAULT_DOMAIN),
                params)));
            data.setCyUrl(StringTools.nvl(UrlTools.processForView(
                UrlTools.getRelativelyUrl(paramMap.get(ParamConstants.CY_URL), UesServiceUtils.DEFAULT_DOMAIN),
                params)));
            
            // 设置组件样式
            BeanMergeUtils.setComponentStyle(data, request.getParamMapMap());
            // 设置返回数据
            builder.setData(data);
            // 设置组件可见
            BeanMergeUtils.setCommonResponseField(builder, ErrorCodeAndDesc.SUCCESS, ParamConstants.TRUE);
        }
        catch (Exception e)
        {
            LOGGER.error("PreferenceLinksMethodImpl.preferenceLinks() failed, identityId:{},error Message:{},Request:{}",
                CommonHttpUtil.getIdentity(),
                e.getMessage(),
                JsonFormatUtil.printToString(request));
            // 设置组件不可见
            BeanMergeUtils.setCommonResponseField(builder, ErrorCodeAndDesc.OTHER_DEFAULT_FAIL, ParamConstants.FALSE);
            return new InvokeResult<PreferenceLinksResponse>(builder.build());
        }
        
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("Exit PreferenceLinksMethodImpl.preferenceLinks(),identityId:{}, PreferenceLinksResponse :{}",
                CommonHttpUtil.getIdentity(), JsonFormat.printToString(builder.build()));
        }
        
        return new InvokeResult<PreferenceLinksResponse>(builder.build());
    }
    
}
