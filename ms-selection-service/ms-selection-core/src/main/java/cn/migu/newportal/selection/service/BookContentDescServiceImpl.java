package cn.migu.newportal.selection.service;

import javax.annotation.Resource;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

import cn.migu.newportal.selection.service.selection.BookContentDescInfo;
import cn.migu.selection.proto.base.BookContentDescResponseOuterClass.BookContentDescResponse;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.bookcontentdescservice._BookContentDescService.BookContentDescService;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceImpl;
import cn.migu.wheat.service.ServiceMethod;

public class BookContentDescServiceImpl extends BookContentDescService implements BookContentDescService.Interface,ServiceImpl
{
    @Resource(name="bookContentDesc")
    private BookContentDescInfo bookContentDescInfo;
    
    private String serviceName;
    
    private String version;
    
    private ServiceMethod[] methods = null;
    public BookContentDescServiceImpl()
    {
        
    }
    
    public void init()
    {
        methods = new ServiceMethod[] {bookContentDescInfo};
    }
    
    @Override
    public void getBookContentDescInfo(RpcController controller, ComponentRequest request,
        RpcCallback<BookContentDescResponse> done)
    {
        if (controller instanceof ServiceController)
        {
            bookContentDescInfo.call((ServiceController)controller, request, done);
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
