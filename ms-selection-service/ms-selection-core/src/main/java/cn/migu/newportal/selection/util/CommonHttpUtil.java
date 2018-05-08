package cn.migu.newportal.selection.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.migu.newportal.util.http.EnvironmentInterceptor;

/**
 * 
 * 工具类,获取请求、响应、参数等
 * 
 * @author zhangjiadao
 * @version C10 Apr 7, 2017
 * @since SDP V300R003C10
 */
public class CommonHttpUtil
{
    private static CommonHttpUtil instance  = new CommonHttpUtil();
    
    public static CommonHttpUtil getInstance()
    {
        return instance;
    }
    
    /**
     * 获取当前请求的request对象
     * 
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static HttpServletRequest getRequest()
    {
        return EnvironmentInterceptor.environment.get().getRequest();
    }
    
    /**
     * 获取当前请求的request对象
     * 
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static HttpServletResponse getResponse()
    {
        return EnvironmentInterceptor.environment.get().getResponse();
    }

}
