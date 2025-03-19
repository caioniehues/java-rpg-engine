package io.cote.chatdm.oracle.cli;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Configuration for handling command line options.
 */
@Configuration
public class CommandLineConfig {

    /**
     * Sets the external oracle directory from command line arguments.
     */
    @Bean
    public ApplicationRunner commandLineRunner(Environment environment) {
        return args -> {
            // Check for --oracle-dir argument
            if (args.containsOption("oracle-dir")) {
                String externalDir = args.getOptionValues("oracle-dir").get(0);
                // Set system property to override application.properties
                System.setProperty("oracle.external.directory", externalDir);
            }
        };
    }
}
