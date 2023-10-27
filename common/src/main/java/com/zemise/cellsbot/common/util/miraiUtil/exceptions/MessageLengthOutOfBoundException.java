package com.zemise.cellsbot.common.util.miraiUtil.exceptions;

public class MessageLengthOutOfBoundException extends Exception{
    /**
     * 异常：本条消息过长，将不转发至服务器
     */
    public MessageLengthOutOfBoundException(){
        super("本条消息过长，将不转发至服务器");
    }
}
