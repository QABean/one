package cn.migu.newportal.selection.util;

import cn.migu.newportal.util.constants.ParamConstants;
import cn.migu.newportal.util.http.HttpUtil;
import cn.migu.newportal.util.string.StringTools;

public class UserSessionCheck
{
    /**
     * 判断用户是否是游客
     * 
     * @param request
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static boolean isGuestUser()
    {
        if (StringTools.isNotEmpty(HttpUtil.getHeader(ParamConstants.X_Identity_ID)))
        {
            if (StringTools.isEq(ParamConstants.NINE, HttpUtil.getHeader(ParamConstants.X_Identity_ID).substring(0, 1)))
            {
                return true;
            }
            return false;
        }
        return true;
    }
}
