package mydnd;

import dnd.models.Monster;
import dnd.models.Treasure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Creates a Passage. Passages are made up of passage sections.
 */
public class Passage extends Space implements Serializable {

    /**holds all sections in the passage.*/
    private ArrayList<PassageSection> thePassage;
    /**holds all doors in the passage and their associated sections.*/
    private HashMap<Door, PassageSection> doorMap;
    /**holds the first and last door.*/
    private ArrayList<Door> myDoors;
    /**holds a list of the monsters in the passage.*/
    private ArrayList<Monster> myMonsters;
    /**holds a description of the passage.*/
    private String description;

    /**
     * Creates an object with the Passage class.
     */
    public Passage() {
        thePassage = new ArrayList<>();
        doorMap = new HashMap<>();
        myDoors = new ArrayList<>();
    }

    /**
     * Accesses all the doors in the Passage.
     * @return a list of all the doors in the passage.
     */
    public ArrayList<Door> getDoors() {
        //gets all of the doors in the entire passage
        Set<Door> dSet = doorMap.keySet();
        ArrayList<Door> doors = new ArrayList<>(dSet);
        return myDoors;
    }

    /**
     * Accessor for a selected door in the Passage.
     * @param i the index of the selected door.
     * @return the selected door.
     */
    public Door getDoor(int i) {
        return myDoors.get(i);
    }

    /**
     * Accessor for the monster in a specific passage section.
     * @return the monster at the selected section.
     */
    public ArrayList<Monster> getMonsters() {
        return thePassage.get(0).getMonsters();
    }

    /**
     * Adds a monster to a selected passage section.
     * @param theMonster the monster to be added.
     */
    public void addMonster(Monster theMonster) {
        thePassage.get(0).addMonster(theMonster);
    }

    /**
     * removes a monster from the passage.
     */
    public void remMonster() {
        thePassage.get(0).remMonster();
    }

    /**
     * adds a treasure to the passage.
     * @param theTreasure the treasure to add.
     */
    public void addTreasure(Treasure theTreasure) {
        thePassage.get(0).addTreasure(theTreasure);
    }

    /**
     * removes a treasure from the passage.
     */
    public void remTreasure() {
        thePassage.get(0).remTreasure();
    }

    /**
     * Adds a section to the passage.
     * @param toAdd the section to add.
     */
    public void addPassageSection(PassageSection toAdd) {
        //adds the passage section to the passageway
        thePassage.add(toAdd);      //adds the section to the arraylist of sections
    }

    /**
     * adds a door to the passage.
     * @param newDoor the door to be added.
     */
    public void setDoor(Door newDoor) {
        //should add a door connection to the current Passage Section
        myDoors.add(newDoor);
        if (thePassage.size() != 0) {
            doorMap.put(newDoor, thePassage.get(thePassage.size() - 1));    //now assuming the "current" section is the most recent one
        }
    }

    /**
     * Accessor for the description of the Passage.
     * @return a String with a description of the passage.
     */
    @Override
    public String getDescription() {
        description = "";
        for (int i = 0; i < thePassage.size(); i++) {
            description += thePassage.get(i).getDescription();
        }
        return description;
    }

    /**
     * returns the arraylist of sections.
     * @return the arraylist of sections in the passage.
     */
    public ArrayList<PassageSection> getSections() {
        return thePassage;
    }
}
