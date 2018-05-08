package cn.migu.newportal.selection.service;

import javax.annotation.Resource;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

import cn.migu.newportal.selection.service.selection.VerticalComponentInfoImpl;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.VerticalComponentResponseOuterClass.VerticalComponentResponse;
import cn.migu.selection.proto.verticalcomponentservice._VerticalComponentService.VerticalComponentService;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceImpl;
import cn.migu.wheat.service.ServiceMethod;

public class VerticalComponentServiceImpl extends VerticalComponentService
    implements VerticalComponentService.Interface, ServiceImpl
{
    
    @Resource(name = "verticalComponentInfo")
    VerticalComponentInfoImpl verticalComponentInfo;
    
    private String serviceName;
    
    private String version;
    
    private ServiceMethod[] methods = null;
    
    public VerticalComponentServiceImpl()
    {
        super();
    }
    
    public void init()
    {
        methods = new ServiceMethod[] {verticalComponentInfo};
    }
    
    @Override
    public void getVerticalComponentInfo(RpcController controller, ComponentRequest request,
        RpcCallback<VerticalComponentResponse> done)
    {
        if (controller instanceof ServiceController)
        {
            
            verticalComponentInfo.call((ServiceController)controller, request, done);
            
        }
        else
        {
            
            controller.setFailed("verticalComponent controller");
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
