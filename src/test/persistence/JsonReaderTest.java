package persistence;

import model.MediaEntry;
import model.MediaList;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;
import persistence.JsonTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Code borrowed from JsonSerializationDemo
public class JsonReaderTest extends JsonTest {
    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            MediaList ml = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyMediaList() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyMediaList.json");
        try {
            MediaList ml = reader.read();
            assertEquals(0, ml.size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralMediaList() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralMediaList.json");
        try {
            MediaList ml = reader.read();
            List<MediaEntry> entries = ml.getEntryList();
            assertEquals(2, ml.size());
            checkEntry("Title 1", "Author 1", 1, "Comment 1", entries.get(0));
            checkEntry("Title 2", "Author 2", 2, "Comment 2", entries.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

}
