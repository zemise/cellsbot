package com.zemise.cellsbot.waterfall;

import com.zemise.cellsbot.common.ConfigManager;
import com.zemise.cellsbot.common.configuration.Configuration;
import com.zemise.cellsbot.common.evnet.EventBus;
import com.zemise.cellsbot.common.util.miraiUtil.BotOperator;
import com.zemise.cellsbot.common.util.miraiUtil.event.BotLoginEvent;
import com.zemise.cellsbot.waterfall.commands.CellBotCommand;
import com.zemise.cellsbot.waterfall.listeners.onGameChatListener;
import com.zemise.cellsbot.waterfall.listeners.onPlayerGroupListener;
import com.zemise.cellsbot.waterfall.utils.DatabaseUtil.DownloadDatabaseIP;
import com.zemise.cellsbot.waterfall.utils.DatabaseUtil.MysqlUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.luckperms.api.LuckPerms;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.util.TimeZone;

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
    @Getter
    public static Main instance;

    public static EventBus eventBus = new EventBus();
    @Override
    public void onEnable() {
        File pluginFile = Main.this.getDataFolder();
        instance = this;
        System.out.println(pluginFile.getPath());


        if (!pluginFile.exists()) {
            log.warn("请在生成的配置文件中修改相关配置后，再启动本插件！");
        }
        //
        ConfigManager.saveDefaultConfig(pluginFile);
        ConfigManager.reload();

        Configuration config = ConfigManager.getConfig();

        TimeZone.setDefault(TimeZone.getTimeZone(ConfigManager.getConfig().getString("TimeZone")));

        new Thread(() -> {
            try {
                BotOperator.login(config.getLong("bot-account"), config.getString("bot-password"));

                // 发送已经登陆的事件
                eventBus.post(new BotLoginEvent("qq bot登陆成功"));

                // 注册QQ事件
                BotOperator.getBot().getEventChannel().registerListenerHost(new onPlayerGroupListener());
            } catch (Exception e) {
                log.info("机器人登陆错误", e);
            }
        }).start();

        if(ConfigManager.getConfig().getBoolean("LuckPerms")){
            LuckPerms luckPerms = net.luckperms.api.LuckPermsProvider.get();
            log.info("LuckPerms已挂钩");
        }

        // 服内监听事件注册
        this.getProxy().getPluginManager().registerListener(this, new onGameChatListener());
        getProxy().getPluginManager().registerCommand(this, new CellBotCommand());

        new MysqlUtil().init();
        //初始IP化数据库
        DownloadDatabaseIP.downloadDatabaseIP();

    }

    @Override
    public void onDisable() {
        BotOperator.logout();
        super.onDisable();
    }
}