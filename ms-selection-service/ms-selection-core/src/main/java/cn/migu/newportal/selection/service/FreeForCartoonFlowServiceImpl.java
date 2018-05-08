package cn.migu.newportal.selection.service;

import javax.annotation.Resource;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

import cn.migu.newportal.selection.service.selection.FreeForCartoonFlowInfo;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.FreeForCartoonFlowResponseOuterClass.FreeForCartoonFlowResponse;
import cn.migu.selection.proto.freeforcartoonflowservice._FreeForCartoonFlowService.FreeForCartoonFlowService;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceImpl;
import cn.migu.wheat.service.ServiceMethod;

public class FreeForCartoonFlowServiceImpl extends FreeForCartoonFlowService
    implements FreeForCartoonFlowService.Interface, ServiceImpl
{
    @Resource(name = "freeForCartoonFlowInfo")
    private FreeForCartoonFlowInfo freeForCartoonFlowInfo;
    
    private String serviceName;
    
    private String version;
    
    private ServiceMethod[] methods = null;
    
    public FreeForCartoonFlowServiceImpl()
    {
    }
    
    public void init()
    {
        
        methods = new ServiceMethod[] {freeForCartoonFlowInfo};
    }
    
    @Override
    public void getFreeCartoonFlowShowInfo(RpcController controller, ComponentRequest request,
        RpcCallback<FreeForCartoonFlowResponse> done)
    {
        if (controller instanceof ServiceController)
        {
            freeForCartoonFlowInfo.call((ServiceController)controller, request, done);
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
