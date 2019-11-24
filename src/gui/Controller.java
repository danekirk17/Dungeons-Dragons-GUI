package gui;

import dnd.die.D20;
import dnd.models.Monster;
import dnd.models.Treasure;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import mydnd.*;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Serializable {

    private Gui myGui;
    private Level myLevel;
    private HashMap<String, Space> spaceMap;
    private HashMap<Space, String> revSpaceMap;

    private D20 d20;

    public Controller() {

    }

    public Controller(Gui theGui) {
        myGui = theGui;
        myLevel = new Level(5);
        d20 = new D20();
        buildSpaceMap();
        buildRevSpaceMap();
    }

    private void buildSpaceMap() {
        spaceMap = new HashMap<>();
        String spaceID;
        ArrayList<Chamber> chambers = getLevelChambers();
        ArrayList<Passage> passages = getLevelPassages();
        for (int i = 0; i < chambers.size(); i++) {
            spaceID = "Chamber " + (i+1);
            spaceMap.put(spaceID, chambers.get(i));
        }
        for (int i = 0; i < passages.size(); i++) {
            spaceID = "Passage " + (i+1);
            spaceMap.put(spaceID, passages.get(i));
        }
    }

    private void buildRevSpaceMap() {
        revSpaceMap = new HashMap<>();
        String spaceID;
        ArrayList<Chamber> chambers = getLevelChambers();
        ArrayList<Passage> passages = getLevelPassages();
        for (int i = 0; i < chambers.size(); i++) {
            spaceID = "Chamber " + (i+1);
            revSpaceMap.put(chambers.get(i), spaceID);
        }
        for (int i = 0; i < passages.size(); i++) {
            spaceID = "Passage " + (i+1);
            revSpaceMap.put(passages.get(i), spaceID);
        }
    }

    public ObservableList<String> getDoorMenuList() {
        Space mySpace = getSelectedSpace();
        ObservableList<String> doorList = FXCollections.observableArrayList();
        for (int i = 0; i < mySpace.getDoors().size(); i++) {
            doorList.add("Door " + (i+1));
        }
        return doorList;
    }

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

    private String getDoorSpaces(Door myDoor) {
        String connections = "\nConnections:\n";
        ArrayList<Space> mySpaces = myDoor.getAllSpaces();
        for (int i = 0; i < mySpaces.size(); i++) {
            connections += "Space " + (i+1) + ": " + revSpaceMap.get(mySpaces.get(i)) + "\n";
        }
        return connections;
    }

    private Alert getDoorAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Door Description");
        return alert;
    }

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
        } catch(IOException ex) {
            System.out.println("IOException is caught");
        }
    }

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
            myLevel = (Level)in.readObject();

            in.close();
            file.close();

            buildSpaceMap();
            buildRevSpaceMap();
            myGui.setDesTAText();
        } catch(IOException ex) {
            System.out.println("IOException is caught");
        } catch(ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
        }
    }

    public void addMonster() {
        Space mySpace = getSelectedSpace();
        Monster m = new Monster();
        m.setType(d20.roll());
        mySpace.addMonster(m);
    }

    public void remMonster() {
        getSelectedSpace().remMonster();
    }

    public void addTreasure() {
        Treasure t = new Treasure();
        t.chooseTreasure(d20.roll() * 5);
        t.setContainer(d20.roll());
        getSelectedSpace().addTreasure(t);
    }

    public void remTreasure() {
        getSelectedSpace().remTreasure();
    }

    public Space getSelectedSpace() {
        Space mySpace;
        String selected = myGui.getSelectedSpaceStr();
        mySpace = spaceMap.get(selected);
        return mySpace;
    }

    public ArrayList<Passage> getLevelPassages() {
        return myLevel.getPassages();
    }

    public ArrayList<Chamber> getLevelChambers() {
        return myLevel.getChambers();
    }

    public HashMap<Chamber, Integer> getChamberNumbers() {
        return myLevel.getChamberNumbers();
    }
}
