package com.zemise.cellsbot.common.plugin.mcping.examples;


import com.zemise.cellsbot.common.plugin.mcping.MCPing;
import com.zemise.cellsbot.common.plugin.mcping.MCPingOptions;
import com.zemise.cellsbot.common.plugin.mcping.MCPingResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MCPingExample {

    public static void main(String[] args) throws IOException {

        MCPingOptions options = MCPingOptions.builder()
                .hostname("cellcraft.top").port(25565)
                .build();

        MCPingResponse reply;

        try {
            reply = MCPing.getPing(options);
        } catch (IOException ex) {
            System.out.println(options.getHostname() + " is down or unreachable.");
            return;
        }

        System.out.printf("Full response from %s:%n", options.getHostname());
        System.out.println();

        MCPingResponse.Description description = reply.getDescription();

        System.out.println("Description:");
        System.out.println("    Raw: " + description.getText());
        System.out.println("    No color codes: " + description.getStrippedText());
        System.out.println();

        MCPingResponse.Players players = reply.getPlayers();

        System.out.println("Players: ");
        System.out.println("    Online count: " + players.getOnline());
        System.out.println("    Max players: " + players.getMax());
        System.out.println();

        // Can be null depending on the server
        List<MCPingResponse.Player> sample = players.getSample();

        if (sample != null) {
            System.out.println("    Players: " + players.getSample().stream()
                    .map(player -> String.format("%s@%s", player.getName(), player.getId()))
                    .collect(Collectors.joining(", "))
            );
            System.out.println();
        }

        MCPingResponse.Version version = reply.getVersion();

        System.out.println("Version: ");

        // The protocol is the version number: http://wiki.vg/Protocol_version_numbers
        System.out.println("    Protocol: " + version.getProtocol());
        System.out.println("    Name: " + version.getName());
        System.out.println();


        System.out.printf("Favicon: %s%n", reply.getFavicon());
    }


}
