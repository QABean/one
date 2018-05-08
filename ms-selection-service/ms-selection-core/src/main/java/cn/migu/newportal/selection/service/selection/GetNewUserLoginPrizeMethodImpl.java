package cn.migu.newportal.selection.service.selection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.migu.newportal.cache.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.migu.newportal.cache.business.user.UserManager;
import cn.migu.newportal.cache.cache.engine.UsersnsConsumerEngine;
import cn.migu.compositeservice.usersnsservice.GetNewUserTicketRecords.TicketRecord;
import cn.migu.compositeservice.usersnsservice.GetNewUserTicketRecords.UserTicketRecordsRequest;
import cn.migu.compositeservice.usersnsservice.GetNewUserTicketRecords.UserTicketRecordsResponse;
import cn.migu.compositeservice.usersnsservice.IsNewUser.IsNewUserRequest;
import cn.migu.compositeservice.usersnsservice.IsNewUser.IsNewUserResponse;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.exception.PortalException;
import cn.migu.newportal.util.http.HttpUtil;
import cn.migu.newportal.util.http.UrlTools;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode.ErrorCodeAndDesc;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.NewUserLoginPrizeResponseOuterClass.NewUserLoginPrize;
import cn.migu.selection.proto.base.NewUserLoginPrizeResponseOuterClass.NewUserLoginPrizeResponse;
import cn.migu.selection.proto.base.NewUserLoginPrizeResponseOuterClass.TicketRecords;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;

/**
 * 新用户领新礼____newuser_loginprize
 *
 * @author Li Bin
 * @version C10 2017年12月15日
 * @since SDP V300R003C10
 */
public class GetNewUserLoginPrizeMethodImpl extends ServiceMethodImpl<NewUserLoginPrizeResponse, ComponentRequest>
{
    // 日志信息
    private static Logger LOGGER = LoggerFactory.getLogger(GetNewUserLoginPrizeMethodImpl.class);
    
    // 方法名称
    private static final String METHOD_NAME = "newUserLoginPrize";
    
    @Autowired
    private UsersnsConsumerEngine userSnsConsumerEngine;
    
    private static final String NEW_USER_ENTRANCE = "1";// 新用户入口
    
    private static final String NEW_USER_EQUITY_ENTRANCE = "2";// 新用户权益入口
    
    public GetNewUserLoginPrizeMethodImpl()
    {
        super(METHOD_NAME);
    }
    
    /**
     * 新用户领新礼————业务逻辑
     * 
     * @author Li Bin
     * @param controller
     * @param request
     * @return
     */
    @ImplementMethod
    public InvokeResult<NewUserLoginPrizeResponse> getNewUserLoginPrize(ServiceController controller,
        ComponentRequest request)
    {
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("Enter GetNewUserLoginPrizeMethodImpl.getNewUserLoginPrize,identityId:{}, Request :"
                , CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(request));
        }
        Map<String, String> paramMap = request.getParamMapMap();
        NewUserLoginPrizeResponse.Builder builder = NewUserLoginPrizeResponse.newBuilder();
        builder.setPluginCode(StringTools.nvl(paramMap.get(ParamConstants.PLUGINCODE)));
        UesServiceUtils.setPublicParamToRequest(paramMap);
        try
        {
            builder.setData(getNewUserLoginPrize(paramMap));
        }
        catch (Throwable e)
        {
            LOGGER.error("Error GetNewUserLoginPrizeMethodImpl.getNewUserLoginPrize failed,identityId:{}, error:{}",
                CommonHttpUtil.getIdentity(),
                e.getMessage());
            BeanMergeUtils.setCommonResponseField(builder, ErrorCodeAndDesc.OTHER_DEFAULT_FAIL, ParamConstants.FALSE);
            return new InvokeResult<NewUserLoginPrizeResponse>(builder.build());
        }
        // 设置组件可见
        BeanMergeUtils.setCommonResponseField(builder, ErrorCodeAndDesc.SUCCESS, ParamConstants.TRUE);
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("exit GetNewUserLoginPrizeMethodImpl.getNewUserLoginPrize ,identityId:{},NewUserPrizeActionResponse :{}",
                CommonHttpUtil.getIdentity(),JsonFormatUtil.printToString(builder.build()));
        }
        return new InvokeResult<NewUserLoginPrizeResponse>(builder.build());
    }
    
    /**
     * 新用户领新礼数据获取
     * 
     * @author Li Bin
     * @param paramMap
     * @throws PortalException
     */
    private NewUserLoginPrize getNewUserLoginPrize(Map<String, String> paramMap)
        throws PortalException
    {
        NewUserLoginPrize.Builder newUserLoginPrize = NewUserLoginPrize.newBuilder();
        // 设置组件样式
        BeanMergeUtils.setComponentStyle(newUserLoginPrize, paramMap);
        String scene = paramMap.get(ParamConstants.SCENE);
        newUserLoginPrize
            .setScene(NEW_USER_EQUITY_ENTRANCE.equals(scene) ? NEW_USER_EQUITY_ENTRANCE : NEW_USER_ENTRANCE);// 场景枚举
        String moreUrl = paramMap.get(ParamConstants.MORE_URL);
        newUserLoginPrize
            .setMoreUrl(StringTools.nvl(UrlTools.getRelativelyUrl(moreUrl, UesServiceUtils.DEFAULT_DOMAIN)));// 更过福利链接
        String msisdn = HttpUtil.getHeader(ParamConstants.X_Identity_ID);
        // 校验游客
        if (UserManager.isGuestUser(msisdn))
        {
            return newUserLoginPrize.build();
        }
        try
        {
            // 判断是否为新用户
            IsNewUserRequest.Builder isNewUserRequest = IsNewUserRequest.newBuilder();
            isNewUserRequest.setMsisdn(msisdn);
            IsNewUserResponse isNewUserResponse =
                userSnsConsumerEngine.getIsNewUserMethodInvoker.getCompositeResponse(isNewUserRequest.build());
            boolean inGroup = isNewUserResponse.getInGroup();
            newUserLoginPrize.setNewUser(inGroup);
            if (!inGroup)// 不是新用户
            {
                return newUserLoginPrize.build();
            }
            // 可展示的礼品信息
            UserTicketRecordsRequest.Builder userTicketRecordsRequest = UserTicketRecordsRequest.newBuilder();
            userTicketRecordsRequest.setMsisdn(StringTools.nvl(msisdn));
            UserTicketRecordsResponse userTicketRecordsResponse =
                userSnsConsumerEngine.getNewUserTicketRecords.getCompositeResponse(userTicketRecordsRequest.build());
            List<TicketRecord> userTicketRecordsList = userTicketRecordsResponse.getUserTicketRecordsList();
            List<TicketRecords> ticketRecordList = new ArrayList<>();
            if (userTicketRecordsList != null && userTicketRecordsList.size() > 0)
            {
                int num = 0;// 计数器，只取前3条数据，不管空不空
                for (TicketRecord ticketRecord : userTicketRecordsList)
                {
                    num++;
                    if (num > 3)
                    {
                        break;
                    }
                    if (Util.isEmpty(ticketRecord))
                    {
                        continue;
                    }
                    TicketRecords.Builder builder = TicketRecords.newBuilder();
                    builder.setAmount(ticketRecord.getAmount());// 金额
                    builder.setDayNum(ticketRecord.getDayNum());// 连续登陆的天数
                    builder.setStatus(ticketRecord.getStatus());// 礼品领取状态，0：未领取 1：已领取 2：已失效 3：未知
                    ticketRecordList.add(builder.build());
                }
            }
            newUserLoginPrize.addAllTicketRecords(ticketRecordList);
            // 领取链接
            newUserLoginPrize.setAjaxDraw(StringTools.nvl(ActionUrlUtils.getNewUserDrawPrize()));
            return newUserLoginPrize.build();
        }
        catch (PortalException e)
        {
            LOGGER.error("GetNewUserLoginPrizeMethodImpl.getNewUserLoginPrize failed,identityId:{} , e:{},msisdn:{}",
                CommonHttpUtil.getIdentity(),
                e,
                msisdn);
            throw e;
        }
        
    }
}
