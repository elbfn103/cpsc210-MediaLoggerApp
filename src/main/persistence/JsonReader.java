package persistence;

import model.MediaEntry;
import model.MediaList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// code borrowed from JsonSerializationDemo
// Represents a reader that reads library from JSON data stored in file
public class JsonReader {

    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads library from file and returns it;
    // throws IOException if an error occurs reading data from file
    public MediaList read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseMediaList(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses MediaList from JSON object and returns it
    private MediaList parseMediaList(JSONObject jsonObject) {
        MediaList ml = new MediaList();
        addEntries(ml, jsonObject);
        return ml;
    }

    // MODIFIES: ml
    // EFFECTS: parses entries from JSON object and adds them to MediaList
    private void addEntries(MediaList ml, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("entries");
        for (Object json : jsonArray) {
            JSONObject nextEntry = (JSONObject) json;
            addEntry(ml, nextEntry);
        }
    }

    // MODIFIES: ml
    // EFFECTS: parses entry from JSON object and adds it to MediaList
    private void addEntry(MediaList ml, JSONObject jsonObject) {
        String title = jsonObject.getString("title");
        String author = jsonObject.getString("author");
        int rating = jsonObject.getInt("rating");
        String comment = jsonObject.getString("comment");
        ml.addEntry(title, author, rating, comment);
    }
}
