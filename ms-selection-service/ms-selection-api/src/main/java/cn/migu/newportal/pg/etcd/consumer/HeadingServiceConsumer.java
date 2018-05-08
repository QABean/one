package cn.migu.newportal.pg.etcd.consumer;


import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.ServiceDescriptor;

import cn.migu.millet.MilletContext;
import cn.migu.selection.proto.headingservice._HeadingService.HeadingService;
import cn.migu.wheat.service.ServiceConsumer;

public class HeadingServiceConsumer extends ServiceConsumer
{
    private static final String SERVICE = "ms.selection.headingService";
    
    private static Descriptors.ServiceDescriptor descriptor = HeadingService.getDescriptor();
    
    public HeadingServiceConsumer(MilletContext context,String version)
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
