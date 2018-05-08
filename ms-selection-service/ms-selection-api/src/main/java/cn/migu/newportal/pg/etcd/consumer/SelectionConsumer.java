package cn.migu.newportal.pg.etcd.consumer;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.ServiceDescriptor;
import cn.migu.millet.MilletContext;
import cn.migu.selection.proto.selectionService._SelectionService.SelectionService;
import cn.migu.wheat.service.ServiceConsumer;
/**
 * 
 * SelectionConsumer定义
 *
 * @author liuchuanzhuang
 * @version C10 2017年12月13日
 * @since SDP V300R003C10
 */
public class SelectionConsumer extends ServiceConsumer
{
    
    private static final String SERVICE = "ms.selection.selectionService";
    
    private static Descriptors.ServiceDescriptor descriptor = SelectionService.getDescriptor();
    
    public SelectionConsumer(MilletContext context, String version)
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
