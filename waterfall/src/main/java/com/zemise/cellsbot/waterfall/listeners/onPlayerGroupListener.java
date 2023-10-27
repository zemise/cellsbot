package com.zemise.cellsbot.waterfall.listeners;

import com.zemise.cellsbot.common.ConfigManager;
import com.zemise.cellsbot.common.configuration.Configuration;
import com.zemise.cellsbot.common.util.miraiUtil.BotOperator;
import com.zemise.cellsbot.common.util.miraiUtil.exceptions.CommandNotFoundException;
import com.zemise.cellsbot.waterfall.Main;
import com.zemise.cellsbot.waterfall.plugins.PluginManager;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Zemise
 */
@Slf4j
public class onPlayerGroupListener extends SimpleListenerHost {
    //群消息事件
    //群消息(非指令消息)转发至游戏服务器
    @EventHandler
    public void onMessageForward(GroupMessageEvent e) {
        Configuration config = ConfigManager.getConfig();
        long playerGroup = config.getLong("player-group");

        // 只处理玩家群消息
        if (e.getGroup().getId() != playerGroup) {
            return;
        }

        try {
            // 如果群消息发送者被屏蔽，这条消息不会被转发到服务器
            List<Long> blacklist = config.getLongList("blacklist.qq");
            if (blacklist.contains(e.getSender().getId())) {
                return;
            }

            // 获取Q群成员的群名片
            String name = e.getSender().getNameCard();
            // 当成员群名片昵称为空，同时当群名片不存在时,尝试获取昵称
            if ("".equalsIgnoreCase(name) && config.getBoolean("use-nick-if-namecard-null")) {
                name = e.getSenderName();
            }

            // 将群消息加工，去掉前后空格字符
            String textString = e.getMessage().contentToString().trim();

            // 不处理发送给控制台的消息
            String consoleCommandPrefix = config.getString("console-command-prefix", "/");
            String botCommandPrefix = config.getString("bot-command-prefix");
            if (textString.startsWith(consoleCommandPrefix) || textString.startsWith(botCommandPrefix)) {
                return;
            }


            // 判断消息来源是玩家群还是管理群(默认玩家群才转发消息)
            if (e.getGroup().getId() != playerGroup) {
                return;
            }

            // 判断是否开启聊天转发至游戏服务器(默认true开启)
            if (!config.getBoolean("message-forward.group-to-server.enable")) {
                return;
            }

            // 判断聊天前缀(聊天需要指定前缀才能发送到服务器，默认true开启)
            // 判断消息转发模式
            switch (config.getString("message-forward.group-to-server.mode")) {
                //无需前缀，消息直接转发至服务器
                case "all" -> {
                }
                //需要前缀，只有config里设定的特定前缀才能转发至服务器
                case "prefix" -> {
                    for (String prefix : config.getStringList("message-forward.group-to-server.prefix")) {
                        if (!textString.startsWith(prefix)) {
                            return;
                        }
                    }
                }
                //提示配置里模式填写错误
                default -> {
                    log.warn("config.yml: message-forward.group-to-server.mode 选择错误，请重新填写");
                    return;
                }
            }

            //判断消息是否带有前缀
            for (String prefix : config.getStringList("message-forward.group-to-server.prefix")) {
                if (textString.startsWith(prefix)) {
                    textString = textString.substring(prefix.length());
                    break;
                }
            }

            String formatText;

            formatText = config.getString("bot.in-game-chat-format")
                    .replace("%groupname%", e.getGroup().getName())
                    .replace("%groupid%", String.valueOf(e.getGroup().getId()))
                    .replace("%nick%", name)
                    .replace("%qq%", String.valueOf(e.getSender().getId()))
                    .replace("%message%", textString);
            Main.instance.getProxy().broadcast(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', formatText)));

        } catch (Exception es) {
            log.error("发生错误：%s", es);
        }
    }

    @EventHandler
    public void onCommandReceive(GroupMessageEvent e) {
        Configuration config = ConfigManager.getConfig();

        try {
            String textString = e.getMessage().contentToString().trim();

            if (!textString.startsWith(config.getString("bot-command-prefix"))) {
                return;
            }

            //按空格分割，并去掉前缀字符，获得具体指令字符串
            String command = textString.split(" ", 2)[0].substring(1);

            List<Message> argsList = new ArrayList<>();
            for (Message message : e.getMessage()) {
                if (message instanceof PlainText) {
                    for (String arg : message.contentToString().split(" ")) {
                        argsList.add(new PlainText(arg));
                    }
                } else {
                    argsList.add(message);
                }
            }

            argsList.remove(0);
            argsList.remove(0);

            QuoteReply quoteReply = e.getMessage().get(QuoteReply.Key);

            Message[] args = argsList.toArray(new Message[0]);
            Main.getInstance().getProxy().getScheduler().runAsync(Main.getInstance(), () -> {
                MessageChainBuilder message = new MessageChainBuilder();
                try {
                    message.append(PluginManager.commandHandler(command, e.getGroup().getId(), e.getSender().getId(), args, quoteReply));
                } catch (CommandNotFoundException ex) {
                    message.append(ex.getMessage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                BotOperator.sendGroupMessage(e.getGroup().getId(), message.build());
            });

        } catch (Exception es) {
            es.printStackTrace();
        }
    }


    //群发送到console的指令消息
    @EventHandler
    public void onConsoleCommandReceive(GroupMessageEvent e) {
        Configuration config = ConfigManager.getConfig();
        MessageContent messageContent = e.getMessage().get(PlainText.Key);
        if (messageContent == null) {
            return;
        }

        String textString = messageContent.contentToString().trim();
        String consoleCommandPrefix = config.getString("console-command-prefix", "/");
        if (!textString.startsWith(consoleCommandPrefix)) {
            return;
        }
        if (!config.getLongList("super-admin-account").contains(e.getSender().getId())) {
            return;
        }

        CommandSender consoleSender = ProxyServer.getInstance().getConsole();
        consoleSender.sendMessage(new TextComponent("[CellsBot]发送指令：" + textString));


        String command = textString.substring(consoleCommandPrefix.length());
        if ("stop".equalsIgnoreCase(command)) {
            BotOperator.sendGroupMessage(e.getGroup().getId(), "收到关闭BC服指令，5秒后将关闭服务器");
            log.info("收到关闭BC服指令，5秒后将关闭服务器");
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // 在此处执行需要延迟的操作
                    BotOperator.sendGroupMessage(e.getGroup().getId(), "即将关闭...");
                    log.info("即将关闭...");
                    ProxyServer.getInstance().stop();
                }
            }, 5000); // 5秒钟
        }

        ProxyServer.getInstance().getPluginManager().dispatchCommand(consoleSender, command);

        //todo 想办法获得控制台的回馈
        Main.getInstance().getProxy().getScheduler().runAsync(Main.getInstance(), () -> {
            BotOperator.sendGroupMessage(e.getGroup().getId(), "控制台指令已发送");
        });

    }
}
