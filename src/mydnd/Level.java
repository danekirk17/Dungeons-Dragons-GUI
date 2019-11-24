package mydnd;

import dnd.die.D8;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Level implements Serializable {
    /**
     * holds all the passages in the level.
     */
    private ArrayList<Passage> passages;
    /**
     * holds all the chambers in the level.
     */
    private ArrayList<Chamber> chambers;
    /**
     * holds a mapping of every door in the level and the chambers they connect.
     */
    private HashMap<Door, ArrayList<Chamber>> map;
    /**
     * holds a string description of the level including all chambers and passages.
     */
    private String description;
    /**
     * holds a D8 for randomly mapping doors to chambers.
     */
    private D8 d8;
    /**
     * tracks the number of each chamber for identification purposes.
     */
    private HashMap<Chamber, Integer> chamberNumbers;

    /**
     * Creates an object with the class Level, initializes all instance variables.
     */
    public Level() {
        init();
    }

    /**
     * Creates an object with the class Level containing a specified number of chambers, initializes all instance variables.
     * @param numChambers number of chambers to initialize level with.
     */
    public Level(int numChambers) {
        init();
        for (int i = 0; i < numChambers; i++) {
            addChamber();
            chamberNumbers.put(chambers.get(chambers.size() - 1), i);
        }
        setTargets();
        mapPassages();
    }

    /**
     * initializes all instance variables.
     */
    private void init() {
        passages = new ArrayList<>();
        chambers = new ArrayList<>();
        map = new HashMap<>();
        d8 = new D8();
        chamberNumbers = new HashMap<>();
        description = "";
    }

    /**
     * adds a chamber to the level. creates all necessary doors and adds them to the new chamber.
     */
    private void addChamber() {
        Chamber c = new Chamber();
        for (int j = 0; j < c.getNumExits(); j++) {
            c.setDoor(new Door());
        }
        chambers.add(c);
    }

    /**
     *  maps all doors to random chambers in the level.
     */
    private void setTargets() {
        int chamberCount;
        for (int i = 0; i < 5; i++) {
            ArrayList<Door> doors = chambers.get(i).getDoors();
            for (int j = 0; j < doors.size(); j++) {
                chamberCount = d8.roll() / 2;
                ArrayList<Chamber> targets = new ArrayList<>();
                while (doors.get(j).getSpaces().get(0).equals(chambers.get(chamberCount))) {
                    chamberCount = d8.roll() / 2;
                }
                targets.add(chambers.get(chamberCount));
                map.put(doors.get(j), targets);
            }
        }
    }

    /**
     * connects all doors to their target chambers with new passages.
     */
    private void mapPassages() {
        ArrayList<Door> doorList = new ArrayList<>(map.keySet());
        for (int i = 0; i < doorList.size(); i++) {
            Passage p = new Passage();
            PassageSection ps1;
            PassageSection ps2;
            ps1 = new PassageSection("passage goes straight for 10 ft");
            ps2 = new PassageSection("passage ends in Door to a Chamber");
            p.addPassageSection(ps1);
            p.addPassageSection(ps2);

            Door targetDoor = map.get(doorList.get(i)).get(0).getDoors().get(0);
            p.setDoor(doorList.get(i));
            doorList.get(i).addSpace(p);
            p.setDoor(targetDoor);
            targetDoor.addSpace(p);
            targetDoor.setSpaces(null, p);   //find the first door in the door's target chamber and set spaces with the new passage
            passages.add(p);
        }
    }

    /**
     * adds a passage to the list of passages in this level.
     */
    public void addPassage() {
        passages.add(new Passage());
    }

    /**
     * adds a section to the most recent passage.
     */
    private void addSection() {
        PassageSection ps;
        do {
            ps = new PassageSection();
        } while (ps.isEnd());
        passages.get(passages.size() - 1).addPassageSection(new PassageSection());
    }

    /**
     * returns a specified chamber in the level.
     * @param i the index of the chamber to be returned.
     * @return the specified chamber.
     */
    public Chamber getChamber(int i) {
        return chambers.get(i);
    }

    /**
     * get the list of chambers in the level.
     * @return the list of chambers in the level.
     */
    public ArrayList<Chamber> getChambers() {
        return chambers;
    }

    /**
     * Accessor for chamber numbers hashmap.
     * @return the hashmap with chambers and their index.
     */
    public HashMap<Chamber, Integer> getChamberNumbers() {
        return chamberNumbers;
    }

    /**
     * gets the list of passages in the dungeon level.
     * @return the list of passages in the dungeon level.
     */
    public ArrayList<Passage> getPassages() {
        return passages;
    }

    /**
     * returns a string description of the level including all chambers and passages.
     * @return a string description of the level including all chambers and passages.
     */
    public String getDescription() {
        description += "***CHAMBERS***\n";
        for (int i = 0; i < chambers.size(); i++) {
            description += "Chamber #" + (i + 1) + "\n";
            description += chambers.get(i).getDescription();
            for (int j = 0; j < chambers.get(i).getDoors().size(); j++) {
                description += "Door " + (j + 1) + ": ";
                description += chambers.get(i).getDoors().get(j).getDescription() + "\n";
            }
            description += "\n";
        }
        Chamber c1;
        Chamber c2;
        description += "\n***PASSAGES***\n";
        for (int i = 0; i < passages.size(); i++) {
            c1 = (Chamber) passages.get(i).getDoors().get(0).getSpaces().get(0);
            c2 = (Chamber) passages.get(i).getDoors().get(1).getSpaces().get(0);
            description += "Passage #" + (i + 1) + "\n";
            description += passages.get(i).getDescription();
            description += "Connects Chamber #" + (chamberNumbers.get(c1) + 1) + " to Chamber #" + (chamberNumbers.get(c2) + 1) + ".\n\n";
        }
        return description;
    }
}
