package com.zemise.cellsbot.common.evnet.entity;

import com.zemise.cellsbot.common.evnet.Event;

/**
 * @Author Zemise_
 * @Date 2023/4/4
 * @Description
 */
public class newGameChat extends Event {

    private String message;

    public newGameChat(String message) {
        this.message = message;
    }


    public String getMessage() {
        return message;
    }
}
