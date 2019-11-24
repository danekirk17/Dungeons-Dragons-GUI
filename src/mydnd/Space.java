package mydnd;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Abstract class for chambers and spaces.
 */
public abstract class Space implements Serializable {

    /**
     * abstract method header for description accessors.
     * @return String containing the description of the space.
     */
    public abstract  String getDescription();

    /**
     * abstract method header for methods assigning doors to spaces.
     * @param theDoor the door to be added.
     */
    public abstract void setDoor(Door theDoor);

    /**
     * abstract method header for door ArrayList accessors.
     * @return ArrayList of all doors in the space.
     */
    public abstract ArrayList<Door> getDoors();
}
