package cn.migu.newportal.selection.service;

import javax.annotation.Resource;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

import cn.migu.newportal.selection.service.selection.GetPortalBookLinkMethodImpl;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.PortalBookLinkResponseOuterClass.PortalBookLinkResponse;
import cn.migu.selection.proto.portalbookLinkservice._PortalBookLinkService.PortalBookLinkService;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceImpl;
import cn.migu.wheat.service.ServiceMethod;

public class PortalBookLinkServiceImpl extends PortalBookLinkService
    implements PortalBookLinkService.Interface, ServiceImpl
{
    @Resource(name = "portalBookLinkInfo")
    private GetPortalBookLinkMethodImpl portalBookLinkInfo;
    
    private String serviceName;
    
    private String version;
    
    private ServiceMethod[] methods = null;
    
    public PortalBookLinkServiceImpl()
    {
        
    }
    
    public void init()
    {
        
        methods = new ServiceMethod[] {portalBookLinkInfo};
    }
    
    @Override
    public void getPortalBookLink(RpcController controller, ComponentRequest request,
        RpcCallback<PortalBookLinkResponse> done)
    {
        if (controller instanceof ServiceController)
        {
            portalBookLinkInfo.call((ServiceController)controller, request, done);
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
