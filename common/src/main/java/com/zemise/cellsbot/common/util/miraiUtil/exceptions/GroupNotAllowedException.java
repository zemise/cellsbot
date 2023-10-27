package com.zemise.cellsbot.common.util.miraiUtil.exceptions;

public class GroupNotAllowedException extends Exception{
    /**
     * 异常：此功能未在本群启用
     */
    public GroupNotAllowedException(){
        super("此功能未在本群启用");
    }
}
