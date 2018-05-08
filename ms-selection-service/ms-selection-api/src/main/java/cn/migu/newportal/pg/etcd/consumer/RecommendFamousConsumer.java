package cn.migu.newportal.pg.etcd.consumer;


import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.ServiceDescriptor;

import cn.migu.millet.MilletContext;
import cn.migu.selection.proto.RecommendFamousService._RecommendFamousService.RecommendFamousService;
import cn.migu.wheat.service.ServiceConsumer;

public class RecommendFamousConsumer extends ServiceConsumer
{
    private static final String SERVICE = "ms.selection.RecommendFamousService";
    
    private static Descriptors.ServiceDescriptor descriptor = RecommendFamousService.getDescriptor();
    
    public RecommendFamousConsumer(MilletContext context,String version)
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
