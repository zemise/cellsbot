package com.zemise.cellsbot.waterfall;

import com.zemise.cellsbot.common.ConfigManager;
import lombok.extern.slf4j.Slf4j;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;

/**
 * <p>
 *
 * </p>
 *
 * @author <a href= "https://github.com/zemise">Zemise</a>
 * @since 2023/7/14
 */
@Slf4j
public class Main extends Plugin {
    @Override
    public void onEnable() {
        System.out.println("至少这次是成功启动了");
        File pluginFile = Main.this.getDataFolder();
        System.out.println(pluginFile.getPath());
        //
        if (!pluginFile.exists()) {
            log.warn("请在生成的配置文件中修改相关配置后，再启动本插件！");
        }
        //
        ConfigManager.saveDefaultConfig(pluginFile);
        ConfigManager.reload();
        //
        //BotOperator.login(123456L, "123456");


    }

    @Override
    public void onDisable() {
        System.out.println("这次是关闭了");
        super.onDisable();
    }
}