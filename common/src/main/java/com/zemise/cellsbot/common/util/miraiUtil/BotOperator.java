package com.zemise.cellsbot.common.util.miraiUtil;

import com.zemise.cellsbot.common.ConfigManager;
import com.zemise.cellsbot.common.configuration.Configuration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.auth.BotAuthorization;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.utils.BotConfiguration;
import net.mamoe.mirai.utils.DeviceInfo;
import xyz.cssxsh.mirai.tool.FixProtocolVersion;


import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * @Author Zemise_
 * @Date 2023/3/31
 * @Description
 */
@Slf4j
public class BotOperator {
    @Getter
    private static Bot bot;

    public static void login(Long botAccount, String botPassword) {

        Configuration config = ConfigManager.getConfig();

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(GlobalEventChannel.class.getClassLoader());

        boolean loginByQR = config.getBoolean("loginByQR");
        File workingDir = new File(ConfigManager.pluginDirectory, "bot");

        if (!workingDir.exists()) {
            workingDir.mkdirs();
        }


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

                    // fix protocol
                    FixProtocolVersion.fetch(MiraiProtocol.ANDROID_PHONE, "latest");
                    FixProtocolVersion.fetch(MiraiProtocol.ANDROID_PHONE, "8.9.63");
                    FixProtocolVersion.load(MiraiProtocol.ANDROID_PHONE);
                    FixProtocolVersion.update();
                    System.out.println(FixProtocolVersion.info());

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


    public static void sendGroupMessage(List<Long> groupIDs, Object message) {
        for (Long groupID : groupIDs) {
            try {
                Group group = bot.getGroup(groupID);
                if (message instanceof MessageChain) {
                    group.sendMessage((MessageChain) message);
                } else if (message instanceof String) {
                    group.sendMessage((String) message);
                } else {
                    log.error("Invalid message type: {}", message.getClass().getName());
                }
            } catch (NullPointerException e) {
                log.error("Failed to send message to group {} - group is null: {}", groupID, e.getMessage());
            } catch (IllegalStateException e) {
                log.error("Failed to send message to group {} - account may be blocked: {}", groupID, e.getMessage());
            } catch (Exception e) {
                log.error("Failed to send message to group {}: {}", groupID, e.getMessage());
            }
        }
    }

    public static void sendGroupMessage(Long groupID, Object message) {
        sendGroupMessage(Collections.singletonList(groupID), message);
    }

    public static void logout(){
        bot.close();
    }
}