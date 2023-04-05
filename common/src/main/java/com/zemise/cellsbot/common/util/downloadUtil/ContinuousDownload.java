package com.zemise.cellsbot.common.util.downloadUtil;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author Zemise_
 * @Date 2023/3/31
 * @Description 断点续传下载
 */
@Slf4j
public class ContinuousDownload {
    @Getter
    private static File downloadedFile;
    private static final int BUFFER_SIZE = 4096;

    /**
     * 下载文件，支持断点下载
     * @param fileURL 文件URL
     * @param saveDir 存储路径
     * @throws IOException 抛出
     */
    public static void downloadFile(String fileURL, String saveDir) throws IOException {
        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

        // 检查HTTP响应码是否为200
        if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            log.info("文件下载失败. 响应码: " + httpConn.getResponseCode());
            return;
        }

        String fileName = "";
        String disposition = httpConn.getHeaderField("Content-Disposition");
        String contentType = httpConn.getContentType();
        int contentLength = httpConn.getContentLength();

        if (disposition != null) {
            // 从Content-Disposition头中获取文件名
            int index = disposition.indexOf("filename=");
            if (index > 0) {
                fileName = disposition.substring(index + 10, disposition.length() - 1);
            }
        }

        if (fileName.equals("") || new File(fileName).isDirectory()) {
            //指定文件名字，尽量不包含上级文件夹
            //有时候，URL链接还有多余判断语句，导致上一条获取的依旧是文件夹而不是文件名，多加一条判断
            String[] urlParts = fileURL.split("/");
            fileName = urlParts[urlParts.length - 1].split("\\?")[0];
        }

        log.info("Content-Type = " + contentType);
        log.info("Content-Disposition = " + disposition);
        log.info("Content-Length = " + contentLength);
        log.info("File name = " + fileName);

        // 检查是否已经存在同名文件，如果已存在，则判断文件大小是否一致
        File localFile = new File(saveDir + File.separator + fileName);
        long downloadedLength = 0;
        if (localFile.exists()) {
            if (localFile.length() == contentLength) {
                log.info("文件已经存在，无需下载");
                return;
            } else {
                // 如果文件已经存在，那么使用断点续传方式下载
                downloadedLength = localFile.length();
                log.info("文件已经存在，使用断点续传方式下载，已下载：" + downloadedLength + "字节");

                httpConn.disconnect(); // 关闭之前的连接
                // 重新创建URLConnection连接
                httpConn = (HttpURLConnection) url.openConnection();

                httpConn.setRequestProperty("Range", "bytes=" + downloadedLength + "-");
            }
        }

        // 打开网络流和本地文件流
        InputStream inputStream = httpConn.getInputStream();
        FileOutputStream outputStream = new FileOutputStream(localFile, true);

        // 读取输入流并写入输出流
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            // 计算本次应该写入的数据长度
            int writeLength = bytesRead;
            if (downloadedLength + writeLength > contentLength) {
                writeLength = (int) (contentLength - downloadedLength);
            }

            // 写入数据到本地文件
            outputStream.write(buffer, 0, writeLength);

            // 更新已下载长度
            downloadedLength += writeLength;
            if (downloadedLength >= contentLength) {
                break;
            }
        }

        outputStream.close();
        inputStream.close();

        downloadedFile = localFile;
    }
}
