package com.zemise.cellsbot.common.util.fileUtil;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @Author Zemise_
 * @Date 2023/4/2
 * @Description MoreFiles
 */
public final class MoreFiles {
    // 防止类被实例化
    private MoreFiles() {
    }

    /**
     * 如果指定路径不存在，则创建一个空文件。
     *
     * @param path 要创建的文件路径
     * @return 创建的文件路径
     * @throws IOException 如果文件创建失败
     */
    public static Path createFileIfNotExists(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        return path;
    }

    /**
     * 如果指定路径不存在，则创建一个新目录。
     *
     * @param path 要创建的目录路径
     * @return 创建的目录路径
     * @throws IOException 如果目录创建失败
     */
    public static Path createDirectoryIfNotExists(Path path) throws IOException {
        if (Files.exists(path) && (Files.isDirectory(path) || Files.isSymbolicLink(path))) {
            return path;
        }
        try {
            Files.createDirectory(path);
        } catch (FileAlreadyExistsException ignored) {
        }

        return path;
    }

    /**
     * 如果指定路径不存在，则创建所有缺少的父目录，最后创建指定目录。
     *
     * @param path 要创建的目录路径
     * @return 创建的目录路径
     * @throws IOException 如果目录创建失败
     */
    public static Path createDirectoriesIfNotExists(Path path) throws IOException {
        if (Files.exists(path) && (Files.isDirectory(path) || Files.isSymbolicLink(path))) {
            return path;
        }

        try {
            Files.createDirectories(path);
        } catch (FileAlreadyExistsException e) {
            // ignore
        }

        return path;
    }

    /**
     * 递归删除指定目录和其中的所有文件和子目录。
     *
     * @param path 要删除的目录路径
     * @throws IOException 如果删除操作失败
     */
    public static void deleteDirectory(Path path) throws IOException {
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            return;
        }

        try (DirectoryStream<Path> contents = Files.newDirectoryStream(path)) {
            for (Path file : contents) {
                if (Files.isDirectory(file)) {
                    deleteDirectory(file);
                } else {
                    Files.delete(file);
                }
            }
        }

        Files.delete(path);
    }
}
