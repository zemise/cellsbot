package com.zemise.cellsbot.common.util.miraiUtil.filter;

import net.mamoe.mirai.event.events.MessageEvent;

/**
 * @Author Zemise_
 * @Date 2023/5/14
 * @Description 注解处理过滤器接口
 */
public interface ChannelFilterInter {
    /**
     * @param event 消息事件
     * @param text
     * @return Boolean
     */
    Boolean filter(MessageEvent event, String text);
}
