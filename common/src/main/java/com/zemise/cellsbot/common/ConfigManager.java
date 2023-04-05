package com.zemise.cellsbot.common;

import com.zemise.cellsbot.common.configuration.Configuration;
import com.zemise.cellsbot.common.configuration.file.YamlConfiguration;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * @Author Zemise
 * @Date 2023/4/5
 * @Description plugin文件加载
 */

public class ConfigManager {
    @Getter
    public static ConfigManager instance;
    @Getter
    private static Configuration config, messageConfig, autoReplyConfig;

    //所有配置文件
    private static String[] config_files = {"config.yml", "messages.yml", "auto_reply.yml", "cells.png"};

    // plugin目录对象
    @Setter
    public static File pluginDirectory;

    static {
        reload();
    }

    //直接重载所有的配置文件
    public static void reload() {
        config = YamlConfiguration.loadConfiguration(new File(pluginDirectory, "config.yml"));
        messageConfig = YamlConfiguration.loadConfiguration(new File(pluginDirectory, "messages.yml"));
        autoReplyConfig = YamlConfiguration.loadConfiguration(new File(pluginDirectory, "auto_reply.yml"));
    }

    //生成默认的配置文件
    public static void saveDefaultConfig(File directory) {
        setPluginDirectory(directory);

        //判断是否有相应的配置文件夹，如果无，生成plugin文件夹
        if (directory.exists()) directory.mkdirs();

        //生成默认的配置文件，文件不存在时,就生成；存在同名文件，不进行覆盖
        for (String s : config_files) {
            File file = new File(directory, s);
            if (!file.exists()) {
                try (InputStream is = ConfigManager.class.getClassLoader().getResourceAsStream(s)) {
                    Files.copy(is, file.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
