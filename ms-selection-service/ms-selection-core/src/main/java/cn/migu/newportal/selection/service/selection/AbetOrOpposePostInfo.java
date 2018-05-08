package cn.migu.newportal.selection.service.selection;

import cn.migu.newportal.cache.util.CommonHttpUtil;
import cn.migu.newportal.cache.util.JsonFormatUtil;
import org.apache.commons.lang.StringUtils;


import cn.migu.compositeservice.forumsnsservice.AbetOrOpposePost.AbetOrOpposePostRequest;
import cn.migu.newportal.cache.util.GetSpringContext;
import cn.migu.newportal.cache.util.PropertiesConfig;
import cn.migu.newportal.util.cache.util.CachedDAO;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.http.HttpUtil;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode;
import cn.migu.selection.proto.base.AbetOrOpposePostResponseOuterClass.AbetOrOpposePostResponse;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import cn.migu.wheat.service.ServiceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbetOrOpposePostInfo extends ServiceMethodImpl<AbetOrOpposePostResponse, ComponentRequest>
{
    // 日志对象
    private static Logger logger = LoggerFactory.getLogger(AbetOrOpposePostInfo.class.getName());
    
    private static final String METHOD_NAME = "abetOrOpposePostInfo";
    
    public AbetOrOpposePostInfo()
    {
        super(METHOD_NAME);
    }
    
    @ImplementMethod
    public InvokeResult<AbetOrOpposePostResponse> v1(ServiceController controller, ComponentRequest request)
    {
        logger.info("enter AbetOrOpposePostInfo...identityId:{} ComponentRequest:{}", CommonHttpUtil.getIdentity(),
            JsonFormatUtil.printToString(request));
        AbetOrOpposePostResponse.Builder builder = AbetOrOpposePostResponse.newBuilder();
        builder.setStatus(MSResultCode.ErrorCodeAndDesc.CONNECTION_FAILED.getCode());
        builder.setMessageDesc("点赞失败");
        // 从请求消息头里取出用户手机号
        String msisdn = HttpUtil.getHeader(ParamConstants.MSISDN);
        // 目前pg的请求头带到ms后获取不到, 手机号从请求参数中获取
        if (msisdn == null)
            msisdn = request.getParamMapMap().get(ParamConstants.MSISDN);
        // 帖子id
        String pid = request.getParamMapMap().get(ParamConstants.PID);
        // 操作行为，1顶，2驳
        String action = request.getParamMapMap().get(ParamConstants.ACTION);
        
        if (StringTools.isEmpty(msisdn) || StringTools.isEmpty(pid) || StringTools.isEmpty(action))
        {
            logger.error("params error message,identityId:{},msisdn:{} or pid:{} or action:{} is empty",
                CommonHttpUtil.getIdentity(),
                msisdn,
                pid,
                action);
            return new InvokeResult<AbetOrOpposePostResponse>(builder.build());
        }
        
        AbetOrOpposePostRequest.Builder requestBuilder = AbetOrOpposePostRequest.newBuilder();
        requestBuilder.setMsisdn(msisdn);
        requestBuilder.setPid(pid);
        requestBuilder.setAction(action);
        try
        {
            int abetOrOppostPostMode = PropertiesConfig.getInt("abetOrOppostPostMode", 0);
            if (abetOrOppostPostMode == 0)
            {
                builder.setStatus(MSResultCode.ErrorCodeAndDesc.SUCCESS.getCode());
                builder.setMessageDesc("点赞成功");
                CachedDAO.pool.execute(new Runnable()
                {
                    public void run()
                    {
                        int count = 0;
                        int defaultCount = PropertiesConfig.getInt("defaultCount", 3);
                        ServiceResult<cn.migu.compositeservice.forumsnsservice.AbetOrOpposePost.AbetOrOpposePostResponse> resultResponse =
                            null;
                        while (resultResponse == null)
                        {
                            
                            try
                            {
                                resultResponse =
                                    GetSpringContext.getCompositeEngine().abetOrOpposePost(requestBuilder.build());
                                if (count < defaultCount)
                                {
                                    count++;
                                }
                                else
                                {
                                    break;
                                }
                            }
                            catch (Exception e)
                            {
                                logger.error("CompositeEngine.abetOrOpposePost failed,identityId:{},param:{},e:{}",
                                    CommonHttpUtil.getIdentity(),
                                    JsonFormatUtil.printToString(requestBuilder),
                                    e);
                                if (count < defaultCount)
                                {
                                    count++;
                                }
                                else
                                {
                                    break;
                                }
                            }
                        }
                    }
                });
            }
            else
            {
                ServiceResult<cn.migu.compositeservice.forumsnsservice.AbetOrOpposePost.AbetOrOpposePostResponse> resultResponse =
                    GetSpringContext.getCompositeEngine().abetOrOpposePost(requestBuilder.build());
                if (resultResponse != null && (resultResponse.getCode() == 200 || resultResponse.getCode() == 0))
                {
                    builder.setStatus(MSResultCode.ErrorCodeAndDesc.SUCCESS.getCode());
                    builder.setMessageDesc("点赞成功");
                    builder.setIsShowline(
                        StringUtils.defaultString(request.getParamMapMap().get(ParamConstants.ISSHOWLINE)));
                    builder.setIsMarginTop(
                        StringUtils.defaultString(request.getParamMapMap().get(ParamConstants.ISMARGINTOP)));
                    builder.setIsMarginBottom(
                        StringUtils.defaultString(request.getParamMapMap().get(ParamConstants.ISMARGINBOTTOM)));
                }
            }
        }
        catch (Exception e)
        {
            logger.error("CompositeEngine.abetOrOpposePost failed,identityId:{},param:{},e:{}",
                CommonHttpUtil.getIdentity(),
                JsonFormatUtil.printToString(requestBuilder),
                e);
        }
        return new InvokeResult<AbetOrOpposePostResponse>(builder.build());
    }
    
}
