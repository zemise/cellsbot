package com.zemise.cellsbot.event.custom;

import com.zemise.cellsbot.common.evnet.Event;
import lombok.Getter;

/**
 * @Author Zemise_
 * @Date 2023/5/14
 * @Description
 */
@Getter
public class BotOnlineEvent extends Event {
    private final String message;

    public BotOnlineEvent(String message) {
        this.message = message;
    }
}
