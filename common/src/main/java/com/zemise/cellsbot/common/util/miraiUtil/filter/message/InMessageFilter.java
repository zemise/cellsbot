package com.zemise.cellsbot.common.util.miraiUtil.filter.message;

import net.mamoe.mirai.event.events.MessageEvent;

import java.util.List;

/**
 * @Author Zemise_
 * @Date 2023/5/14
 * @Description 消息过滤类，此类判断是消息是否在text中
 */
public class InMessageFilter extends AbstractMessageFilter{
    private InMessageFilter() {
    }

    @Override
    public Boolean filter(MessageEvent event, String text) {
        return List.of(text.split(",\\s+")).contains(event.getMessage().contentToString());
    }
}
