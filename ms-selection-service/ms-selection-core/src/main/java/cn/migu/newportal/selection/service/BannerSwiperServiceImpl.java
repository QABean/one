package cn.migu.newportal.selection.service;

import javax.annotation.Resource;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

import cn.migu.newportal.selection.service.selection.BannerSwiperInfoImpl;
import cn.migu.selection.proto.bannerswiper._BannerSwiperService.BannerSwiperService;
import cn.migu.selection.proto.base.BannerSwiperResponseOuterClass.BannerSwiperResponse;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceImpl;
import cn.migu.wheat.service.ServiceMethod;

public class BannerSwiperServiceImpl extends BannerSwiperService implements BannerSwiperService.Interface, ServiceImpl
{
    
    @Resource(name = "bannerSwiperInfoImpl")
    private BannerSwiperInfoImpl bannerSwiperInfoImpl;
    
    private String serviceName;
    
    private String version;
    
    private ServiceMethod[] methods = null;
    
    public void init()
    {
        methods = new ServiceMethod[] {bannerSwiperInfoImpl};
    }
    
    @Override
    public void getBannerSwiperInfo(RpcController controller, ComponentRequest req,
        RpcCallback<BannerSwiperResponse> res)
    {
        if (controller instanceof ServiceController)
        {
            
            bannerSwiperInfoImpl.call((ServiceController)controller, req, res);
            
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
