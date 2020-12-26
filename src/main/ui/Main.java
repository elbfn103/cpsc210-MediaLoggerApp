package ui;

import exceptions.EntryNotFoundException;
import model.MediaEntry;
import model.MediaList;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.event.*;

// A lot of code borrowed from ListDemoProject
public class Main extends JPanel
                        implements ListSelectionListener {
    private static JFrame frame;
    private static JList list;
    private static DefaultListModel listModel;
    private static MediaList medialist;

    private JButton removeButton;
    private JTextField entryTitle;
    private JButton saveButton;
    private JButton loadButton;

    private static JMenuBar editMenuBar;
    private static JMenu editMenu = new JMenu("Edit");
    private static JMenuItem editTitle = new JMenuItem("Title");
    private static JMenuItem editAuthor = new JMenuItem("Author");
    private static JMenuItem editRating = new JMenuItem("Rating");
    private static JMenuItem editComment = new JMenuItem("Comment");

    private static final String JSON_STORE = "./data/library.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;


    // EFFECTS: Constructor for app that sets up necessary components
    public Main() {
        super(new BorderLayout());

        setup();

        //Create the list and put it in a scroll pane.
        setupList();
        JScrollPane listScrollPane = new JScrollPane(list);

        JButton addButton = new JButton("Add");
        AddListener addListener = setupAddListener(addButton);

        setupButtons();

        entryTitle = new JTextField(10);
        entryTitle.addActionListener(addListener);
        entryTitle.getDocument().addDocumentListener(addListener);

        setupPanel(listScrollPane, addButton);
    }

    // MODIFIES: this
    // EFFECTS: sets up required JList
    // Helper method to setup lists for constructor
    private void setupList() {
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.addMouseListener(new DisplayInformation());
        list.setVisibleRowCount(5);
    }

    // EFFECTS: sets up JPanel for buttons
    private void setupPanel(JScrollPane listScrollPane, JButton addButton) {
        //Create a panel that uses BoxLayout.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                BoxLayout.LINE_AXIS));
        buttonPane.add(removeButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(entryTitle);
        buttonPane.add(addButton);
        buttonPane.add(saveButton);
        buttonPane.add(loadButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
    }

    // MODIFIES: this
    // EFFECTS: creates require buttons and sets commands/listeners
    private void setupButtons() {
        removeButton = new JButton("Remove");
        removeButton.setActionCommand("Remove");
        removeButton.addActionListener(new RemoveListener());

        saveButton = new JButton("Save");
        saveButton.setActionCommand("Save");
        saveButton.addActionListener(new SaveListener());

        loadButton = new JButton("Load");
        loadButton.setActionCommand("Load");
        loadButton.addActionListener(new LoadListener());
    }

    // MODIFIES: this
    // EFFECTS: sets up required editing menu with listeners
    private static void setupEditMenu() {
        editMenuBar = new JMenuBar();
        editMenu = new JMenu("Edit");
        editTitle = new JMenuItem("Title");
        editAuthor = new JMenuItem("Author");
        editRating = new JMenuItem("Rating");
        editComment = new JMenuItem("Comment");

        editTitle.addActionListener(new EditTitleListener());
        editAuthor.addActionListener(new EditAuthorListener());
        editRating.addActionListener(new EditRatingListener());
        editComment.addActionListener(new EditCommentListener());

        editMenu.add(editTitle);
        editMenu.add(editAuthor);
        editMenu.add(editRating);
        editMenu.add(editComment);

        editMenuBar.add(editMenu);
    }

    // MODIFIES: this
    // EFFECTS: sets up required fields
    private void setup() {
        listModel = new DefaultListModel();
        medialist = new MediaList();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // EFFECTS: sets up listener for add button
    private AddListener setupAddListener(JButton addButton) {
        AddListener addListener = new AddListener(addButton);
        addButton.setActionCommand("Add");
        addButton.addActionListener(addListener);
        addButton.setEnabled(false);
        return addListener;
    }

    // Event for displaying information of entry on double-click
    static class DisplayInformation extends MouseAdapter {

        // EFFECTS: Shows pop-up dialog on double-click of an entry with name, author,
        //          rating, comment and a small icon
        @Override
        public void mouseClicked(MouseEvent e)  {
            JList list = (JList)e.getSource();
            if (e.getClickCount() == 2) {
                int index = list.locationToIndex(e.getPoint());

                String name = listModel.get(index).toString();
                MediaEntry entry = new MediaEntry(name, "", 0, "");
                MediaEntry selected = null;
                try {
                    selected = medialist.findEntry(entry);
                } catch (EntryNotFoundException e2) {
                    System.out.println("Entry not found");
                }

                setupInfo(selected);

                playSound("./data/insight.wav");
            }
        }

        private void setupInfo(MediaEntry selected) {
            JDialog info = new JDialog(frame, "Media Information");

            JLabel infoLabel = new JLabel("<html>Title: " + selected.getTitle()
                    + "<br/>Author: " + selected.getAuthor()
                    + "<br/>Rating: " + selected.getRating()
                    + "<br/>Comment: " + selected.getComment() + "</html>");
            infoLabel.setIcon(new ImageIcon("./data/music.png"));
            infoLabel.setHorizontalTextPosition(JLabel.LEFT);
            infoLabel.setVerticalTextPosition(JLabel.CENTER);

            info.add(infoLabel);

            info.setSize(300, 300);
            info.setLocationRelativeTo(null);
            info.setVisible(true);
        }
    }

    // Event for removing selected entry (for remove button)
    class RemoveListener implements ActionListener {

        // MODIFIES: this
        // EFFECTS: removes selected entry
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = list.getSelectedIndex();
            String name = listModel.get(index).toString();
            listModel.remove(index);

            MediaEntry entry = new MediaEntry(name, "", 0, "");
            try {
                medialist.removeEntry(entry);
            } catch (EntryNotFoundException e2) {
                Toolkit.getDefaultToolkit().beep();
            }

            int size = listModel.getSize();

            if (size == 0) { //Nothing's left, disable removing
                removeButton.setEnabled(false);

            } else { //Select an index.
                if (index == listModel.getSize()) {
                    //removed item in last position
                    index--;
                }

                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
            }
        }
    }

    // Event for adding an entry (for add button)
    class AddListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;

        public AddListener(JButton button) {
            this.button = button;
        }

        // MODIFIES: this
        // EFFECTS: adds an entry with title given by user input in box
        public void actionPerformed(ActionEvent e) {
            String name = entryTitle.getText();

            //User didn't type in a unique name...
            if (notUniqueName(name)) {
                return;
            }

            //Adding new media entry with given name
            medialist.addEntry(name, "", 0, "");

            int index = list.getSelectedIndex(); //get selected index
            if (index == -1) { //no selection, so insert at beginning
                index = 0;
            } else {           //add after the selected item
                index++;
            }

            //Adds entry to display list
            MediaEntry entry = new MediaEntry(name, "", 0, "");
            try {
                listModel.addElement(medialist.findEntry(entry).getTitle());
            } catch (EntryNotFoundException e2) {
                System.out.println("Entry not found");
            }

            //Reset the text field.
            entryTitle.requestFocusInWindow();
            entryTitle.setText("");

            //Select the new item and make it visible.
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
        }

        private boolean notUniqueName(String name) {
            if (name.equals("") || alreadyInList(name)) {
                Toolkit.getDefaultToolkit().beep();
                entryTitle.requestFocusInWindow();
                entryTitle.selectAll();
                return true;
            }
            return false;
        }

        // EFFECTS: tests for string equality, true if display list already contains name,
        //          false otherwise
        protected boolean alreadyInList(String name) {
            return listModel.contains(name);
        }

        //Required by DocumentListener.
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        //Required by DocumentListener.
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        //Required by DocumentListener.
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        // MODIFIES: this
        // EFFECTS: enables button
        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }

        // MODIFIES: this
        // EFFECTS: if text field empty, disables button
        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
    }

    // Event for saving list/library (for save button)
    class SaveListener implements ActionListener {

        // MODIFIES: this
        // EFFECTS: saves current media list to file
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                jsonWriter.open();
                jsonWriter.write(medialist);
                jsonWriter.close();
                System.out.println("Saved " + " to " + JSON_STORE);
            } catch (FileNotFoundException ex) {
                System.out.println("Unable to write to file: " + JSON_STORE);
            }
        }
    }

    // Event for loading list/library (for load button)
    class LoadListener implements ActionListener {

        // MODIFIES: this
        // EFFECTS: loads media list from file
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                medialist = jsonReader.read();
                System.out.println("Loaded " + " from " + JSON_STORE);
                listModel.clear();
                for (MediaEntry me : medialist.getEntryList()) {
                    listModel.addElement(me.getTitle());
                }
            } catch (IOException ex) {
                System.out.println("Unable to read from file: " + JSON_STORE);
            }
        }
    }

    // Event for editing title of an entry (for edit menu)
    static class EditTitleListener implements ActionListener {

        // MODIFIES: this
        // EFFECTS: changes title of selected entry
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = list.getSelectedIndex();

            String name = listModel.get(index).toString();
            MediaEntry entry = new MediaEntry(name, "", 0, "");

            String newName = JOptionPane.showInputDialog("New Title: ");

            try {
                medialist.findEntry(entry).changeTitle(newName);
            } catch (EntryNotFoundException e2) {
                System.out.println("Entry not found");
            }
            listModel.remove(index);
            listModel.insertElementAt(newName, index);
        }
    }

    // Event for editing author of an entry (for edit menu)
    static class EditAuthorListener implements ActionListener {

        // MODIFIES: this
        // EFFECTS: changes author of selected entry
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = list.getSelectedIndex();

            String name = listModel.get(index).toString();
            MediaEntry entry = new MediaEntry(name, "", 0, "");

            String newName = JOptionPane.showInputDialog("New Author: ");

            try {
                medialist.findEntry(entry).changeAuthor(newName);
            } catch (EntryNotFoundException e2) {
                System.out.println("Entry not found");
            }
            listModel.remove(index);
            listModel.insertElementAt(name, index);
        }
    }

    // Event for editing rating of an entry (for edit menu)
    static class EditRatingListener implements ActionListener {

        // MODIFIES: this
        // EFFECTS: changes rating of selected entry
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = list.getSelectedIndex();

            String name = listModel.get(index).toString();
            MediaEntry entry = new MediaEntry(name, "", 0, "");

            String newRating = JOptionPane.showInputDialog("New Rating: ");
            int rating = Integer.parseInt(newRating);

            try {
                medialist.findEntry(entry).changeRating(rating);
            } catch (EntryNotFoundException e2) {
                System.out.println("Entry not found");
            }
            listModel.remove(index);
            listModel.insertElementAt(name, index);
        }
    }

    // Event for editing comment of an entry (for edit menu)
    static class EditCommentListener implements ActionListener {

        // MODIFIES: this
        // EFFECTS: changes comment of selected entry
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = list.getSelectedIndex();

            String name = listModel.get(index).toString();
            MediaEntry entry = new MediaEntry(name, "", 0, "");

            String newComment = JOptionPane.showInputDialog("New Comment: ");

            try {
                medialist.findEntry(entry).changeComment(newComment);
            } catch (EntryNotFoundException e2) {
                System.out.println("Entry not found");
            }
            listModel.remove(index);
            listModel.insertElementAt(name, index);
        }
    }

    // MODIFIES: this
    // EFFECTS: disables specific buttons when no selection, enables when selected
    // This method is required by ListSelectionListener.
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {

            if (list.getSelectedIndex() == -1) {
                //No selection, disable fire button.
                removeButton.setEnabled(false);
                editMenu.setEnabled(false);

            } else {
                //Selection, enable the fire button.
                removeButton.setEnabled(true);
                editMenu.setEnabled(true);
            }
        }
    }

    // EFFECTS: Plays sound file of given filename
    public static void playSound(String soundName) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    // MODIFIES: this
    // EFFECTS: sets up required JFrame with components and makes it visible
    //          (with startup sound)
    public static void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("LibraryApp");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupEditMenu();
        frame.setJMenuBar(editMenuBar);

        //Create and set up the content pane.
        JComponent newContentPane = new Main();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setSize(750, 750);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        playSound("./data/doneforyou.wav");
    }

    // EFFECTS: runs GUI
    public static void main(String[] args) {

        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Main.createAndShowGUI();
            }
        });
    }
}
