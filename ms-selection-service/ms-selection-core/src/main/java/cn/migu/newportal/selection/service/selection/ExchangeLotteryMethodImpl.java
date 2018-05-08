package cn.migu.newportal.selection.service.selection;


import cn.migu.compositeservice.campaignservice.ProcessLottery.ProcessLotteryResponse;
import cn.migu.newportal.cache.util.CommonHttpUtil;
import cn.migu.newportal.cache.util.JsonFormatUtil;
import cn.migu.newportal.selection.engine.ContentServiceEngine;
import cn.migu.newportal.selection.util.SelectionConstants;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.constants.Types;
import cn.migu.newportal.util.exception.PortalException;
import cn.migu.newportal.util.http.HttpUtil;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode;
import cn.migu.selection.proto.base.CommonAjaxResponseOuterClass.CommonAjaxResponse;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 抽奖事件
 *
 * @author hushanqing
 * @version C10 2017年9月27日
 * @since SDP V300R003C10
 */
public class ExchangeLotteryMethodImpl extends ServiceMethodImpl<CommonAjaxResponse, ComponentRequest>
{
    /** 日志对象 */
    private static final Logger logger = LoggerFactory.getLogger(ExchangeLotteryMethodImpl.class);

    /** 方法名称 */
    private static final String METHOD_NAME = "exchangeLottery";

    public ExchangeLotteryMethodImpl()
    {
        super(METHOD_NAME);
    }

    /**
     * 抽奖处理
     *
     * @author hushanqing
     * @param controller
     * @param request
     * @return
     */
    @ImplementMethod
    public InvokeResult<CommonAjaxResponse> exchangeLottery(ServiceController controller, ComponentRequest request)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("Enter GetGeneralHeadingMethodImpl.getGeneralHeading(),identityId:{} request:{} ",
                CommonHttpUtil.getIdentity(),
                JsonFormatUtil.printToString(request));
        }
        CommonAjaxResponse.Builder builder = CommonAjaxResponse.newBuilder();

        // 抽奖活动id
        String lotteryId = request.getParamMapMap().get(ParamConstants.LOTTERYID);
        // 任务id
        String missionId = request.getParamMapMap().get(ParamConstants.MISSIONID);
        // 门户类型
        String portalType = "1";
        // 渠道编码
        String channelCode = request.getParamMapMap().get(ParamConstants.PARAM_CHANNEL);// 如果为空 =0000

        // 用户终端的ua信息
        String userAgent = HttpUtil.getHeader(ParamConstants.User_Agent);

        String msisdn = HttpUtil.getHeader(ParamConstants.X_Identity_ID);
        // 1点击抽奖，2摇一摇抽奖
        String isShake = request.getParamMapMap().get(ParamConstants.SHAKE_LOTTERY_KEY);

        if (StringTools.isEq(isShake, SelectionConstants.SHAKE_LOTTERY))
        {
            // TODO 加入行为话单

            ProcessLotteryResponse resp = null;
            try
            {
                resp = ContentServiceEngine.getInstance().processLottery("",
                    portalType,
                    msisdn,
                    lotteryId,
                    channelCode,
                    userAgent,
                    isShake,
                    missionId);
            }
            catch (PortalException e)
            {
                logger.debug(
                    "call ContentServiceEngine.processLottery() failed,identityId:{} |lotteryId:{} |portalType:{} |msisdn:{}|channelCode:{}|userAgent:{}|isShake:{}|missionId:{}",
                    CommonHttpUtil.getIdentity(),
                    lotteryId,
                    portalType,
                    msisdn,
                    channelCode,
                    userAgent,
                    isShake,
                    missionId);
                builder.setResultCode(MSResultCode.ErrorCodeAndDesc.RESPONSE_ERROR.getCode());
                builder.setCodeDesc(MSResultCode.ErrorCodeAndDesc.RESPONSE_ERROR.getDesc());
                return new InvokeResult<CommonAjaxResponse>(builder.build());
            }

            if (Util.isEmpty(resp))
            {
                builder.setResultCode(MSResultCode.ErrorCodeAndDesc.RESPONSE_IS_NULL.getCode());
                builder.setCodeDesc(MSResultCode.ErrorCodeAndDesc.RESPONSE_IS_NULL.getDesc());
                return new InvokeResult<CommonAjaxResponse>(builder.build());
            }
            // TODO 与组合服务确认errorCode所在位置是在响应体还是在
            if (Util.isNotEmpty(resp.getErrCode()))
            {

                builder.setResultCode(resp.getErrCode());
                builder.setCodeDesc(resp.getErrValue());
                return new InvokeResult<CommonAjaxResponse>(builder.build());
            }

            if (StringTools.isEq(resp.getIsWinning(), Types.TRUE))
            {
                builder.setResultCode(MSResultCode.ErrorCodeAndDesc.SUCCESS.getCode());
            }

        }

        if (logger.isDebugEnabled())
        {
            logger.debug("Exit GetGeneralHeadingMethodImpl.getGeneralHeading(),identityId:{} response:{} ",
                CommonHttpUtil.getIdentity(),
                JsonFormatUtil.printToString(builder));
        }
        return new InvokeResult<CommonAjaxResponse>(builder.build());

    }
}
