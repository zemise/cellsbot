package com.zemise.cellsbot.common.Atest;



import com.zemise.cellsbot.common.evnet.EventHandler;
import com.zemise.cellsbot.common.evnet.Listener;
import com.zemise.cellsbot.common.evnet.entity.gameChat;
import com.zemise.cellsbot.common.evnet.entity.newGameChat;

import static com.zemise.cellsbot.common.Atest.testMain.eventBus;

/**
 * @Author Zemise_
 * @Date 2023/4/3
 * @Description 测试类002
 */
public class testListener implements Listener {

    @EventHandler(priority = 0)
    public static void test001(newGameChat event) {
        System.out.println("事件触发了newGameChat事件");
        System.out.println("也就是说包含屏蔽词，原话是：" + event.getMessage());
    }

    @EventHandler(priority = 32)
    public static void test002(gameChat event) {
        System.out.println("事件触发了一次gameChat事件");

        if(event.getMessage().contains("/")){
            eventBus.post(new newGameChat(event.getMessage()));
        }

    }

}
