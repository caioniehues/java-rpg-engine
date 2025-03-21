package io.cote.chatdm.dmstore;

import java.io.InputStream;
import java.io.IOException;
import java.util.List;

public interface DMStoreRepository {
    InputStream loadResource(String name) throws IOException;
    boolean exists(String name);
    void saveResource(String name, InputStream data) throws IOException;
    List<String> listResources();

}