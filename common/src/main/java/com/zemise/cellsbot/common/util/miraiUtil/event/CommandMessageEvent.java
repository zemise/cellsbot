package com.zemise.cellsbot.common.util.miraiUtil.event;

import com.zemise.cellsbot.common.evnet.Event;
import lombok.Getter;
import net.mamoe.mirai.event.events.GroupMessageEvent;

/**
 * @Author Zemise_
 * @Date 2023/5/14
 * @Description
 */
public class CommandMessageEvent extends Event {
    @Getter
    private final GroupMessageEvent commandMessageEvent;

    public CommandMessageEvent(GroupMessageEvent groupMessageEvent) {
        this.commandMessageEvent = groupMessageEvent;
    }

}
