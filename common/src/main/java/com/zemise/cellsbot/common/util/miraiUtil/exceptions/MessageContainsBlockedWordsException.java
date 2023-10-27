package com.zemise.cellsbot.common.util.miraiUtil.exceptions;

public class MessageContainsBlockedWordsException extends Exception{
    /**
     * 异常：消息包含违禁词
     */
    public MessageContainsBlockedWordsException(){
        super("消息包含违禁词");
    }
}
