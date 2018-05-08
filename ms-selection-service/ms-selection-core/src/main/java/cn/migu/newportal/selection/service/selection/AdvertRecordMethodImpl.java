package cn.migu.newportal.selection.service.selection;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.migu.newportal.cache.util.CommonHttpUtil;
import cn.migu.newportal.cache.util.JsonFormatUtil;
import cn.migu.newportal.cache.util.MsCdrTools;
import cn.migu.newportal.cache.util.PropertiesConfig;
import cn.migu.newportal.cache.util.UesParamKeyConstants;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.http.UrlTools;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode.ErrorCodeAndDesc;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.sns.proto.base.NewAjaxResponseOuterClass.NewAjaxResponse;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;

/**
 * 记录广告行为话单
 *  仅作记录行为用
 *
 * @author gaojun
 * @version C10 2018年4月16日
 * @since SDP V300R003C10
 */
public class AdvertRecordMethodImpl extends ServiceMethodImpl<NewAjaxResponse, ComponentRequest>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AdvertRecordMethodImpl.class);
    
    private static final String METHOD_NAME = "getAdvertRecord";
    
    public AdvertRecordMethodImpl()
    {
        super(METHOD_NAME);
    }
    
    @ImplementMethod
    public InvokeResult<NewAjaxResponse> getAdvertRecord(ServiceController controller, ComponentRequest request)
    {
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("Enter AdvertRecordMethodImpl.getAdvertRecord(),identityId:{} request:{} ",
                CommonHttpUtil.getIdentity(),
                JsonFormatUtil.printToString(request));
        }
        NewAjaxResponse.Builder builder = NewAjaxResponse.newBuilder();
        try
        {
            Map<String, String> paramMap = request.getParamMapMap();
            String id = StringTools.nvl(paramMap.get(ParamConstants.ADVERTID));
            String advertPlat = StringTools.nvl(paramMap.get(ParamConstants.ONE_ADVERT_PLAT));
            String advertType = StringTools.nvl(paramMap.get(ParamConstants.ADVERTTYPE));
            String advertActionType = StringTools.nvl(paramMap.get(ParamConstants.ADVERTACTIONTYPE));
            
            //中文特殊处理
            advertPlat = advertPlat.replaceAll("\\|",  UrlTools.encode("|"));
            advertPlat = StringTools.subString(advertPlat, 0, 254);
            advertPlat = UrlTools.encode(advertPlat);
            
            MsCdrTools.setActionCdrParam(MsCdrTools.ACTIONTYPE, MsCdrTools.ADVERT);//设置行为编码
            MsCdrTools.setActionCdrParam(MsCdrTools.ERRTYPE, "0");//设置错误类型
            MsCdrTools.setActionCdrParam(MsCdrTools.ERRCODE, "");//设置错误码
            MsCdrTools.setActionCdrExtendsParam(1, id);//拓展属性
            MsCdrTools.setActionCdrExtendsParam(2, advertPlat);
            MsCdrTools.setActionCdrExtendsParam(3, advertType);
            MsCdrTools.setActionCdrExtendsParam(4, advertActionType);
            
            builder.setStatus(ErrorCodeAndDesc.SUCCESS.getCode());
            builder.setMessage(UesParamKeyConstants.ADVERT_RECORD_SUCCESS);         
        }
        catch (Throwable e)
        {
            LOGGER.error(
                "AbetOrCancelAbetActionMethodImpl.abetOrCancelAbetAction has error, request:{}, e:{}",
                JsonFormatUtil.printToString(request),
                e);
            builder.setStatus(ErrorCodeAndDesc.OTHER_DEFAULT_FAIL.getCode());
            builder.setMessage(PropertiesConfig.getProperty(UesParamKeyConstants.NEWPORTAL_BUY_DRAWCARD_OTHER_ERROR));
            return new InvokeResult<NewAjaxResponse>(builder.build());
            
        }
        
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("Exit AdvertRecordMethodImpl.getAdvertRecord(),identityId:{} ", CommonHttpUtil.getIdentity());
        }
        return new InvokeResult<NewAjaxResponse>(builder.build());
    }
    
}
