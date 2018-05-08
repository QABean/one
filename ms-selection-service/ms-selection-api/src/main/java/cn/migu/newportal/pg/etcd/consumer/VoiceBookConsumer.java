package cn.migu.newportal.pg.etcd.consumer;


import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.ServiceDescriptor;

import cn.migu.millet.MilletContext;
import cn.migu.selection.proto.VoiceBookService._VoiceBookService.VoiceBookService;
import cn.migu.wheat.service.ServiceConsumer;

public class VoiceBookConsumer extends  ServiceConsumer {
	
    private static final String SERVICE = "ms.selection.voiceBookService";
    
    private static Descriptors.ServiceDescriptor descriptor = VoiceBookService.getDescriptor();
    
    public VoiceBookConsumer(MilletContext context,String version)
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
