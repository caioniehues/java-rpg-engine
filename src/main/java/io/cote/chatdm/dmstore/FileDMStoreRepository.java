package io.cote.chatdm.dmstore;

import io.cote.chatdm.config.ChatDMProperties;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.*;


/**
 * This repository gives access to the files used by various ChatDM tools, for example, oracles.
 *
 * It will create a defaults <code>.chatdm</code> directory and populate it with defaul files from the JAR file if
 * they do not exist.
 */
@Repository
public class FileDMStoreRepository {

    private static final Logger logger = LoggerFactory.getLogger(FileDMStoreRepository.class);

    private final Path dmDir;

    // Inject ChatDMProperties into the constructor
    public FileDMStoreRepository(ChatDMProperties properties) {
        this.dmDir = properties.getChatDMDirPath().toAbsolutePath();
    }

    /**
     * Setups up the .chatdm directory if it does not exist.
     *
     * @throws IOException
     */
    @PostConstruct
    void init() throws IOException {
        if (!Files.exists(dmDir)) {
            Files.createDirectories(dmDir);
            logger.info("Created the DM store directory: {}", dmDir);
        }
        else {
            logger.info(".chatdm directory already exists: {}", dmDir);
        }


        // TK This should be done in a properties file with a list of defaults
        // if not defined.
        logger.info("Creating and populating bundles with default files.");
        createAndPopulateBundle("oracle");
        createAndPopulateBundle("prompts");
        createAndPopulateBundle("journal");
        createAndPopulateBundle("dc");
    }

    /**
     * Retrieves the directory for the named store, e.g., "oracle."
     * @param name name of the top-level directory in the .chatdm dir.
     * @return
     */
    public File getNamedDir(String name) {
        File dir = dmDir.resolve(name).toFile();
        if (!dir.exists()) {
            logger.info( "Creating directory: {}", dir);
            dir.mkdir();
            // find any default files

        }
        return dmDir.resolve(name).toFile();
    }


    /**
     * The root of the chatdm directory.
     *
     * @return {@link Path} for the root directory.
     */
    public Path getDirPath() {
        return dmDir;
    }


    /**
     * Loads a file from the DM store. If this file does not exist, it will look for the default file in the classpath.
     * If it finds that file, it will write that file out to the DM store.
     *
     * @param fileName the name of the file/resource.
     * @return the File, or null if no File by that name exists.
     * @throws IOException
     */
    public File load(String fileName) throws IOException {
        Path filePath = dmDir.resolve(fileName);
        if (Files.exists(filePath)) {
            return filePath.toFile();
        } else {
            return null;
        }
    }

    /**
     * Loads the file and converts it to a String. Returns null if there is no such file.
     *
     * @param fileName path to file to retrieve.
     * @return the contents of the file
     * @throws IOException
     */
    public String loadToString(String fileName) throws IOException {
        File file = load(fileName);
        if (file == null) {
            return null;
        } else {
            return new String(Files.readAllBytes(file.toPath()));
        }
    }

    public void save(String name, InputStream data) throws IOException {
        Path target = dmDir.resolve(name);
        Files.createDirectories(target.getParent());
        Files.copy(data, target, StandardCopyOption.REPLACE_EXISTING);
    }

    private synchronized void createAndPopulateBundle(String bundleName) throws IOException {

        // Get files for bundle
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        String pathPattern = "classpath:/chatdm-default-files/" + bundleName + "/*";
        Resource [] resources = resolver.getResources(pathPattern);

        // Create the directory for the bundle if it does not exist
        File bundleDir = new File(dmDir.toFile(), bundleName);
        if (!bundleDir.exists()) {
            bundleDir.mkdir();
        }

        // Iterate over the resources and write them out to the bundle directory
        for (Resource resource : resources) {
            try (InputStream inputStream = resource.getInputStream()) {
                // TK check for null filename
                Path destinationPath = bundleDir.toPath().resolve(resource.getFilename());
                // Create necessary directories
                Files.createDirectories(destinationPath.getParent());

                if (!Files.exists(destinationPath)) {
                    // Write the contents from the InputStream into the destination file
                    // Only write it out if it does not exist.
                    // We want people to be able ot edit these files and keep using them, only
                    // replacing them if they are not there.
                    Files.copy(inputStream, destinationPath);
                }
            } catch (IOException e) {
                logger.error("Error copying default resource {} for bundle {}",
                        resource,
                        bundleName,
                        e);
            }
        }
    }
}