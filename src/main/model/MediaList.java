package model;

import exceptions.EntryNotFoundException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
 * Represents a list of media entries
 */

public class MediaList implements Writable {
    private List<MediaEntry> entrylist;

    // creates new media list
    // EFFECTS: creates an empty list of media entries
    public MediaList() {
        entrylist = new ArrayList<>();
    }

    // adds entry to library
    // MODIFIES: this
    // EFFECTS: adds a media entry to the list of entries
    public void addEntry(String t, String a, int r, String c) {
        MediaEntry entry = new MediaEntry(t, a, r, c);
        entrylist.add(entry);
    }

    // deletes entry from library
    // MODIFIES: this
    // EFFECTS: finds the first matching media entry from the list of entries
    // and deletes it, throws EntryNotFoundException if entry not found
    public void removeEntry(MediaEntry me) throws EntryNotFoundException {
        int i = 0;
        for (MediaEntry entry : entrylist) {
            if ((Objects.equals(entry.getTitle(), me.getTitle()))) {
                entrylist.remove(i);
                return;
            }
            i++;
        }
        throw new EntryNotFoundException();
    }

    // finds given entry in library
    // EFFECTS: finds the first matching media entry from list of entries and
    // returns it, otherwise throw EntryNotFoundException
    public MediaEntry findEntry(MediaEntry me) throws EntryNotFoundException {
        for (MediaEntry entry : entrylist) {
            if ((Objects.equals(entry.getTitle(), me.getTitle()))) {
                return entry;
            }
        }
        throw new EntryNotFoundException();
    }


    // gets number of entries in list
    // EFFECTS: returns size of entry list
    public int size() {
        return entrylist.size();
    }

    // gives whether list contains the entry
    // EFFECTS: returns true if list contains the entry, otherwise returns false
    public boolean contains(MediaEntry me) {
        for (MediaEntry entry : entrylist) {
            if ((Objects.equals(entry.getTitle(), me.getTitle()))) {
                return true;
            }
        }
        return false;
    }

    // gets list of entries
    // EFFECTS: returns the list of entries currently in list
    public List<MediaEntry> getEntryList() {
        return entrylist;
    }

    // Code borrowed from JsonSerializationDemo
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("entries", medialistToJson());
        return json;
    }

    // Code borrowed from JsonSerializationDemo
    // EFFECTS: returns things in this MediaList as a JSON array
    private JSONArray medialistToJson() {
        JSONArray jsonArray = new JSONArray();

        for (MediaEntry e : entrylist) {
            jsonArray.put(e.toJson());
        }

        return jsonArray;
    }

}
