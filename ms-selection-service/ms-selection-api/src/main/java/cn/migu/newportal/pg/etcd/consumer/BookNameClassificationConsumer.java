package cn.migu.newportal.pg.etcd.consumer;


import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.ServiceDescriptor;

import cn.migu.millet.MilletContext;
import cn.migu.selection.proto.booknameclassificationservice._BookNameClassificationService.BookNameClassificationService;
import cn.migu.wheat.service.ServiceConsumer;

public class BookNameClassificationConsumer extends ServiceConsumer
{
    private static final String SERVICE = "ms.selection.bookNameClassificationService";
    
    private static Descriptors.ServiceDescriptor descriptor = BookNameClassificationService.getDescriptor();
    
    public BookNameClassificationConsumer(MilletContext context,String version)
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
