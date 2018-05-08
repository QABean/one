package cn.migu.newportal.selection.service;

import javax.annotation.Resource;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

import cn.migu.newportal.selection.service.selection.ActivityBannerImpl;
import cn.migu.selection.proto.ActivityBannerService._ActivityBannerService.ActivityBannerService;
import cn.migu.selection.proto.base.ActivityBannerResponseOuterClass.ActivityBannerResponse;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceImpl;
import cn.migu.wheat.service.ServiceMethod;

public class ActivityBannerServiceImpl extends ActivityBannerService
    implements ActivityBannerService.Interface, ServiceImpl
{
    @Resource(name = "activityBannerInfo")
    ActivityBannerImpl activityBannerInfo;
    
    private String serviceName;
    
    private String version;
    
    private ServiceMethod[] methods = null;
    
    public ActivityBannerServiceImpl()
    {
        super();
    }
    
    public void init()
    {
        methods = new ServiceMethod[] {activityBannerInfo};
    }
    
    @Override
    public void getActivityBannerInfo(RpcController controller, ComponentRequest request,
        RpcCallback<ActivityBannerResponse> done)
    {
        if (controller instanceof ServiceController)
        {
            
            activityBannerInfo.call((ServiceController)controller, request, done);
            
        }
        else
        {
            
            controller.setFailed("ActivityBanner controller");
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
