package com.zemise.cellsbot.waterfall.utils.DependentUtil;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LuckPermsProvider {
    static LuckPerms luckPerms = net.luckperms.api.LuckPermsProvider.get();

    String prefix,suffix;
    User user;

    public LuckPermsProvider() {}

    public String getPrefix(User user) {
        prefix = user.getCachedData().getMetaData().getPrefix();
        return prefix;
    }
    public String getPrefix(ProxiedPlayer player) {
        LuckPermsProvider luckPermsProvider = new LuckPermsProvider();
        User user = luckPermsProvider.getUser(player);
        prefix = user.getCachedData().getMetaData().getPrefix();
        return prefix;
    }


    public String getSuffix(User user) {
        suffix = user.getCachedData().getMetaData().getSuffix();
        return suffix;
    }

    public String getSuffix(ProxiedPlayer player) {
        LuckPermsProvider luckPermsProvider = new LuckPermsProvider();
        User user = luckPermsProvider.getUser(player);
        suffix = user.getCachedData().getMetaData().getSuffix();
        return suffix;
    }

    public User getUser(ProxiedPlayer player) {
        user = luckPerms.getPlayerAdapter(ProxiedPlayer.class).getUser(player);
        return user;
    }

    //定义一个去除前后缀中颜色代码的方法
    public String noColor(String s){
        //括号里为正则表达式
        Pattern compile = Pattern.compile("&[^#]|&#[a-zA-Z0-9]{6}");
        //括号里为需要替换的字符串
        Matcher matcher = compile.matcher(s);
        //结果
        //替换之后的新字符为""
        return matcher.replaceAll("").trim();
    }

    public LuckPerms getLuckPerms() {
        return luckPerms;
    }
}
