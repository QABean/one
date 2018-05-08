package cn.migu.newportal.selection.manager;

import cn.migu.newportal.cache.cache.service.GetUserPreferenceService;
import cn.migu.newportal.util.string.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class UserPreferenceManager
{
    // 日志对象
    private static final Logger logger = LoggerFactory.getLogger(UserPreferenceManager.class.getName());
    /**
     * 获取用户偏好信息，不包含默认偏好
     * 
     *
     * @author zhangmm
     * @return
     */
    public static List<String> getUserSelectedPreference()
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("'enter into UserPreferenceManager.getUserSelectedPreference()");
        }
        // 从缓存中获取用户偏好
        String preference = GetUserPreferenceService.getInstance().getUserPreference();
        if (StringTools.isEmpty(preference))
        {
            return null;
        }
        else
        {
            List<String> userPreference = new ArrayList<String>();
            String[] preferences = preference.split(",");
            for (String str : preferences)
            {
                userPreference.add(str);
            }
            if (logger.isDebugEnabled())
            {
                logger.debug("'Exit UserPreferenceManager.getUserSelectedPreference() and find userPreference="
                    + userPreference);
            }
            return userPreference;
        }
    }
    
}
