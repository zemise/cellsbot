import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author zemise_
 * @Date 2023/4/24
 * @Description 通过ScreenMachine api 截图
 */
public class ScreenShot {
    private String apiKey;

    public ScreenShot(String apiKey) {
        this.apiKey = apiKey;
    }

    public void takePNG(String webUrl, String savePath) throws IOException, NoSuchAlgorithmException {
        ScreenshoMachine sm = new ScreenshoMachine(apiKey, "");

        Map<String, String> options = new HashMap<>();
        // mandatory parameter
        options.put("url", webUrl);
        // see website screenshot API guide for more details
        options.put("dimension", "1366x768"); // or "1366xfull" for full length screenshot
        options.put("device", "desktop");
        options.put("format", "png");
        options.put("cacheLimit", "0");
        options.put("delay", "200");
        options.put("zoom", "100");

        String apiUrl = sm.generateScreenshotApiUrl(options);

        // or save screenshot as an image
        URLConnection con = new URL(apiUrl).openConnection();
        con.addRequestProperty("User-Agent", "Mozilla/4.0");
        InputStream in = con.getInputStream();

        Files.copy(in, Paths.get(savePath), StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Screenshot saved as " + savePath);
    }
}
