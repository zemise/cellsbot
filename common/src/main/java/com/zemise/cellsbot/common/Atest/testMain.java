package com.zemise.cellsbot.common.Atest;

import com.zemise.cellsbot.common.evnet.EventBus;
import com.zemise.cellsbot.common.evnet.entity.gameChat;

/**
 * @Author Zemise_
 * @Date 2023/4/3
 * @Description 测试类003
 */
public class testMain {
    public static testMain instance;
    public static EventBus eventBus = new EventBus();
    public static void main(String[] args) {

        eventBus.register(new testListener());

        System.out.println("/A 说了第1句话，包含命令符");
        eventBus.post(new gameChat("/包含命令符"));
        eventBus.post(new gameChat("不包含命令符"));
        eventBus.post(new gameChat("不包含命令符"));


    }
}
