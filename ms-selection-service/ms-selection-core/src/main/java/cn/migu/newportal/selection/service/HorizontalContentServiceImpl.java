package cn.migu.newportal.selection.service;

import javax.annotation.Resource;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

import cn.migu.newportal.selection.service.selection.HorizontalContentInfoImpl;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.HorizontalContentResponseOuterClass.HorizontalContentResponse;
import cn.migu.selection.proto.horizontalcontentservice._HorizontalContentService.HorizontalContentService;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceImpl;
import cn.migu.wheat.service.ServiceMethod;

public class HorizontalContentServiceImpl extends HorizontalContentService
    implements HorizontalContentService.Interface, ServiceImpl
{
    
    @Resource(name = "horizontalContentInfo")
    private HorizontalContentInfoImpl horizontalContentInfo;
    
    private String serviceName;
    
    private String version;
    
    private ServiceMethod[] methods = null;
    
    public HorizontalContentServiceImpl()
    {
        
    }
    
    public void init()
    {
        
        methods = new ServiceMethod[] {horizontalContentInfo};
    }
    
    @Override
    public void getHorizontalContentInfo(RpcController controller, ComponentRequest request,
        RpcCallback<HorizontalContentResponse> done)
    {
        
        if (controller instanceof ServiceController)
        {
            horizontalContentInfo.call((ServiceController)controller, request, done);
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
