package ui;

import model.MediaEntry;
import model.MediaList;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

// A lot of code is borrowed from TellerApp
public class LibraryConsoleApp {
    private static final String JSON_STORE = "./data/library.json";
    private MediaList lib;
    private Scanner input;
    private String user;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs the library application
    public LibraryConsoleApp() {
        runLibrary();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runLibrary() {
        boolean keepRunning = true;
        String command;

        setup();

        while (keepRunning) {
            displayMainMenu();
            command = input.next();
            command = command.toLowerCase();
            input.nextLine();

            if (command.equals("exit")) {
                keepRunning = false;
            } else {
                processMainCommand(command);
            }
        }

        System.out.println("\nShutting down...");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processMainCommand(String command) {
        if (command.equals("add")) {
            doAdd();
        } else if (command.equals("remove")) {
            doRemove();
        } else if (command.equals("edit")) {
            doEdit();
        } else if (command.equals("view")) {
            printLibrary(lib);
        } else if (command.equals("save")) {
            saveLibrary();
        } else if (command.equals("load")) {
            loadLibrary();
        } else {
            System.out.println("Invalid input...");
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes media lists
    private void setup() {
        lib = new MediaList();
        input = new Scanner(System.in);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        System.out.println("\nGreetings new user! Thanks for choosing to use this program.");
        System.out.println("\nTo get started, please enter your username: ");

        user = input.next();
    }

    // EFFECTS: displays menu of options to user
    private void displayMainMenu() {
        System.out.println("\nWelcome, " + user);
        System.out.println("\tadd - add entry");
        System.out.println("\tremove - remove entry");
        System.out.println("\tedit - edit entries");
        System.out.println("\tview - view library");
        System.out.println("\tsave - save library to file");
        System.out.println("\tload - load library from file");
        System.out.println("\texit - quit app");
    }

    // MODIFIES: this
    // EFFECTS: adds an entry to library
    private void doAdd() {
        System.out.println("Enter title of entry: ");
        String title = input.nextLine();
        System.out.println("Enter author of entry: ");
        String author = input.nextLine();

        lib.addEntry(title, author, 0, "");

        printLibrary(lib);
    }

    // MODIFIES: this
    // EFFECTS: removes an entry from library
    private void doRemove() {
        System.out.println("Enter title of entry: ");
        String title = input.nextLine();
        System.out.println("Enter author of entry: ");
        String author = input.nextLine();

        MediaEntry entry = new MediaEntry(title, author, 0, "");

//        if (!lib.removeEntry(entry)) {
//            System.out.println("Invalid input, entry does not exist...");
//        }

        printLibrary(lib);
    }

    // MODIFIES: this
    // EFFECTS: changes title, author, rating, and/or comment of selected entry
    private void doEdit() {
        MediaEntry selected = selectEntry();
        boolean keepRunning = true;
        String command;

        while (keepRunning) {
            displayEditMenu();
            command = input.next();
            command = command.toLowerCase();
            input.nextLine();

            if (command.equals("exit")) {
                keepRunning = false;
            } else {
                processEditCommand(command, selected);
            }
        }
    }

    // MODIFIES: selected entry
    // EFFECTS: processes user command for editor
    private void processEditCommand(String command, MediaEntry entry) {
        if (command.equals("t")) {
            doEditTitle(entry);
        } else if (command.equals("a")) {
            doEditAuthor(entry);
        } else if (command.equals("r")) {
            doEditRating(entry);
        } else if (command.equals("c")) {
            doEditComment(entry);
        } else {
            System.out.println("Invalid input...");
        }
    }

    // EFFECTS: displays menu of editing options to user
    private void displayEditMenu() {
        System.out.println("\nEditor: ");
        System.out.println("\tt - edit title");
        System.out.println("\ta - edit author");
        System.out.println("\tr - edit rating");
        System.out.println("\tc - edit comment");
        System.out.println("\texit - quit editor");
    }

    // MODIFIES: selected entry
    // EFFECTS: changes title of selected entry
    private void doEditTitle(MediaEntry entry) {
        System.out.println("\nCurrent title: " + entry.getTitle());
        System.out.println("Enter new title: ");

        entry.changeTitle(input.nextLine());
    }

    // MODIFIES: selected entry
    // EFFECTS: changes author of selected entry
    private void doEditAuthor(MediaEntry entry) {
        System.out.println("\nCurrent author: " + entry.getAuthor());
        System.out.println("Enter new author: ");

        entry.changeAuthor(input.nextLine());
    }

    // MODIFIES: selected entry
    // EFFECTS: changes rating of selected entry
    private void doEditRating(MediaEntry entry) {
        System.out.println("\nCurrent rating: " + entry.getRating());
        System.out.println("Enter new rating: ");

        entry.changeRating(input.nextInt());
    }

    // MODIFIES: selected entry
    // EFFECTS: changes comment of selected entry
    private void doEditComment(MediaEntry entry) {
        System.out.println("\nCurrent comment: " + entry.getComment());
        System.out.println("Enter new comment: ");

        entry.changeComment(input.nextLine());
    }

    // EFFECTS: prompts user to search for their desired entry in their library and returns it
    private MediaEntry selectEntry() {
        System.out.println("Enter title of entry: ");
        String title = input.nextLine();
        System.out.println("Enter author of entry: ");
        String author = input.nextLine();

        MediaEntry me = new MediaEntry(title, author, 0, "");
        ArrayList<MediaEntry> entrylist = new ArrayList(lib.getEntryList());

        int i = 0;
        for (MediaEntry entry : entrylist) {
            if ((Objects.equals(entry.getTitle(), me.getTitle()))
                    && (Objects.equals(entry.getAuthor(), me.getAuthor()))) {
                break;
            }
            i++;
        }
        return entrylist.get(i);
    }

    // EFFECTS: prints out view of whole library
    private void printLibrary(MediaList library) {
        for (MediaEntry entry : library.getEntryList()) {
            System.out.println("\nTitle: " + entry.getTitle());
            System.out.println("\tAuthor: " + entry.getAuthor());
            System.out.println("\tRating: " + entry.getRating() + " stars out of 5");
            System.out.println("\tComments: " + entry.getComment());
            System.out.println("\n");
        }
    }

    // Code borrowed from JsonSerializationDemo
    // EFFECTS: saves the library to file
    private void saveLibrary() {
        try {
            jsonWriter.open();
            jsonWriter.write(lib);
            jsonWriter.close();
            System.out.println("Saved " + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // Code borrowed from JsonSerializationDemo
    // MODIFIES: this
    // EFFECTS: loads library from file
    private void loadLibrary() {
        try {
            lib = jsonReader.read();
            System.out.println("Loaded " + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}
