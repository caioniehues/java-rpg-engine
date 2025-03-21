package io.cote.chatdm.journal;

import java.util.List;

public interface DMJournalRepository
{
    void saveEntry(String entry);
    List<String> findAllEntries();
}
