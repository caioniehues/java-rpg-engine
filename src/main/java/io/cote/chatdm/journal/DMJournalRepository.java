package io.cote.chatdm.journal;

import java.io.IOException;
import java.util.List;

public interface DMJournalRepository
{
    /**
     * Adds an entry to the journal. The journal will be in markdown
     * and have a dated header for the entry.
     *
     * @param entry entry text, should be in markdown
     * @throws IOException if an error occurs.
     */
    void addEntry(String entry) throws IOException;

    /**
     * Retrieves all the entries from the journal as a single string.
     * If the journal file does not exist, a default starter content
     * or an empty string may be returned.
     *
     * @return the combined content of all journal entries as a single string.
     *         Returns an empty string if no entries are present or if the file does not exist.
     * @throws IOException if an error occurs while reading the journal file.
     */
    String entries() throws IOException;
}
