package cn.migu.newportal.selection.service;

import javax.annotation.Resource;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

import cn.migu.newportal.selection.service.selection.AbetOrOpposePostInfo;
import cn.migu.selection.proto.abetoropposepostservice._AbetOrOpposePostService.AbetOrOpposePostService;
import cn.migu.selection.proto.base.AbetOrOpposePostResponseOuterClass.AbetOrOpposePostResponse;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceImpl;
import cn.migu.wheat.service.ServiceMethod;

public class AbetOrOpposePostServiceImpl extends AbetOrOpposePostService
    implements AbetOrOpposePostService.Interface, ServiceImpl
{
    @Resource(name = "abetOrOpposePostInfo")
    private AbetOrOpposePostInfo abetOrOpposePostInfo;
    
    private String serviceName;
    
    private String version;
    
    private ServiceMethod[] methods = null;
    
    public AbetOrOpposePostServiceImpl()
    {
    }
    
    public void init()
    {
        methods = new ServiceMethod[] {abetOrOpposePostInfo};
    }
    
    @Override
    public void abetOrOpposePostInfo(RpcController controller, ComponentRequest request,
        RpcCallback<AbetOrOpposePostResponse> done)
    {
        if (controller instanceof ServiceController)
        {
            abetOrOpposePostInfo.call((ServiceController)controller, request, done);
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
