package cn.migu.newportal.selection.service;

import javax.annotation.Resource;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

import cn.migu.newportal.selection.service.selection.BookNameClassificationImpl;
import cn.migu.selection.proto.base.BookNameClassificationResponseOuterClass.BookNameClassificationResponse;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.booknameclassificationservice._BookNameClassificationService.BookNameClassificationService;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceImpl;
import cn.migu.wheat.service.ServiceMethod;

public class BookNameClassificationServiceImpl extends BookNameClassificationService
    implements BookNameClassificationService.Interface, ServiceImpl
{
    @Resource(name = "bookNameClassificationImpl")
    private BookNameClassificationImpl bookNameClassificationImpl;
    
    private String serviceName;
    
    private String version;
    
    private ServiceMethod[] methods = null;
    
    public BookNameClassificationServiceImpl()
    {
    }
    
    public void init()
    {
        methods = new ServiceMethod[] {bookNameClassificationImpl};
    }
    
    @Override
    public void bookNameClassificationForLink(RpcController controller, ComponentRequest request,
        RpcCallback<BookNameClassificationResponse> done)
    {
        if (controller instanceof ServiceController)
        {
            bookNameClassificationImpl.call((ServiceController)controller, request, done);
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
