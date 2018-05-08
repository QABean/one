package cn.migu.newportal.selection.service;

import javax.annotation.Resource;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

import cn.migu.newportal.selection.service.selection.ThreeRecommendHorizontalInfo;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.ThreeRecommendHorizontalResponseOuterClass.ThreeRecommendHorizontalResponse;
import cn.migu.selection.proto.threerecommendhorizontalservice._ThreeRecommendHorizontalResponse.ThreeRecommendHorizontalService;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceImpl;
import cn.migu.wheat.service.ServiceMethod;

public class ThreeRecommendHorizontalServiceImpl extends ThreeRecommendHorizontalService
    implements ThreeRecommendHorizontalService.Interface, ServiceImpl
{
    @Resource(name = "threeRecommendHorizontalInfo")
    private ThreeRecommendHorizontalInfo threeRecommendHorizontalInfo;
    
    private String serviceName;
    
    private String version;
    
    private ServiceMethod[] methods = null;
    
    public ThreeRecommendHorizontalServiceImpl()
    {
    }
    
    public void init()
    {
        
        methods = new ServiceMethod[] {threeRecommendHorizontalInfo};
    }
    
    @Override
    public void getThreeRecommendForHorizontal(RpcController controller, ComponentRequest request,
        RpcCallback<ThreeRecommendHorizontalResponse> done)
    {
        if (controller instanceof ServiceController)
        {
            threeRecommendHorizontalInfo.call((ServiceController)controller, request, done);
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
