package com.zemise.cellsbot.common.util.miraiUtil.exceptions;

public class PlayerNotFoundException extends Exception{
    /**
     * 异常：未找到该玩家
     */
    public PlayerNotFoundException(){
        super("未找到该玩家");
    }
}
