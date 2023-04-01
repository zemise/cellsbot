package com.zemise.cellsbot.common.utils.MiraiUtil;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.message.data.MessageChain;

import java.io.File;

/**
 * @Author Zemise_
 * @Date 2023/3/31
 * @Description
 */
@Slf4j
public class BotOperator {
    @Getter
    private static Bot bot;

    public static void login(Long botAccount, String botPassword, boolean loginByQR) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(GlobalEventChannel.class.getClassLoader());

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
