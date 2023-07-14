package com.zemise.cellsbot.common.plugin.screenshot.seleshot;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * @Author Zemise_
 * @Date 2023/4/27
 * @Description
 */
public class ScreenUtil {

    public static Long getHeight(WebDriver driver){
        // 获取当前网页的实际高度和宽度
        long pageHeight = (Long) ((JavascriptExecutor) driver).executeScript("return Math.max( document.body.scrollHeight, document.body.offsetHeight, document.documentElement.clientHeight, document.documentElement.scrollHeight, document.documentElement.offsetHeight )");
        return pageHeight;
    }
    public static Long getWidth(WebDriver driver){
        // 获取当前网页的实际高度和宽度
        long pageWidth = (Long) ((JavascriptExecutor) driver).executeScript("return Math.max( document.body.scrollWidth, document.body.offsetWidth, document.documentElement.clientWidth, document.documentElement.scrollWidth, document.documentElement.offsetWidth )");
        return pageWidth;
    }
  }
