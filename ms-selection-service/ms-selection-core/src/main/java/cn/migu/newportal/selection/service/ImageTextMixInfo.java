package cn.migu.newportal.selection.service;

import javax.annotation.Resource;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

import cn.migu.newportal.selection.service.selection.ImageTextMixImpl;
import cn.migu.selection.proto.ComponentShowService._ComponentShowService.ComponentShowService;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.ComponentShowResponseOuterClass.ComponentShowResponse;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceImpl;
import cn.migu.wheat.service.ServiceMethod;

/**
 * 图文混排列表
 * 
 *
 * @author zhangmm
 * @version C10 2017年7月25日
 * @since SDP V300R003C10
 */
public class ImageTextMixInfo extends ComponentShowService implements ComponentShowService.Interface, ServiceImpl
{
    
    @Resource(name = "componentShowImpl")
    private ImageTextMixImpl componentShowImpl;
    
    private String serviceName;
    
    private String version;
    
    private ServiceMethod[] methods = null;
    
    public ImageTextMixInfo()
    {
        
    }
    
    public void init()
    {
        methods = new ServiceMethod[] {componentShowImpl};
    }
    
    @Override
    public void getComponentShow(RpcController controller, ComponentRequest request,
        RpcCallback<ComponentShowResponse> done)
    {
        
        if (controller instanceof ServiceController)
        {
            componentShowImpl.call((ServiceController)controller, request, done);
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
