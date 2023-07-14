package com.zemise.cellsbot.common.util.miraiUtil.filter.message;

import com.zemise.cellsbot.common.util.miraiUtil.filter.ChannelFilterInter;
import net.mamoe.mirai.event.events.MessageEvent;

/**
 * @Author Zemise_
 * @Date 2023/5/14
 * @Description 注解处理过滤器接口
 */
public abstract class AbstractMessageFilter implements ChannelFilterInter {

    @Override
    public abstract Boolean filter(MessageEvent event, String text);
}
