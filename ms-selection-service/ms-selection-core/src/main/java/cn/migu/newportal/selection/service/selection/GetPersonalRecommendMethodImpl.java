package cn.migu.newportal.selection.service.selection;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.migu.compositeservice.contservice.BookmarkInfoOuterClass.BookmarkInfo;
import cn.migu.compositeservice.contservice.GetBookmark.GetBookmarkRequest;
import cn.migu.compositeservice.contservice.GetBookmark.GetBookmarkResponse;
import cn.migu.newportal.cache.business.user.UserManager;
import cn.migu.newportal.cache.cache.engine.ContentConsumerEngine;
import cn.migu.newportal.cache.util.BeanMergeUtils;
import cn.migu.newportal.cache.util.CommonHttpUtil;
import cn.migu.newportal.cache.util.JsonFormatUtil;
import cn.migu.newportal.cache.util.PropertiesConfig;
import cn.migu.newportal.cache.util.RequestParamCheckUtils;
import cn.migu.newportal.util.bean.user.PortalUserInfo;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.exception.PortalException;
import cn.migu.newportal.util.http.HttpUtil;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode.ErrorCodeAndDesc;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.GetPersonalRecommendResponseOuterClass.GetPersonalRecommendData;
import cn.migu.selection.proto.base.GetPersonalRecommendResponseOuterClass.GetPersonalRecommendResponse;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;

/**
 * 私人推荐
 * 
 * pluginCode：personal_recommend
 * 
 * @author wanghao
 * @version C10 2017年12月14日
 * @since SDP V300R003C10
 */
public class GetPersonalRecommendMethodImpl extends ServiceMethodImpl<GetPersonalRecommendResponse, ComponentRequest>
{
    // 日志
    private final static Logger LOGGER = LoggerFactory.getLogger(GetPersonalRecommendMethodImpl.class);
    
    // 方法名称
    private final static String METHOD_NAME = "getPersonalRecommend";
    
    /** 默认标题文本 */
    private final static String DEFAULT_TITLETEXT =
        PropertiesConfig.getProperty("personal_recommend_default_titletext");
    
    /** 展示样式 1:在看人数; 默认为1 */
    private final static String DEFAULT_SHOWSTYLE = "1";
    
    /** 展示样式 2:作者名 */
    private final static String AUTHOR_NAME_SHOWSTYLE = "2";
    
    /** 角标展示:0:无;1:免费;2:限免;3:会员;4:完本;5:名家;6:上传，ues复选框，可选 */
    private final static String CORNERSHOW_TYPE = "0,1,2,3,4,5,6,7,8,9";
    
    /** 固定图书类型 */
    private final static String CONTENTYPE = "1;2";
    
    /** 引擎 */
    @Autowired
    private ContentConsumerEngine engine;
    
    public GetPersonalRecommendMethodImpl()
    {
        super(METHOD_NAME);
    }
    
    /**
     * 私人推荐方法实现
     *
     * @author wanghao
     * @param controller
     * @param request
     * @return
     */
    @ImplementMethod
    public InvokeResult<GetPersonalRecommendResponse> getPersonalRecommend(ServiceController controller,
        ComponentRequest request)
    {
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug(" Enter GetPersonalRecommendMethodImpl.getPersonalRecommend(),identityId:{}, Request:{}",
                CommonHttpUtil.getIdentity(),
                JsonFormatUtil.printToString(request));
        }
        GetPersonalRecommendResponse.Builder builder = GetPersonalRecommendResponse.newBuilder();
        Map<String, String> paramMap = request.getParamMapMap();
        String pluginCode = paramMap.get(ParamConstants.PLUGINCODE);
        builder.setPluginCode(StringTools.nvl(pluginCode));
        
        String msisdn = HttpUtil.getHeader(ParamConstants.X_Identity_ID);
        if (UserManager.isGuestUser(msisdn))
        {
            LOGGER.error(" Error GetPersonalRecommendMethodImpl.getPersonalRecommend(),msisdn is GuestUser ,msisdn:{}",
                msisdn);
            BeanMergeUtils.setCommonResponseField(builder, ErrorCodeAndDesc.MSISDN_IS_EMPTY, ParamConstants.FALSE);
            return new InvokeResult<GetPersonalRecommendResponse>(builder.build());
        }
        try
        {
            setGetPersonalRecommendData(builder, paramMap, msisdn);
        }
        catch (Exception e)
        {
            LOGGER.error(" Error GetPersonalRecommendMethodImpl.getPersonalRecommend(),identityId:{}, e:{},Request:{}",
                CommonHttpUtil.getIdentity(),
                e,
                JsonFormatUtil.printToString(request));
            BeanMergeUtils.setCommonResponseField(builder, ErrorCodeAndDesc.OTHER_DEFAULT_FAIL, ParamConstants.FALSE);
            return new InvokeResult<GetPersonalRecommendResponse>(builder.build());
        }
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug(" Exit GetPersonalRecommendMethodImpl.getPersonalRecommend(),identityId:{} Response:{}",
                CommonHttpUtil.getIdentity(),
                JsonFormatUtil.printToString(builder));
        }
        return new InvokeResult<GetPersonalRecommendResponse>(builder.build());
        
    }
    
    /**
     * 封装响应数据
     *
     * @author wanghao
     * @param builder
     * @param paramMap
     * @param msisdn
     */
    private void setGetPersonalRecommendData(GetPersonalRecommendResponse.Builder builder, Map<String, String> paramMap,
        String msisdn)
    {
        GetPersonalRecommendData.Builder dataBuilder = GetPersonalRecommendData.newBuilder();
        BeanMergeUtils.setComponentStyle(dataBuilder, paramMap);
        // 标题文本
        String titleText = paramMap.get(ParamConstants.TITLE);
        dataBuilder.setTitleText(StringTools.nvl2(titleText, DEFAULT_TITLETEXT));
        // 标题描述
        String titleDesc = paramMap.get(ParamConstants.TITLEDESC);
        dataBuilder.setTitleDesc(StringTools.nvl(titleDesc));
        
        // 角标展示
        String cornerShow = RequestParamCheckUtils.checkArrayStringEunm(paramMap.get(ParamConstants.CORNERSHOWTYPE),
            CORNERSHOW_TYPE,
            ParamConstants.CORNERSHOWTYPE_0);
        dataBuilder.setCornerShow(cornerShow);
        // 展示样式
        String showStyle = AUTHOR_NAME_SHOWSTYLE.equals(paramMap.get(ParamConstants.SHOWTYPE)) ? AUTHOR_NAME_SHOWSTYLE
            : DEFAULT_SHOWSTYLE;
        dataBuilder.setShowStyle(showStyle);
        // 会话中心取用户手机号
        PortalUserInfo portalUserInfo = UserManager.getPortalUserInfo(msisdn);
        if (Util.isNotEmpty(portalUserInfo) && Util.isNotEmpty(portalUserInfo.getUserInfo()))
        {
            dataBuilder.setMsisdn(StringTools.nvl(portalUserInfo.getUserInfo().getUserRelatedMobile()));
        }
        int pageSize = RequestParamCheckUtils.paginationParamCheck(paramMap.get(ParamConstants.PAGESIZE),
            ParamConstants.PAGE_SIZE_TEN);
        String bookArray = getSysBookmarkIds(msisdn, pageSize);
        dataBuilder.setBookArray(StringTools.nvl(bookArray));
        builder.setData(dataBuilder);
        BeanMergeUtils.setCommonResponseField(builder, ErrorCodeAndDesc.SUCCESS, ParamConstants.TRUE);
        
    }
    
    /**
     * 获取系统书签图书ID列表
     * 
     * @author wanghao
     * @param msisdn
     * @param pageSize
     * @return
     */
    private String getSysBookmarkIds(String msisdn, int pageSize)
    {
        List<BookmarkInfo> bookmarkInfos = getSysBookmarkList(msisdn, pageSize);
        if (Util.isNotEmpty(bookmarkInfos))
        {
            StringBuffer sb = new StringBuffer();
            for (BookmarkInfo bookmarkInfo : bookmarkInfos)
            {
                if (Util.isEmpty(bookmarkInfo) || Util.isEmpty(bookmarkInfo.getBookId()))
                {
                    continue;
                }
                sb.append(bookmarkInfo.getBookId());
                // 拼接逗号
                sb.append(",");
            }
            if (sb.length() > 0)
            {
                // 截取最后一个逗号
                return sb.substring(0, sb.length() - 1);
            }
        }
        return null;
    }
    
    /**
     * 获取系统书签图书列表
     *
     * @author wanghao
     * @param msisdn
     * @return
     * @throws PortalException
     */
    private List<BookmarkInfo> getSysBookmarkList(String msisdn, int pageSize)
    {
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug(" Enter GetPersonalRecommendMethodImpl.getSysBookmarkList(),msisdn:{},pageSize:{}",
                msisdn,
                pageSize);
        }
        GetBookmarkRequest.Builder request = GetBookmarkRequest.newBuilder();
        request.setMobile(msisdn);
        request.setContentType(CONTENTYPE);
        request.setStart(StringTools.toInt(ParamConstants.PAGE_NUM, 1));
        request.setBookmarkType(ParamConstants.SYSTEM_BOOKMARK);
        request.setCount(pageSize);
        try
        {
            GetBookmarkResponse response = engine.getBookmarkMethodInvoker.getCompositeResponse(request.build());
            if (LOGGER.isDebugEnabled())
            {
                LOGGER.debug(" Exit GetPersonalRecommendMethodImpl.getSysBookmarkList(),identityId:{} Response:{}",
                    CommonHttpUtil.getIdentity(),
                    JsonFormatUtil.printToString(response));
            }
            return response.getBookmarkListList();
        }
        catch (PortalException e)
        {
            LOGGER.error(
                " Error GetPersonalRecommendMethodImpl.getSysBookmarkList() ContentConsumerEngine Call getBookmark  Error,identityId:{}, e:{},e.code:{},Request:{}",
                CommonHttpUtil.getIdentity(),
                e,
                e.getErrorCode(),
                JsonFormatUtil.printToString(request));
            return null;
        }
    }
    
}
