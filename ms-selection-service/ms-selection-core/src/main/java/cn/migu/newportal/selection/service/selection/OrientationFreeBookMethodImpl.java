package cn.migu.newportal.selection.service.selection;

import java.util.Map;

import cn.migu.newportal.cache.util.CommonHttpUtil;
import cn.migu.newportal.cache.business.user.UserManager;
import cn.migu.newportal.cache.util.BeanMergeUtils;
import cn.migu.newportal.cache.util.JsonFormatUtil;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.http.HttpUtil;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode.ErrorCodeAndDesc;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.OrientationFreeBookResponseOuterClass.OrientationFreeBookData;
import cn.migu.selection.proto.base.OrientationFreeBookResponseOuterClass.OrientationFreeBookResponse;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 综合首页定向免费，组件名 orientation_freebook
 *
 * @author liuchuanzhuang
 * @version C10 2017年12月13日
 * @since SDP V300R003C10
 */
public class OrientationFreeBookMethodImpl extends ServiceMethodImpl<OrientationFreeBookResponse, ComponentRequest>
{
    private static Logger LOGGER = LoggerFactory.getLogger(OrientationFreeBookMethodImpl.class);
    
    private static final String METHOD_NAME = "orientationFreeBook";
    
    public OrientationFreeBookMethodImpl()
    {
        super(METHOD_NAME);
    }
    
    /**
     * 
     * orientation_freebook 组件逻辑
     *
     * @author liuchuanzhuang
     * @param controller
     * @param request
     * @return
     */
    @ImplementMethod
    public InvokeResult<OrientationFreeBookResponse> orientationFreeBook(ServiceController controller,
        ComponentRequest request)
    {
        
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug(
                "Enter OrientationFreeBookMethodImpl.orientationFreeBook() ,identityId:{},ComponentRequest :{}",
                CommonHttpUtil.getIdentity(),
                JsonFormatUtil.printToString(request));
        }
        
        Map<String, String> paramMap = request.getParamMapMap();
        
        OrientationFreeBookResponse.Builder builder = OrientationFreeBookResponse.newBuilder();
        // 设置组件名称
        builder.setPluginCode(StringTools.nvl(paramMap.get(ParamConstants.PLUGINCODE)));
        // 判断是否是游客
        String msisdn = HttpUtil.getHeader(ParamConstants.X_Identity_ID);
        if (UserManager.isGuestUser(msisdn))
        {
            LOGGER.error("OrientationFreeBookMethodImpl.orientationFreeBook() the msisdn is guest ,msisdn:{}", msisdn);
            // 为游客,设置组件不可见
            BeanMergeUtils.setCommonResponseField(builder, ErrorCodeAndDesc.MSISDN_IS_EMPTY, ParamConstants.FALSE);
            return new InvokeResult<OrientationFreeBookResponse>(builder.build());
        }
        OrientationFreeBookData.Builder data = OrientationFreeBookData.newBuilder();
        // 1.标题，ues配置 ,非必需
        data.setTitle(StringTools.nvl(paramMap.get(ParamConstants.TITLE)));
        // 2.描述，ues配置 ,非必需
        data.setDesc(StringTools.nvl(paramMap.get(ParamConstants.DESC)));
        // 3.阅读号
        data.setMsisdn(msisdn);
        // 设置组件样式
        BeanMergeUtils.setComponentStyle(data, paramMap);
        // 设置返回数据
        builder.setData(data);
        // 设置组件可见
        BeanMergeUtils.setCommonResponseField(builder, ErrorCodeAndDesc.SUCCESS, ParamConstants.TRUE);
        
        if (LOGGER.isDebugEnabled())
        
        {
            LOGGER.debug(
                "Exit OrientationFreeBookMethodImpl.orientationFreeBook(),identityId:{}, OrientationFreeBookResponse :{}",
                CommonHttpUtil.getIdentity(),
                JsonFormatUtil.printToString(builder.build()));
        }
        
        return new InvokeResult<OrientationFreeBookResponse>(builder.build());
    }
    
}
