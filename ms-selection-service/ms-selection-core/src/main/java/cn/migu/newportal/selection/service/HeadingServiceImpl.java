package cn.migu.newportal.selection.service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

import cn.migu.newportal.selection.service.selection.GetGeneralHeadingMethodImpl;
import cn.migu.newportal.selection.service.selection.HeadingInfo;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.GeneralHeadingResponseOuterClass.GeneralHeadingResponse;
import cn.migu.selection.proto.base.HeadingResponseOuterClass.HeadingResponse;
import cn.migu.selection.proto.headingservice._HeadingService.HeadingService;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceImpl;
import cn.migu.wheat.service.ServiceMethod;

/**
 * 
 * 通用标题栏
 *
 * @author yanzp
 * @version C10 2017年4月26日
 * @since SDP V300R003C10
 */
public class HeadingServiceImpl extends HeadingService implements HeadingService.Interface,ServiceImpl
{
    @Resource(name="headingInfo")
    private HeadingInfo headingInfo;
    
    @Resource(name="generalHeadingMethodImpl")
    private GetGeneralHeadingMethodImpl generalHeadingMethodImpl;
    
    private String serviceName;
    
    private String version;
    
    private ServiceMethod[] methods = null;
    
    @PostConstruct
    public void init()
    {
        methods = new ServiceMethod[] {headingInfo,generalHeadingMethodImpl};
    }

    @Override
    public void getHeadingInfo(RpcController controller, ComponentRequest request, RpcCallback<HeadingResponse> done)
    {
        if (controller instanceof ServiceController)
        {
            headingInfo.call((ServiceController)controller, request, done);
        }
        else
        {
            controller.setFailed("Invalid controller");
        }
        
    }

    @Override
    public void getGeneralHeading(RpcController controller, ComponentRequest request,
        RpcCallback<GeneralHeadingResponse> done)
    {
        if (controller instanceof ServiceController)
        {
            generalHeadingMethodImpl.call((ServiceController)controller, request, done);
        }
        else
        {
            controller.setFailed("Invalid controller");
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
