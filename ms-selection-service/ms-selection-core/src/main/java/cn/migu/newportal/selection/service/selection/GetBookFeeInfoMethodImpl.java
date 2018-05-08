package cn.migu.newportal.selection.service.selection;

import cn.migu.newportal.cache.util.CommonHttpUtil;
import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.huawei.iread.server.constant.Types;
import com.huawei.iread.server.pub.PriceUtil;

import cn.migu.compositeservice.bookproductservice.GetBookFeeDesc.GetBookFeeDescResponse;
import cn.migu.newportal.cache.bean.book.BookItem;
import cn.migu.newportal.cache.business.user.UserManager;
import cn.migu.newportal.cache.cache.service.PortalContentInfoCacheService;
import cn.migu.newportal.cache.util.JsonFormatUtil;
import cn.migu.newportal.cache.util.PropertiesConfig;
import cn.migu.newportal.selection.engine.ContentServiceEngine;
import cn.migu.newportal.selection.service.BookContentServiceImpl;
import cn.migu.newportal.selection.util.ContentUtil;
import cn.migu.newportal.selection.util.SelectionConstants;
import cn.migu.newportal.util.constants.BookContants;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.exception.PortalException;
import cn.migu.newportal.util.http.HttpUtil;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode;
import cn.migu.newportal.util.tools.RequestCheckUtil;
import cn.migu.selection.proto.base.BookFeeResponseOuterClass.BookFeeData;
import cn.migu.selection.proto.base.BookFeeResponseOuterClass.BookFeeResponse;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;

/**
 * 图书资费信息
 * 
 * @author wulinfeng
 * @version C10 2017年5月31日
 * @since SDP V300R003C10
 */
public class GetBookFeeInfoMethodImpl extends ServiceMethodImpl<BookFeeResponse, ComponentRequest>
{
    // 日志对象
    private static Logger logger = LoggerFactory.getLogger(GetBookFeeInfoMethodImpl.class.getName());
    
    private static final String METHOD_NAME = "getBookFeeInfo";
    
    private BookContentServiceImpl serviceImpl;
    
    // 会员权益折扣
    public static final String DISCOUNT_MONTH = "0";
    
    // 促销专区折扣
    public static final String DISCOUNT_PROMOTION = "1";
    
    // 定向免费
    public static final String CHARGE_MODE_DIRECTIONAL_FREE_PASS_THROUGH = "24";
    
    public GetBookFeeInfoMethodImpl()
    {
        super(METHOD_NAME);
    }
    
    /**
     * 获取资费相关信息
     * 
     * @author hushanqing
     * @param controller
     * @param request
     * @return
     */
    @ImplementMethod
    public InvokeResult<BookFeeResponse> getBookFeeInfo(ServiceController controller, ComponentRequest request)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug(
                "enter GetBookFeeInfoMethodImpl.getBookFeeInfo(),identityId:{} request:{}",
                CommonHttpUtil.getIdentity() ,JsonFormatUtil.printToString(request));
        }
        
        BookFeeResponse.Builder builder = BookFeeResponse.newBuilder();
        builder.setPluginCode(StringTools.nvl(request.getParamMapMap().get(ParamConstants.PLUGINCODE)));
        builder.setIsvisable(ParamConstants.NOT_VISABLE);
        builder.setStatus(MSResultCode.ErrorCodeAndDesc.CONNECTION_FAILED.getCode());
        builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.CONNECTION_FAILED.getDesc());
        
        BookFeeData.Builder dataBuilder = BookFeeData.newBuilder();
        
        // 获取请求参数
        String bid = request.getParamMapMap().get(ParamConstants.BID);
        
        if (StringUtils.isEmpty(bid))
        {
            builder.setStatus(MSResultCode.ErrorCodeAndDesc.BOOK_ID_EMPTY.getCode());
            builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.BOOK_ID_EMPTY.getDesc());
            return new InvokeResult<BookFeeResponse>(builder.build());
        }
        
        // 设置通用数据
        setCommonData(request, dataBuilder);
        
        BookItem bookItem = PortalContentInfoCacheService.getInstance().getBookItem(bid);
        
        if (bookItem == null || ParamConstants.BOOK_SHELF_STATUS.equals(bookItem.getStatus()))
        {
            builder.setStatus(MSResultCode.ErrorCodeAndDesc.BOOK_EMPTY_OR_SHELF.getCode());
            builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.BOOK_EMPTY_OR_SHELF.getDesc());
            return new InvokeResult<BookFeeResponse>(builder.build());
        }
        
        // 设置响应数据
        buildResponse(controller, request, builder, dataBuilder, bid, bookItem);
        
        if (logger.isDebugEnabled())
        {
            logger.debug(
                "exit GetBookFeeInfoMethodImpl.getBookFeeInfo(),identityId:{} response :{} ",
                CommonHttpUtil.getIdentity(),
                JsonFormatUtil.printToString(builder));
        }
        
        return new InvokeResult<BookFeeResponse>(builder.build());
    }
    
    /**
     * 设置响应数据
     * 
     * @author hushanqing
     * @param controller
     * @param request
     * @param builder
     * @param dataBuilder
     * @param bid
     * @param bookItem
     */
    private void buildResponse(ServiceController controller, ComponentRequest request, BookFeeResponse.Builder builder,
        BookFeeData.Builder dataBuilder, String bid, BookItem bookItem)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("enter GetBookFeeInfoMethodImpl.buildReponse(),bookId:{}" , bid);
        }
        // 图书类型
        String contentType = bookItem.getItemType();
        // 是否完结
        String isFinish = bookItem.getIsFinish();
        
        // 是否有实体
        String hasActual = null;
        if (StringTools.isEmpty(bookItem.getActualPrice())
            || StringTools.isEq(bookItem.getActualPrice(), SelectionConstants.ACTUALPRICE_0)
            || StringTools.isEq(bookItem.getActualPrice(), SelectionConstants.ACTUALPRICE_NONE))
        {
            hasActual = Types.FALSE;
        }
        else
        {
            hasActual = Types.TRUE;
        }
        
        // 是否支持关联推荐
        String isRecommond = null;
        if (StringUtils.isNotEmpty(bookItem.getIsRecommend())
            && StringTools.isEq(bookItem.getIsRecommend(), SelectionConstants.NOT_RECOMMOND))
        {
            isRecommond = SelectionConstants.NOT_RECOMMOND;
        }
        else
        {
            isRecommond = SelectionConstants.IS_RECOMMOND;
        }
        
        // 从请求消息头里取出用户手机号
        String mobile = HttpUtil.getHeader(ParamConstants.X_Identity_ID);
        String isMember = UserManager.isMember(mobile) ? ParamConstants.TRUE : ParamConstants.FALSE;
        String loginType = UserManager.getUserLoginType(HttpUtil.getHeader(ParamConstants.X_Auth_Token), mobile);
        
        // 调用组合服务获取图书资费
        try
        {
            GetBookFeeDescResponse response = ContentServiceEngine.getInstance().getBookFeeDesc(controller.getContext(),
                StringUtils.defaultString(bid),
                StringUtils.defaultString(mobile),
                StringUtils.defaultString(contentType),
                StringUtils.defaultString(isFinish),
                StringUtils.defaultString(hasActual),
                StringUtils.defaultString(isRecommond),
                StringUtils.defaultString(loginType));
            
            // 拼接价格、资费提示语
            if (Util.isNotEmpty(response))
            {
                // 是否绑定实体卡标志位
                String isBindCard = Types.FALSE;
                String isBlackUser = Types.FALSE;
                try
                {
                    isBlackUser = ContentServiceEngine.getInstance().isBlackUser(controller.getContext(), mobile);
                    isBindCard = ContentServiceEngine.getInstance().isBindCard(controller.getContext(), mobile);
                }
                catch (Exception e)
                {
                    logger.error("ContentServiceEngine isBlackUser or isBindCard error,identityId:{},mobile:{},e:{} "
                        , CommonHttpUtil.getIdentity(), mobile, e);
                }
                buildBookFee(dataBuilder, response, bookItem, isBlackUser, isBindCard, isMember);
                builder.setData(dataBuilder);
                
            }
            // 判断是否ajax请求
            String isAjax = request.getParamMapMap().get(ParamConstants.ISAJAX);
            if (StringTools.isNotEmpty(isAjax) && StringTools.isEq(ParamConstants.IS_AJAX, isAjax))
            {
                ComponentRequest.Builder paramMapBuilder = request.toBuilder();
                paramMapBuilder.removeParamMap(ParamConstants.ISAJAX);
                paramMapBuilder.removeParamMap(ParamConstants.BID);
                dataBuilder.putAllCtag(paramMapBuilder.getParamMapMap());
            }
            builder.setData(dataBuilder);
            
            // 设置成功返回码
            builder.setIsvisable(ParamConstants.IS_VISABLE);
            builder.setStatus(MSResultCode.ErrorCodeAndDesc.SUCCESS.getCode());
            builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.SUCCESS.getDesc());
        }
        catch (Exception | PortalException e)
        {
            logger.error("GetBookFeeInfoMethodImpl buildResponse failed,identityId:{} e : {}",
                CommonHttpUtil.getIdentity(),
                e);
            builder.setStatus(MSResultCode.ErrorCodeAndDesc.RESPONSE_ERROR.getCode());
            builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.RESPONSE_ERROR.getDesc());
        }
    }
    
    /**
     * 设置通用数据
     * 
     * @author hushanqing
     * @param request
     * @param dataBuilder
     */
    private void setCommonData(ComponentRequest request, BookFeeData.Builder dataBuilder)
    {
        String isShowLine = request.getParamMapMap().get(ParamConstants.ISSHOWLINE);
        String isMarginTop = request.getParamMapMap().get(ParamConstants.ISMARGINTOP);
        String isMarginBottom = request.getParamMapMap().get(ParamConstants.ISMARGINBOTTOM);
        String isPaddingTop = request.getParamMapMap().get(ParamConstants.ISPADDINGTOP);
        
        dataBuilder.setIsShowLine(RequestCheckUtil.getCSSType(isShowLine));
        dataBuilder.setIsMarginTop(RequestCheckUtil.getCSSType(isMarginTop));
        dataBuilder.setIsMarginBottom(RequestCheckUtil.getCSSType(isMarginBottom));
        dataBuilder.setIsPaddingTop(RequestCheckUtil.getCSSType(isPaddingTop));
    }
    
    public BookContentServiceImpl getBookContentServiceImpl()
    {
        return serviceImpl;
    }
    
    /**
     * 根据不同鉴权结果，构造不同的价格描述和资费描述
     *
     * @author wulinfeng
     * @param dataBuilder
     * @param bookFeeResponse
     * @param bookItem
     * @param isBindCard
     * @param isMember
     * @return
     */
    private void buildBookFee(BookFeeData.Builder dataBuilder, GetBookFeeDescResponse bookFeeResponse,
        BookItem bookItem, String isBlackUser, String isBindCard, String isMember)
    {
        // 获取版本号
        double version = Double.valueOf(ContentUtil.getUAVersion());
        
        // 图书计费模式
        String chargeMode = StringTools.isEmpty(bookItem.getChargeMode()) ? Types.CHARGE_MODE_FREE : bookItem.getChargeMode();
        
        // 根据调用资费接口响应，获取图书鉴权类型
        String authenticateType = StringUtils.defaultString(bookFeeResponse.getAuthenticatePtype());

        logger.info("version : {},isMember : {},isBlackUser :{},isBindCard : {},authenticateType :{}",
            version,
            isMember,
            isBlackUser,
            isBindCard,
            authenticateType);
        
        // 一.用户绑定礼品卡且在不在黑名单
        if (StringTools.isEq(Types.FALSE, isBlackUser) && StringTools.isEq(Types.TRUE, isBindCard))
        {
            dataBuilder.setBookPrice(getPriceDesc(bookFeeResponse.getPrice(), chargeMode));
            dataBuilder.setBookPriceDesc(PropertiesConfig.getProperty("vip_white_user_desc"));
            dataBuilder.setPriceDescStyle(SelectionConstants.PRICEDESCSTYLE_MEMBER);
        }
        else
        {
            // 二、5.6版本及以上
            if (version >= 5.6)
            {
                // 1.会员且包内
                if ((StringTools.isEq(Types.CHARGE_MODE_MONTHLY_PAYMENT, authenticateType)
                    || StringTools.isEq(Types.CHARGE_MODE_MONTHLY_OUT, authenticateType))
                    && StringTools.isEq(ParamConstants.IS_MEMBER, isMember))
                {
                    dataBuilder.setBookPrice(getPriceDesc(bookFeeResponse.getPrice(), chargeMode));
                    dataBuilder.setBookPriceDesc(PropertiesConfig.getProperty("member_online_free_desc"));
                    dataBuilder.setPriceDescStyle(SelectionConstants.PRICEDESCSTYLE_MEMBER);
                }
                else
                {
                    // 2.按本已定购或者包内
                    if (StringTools.isEq(Types.CHARGE_MODE_BOOK, authenticateType)
                        || StringTools.isEq(Types.CHARGE_MODE_MONTHLY_PAYMENT, authenticateType)
                        || StringTools.isEq(Types.CHARGE_MODE_MONTHLY_OUT, authenticateType)
                        || StringTools.isEq(Types.CHARGE_MODE_PROMOTION_PACKET, authenticateType)
                        || StringTools.isEq(Types.CHARGE_MODE_SEND, authenticateType))
                    {
                        dataBuilder.setBookPriceDesc(PropertiesConfig.getProperty("order_fee_desc"));
                        dataBuilder.setPriceDescStyle(SelectionConstants.PRICEDESCSTYLE_ORDERED);
                    }
                    else if (StringTools.isEq(CHARGE_MODE_DIRECTIONAL_FREE_PASS_THROUGH, authenticateType)) // 3.
                                                                                                            // 定向免费(专属免费)
                    {
                        dataBuilder.setBookPrice(getPriceDesc(bookFeeResponse.getPrice(), chargeMode));
                        dataBuilder.setBookPriceDesc(PropertiesConfig.getProperty("direction_free_desc"));
                        dataBuilder.setPriceDescStyle(SelectionConstants.PRICEDESCSTYLE_NORMAL);
                    }
                    else if (StringTools.isEq(Types.CHARGE_MODE_FREE, authenticateType)) // 4. 永久免费
                    {
                        String priceDesc = null;
                        if (BookContants.BOOKTYPE_VOICEBOOK.equals(bookItem.getItemType()))
                        {
                            priceDesc = PropertiesConfig.getProperty("listen_forever_free_desc");
                        }
                        else
                        {
                            priceDesc = PropertiesConfig.getProperty("forever_free_desc");
                        }
                        dataBuilder.setBookPriceDesc(priceDesc);
                        dataBuilder.setPriceDescStyle(SelectionConstants.PRICEDESCSTYLE_FREE);
                    }
                    else if (Util.isNotEmpty(bookFeeResponse.getPromotionInfo()) // 5. 限时免费
                        && StringTools.isEq(SelectionConstants.ACTIVITYTYPE_LIMITFREE,
                            bookFeeResponse.getPromotionInfo().getActivityType()))
                    {
                        dataBuilder.setBookPrice(getPriceDesc(bookFeeResponse.getPrice(), chargeMode));
                        dataBuilder.setPriceDescStyle(SelectionConstants.PRICEDESCSTYLE_NORMAL);
                        dataBuilder.setBookPriceDesc(PropertiesConfig.getProperty("online_limit_free_desc"));
                    }
                    else if (Util.isNotEmpty(bookFeeResponse.getPromotionInfo()) // 6. 特价促销
                        && StringTools.isEq(SelectionConstants.ACTIVITYTYPE_SPECIALOFFER,
                            bookFeeResponse.getPromotionInfo().getActivityType()))
                    {
                        dataBuilder.setBookPrice(getPriceDesc(bookFeeResponse.getPrice(), chargeMode));
                        if (bookFeeResponse.getVipPrice() < bookFeeResponse.getPrice())
                        {
                            dataBuilder.setBookRealPrice(getPriceDesc(bookFeeResponse.getVipPrice(), chargeMode));
                            dataBuilder.setPriceDescStyle(SelectionConstants.PRICEDESCSTYLE_NORMAL);
                            dataBuilder.setBookPriceDesc(PropertiesConfig.getProperty("special_promotion_desc"));
                        }
                    }
                    else if (StringTools.isEq(SelectionConstants.BOOK_LEVEL_1, bookItem.getBookLevel())) // 7. 一级图书
                    {
                        dataBuilder.setBookPrice(getPriceDesc(bookFeeResponse.getPrice(), chargeMode));
                        dataBuilder.setPriceDescStyle(SelectionConstants.PRICEDESCSTYLE_NORMAL);
                        if (StringTools.isEq(ParamConstants.IS_MEMBER, isMember)) // 会员
                        {
                            dataBuilder.setBookPriceDesc(PropertiesConfig.getProperty("online_no_ad_desc"));
                        }
                        else
                        {
                            dataBuilder.setBookPriceDesc(PropertiesConfig.getProperty("online_ad_desc"));
                        }
                    }
                    else // 8. 其他
                    {
                        buildOtherPrice(dataBuilder, bookFeeResponse, isMember, chargeMode);
                    }
                }
            }
            // 三、5.6版本以下
            else
            {
                // 1.按本已定购或者包月
                if (StringTools.isEq(Types.CHARGE_MODE_BOOK, authenticateType)
                    || StringTools.isEq(Types.CHARGE_MODE_MONTHLY_PAYMENT, authenticateType)
                    || StringTools.isEq(Types.CHARGE_MODE_MONTHLY_OUT, authenticateType)
                    || StringTools.isEq(Types.CHARGE_MODE_PROMOTION_PACKET, authenticateType)
                    || StringTools.isEq(Types.CHARGE_MODE_SEND, authenticateType))
                {
                    dataBuilder.setBookPriceDesc(PropertiesConfig.getProperty("order_fee_desc"));
                    dataBuilder.setPriceDescStyle(SelectionConstants.PRICEDESCSTYLE_ORDERED);
                }
                else if (StringTools.isEq(Types.CHARGE_MODE_FREE, authenticateType)) // 2. 永久免费
                {
                    String priceDesc = null;
                    if (BookContants.BOOKTYPE_VOICEBOOK.equals(bookItem.getItemType()))
                    {
                        priceDesc = PropertiesConfig.getProperty("listen_forever_free_desc");
                    }
                    else
                    {
                        priceDesc = PropertiesConfig.getProperty("forever_free_desc");
                    }
                    dataBuilder.setBookPriceDesc(priceDesc);
                    dataBuilder.setPriceDescStyle(SelectionConstants.PRICEDESCSTYLE_FREE);
                }
                else if (Util.isNotEmpty(bookFeeResponse.getPromotionInfo()) // 3. 限时免费
                    && StringTools.isEq(SelectionConstants.ACTIVITYTYPE_LIMITFREE,
                        bookFeeResponse.getPromotionInfo().getActivityType()))
                {
                    dataBuilder.setBookPrice(getPriceDesc(bookFeeResponse.getPrice(), chargeMode));
                    dataBuilder.setPriceDescStyle(SelectionConstants.PRICEDESCSTYLE_NORMAL);
                    dataBuilder.setBookPriceDesc(PropertiesConfig.getProperty("online_limit_free_desc"));
                }
                else if (Util.isNotEmpty(bookFeeResponse.getPromotionInfo()) // 4. 特价促销
                    && StringTools.isEq(SelectionConstants.ACTIVITYTYPE_SPECIALOFFER,
                        bookFeeResponse.getPromotionInfo().getActivityType()))
                {
                    dataBuilder.setBookPrice(getPriceDesc(bookFeeResponse.getPrice(), chargeMode));
                    if (bookFeeResponse.getVipPrice() < bookFeeResponse.getPrice())
                    {
                        dataBuilder.setBookRealPrice(getPriceDesc(bookFeeResponse.getVipPrice(), chargeMode));
                        dataBuilder.setPriceDescStyle(SelectionConstants.PRICEDESCSTYLE_NORMAL);
                        dataBuilder.setBookPriceDesc(PropertiesConfig.getProperty("special_promotion_desc"));
                    }
                }
                else // 5. 其他
                {
                    buildOtherPrice(dataBuilder, bookFeeResponse, isMember, chargeMode);
                }
            }
        }
        
    }
    
    /**
     * 返回价格描述，把分转成元，区分按本和按章，包括基本价格、优惠价格
     * 
     * @author wulinfeng
     * @param price
     * @param chargeMode
     * @return
     */
    private String getPriceDesc(int price, String chargeMode)
    {
        String priceDesc = PriceUtil.changeFentoYuan(price);
        if (StringTools.isEq(Types.CHARGE_MODE_CHAPTER, chargeMode)) // 按章计费
        {
            priceDesc += PropertiesConfig.getProperty("price_unit_chapter");
        }
        else // 按本计费
        {
            priceDesc += PropertiesConfig.getProperty("price_unit_book");
        }
        return priceDesc;
    }
    
    /**
     * 构造其他所有条件分支的价格
     *
     * @author wulinfeng
     * @param dataBuilder
     * @param bookFeeResponse
     * @param isMember
     * @param chargeMode
     */
    private void buildOtherPrice(BookFeeData.Builder dataBuilder, GetBookFeeDescResponse bookFeeResponse,
        String isMember, String chargeMode)
    {
        // 有优惠
        if (bookFeeResponse.getVipPrice() < bookFeeResponse.getPrice())
        {
            dataBuilder.setBookPrice(getPriceDesc(bookFeeResponse.getPrice(), chargeMode));
            dataBuilder.setBookRealPrice(getPriceDesc(bookFeeResponse.getVipPrice(), chargeMode));
            if (ParamConstants.IS_MEMBER.equals(isMember)) // 会员
            {
                dataBuilder.setBookPriceDesc(PropertiesConfig.getProperty("member_discount_desc").replace("{X}",
                    String.valueOf(bookFeeResponse.getDiscount() / 10)));
                dataBuilder.setPriceDescStyle(SelectionConstants.PRICEDESCSTYLE_MEMBER);
            }
            else // 非会员
            {
                dataBuilder.setBookPriceDesc(PropertiesConfig.getProperty("special_promotion_desc"));
                dataBuilder.setPriceDescStyle(SelectionConstants.PRICEDESCSTYLE_NORMAL);
            }
        }
        // 无优惠
        else
        {
            dataBuilder.setBookPrice(getPriceDesc(bookFeeResponse.getPrice(), chargeMode));
        }
    }
    
}
