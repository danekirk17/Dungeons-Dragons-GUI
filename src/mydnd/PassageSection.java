package mydnd;

import dnd.die.D20;
import dnd.models.Monster;
import dnd.models.Treasure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/* Represents a 10 ft section of passageway */

/**
 * Creates a passage section. Sections make up passages.
 */
public class PassageSection implements Serializable {

    /**holds a D20 for random generation.*/
    private D20 d20;
    /**holds the door in the section.*/
    private Door myDoor;
    /**holds the monsters in the section.*/
    private ArrayList<Monster> myMonsters;
    /**holds the treasures in the section*/
    private ArrayList<Treasure> myTreasures;
    /**holds the chamber in the section.*/
    private Chamber myChamber;
    /**holds the given table for generating sections randomly.*/
    private HashMap<Integer, String> table;
    /**true if passage is a dead end.*/
    private boolean isEnd;
    /**
     * true if passage section ends in a chamber.
     */
    private boolean isChamber;
    /**holds description of the section.*/
    private String description;

    /**
     * Creates an object from the PassageSection class.
     */
    public PassageSection() {
        //sets up the 10 foot section with default settings
        d20 = new D20();
        myMonsters = new ArrayList<>();
        myTreasures = new ArrayList<>();
        int roll;
        initTable();
        description = "";
        roll = d20.roll();
        isEnd = false;
        setupSection(roll);
    }

    /**
     * Creates an object from the PassageSection class using a String description.
     * @param theDescription the string description.
     */
    public PassageSection(String theDescription) {
        //sets up a specific passage based on the values sent in from
        //modified table 1
        int roll = 0;
        myMonsters = new ArrayList<>();
        myTreasures = new ArrayList<>();
        isEnd = false;
        initTable();
        description = theDescription;
        if (table.containsValue(description)) {
            for (int i = 1; i <= 20; i++) {
                if (description.equals(table.get(i))) {
                    roll = i;
                    description = "";
                }
            }
        }
        setupSection(roll);
    }

    /**
     * Accessor for the door in the section.
     * @return the door in the section.
     */
    public Door getDoor() {
        //returns the door that is in the passage section, if there is one
        return myDoor;
    }

    /**
     * Accessor for the Monster in the section.
     * @return the monster in the section.
     */
    public ArrayList<Monster> getMonsters() {
        //returns the monster that is in the passage section, if there is one
        return myMonsters;
    }

    /**
     * Accessor for the chamber attached to the section.
     * @return the chamber attached to the section.
     */
    public Chamber getChamber() {
        return myChamber;
    }

    /**
     * Adds a monster to the passage section.
     * @param theMonster the monster to add to the section.
     */
    public void addMonster(Monster theMonster) {
        myMonsters.add(theMonster);
        setupSection(20);
    }

    public void remMonster() {
        if (myMonsters.size() > 0) {
            int ind = myMonsters.size() - 1;
            myMonsters.remove(ind);
        }
        setupSection(20);
    }

    public void addTreasure(Treasure theTreasure) {
        myTreasures.add(theTreasure);
        setupSection(20);
    }

    public void remTreasure() {
        if (myTreasures.size() > 0) {
            int ind = myTreasures.size() - 1;
            myTreasures.remove(ind);
        }
        setupSection(20);
    }

    /**
     * Accessor for the description of the passage section.
     * @return a String with the description of the passage section.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Accessor for if the section is at the end of a passage.
     * @return true if the section is at the end of a passage.
     */
    public boolean isEnd() {
        return isEnd;
    }

    /**
     * accessing whether or not section ends in a chamber.
     * @return true if section ends in a chamber.
     */
    public boolean isChamber() {
        return isChamber;
    }

    /**
     * fills the hashtable with data used to generate descriptions of the section.
     */
    private void initTable() {
        table = new HashMap<>();
        table.put(1, "passage goes straight for 10 ft");
        table.put(2, "passage goes straight for 10 ft");
        table.put(3, "passage ends in Door to a Chamber");
        table.put(4, "passage ends in Door to a Chamber");
        table.put(5, "passage ends in Door to a Chamber");
        table.put(6, "archway (door) to right (main passage continues straight for 10 ft)");
        table.put(7, "archway (door) to right (main passage continues straight for 10 ft)");
        table.put(8, "archway (door) to left (main passage continues straight for 10 ft)");
        table.put(9, "archway (door) to left (main passage continues straight for 10 ft)");
        table.put(10, "passage turns to left and continues for 10 ft");
        table.put(11, "passage turns to left and continues for 10 ft");
        table.put(12, "passage turns to right and continues for 10 ft");
        table.put(13, "passage turns to right and continues for 10 ft");
        table.put(14, "passage ends in archway (door) to chamber");
        table.put(15, "passage ends in archway (door) to chamber");
        table.put(16, "passage ends in archway (door) to chamber");
        table.put(17, "Stairs, (passage continues straight for 10 ft)");
        table.put(18, "Dead End");
        table.put(19, "Dead End");
        table.put(20, "Wandering Monster (passage continues straight for 10 ft)");
    }

    /**
     * generates elements of the section based on a die roll.
     * @param roll contains the die roll.
     */
    private void setupSection(int roll) {
        description = "";
        if (roll >= 1 && roll <= 2) {
            description += "The passage goes straight for 10ft.\n";
        } else if (roll >= 3 && roll <= 5) {
            description += "The passage ends in a door to a chamber.\n";
            myDoor = new Door();
            isChamber = true;
        } else if (roll >= 6 && roll <= 9) {
            description += "There is an " + table.get(roll) + ".\n";
            myDoor = new Door();
        } else if (roll >= 10 && roll <= 13) {
            description += "The " + table.get(roll) + ".\n";
        } else if (roll >= 14 && roll <= 16) {
            description += "The " + table.get(roll) + ".\n";
            myDoor = new Door();
            isChamber = true;
        } else if (roll == 17) {
            description += "There is a staircase. Main passage continues 10ft.\n";
        } else if (roll >= 18 && roll <= 19) {
            description += "There is a dead end. Backtrack to the nearest door.\n";
            isEnd = true;
        } else if (roll == 20) {
            if (myMonsters.size() > 0) {
                description += "Monsters!\n";
                for (int i = 0; i < myMonsters.size(); i++) {
                    description += "Monster " + (i + 1) + ": " + myMonsters.get(i).getDescription() + "\n";
                }
            }
            if (myTreasures.size() > 0) {
                description += "Treasures!\n";
                for (int i = 0; i < myTreasures.size(); i++) {
                    description += "Treasure " + (i + 1) + ": " + myTreasures.get(i).getDescription() + "\n";
                }
            }
        }
    }
}
