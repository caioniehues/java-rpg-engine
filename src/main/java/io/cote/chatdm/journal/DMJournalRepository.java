package io.cote.chatdm.journal;

import java.io.IOException;
import java.util.List;

public interface DMJournalRepository
{
    void addEntry(String entry) throws IOException;
    String entries() throws IOException;
}
