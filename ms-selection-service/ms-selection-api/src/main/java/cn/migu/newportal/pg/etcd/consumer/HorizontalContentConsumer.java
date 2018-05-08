package cn.migu.newportal.pg.etcd.consumer;


import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.ServiceDescriptor;

import cn.migu.millet.MilletContext;
import cn.migu.selection.proto.horizontalcontentservice._HorizontalContentService.HorizontalContentService;
import cn.migu.wheat.service.ServiceConsumer;

public class HorizontalContentConsumer extends ServiceConsumer{
	
	private static final String SERVICE = "ms.selection.horizontalContentService";
	
	private static Descriptors.ServiceDescriptor descriptor = HorizontalContentService.getDescriptor();
	
	public HorizontalContentConsumer(MilletContext context,String version){
		super(context,SERVICE,version);
	}
	
	public void init(){
		super.initialize();
	}

    @Override
    protected ServiceDescriptor getServiceDescriptor()
    {
        return descriptor;
    }
}
