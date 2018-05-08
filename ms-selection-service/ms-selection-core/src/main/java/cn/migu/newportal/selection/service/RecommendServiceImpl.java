package cn.migu.newportal.selection.service;

import javax.annotation.Resource;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

import cn.migu.newportal.selection.service.selection.RecommendInfoImpl;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.RecommendResponseOuterClass.RecommendResponse;
import cn.migu.selection.proto.recommendservice._RecommendService.RecommendService;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceImpl;
import cn.migu.wheat.service.ServiceMethod;

public class RecommendServiceImpl extends RecommendService implements RecommendService.Interface,ServiceImpl
{
    @Resource(name="recommendServiceImpl")
    RecommendInfoImpl recommendInfoImpl;
    
    private String serviceName;
    
    private String version;
    
    private ServiceMethod[] methods = null;
    
    public RecommendServiceImpl()
    {
        super();
    }
    
    public void init()
    {
        
        methods = new ServiceMethod[] {recommendInfoImpl};
    }
    
    @Override
    public void getRecommendInfo(RpcController controller, ComponentRequest req, RpcCallback<RecommendResponse> rpc)
    {
        if (controller instanceof ServiceController)
        {
            
            recommendInfoImpl.call((ServiceController)controller, req, rpc);
            
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
