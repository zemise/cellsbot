package com.zemise.cellsbot.common.plugin.screenshot.seleshot.example;

import com.zemise.cellsbot.common.plugin.screenshot.testTemp.ScreenShot;
import org.jetbrains.annotations.TestOnly;

import java.io.IOException;

/**
 * @Author Zemise_
 * @Date 2023/4/27
 * @Description shot screen example
 */

@TestOnly
public class ShotExample {
    public static void main(String[] args) throws IOException, InterruptedException {
        ScreenShot screen= new ScreenShot();
        screen.capture();

    }
}
