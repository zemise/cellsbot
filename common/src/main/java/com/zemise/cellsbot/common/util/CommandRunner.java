package com.zemise.cellsbot.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class CommandRunner {
    private final List<String> commands;
    private final String workingDir;
    private final Map<String, String> environmentVariables;

    public CommandRunner(String command) {
        this(Arrays.asList(command), null, null);
    }

    public CommandRunner(List<String> commands) {
        this(commands, null, null);
    }

    public CommandRunner(List<String> commands, String workingDir, Map<String, String> environmentVariables) {
        this.commands = commands;
        this.workingDir = workingDir;
        this.environmentVariables = environmentVariables;
    }

    public CompletableFuture<List<Integer>> runCommandsAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return runCommands();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public List<Integer> runCommands() throws IOException, InterruptedException {
        log.info("Running commands: " + commands);
        List<Integer> exitCodes = new ArrayList<>();
        for (String command : commands) {
            ProcessBuilder pb = new ProcessBuilder(getCommandList(command));
            if (workingDir != null) {
                pb.directory(new File(workingDir));
            }
            if (environmentVariables != null) {
                pb.environment().putAll(environmentVariables);
            }
            pb.redirectErrorStream(true); // 将错误输出流与标准输出流合并
            Process process = pb.start();
            printOutput(process.getInputStream());
            int exitCode = process.waitFor();
            exitCodes.add(exitCode);
            log.info("Command exited with code: " + exitCode);
        }
        return exitCodes;
    }

    private List<String> getCommandList(String command) {
        List<String> commandList = new ArrayList<>();
        String[] commandSplit = command.split("\\s+");
        commandList.addAll(Arrays.asList(commandSplit));
        return commandList;
    }

    private void printOutput(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            log.info(line);
        }
    }
}
