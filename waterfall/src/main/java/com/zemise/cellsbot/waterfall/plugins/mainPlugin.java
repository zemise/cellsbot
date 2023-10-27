package com.zemise.cellsbot.waterfall.plugins;

import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.QuoteReply;

import java.util.Map;

public interface mainPlugin {
//    default void setContextClassLoader() {
//        Thread currentThread = Thread.currentThread();
//        currentThread.setContextClassLoader(Mirai.class.getClassLoader());
//    }
    //MessageChain
    MessageChain onEnable(Long groupID, Long senderID, Message[] args, QuoteReply quoteReply);

    Map<String, Object> register() throws NoSuchMethodException;
}
