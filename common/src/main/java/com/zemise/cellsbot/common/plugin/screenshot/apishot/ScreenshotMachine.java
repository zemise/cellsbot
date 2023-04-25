package com.zemise.cellsbot.common.plugin.screenshot.apishot;

import javax.xml.bind.DatatypeConverter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import static com.zemise.cellsbot.common.plugin.screenshot.apishot.Constant.API_BASE_URL;
import static com.zemise.cellsbot.common.plugin.screenshot.apishot.Constant.PDF_API_BASE_URL;

@SuppressWarnings("unused")
public class ScreenshotMachine {
    private final String customerKey;
    private final String secretPhrase;


    public ScreenshotMachine(String customerKey, String secretPhrase) {
        this.customerKey = customerKey;
        this.secretPhrase = secretPhrase;
    }

    public String generateScreenshotApiUrl(Map<String, String> options) throws NoSuchAlgorithmException {
        return generateUrl(API_BASE_URL, options);
    }

    public String generatePdfApiUrl(Map<String, String> options) throws NoSuchAlgorithmException {
        return generateUrl(PDF_API_BASE_URL, options);
    }

    public String generateUrl(String baseUrl, Map<String, String> options) throws NoSuchAlgorithmException {
        StringBuilder apiUrl = new StringBuilder(baseUrl);
        apiUrl.append("key=").append(customerKey);
        if (secretPhrase != null && secretPhrase.trim().length() > 0) {
            apiUrl.append("&hash=").append(calculateHash(options.get("url") + secretPhrase));
        }
        for (String key : options.keySet()) {
            apiUrl.append("&").append(key).append("=").append(URLEncoder.encode(options.get(key), StandardCharsets.UTF_8));
        }
        return apiUrl.toString();
    }

    private String calculateHash(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(text.getBytes());
        return DatatypeConverter.printHexBinary(md.digest()).toLowerCase();
    }

}