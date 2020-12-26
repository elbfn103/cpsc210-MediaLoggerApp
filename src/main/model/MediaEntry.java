package model;

/*
 * Represents a media entry (eg. Book, Movie, Song, etc.)
 */

import org.json.JSONObject;
import persistence.Writable;

public class MediaEntry implements Writable {
    private String title;
    private String author;
    private int rating;
    private String comment;

    // creates media entry
    // EFFECTS: creates a media entry with title and author, default rating 0 and no comment
    public MediaEntry(String t, String a, int rating, String comment) {
        title = t;
        author = a;
        this.rating = rating;
        this.comment = comment;
    }

    // changes title
    // MODIFIES: this
    // EFFECTS: changes title of the entry to the given string
    public void changeTitle(String t) {
        title = t;
    }

    // changes author
    // MODIFIES: this
    // EFFECTS: changes author of the entry to the given string
    public void changeAuthor(String a) {
        author = a;
    }

    //
    // adds rating
    // REQUIRES: input integer from 0 to 5
    // MODIFIES: this
    // EFFECTS: adds rating out of 5 to media entry
    public void changeRating(int r) {
        rating = r;
    }

    // adds comment
    // MODIFIES: this
    // EFFECTS: adds comment to media entry
    public void changeComment(String c) {
        comment = c;
    }

    // gets title
    // EFFECTS: gets title of entry
    public String getTitle() {
        return title;
    }

    // gets author
    // EFFECTS: gets author of entry
    public String getAuthor() {
        return author;
    }

    // gets rating
    // EFFECTS: gets rating of entry
    public int getRating() {
        return rating;
    }

    // gets comment
    // EFFECTS: gets comment of entry
    public String getComment() {
        return comment;
    }

    // Code borrowed from JsonSerializationDemo
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("author", author);
        json.put("rating", rating);
        json.put("comment", comment);
        return json;
    }
}
