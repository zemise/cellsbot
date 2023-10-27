package com.zemise.cellsbot.event.custom;

import com.zemise.cellsbot.common.evnet.Event;
import lombok.Getter;

/**
 * @Author Zemise_
 * @Date 2023/5/14
 * @Description
 */
public class MessageRecallEvent extends Event {
    @Getter
    private final String message;

    public MessageRecallEvent(String message) {
        this.message = message;
    }
}
