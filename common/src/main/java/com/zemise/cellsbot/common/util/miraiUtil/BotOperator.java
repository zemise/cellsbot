package com.zemise.cellsbot.common.util.miraiUtil;

import com.zemise.cellsbot.common.ConfigManager;
import com.zemise.cellsbot.common.configuration.Configuration;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.auth.BotAuthorization;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.utils.BotConfiguration;
import net.mamoe.mirai.utils.DeviceInfo;

import java.io.File;

/**
 * @Author Zemise_
 * @Date 2023/3/31
 * @Description
 */
@Slf4j
public class BotOperator {
    private static Bot bot;

    public static void login(Long botAccount, String botPassword) {

        Configuration config = ConfigManager.getConfig();

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(GlobalEventChannel.class.getClassLoader());

        boolean loginByQR = config.getBoolean("loginByQR");
        File workingDir = new File(ConfigManager.pluginDirectory, "bot");

        if(!workingDir.exists()) workingDir.mkdirs();


        BotFactory.BotConfigurationLambda botConfigurationLambda = botConfiguration -> {
            botConfiguration.setWorkingDir(workingDir);
            if (config.getBoolean("redirectBotLogToDirectory")) {
                botConfiguration.redirectBotLogToDirectory();
            }
            if (config.getBoolean("noNetworkLog")) {
                botConfiguration.noNetworkLog();
            }
            if (config.getBoolean("noBotLog")) {
                botConfiguration.noBotLog();
            }
            botConfiguration.setProtocol(BotConfiguration.MiraiProtocol.valueOf(config.getString("bot-login-device")));
            botConfiguration.setCacheDir(new File("cache"));
            botConfiguration.fileBasedDeviceInfo();
            File deviceFile = new File(botConfiguration.getWorkingDir(), "device.json");
            if (deviceFile.exists()) {
                botConfiguration.setDeviceInfo(bot1 -> DeviceInfo.from(deviceFile));
            }
        };

        bot = loginByQR ? BotFactory.INSTANCE.newBot(botAccount, BotAuthorization.byQRCode(), botConfigurationLambda) :
                BotFactory.INSTANCE.newBot(botAccount, botPassword, new BotConfiguration() {{
                    setWorkingDir(workingDir);
                    if (config.getBoolean("redirectBotLogToDirectory")) {
                        redirectBotLogToDirectory();
                    }
                    if (config.getBoolean("noNetworkLog")) {
                        noNetworkLog();
                    }
                    if (config.getBoolean("noBotLog")) {
                        noBotLog();
                    }
                    //FixProtocolVersion.update();
                    setProtocol(MiraiProtocol.valueOf(config.getString("bot-login-device")));
                    setCacheDir(new File("cache"));
                    fileBasedDeviceInfo();
                    File deviceFile = new File(getWorkingDir(), "device.json");
                    if (deviceFile.exists()) {
                        setDeviceInfo(bot1 -> DeviceInfo.from(deviceFile));
                    }
                }});

        bot.login();
        Thread.currentThread().setContextClassLoader(loader);
    }
    public static void sendGroupMessage(Long groupID, MessageChain message) {
        try {
            bot.getGroup(groupID).sendMessage(message);
        } catch (NullPointerException e) {
            log.info("QQ账户正在登陆中，登陆期间的消息将不会转发");
        } catch (IllegalStateException e) {
            log.info("发送消息失败，QQ账户可能被风控，请及时处理");
        }
    }

    public static void sendGroupMessage(Long groupID, String message) {
        try {
            bot.getGroup(groupID).sendMessage(message);
        } catch (NullPointerException e) {
            log.info("QQ账户正在登陆中，登陆期间的消息将不会转发");
        } catch (IllegalStateException e) {
            log.info("发送消息失败，QQ账户可能被风控，请及时处理");
        }
    }
}
