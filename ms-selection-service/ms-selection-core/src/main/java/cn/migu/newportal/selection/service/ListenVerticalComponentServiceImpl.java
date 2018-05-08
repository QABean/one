package cn.migu.newportal.selection.service;

import javax.annotation.Resource;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

import cn.migu.newportal.selection.service.selection.ListenVerticalComponentShowInfoImpl;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.ListenVerticalComponentResponseOuterClass.ListenVerticalComponentResponse;
import cn.migu.selection.proto.listenVerticalComponentService._ListenVerticalComponentService.ListenVerticalComponentService;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceImpl;
import cn.migu.wheat.service.ServiceMethod;

/**
 * 听书横向内容
 *
 * @author yejiaxu
 * @version C10 2017年7月17日
 * @since SDP V300R003C10
 */
public class ListenVerticalComponentServiceImpl extends ListenVerticalComponentService
    implements ListenVerticalComponentService.Interface,ServiceImpl
{
    
    @Resource(name="listenVerticalComponentShowInfoImpl")
    private ListenVerticalComponentShowInfoImpl listenVerticalComponentShowInfoImpl;
    
    private String serviceName;
    
    private String version;
    
    private ServiceMethod[] methods = null;
    
    public ListenVerticalComponentServiceImpl()
    {
        super();
    }
    
    public void init()
    {
        methods = new ServiceMethod[] {listenVerticalComponentShowInfoImpl};
    }
    
    @Override
    public void getListenVerticalComponentShowInfo(RpcController controller, ComponentRequest request,
        RpcCallback<ListenVerticalComponentResponse> done)
    {
        if (controller instanceof ServiceController)
        {
            listenVerticalComponentShowInfoImpl.call((ServiceController)controller, request, done);
        }
        else
        {
            controller.setFailed("listenVerticalComponentShowInfo controller");
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
