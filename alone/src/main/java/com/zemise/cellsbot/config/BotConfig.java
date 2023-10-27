package com.zemise.cellsbot.config;

import com.zemise.cellsbot.common.ConfigManager;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.io.File;


@Configuration
@Data
@Slf4j
public class BotConfig {
    public com.zemise.cellsbot.common.configuration.Configuration config, messageConfig, autoReplyConfig;
    @PostConstruct
    protected void initProjectFile(){
        File projectFile = new File("Cells");
        if (!projectFile.exists()) {
            log.warn("请在生成的配置文件中修改相关配置后，再启动！");
        }

        ConfigManager.saveDefaultConfig(projectFile);
        ConfigManager.reload();

        config = ConfigManager.getConfig();
        messageConfig= ConfigManager.getMessageConfig();
        autoReplyConfig = ConfigManager.getAutoReplyConfig();

    }
}
