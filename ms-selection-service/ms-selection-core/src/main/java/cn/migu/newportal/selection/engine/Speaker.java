/*
 * 文 件 名: Speaker.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  yangsen
 * 修改时间:  2012-7-2
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package cn.migu.newportal.selection.engine;

import org.apache.commons.lang.StringUtils;


import cn.migu.newportal.cache.bean.SpeakerInfo;
import cn.migu.newportal.cache.cache.service.SpeakerInfoCacheService;
import cn.migu.newportal.cache.util.UesServerServiceUtils;
import cn.migu.newportal.selection.util.SelectionConstants;
import cn.migu.newportal.util.string.StringTools;
import cn.migu.wheat.service.ServiceController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 听书主播
 * 
 * @author zhangmm
 * @version C10 2017年7月5日
 * @since SDP V300R003C10
 */
public class Speaker
{
    private static final Logger LOG = LoggerFactory.getLogger(Speaker.class.getName());
    /**主播标识*/
    private String id = null;
    
    /**主播名*/
    private String name = null;
    
    /**是否支持分页*/
    private String isPaged = "0";
    
    /**显示个数*/
    private String showNum = null;
    
    private final static String SITE_URL = "/p/";
    
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getIsPaged()
    {
        return isPaged;
    }

    public void setIsPaged(String isPaged)
    {
        this.isPaged = isPaged;
    }

    public String getShowNum()
    {
        return showNum;
    }

    public void setShowNum(String showNum)
    {
        this.showNum = showNum;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Speaker()
    {
        
    }
    
    public Speaker(String id)
    {
        this();
        this.id = id;
    }
    
    public Speaker(String id, String name)
    {
        this(id);
        this.name = name;
    }
    
    /**
     * 获取主播名称
     */
    public String getName()
    {
        if(StringUtils.isEmpty(name))
        {
            name = getSpeakerInfo(this);
        }
        return StringTools.replaceSpecialChar(name);
    }
    
    private static String getSpeakerInfo(Speaker speaker)
    {
        String id = null;
        if(speaker != null){
            id = speaker.getId();
        }
        SpeakerInfo info = SpeakerInfoCacheService.getInstance().getSpeakerInfo(id);
        if (info != null)
        {
            return info.getName();
        }
        return null;
    }
    
    /**
     * 拼接主播详情页链接
     *
     * @author
     * @param controller
     * @return
     */
    public static String getSpeakerUrlPrefix(ServiceController controller)
    {
       return UesServerServiceUtils.getPresetPage(SelectionConstants.SPEAKER_PREFIX);
/*        // 获取图书链接
        String urlPrefix = null;
        String speakerPrefix = null;
        try
        {
            ConfigRequest.Builder configBuilder = ConfigRequest.newBuilder();
            configBuilder.setSiteId(SelectionConstants.DEFAULT_SITEID);
            configBuilder.setConfigkey(SelectionConstants.DOMAIN_PREFIX);
            urlPrefix = ContentServiceEngine.getInstance().getConfig(controller.getContext(), configBuilder.build());
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Speaker.getgetSpeakerUrlPrefix configBuilder = " + configBuilder.toString() + "urlPreFix = "
                    + urlPrefix);
            }
            ParamRequest.Builder paramBuilder = ParamRequest.newBuilder();
            paramBuilder.setSiteId(SelectionConstants.DEFAULT_SITEID);
            paramBuilder.setParamkey(SelectionConstants.SPEAKER_PREFIX);
            speakerPrefix =
                (String)ContentServiceEngine.getInstance().getParam(controller.getContext(), paramBuilder.build());
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Speaker.getgetSpeakerUrlPrefix paramBuilder = " + paramBuilder.toString()
                    + "speakerPrefix = " + speakerPrefix);
            }
        }
        catch (cn.migu.newportal.util.exception.PortalException e)
        {
            LOG.error("Speaker.getgetSpeakerUrlPrefix getParam error: " + e);
        }
        urlPrefix =
            StringTools.isEmpty(urlPrefix) ? PropertiesConfig.getProperty("group.speakerLink.url.prefix") : urlPrefix;
        speakerPrefix = StringTools.isEmpty(speakerPrefix)
            ? PropertiesConfig.getProperty("group.speakerLink.book.prefix") : speakerPrefix;
        if (urlPrefix.endsWith("/"))
        {
            urlPrefix = urlPrefix + SITE_URL.substring(1);
        }
        else
        {
            urlPrefix = urlPrefix + SITE_URL;
        }
        if(speakerPrefix.startsWith("/"))
        {
            return urlPrefix + speakerPrefix.substring(1);
        }
        return urlPrefix + speakerPrefix;*/
        
    }
}
