package cn.migu.newportal.pg.etcd.consumer;


import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.ServiceDescriptor;

import cn.migu.millet.MilletContext;
import cn.migu.selection.proto.bookcontentdescservice._BookContentDescService.BookContentDescService;
import cn.migu.wheat.service.ServiceConsumer;

public class BookContentDescConsumer extends ServiceConsumer
{
    private static final String SERVICE = "ms.selection.bookContentDescService";
    
    private static Descriptors.ServiceDescriptor descriptor = BookContentDescService.getDescriptor();
    
    public BookContentDescConsumer(MilletContext context,String version)
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
