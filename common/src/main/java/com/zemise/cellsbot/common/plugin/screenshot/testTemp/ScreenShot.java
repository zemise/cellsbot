package com.zemise.cellsbot.common.plugin.screenshot.testTemp;

import com.zemise.cellsbot.common.plugin.screenshot.seleshot.ScreenShotMachine;
import com.zemise.cellsbot.common.plugin.screenshot.seleshot.ScreenshotOptions;
import org.openqa.selenium.Proxy;

import java.io.IOException;

// 定义一个具体子类，继承抽象类，并实现抽象方法
public class ScreenShot extends ScreenshotTemplate {
    ScreenShotMachine screenShotMachine;

    @Override
    protected void init() {
        String proxyServer = "localhost:7890";
        Proxy proxy = new Proxy();
        proxy.setHttpProxy(proxyServer).setHttpProxy(proxyServer).setSslProxy(proxyServer);

        ScreenshotOptions options = ScreenshotOptions.builder()
                .height(1366)
                .width(1600)
                .waitTime(2500)
                .proxy(proxy)
                .build();

        String driverPath = "/Users/zhaowang/Downloads/chromedriver_mac64-2/chromedriver";
        screenShotMachine = new ScreenShotMachine(driverPath, options);
    }

    @Override
    protected void execute() throws IOException, InterruptedException {
        System.out.println("执行截图操作");
        screenShotMachine.takePNG("https://kns.cnki.net/kns8/AdvSearch?dbprefix=CFLS&&crossDbcodes=CJFQ%2CCDMD%2CCIPD%2CCCND%2CCISD%2CSNAD%2CBDZK%2CCCJD%2CCCVD%2CCJFN", "out.png");
        screenShotMachine.takePNG("https://baidu.com", "outs.png");
    }

    @Override
    protected void close() {
        screenShotMachine.close();
    }

}
