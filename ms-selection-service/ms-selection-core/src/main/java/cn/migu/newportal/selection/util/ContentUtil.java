package cn.migu.newportal.selection.util;

import org.apache.commons.lang.StringUtils;

import cn.migu.newportal.uesserver.api.Struct.ConfigRequest;
import cn.migu.newportal.uesserver.api.Struct.ParamRequest;

import cn.migu.newportal.cache.util.PropertiesConfig;
import cn.migu.newportal.cache.util.UesServerServiceUtils;
import cn.migu.newportal.selection.engine.ContentServiceEngine;
import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.http.HttpUtil;
import cn.migu.wheat.service.ServiceController;

/**
 * 内容帮助类
 *
 * @author wulinfeng
 * @version C10 2017年6月28日
 * @since SDP V300R003C10
 */
public class ContentUtil
{
    /**
     * 拼接图书详情页链接
     *
     * @author wulinfeng
     * @param controller
     * @return
     */
    public static String getBookUrlPrefix(ServiceController controller)
    {
        // 获取图书链接
        
        String urlPrefix = UesServerServiceUtils.getUesParamConfig(SelectionConstants.DOMAIN_PREFIX, "");
        
        String bookPrefix = UesServerServiceUtils.getPresetPage(SelectionConstants.BOOK_PREFIX);
        
        urlPrefix =
            StringUtils.isBlank(urlPrefix) ? PropertiesConfig.getProperty("group.bookLink.url.prefix") : urlPrefix;
        String schema = PropertiesConfig.getProperty("group.bookLink.schema");
        bookPrefix =
            StringUtils.isBlank(bookPrefix) ? PropertiesConfig.getProperty("group.bookLink.book.prefix") : bookPrefix;
        
        return urlPrefix + schema + bookPrefix;
    }
    
    /**
     * 返回字段为空，则设置空字符串
     *
     * @author wulinfeng
     * @param str
     * @return
     */
    public static String defaultString(String str)
    {
        return StringUtils.isEmpty(str) ? " " : str;
    }
    
    /**
     * 获取版本号
     *
     * @author wulinfeng
     * @return
     */
    public static String getUAVersion()
    {
        String useAgent = HttpUtil.getHeader(ParamConstants.User_Agent);
        if (StringUtils.isNotBlank(useAgent))
        {
            int uaIndex = useAgent.indexOf("_V");
            if(uaIndex != -1){
                String ua = useAgent.substring(uaIndex + 2, uaIndex + 5);
                return ua;
            }
        }
        return "0";
    }
}
