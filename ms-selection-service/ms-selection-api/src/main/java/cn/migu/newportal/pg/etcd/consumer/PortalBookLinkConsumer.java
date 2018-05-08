package cn.migu.newportal.pg.etcd.consumer;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.ServiceDescriptor;

import cn.migu.millet.MilletContext;
import cn.migu.selection.proto.portalbookLinkservice._PortalBookLinkService.PortalBookLinkService;
import cn.migu.wheat.service.ServiceConsumer;

public class PortalBookLinkConsumer extends ServiceConsumer
{
    private static final String SERVICE = "ms.selection.portalBookLinkService";
    
    private static Descriptors.ServiceDescriptor descriptor = PortalBookLinkService.getDescriptor();
    
    public PortalBookLinkConsumer(MilletContext context, String version)
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
