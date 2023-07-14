package com.zemise.cellsbot.common.util.miraiUtil.filter.message;

import net.mamoe.mirai.event.events.MessageEvent;

import java.util.regex.Pattern;

/**
 * @Author Zemise_
 * @Date 2023/5/14
 * @Description 消息过滤类，此类判断消息是否满足regex
 */
public class RegexMessageFilter extends AbstractMessageFilter {
    private RegexMessageFilter() {

    }


    @Override
    public Boolean filter(MessageEvent event, String regex) {
        return Pattern.compile(regex).matcher(event.getMessage().contentToString()).find();
    }
}
