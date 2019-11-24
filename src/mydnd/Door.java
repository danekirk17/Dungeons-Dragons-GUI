package mydnd;
import dnd.die.D10;
import dnd.die.D20;
import dnd.die.D6;
import dnd.models.Exit;
import dnd.models.Trap;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Creates a door. Doors connect two spaces (passage or chamber).
 */
public class Door implements Serializable {

    /**holds the trap in the room.*/
    private Trap myTrap;
    /**holds the two spaces connected by the door.*/
    private ArrayList<Space> mySpaces;
    /**holds ALL the spaces.*/
    private ArrayList<Space> allSpaces;
    /**true if door in an archway.*/
    private boolean isArchway;
    /**true if door is open.*/
    private boolean isOpen;
    /**true if door is locked.*/
    private boolean isLocked;
    /**true if door is trapped.*/
    private boolean isTrapped;
    /**holds a D20 for random generation.*/
    private D20 d20;
    /**holds description of the room.*/
    private String description;

    /**
     * Creates an object from the class Door.
     */
    public Door() {
        //needs to set defaults
        mySpaces = new ArrayList<>();
        allSpaces = new ArrayList<>();
        mySpaces.add(null); mySpaces.add(null);
        d20 = new D20();
        rndDoor();
        if (!isArchway && isTrapped) {
            myTrap = new Trap();
            myTrap.chooseTrap(d20.roll());
        }
    }

    /**
     * Creates an object from the class Door using an Exit.
     * @param theExit contains information on the location of the door.
     */
    public Door(Exit theExit) {
        //sets up the door based on the Exit from the tables
        mySpaces = new ArrayList<>();
        mySpaces.add(null); mySpaces.add(null);
        d20 = new D20();
        description = "A door is located " + theExit.getDirection() + " and " + theExit.getLocation() + ".\n";
        rndDoor();
        if (!isArchway && isTrapped) {
            myTrap = new Trap();
            myTrap.chooseTrap(d20.roll());
        }
    }

    /**
     * sets the trap in the door.
     * @param flag door will be trapped if true.
     * @param roll contains the roll to decide what the trap is.
     */
    public void setTrapped(boolean flag, int... roll) {
        // true == trapped.  Trap must be rolled if no integer is given
        if (!isArchway) {
            isTrapped = flag;
        } else {
            isTrapped = false;
        }
        //something with roll integer
        if (isTrapped) {
            myTrap = new Trap();
            if (roll.length != 0) {
                myTrap.chooseTrap(roll[0]);
            }
        }
    }

    /**
     * sets if door is open or closed.
     * @param flag door is open if true.
     */
    public void setOpen(boolean flag) {
        //true == open
        if (!isArchway) {
            isOpen = flag;
        } else {
            isOpen = true;
        }
    }

    /**
     * sets if door is an archway or not.
     * @param flag door is an archway if true.
     */
    public void setArchway(boolean flag) {
        //true == is archway
        isArchway = flag;
        if (isArchway) {
            isOpen = true;
            isLocked = false;
        }
    }

    /**
     * Accesses if door is trapped or not.
     * @return true if door is trapped.
     */
    public boolean isTrapped() {
        return isTrapped;
    }

    /**
     * Accesses if door is open or not.
     * @return true if door is open.
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Accesses if door is an archway or not.
     * @return true if door is an archway.
     */
    public boolean isArchway() {
        return isArchway;
    }

    /**
     * Accesses description of the trap in the door.
     * @return String with description of door trap.
     */
    public String getTrapDescription() {
        return myTrap.getDescription();
    }

    /**
     * Accesses the two spaces connected by the door.
     * @return a list containing the two spaces connected by the door.
     */
    public ArrayList<Space> getSpaces() {
        //returns the two spaces that are connected by the door
        return mySpaces;
    }

    /**
     * returns the list of all spaces connected by the door.
     * @return the list of all spaces connected by the door.
     */
    public ArrayList<Space> getAllSpaces() {
        return allSpaces;
    }

    /**
     * Sets the two spaces connected by the door.
     * @param spaceOne the first space connected by the door.
     * @param spaceTwo the second space connected by the door.
     */
    public void setSpaces(Space spaceOne, Space spaceTwo) {
        //identifies the two spaces with the door
        // this method should also call the setDoor method from Space
        if (spaceOne != null) {
            mySpaces.set(0, spaceOne);
        }
        if (spaceTwo != null) {
            mySpaces.set(1, spaceTwo);
        }
    }

    /**
     * adds a space to the comprehensive list of all spaces.
     * @param mySpace the space to be added.
     */
    public void addSpace(Space mySpace) {
        allSpaces.add(mySpace);
    }

    /**
     * Accesses a description of the door.
     * @return a String with description of the door.
     */
    public String getDescription() {
        description = "";
        description += "The door is ";
        if (isArchway) {
            description += "an archway. ";
        } else {
            if (isOpen) {
                description += "open. ";
            } else {
                description += "closed and ";
                if (isLocked) {
                    description += "locked. ";
                } else {
                    description += "unlocked. ";
                }
            }
        }
        if (isTrapped) {
            description += "It is booby trapped with " + myTrap.getDescription() + ".";
        }
        return description;
    }

    /**
     * generates random settings for the door based on die rolls.
     */
    private void rndDoor() {
        int roll;
        D6 d6 = new D6();
        D10 d10 = new D10();
        d20 = new D20();
        //use dice to decide if door is open, trapped etc.
        roll = d10.roll();
        if (roll == 1) {
            isArchway = true;
        } else {
            isArchway = false;
        }
        if (!isArchway) {
            roll = d20.roll();
            if (roll == 1) {
                isTrapped = true;
            } else {
                isTrapped = false;
            }
            roll = d20.roll();
            if (roll <= 10) {
                isOpen = true;
            } else {
                isOpen = false;
            }
            if (!isOpen) {
                roll = d6.roll();
                if (roll == 1) {
                    isLocked = true;
                } else {
                    isLocked = false;
                }
            } else {
                isLocked = false;
            }
        } else {
            isOpen = true;
            isLocked = false;
            isTrapped = false;
        }
    }
}
