package cn.migu.newportal.selection.service.selection;

import cn.migu.newportal.cache.util.CommonHttpUtil;
import cn.migu.newportal.cache.util.JsonFormatUtil;
import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.migu.newportal.cache.bean.book.BookItem;
import cn.migu.newportal.cache.cache.service.PortalContentInfoCacheService;
import cn.migu.newportal.selection.service.BookContentDescServiceImpl;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.tools.MSResultCode;
import cn.migu.selection.proto.base.BookContentDescResponseOuterClass.BookContentDescData;
import cn.migu.selection.proto.base.BookContentDescResponseOuterClass.BookContentDescResponse;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.wheat.annotation.ImplementMethod;
import cn.migu.wheat.service.InvokeResult;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceMethodImpl;

/**
 * 内容简介组合服务
 * 
 * @author wulinfeng
 * @version C10 2017年6月9日
 * @since SDP V300R003C10
 */
public class BookContentDescInfo extends ServiceMethodImpl<BookContentDescResponse, ComponentRequest>
{
    // 日志对象
    private static Logger logger = LoggerFactory.getLogger(BookContentDescInfo.class);
    
    private static final String METHOD_NAME = "getBookContentDescInfo";
    
    private BookContentDescServiceImpl serviceImpl;
    
    public BookContentDescInfo()
    {
        super(METHOD_NAME);
    }
    
    
    @ImplementMethod
    public InvokeResult<BookContentDescResponse> v1(ServiceController controller, ComponentRequest request)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug( "enter BookContentDescInfo-v1，identityId:{} ComponentRequest :{}"
                , CommonHttpUtil.getIdentity(), JsonFormatUtil.printToString(request));
        }
        BookContentDescResponse.Builder builder = BookContentDescResponse.newBuilder();
        builder.setPluginCode(request.getParamMapMap().get(ParamConstants.PLUGINCODE));
        builder.setIsvisable(ParamConstants.NOT_VISABLE);
        builder.setStatus(MSResultCode.ErrorCodeAndDesc.CONNECTION_FAILED.getCode());
        builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.CONNECTION_FAILED.getDesc());
        
        BookContentDescData.Builder dataBuilder = BookContentDescData.newBuilder();
        
        // 获取请求参数
        String bid = request.getParamMapMap().get(ParamConstants.BID);
        String isShowLine = request.getParamMapMap().get(ParamConstants.ISSHOWLINE);
        
        // 是否显示页面底部横线
        if (null != isShowLine)
        {
            dataBuilder.setIsShowLine(isShowLine);
        }
        
        dataBuilder.setIsMarginTop(StringUtils.defaultString(request.getParamMapMap().get(ParamConstants.ISMARGINTOP)));
        dataBuilder.setIsMarginBottom(StringUtils.defaultString(request.getParamMapMap().get(ParamConstants.ISMARGINBOTTOM)));
        
        // 调用缓存获取图书详情
        try
        {
            BookItem book = PortalContentInfoCacheService.getInstance().getBookItem(bid);
            
            if (book != null)
            {
                dataBuilder.setBookRecommend(book.getShortRecommendReason());
                dataBuilder.setBookDesc(book.getLongDescription());
                
                // 判断是否ajax请求
                String isAjax = request.getParamMapMap().get(ParamConstants.ISAJAX);
                if (StringUtils.isNotEmpty(isAjax) && "1".equals(isAjax))
                {
                    ComponentRequest.Builder paramMapBuilder = request.toBuilder();
                    paramMapBuilder.removeParamMap(ParamConstants.ISAJAX);
                    dataBuilder.putAllCtag(paramMapBuilder.getParamMapMap());
                }
                builder.setData(dataBuilder);
                builder.setIsvisable(ParamConstants.IS_VISABLE);
            }
            // 设置成功返回码
            builder.setStatus(MSResultCode.ErrorCodeAndDesc.SUCCESS.getCode());
            builder.setMessageDesc(MSResultCode.ErrorCodeAndDesc.SUCCESS.getDesc());
        }
        catch (Exception e)
        {
            logger.error("BookContentDescServiceImpl-BookContentDescInfo identityId:{} e:{}",
                CommonHttpUtil.getIdentity(),
                e);
        }
        
        return new InvokeResult<BookContentDescResponse>(builder.build());
    }
    
    public BookContentDescServiceImpl getBookContentDescServiceImpl()
    {
        return serviceImpl;
    }
    
}
