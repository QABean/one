package cn.migu.newportal.selection.service.selection;

import cn.migu.newportal.cache.util.CommonHttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.migu.newportal.cache.util.BeanMergeUtils;
import cn.migu.newportal.cache.util.JsonFormatUtil;
import cn.migu.newportal.cache.util.UesServiceUtils;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.http.UrlTools;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.tools.MSResultCode.ErrorCodeAndDesc;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.ModifyPreferenceEntranceResponseOuterClass.ModifyPreferenceEntranceData;
import cn.migu.selection.proto.base.ModifyPreferenceEntranceResponseOuterClass.ModifyPreferenceEntranceResponse;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;

/**
 * 偏好修改入口_V7preference_modify
 * 
 * @author hanyafei
 * @version C10 2018年1月2日
 * @since SDP V300R003C10
 */
public class ModifyPreferenceEntranceMethodImpl
    extends ServiceMethodImpl<ModifyPreferenceEntranceResponse, ComponentRequest>
{
    private static Logger LOGGER = LoggerFactory.getLogger(ModifyPreferenceEntranceMethodImpl.class);
    
    private static final String METHOD_NAME = "modifyPreferenceEntrance";
    
    public ModifyPreferenceEntranceMethodImpl()
    {
        super(METHOD_NAME);
    }
    
    /**
     * 偏好修改入口rpc方法业务逻辑
     * 
     * @author hanyafei
     * @param controller
     * @param request
     * @return
     */
    @ImplementMethod
    public InvokeResult<ModifyPreferenceEntranceResponse> modifyPreferenceEntrance(ServiceController controller,
        ComponentRequest request)
    {

        LOGGER.debug(
            "enter ModifyPreferenceEntranceMethodImpl.modifyPreferenceEntrance ,identityId:{},ComponentRequest:{} ",
            CommonHttpUtil.getIdentity(),
            JsonFormatUtil.printToString(request));
        
        ModifyPreferenceEntranceResponse.Builder builder = ModifyPreferenceEntranceResponse.newBuilder();
        
        // 获取参数
        builder.setPluginCode(StringUtils.defaultString(request.getParamMapMap().get(ParamConstants.PLUGINCODE)));
        String picUrl = request.getParamMapMap().get(ParamConstants.PIC_URL);
        // 参数校验
        if (Util.isEmpty(picUrl))
        {
            LOGGER.error("GetClassifyNodeListMethodImpl.getClassifyNodeList param is null,identityId:{},picUrl:{}",
                CommonHttpUtil.getIdentity(),
                picUrl);
            BeanMergeUtils.setCommonResponseField(builder, ErrorCodeAndDesc.PARAMNAME_EROOR, ParamConstants.FALSE);
            return new InvokeResult<ModifyPreferenceEntranceResponse>(builder.build());
        }
        
        ModifyPreferenceEntranceData.Builder dataBuilder = ModifyPreferenceEntranceData.newBuilder();
        
        // 设置组件样式
        BeanMergeUtils.setComponentStyle(dataBuilder, request.getParamMapMap());
        
        // 设置并拼接图片前缀
        dataBuilder
            .setPicUrl(StringUtils.defaultString(UrlTools.getRelativelyUrl(picUrl, UesServiceUtils.DEFAULT_DOMAIN)));
        
        builder.setData(dataBuilder.build());
        // 设置组件可见
        BeanMergeUtils.setCommonResponseField(builder, ErrorCodeAndDesc.SUCCESS, ParamConstants.TRUE);
        
        LOGGER.debug(
            "exit ModifyPreferenceEntranceMethodImpl.modifyPreferenceEntrance,identityId:{} ModifyPreferenceEntranceResponse:{}",
            CommonHttpUtil.getIdentity(),JsonFormatUtil.printToString(builder));
        return new InvokeResult<ModifyPreferenceEntranceResponse>(builder.build());
    }
}
