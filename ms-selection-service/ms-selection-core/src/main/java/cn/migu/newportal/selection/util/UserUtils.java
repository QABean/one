package cn.migu.newportal.selection.util;

import com.huawei.iread.server.constant.Types;

import cn.migu.newportal.cache.business.user.UserManager;
import cn.migu.newportal.util.bean.user.PortalUserInfo;
import cn.migu.newportal.util.bean.user.UserInfo;
import cn.migu.newportal.util.constants.SystemConstants;
import cn.migu.newportal.util.string.StringTools;

public class UserUtils
{
    public static PortalUserInfo getUserInfo()
    {
        
        PortalUserInfo user = new PortalUserInfo();
        
        UserInfo userInfo = new UserInfo();
        
        userInfo.setCityId("000");
        userInfo.setProvinceId("001");
        user.setUserInfo(userInfo);
        
        return user;
    }
    
    /**
     * 
     * 
     *
     * @author zhaoxinwei
     * @param request
     * @return
     */
    public static String getCityID(String identityId)
    {
        if (StringTools.isEmpty(identityId))
        {
            return Types.COUNTRY;
        }
        PortalUserInfo userInfo = UserManager.getInstance().getPortalUserInfo(identityId);
        
        if (null == userInfo || null == userInfo.getUserInfo() || StringTools.isEmpty(userInfo.getUserInfo().getCity()))
        {
            return Types.COUNTRY;
        }
        return userInfo.getUserInfo().getCity();
    }
    
    /**
     * 
     * 
     *
     * @author zhaoxinwei
     * @param request
     * @return
     */
    public static String getProvinceID(String identityId)
    {
        if (StringTools.isEmpty(identityId))
        {
            return SystemConstants.CHINA_ID;
        }
        PortalUserInfo userInfo = UserManager.getInstance().getPortalUserInfo(identityId);
        
        if (null == userInfo || null == userInfo.getUserInfo() || StringTools.isEmpty(userInfo.getUserInfo().getProvinceId()))
        {
            return SystemConstants.CHINA_ID;
        }
        return userInfo.getUserInfo().getProvinceId();
    }
    
}
