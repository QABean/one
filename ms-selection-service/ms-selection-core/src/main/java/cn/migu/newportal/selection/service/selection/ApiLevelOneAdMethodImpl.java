package cn.migu.newportal.selection.service.selection;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.migu.newportal.cache.business.user.UserManager;
import cn.migu.newportal.cache.util.ActionUrlUtils;
import cn.migu.newportal.cache.util.BeanMergeUtils;
import cn.migu.newportal.cache.util.CommonHttpUtil;
import cn.migu.newportal.cache.util.JsonFormatUtil;
import cn.migu.newportal.cache.util.PropertiesConfig;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.http.HttpUtil;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.PortalUtils;
import cn.migu.newportal.util.tools.MSResultCode.ErrorCodeAndDesc;
import cn.migu.selection.proto.base.ApiLevelOneAdResponseOuterClass.ApiLevelOneAdData;
import cn.migu.selection.proto.base.ApiLevelOneAdResponseOuterClass.ApiLevelOneAdResponse;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;

/**
 * 一级平台广告
 * 
 *
 * @author gaojun
 * @version C10 2018年4月13日
 * @since SDP V300R003C10
 */
public class ApiLevelOneAdMethodImpl extends ServiceMethodImpl<ApiLevelOneAdResponse, ComponentRequest>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiLevelOneAdMethodImpl.class);
    
    private static final String METHOD_NAME = "getApiLevelOneAd";
    
    // 曝光
    private static final String ISBAO = "1";
    
    // 不曝光
    private static final String ISNOBAO = "0";
    
    // 广告刷分
    private static final String BGBFBDEF = "0";  
    
    public ApiLevelOneAdMethodImpl()
    {
        super(METHOD_NAME);
    }
    
    @ImplementMethod
    public InvokeResult<ApiLevelOneAdResponse> getApiLevelOneAd(ServiceController controller, ComponentRequest request)
    {
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("Enter ApiLevelOneAdMethodImpl.getApiLevelOneAd(),identityId:{} request:{} ",
                CommonHttpUtil.getIdentity(),
                JsonFormatUtil.printToString(request));
        }
        ApiLevelOneAdResponse.Builder builder = ApiLevelOneAdResponse.newBuilder();
        
        Map<String, String> paramMap = request.getParamMapMap();
        builder.setPluginCode(PortalUtils.defaultString(paramMap.get(ParamConstants.PLUGINCODE)));
        
        try
        {
            builder.setData(createApiLevelOneAdData(paramMap));
            // 设置组件可见
            BeanMergeUtils.setCommonResponseField(builder, ErrorCodeAndDesc.SUCCESS, ParamConstants.TRUE);
        }
        catch (Exception e)
        {
            LOGGER.error("ApiLevelOneAdMethodImpl.getApiLevelOneAd()  failed,identityId:{},e:{}",
                CommonHttpUtil.getIdentity(),
                e);
            BeanMergeUtils.setCommonResponseField(builder, ErrorCodeAndDesc.RESPONSE_ERROR, ParamConstants.FALSE);
            return new InvokeResult<ApiLevelOneAdResponse>(builder.build());
        }
        
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("Exit ApiLevelOneAdMethodImpl.getApiLevelOneAd(),identityId:{} response:{} ",
                CommonHttpUtil.getIdentity(),
                JsonFormatUtil.printToString(builder));
        }
        return new InvokeResult<ApiLevelOneAdResponse>(builder.build());
    }
    
    /**
     * data数据封装
     * 
     *
     * @author gaojun
     * @param paramMap
     * @return
     */
    private ApiLevelOneAdData createApiLevelOneAdData(Map<String, String> paramMap)
    {
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("enter ApiLevelOneAdMethodImpl.createApiLevelOneAdData(),identityId:{}",
                CommonHttpUtil.getIdentity());
        }
        ApiLevelOneAdData.Builder apiLevelOneAdData = ApiLevelOneAdData.newBuilder();
        // appid
        apiLevelOneAdData.setAppid(StringTools.nvl(paramMap.get(ParamConstants.APPID)));
        // 平台授权authid
        apiLevelOneAdData.setAuthid(StringTools.nvl(paramMap.get(ParamConstants.AUTHID)));
        // 广告位id
        apiLevelOneAdData.setId(StringTools.nvl(paramMap.get(ParamConstants.ONE_ADVERT_ID)));
        // 行为类型 0:全部;1:跳转;2:下载;
        apiLevelOneAdData.setActiontype(StringTools.nvl(paramMap.get(ParamConstants.ACTIONTYPE)));
        // token
        apiLevelOneAdData.setToken(StringTools.nvl(paramMap.get(ParamConstants.TOKEN)));
        // 是否只是曝光，1是
        apiLevelOneAdData
            .setIsbaoNoShow(ISBAO.equals(paramMap.get(ParamConstants.IS_BAO_NO_SHOW)) ? ISBAO : ISNOBAO);
        // 是否点击曝光，1:是;0:否;默认0
        apiLevelOneAdData
            .setIsClickBaoguang(ISBAO.equals(paramMap.get(ParamConstants.ISCLICKBAOGUANG)) ? ISBAO : ISNOBAO);
        // 广告刷量百分之多少，范围0~100，默认0
        apiLevelOneAdData.setBgbfb(StringTools.nvl2(paramMap.get(ParamConstants.BGBFB), BGBFBDEF));
        // 手机号传输方式，0:明文;1:私有加密;
        String pnumberha = StringTools.nvl(paramMap.get(ParamConstants.PNUMBERHA));
        apiLevelOneAdData.setPnumberha(pnumberha);
        // 获取用户手机号
        String msisdn = HttpUtil.getHeader(ParamConstants.X_Identity_ID);
        String pnumber = UserManager.getPhoneNumberByTransferType(pnumberha, msisdn);  
        apiLevelOneAdData.setPnumber(pnumber);
        
        // ajax 请求出封装 拼接 参数
        String id = StringTools.nvl(paramMap.get(ParamConstants.ADVERTID));
        String advertPlat = PropertiesConfig.getProperty(ParamConstants.ONE_ADVERT_PLAT);
        String advertType = "1"; 
        String advertActionType = "1";
        String ajaxAdvertRecord = ActionUrlUtils.getAdvertRecord(id, advertPlat, advertType, advertActionType);
        apiLevelOneAdData.setAjaxAdvertRecord(ajaxAdvertRecord);
        
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("exit ApiLevelOneAdMethodImpl.createApiLevelOneAdData(),identityId:{} ",
                CommonHttpUtil.getIdentity());
        }
        // 样式属性设置
        BeanMergeUtils.setComponentStyle(apiLevelOneAdData, paramMap);
        return apiLevelOneAdData.build();
    }
}
