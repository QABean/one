package cn.migu.newportal.selection.service;

import javax.annotation.Resource;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

import cn.migu.newportal.selection.service.selection.BookSheetImpl;
import cn.migu.selection.proto.base.BookSheetInfoResponseOuterClass.BookSheetInfoResponse;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.booksheetervice._BookSheetService.BookSheetService;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceImpl;
import cn.migu.wheat.service.ServiceMethod;

/**
 * 内容书单服务类
 * 
 * @author hanyafei
 *
 */
public class BookSheetServiceImpl extends BookSheetService implements BookSheetService.Interface, ServiceImpl
{
    
    @Resource(name = "bookSheetImpl")
    BookSheetImpl bookSheetImpl;
    
    private String serviceName;
    
    private String version;
    
    private ServiceMethod[] methods = null;
    
    public BookSheetServiceImpl()
    {
        super();
    }
    
    public void init()
    {
        
        methods = new ServiceMethod[] {bookSheetImpl};
        
    }
    
    @Override
    public void getBookSheetInfo(RpcController controller, ComponentRequest request,
        RpcCallback<BookSheetInfoResponse> done)
    {
        if (controller instanceof ServiceController)
        {
            bookSheetImpl.call((ServiceController)controller, request, done);
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
