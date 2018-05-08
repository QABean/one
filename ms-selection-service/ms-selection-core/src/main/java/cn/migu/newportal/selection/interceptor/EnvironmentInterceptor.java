package cn.migu.newportal.selection.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.migu.newportal.util.bean.common.Environment;
import cn.migu.newportal.util.threadlocal.ThreadLocalUtils;


/**
 * 初始化本地线程环境变量拦截器
 *
 * @author zhangjiadao
 * @version C10 Apr 7, 2017
 * @since SDP V300R003C10
 */
public class EnvironmentInterceptor implements HandlerInterceptor
{
    
    /** 日志对象 */
    private static Logger logger = LoggerFactory.getLogger(EnvironmentInterceptor.class.getName());
    
  //  public static final ThreadLocal<Environment> environment = new ThreadLocal<Environment>();
    
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2)
        throws Exception
    {
        logger.debug("EnvironmentFilter.doFilter requesturl:{}",request.getRequestURL());
 //      environment.remove();
        ThreadLocalUtils.getInstance().delEnvironment();
        Environment myEnv = new Environment();
        myEnv.setRequest((HttpServletRequest)request);
        myEnv.setResponse((HttpServletResponse)response);
        ThreadLocalUtils.getInstance().setEnvironment(myEnv);
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception arg3)
        throws Exception
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
        throws Exception
    {
        // TODO Auto-generated method stub
        
    }
    
   
}
