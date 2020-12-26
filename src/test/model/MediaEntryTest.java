package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MediaEntryTest {
    private MediaEntry entry;

    @BeforeEach
    void setup(){
        entry = new MediaEntry("", "", 0, "");
    }

    @Test
    void testConstructor() {
        MediaEntry testentry = new MediaEntry("Harry Potter", "J.K. Rowling", 0, "");

        assertEquals("Harry Potter", testentry.getTitle());
        assertEquals("J.K. Rowling", testentry.getAuthor());
    }

    @Test
    void testChangeTitleEmpty() {
        entry.changeTitle("Harry Potter");

        assertEquals("Harry Potter", entry.getTitle());
    }

    @Test
    void testChangeTitleNotEmpty() {
        entry.changeTitle("Harry Potter");
        entry.changeTitle("War and Peace");

        assertEquals("War and Peace", entry.getTitle());
    }

    @Test
    void testChangeAuthorEmpty() {
        entry.changeAuthor("J.K. Rowling");

        assertEquals("J.K. Rowling", entry.getAuthor());
    }

    @Test
    void testChangeAuthorNotEmpty() {
        entry.changeAuthor("J.K. Rowling");
        entry.changeAuthor("Charles Dickens");

        assertEquals("Charles Dickens", entry.getAuthor());
    }

    @Test
    void testChangeRating() {
        entry.changeRating(3);

        assertEquals(3, entry.getRating());
    }

    @Test
    void testAddComment() {
        entry.changeComment("This is really interesting!");

        assertEquals("This is really interesting!", entry.getComment());
    }

    @Test
    void testAddCommentNotEmpty() {
        entry.changeComment("This is really interesting!");
        entry.changeComment("This is pretty boring.");

        assertEquals("This is pretty boring.", entry.getComment());
    }

}
