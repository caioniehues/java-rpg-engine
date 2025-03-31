package io.cote.chatdm.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "chatdm.dir=/tmp/.chatdm",
    "chatdm.dirs=oracle,journal,dc"
})
class ChatDMPropertiesTest {

    @Autowired
    private ChatDMProperties properties;

    @Test
    void testDirectoriesAreCorrectlyResolvedAgainstRoot() {
        // Test the root directory path
        Path expectedRootPath = Path.of("/tmp/.chatdm");
        assertEquals(expectedRootPath, properties.getChatDMDirPath());

        
        // Test that each directory path is correctly resolved against the root
        List<Path> dirsPaths = properties.getDirs();
        assertNotNull(dirsPaths);
        assertEquals(3, dirsPaths.size());
        
        assertEquals(Path.of("/tmp/.chatdm/oracle"), dirsPaths.get(0));
        assertEquals(Path.of("/tmp/.chatdm/journal"), dirsPaths.get(1));
        assertEquals(Path.of("/tmp/.chatdm/dc"), dirsPaths.get(2));
    }

}