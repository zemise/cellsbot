package com.zemise.cellsbot.common.util.miraiUtil.event;

import com.zemise.cellsbot.common.evnet.Event;
import lombok.Getter;

/**
 * @Author Zemise_
 * @Date 2023/5/14
 * @Description
 */
public class BotLoginEvent extends Event {
    @Getter
    private final String message;

    public BotLoginEvent(String message) {
        this.message = message;
    }
}
