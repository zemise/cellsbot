package com.zemise.cellsbot.common.util.miraiUtil.filter.message;

import net.mamoe.mirai.event.events.MessageEvent;

/**
 * @Author Zemise_
 * @Date 2023/5/14
 * @Description 消息过滤类，此类判断是否以text结尾
 */
public class EndMessageFilter extends AbstractMessageFilter {
    private EndMessageFilter() {
    }

    @Override
    public Boolean filter(MessageEvent event, String suffix) {
        return event.getMessage().contentToString().endsWith(suffix);
    }
}
