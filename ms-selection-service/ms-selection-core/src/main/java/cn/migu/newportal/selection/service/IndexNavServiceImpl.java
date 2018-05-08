package cn.migu.newportal.selection.service;

import javax.annotation.Resource;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

import cn.migu.newportal.selection.service.selection.IndexNavInfoImpl;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.IndexNavResponseOuterClass.IndexNavResponse;
import cn.migu.selection.proto.indexnavservice._IndexNavService.IndexNavService;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceImpl;
import cn.migu.wheat.service.ServiceMethod;

public class IndexNavServiceImpl extends IndexNavService  implements  IndexNavService.Interface,ServiceImpl
{
    
    @Resource(name="indexNavInfoImpl")
    private IndexNavInfoImpl indexNavInfoImpl;
    
    private String serviceName;
    
    private String version;
    
    private ServiceMethod[] methods = null;
    
    public IndexNavServiceImpl(){
        super();
    }
    
    public void init() {
        methods = new ServiceMethod[] {indexNavInfoImpl};
    }
    @Override
    public void getIndexNavInfo(RpcController controller, ComponentRequest req, RpcCallback<IndexNavResponse> res)
    {
       if(controller instanceof ServiceController){
           
           indexNavInfoImpl.call((ServiceController)controller, req, res);
           
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
