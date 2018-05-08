package cn.migu.newportal.selection.service;


import javax.annotation.Resource;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

import cn.migu.newportal.selection.service.selection.BookCommentInfo;
import cn.migu.selection.proto.base.BookCommentResponseOuterClass.BookCommentResponse;
import cn.migu.selection.proto.base.ComponentRequestOuterClass.ComponentRequest;
import cn.migu.selection.proto.bookcommentservice._BookCommentService.BookCommentService;
import cn.migu.wheat.service.ServiceController;
import cn.migu.wheat.service.ServiceImpl;
import cn.migu.wheat.service.ServiceMethod;

/**
 * 图书评论组件服务
 *
 * @author fengjiangtao
 * @version C10 2017年6月14日
 * @since SDP V300R003C10
 */
public class BookCommentServiceImpl extends BookCommentService implements BookCommentService.Interface,ServiceImpl
{
    @Resource(name="bookCommentInfo")
    private BookCommentInfo bookCommentInfo;
    
    private String serviceName;
    
    private String version;
    
    private ServiceMethod[] methods = null;
    
    public BookCommentServiceImpl()
    {
    }
    
    public void init()
    {
        methods = new ServiceMethod[] {bookCommentInfo};
    }
    
    @Override
    public void getBookCommentInfo(RpcController controller, ComponentRequest request,
        RpcCallback<BookCommentResponse> done)
    {
        if (controller instanceof ServiceController)
        {
            bookCommentInfo.call((ServiceController)controller, request, done);
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
