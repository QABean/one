package cn.migu.newportal.pg.etcd.consumer;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.ServiceDescriptor;

import cn.migu.millet.MilletContext;
import cn.migu.selection.proto.ActivityBannerService._ActivityBannerService.ActivityBannerService;
import cn.migu.wheat.service.ServiceConsumer;

public class ActivityBannerConsumer extends ServiceConsumer
{
    private static final String SERVICE = "ms.selection.ActivityBannerService";
    
    private static Descriptors.ServiceDescriptor descriptor = ActivityBannerService.getDescriptor();
    
    public ActivityBannerConsumer(MilletContext context,String version)
    {
        super(context, SERVICE, version);
    }
    
    public void init()
    {
        super.initialize();
    }


    @Override
    protected ServiceDescriptor getServiceDescriptor()
    {
        return descriptor;
    }
    
}
