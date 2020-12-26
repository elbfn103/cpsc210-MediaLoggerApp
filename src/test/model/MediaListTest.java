package model;

import exceptions.EntryNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MediaListTest {
    private static final int NUMITEMS = 300;
    private MediaList list;

    @BeforeEach
    void setup() {
        list = new MediaList();
    }

    @Test
    void testConstructor() {
        MediaList testlist = new MediaList();

        assertEquals(0, testlist.size());
    }

    @Test
    void testAddEntryOne() {
        MediaEntry me = new MediaEntry("Harry Potter", "J.K. Rowling", 0, "");

        list.addEntry("Harry Potter", "J.K. Rowling", 0, "");

        assertEquals(1, list.size());
        assertTrue(list.contains(me));
    }

    @Test
    void testAddEntryMany() {
        for (int i = 0; i < NUMITEMS; i++) {
            list.addEntry("Title", "Author", 0, "");
        }

        assertEquals(300, list.size());
    }

// These tests are the non-exception catching versions
    @Test
    void testDeleteEntryDifferentTitle() {
        MediaEntry me = new MediaEntry("Harry Potter", "J.K. Rowling", 0, "");

        list.addEntry("Harry Potter 2", "J.K. Rowling", 0, "");
        list.addEntry("Harry Potter", "J.K. Rowling", 0, "");
        try {
            list.removeEntry(me);
        } catch (EntryNotFoundException e) {
            fail();
        }

        assertEquals(1, list.size());
        assertFalse(list.contains(me));
    }

    @Test
    void testDeleteEntryFalse() {
        MediaEntry me = new MediaEntry("Harry Potter", "J.K. Rowling", 0, "");

        list.addEntry("Harry Potter 2", "J.K. Rowling 2", 0, "");
        try {
            list.removeEntry(me);
            fail();
        } catch (EntryNotFoundException e) {
            System.out.println("Exception expected and thrown");
        }

        assertEquals(1, list.size());
        assertFalse(list.contains(me));
    }

    @Test
    void testDeleteEntryOnlyOne() {
        MediaEntry me = new MediaEntry("Harry Potter", "J.K. Rowling", 0, "");

        list.addEntry("Harry Potter", "J.K. Rowling", 0, "");
        try {
            list.removeEntry(me);
        } catch (EntryNotFoundException e) {
            fail("Exception not expected");
        }

        assertEquals(0, list.size());
    }

    @Test
    void testDeleteEntryNotOnlyOne() {
        MediaEntry me = new MediaEntry("Harry Potter", "J.K. Rowling", 0, "");

        list.addEntry("Harry Potter", "J.K. Rowling", 0, "");
        list.addEntry("War and Peace", "Leo Tolstoy", 0, "");
        list.addEntry("The Lord of the Rings", "J.R.R. Tolkien", 0, "");
        try {
            list.removeEntry(me);
        } catch (EntryNotFoundException e) {
            fail("Exception not expected");
        }

        assertEquals(2, list.size());
        assertFalse(list.contains(me));
    }

    @Test
    void testDeleteEntryMultiple() {
        MediaEntry me1 = new MediaEntry("Harry Potter", "J.K. Rowling", 0, "");
        MediaEntry me2 = new MediaEntry("The Lord of the Rings", "J.R.R. Tolkien", 0, "");
        MediaEntry me3 = new MediaEntry("War and Peace", "Leo Tolstoy", 0, "");

        list.addEntry("Harry Potter", "J.K. Rowling", 0, "");
        list.addEntry("War and Peace", "Leo Tolstoy", 0, "");
        list.addEntry("The Lord of the Rings", "J.R.R. Tolkien", 0, "");
        try {
            list.removeEntry(me1);
            list.removeEntry(me2);
        } catch (EntryNotFoundException e) {
            fail("Exception not expected");
        }

        assertEquals(1, list.size());
        assertTrue(list.contains(me3));
    }

    @Test
    void testContainsDifferentTitle() {
        MediaEntry me = new MediaEntry("Harry Potter", "J.K. Rowling", 0, "");

        list.addEntry("Harry Potter 2", "J.K. Rowling", 0, "");

        assertFalse(list.contains(me));
    }

    @Test
    void testContainsBothDifferent() {
        MediaEntry me = new MediaEntry("Harry Potter", "J.K. Rowling", 0, "");

        list.addEntry("The Lord of the Rings", "J.R.R. Tolkien", 0, "");

        assertFalse(list.contains(me));
    }
    @Test
    void testGetEntryList() {
        MediaEntry me1 = new MediaEntry("Harry Potter", "J.K. Rowling", 0, "");
        MediaEntry me2 = new MediaEntry("The Lord of the Rings", "J.R.R. Tolkien", 0, "");
        List<MediaEntry> entrylist = new ArrayList<>();

        entrylist.add(me1);
        entrylist.add(me2);
        list.addEntry("Harry Potter", "J.K. Rowling", 0, "");
        list.addEntry("The Lord of the Rings", "J.R.R. Tolkien", 0, "");

        List<MediaEntry> getlist = list.getEntryList();

        assertEquals(entrylist.size(), getlist.size());
        assertTrue(entrylist.contains(me1) && list.contains(me1));
        assertTrue(entrylist.contains(me2) && list.contains(me2));
    }

    @Test
    void testFindEntry() {
        MediaEntry me = new MediaEntry("War and Peace", "Leo Tolstoy", 0, "");
        MediaEntry me1 = new MediaEntry("Harry Potter", "J.K. Rowling", 0, "");
        MediaEntry me2 = new MediaEntry("The Lord of the Rings", "J.R.R. Tolkien", 0, "");

        list.addEntry("Harry Potter", "J.K. Rowling", 0, "");
        list.addEntry("The Lord of the Rings", "J.R.R. Tolkien", 0, "");

        try {
            assertEquals(list.findEntry(me1), list.findEntry(me1));
            assertEquals(list.findEntry(me2), list.findEntry(me2));
        } catch (EntryNotFoundException e) {
            fail();
        }
    }

    @Test
    void testFindEntryException() {
        MediaEntry me = new MediaEntry("War and Peace", "Leo Tolstoy", 0, "");
        MediaEntry me1 = new MediaEntry("Harry Potter", "J.K. Rowling", 0, "");
        MediaEntry me2 = new MediaEntry("The Lord of the Rings", "J.R.R. Tolkien", 0, "");

        list.addEntry("Harry Potter", "J.K. Rowling", 0, "");
        list.addEntry("The Lord of the Rings", "J.R.R. Tolkien", 0, "");

        try {
            list.findEntry(me);
            fail();
        } catch (EntryNotFoundException e) {
            System.out.println("Exception expected and thrown");
        }
    }
}
