package cn.migu.newportal.selection.service;

import javax.annotation.Resource;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

import cn.migu.newportal.selection.service.selection.ExchangeLotteryMethodImpl;
import cn.migu.selection.proto.base.CommonAjaxResponseOuterClass.CommonAjaxResponse;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.portalbookLinkservice._RaffleService.RaffleService;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceImpl;
import cn.migu.wheat.service.ServiceMethod;

/**
 * 抽奖
 * 
 * @author hushanqing
 * @version C10 2017年9月27日
 * @since SDP V300R003C10
 */
public class RaffleServiceImpl extends RaffleService implements RaffleService.Interface, ServiceImpl
{
    @Resource(name = "exchangeLotteryMethodImpl")
    ExchangeLotteryMethodImpl exchangeLotteryMethodImpl;
    
    private String serviceName;
    
    private String version;
    
    private ServiceMethod[] methods = null;
    
    public RaffleServiceImpl()
    {
        
    }
    
    public void init()
    {
        methods = new ServiceMethod[] {exchangeLotteryMethodImpl};
    }
    
    @Override
    public void exchangeLottery(RpcController controller, ComponentRequest request,
        RpcCallback<CommonAjaxResponse> done)
    {
        if (controller instanceof ServiceController)
        {
            exchangeLotteryMethodImpl.call((ServiceController)controller, request, done);
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
