package cn.migu.newportal.pg.etcd.consumer;


import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.ServiceDescriptor;

import cn.migu.millet.MilletContext;
import cn.migu.selection.proto.portalbookLinkservice._RaffleService.RaffleService;
import cn.migu.wheat.service.ServiceConsumer;

public class RaffleServiceConsumer extends ServiceConsumer
{
    private static final String SERVICE = "ms.selection.raffleService";
    
    private static Descriptors.ServiceDescriptor descriptor = RaffleService.getDescriptor();
    
    public RaffleServiceConsumer(MilletContext context, String version)
    {
        super(context,SERVICE, version);
    }
    
    public void init(){
        super.initialize();
    }

    @Override
    protected ServiceDescriptor getServiceDescriptor()
    {
        return descriptor;
    }
    
}
