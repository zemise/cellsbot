package com.zemise.cellsbot.controllers;

import com.zemise.cellsbot.common.util.miraiUtil.BotOperator;
import com.zemise.cellsbot.config.BotConfig;
import com.zemise.cellsbot.domain.qq.service.QQService;
import com.zemise.cellsbot.event.custom.BotOnlineEvent;
import com.zemise.cellsbot.event.miari.RecordMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

/**
 * @author Zemise
 */
@Controller
@Slf4j
public class BotController {
    private final BotConfig botConfig;

    private final EventCentral eventCentral;

    private final QQService qqService;

    @Autowired
    public BotController(BotConfig botConfig, EventCentral eventCentral, QQService qqService) {
        this.botConfig = botConfig;
        this.eventCentral = eventCentral;
        this.qqService = qqService;
    }


    @Bean
    public void login() {
        BotOperator.login(botConfig.getConfig().getLong("bot-account"), botConfig.getConfig().getString("bot-password"));

        // 登录成功后注册一系列事情
        eventCentral.eventBus.post(new BotOnlineEvent("qq bot登陆成功"));
        // 注册QQ事件
        //BotOperator.getBot().getEventChannel().registerListenerHost(new RecallEventHandle());
        //BotOperator.getBot().getEventChannel().registerListenerHost(new ResetPasswordEventHandle());
        //BotOperator.getBot().getEventChannel().registerListenerHost(new QGroupMessageEvent());
        BotOperator.getBot().getEventChannel().registerListenerHost(new RecordMessage(qqService));
    }
}
