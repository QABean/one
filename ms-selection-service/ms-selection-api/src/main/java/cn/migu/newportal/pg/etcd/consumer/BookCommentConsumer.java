package cn.migu.newportal.pg.etcd.consumer;


import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.ServiceDescriptor;

import cn.migu.millet.MilletContext;
import cn.migu.selection.proto.bookcommentservice._BookCommentService.BookCommentService;
import cn.migu.wheat.service.ServiceConsumer;

public class BookCommentConsumer extends ServiceConsumer
{
    
    private static final String SERVICE = "ms.selection.bookCommentService";
    
    private static Descriptors.ServiceDescriptor descriptor = BookCommentService.getDescriptor();
    
    public BookCommentConsumer(MilletContext context,String version)
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
