package cn.migu.newportal.selection.engine;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;

import cn.migu.wheat.service.ServiceConsumer;

/**
 * 
 * UESServer引擎,用于调用uesserver发布到etcd上的服务
 * 
 * @author zhangjiadao
 * @version C10 Apr 6, 2017
 * @since SDP V300R003C10
 */
public class UESServiceEngine
{
    // 日志对象
    private static Logger logger = LoggerFactory.getLogger(UESServiceEngine.class.getName());
    
    @Autowired
    WebApplicationContext wac;
    
    // 管理类中的consumer列表，beanid作为key
    private Map<String, ServiceConsumer> consumerMap;
    
    protected void init()
    {
        consumerMap = wac.getBeansOfType(ServiceConsumer.class);
    }
    
    public UESServiceEngine()
    {
        
    }
    
    private ServiceConsumer getConsumer(String key)
    {
        if(StringUtils.isEmpty(key))
        {
            return null;
        }
        if(null == consumerMap||consumerMap.size()<=0)
        {
            return  null;
        }
        return consumerMap.get(key);
    }
}
