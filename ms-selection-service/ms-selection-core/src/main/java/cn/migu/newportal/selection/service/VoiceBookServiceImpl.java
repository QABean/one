package cn.migu.newportal.selection.service;

import javax.annotation.Resource;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

import cn.migu.newportal.selection.service.selection.VoiceBookInfo;
import cn.migu.selection.proto.VoiceBookService._VoiceBookService.VoiceBookService;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.VoiceBookResponseOuterClass.VoiceBookResponse;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceImpl;
import cn.migu.wheat.service.ServiceMethod;

public class VoiceBookServiceImpl  extends VoiceBookService implements VoiceBookService.Interface,ServiceImpl
{
    @Resource(name="voiceBookInfo")
    private VoiceBookInfo freeListenBookInfo;
    
    private String serviceName;
    
    private String version;
    
    private ServiceMethod[] methods = null;
    
    public VoiceBookServiceImpl()
    {
        super();
    }
    
    public void init()
    {
        methods = new ServiceMethod[] {freeListenBookInfo};
    }
    
    @Override
    public void voiceBookService(RpcController controller, ComponentRequest request,
        RpcCallback<VoiceBookResponse> done)
    {
        if (controller instanceof ServiceController)
        {
            freeListenBookInfo.call((ServiceController)controller, request, done);
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
