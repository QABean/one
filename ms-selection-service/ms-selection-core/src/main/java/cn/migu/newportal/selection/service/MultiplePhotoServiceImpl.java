package cn.migu.newportal.selection.service;

import javax.annotation.Resource;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

import cn.migu.newportal.selection.service.selection.MultplePhotoInfo;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.MultiplePhotoResponseOuterClass.MultiplePhotoResponse;
import cn.migu.selection.proto.multiplephotoservice._MultiplePhotoService.MultiplePhotoService;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceImpl;
import cn.migu.wheat.service.ServiceMethod;

public class MultiplePhotoServiceImpl extends MultiplePhotoService implements MultiplePhotoService.Interface,ServiceImpl {

	@Resource(name="multplePhotoInfo")
	private MultplePhotoInfo multiplePhotoInfo;
	
    private String serviceName;
    
    private String version;
    
    private ServiceMethod[] methods = null;

	public MultiplePhotoServiceImpl() {
	}

	public void init() {
	    methods = new ServiceMethod[] {multiplePhotoInfo};
	}

	@Override
	public void getComponentShowInfo(RpcController controller, ComponentRequest request,
			RpcCallback<MultiplePhotoResponse> done) {
		if (controller instanceof ServiceController) {
			multiplePhotoInfo.call((ServiceController) controller, request, done);
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
