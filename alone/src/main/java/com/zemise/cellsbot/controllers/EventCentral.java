package com.zemise.cellsbot.controllers;

import com.zemise.cellsbot.common.evnet.EventBus;
import com.zemise.cellsbot.domain.qq.service.QQService;
import com.zemise.cellsbot.event.custom.handle.BotOnlineEventHandle;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 * @author Zemise
 */
@Component
public class EventCentral {
    public EventBus eventBus;
    private final QQService service;

    public EventCentral(QQService service) {
        this.service = service;
    }

    @PostConstruct
    private void registerEvent() {
        eventBus = new EventBus();
        // 进行注册
        eventBus.register(new BotOnlineEventHandle(service));
    }
}
