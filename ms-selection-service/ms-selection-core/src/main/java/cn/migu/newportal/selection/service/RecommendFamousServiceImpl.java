package cn.migu.newportal.selection.service;

import javax.annotation.Resource;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

import cn.migu.newportal.selection.service.selection.RecommendFamousMethodImpl;
import cn.migu.selection.proto.RecommendFamousService._RecommendFamousService.RecommendFamousService;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.RecommendFamousResponseOuterClass.RecommendFamousResponse;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceImpl;
import cn.migu.wheat.service.ServiceMethod;

public class RecommendFamousServiceImpl extends RecommendFamousService implements RecommendFamousService.Interface,ServiceImpl
{
    @Resource(name="recommendFamousInfo")
    RecommendFamousMethodImpl recommendFamousInfo;
    private String serviceName;
    
    private String version;
    
    private ServiceMethod[] methods = null;
    
    public RecommendFamousServiceImpl()
    {
        super();
    }
    
    public void init()
    {
        methods = new ServiceMethod[] {recommendFamousInfo};
    }
    
    @Override
    public void getRecommendFamousInfo(RpcController controller, ComponentRequest request,
        RpcCallback<RecommendFamousResponse> done)
    {
        if (controller instanceof ServiceController)
        {
            recommendFamousInfo .call((ServiceController)controller, request, done);

        }
        else
        {
            
            controller.setFailed("RecommendFamousInfo controller");
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
