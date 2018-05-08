package cn.migu.newportal.selection.etcd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.ResourceUtils;

import cn.migu.newportal.util.cache.util.CachedDAOFactory;
import cn.migu.newportal.util.cache.util.PortalConfig;
import cn.migu.newportal.util.tools.SystemConfig;

public class StartUp
{
    
    private static Logger logger = LogManager.getLogger(StartUp.class.getName());
    
    private static StartUp instance = new StartUp();
    
    private StartUp()
    {
        
    }
    
    public static StartUp getInstance()
    {
        return instance;
    }
    
    public void init()
    {
        logger.debug("Start to init------------------");
        //缓存初始化
        initCache();
        
    }
    
    /**
     * 缓存初始化
     * 
     *
     * @author zhangjiadao
     */
    void initCache()
    {

        String basePath = "";
        try
        {
            File f = ResourceUtils.getFile("classpath:");
            basePath = f.getPath();
        }
        catch (FileNotFoundException e1)
        {
            e1.printStackTrace();
            logger.error("get classpath fail ");
        }
        
        SystemConfig.getInstance().setWwwroot(basePath);
        // 设置oscache配置文件目录
        SystemConfig.getInstance().setOscachePath(basePath + "/cache/common_oscache_config/");
        // 加载配置文件
        try
        {
            PortalConfig.loadConfig(basePath + "/cache/common_ues_config.xml");
        }
        catch (IOException e)
        {
            logger.error("load common_ues_config.xml error:"+e.getMessage());
            e.printStackTrace();
        }
        
        logger.info("------------init common_ues_config.xml finished---------------");
        
        // 缓存初始化
        String oscacheConfPath = basePath + "/cache/common_cache_config.xml";
        CachedDAOFactory.init(oscacheConfPath);
        logger.info("------------init common_cache_config.xml finished---------------");
    }
    
}
