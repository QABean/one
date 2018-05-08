package cn.migu.newportal.pg.etcd.consumer;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.ServiceDescriptor;

import cn.migu.millet.MilletContext;
import cn.migu.selection.proto.bannerswiper._BannerSwiperService.BannerSwiperService;
import cn.migu.wheat.service.ServiceConsumer;

public class BannerSwiperConsumer extends ServiceConsumer
{
    
    private static final String SERVICE = "ms.selection.bannerSwiperService";
    
    private static Descriptors.ServiceDescriptor descriptor = BannerSwiperService.getDescriptor();
    
    public BannerSwiperConsumer(MilletContext context, String version)
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
