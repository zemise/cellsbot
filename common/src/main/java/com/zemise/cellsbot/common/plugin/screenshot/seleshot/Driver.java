package com.zemise.cellsbot.common.plugin.screenshot.seleshot;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chromium.ChromiumDriver;


@Getter
public enum Driver {
    CHROME("chrome"),
    EDGE("edge"),
    FIREFOX("firefox"),
    GECKO("gecko"),
    ;
    private final String name;

    Driver(String name) {
        this.name = name;
    }
}
