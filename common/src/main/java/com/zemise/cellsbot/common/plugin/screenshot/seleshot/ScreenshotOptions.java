package com.zemise.cellsbot.common.plugin.screenshot.seleshot;

import lombok.Builder;
import lombok.Getter;
import org.openqa.selenium.Proxy;

/**
 * @Author Zemise_
 * @Date 2023/4/27
 * @Description 基本调节参数
 */
@Builder
public class ScreenshotOptions {
    @Getter
    @Builder.Default
    private int width = 1366;

    @Getter
    @Builder.Default
    private int height = 1300;

    @Getter
    @Builder.Default
    private boolean fullWeb = false;

    @Getter
    @Builder.Default
    private boolean headless = true;

    @Getter
    @Builder.Default
    private Integer waitTime = 1000;

    @Getter
    @Builder.Default
    private Proxy proxy = null;

    @Getter
    @Builder.Default
    private Driver driver = Driver.CHROME;

    @Getter
    @Builder.Default
    private boolean whitelistedIps = false;

    @Getter
    @Builder.Default
    private boolean remoteAllow = false;
}
