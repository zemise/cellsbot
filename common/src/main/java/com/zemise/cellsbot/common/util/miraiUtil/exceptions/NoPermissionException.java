package com.zemise.cellsbot.common.util.miraiUtil.exceptions;

public class NoPermissionException extends Exception {
    /**
     * 异常：管理群成员才拥有此权限，具体联系管理员
     */
    public NoPermissionException() {
        super("管理群成员才拥有此权限，具体联系管理员");
    }
}
