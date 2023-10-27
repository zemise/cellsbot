package com.zemise.cellsbot.waterfall.plugins;


import com.zemise.cellsbot.common.ConfigManager;
import com.zemise.cellsbot.common.configuration.Configuration;
import com.zemise.cellsbot.waterfall.Main;
import com.zemise.cellsbot.waterfall.utils.DependentUtil.LuckPermsProvider;
import net.mamoe.mirai.message.data.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.*;

//使用BungeeCore的 Interface ServerInfo显示当前服务器内的在线人数


public class ListPlayers implements mainPlugin {
    public static List<String> list() {
        Configuration config = ConfigManager.getConfig();

        List<String> onlineList = new ArrayList<>();

        //获得玩家集合
        Collection<ProxiedPlayer> players = Main.getInstance().getProxy().getPlayers();
        //获得[server] player格式列表
        for (ProxiedPlayer player : players) {
            //如果在线玩家里包括config文件里的隐身玩家，则去除，只获得非隐身玩家
            if (config.getStringList("vanishlist.player").stream().noneMatch(s -> player.getName().contains(s))) {
                String serverName = player.getServer().getInfo().getName();
                String name = player.getName();

                //如果开启LuckPerms的前后缀功能，转发消息时，玩家名字带前后缀
                //默认关闭(false)
                if (config.getBoolean("LuckPerms")) {
                    //实例化LuckPermsProvider
                    LuckPermsProvider luckPermsOperator = new LuckPermsProvider();
                    String prefix = luckPermsOperator.getPrefix(player);
                    String suffix = luckPermsOperator.getSuffix(player);

                    //如果前缀不为null
                    if (prefix != null) {
                        prefix = luckPermsOperator.noColor(prefix);
                        name = prefix + name;
                    }
                    if (suffix != null) {
                        suffix = luckPermsOperator.noColor(suffix);
                        name = name + suffix;
                    }
                }

                onlineList.add(String.format("[%s] %s", serverName, name));
            }
        }
        return onlineList;  //返回的是去除隐身玩家的列表
    }


    public MessageChain onList(@NotNull Long groupID, @NotNull Long senderID, Message[] args, @Nullable QuoteReply quoteReply) throws Exception {
        MessageChainBuilder msgs = new MessageChainBuilder();
        Configuration messages = ConfigManager.getMessageConfig();

        List<String> onLineList = list();
        StringBuilder msgBuilder = new StringBuilder();

        //当前在线人数，此处为真实人数，现在改为第二条模式
//        int onlineSize = Main.getInstance().getProxy().getOnlineCount();
        int onlineSize = onLineList.size();

        //排列输出在线玩家
        msgBuilder.append(String.format("%s当前在线 %d 人：\n", messages.getString("server-name"), onlineSize));
        for (String serverPlayer : onLineList) {
            msgBuilder.append(serverPlayer).append("\r\n");
        }

        String msg = msgBuilder.toString();
        msg = msg.substring(0, msg.length() - 2);

        return new MessageChainBuilder().append(msg).build();
    }

    @Override
    public MessageChain onEnable(@NotNull Long groupID, @NotNull Long senderID, Message[] args, @Nullable QuoteReply quoteReply) {
        return null;
    }

    @Override
    public Map<String, Object> register() throws NoSuchMethodException {
        Map<String, Object> info = new HashMap<>();
        Map<String, Method> commands = new HashMap<>();
        Map<String, String> usages = new HashMap<>();

        commands.put("list", ListPlayers.class.getMethod("onList", Long.class, Long.class, Message[].class, QuoteReply.class));

        usages.put("list", "#list - 获取在线玩家列表");

        info.put("name", "ListPlayers");
        info.put("commands", commands);
        info.put("usages", usages);
        info.put("author", "Zemise_");
        info.put("description", "获取在线玩家列表");
        info.put("version", "1.0.0");
        return info;
    }
}
