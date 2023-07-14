package com.zemise.cellsbot.common.util.miraiUtil.filter.message;

import net.mamoe.mirai.event.events.MessageEvent;

/**
 * @Author Zemise_
 * @Date 2023/5/14
 * @Description 实体消息过滤类，此类判断是否prefix开头
 */
public class BeginMessageFilter extends AbstractMessageFilter {

    private BeginMessageFilter() {

    }

    @Override
    public Boolean filter(MessageEvent event, String prefix) {
        return event.getMessage().contentToString().startsWith(prefix);
    }
}
