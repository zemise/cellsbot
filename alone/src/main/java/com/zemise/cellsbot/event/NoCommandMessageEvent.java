package com.zemise.cellsbot.event;

import com.zemise.cellsbot.common.evnet.Event;
import lombok.Getter;
import net.mamoe.mirai.event.events.GroupMessageEvent;

/**
 * @Author Zemise_
 * @Date 2023/5/14
 * @Description
 */
public class NoCommandMessageEvent extends Event {
    @Getter
    private final GroupMessageEvent groupMessageEvent;

    public NoCommandMessageEvent(GroupMessageEvent groupMessageEvent) {
        this.groupMessageEvent = groupMessageEvent;
    }

}
