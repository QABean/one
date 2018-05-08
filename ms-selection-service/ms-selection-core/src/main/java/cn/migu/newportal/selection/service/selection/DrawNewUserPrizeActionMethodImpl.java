package cn.migu.newportal.selection.service.selection;

import cn.migu.newportal.cache.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.migu.newportal.cache.business.user.UserManager;
import cn.migu.newportal.cache.cache.engine.UsersnsConsumerEngine;
import cn.migu.compositeservice.usersnsservice.NewUserTicketReceive.NewUserTicketReceiveReponse;
import cn.migu.compositeservice.usersnsservice.NewUserTicketReceive.NewUserTicketReceiveRequest;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.exception.PortalException;
import cn.migu.newportal.util.http.HttpUtil;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode.ErrorCodeAndDesc;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.NewUserPrizeActionResponseOuterClass.NewUserPrizeActionResponse;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;

/**
 * 新用户领礼品action
 *
 * @author Li Bin
 * @version C10 2017年12月13日
 * @since SDP V300R003C10
 */
public class DrawNewUserPrizeActionMethodImpl extends ServiceMethodImpl<NewUserPrizeActionResponse, ComponentRequest>
{
    // 日志信息
    private static Logger LOGGER = LoggerFactory.getLogger(DrawNewUserPrizeActionMethodImpl.class);
    
    // 方法名称
    private static final String METHOD_NAME = "drawNewUserPrize";
    
    @Autowired
    private UsersnsConsumerEngine userSnsConsumerEngine;
    
    /** 领取成功 */
    private String RESULTCODE_ZERO = "0";
    
    private String RESULTCODE_TWOHONDRED = "200";
    
    /** 重复领取 */
    private String REWARD_REPEAT = "4624";
    
    public DrawNewUserPrizeActionMethodImpl()
    {
        super(METHOD_NAME);
    }
    
    /**
     * 新用户领礼品action————业务逻辑
     * 
     * @author Li Bin
     * @param controller
     * @param request
     * @return
     */
    @ImplementMethod
    public InvokeResult<NewUserPrizeActionResponse> drawNewUserPrizeAction(ServiceController controller,
        ComponentRequest request)
    {
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("Enter DrawNewUserPrizeActionMethodImpl.drawNewUserPrizeAction ,identityId:{} Request :{}",
                CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(request));
        }
        NewUserPrizeActionResponse.Builder builder = NewUserPrizeActionResponse.newBuilder();
        String msisdn = HttpUtil.getHeader(ParamConstants.X_Identity_ID);
        // 校验游客
        if (UserManager.isGuestUser(msisdn))
        {
            BeanMergeUtils.setCommonResponseField(builder, ErrorCodeAndDesc.MSISDN_IS_EMPTY, ParamConstants.FALSE);
            return new InvokeResult<NewUserPrizeActionResponse>(builder.build());
        }
        NewUserTicketReceiveRequest.Builder newUserTicketReceiveRequest = NewUserTicketReceiveRequest.newBuilder();
        newUserTicketReceiveRequest.setMsisdn(msisdn);
        String resultMessage = null;
        String errorCode = null;
        try
        {
            NewUserTicketReceiveReponse response =
                userSnsConsumerEngine.getNewUserTicketReceive.getCompositeResponse(newUserTicketReceiveRequest.build());
            if (StringUtils.isNotBlank(response.getResultcode()))
            {
                String resultcode = response.getResultcode();
                if (StringTools.isEq(resultcode, RESULTCODE_ZERO)
                    || StringTools.isEq(resultcode, RESULTCODE_TWOHONDRED))
                {
                    // 领取成功提示语
                    resultMessage = UesServerServiceUtils.getUesParamConfig(UesParamKeyConstants.DRAW_SUCCESS, "");
                    creatNewUserPrizeActionResponse(builder, resultcode, ErrorCodeAndDesc.DRAW_SUCCESS, resultMessage);
                }
                else
                {
                    // 領取失敗提示語
                    resultMessage = UesServerServiceUtils.getUesParamConfig(UesParamKeyConstants.DRAW_FAIL, "");
                    creatNewUserPrizeActionResponse(builder, errorCode, ErrorCodeAndDesc.DRAW_FAIL, resultMessage);
                }
            }
        }
        catch (PortalException e)
        {
            errorCode = String.valueOf(e.getErrorCode());
            LOGGER.error("userSnsConsumerEngine.getNewUserTicketReceive has error e:{},msisdn:{}",
                e.getMessage(),
                msisdn);
            if (StringTools.isEq(errorCode, REWARD_REPEAT))
            {
                // 重复领取提示语
                resultMessage = UesServerServiceUtils.getUesParamConfig(UesParamKeyConstants.DRAW_REPEAT, "");
                creatNewUserPrizeActionResponse(builder, errorCode, ErrorCodeAndDesc.DRAW_REPEAT, resultMessage);
            }
            else
            {
                // 領取失敗提示語
                resultMessage = UesServerServiceUtils.getUesParamConfig(UesParamKeyConstants.DRAW_FAIL, "");
                creatNewUserPrizeActionResponse(builder, errorCode, ErrorCodeAndDesc.DRAW_FAIL, resultMessage);
            }
        }
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug(
                "exit DrawNewUserPrizeActionMethodImpl.drawNewUserPrizeAction,identityId:{}, NewUserPrizeActionResponse :{}",
                CommonHttpUtil.getIdentity(),
                JsonFormatUtil.printToString(builder.build()));
        }
        return new InvokeResult<NewUserPrizeActionResponse>(builder.build());
    }
    
    /**
     * 数据回显并提示
     * 
     * @author Li Bin
     * @param resultCode 响应码，0为成功，其他为失败
     * @param codeDesc 响应码描述
     * @param resultMsg 提示语，展示给用户的提示语
     */
    private void creatNewUserPrizeActionResponse(NewUserPrizeActionResponse.Builder builder, String resultCode,
        ErrorCodeAndDesc codeDesc, String resultMsg)
    {
        builder.setResultCode(resultCode);
        builder.setCodeDesc(StringTools.nvl(codeDesc.getDesc()));
        builder.setResultMsg(StringTools.nvl(resultMsg));
    }
}
