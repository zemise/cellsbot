package com.zemise.cellsbot.waterfall.listeners;

import com.zemise.cellsbot.common.ConfigManager;
import com.zemise.cellsbot.common.configuration.Configuration;
import com.zemise.cellsbot.common.util.miraiUtil.BotOperator;
import com.zemise.cellsbot.waterfall.Main;
import com.zemise.cellsbot.waterfall.utils.DependentUtil.LuckPermsProvider;
import lombok.extern.slf4j.Slf4j;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

@Slf4j
public class onGameChatListener implements Listener {
    @EventHandler
    public void chatForward(ChatEvent e) {
        Configuration config = ConfigManager.getConfig();
        boolean serverToGroupEnabled = config.getBoolean("message-forward.server-to-group.enable");

        try {
            String message = e.getMessage();
            ProxiedPlayer sender = (ProxiedPlayer) e.getSender();
            String playerName = sender.getName();
            String serverName = sender.getServer().getInfo().getName();

            if (!serverToGroupEnabled || e.isCancelled() || message.trim().startsWith("/")) {
                return;
            }

            String name = playerName;

            if (config.getBoolean("LuckPerms")) {
                LuckPermsProvider luckPermsOperator = new LuckPermsProvider();
                String prefix = luckPermsOperator.getPrefix(sender);
                String suffix = luckPermsOperator.getSuffix(sender);
                if (prefix != null) {
                    prefix = luckPermsOperator.noColor(prefix);
                    name = prefix + playerName;
                }
                if (suffix != null) {
                    suffix = luckPermsOperator.noColor(suffix);
                    name = name + suffix;
                }
            }

            String finalName = name;
            Main.getInstance().getProxy().getScheduler().runAsync(Main.getInstance(), () ->
                    BotOperator.sendGroupMessage(config.getLong("player-group"),
                            String.format("[%s] %s: %s", serverName, finalName, message.trim())));
        } catch (Exception ex) {
            log.error("bot发送消息至服务器发生错误：%s", ex);
        }
    }

}
