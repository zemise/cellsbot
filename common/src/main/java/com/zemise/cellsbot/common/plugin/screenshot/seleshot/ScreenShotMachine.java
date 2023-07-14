package com.zemise.cellsbot.common.plugin.screenshot.seleshot;

import com.zemise.cellsbot.common.util.NumberConversions;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * @Author Zemise_
 * @Date 2023/4/24
 * @Description 通过selenium网页截图
 */

@SuppressWarnings("unused")
public class ScreenShotMachine {
    private final String driverPath;
    private final WebDriver driver;
    private final ScreenshotOptions options;

    public ScreenShotMachine(String driverPath) {
        this(driverPath, null);
    }

    public ScreenShotMachine(String driverPath, ScreenshotOptions options) {
        this.driverPath = driverPath;
        this.options = options;
        this.driver = initWebDriver();
    }

    public void takePNG(List<String> webUrls, String savePath) {
        webUrls.forEach(webUrl -> takePNG(webUrl, savePath));
    }

    public void takePNG(String webUrl, String savePath) {
        try {
            driver.get(webUrl);
            Thread.sleep(options.getWaitTime());

            if (options.isFullWeb()) {
                // 设置浏览器窗口大小为网页大小
                int width = NumberConversions.toInt(ScreenUtil.getWidth(driver));
                int height = NumberConversions.toInt(ScreenUtil.getHeight(driver));
                driver.manage().window().setSize(new Dimension(width, height));
            }

            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(srcFile, new File(savePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private WebDriver initWebDriver() {
        System.setProperty(
                String.format("webdriver.%s.driver", options.getDriver().getName()),
                driverPath);

        if (options.isWhitelistedIps()) {
            System.setProperty(
                    String.format("webdriver.%s.whitelistedIps", options.getDriver().getName()),
                    "");
        }

        // todo 还只写了驱动Chrome，关键是每个浏览器的Option还不一样
        ChromeOptions option = createChromeOptions();
        return new ChromeDriver(option);
    }

    private ChromeOptions createChromeOptions() {
        ChromeOptions webOption = new ChromeOptions();

        //固定
        webOption.addArguments("disable-infobars");
        webOption.addArguments("--dns-prefetch-disable");
        webOption.addArguments("--disable-gpu");
        webOption.addArguments("--disable-audio");
        webOption.addArguments("--ignore-certificate-errors");
        webOption.addArguments("--no-referrers");
        webOption.addArguments("--allow-insecure-localhost");
        webOption.addArguments("--no-sandbox");

        webOption.addArguments(String.format("--window-size=%d,%d", options.getWidth(), options.getHeight()));

        if (options.isRemoteAllow()) webOption.addArguments("--remote-allow-origins=*");
        if (options.isHeadless()) webOption.addArguments("--headless");
        if (options.getProxy() != null) webOption.setProxy(options.getProxy());

        return webOption;
    }

    public void close() {
        driver.quit();
    }
}
