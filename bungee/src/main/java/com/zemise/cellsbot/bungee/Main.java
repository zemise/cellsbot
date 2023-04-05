
package com.zemise.cellsbot.bungee;

import com.zemise.cellsbot.common.ConfigManager;
import com.zemise.cellsbot.common.util.miraiUtil.BotOperator;
import lombok.extern.slf4j.Slf4j;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;


/**
 * @Author Zemise_
 * @Date 2023/3/31
 * @Description BungeeCord插件入口
 */

@Slf4j
public class Main extends Plugin {
    @Override
    public void onEnable() {
        File pluginFile = Main.this.getDataFolder();

        if (!pluginFile.exists()) {
            log.warn("请在生成的配置文件中修改相关配置后，再启动本插件！");
        }

        ConfigManager.saveDefaultConfig(pluginFile);
        ConfigManager.reload();


        BotOperator.login(123456L, "123456");


    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}

