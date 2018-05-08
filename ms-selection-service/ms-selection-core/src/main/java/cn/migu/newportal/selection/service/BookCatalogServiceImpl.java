package cn.migu.newportal.selection.service;

import javax.annotation.Resource;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

import cn.migu.newportal.selection.service.selection.GetBookCatalogInfoMethodImpl;
import cn.migu.newportal.selection.service.selection.ListenBookCatalogInfo;
import cn.migu.selection.proto.base.BookCatalogResponseOuterClass.BookCatalogResponse;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.base.ListenBookCatalogResponseOuterClass.ListenBookCatalogResponse;
import cn.migu.selection.proto.bookcatalogservice._BookCatalogService.BookCatalogService;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceImpl;
import cn.migu.wheat.service.ServiceMethod;

/**
 * 图书目录组件服务
 *
 * @author fengjiangtao
 * @version C10 2017年6月18日
 * @since SDP V300R003C10
 */
public class BookCatalogServiceImpl extends BookCatalogService implements BookCatalogService.Interface, ServiceImpl
{
    @Resource(name = "bookCatalogInfo")
    private GetBookCatalogInfoMethodImpl bookCatalogInfo;
    
    @Resource(name = "listenBookCatalogInfo")
    private ListenBookCatalogInfo listenBookCatalogInfo;
    
    private String serviceName;
    
    private String version;
    
    private ServiceMethod[] methods = null;
    
    public BookCatalogServiceImpl()
    {
    }
    
    public void init()
    {
        methods = new ServiceMethod[] {bookCatalogInfo, listenBookCatalogInfo};
    }
    
    @Override
    public void getBookCatalogInfo(RpcController controller, ComponentRequest request,
        RpcCallback<BookCatalogResponse> done)
    {
        if (controller instanceof ServiceController)
        {
            bookCatalogInfo.call((ServiceController)controller, request, done);
        }
        else
        {
            controller.setFailed("invalid controller");
        }
        
    }
    
    @Override
    public void getListenBookCatalogInfo(RpcController controller, ComponentRequest request,
        RpcCallback<ListenBookCatalogResponse> done)
    {
        if (controller instanceof ServiceController)
        {
            listenBookCatalogInfo.call((ServiceController)controller, request, done);
        }
        else
        {
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
