/*
 * 文 件 名:  JsonUtil.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  yangxd
 * 修改时间:  2017年4月7日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package cn.migu.newportal.selection.util;

import com.google.gson.Gson;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  yangxd
 * @version  [版本号, 2017年4月7日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class JsonUtil
{
    
    public static Gson G_JSON = new Gson();
    
    public static String objToJson(Object obj)
    {
        return G_JSON.toJson(obj);
    }
}
