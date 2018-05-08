package cn.migu.newportal.pg.etcd.consumer;


import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.ServiceDescriptor;

import cn.migu.millet.MilletContext;
import cn.migu.selection.proto.booksheetervice._BookSheetService.BookSheetService;
import cn.migu.wheat.service.ServiceConsumer;

public class BookSheetInfoConsumer extends ServiceConsumer{

    private static final String SERVICE = "ms.selection.bookSheetService";
    
    private static Descriptors.ServiceDescriptor descriptor = BookSheetService.getDescriptor();
    
    public BookSheetInfoConsumer(MilletContext context,String version)
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
