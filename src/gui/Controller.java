package gui;

import dnd.die.D20;
import dnd.models.Monster;
import dnd.models.Treasure;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import mydnd.Level;
import mydnd.Space;
import mydnd.Chamber;
import mydnd.Passage;
import mydnd.Door;

import java.io.Serializable;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

/**
 * controls the program.
 */
public class Controller implements Serializable {

    /**holds the gui.*/
    private Gui myGui;
    /**holds the dungeon level.*/
    private Level myLevel;
    /**holds a map of all the spaces keyed by their string representations.*/
    private HashMap<String, Space> spaceMap;
    /**holds a map of all string reps keyed by their spaces.*/
    private HashMap<Space, String> revSpaceMap;

    /**holds a D20 for random monster generation.*/
    private D20 d20;

    /**
     * empty constructor.
     */
    public Controller() {

    }

    /**
     * constructor for controller class.
     * @param theGui the gui.
     */
    public Controller(Gui theGui) {
        myGui = theGui;
        myLevel = new Level(5);
        d20 = new D20();
        buildSpaceMap();
        buildRevSpaceMap();
    }

    /**
     * builds space map hash map.
     */
    private void buildSpaceMap() {
        spaceMap = new HashMap<>();
        String spaceID;
        ArrayList<Chamber> chambers = getLevelChambers();
        ArrayList<Passage> passages = getLevelPassages();
        for (int i = 0; i < chambers.size(); i++) {
            spaceID = "Chamber " + (i + 1);
            spaceMap.put(spaceID, chambers.get(i));
        }
        for (int i = 0; i < passages.size(); i++) {
            spaceID = "Passage " + (i + 1);
            spaceMap.put(spaceID, passages.get(i));
        }
    }

    /**
     * builds the reverse space map.
     */
    private void buildRevSpaceMap() {
        revSpaceMap = new HashMap<>();
        String spaceID;
        ArrayList<Chamber> chambers = getLevelChambers();
        ArrayList<Passage> passages = getLevelPassages();
        for (int i = 0; i < chambers.size(); i++) {
            spaceID = "Chamber " + (i + 1);
            revSpaceMap.put(chambers.get(i), spaceID);
        }
        for (int i = 0; i < passages.size(); i++) {
            spaceID = "Passage " + (i + 1);
            revSpaceMap.put(passages.get(i), spaceID);
        }
    }

    /**
     * get a list of doors in the selected space.
     * @return a list of doors in the selected space.
     */
    public ObservableList<String> getDoorMenuList() {
        Space mySpace = getSelectedSpace();
        ObservableList<String> doorList = FXCollections.observableArrayList();
        for (int i = 0; i < mySpace.getDoors().size(); i++) {
            doorList.add("Door " + (i + 1));
        }
        return doorList;
    }

    /**
     * show info dialog for the selected door.
     * @param selected string rep of the selected door.
     */
    public void displayDoorInfo(String selected) {
        String des;
        if (selected != null) {
            Space selectedSpace = getSelectedSpace();
            Door myDoor = selectedSpace.getDoors().get(myGui.parseSelectionNum(selected) - 1);
            des = myDoor.getDescription();
            des += getDoorSpaces(myDoor);
            Alert alert = getDoorAlert();
            alert.setContentText(des);
            alert.showAndWait();
        }
    }

    /**
     * gets a list of spaces the door connects.
     * @param myDoor the door.
     * @return string containing a list of spaces the door connects.
     */
    private String getDoorSpaces(Door myDoor) {
        String connections = "\nConnections:\n";
        ArrayList<Space> mySpaces = myDoor.getAllSpaces();
        for (int i = 0; i < mySpaces.size(); i++) {
            connections += "Space " + (i + 1) + ": " + revSpaceMap.get(mySpaces.get(i)) + "\n";
        }
        return connections;
    }

    /**
     * initialize the door info dialog.
     * @return the door info dialog.
     */
    private Alert getDoorAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Door Description");
        return alert;
    }

    /**
     * write the dungeon level to file.
     */
    public void writeFile() {
        TextInputDialog inputDialog = new TextInputDialog("Filename here");
        inputDialog.setHeaderText("Enter filename to save dungeon level");
        Optional<String> filename = inputDialog.showAndWait();
        // Serialization
        try {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(filename.get());
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(myLevel);

            out.close();
            file.close();
        } catch (IOException ex) {
            System.out.println("IOException is caught");
        }
    }

    /**
     * read a dungeon level from a file.
     */
    public void readFile() {
        TextInputDialog inputDialog = new TextInputDialog("Filename here");
        inputDialog.setHeaderText("Enter filename to load dungeon level");
        Optional<String> filename = inputDialog.showAndWait();
        // Deserialization
        try {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(filename.get());
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            myLevel = (Level) in.readObject();

            in.close();
            file.close();

            buildSpaceMap();
            buildRevSpaceMap();
            myGui.setDesTAText();
        } catch (IOException ex) {
            System.out.println("IOException is caught");
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
        }
    }

    /**
     * adds a monster to the selected space.
     */
    public void addMonster() {
        Space mySpace = getSelectedSpace();
        Monster m = new Monster();
        m.setType(d20.roll());
        mySpace.addMonster(m);
    }

    /**
     * removes a monster from the selected space.
     */
    public void remMonster() {
        getSelectedSpace().remMonster();
    }

    /**
     * add treasure to the selected space.
     */
    public void addTreasure() {
        Treasure t = new Treasure();
        t.chooseTreasure(d20.roll() * 5);
        t.setContainer(d20.roll());
        getSelectedSpace().addTreasure(t);
    }

    /**
     * removes a treasure from the selected space.
     */
    public void remTreasure() {
        getSelectedSpace().remTreasure();
    }

    /**
     * returns the selected space.
     * @return the selected space.
     */
    public Space getSelectedSpace() {
        Space mySpace;
        String selected = myGui.getSelectedSpaceStr();
        mySpace = spaceMap.get(selected);
        return mySpace;
    }

    /**
     * gets the list of passages in the dungeon level.
     * @return the list of passages in the dungeon level.
     */
    public ArrayList<Passage> getLevelPassages() {
        return myLevel.getPassages();
    }

    /**
     * gets the list of chambers in the dungeon level.
     * @return the list of chambers in the dungeon level.
     */
    public ArrayList<Chamber> getLevelChambers() {
        return myLevel.getChambers();
    }
}
