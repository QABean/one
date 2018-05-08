package cn.migu.newportal.pg.etcd.consumer;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.ServiceDescriptor;

import cn.migu.millet.MilletContext;
import cn.migu.selection.proto.bookcatalogservice._BookCatalogService.BookCatalogService;
import cn.migu.wheat.service.ServiceConsumer;

public class BookCatalogConsumer extends ServiceConsumer
{
    
    private static final String SERVICE = "ms.selection.bookCatalogService";
    
    private static Descriptors.ServiceDescriptor descriptor = BookCatalogService.getDescriptor();
    
    public BookCatalogConsumer(MilletContext context,String version)
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
