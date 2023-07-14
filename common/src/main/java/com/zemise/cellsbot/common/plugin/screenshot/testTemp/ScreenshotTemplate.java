package com.zemise.cellsbot.common.plugin.screenshot.testTemp;

import java.io.IOException;

/**
 * @Author Zemise_
 * @Date 2023/4/27
 * @Description
 */
// 定义一个抽象类，包含一个模板方法和三个抽象方法
public abstract class ScreenshotTemplate {
    // 模板方法，定义了算法的骨架
    public final void capture() throws IOException, InterruptedException {
        // 第一步，初始化操作
        init();
        // 第二步，执行操作
        execute();
        // 第三步，关闭资源
        close();
    }

    // 抽象方法，由子类实现
    protected abstract void init();

    // 抽象方法，由子类实现
    protected abstract void execute() throws IOException, InterruptedException;

    // 抽象方法，由子类实现
    protected abstract void close();
}


