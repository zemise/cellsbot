package com.zemise.cellsbot.common.plugin.screenshot.seleshot;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Author Zemise_
 * @Date 2023/4/24
 * @Description 通过selenium网页截图
 */

@SuppressWarnings("unused")
public class ScreenShot {
    private final String driverPath;
    protected Proxy proxy;
    private final WebDriver driver;

    public ScreenShot(String driverPath) {
        this(driverPath, null, null);
    }

    public ScreenShot(String driverPath, List<String> arguments) {
        this(driverPath, null, arguments);
    }

    public ScreenShot(String driverPath, Proxy proxy, List<String> arguments) {
        this.driverPath = driverPath;
        this.proxy = proxy;
        this.driver = initWebDriver(arguments);
    }

    public void takePNG(String webUrl, String savePath) throws IOException, InterruptedException {
        takePNG(webUrl, savePath, null);
    }

    public void takePNG(String webUrl, String savePath, Integer waitTime) throws IOException, InterruptedException {
        driver.get(webUrl);
        if (waitTime != null) {
            Thread.sleep(waitTime);
        }
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(srcFile, new File(savePath));
    }

    public WebDriver initWebDriver(List<String> arguments) {
        System.setProperty("java.awt.headless", "true");
        ChromeOptions option = createChromeOptions(arguments);
        // remote
        //System.setProperty("webdriver.chrome.whitelistedIps", "");
        System.setProperty("webdriver.chrome.driver", driverPath);
        return new ChromeDriver(option);
    }

    public ChromeOptions createChromeOptions(List<String> arguments) {
        ChromeOptions option = new ChromeOptions();

        //可变
        //option.addArguments("--remote-allow-origins=*");
        //option.addArguments("--headless");
        //option.addArguments("--window-size=1366,1300");

        //固定
        option.addArguments("disable-infobars");
        option.addArguments("--dns-prefetch-disable");
        option.addArguments("--disable-gpu");
        option.addArguments("--disable-audio");
        option.addArguments("--ignore-certificate-errors");
        option.addArguments("--no-referrers");
        option.addArguments("--allow-insecure-localhost");
        option.addArguments("--no-sandbox");

        if (arguments != null) {
            for (String argument : arguments) {
                option.addArguments(argument);
            }
        }

        if (proxy != null) {
            option.setProxy(proxy);
        }
        return option;
    }

    public void close() {
        driver.quit();
    }
}
