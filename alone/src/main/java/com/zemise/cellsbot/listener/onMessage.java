//package com.zemise.cellsbot.listener;
//
//import com.zemise.cellsbot.common.ConfigManager;
//import com.zemise.cellsbot.common.util.miraiUtil.MiraiMessageUtil;
//import com.zemise.cellsbot.event.CommandMessageEvent;
//import com.zemise.cellsbot.event.NoCommandMessageEvent;
//import net.mamoe.mirai.event.EventHandler;
//import net.mamoe.mirai.event.SimpleListenerHost;
//import net.mamoe.mirai.event.events.FriendMessageEvent;
//import net.mamoe.mirai.event.events.GroupMessageEvent;
//import net.mamoe.mirai.event.events.MessageRecallEvent;
//
//import static com.zemise.cellsbot.Main.eventBus;
//
///**
// * @Author Zemise_
// * @Date 2023/5/14
// * @Description Mirai Bot的各类消息
// */
//public class onMessage extends SimpleListenerHost {
//
//    @EventHandler
//    public static void afterFriendMessage(FriendMessageEvent event) {
//
//    }
//
//    @EventHandler
//    public static void afterGroupMessage(GroupMessageEvent event) {
//        // 将群消息分类为指令消息以及普通消息，分流处理
//
//        // 获取设定的command前缀
//        String commandPrefix = ConfigManager.getConfig().getString("bot-command-prefix");
//        boolean isCommandMessage = MiraiMessageUtil.startWithKeywords(event, commandPrefix);
//
//
//        if (isCommandMessage) {
//            // 可以考虑把本次获得的具体指令传过去
//            eventBus.post(new CommandMessageEvent(event));
//
//        } else {
//            // 非指令消息
//            eventBus.post(new NoCommandMessageEvent(event));
//        }
//
//    }
//
//    @EventHandler
//    public static void afterRecallMessage(MessageRecallEvent event) {
//
//    }
//}
