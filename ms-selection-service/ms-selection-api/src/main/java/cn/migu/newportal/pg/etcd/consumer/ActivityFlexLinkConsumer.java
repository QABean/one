package cn.migu.newportal.pg.etcd.consumer;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.ServiceDescriptor;

import cn.migu.millet.MilletContext;
import cn.migu.selection.proto.activityflexlinkservice._ActivityFlexLinkService.ActivityFlexLinkService;
import cn.migu.wheat.service.ServiceConsumer;

public class ActivityFlexLinkConsumer extends ServiceConsumer
{
    
    private static final String SERVICE = "ms.selection.activityFlexLinkServcie";
    
    private static Descriptors.ServiceDescriptor descriptor = ActivityFlexLinkService.getDescriptor();
    
    public ActivityFlexLinkConsumer(MilletContext context, String version)
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
