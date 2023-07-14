package com.zemise.cellsbot.common.util.miraiUtil.filter.message;

import net.mamoe.mirai.event.events.MessageEvent;

/**
 * @Author Zemise_
 * @Date 2023/5/14
 * @Description 实体消息过滤类，此类判断是否包含s
 */
public class ContainsMessageFilter extends AbstractMessageFilter {
    private ContainsMessageFilter() {
    }

    @Override
    public Boolean filter(MessageEvent event, String s) {
        return event.getMessage().contentToString().contains(s);
    }

}
