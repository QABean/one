package cn.migu.newportal.pg.etcd.consumer;


import com.google.protobuf.Descriptors.ServiceDescriptor;

import cn.migu.millet.MilletContext;
import cn.migu.wheat.service.ServiceConsumer;

public class UserLoveConsumer extends ServiceConsumer
{
    
    private static final String SERVICE = "ms.selection.userLoveService";
    
    
    
    public UserLoveConsumer(MilletContext context,String version)
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
        // TODO Auto-generated method stub
        return null;
    }
}
