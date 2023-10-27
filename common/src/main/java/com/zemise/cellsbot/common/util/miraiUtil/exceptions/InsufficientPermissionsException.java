package com.zemise.cellsbot.common.util.miraiUtil.exceptions;

public class InsufficientPermissionsException extends Exception {

    /**
     * 异常：权限不足异常
     */
    public InsufficientPermissionsException() {
    }

    public InsufficientPermissionsException(String message) {
        super(message);
    }
}
