package cn.migu.newportal.pg.etcd.consumer;


import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.ServiceDescriptor;

import cn.migu.millet.MilletContext;
import cn.migu.selection.proto.indexnavservice._IndexNavService.IndexNavService;
import cn.migu.wheat.service.ServiceConsumer;

public class IndexNavConsumer extends ServiceConsumer
{
    private static final String SERVICE = "ms.selection.indexNavService";
    
    private static Descriptors.ServiceDescriptor descriptor = IndexNavService.getDescriptor();
    
    public IndexNavConsumer(MilletContext context,String version)
    {
        super(context,SERVICE,version);
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
