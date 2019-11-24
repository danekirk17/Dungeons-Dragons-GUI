package mydnd;

import dnd.die.D20;
import dnd.exceptions.NotProtectedException;
import dnd.exceptions.UnusualShapeException;
import dnd.models.ChamberShape;
import dnd.models.ChamberContents;
import dnd.models.Monster;
import dnd.models.Trap;
import dnd.models.Treasure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Creates rooms in the level. Rooms have doors which allow the adventure to continue.
 */
public class Chamber extends Space implements Serializable {

    /** holds the contents of the chamber.*/
    private ChamberContents myContents;
    /** holds the shape and size of the chamber.*/
    private ChamberShape mySize;
    /**holds the doors in the chamber.*/
    private ArrayList<Door> myDoors;
    /**holds the monsters in the chamber.*/
    private ArrayList<Monster> myMonsters;
    /**holds the treasures in the chamber.*/
    private ArrayList<Treasure> myTreasures;
    /**holds the trap in the chamber.*/
    private Trap myTrap;
    /**holds and inverted ChamberContents table for looking of die rolls with descriptions.*/
    private HashMap<String, Integer> revContents;
    /**holds the description of the chamber.*/
    private String description;
    /**
     * holds a D20 for random generation of chamber elements.
     */
    private D20 d20 = new D20();

    /**
     * Creates an object from the class Chamber.
     */
    public Chamber() {
        int roll = 0;
        initRevContents();
        myContents = new ChamberContents();
        mySize = ChamberShape.selectChamberShape(d20.roll());
        myMonsters = new ArrayList<>();
        myTreasures = new ArrayList<>();
        myDoors = new ArrayList<>();
        mySize.setNumExits();

        myContents.chooseContents(d20.roll());
        if (revContents.containsKey(myContents.getDescription())) {
            roll = revContents.get(myContents.getDescription());
        } else {
            System.out.println("revContents key not found.\n");
        }
        description = "";
        setupRoom(roll);
    }

    /**
     * Creates an object from the class Chamber using ChamberShape and ChamberContents objects.
     *
     * @param theShape Contains information on the size and shape of the room, as well as the number of exits.
     * @param theContents Contains information on which elements are present in the room.
     */
    public Chamber(ChamberShape theShape, ChamberContents theContents) {
        myContents = theContents;
        mySize = theShape;
        myMonsters = new ArrayList<>();
        myTreasures = new ArrayList<>();
        myDoors = new ArrayList<>();
        initRevContents();

        if (mySize.getNumExits() == 0) {
            while (mySize.getNumExits() != 1) {
                mySize.setNumExits();
            }
        }

        int roll = revContents.get(myContents.getDescription());
        setupRoom(roll);
        for (int i = 0; i < mySize.getNumExits(); i++) {
            Door d = new Door();
            Passage p = new Passage();
            p.addPassageSection(new PassageSection());
            d.setSpaces(this, p);
        }
    }

    /**
     * Sets the shape, size, and exits of the chamber.
     * @param theShape A ChamberShape object containing the shape, size, and exits.
     */
    public void setShape(ChamberShape theShape) {
        mySize = theShape;

        int roll = revContents.get(myContents.getDescription());
        setupRoom(roll);

        myDoors.clear();
        for (int i = 0; i < mySize.getNumExits(); i++) {
            Door d = new Door();
            Passage p = new Passage();
            p.addPassageSection(new PassageSection());
            d.setSpaces(this, p);
            myDoors.add(d);
        }
    }

    /**
     * add a new door to the room.
     * @param newDoor the door to be added.
     */
    @Override
    public void setDoor(Door newDoor) {
        //should add a door connection to this room
        myDoors.add(newDoor);
        newDoor.setSpaces(this, null);
        newDoor.addSpace(this);
    }

    /**
     * Accesses the doors in the room.
     * @return a list of all the doors in the room.
     */
    public ArrayList<Door> getDoors() {
        return myDoors;
    }

    /**
     * return the size of the door array.
     * @return the size of the door array.
     */
    public int getNumDoors() {
        return myDoors.size();
    }

    /**
     * return number of exits in the ChamberShape variable.
     * @return number of exits in the ChamberShape variable.
     */
    public int getNumExits() {
        return mySize.getNumExits();
    }

    /**
     * add a monster to the room.
     * @param theMonster the monster to be added.
     */
    public void addMonster(Monster theMonster) {
        myMonsters.add(theMonster);
    }

    public void remMonster() {
        if (myMonsters.size() > 0) {
            int ind = myMonsters.size() - 1;
            myMonsters.remove(ind);
        }
    }

    /**
     * Accesses the monsters in the room.
     * @return a list of all the monsters in the room.
     */
    public ArrayList<Monster> getMonsters() {
        return myMonsters;
    }

    /**
     * Add treasure to the room.
     * @param theTreasure the treasure to be added.
     */
    public void addTreasure(Treasure theTreasure) {
        myTreasures.add(theTreasure);
    }

    /**
     * Accesses the treasure in the room.
     * @return a list containing all the treasure in the room.
     */
    public ArrayList<Treasure> getTreasureList() {
        return myTreasures;
    }

    public void remTreasure() {
        if (myTreasures.size() > 0) {
            int ind = myTreasures.size() - 1;
            myTreasures.remove(ind);
        }
    }

    /**
     * Get a description of the room.
     * @return a String containing a description of the room.
     */
    @Override
    public String getDescription() {
        if (description.contains("The chamber is empty.") && myMonsters.size() == 0 && myTreasures.size() == 0) {
            description = "";
            setupShape();
            description += "The chamber is empty.\n";
        } else if (description.contains("The chamber contains stairs.") && myMonsters.size() == 0 && myTreasures.size() == 0) {
            description = "";
            setupShape();
            description += "The chamber contains stairs.\n";
        } else {
            description = "";
            setupShape();
        }

        for (int i = 0; i < myMonsters.size(); i++) {
            description += "Monster " + (i+1) + ": " + myMonsters.get(i).getDescription() + "\n";
        }
        for (int i = 0; i < myTreasures.size(); i++) {
            description += "Treasure " + (i+1) + ": " + myTreasures.get(i).getDescription()
                    + " stored in " + myTreasures.get(i).getContainer() + " and protected by ";
            try {
                description += myTreasures.get(i).getProtection() + ".\n";
            } catch (NotProtectedException e) {
                description += "nothing!\n";
            }
        }
        if (myTrap != null) {
            description += "Trap: " + myTrap.getDescription() + "\n";
        }
        return description;
    }

    /**
     * Create a hashtable for determining a roll based on a description.
     */
    private void initRevContents() {
        revContents = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            revContents.put("empty", i);
        }
        revContents.put("monster only", 13);
        revContents.put("monster only", 14);
        revContents.put("monster and treasure", 15);
        revContents.put("monster and treasure", 16);
        revContents.put("monster and treasure", 17);
        revContents.put("stairs", 18);
        revContents.put("trap", 19);
        revContents.put("treasure", 20);
    }

    /**
     * Initialize all elements of a room based on die rolls.
     * @param roll contains the die rolls
     */
    private void setupRoom(int roll) {
//        setupShape();
        if (roll >= 1 && roll <= 12) {
            description += "The chamber is empty.\n";
        } else if (roll >= 13 && roll <= 14) {
            Monster m = new Monster();
            m.setType(d20.roll());
            myMonsters.add(m);
//            description += "The chamber contains a monster only. The monster is a " + myMonsters.get(0).getDescription() + ".\n";
        } else if (roll >= 15 && roll <= 17) {
            Monster m = new Monster();
            m.setType(d20.roll());
            myMonsters.add(m);
            Treasure t = new Treasure();
            t.chooseTreasure(d20.roll());
            t.setContainer(d20.roll());
            myTreasures.add(t);
////            description += "The chamber contains a monster and treasure.\n"
////                    + "The monster is a " + myMonsters.get(0).getDescription() + ".\n"
////                    + "The treasure is " + myTreasures.get(0).getDescription()
////                    + " stored in " + myTreasures.get(0).getContainer() + " and protected by ";
//            try {
//                description += myTreasures.get(0).getProtection() + ".\n";
//            } catch (NotProtectedException e) {
//                description += "nothing!\n";
//            }
        } else if (roll == 18) {
            description += "The chamber contains stairs.\n";
        } else if (roll == 19) {
            myTrap = new Trap();
            myTrap.chooseTrap(d20.roll());
//            description += "The chamber has a trap. It is trapped with " + myTrap.getDescription() + ".\n";
        } else if (roll == 20) {
            Treasure t = new Treasure();
            t.chooseTreasure(d20.roll());
            myTreasures.add(t);
//            description += "The chamber contains treasure. The treasure is " + myTreasures.get(0).getDescription()
//                    + " stored in " + myTreasures.get(0).getContainer() + " and protected by ";
//            try {
//                description += myTreasures.get(0).getProtection() + ".\n";
//            } catch (NotProtectedException e) {
//                description += "nothing!\n";
//            }
        }
    }

    /**
     * setup the shape and size of the chamber.
     */
    private void setupShape() {
        try {
            description += "The chamber is " + mySize.getShape() + ". It is " + mySize.getWidth()
                    + " wide and " + mySize.getLength() + " long. ";
        } catch (UnusualShapeException e) {
            description += "The chamber is " + mySize.getShape() + " with an area of " + mySize.getArea() + ".\n";
        }
        description += "It has " + mySize.getNumExits() + " doors.\n";
    }
}
