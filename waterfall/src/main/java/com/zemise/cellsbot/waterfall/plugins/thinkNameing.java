package com.zemise.cellsbot.waterfall.plugins;


import com.zemise.cellsbot.common.ConfigManager;
import com.zemise.cellsbot.common.configuration.Configuration;
import com.zemise.cellsbot.common.util.miraiUtil.BotOperator;
import com.zemise.cellsbot.common.util.miraiUtil.exceptions.GroupNotAllowedException;
import com.zemise.cellsbot.common.util.miraiUtil.exceptions.InvalidSyntaxException;
import com.zemise.cellsbot.common.util.miraiUtil.exceptions.NoPermissionException;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.data.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class thinkNameing implements mainPlugin {

    public MessageChain reload(Long groupID, Long senderID, Message[] args, QuoteReply quoteReply) throws InvalidSyntaxException, NoPermissionException, GroupNotAllowedException {
        Configuration config = ConfigManager.getConfig();
        //指令长度符合要求
        if (args.length > 2) {
            throw new InvalidSyntaxException();
        }

        //只有管理群的成员才能
        if (!(BotOperator.getBot().getGroup(config.getLong("op-group")).contains(senderID))) {
            throw new NoPermissionException();
        }
        ConfigManager.reload();
        return MessageUtils.newChain(new At(senderID)).plus(new PlainText(" 配置文件已重新载入"));
    }

    @Override
    public MessageChain onEnable(Long groupID, Long senderID, Message[] args, QuoteReply quoteReply) {
        return null;
    }

    @Override
    public Map<String, Object> register() throws NoSuchMethodException {
        Map<String, Object> info = new HashMap<>();
        Map<String, Method> commands = new HashMap<>();
        Map<String, String> usages = new HashMap<>();

        commands.put("reload", thinkNameing.class.getMethod("reload", Long.class, Long.class, Message[].class, QuoteReply.class));

        usages.put("reload", "#reload - 重载配置文件");

        info.put("name", "reload");
        info.put("commands", commands);
        info.put("usages", usages);
        info.put("author", "Zemise_");
        info.put("description", "重载配置文件");
        info.put("version", "1.0");

        return info;
    }
}
