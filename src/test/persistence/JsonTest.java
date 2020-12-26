package persistence;

import model.MediaEntry;

import static org.junit.jupiter.api.Assertions.*;

// Code borrowed from JsonSerializationDemo
public class JsonTest {
    protected void checkEntry(String title, String author, int rating, String comment, MediaEntry entry) {
        assertEquals(title, entry.getTitle());
        assertEquals(author, entry.getAuthor());
        assertEquals(rating, entry.getRating());
        assertEquals(comment, entry.getComment());
    }
}
