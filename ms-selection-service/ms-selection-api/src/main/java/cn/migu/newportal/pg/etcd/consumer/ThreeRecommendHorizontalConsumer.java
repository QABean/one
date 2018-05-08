package cn.migu.newportal.pg.etcd.consumer;


import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.ServiceDescriptor;

import cn.migu.millet.MilletContext;
import cn.migu.selection.proto.threerecommendhorizontalservice._ThreeRecommendHorizontalResponse.ThreeRecommendHorizontalService;
import cn.migu.wheat.service.ServiceConsumer;

public class ThreeRecommendHorizontalConsumer extends ServiceConsumer
{
    private static final String SERVICE = "ms.selection.threeRecommendHorizontalService";
    
    private static Descriptors.ServiceDescriptor descriptor = ThreeRecommendHorizontalService.getDescriptor();
    
    public ThreeRecommendHorizontalConsumer(MilletContext context,String version)
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
