package cn.migu.newportal.selection.service.selection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.migu.newportal.cache.util.*;
import org.apache.commons.lang.StringUtils;


import cn.migu.newportal.uesserver.api.Struct.LinkInfo;
import cn.migu.newportal.uesserver.api.Struct.LinkRequest;

import cn.migu.newportal.cache.bean.book.BookItem;
import cn.migu.newportal.cache.cache.service.PortalBookCoverCacheService;
import cn.migu.newportal.cache.cache.service.PortalContentInfoCacheService;
import cn.migu.newportal.selection.bean.local.request.ThreeRecommendHorizontalRequest;
import cn.migu.newportal.selection.engine.ContentServiceEngine;
import cn.migu.newportal.selection.util.BookDataUtil;
import cn.migu.newportal.selection.util.ParamUtil;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.exception.PortalException;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.newportal.util.tools.MSResultCode;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.ThreeRecommendHorizontalResponseOuterClass.ThreeRecommendHorizontal;
import cn.migu.selection.proto.base.ThreeRecommendHorizontalResponseOuterClass.ThreeRecommendHorizontalData;
import cn.migu.selection.proto.base.ThreeRecommendHorizontalResponseOuterClass.ThreeRecommendHorizontalResponse;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeContext;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 推荐三封面横向列表
 * 
 * @author hushanqing
 * @version C10 2017年7月27日
 * @since
 */
public class ThreeRecommendHorizontalInfo extends ServiceMethodImpl<ThreeRecommendHorizontalResponse, ComponentRequest>
{
    /** 日志对象 */
    private static Logger logger = LoggerFactory.getLogger(ThreeRecommendHorizontalInfo.class);
    
    /** 接口名称 */
    private static final String METHOD_NAME = "getThreeRecommendForHorizontal";
    
    /** 显示个数 */
    private static int showNum = 3;
    
    public ThreeRecommendHorizontalInfo()
    {
        super(METHOD_NAME);
    }
    
    /**
     * 推荐三封面横向列表处理
     * 
     * @author hushanqing
     * @param controller
     * @param request
     * @return
     */
    @ImplementMethod
    public InvokeResult<ThreeRecommendHorizontalResponse> getThreeRecommendForHorizontal(ServiceController controller,
        ComponentRequest request)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("enter ThreeRecommendHorizontalInfo.getThreeRecommendForHorizontal(),identityId:{},request:{}" ,
                CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(request));
        }
        Map<String, String> params = request.getParamMapMap();
        
        UesServiceUtils.setPublicParamToRequest(params);
        
        ThreeRecommendHorizontalRequest threeRecommend = new ThreeRecommendHorizontalRequest(params);
        
        ThreeRecommendHorizontalResponse.Builder builder = ThreeRecommendHorizontalResponse.newBuilder();
        builder.setPluginCode(threeRecommend.getPluginCode());
        builder.setIsvisable(ParamConstants.NOT_VISABLE);
        
        ThreeRecommendHorizontalData.Builder dataBuilder = ThreeRecommendHorizontalData.newBuilder();
        
        if (StringTools.isEmpty(threeRecommend.getInstanceId()))
        {
            builder.setStatus(MSResultCode.ERRORCODE_PARAMVALUE_IS_NULL);
            builder.setMessageDesc("instanceId :" + MSResultCode.ERRORMSG_PARAMVALUE_IS_NULL);
            return new InvokeResult<ThreeRecommendHorizontalResponse>(builder.build());
        }
        
        String link_id_list = StringTools.nvl(threeRecommend.getLink_id_list());
        String[] linkIds = link_id_list.split(",");
        
        // 存放要显示的图书信息
        List<ThreeRecommendHorizontal> showList = new ArrayList<>();
        
        String[] tempLinkIds = new String[showNum];
        int num = linkIds.length > showNum ? showNum : linkIds.length;
        
        System.arraycopy(linkIds, 0, tempLinkIds, 0, num);
        
        // 第一本图书信息
        ThreeRecommendHorizontal bookInfo1 = getBookInfo(controller,
            threeRecommend.getBookid1(),
            threeRecommend.getNameShowType1(),
            tempLinkIds[0],
            threeRecommend.getBookClass1(),
            params);
        showList.add(bookInfo1);
        // 第二本图书信息
        ThreeRecommendHorizontal bookInfo2 = getBookInfo(controller,
            threeRecommend.getBookid2(),
            threeRecommend.getNameShowType2(),
            tempLinkIds[1],
            threeRecommend.getBookClass2(),
            params);
        showList.add(bookInfo2);
        // 第三本图书信息
        ThreeRecommendHorizontal bookInfo3 = getBookInfo(controller,
            threeRecommend.getBookid3(),
            threeRecommend.getNameShowType3(),
            tempLinkIds[2],
            threeRecommend.getBookClass3(),
            params);
        showList.add(bookInfo3);
        
        // 封装响应
        dataBuilder.setIsMarginTop(threeRecommend.getIsMarginTop());
        dataBuilder.setIsMarginBottom(threeRecommend.getIsMarginBottom());
        dataBuilder.setIsPaddingTop(threeRecommend.getIsPaddingTop());
        dataBuilder.setIsShowLine(threeRecommend.getIsShowLine());
        dataBuilder.addAllBooks(showList);
        
        builder.setIsvisable(ParamConstants.IS_VISABLE);
        // 判断是否ajax请求
        String isAjax = params.get(ParamConstants.ISAJAX);
        if (StringUtils.isNotEmpty(isAjax) && ParamConstants.IS_AJAX.equals(isAjax))
        {
            ComponentRequest.Builder paramMapBuilder = request.toBuilder();
            paramMapBuilder.removeParamMap(ParamConstants.ISAJAX);
            dataBuilder.putAllCtag(paramMapBuilder.getParamMapMap());
        }
        
        builder.setData(dataBuilder);
        
        // 设置成功返回码
        builder.setStatus(MSResultCode.ErrorCodeAndDesc.SUCCESS.getCode());
        builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.SUCCESS.getDesc());
        
        if (logger.isDebugEnabled())
        {
            logger.debug("exit ThreeRecommendHorizontalInfo.getThreeRecommendForHorizontal(),identityId:{} ,response = {}",
                CommonHttpUtil.getIdentity(),JsonFormatUtil.printToString(builder));
        }
        
        return new InvokeResult<ThreeRecommendHorizontalResponse>(builder.build());
    }
    
    /**
     * 获取图书三封面相关信息
     * 
     * @author hushanqing
     * @param controller
     * @param bookId
     * @param nameShowType
     * @param linkid
     * @param bookClass
     * @return
     */
    public ThreeRecommendHorizontal getBookInfo(ServiceController controller, String bookId, String nameShowType,
        String linkid, String bookClass, Map<String, String> paramMap)
    {
        ThreeRecommendHorizontal.Builder threeRecommen = ThreeRecommendHorizontal.newBuilder();
        
        if (StringTools.isNotEmpty(bookId))
        {
            // 从缓存中获取图书信息
            BookItem bookItem = PortalContentInfoCacheService.getInstance().getBookItem(bookId);
            if (null != bookItem)
            {
                // 获取封面信息
                String bookCover = PortalBookCoverCacheService.getInstance().getBookCover(bookId,
                    PropertiesConfig.getProperty("cdnUrl"));
                threeRecommen.setBookCoverLogo(ParamUtil.checkResponse(bookCover));
                
                // 获取图书显示名称
                String bookShowName = BookDataUtil.getShowName(bookId, nameShowType);
                threeRecommen.setBookShowName(ParamUtil.checkResponse(bookShowName));
                
                // 图书地址
                Map<String, String> params = UesServiceUtils.buildPublicParaMap(null, null);
                params.put(ParamConstants.BID, bookId);
                params.put(ParamConstants.NID, paramMap.get(ParamConstants.NID));
                
                String bookDetailUrl = PresetPageUtils.getBookDetailPage(params);
                threeRecommen.setBookDetailUrl(ParamUtil.checkResponse(bookDetailUrl));
            }
            else
            {
                putEmptyBookInfo(threeRecommen);
            }
            
        }
        else
        {
            putEmptyBookInfo(threeRecommen);
        }
        // 图书分类
        threeRecommen.setBookClass(ParamUtil.checkResponse(bookClass));
        
        // 获取链接信息
        if (StringTools.isNotEmpty(linkid))
        {
            // 调用ues微服务获取链接信息
            List<LinkInfo> list = UesServerServiceUtils.getLinkInfoList(ParamUtil.checkResponse(linkid));
            
            if (Util.isNotEmpty(list))
            {
                LinkInfo linkInfo = list.get(0);
                // 配置的链接描述
                String linkDesc = linkInfo.getDescription();
                threeRecommen.setLinkDesc(ParamUtil.checkResponse(linkDesc));
                
                // 配置的链接地址
                String linkUrl = linkInfo.getLinkurl();
                
                Map<String, String> params =
                    UesServiceUtils.buildPublicParaMap(null, StringTools.nvl(linkInfo.getSortvalue()));
                
                linkUrl = UesServiceUtils.getUESUrl(linkUrl, params);
                threeRecommen.setLinkUrl(ParamUtil.checkResponse(linkUrl));
            }
            else
            {
                putEmptyLinkInfo(threeRecommen);
            }
        }
        else
        {
            putEmptyLinkInfo(threeRecommen);
        }
        
        if (logger.isDebugEnabled())
        {
            logger.debug("exit ThreeRecommendHorizontalInfo.getBookInfo(),result is {}" , threeRecommen);
        }
        
        return threeRecommen.build();
    }
    
    /**
     * 给链接对象设空值
     * 
     * @author hushanqing
     * @param threeRecommen
     */
    public void putEmptyLinkInfo(ThreeRecommendHorizontal.Builder threeRecommen)
    {
        threeRecommen.setLinkDesc(" ");
        threeRecommen.setLinkUrl(" ");
    }
    
    /**
     * 给图书对象设空值
     * 
     * @author hushanqing
     * @param threeRecommen
     */
    public void putEmptyBookInfo(ThreeRecommendHorizontal.Builder threeRecommen)
    {
        threeRecommen.setBookCoverLogo(" ");
        threeRecommen.setBookShowName(" ");
        threeRecommen.setBookDetailUrl(" ");
    }
}
