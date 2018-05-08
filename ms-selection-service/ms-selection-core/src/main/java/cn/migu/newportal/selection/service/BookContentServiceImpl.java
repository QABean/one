package cn.migu.newportal.selection.service;

import javax.annotation.Resource;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

import cn.migu.newportal.selection.service.selection.GetBookContentInfoMethodImpl;
import cn.migu.newportal.selection.service.selection.GetBookFeeInfoMethodImpl;
import cn.migu.selection.proto.base.BookContentResponseOuterClass.BookContentResponse;
import cn.migu.selection.proto.base.BookFeeResponseOuterClass.BookFeeResponse;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.bookcontentservice._BookContentService.BookContentService;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceImpl;
import cn.migu.wheat.service.ServiceMethod;

/**
 * 图书详情页组件服务:图书详情信息+图书资费信息
 * 
 * @author wulinfeng
 * @version C10 2017年5月31日
 * @since SDP V300R003C10
 */
public class BookContentServiceImpl extends BookContentService implements BookContentService.Interface,ServiceImpl
{
    @Resource(name="getBookContentInfoMethodImpl")
    private GetBookContentInfoMethodImpl bookContentInfo;
    
    @Resource(name="getBookFeeInfoMethodImpl")
    private GetBookFeeInfoMethodImpl bookFeeInfo;
    
    private String serviceName;
    
    private String version;
    
    private ServiceMethod[] methods = null;
    
    public BookContentServiceImpl()
    {
    }
    
    public void init()
    {
        methods = new ServiceMethod[] {bookContentInfo,bookFeeInfo};
    }
    
    @Override
    public void getBookContentInfo(RpcController controller, ComponentRequest request,
        RpcCallback<BookContentResponse> done)
    {
        if (controller instanceof ServiceController)
        {
            bookContentInfo.call((ServiceController)controller, request, done);
        }
        else
        {
            controller.setFailed("invalid controller");
        }
    }
    
    @Override
    public void getBookFeeInfo(RpcController controller, ComponentRequest request, RpcCallback<BookFeeResponse> done)
    {
        if (controller instanceof ServiceController)
        {
            bookFeeInfo.call((ServiceController)controller, request, done);
        }
        else
        {
            controller.setFailed("invalid controller");
        }
    }

    public String getServiceName()
    {
        return serviceName;
    }

    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public ServiceMethod[] getMethods()
    {
        return methods;
    }

    public void setMethods(ServiceMethod[] methods)
    {
        this.methods = methods;
    }
    
}
