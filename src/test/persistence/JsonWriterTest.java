package persistence;

import model.MediaEntry;
import model.MediaList;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;
import persistence.JsonTest;
import persistence.JsonWriter;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Code borrowed from JsonSerializationDemo
public class JsonWriterTest extends JsonTest {
    @Test
    void testWriterInvalidFile() {
        try {
            MediaList ml = new MediaList();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyMediaList() {
        try {
            MediaList ml = new MediaList();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyMediaList.json");
            writer.open();
            writer.write(ml);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyMediaList.json");
            ml = reader.read();
            assertEquals(0, ml.size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralMediaList() {
        try {
            MediaList ml = new MediaList();
            ml.addEntry("Title 1", "Author 1", 1, "Comment 1");
            ml.addEntry("Title 2", "Author 2", 2, "Comment 2");
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralMediaList.json");
            writer.open();
            writer.write(ml);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralMediaList.json");
            ml = reader.read();
            List<MediaEntry> entries = ml.getEntryList();
            assertEquals(2, entries.size());
            checkEntry("Title 1", "Author 1", 1, "Comment 1", entries.get(0));
            checkEntry("Title 2", "Author 2", 2, "Comment 2", entries.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
