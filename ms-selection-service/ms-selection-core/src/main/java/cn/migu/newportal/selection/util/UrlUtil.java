package cn.migu.newportal.selection.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.migu.newportal.cache.util.UesServerServiceUtils;
import cn.migu.newportal.cache.util.UesServiceUtils;
import cn.migu.newportal.util.http.UrlTools;
import cn.migu.newportal.util.kafka.PropertiesConUtil;
import cn.migu.newportal.util.other.Util;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.wheat.service.ServiceController;

/**
 * 
 * URL相关处理类
 * 
 * @author hKF68354
 * @version [版本号, Mar 6, 2012]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class UrlUtil
{
    // 日志对象
    private static final Logger LOG = LogManager.getLogger(UrlUtil.class);
    /**
     * 获取URL前缀
     * 
     *
     * @author zhaoxinwei
     * @param controller
     * @return
     */
    public static String getUrlPrefix(ServiceController controller)
    {
        if (LOG.isDebugEnabled())
        {
            LOG.debug("enter UrlTools getUrlPrefix ");
        }
        
        if (Util.isEmpty(controller))
        {
            return null;
        }
        
        String prefix = UesServerServiceUtils.getUesParamConfig(SelectionConstants.DOMAIN_PREFIX, "");
        
        if (LOG.isDebugEnabled())
        {
            LOG.debug("exit UrlTools getUrlPrefix prefix = " + prefix);
        }
        return prefix;
    } 
    
    /**
     * 构造url(只使用于编辑系统配置的 图片地址，连接地址等地址)
     * 
     *
     * @author zhangmm
     * @param prefix
     * @param url
     * @return
     */
    public static String getUESUrl(String prefix, String url)
    {
        if (StringTools.isEmpty(url))
        {
            return "";
        }
        
        String urlTemp = UrlTools.removeProtocol(url);
        // 如果urlTemp 以//开头，表示不需要做其他处理直接返回对应的url
        if (urlTemp.startsWith(PropertiesConUtil.getProperty("double_slash", "//")))
        {
            return url;
        }
        
        // urlTemp不是以//开头，需要拼接 连接前缀+p
        url = prefix + PropertiesConUtil.getProperty("local_site_url", "/p/") + UrlTools.processForView(url, UesServiceUtils.buildPublicParaMap(null, null));
        
        return url;
        
    }
    
}
