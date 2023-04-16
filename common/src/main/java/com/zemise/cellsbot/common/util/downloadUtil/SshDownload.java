package com.zemise.cellsbot.common.util.downloadUtil;

import com.jcraft.jsch.*;

import java.io.*;

/**
 * @Author Zemise_
 * @Date 2023/4/8
 * @Description ssh远程主机下载
 */
public class SshDownload {
    private static final int BUFFER_SIZE = 1024;
    private static final int DEFAULT_TIMEOUT = 3000;
    private static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * 从远程主机下载文件到本地
     *
     * @param remoteHost     远程主机地址
     * @param username       登录用户名
     * @param port           端口号
     * @param password       登录密码
     * @param remoteFilePath 远程文件路径
     * @param localFilePath  本地文件路径
     */
    public static void downloadFile(String remoteHost, int port, String username, String password,
                                    String remoteFilePath, String localFilePath) throws DownloadException {
        JSch jsch = new JSch();
        Session session = null;
        try {
            session = jsch.getSession(username, remoteHost, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.setTimeout(DEFAULT_TIMEOUT);
            session.connect();

            ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
            channelSftp.setFilenameEncoding(DEFAULT_ENCODING);

            InputStream inputStream = channelSftp.get(remoteFilePath);
            OutputStream outputStream = new FileOutputStream(localFilePath);

            transferData(inputStream, outputStream);

            inputStream.close();
            outputStream.close();
            channelSftp.disconnect();
            session.disconnect();
        } catch (JSchException | SftpException | IOException e) {
            throw new DownloadException("下载文件失败：" + e.getMessage(), e);
        }
    }

    private static void transferData(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, bytesRead);
        }
    }

    public static class DownloadException extends Exception {
        public DownloadException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
