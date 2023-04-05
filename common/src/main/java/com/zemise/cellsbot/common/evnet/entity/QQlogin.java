package com.zemise.cellsbot.common.evnet.entity;

import com.zemise.cellsbot.common.evnet.Event;

/**
 * @Author Zemise_
 * @Date 2023/4/3
 * @Description
 */
public class QQlogin extends Event {
    private final String message;
    public QQlogin(String message) {
        System.out.println(message);
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

}
