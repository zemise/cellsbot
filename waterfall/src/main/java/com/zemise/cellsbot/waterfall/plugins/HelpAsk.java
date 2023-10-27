package com.zemise.cellsbot.waterfall.plugins;


import com.zemise.cellsbot.common.ConfigManager;
import com.zemise.cellsbot.common.configuration.Configuration;
import com.zemise.cellsbot.common.util.miraiUtil.BotOperator;
import com.zemise.cellsbot.common.util.miraiUtil.exceptions.GroupNotAllowedException;
import com.zemise.cellsbot.common.util.miraiUtil.exceptions.InvalidSyntaxException;
import net.mamoe.mirai.message.data.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/*
    需求：
        当玩家群有人反馈需要管理帮助时，将其诉求消息转发至管理群，同时告知玩家耐心等待
    拓展：
 */
public class HelpAsk implements mainPlugin {
    public MessageChain please(@NotNull Long groupID, @NotNull Long senderID, Message[] args, @Nullable QuoteReply quoteReply) throws Exception {
        Configuration config = ConfigManager.getConfig();

        if (args.length < 1) {
            throw new InvalidSyntaxException();    //指令不符合规范提示
        }

        //功能只在玩家群/管理群里启用
        if (groupID != config.getLong("player-group") & groupID != config.getLong("op-group")) {
            throw new GroupNotAllowedException();
        }


        StringBuilder askMessage = new StringBuilder();
        for(Message arg : args) {
            askMessage.append(arg.toString());
        }


        MessageChainBuilder msg = new MessageChainBuilder();
        msg.append(AtAll.INSTANCE)  //这里要注意，如果bot不是群管理员身份，这条消息发不出来
                .append("\n")
                .append("玩家群有人寻求管理帮助啦：\n")
                //.append(new MysqlUtil().queryPlayer(senderID, "cells_whitelist").get("MC_id").toString())
                .append("(qq:")
                .append(senderID.toString()).append(")")
                .append("：\n")
                .append(askMessage.toString());
        BotOperator.sendGroupMessage(config.getLong("op-group"), msg.build());

        return new MessageChainBuilder()
                .append(new At(senderID))
                .append("\n")
                .append(" 你的诉求已转发至管理群，热心的管理员将尽快解决\n")
                .append("\n转发诉求内容如下：\n")
                .append(askMessage.toString())
                .build();
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

        commands.put("please", HelpAsk.class.getMethod("please", Long.class, Long.class, Message[].class, QuoteReply.class));
        commands.put("请", HelpAsk.class.getMethod("please", Long.class, Long.class, Message[].class, QuoteReply.class));

        usages.put("please", "#please <诉求> - 寻求管理帮助，此条消息转发至管理群");
        usages.put("请", "#请 <诉求> - 寻求管理帮助，此条消息转发至管理群");

        info.put("name", "HelpAsk");
        info.put("commands", commands);
        info.put("usages", usages);
        info.put("author", "Zemise_");
        info.put("description", "寻求管理帮助");
        info.put("version", "1.0.0");
        return info;
    }
}
