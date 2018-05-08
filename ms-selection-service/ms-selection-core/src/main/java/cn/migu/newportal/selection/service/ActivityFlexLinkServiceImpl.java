package cn.migu.newportal.selection.service;

import javax.annotation.Resource;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

import cn.migu.newportal.selection.service.selection.ActivityFlexLinkInfoImpl;
import cn.migu.selection.proto.activityflexlinkservice._ActivityFlexLinkService.ActivityFlexLinkService;
import cn.migu.selection.proto.base.ActivityFlexLinkResponseOuterClass.ActivityFlexLinkResponse;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceImpl;
import cn.migu.wheat.service.ServiceMethod;

public class ActivityFlexLinkServiceImpl extends ActivityFlexLinkService implements ActivityFlexLinkService.Interface,ServiceImpl {

	@Resource(name="activityFlexLinkServiceImpl")
	ActivityFlexLinkInfoImpl activityFlexLinkInfoImpl;
	
    private String serviceName;
    
    private String version;
    
    private ServiceMethod[] methods = null;

	public ActivityFlexLinkServiceImpl() {

		super();

	}

	public void init() {

	    methods = new ServiceMethod[] {activityFlexLinkInfoImpl};
	}

	@Override
	public void getActivityFlexLinkInfo(RpcController controller, ComponentRequest request,
			RpcCallback<ActivityFlexLinkResponse> response) {

		if (controller instanceof ServiceController) {

			activityFlexLinkInfoImpl.call((ServiceController) controller, request, response);

		} else {

			controller.setFailed("invalid controller");
		}

	}

    public String getServiceName()
    {
        return serviceName;
    }

    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public ServiceMethod[] getMethods()
    {
        return methods;
    }

    public void setMethods(ServiceMethod[] methods)
    {
        this.methods = methods;
    }
	
}
