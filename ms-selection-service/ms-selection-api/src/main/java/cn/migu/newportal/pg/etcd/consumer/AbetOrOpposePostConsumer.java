package cn.migu.newportal.pg.etcd.consumer;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.ServiceDescriptor;

import cn.migu.millet.MilletContext;
import cn.migu.selection.proto.abetoropposepostservice._AbetOrOpposePostService.AbetOrOpposePostService;
import cn.migu.wheat.service.ServiceConsumer;

public class AbetOrOpposePostConsumer extends ServiceConsumer
{
    
    private static final String SERVICE = "ms.selection.abetOrOpposePostService";
    
    private static Descriptors.ServiceDescriptor descriptor = AbetOrOpposePostService.getDescriptor();
    
    public AbetOrOpposePostConsumer(MilletContext context,String version)
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
