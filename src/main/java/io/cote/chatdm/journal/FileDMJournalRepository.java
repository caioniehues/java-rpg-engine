package io.cote.chatdm.journal;

import io.cote.chatdm.config.ChatDMProperties;
import io.cote.chatdm.dmstore.FileDMStoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class FileDMJournalRepository implements DMJournalRepository {
    private static final Logger logger = LoggerFactory.getLogger(FileDMJournalRepository.class);
    private final String journalPath = "journal/dm-journal.md";

    private final FileDMStoreRepository dmStore;

    public FileDMJournalRepository(FileDMStoreRepository dmStore) {
        this.dmStore = dmStore;
    }

    @Override
    public void addEntry(String entry) throws IOException {
        // If this file does not exist yet, the load() call will create
        // the journal with the beginning template for a DM journal.
        File journalFile = dmStore.load(journalPath);
        String entryMarkdown = "## DM Journal entry for " + ZonedDateTime.now() + "\n\n" + entry + "\n\n";
        Files.writeString(journalFile.toPath(), entryMarkdown, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    /**
     * Returns all Journal entries from the journal file. If there is no file, a default starter will be returned. If an
     * error occurs, an empty string will be returned.
     *
     * @return all the journal entries in the first item in the List. If there are no entries, an empty list is
     * returned.
     */
    @Override
    public String entries() throws IOException {
        // TK I'm not sure if this is a good way to deal
        // with the exception. Should it just throw it?
        File journalFile = dmStore.load(journalPath);
        return Optional.ofNullable(journalFile)
                .filter(File::exists)  // Only proceed if the file exists
                .map(file -> {

                    try {
                        return String.join("\n", Files.readAllLines(file.toPath()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                })
                .orElse("");  // Return empty string if file is not found or doesn't exist
    }

}
