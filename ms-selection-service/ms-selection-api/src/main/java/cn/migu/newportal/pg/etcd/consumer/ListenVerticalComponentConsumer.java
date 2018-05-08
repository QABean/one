package cn.migu.newportal.pg.etcd.consumer;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.ServiceDescriptor;

import cn.migu.millet.MilletContext;
import cn.migu.selection.proto.listenVerticalComponentService._ListenVerticalComponentService.ListenVerticalComponentService;
import cn.migu.wheat.service.ServiceConsumer;

public class ListenVerticalComponentConsumer extends ServiceConsumer
{
    
    private static final String SERVICE = "ms.selection.listenVerticalComponentService";
    
    private static Descriptors.ServiceDescriptor descriptor = ListenVerticalComponentService.getDescriptor();
    
    public ListenVerticalComponentConsumer(MilletContext context,String version)
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
