package com.zemise.cellsbot.common.util.miraiUtil.filter.message;

import net.mamoe.mirai.event.events.MessageEvent;

/**
 * @Author Zemise_
 * @Date 2023/5/14
 * @Description 消息过滤类，此类判断是否与text相等
 */
public class EqualsMessageFilter extends AbstractMessageFilter {
    private EqualsMessageFilter() {

    }

    @Override
    public Boolean filter(MessageEvent event, String text) {
        return event.getMessage().contentToString().equals(text);
    }
}
