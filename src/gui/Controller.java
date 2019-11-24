package gui;

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

    public Controller() {

    }

    public Controller(Gui theGui) {
        myGui = theGui;
        myLevel = new Level(5);
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

    public void edit() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Editing Instructions");
        alert.setHeaderText("In the following input box, you may add or remove monsters or treasures");
        TextInputDialog inputDialog = new TextInputDialog("Type here");
        Optional<String> str = inputDialog.showAndWait();
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

            System.out.println("Object has been serialized");
            System.out.println("Chamber 1: " + myLevel.getChamber(0).getDescription());

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

            System.out.println("Object has been deserialized ");
            System.out.println("Chamber 1: " + myLevel.getChamber(0).getDescription());
        } catch(IOException ex) {
            System.out.println("IOException is caught");
        } catch(ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
        }
    }

    public Space getSelectedSpace() {
        Space mySpace;
        String selected = myGui.getSelectedSpaceStr();
//        char c = selected.charAt(0);
//        int num = parseSelectionNum(selected);
//        if (c == 'C') {
//            ArrayList<Chamber> chambers = theController.getLevelChambers();
//            mySpace = chambers.get(num - 1);
//        } else {
//            ArrayList<Passage> passages = theController.getLevelPassages();
//            mySpace = passages.get(num - 1);
//        }
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
