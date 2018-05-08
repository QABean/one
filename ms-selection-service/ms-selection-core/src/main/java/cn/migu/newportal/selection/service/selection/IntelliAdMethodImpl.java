package cn.migu.newportal.selection.service.selection;

import cn.migu.newportal.cache.bean.book.UserInfoSNS;
import cn.migu.newportal.cache.cache.service.GetUserPreferenceService;
import cn.migu.newportal.cache.cache.service.PortalUserInfoSNSCacheService;
import cn.migu.newportal.cache.util.BeanMergeUtils;
import cn.migu.newportal.cache.util.CommonHttpUtil;
import cn.migu.newportal.cache.util.JsonFormatUtil;
import cn.migu.newportal.selection.bean.local.request.IntelliAdRequest;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode.ErrorCodeAndDesc;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.IntelliAdResponseOuterClass.IntelliAdData;
import cn.migu.selection.proto.base.IntelliAdResponseOuterClass.IntelliAdResponse;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 智能广告（intelligent_advertisement）
 *
 * @author wangqiang
 * @version C10 2018年3月29日
 * @since SDP V300R003C10
 */
public class IntelliAdMethodImpl extends ServiceMethodImpl<IntelliAdResponse, ComponentRequest>
{
    // 日志对象
    private static final Logger LOGGER = LoggerFactory.getLogger(IntelliAdMethodImpl.class);
    
    private static final String METHOD_NAME = "intelliAd";
    
    public IntelliAdMethodImpl()
    {
        super(METHOD_NAME);
    }
    
    /**
     * 智能广告业务处理
     *
     * @author wangqiang
     * @param controller
     * @param request
     * @return
     */
    @ImplementMethod
    public InvokeResult<IntelliAdResponse> intelliAd(ServiceController controller, ComponentRequest request)
    {
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("enter IntelliAdResponse.intelliAd ,identityId:{},ComponentRequest:{} ",
                CommonHttpUtil.getIdentity(),JsonFormatUtil.printToString(request));
        }
        Map<String, String> param = request.getParamMapMap();
        IntelliAdResponse.Builder builder = IntelliAdResponse.newBuilder();
        IntelliAdRequest req = new IntelliAdRequest(param);
        // 组件名称
        builder.setPluginCode(req.getPluginCode());
        IntelliAdData.Builder dataBuilder = IntelliAdData.newBuilder();
        // 设置组件样式
        BeanMergeUtils.setComponentStyle(dataBuilder, param);
        // 封装响应参数
        builderResponseData(dataBuilder,req);

        builder.setData(dataBuilder);
        // 设置组件可见
        BeanMergeUtils.setCommonResponseField(builder, ErrorCodeAndDesc.SUCCESS, ParamConstants.TRUE);
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("exit IntelliAdResponse.intelliAd,identityId:{} ComponentRequest:{}",
                CommonHttpUtil.getIdentity(),
                JsonFormatUtil.printToString(request));
        }
        return new InvokeResult<IntelliAdResponse>(builder.build());
    }
    //封装相应参数
    private void builderResponseData(IntelliAdData.Builder dataBuilder, IntelliAdRequest req)
    {
        // 推荐位编号
        dataBuilder.setRecNum(req.getRecNum());
        // 外部广告id
        dataBuilder.setWzid(req.getWzid());
        // 图片尺寸
        dataBuilder.setPicSize(req.getPicSize());
        // 获取用户省份信息
        getUserProvince(dataBuilder);
        //获取用户偏好
        getUserPreference(dataBuilder);
    }
    /**
     * 获取用户省份信息
     *
     * @author wangqiang
     * @param dataBuilder
     * @return
    */
    public void getUserProvince(IntelliAdData.Builder dataBuilder)
    {
        // 获取用户阅读号
        String msisdn = CommonHttpUtil.getIdentity();
        // 用户省份编码-默认
        dataBuilder.setProvinceId(ParamConstants.PROVINCEID_DEF);
        // 获取用户信息 getUserInfoSNS
        UserInfoSNS userInfoSNS = PortalUserInfoSNSCacheService.getInstance().getUserInfoSNS(msisdn, null);
        if (Util.isNotEmpty(userInfoSNS) && Util.isNotEmpty(userInfoSNS.getProvinceId()))
        {
            dataBuilder.setProvinceId(userInfoSNS.getProvinceId());
        }
    }
    /**
     * 获取用户偏好
     *
     * @author wangqiang
     * @param dataBuilder
     * @return
    */
    private void getUserPreference(IntelliAdData.Builder dataBuilder)
    {
        // 获取用户偏好
        String preference = GetUserPreferenceService.getInstance().getUserPreference();
        if (Util.isNotEmpty(preference))
        {
            dataBuilder.setPrefers(StringTools.nvl(preference.equals(ParamConstants.ERRORCODE) ? "" : preference));
        }
    }

}
