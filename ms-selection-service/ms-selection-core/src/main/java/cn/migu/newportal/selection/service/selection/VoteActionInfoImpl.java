package cn.migu.newportal.selection.service.selection;

import cn.migu.compositeservice.campaignservice.Balloted.BallotedResponse;
import cn.migu.newportal.cache.util.*;
import cn.migu.newportal.selection.engine.ContentServiceEngine;
import cn.migu.newportal.selection.engine.UESServiceEngine;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.date.DateTools;
import cn.migu.newportal.util.exception.PortalException;
import cn.migu.newportal.util.http.HttpUtil;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode.ErrorCodeAndDesc;
import cn.migu.selection.proto.base.CommonResponseOuterClass.CommonResponse;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.VoteActionResponseOuterClass.VoteActionResponse;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import com.huawei.iread.server.constant.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * 投票action处理
 *
 * @author yejiaxu
 * @version C10 2017年7月3日
 * @since SDP V300R003C10
 */
public class VoteActionInfoImpl extends ServiceMethodImpl<VoteActionResponse, ComponentRequest>
{
    // 日志对象
    private static final Logger LOG = LoggerFactory.getLogger(VoteActionInfoImpl.class.getName());

    private static final String METHOD_NAME = "getVoteAction";

    @Resource
    private UESServiceEngine uesServiceEngine;

    public VoteActionInfoImpl()
    {
        super(METHOD_NAME);
    }

    /**
     * 投票动作
     *
     * @author yejiaxu
     * @param controller
     * @param request
     * @return
     * @throws PortalException
     */
    @ImplementMethod
    public InvokeResult<CommonResponse> getVoteAction(ServiceController controller, ComponentRequest request)
        throws PortalException
    {
        if (LOG.isDebugEnabled())
        {
            LOG.debug("enter VoteActionInfoImpl-getVoteAction，identityId:{} ComponentRequest :{}"
                , CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(request));
        }
        CommonResponse.Builder builder = CommonResponse.newBuilder();
        // 投票活动id
        String ballotId = request.getParamMapMap().get(ParamConstants.BALLOTID);
        // 投票选项id
        String itemid = request.getParamMapMap().get(ParamConstants.ITEMID);
        // 投票选项id
        String bid = request.getParamMapMap().get(ParamConstants.BID);
        // 获取用户信息
        String msisdn = HttpUtil.getHeader(ParamConstants.X_Identity_ID);
        if (StringTools.isEmpty(ballotId) || StringTools.isEmpty(itemid) || StringTools.isEmpty(msisdn))
        {
            builder.setStatus(ErrorCodeAndDesc.BALLOTID_IS_EMPTY.BALLOTID_IS_EMPTY.getCode());
            builder.setMessage(ErrorCodeAndDesc.BALLOTID_IS_EMPTY.BALLOTID_IS_EMPTY.getDesc());
            return new InvokeResult<CommonResponse>(builder.build());
        }
        BallotedResponse ballotResult = null;
        try
        {
            ballotResult =
                ContentServiceEngine.getInstance().balloted(msisdn, ballotId, itemid);
            buildDate( ballotResult, builder);

        }
        catch (PortalException e)
        {
            LOG.error("VoteActionInfo buildResponse error,identityId:{},errorCode:{},e:{}",
                CommonHttpUtil.getIdentity(),
                e.getErrorCode(),
                e);
        }
        String resultCode = null;
        if(Util.isNotEmpty(ballotResult))
        {
            resultCode = String.valueOf(ballotResult.getResultCode());
        }
        recordMsCdr( bid, ballotId, itemid, resultCode);
        if (LOG.isDebugEnabled())
        {

            LOG.debug("exit voteActionInfoImpl.getVoteAction,identityId:{} ,response:{}",
                CommonHttpUtil.getIdentity(),
                JsonFormatUtil.printToString(builder));
        }
        return new InvokeResult<CommonResponse>(builder.build());
    }

    /*/**
     *
     * 构建响应描述
     * @author zhangmm
     * @date 2018/3/29 15:17
     * @param ballotResult
     * @param builder
     * @return
    */
    private void buildDate(BallotedResponse ballotResult, CommonResponse.Builder builder)
    {
        if (Util.isNotEmpty(ballotResult))
        {
            int resultCode = ballotResult.getResultCode();
            String beginTime = ballotResult.getBeginTime();
            String limit = ballotResult.getLimit();
            String message = PropertiesConfig.getProperty("vote_failed");
            // 投票成功
            if (resultCode == ResultCode.SUCCESS)
            {
                message = UesServerServiceUtils.getUesParamConfig("newportal_ballot_successful_show_msg", null);
            }
            // 选项已投过
            else if (resultCode == ResultCode.BALLOTED_ITEM_COUNT_DAY)
            {
                message = UesServerServiceUtils.getUesParamConfig("newportal_ballot_item_voted_desc", null)
                    .replace("{N}", limit);
            }
            // 每天每个用户单个投票项投票次数已达上限
            else if (resultCode == ResultCode.BALLOTED_COUNT_MAX)
            {
                message = UesServerServiceUtils.getUesParamConfig("newportal_ballot_vote_num_desc", null).replace("{N}",
                    limit);
            }
            // 每个用户单个投票项总投票次数已达上限
            else if (resultCode == ResultCode.BALLOTED_ITEM_COUNT_MAX)
            {
                message = UesServerServiceUtils.getUesParamConfig("newportal_ballot_all_vote_num_desc", null)
                    .replace("{N}", limit);
            }
            // 投票已结束
            else if (resultCode == ResultCode.BALLOTED_IS_END)
            {
                message = UesServerServiceUtils.getUesParamConfig("newportal_ballot_finished_desc", null);
            }
            // 投票未开始
            else if (resultCode == ResultCode.BALLOTED_NO_EFFECT || resultCode == ResultCode.BALLOTED_NO_START)
            {
                message = UesServerServiceUtils.getUesParamConfig("newportal_ballot_unbegin_desc", null).replace("{N}",
                    DateTools.timeTransform(beginTime, DateTools.DATE_FORMAT_14, DateTools.DATE_FORMAT_24HOUR_16));
            }
            builder.setStatus(String.valueOf(resultCode));
            builder.setMessage(StringTools.nvl(message));
        }
    }


    /*/**
     *
     * 记录行为话单
     * @author zhangmm
     * @date 2018/3/29 15:06
     * @param bookid
     * @param ballotid
     * @param itemid
     * @param resultCode
     * @return
    */
    private void recordMsCdr(String bookid, String ballotid, String itemid, String resultCode)
    {
        MsCdrTools.setActionCdrParam(MsCdrTools.ACTIONTYPE, MsCdrTools.VOTEACTION);
        MsCdrTools.setActionCdrExtendsParam(1, bookid);
        MsCdrTools.setActionCdrExtendsParam(2, ballotid);
        MsCdrTools.setActionCdrExtendsParam(3, itemid);
        MsCdrTools.setActionCdrExtendsParam(4, resultCode);
    }
}
