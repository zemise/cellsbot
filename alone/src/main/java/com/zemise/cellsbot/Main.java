package com.zemise.cellsbot;

import com.zemise.cellsbot.common.ConfigManager;
import com.zemise.cellsbot.common.configuration.Configuration;
import com.zemise.cellsbot.common.plugin.QGroupMessageEvent;
import com.zemise.cellsbot.common.util.miraiUtil.BotOperator;
import com.zemise.cellsbot.listener.onRecall;
import com.zemise.cellsbot.listener.onResetPassword;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * @Author Zemise_
 * @Date 2023/4/8
 * @Description 主类
 */

@Slf4j
public class Main {
    // 项目文件夹
    public final static File projectFile = new File("Cells");

    public static void main(String[] args) {

        if (!projectFile.exists()) {
            log.warn("请在生成的配置文件中修改相关配置后，再启动本插件！");
        }

        ConfigManager.saveDefaultConfig(projectFile);
        ConfigManager.reload();

        Configuration config = ConfigManager.getConfig();


        new Thread(() -> {
            try {
                //FixProtocolVersion.update();
                BotOperator.login(config.getLong("bot-account"), config.getString("bot-password"));
                BotOperator.getBot().getEventChannel().registerListenerHost(new onRecall());
                BotOperator.getBot().getEventChannel().registerListenerHost(new onResetPassword());
                BotOperator.getBot().getEventChannel().registerListenerHost(new QGroupMessageEvent());
            } catch (Exception e) {
                log.info("机器人登陆错误", e);
            }
        }).start();

    }
}