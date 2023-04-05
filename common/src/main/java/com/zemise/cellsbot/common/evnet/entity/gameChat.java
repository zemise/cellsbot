package com.zemise.cellsbot.common.evnet.entity;


import com.zemise.cellsbot.common.evnet.Event;

/**
 * @Author Zemise_
 * @Date 2023/4/4
 * @Description
 */
public class gameChat extends Event {

    private final String message;

    public gameChat(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
