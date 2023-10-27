package com.zemise.cellsbot.event.miari;

import com.zemise.cellsbot.common.ConfigManager;
import com.zemise.cellsbot.common.util.miraiUtil.MiraiMessageUtil;
import com.zemise.cellsbot.controllers.EventCentral;
import com.zemise.cellsbot.event.custom.CommandMessageEvent;
import com.zemise.cellsbot.event.custom.NoCommandMessageEvent;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageRecallEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @Author Zemise_
 * @Date 2023/5/14
 * @Description Mirai Bot的各类消息
 */

@Component
public class MessageEventHandle extends SimpleListenerHost {
    private final EventCentral eventCentral;

    @Autowired
    public MessageEventHandle(EventCentral eventCentral) {
        this.eventCentral = eventCentral;
    }

    @EventHandler
    public static void afterFriendMessage(FriendMessageEvent event) {

    }

    @EventHandler
    public void afterGroupMessage(GroupMessageEvent event) {
        // 将群消息分类为指令消息以及普通消息，分流处理

        // 获取设定的command前缀
        String commandPrefix = ConfigManager.getConfig().getString("bot-command-prefix");
        boolean isCommandMessage = MiraiMessageUtil.startWithKeywords(event, commandPrefix);


        if (isCommandMessage) {
            // 可以考虑把本次获得的具体指令传过去
            eventCentral.eventBus.post(new CommandMessageEvent(event));

        } else {
            // 非指令消息
            eventCentral.eventBus.post(new NoCommandMessageEvent(event));
        }

    }

    @EventHandler
    public void afterRecallMessage(MessageRecallEvent event) {

    }
}
