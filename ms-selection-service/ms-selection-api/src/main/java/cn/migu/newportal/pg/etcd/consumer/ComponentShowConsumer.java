package cn.migu.newportal.pg.etcd.consumer;


import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.ServiceDescriptor;

import cn.migu.millet.MilletContext;
import cn.migu.selection.proto.ComponentShowService._ComponentShowService.ComponentShowService;
import cn.migu.wheat.service.ServiceConsumer;

public class ComponentShowConsumer extends ServiceConsumer{

    private static final String SERVICE = "ms.selection.ImageTextMix";
    
    private static Descriptors.ServiceDescriptor descriptor = ComponentShowService.getDescriptor();
    
    public ComponentShowConsumer(MilletContext context,String version)
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
