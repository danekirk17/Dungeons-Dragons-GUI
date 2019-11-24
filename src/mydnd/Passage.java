package mydnd;

import dnd.models.Monster;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
/*
A passage begins at a door and ends at a door.  It may have many other doors along
the way. You will need to keep track of which door is the "beginning" of the passage
so that you know how to.
*/
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
     * @param i the index of the passage section to return the monster from.
     * @return the monster at the selected section.
     */
    public Monster getMonster(int i) {
        //returns Monster door in section 'i'. If there is no Monster, returns null
        return thePassage.get(i).getMonster();
    }

    /**
     * Adds a monster to a selected passage section.
     * @param theMonster the monster to be added.
     * @param i the index of the passage section to add the monster to.
     */
    public void addMonster(Monster theMonster, int i) {
        // adds a monster to section 'i' of the passage
        thePassage.get(i).addMonster(theMonster);
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
