package cn.migu.newportal.pg.etcd.consumer;


import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.ServiceDescriptor;

import cn.migu.millet.MilletContext;
import cn.migu.selection.proto.recommendservice._RecommendService.RecommendService;
import cn.migu.wheat.service.ServiceConsumer;

public class RecommendConsumer extends ServiceConsumer
{
    
    private static final String SERVICE = "ms.selection.recommendService";
    
    private static Descriptors.ServiceDescriptor descriptor = RecommendService.getDescriptor();
    
    public RecommendConsumer(MilletContext context,String version)
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
