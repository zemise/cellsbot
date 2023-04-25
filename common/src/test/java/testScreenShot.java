import com.zemise.cellsbot.common.plugin.screenshot.seleshot.ScreenShot;
import org.jetbrains.annotations.TestOnly;
import org.openqa.selenium.Proxy;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @Author Zemise_
 * @Date 2023/4/24
 * @Description 测试网页截图
 */
public class testScreenShot {
    @TestOnly
    public static void main(String[] args) throws IOException, InterruptedException {

        String proxyServer = "localhost:7890";
        Proxy proxy = new Proxy();
        proxy.setHttpProxy(proxyServer).setHttpProxy(proxyServer).setSslProxy(proxyServer);

        ArrayList<String> objects = new ArrayList<>();
        objects.add("--headless");
        objects.add("--window-size=1366,1300");

        ScreenShot screenShot = new ScreenShot("driverPath", objects);

        screenShot.takePNG("https://bilibili.com", "out.png", 1000);
        screenShot.takePNG("https://baidu.com", "outs.png");
        screenShot.close();
    }
}
