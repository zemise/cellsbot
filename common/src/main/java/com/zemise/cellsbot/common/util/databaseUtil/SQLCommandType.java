package com.zemise.cellsbot.common.util.databaseUtil;

import lombok.Getter;

@Getter
public enum SQLCommandType {
    SELECT("select"),
    INSERT("insert"),
    UPDATE("update"),
    DELETE("delete"),

    ;
    private final String name;
    SQLCommandType(String name) {
        this.name = name;
    }
}