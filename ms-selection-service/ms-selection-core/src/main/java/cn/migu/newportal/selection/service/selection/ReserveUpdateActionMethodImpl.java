package cn.migu.newportal.selection.service.selection;

import java.util.HashMap;
import java.util.Map;



import cn.migu.compositeservice.otherservice.UserPreSubscription.UserPreSubscriptionRequest;
import cn.migu.newportal.cache.bean.book.BookItem;
import cn.migu.newportal.cache.business.user.UserManager;
import cn.migu.newportal.cache.cache.engine.OtherConsumerEngine;
import cn.migu.newportal.cache.cache.service.GetUserReserveBooksCacheService;
import cn.migu.newportal.cache.cache.service.PortalContentInfoCacheService;
import cn.migu.newportal.cache.cache.service.PortalGeneralInfoCacheService;
import cn.migu.newportal.cache.util.*;
import cn.migu.newportal.selection.engine.ContentServiceEngine;
import cn.migu.newportal.util.constants.*;
import cn.migu.newportal.util.exception.PortalException;
import cn.migu.newportal.util.http.HttpUtil;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode;
import cn.migu.newportal.util.tools.MSResultCode.ErrorCodeAndDesc;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.ReserveUpdateActionResponseOuterClass.ReserveUpdateActionResponse;
import cn.migu.selection.proto.base.ReserveUpdateActionResponseOuterClass.ReserveUpdateActionResponse.Builder;
import cn.migu.userservice.GetExtendUserInfo.AddExtendUserInfoRequest;
import cn.migu.userservice.GetExtendUserInfo.GetExtendUserInfoResponse;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 预订更新action
 * 
 * @pluginCode recommend_content
 * @author wanghao
 * @date 2017年11月8日
 */
public class ReserveUpdateActionMethodImpl extends ServiceMethodImpl<ReserveUpdateActionResponse, ComponentRequest>
{
    // 日志信息
    private static Logger LOGGER = LoggerFactory.getLogger(ReserveUpdateActionMethodImpl.class.getName());
    
    // 方法名称
    private static final String METHOD_NAME = "reserveUpdateAction";
    
    public ReserveUpdateActionMethodImpl()
    {
        super(METHOD_NAME);
    }

    @Autowired
    OtherConsumerEngine otherConsumerEngine;

    private static final String RESERVE_OTHER_ERROR="7302";

    private static final String UNRESERVE_OTHER_ERROR="7303";
    
    /**
     * 预订更新action逻辑实现
     * 
     * @author wanghao
     * @date 2017年11月8日
     * @param controller
     * @param request
     * @return
     */
    @ImplementMethod
    public InvokeResult<ReserveUpdateActionResponse> reserveUpdateAction(ServiceController controller,
        ComponentRequest request)
    {
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("Enter ReserveUpdateActionMethodImpl.reserveUpdateAction ! identityId:{},Request:{} "
                , CommonHttpUtil.getIdentity(),JsonFormatUtil.printToString(request));
        }
        ReserveUpdateActionResponse.Builder builder = ReserveUpdateActionResponse.newBuilder();
        String bid = request.getParamMapMap().get(ParamConstants.BID);
        if (StringTools.isEmpty(bid))
        {
            LOGGER.error("Error ReserveUpdateActionMethodImpl.reserveUpdateAction Error! identityId:{},bid :{}",
                CommonHttpUtil.getIdentity(),
                bid);
            builder.setStatus(ErrorCodeAndDesc.BOOK_ID_EMPTY.getCode());
            builder.setMessageDesc(ErrorCodeAndDesc.BOOK_ID_EMPTY.getDesc());
            return new InvokeResult<ReserveUpdateActionResponse>(builder.build());
        }
        String msisdn = HttpUtil.getHeader(ParamConstants.X_Identity_ID);
        if (UserManager.isGuestUser(msisdn))
        {
            LOGGER.error("Error ReserveUpdateActionMethodImpl.reserveUpdateAction Error!identityId:{}, msisdn :{} ",
                CommonHttpUtil.getIdentity(),
                msisdn);
            builder.setStatus(ErrorCodeAndDesc.MSISDN_IS_EMPTY.getCode());
            builder.setMessageDesc(ErrorCodeAndDesc.MSISDN_IS_EMPTY.getDesc());
            return new InvokeResult<ReserveUpdateActionResponse>(builder.build());
        }
        
        BookItem bookItem = PortalContentInfoCacheService.getInstance().getBookItem(bid);
        // 图书如果不存在或者图书已经下架则返回错误码
        if (Util.isEmpty(bookItem) || ParamConstants.BOOK_SHELF_STATUS.equals(bookItem.getStatus()))
        {
            LOGGER.error("Error ReserveUpdateActionMethodImpl.reserveUpdateAction ! Book is No Exist, bid ={} " , bid);
            builder.setStatus(ErrorCodeAndDesc.BOOK_EMPTY_OR_SHELF.getCode());
            builder.setMessageDesc(ErrorCodeAndDesc.BOOK_EMPTY_OR_SHELF.getDesc());
            return new InvokeResult<ReserveUpdateActionResponse>(builder.build());
        }
        String reserveType = request.getParamMapMap().get(ParamConstants.RESERVETYPE);
        try
        {
            // 预订功能
            if (ParamConstants.BOOK_RESERVE.equals(reserveType))
            {
                reserveBook(msisdn, bookItem, builder);
                if (LOGGER.isDebugEnabled())
                {
                    LOGGER.debug("Exit ReserveUpdateActionMethodImpl.reserveUpdateAction ! reserve book ,identityId:{} builder ={}"
                        ,CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(builder));
                }
                return new InvokeResult<ReserveUpdateActionResponse>(builder.build());
            }
            // 确认删除预订
            else if (ParamConstants.BOOK_UNRESERVE.equals(reserveType))
            {
                delReserveBook(msisdn, bookItem, builder);
                if (LOGGER.isDebugEnabled())
                {
                    LOGGER.debug(
                        "Exit ReserveUpdateActionMethodImpl.reserveUpdateAction ! delete reserve book ,identityId:{}, builder ={}"
                            ,CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(builder));
                }
                return new InvokeResult<ReserveUpdateActionResponse>(builder.build());
            }
            if (LOGGER.isDebugEnabled())
            {
                LOGGER.debug(
                    "Exit ReserveUpdateActionMethodImpl.reserveUpdateAction ! optionType no matching, optionType ={},builder ={} "
                        , reserveType ,JsonFormatUtil.printToString(builder));
            }
        }
        catch (Exception e)
        {
            LOGGER.error(
                "Error ReserveUpdateActionMethodImpl.reserveUpdateAction   error! identityId:{},request :{},e: {}",
                CommonHttpUtil.getIdentity(),
                JsonFormatUtil.printToString(request),
                e.getCause()
            );
        }

        builder.setStatus(ErrorCodeAndDesc.OTHER_DEFAULT_FAIL.getCode());
        builder.setMessageDesc(ErrorCodeAndDesc.OTHER_DEFAULT_FAIL.getDesc());
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("Exit ReserveUpdateActionMethodImpl.reserveUpdateAction, identityId:{},response:{} "
                , CommonHttpUtil.getIdentity(),JsonFormatUtil.printToString(builder));
        }
        return new InvokeResult<ReserveUpdateActionResponse>(builder.build());
        
    }
    
    /**
     * 删除预订
     * 
     * @author wanghao
     * @date 2017年11月1日
     * @param msisdn
     * @param bookItem
     * @param builder
     * @return
     */
    private void delReserveBook(String msisdn, BookItem bookItem, Builder builder)
    {
        // 操作结果
        int resultCode = delReserve(bookItem, msisdn);
        // 取消预订行为话单
        setActionCdr(bookItem.getBookId(), null, StringTools.nvl(resultCode), MsCdrTools.CANCELRESERVEBOOK);

        // 取消预订成功
        if (ResultCode.SUCCESS == resultCode)
        {
            String contentType = Types.RESERVE_BOOK_TYPE;
            if (!contentType.contains(bookItem.getItemType()))
            {
                contentType = contentType + ";" + bookItem.getItemType();
            }
            // 刷新缓存
            GetUserReserveBooksCacheService.getInstance().updateUserReserveBooks(msisdn, null, contentType, true);


            String message = StringTools.nvl2(
                PortalGeneralInfoCacheService.getInstance().getConfigValue(MSResultCode.USER_CANCELRESERVE_SUCCESS),
                PropertiesConfig.getProperty(MSResultCode.USER_CANCELRESERVE_SUCCESS));
            builder.setMessageDesc(message);
            builder.setStatus(ErrorCodeAndDesc.SUCCESS.getCode());
        }
        else
        {
            String message = PortalGeneralInfoCacheService.getInstance().getConfigValue(UNRESERVE_OTHER_ERROR);
            if (ResultCode.USER_UNPRESUBSCRIPE_THIS_BOOK == resultCode ||
                ResultCode.USER_UNPRESUBSCRIPE_THIS_SERIES == resultCode)
            {
                message = PortalGeneralInfoCacheService.getInstance().getConfigValue(StringTools.nvl(resultCode));
            }
            builder.setStatus(StringTools.nvl(resultCode));
            builder.setMessageDesc(message);
        }
    }
    
    /**
     * 取消预订图书逻辑实现
     * 
     * @author wanghao
     * @date 2017年11月1日
     * @param bookItem
     * @param msisdn
     * @return
     */
    private int delReserve(BookItem bookItem, String msisdn)
    {
        UserPreSubscriptionRequest.Builder userPreSubscriptionRequest = UserPreSubscriptionRequest.newBuilder();
        userPreSubscriptionRequest.setBookId(bookItem.getBookId());
        userPreSubscriptionRequest.setMobile(msisdn);
        userPreSubscriptionRequest.setOperationType(ParamConstants.BOOK_UNRESERVE);
        userPreSubscriptionRequest.setAccessType(SystemConstants.WAP_PORTAL_TYPE);

        //操作结果
        return UserPreSubscription(userPreSubscriptionRequest.build());

    }
    
    /**
     * 预订图书
     * 
     * @author wanghao
     * @date 2017年11月1日
     * @param msisdn
     * @param bookItem
     * @param builder
     * @return
     */
    private void reserveBook(String msisdn, BookItem bookItem, Builder builder)
    {
        // 操作结果
        int resultCode = reserve(bookItem, msisdn);
        // 如果预订成功
        if (ResultCode.SUCCESS == resultCode)
        {
            String contentType = Types.RESERVE_BOOK_TYPE;
            if (!contentType.contains(bookItem.getItemType()))
            {
                contentType = contentType + ";" + bookItem.getItemType();
            }
            // 刷新缓存
            GetUserReserveBooksCacheService.getInstance().updateUserReserveBooks(msisdn, ParamConstants.PORTAL_TYPE_WAP, Types.RESERVE_BOOK_TYPE, true);

            String engageType = null;
            // 取出预订的提醒类型
            AddExtendUserInfoRequest.Builder request = AddExtendUserInfoRequest.newBuilder();
            request.setMsisdn(msisdn);
            try
            {
                GetExtendUserInfoResponse response =
                    ContentServiceEngine.getInstance().getExtendUserInfo(request.build());
                engageType = Util.isEmpty(response) ?
                    Types.SCHEDULE_UPDATE_INSTANT :
                    StringTools.nvl2(response.getScheduleUpdate(), Types.SCHEDULE_UPDATE_INSTANT);
            }
            catch (Exception e)
            {
                LOGGER.error(
                    "ReserveUpdateActionMethodImpl.reserveBook, ContentServiceEngine.getExtendUserInfo is error, identityId={}, request={}",
                    msisdn,
                    request);
            }

            // 记录行为话单
            setActionCdr(bookItem.getBookId(),engageType,StringTools.nvl(resultCode), MsCdrTools.RESERVEBOOK);

            String message = StringTools.nvl2(
                PortalGeneralInfoCacheService.getInstance().getConfigValue(MSResultCode.USER_RESERVE_SUCCESS),
                PropertiesConfig.getProperty(MSResultCode.USER_RESERVE_SUCCESS));
            String typeMessage = "";
            switch (engageType)
            {
                case Types.SCHEDULE_UPDATE_TIMING:
                    typeMessage = StringTools.nvl2(
                        PortalGeneralInfoCacheService.getInstance()
                            .getConfigValue(UesParamKeyConstants.ENGAGETYPE_TIMING_REMIND),
                        PropertiesConfig.getProperty(UesParamKeyConstants.ENGAGETYPE_TIMING_REMIND));
                    break;
                case Types.SCHEDULE_UPDATE_INSTANT:
                    typeMessage = StringTools.nvl2(
                        PortalGeneralInfoCacheService.getInstance()
                            .getConfigValue(UesParamKeyConstants.ENGAGETYPE_INSTANT_REMIND),
                        PropertiesConfig.getProperty(UesParamKeyConstants.ENGAGETYPE_INSTANT_REMIND));
                    break;
                case Types.SCHEDULE_UPDATE_INSTANT_DAYTIME:
                    typeMessage = StringTools.nvl2(
                        PortalGeneralInfoCacheService.getInstance()
                            .getConfigValue(UesParamKeyConstants.ENGAGETYPE_24_REMIND),
                        PropertiesConfig.getProperty(UesParamKeyConstants.ENGAGETYPE_24_REMIND));
                    break;
            }
            message = message.replace("{engagetype}", typeMessage);
            builder.setStatus(ErrorCodeAndDesc.SUCCESS.getCode());
            builder.setMessageDesc(message);
        }
        else
        {
            String message = PortalGeneralInfoCacheService.getInstance().getConfigValue(RESERVE_OTHER_ERROR);
            if (ResultCode.USER_PRE_SUBSCRIBE_MAX == resultCode || ResultCode.USER_HAVE_PRESUBSCRIPE == resultCode)
            {
                message = PortalGeneralInfoCacheService.getInstance().getConfigValue(StringTools.nvl(resultCode));
            }
            builder.setStatus(StringTools.nvl(resultCode));
            builder.setMessageDesc(message);
        }
    }
    
    /**
     * 预订图书 逻辑实现
     * 
     * @author wanghao
     * @date 2017年11月1日
     * @return 状态码
     */
    private int reserve(BookItem bookItem, String msisdn)
    {
        // 文档上写 1为完本，如果该图书室完本则预订失败 完本为1，连载为0
        if (!StringTools.isEq(SystemConstants.SERIALZE_TYPE_SERIAL,bookItem.getIsSerial()))
        {
            LOGGER.error("ReserveUpdateActionMethodImpl.reserve() is errored， book is not serial, bid={}, identityId={}",
                bookItem.getBookId(),
                CommonHttpUtil.getIdentity());
            return ResultCode.OTHER_SERVER_ERROR;
        }
        UserPreSubscriptionRequest.Builder userPreSubscriptionRequest = UserPreSubscriptionRequest.newBuilder();
        userPreSubscriptionRequest.setBookId(bookItem.getBookId());
        userPreSubscriptionRequest.setMobile(msisdn);
        userPreSubscriptionRequest.setOperationType(ParamConstants.BOOK_RESERVE);
        userPreSubscriptionRequest.setAccessType(SystemConstants.WAP_PORTAL_TYPE);

        //操作结果
        return UserPreSubscription(userPreSubscriptionRequest.build());
    }

    /**
     * 预订/取消预订图书接口调用
     * @param request
     * @return
     */
    private int UserPreSubscription(UserPreSubscriptionRequest request)
    {
        try
        {
            otherConsumerEngine.userPreSubscriptionMethodInvoker.getCompositeResponse(request);
        }
        catch (PortalException e)
        {
            LOGGER.error(
                "ReserveUpdateActionMethodImpl.UserPreSubscription(), otherConsumerEngine.userPreSubscriptionMethodInvoker is errored, errorCode={}, request={}, identityId={}",
                e.getErrorCode(),
                request,
                CommonHttpUtil.getIdentity()
                );
            return e.getErrorCode();
        }
        return ResultCode.SUCCESS;
    }

    /**
     * 设置行为话单
     * @param bookId 图书ID
     * @param warnType 提醒类型1、定时提醒；2、即时提醒；3、 24小时提醒
     * @param errCode
     */
    private void setActionCdr(String bookId, String warnType, String errCode,String actionType)
    {
        // 记录行为话单
        MsCdrTools.setActionCdrParam(MsCdrTools.ACTIONTYPE, actionType);
        MsCdrTools.setActionCdrParam(MsCdrTools.ERRCODE, errCode);
        MsCdrTools.setActionCdrExtendsParam(1, bookId);
        if(StringTools.isNotEmpty(warnType))
        {
            MsCdrTools.setActionCdrExtendsParam(2, warnType);
        }
    }
    
}
