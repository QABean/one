package cn.migu.newportal.selection.bean;

import java.io.Serializable;

/**
 * 公共参数
 * @author hushanqing
 * @version C10 2017年7月27日
 * @since 
 */
public class BaseResponseBean implements Serializable
{
    private static final long serialVersionUID = 1L;
    /**组件编号*/
    protected String pluginCode;
    /**响应码*/
    protected String status;
    /**数据是否可见*/
    protected String isvisable;

    public String getPluginCode()
    {
        return pluginCode;
    }

    public void setPluginCode(String pluginCode)
    {
        this.pluginCode = pluginCode;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getIsvisable()
    {
        return isvisable;
    }

    public void setIsvisable(String isvisable)
    {
        this.isvisable = isvisable;
    }
}
