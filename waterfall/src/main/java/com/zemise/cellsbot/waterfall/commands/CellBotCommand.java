package com.zemise.cellsbot.waterfall.commands;

import com.zemise.cellsbot.common.configuration.Configuration;
import com.zemise.cellsbot.common.util.miraiUtil.BotOperator;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import com.zemise.cellsbot.common.ConfigManager;

import java.util.*;
import java.util.function.Consumer;

public class CellBotCommand extends Command implements TabExecutor {

    public CellBotCommand() {
        super("cb");
    }

    private static final Map<String, Consumer<CommandSender>> COMMANDS = new HashMap<>();

    static {
        COMMANDS.put("reload", (sender) -> {
            if (sender.hasPermission("CellsBot.reload")) {
                ConfigManager.reload();
                sendMessage(sender, "§a配置文件已经重新载入！");
            } else {
                sendMessage(sender, "§c你没有使用该命令的权限");
            }
        });
        COMMANDS.put("rebot", (sender) -> {
            if (sender.hasPermission("CellsBot.rebot")) {
                try {
                    BotOperator.getBot().close();
                } catch (NullPointerException e) {
                    sendMessage(sender, "§a之前并未登陆QQ，现在正在尝试重新登陆QQ...");
                }
                Configuration config = ConfigManager.getConfig();
                BotOperator.login(config.getLong("bot-account"), config.getString("bot-password"));
                sendMessage(sender, "§a重新登陆QQ成功！");
            } else {
                sendMessage(sender, "§c你没有使用该命令的权限");
            }
        });
    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0 || args[0].equals("help")) {
            // 发送帮助消息
            sendMessage(sender, getHelpMessage());
            return;
        }

        Consumer<CommandSender> command = COMMANDS.get(args[0].toLowerCase());
        if (command != null) {
            command.accept(sender);
        } else {
            sendMessage(sender, "§c未知的命令，请检查是否输入错误");
        }
    }

    /**
     * 重构Bungee发送消息给CommandSender
     *
     * @param sender  指令发送者
     * @param message 消息内容
     */
    private static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(new TextComponent(message));
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            String[] allCommands = {"help", "receive", "ping", "reload", "rebot"};
            List<String> commandList = new ArrayList<>();
            Collections.addAll(commandList, allCommands);
            return commandList;
        }
        return null;
    }

    private String getHelpMessage() {
        String[] helpMessages = {
                "",
                "§a[帮助菜单]",
                "§e/cb help §f- 显示此菜单",
                "§e/cb receive §f- 切换是否接收群消息",
                "§e/cb ping §f- 查询自己的ping值，也许不是服务器的问题呢？",
                "§e/cb reload §f- 重载插件",
                "§e/cb rebot §f- 重载登陆Bot",
                "",
        };
        return "\n§b§l============ §d§lCellsBot§b§l============§f\n" +
                String.join("\n", helpMessages) +
                "\n§b§l======================================";
    }
}
