package cn.migu.newportal.selection.etcd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.migu.millet.AppBase;
import cn.migu.millet.config.ConfigHandler;
import cn.migu.millet.config.ConfigItem;
import cn.migu.millet.config.ConfigManager;
import cn.migu.millet.config.InheritedConfig;
import cn.migu.millet.config.SysConfigManager;

public class Application extends AppBase {
	private final static Logger logger = LoggerFactory.getLogger(Application.class);

	public Application() {
		super("ms_selection");
	}

	public void init() {
		super.initialize();
		ConfigManager config = super.getConfigManager();
		logger.info("start to set config handler");
		InheritedConfig appConfig = config.getAppConfig();
		SysConfigManager sysConfig = config.getGlobal();
		appConfig.triggerChange(new ConfigHandler() {
			@Override
			public void handleChanged(int action, String name, ConfigItem item) {
				// TODO: 加入应用自身配置变更的代码
			}

		});
		sysConfig.triggerChange(new ConfigHandler() {
			@Override
			public void handleChanged(int action, String name, ConfigItem item) {
				// TODO: 加入Global配置变更的代码
			}

		});
		
		logger.info("Application initialized.");
		
		StartUp.getInstance().init();
	}
}
