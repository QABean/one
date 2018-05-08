package cn.migu.newportal.selection.Exception;

import cn.migu.newportal.util.other.NumberTools;


/**
 * 
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  yangxd
 * @version  [版本号, 2017年4月26日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class PortalException extends Throwable
{
    /**
     * 序列号
     */
    private static final long serialVersionUID = 6849794470754667711L;

    private int errorCode = 500;

    /**
     * 默认构造器
     */
    public PortalException(int errorCode)
    {
        super();
        this.errorCode = errorCode;
    }

    public PortalException(String message)
    {
        super(message);
    }

    public PortalException(Throwable throwable)
    {
        super(throwable);
    }

    /**
     * 通过异常码和异常信息构造异常类。
     */
    public PortalException(int errorCode, String message)
    {
        super(message);
        this.errorCode = errorCode;
    }
    
    /**
     * 通过异常码和异常信息构造异常类。
     */
    public PortalException(String errorCode, String message)
    {
        super(message);
        this.errorCode = NumberTools.toInt(errorCode, 500);
    }

    public PortalException(String message, Throwable throwable)
    {
        super(message, throwable);
    }
    
    public String getExceptionCode()
    {
        return String.valueOf(this.errorCode);
    }
    
    public int getErrorCode()
    {
        return this.errorCode;
    }

    /**
     * 返回该异常类的基本信息。
     * @return String
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer(512);
        sb.append("PortalException[exceptionCode=");
        sb.append(this.errorCode);
        sb.append("; Message =");
        sb.append(this.getMessage());
        sb.append(']');
        return sb.toString();
    }

    public void setErrorCode(int errorCode)
    {
        this.errorCode = errorCode;
    }
}
