package io.cote.chatdm.journal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Repository
public class FileDMJournalRepository implements DMJournalRepository {
    private static final Logger logger = LoggerFactory.getLogger(FileDMJournalRepository.class);

    @Value("${chatdm.dir}")
    private String chatdmDir;

    private final String journalFileName = "dm-journal.md";

    @Override
    public void saveEntry(String entry) {
        Path journalFile = Path.of(chatdmDir, journalFileName);
        try {
            if (!Files.exists(journalFile)) {
                try (var templateStream = getClass().getResourceAsStream("/journal/dmjournal-template.md")) {
                    if (templateStream != null) {
                        Files.copy(templateStream, journalFile);
                        logger.info("Created new journal file from template at {}", journalFile);
                    } else {
                        logger.warn("Template journal file not found in resources. New journal file is empty.");
                        Files.createFile(journalFile);
                    }
                }
            }
            Files.writeString(journalFile, entry, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception e) {
            // TK put in handling to save a tmp file somewhere if possible.
            logger.error("Failed to save journal entry {}", journalFile, e);
        }
    }

    /**
     * Returns all Journal entries from the journal file.
     * @return all the journal entries in the first item in the List. If there are no entries, an empty list is returned.
     */
    @Override
    public List<String> readEntries() {
        // TK need to change this so that the list is actually one journal entry per String. Probably need a record for a journal
        // entry.
        Path journalFile = Path.of(chatdmDir, journalFileName);
        try {
            if (Files.exists(journalFile)) {
                return Files.readAllLines(journalFile);
            }
            else {
                logger.debug("No journal file found at {}", journalFile);
            }
        } catch (Exception e) {
            logger.error("Failed to read journal entries from "+journalFile, e);
        }
        // At this point, there are no entries, so we return an empty list.
        return List.of();
    }

    /**
     * Shows the directory being used along with class information.
     * @return description of this {@link DMJournalRepository}, including class information.
     */
    public String toString() {
        return String.format(
                "FileDMJournalRepository{chatdmDir='%s', journalFileName='%s'}",
                chatdmDir, journalFileName);
    }
}
