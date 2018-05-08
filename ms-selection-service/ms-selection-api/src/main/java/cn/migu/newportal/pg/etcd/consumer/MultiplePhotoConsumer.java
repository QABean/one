package cn.migu.newportal.pg.etcd.consumer;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.ServiceDescriptor;

import cn.migu.millet.MilletContext;
import cn.migu.selection.proto.multiplephotoservice._MultiplePhotoService.MultiplePhotoService;
import cn.migu.wheat.service.ServiceConsumer;

public class MultiplePhotoConsumer extends ServiceConsumer
{
    
    private static final String SERVICE = "ms.selection.multiplePhotoService";
    
    private static Descriptors.ServiceDescriptor descriptor = MultiplePhotoService.getDescriptor();
    
    public MultiplePhotoConsumer(MilletContext context, String version)
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
