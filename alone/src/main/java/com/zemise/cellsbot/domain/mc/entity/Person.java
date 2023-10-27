package com.zemise.cellsbot.domain.mc.entity;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

/**
 * @Author Zemise_
 * @Date 2023/5/14
 * @Description person类，有一些基本的属性信息
 */
public class Person {
    @Getter
    private final String name;

    @Getter
    private final UUID id;

    public Person(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
    }
}
