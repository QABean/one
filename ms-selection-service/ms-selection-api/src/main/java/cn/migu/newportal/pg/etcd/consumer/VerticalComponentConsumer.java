package cn.migu.newportal.pg.etcd.consumer;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.ServiceDescriptor;

import cn.migu.millet.MilletContext;
import cn.migu.selection.proto.verticalcomponentservice._VerticalComponentService.VerticalComponentService;
import cn.migu.wheat.service.ServiceConsumer;

public class VerticalComponentConsumer extends ServiceConsumer{
    
	private static final String SERVICE = "ms.selection.VerticalComponentService";
    
	private static Descriptors.ServiceDescriptor descriptor = VerticalComponentService.getDescriptor();
   
    public VerticalComponentConsumer(MilletContext context,String version) 
    {
		super(context,SERVICE,version);
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
