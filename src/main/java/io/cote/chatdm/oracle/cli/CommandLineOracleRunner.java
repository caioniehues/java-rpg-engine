package io.cote.chatdm.oracle.cli;

import io.cote.chatdm.oracle.Oracle;
import io.cote.chatdm.oracle.OracleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Scanner;

/**
 * Command line interface for interacting with oracles.
 * This will only run when the "cli" profile is active.
 */
@Component
@Profile("cli")
public class CommandLineOracleRunner implements CommandLineRunner {

    private final OracleService oracleService;

    @Autowired
    public CommandLineOracleRunner(OracleService oracleService) {
        this.oracleService = oracleService;
    }

    @Override
    public void run(String... args) {
        System.out.println("=== ChatDM Oracle System ===");
        System.out.println("Available commands:");
        System.out.println("  list          - List all available oracles");
        System.out.println("  roll [oracle] - Get a random entry from an oracle");
        System.out.println("  exit          - Exit the program");

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.print("\nCommand: ");
            String line = scanner.nextLine().trim();

            if (line.equalsIgnoreCase("exit")) {
                running = false;
            } else if (line.equalsIgnoreCase("list")) {
                listOracles();
            } else if (line.startsWith("roll ")) {
                String oracleName = line.substring(5).trim();
                rollOracle(oracleName);
            } else {
                System.out.println("Unknown command. Type 'list', 'roll [oracle]', or 'exit'.");
            }
        }

        System.out.println("Goodbye!");
    }

    private void listOracles() {
        Map<String, Integer> oracleInfo = oracleService.getOracleInfo();

        if (oracleInfo.isEmpty()) {
            System.out.println("No oracles found.");
            return;
        }

        System.out.println("Available oracles:");
        for (Map.Entry<String, Integer> entry : oracleInfo.entrySet()) {
            System.out.printf("  %s (%d entries)%n", entry.getKey(), entry.getValue());
        }
    }

    private void rollOracle(String oracleName) {
        if (!oracleService.hasOracle(oracleName)) {
            System.out.println("Oracle not found: " + oracleName);
            return;
        }
        else {
            Oracle oracle = oracleService.getOracle(oracleName);
            System.out.println("Result from '" + oracleName + "':");
            System.out.println("  " + oracle.getRandomResult());
        }
    }
}
