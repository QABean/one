package cn.migu.newportal.selection.service;

import javax.annotation.Resource;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

import cn.migu.newportal.selection.service.selection.TestInfoImpl;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;

import cn.migu.selection.proto.base.TestResponseOuterClass.TestResponse;
import cn.migu.selection.proto.test._TestService.TestService;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceImpl;
import cn.migu.wheat.service.ServiceMethod;

public class TestServiceImpl extends TestService implements TestService.Interface ,ServiceImpl
{
    @Resource(name="testInfoImpl")
    private TestInfoImpl testInfoImpl;
    
    private String serviceName;
    
    private String version;
    
    private ServiceMethod[] methods = null;
    
    public TestServiceImpl() {
          super();
    }
    public void init() {
        methods = new ServiceMethod[] {testInfoImpl};
    }
    
    
    @Override
    public void info(RpcController controller, ComponentRequest request, RpcCallback<TestResponse> done)
    {
        if(controller instanceof ServiceController){
            testInfoImpl.call((ServiceController) controller, request, done);
        }else{
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
