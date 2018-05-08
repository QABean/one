package cn.migu.newportal.selection.controller;
/*package org.ms.selection.controller;*/

/**
 * 这个例子是用于多个service部署在同一个服务器上的，需要接管ROOT
 * @author shenjundong
 *
 */
/*
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.http.HttpUtil;
import cn.migu.wheat.ErrorCode;
import cn.migu.wheat.XHeaders;
import cn.migu.wheat.context.Extractors;
import cn.migu.wheat.service.InvokeContext;
import cn.migu.wheat.service.ServiceManager;
import cn.migu.wheat.service.ServiceProvider;
import cn.migu.wheat.trace.GlobalTracer;

@Controller
@RequestMapping
public class ServiceController
{
    
    @Resource
    ServiceManager serverManager;
    
    public ServiceController()
    {
    }
    
    @RequestMapping(value = "/{service}/{method}", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody void invoke(@PathVariable("service") String service, @PathVariable("method") String method,
        HttpServletRequest request, HttpServletResponse response)
    {
        InvokeContext context = Extractors.extract(request);
        GlobalTracer.get().joinContext(context);
        HttpUtil.setAttribute(ParamConstants.CURRENT_INVOKECONTEXT, context);
        ServiceProvider provider = serverManager.get(service);
        if (null != provider)
        {
            provider.servletInvoke(method, context, request, response);
        }
        else
        {
            response.setStatus(400);
            response.setHeader(XHeaders.REQUEST_ID, context.getRequestId());
            response.setIntHeader(XHeaders.RESULT_CODE, ErrorCode.NO_SUCH_SERVICE);
        }
    }
    
}
*/