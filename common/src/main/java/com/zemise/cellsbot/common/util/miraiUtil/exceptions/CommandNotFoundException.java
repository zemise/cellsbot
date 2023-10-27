package com.zemise.cellsbot.common.util.miraiUtil.exceptions;

public class CommandNotFoundException extends Exception{
    /**
     * 异常：未知的指令
     */
    public CommandNotFoundException(){
        super("未知的指令");
    }
}
