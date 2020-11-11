package bgu.spl.mics.application.passiveObjects;

import org.json.simple.JSONArray;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * That's where Q holds his gadget (e.g. an explosive pen was used in GoldenEye, a geiger counter in Dr. No, etc).
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Inventory {
    private List<String> gadgets;
    private static Inventory instanceInventory;

    /**
     * Retrieves the single instance of this class.
     */
    private Inventory() {
        gadgets = new LinkedList<>();

    }

    public static Inventory getInstance() {
        if (instanceInventory == null)
            instanceInventory = new Inventory();
        return instanceInventory;
    }

    /**
     * Initializes the inventory. This method adds all the items given to the gadget
     * inventory.
     * <p>
     *
     * @param inventory Data structure containing all data necessary for initialization
     *                  of the inventory.
     */
    public void load(String[] inventory) {
        for (String inven : inventory) {
            if (!this.gadgets.contains(inven))
                this.gadgets.add(inven);
        }
    }

    /**
     * acquires a gadget and returns 'true' if it exists.
     * <p>
     *
     * @param gadget Name of the gadget to check if available
     * @return ‘false’ if the gadget is missing, and ‘true’ otherwise
     */
    public synchronized boolean getItem(String gadget) {
        if (!this.gadgets.contains(gadget)) return false;
        this.gadgets.remove(gadget);
        return true;
    }

    /**
     * <p>
     * Prints to a file name @filename a serialized object List<String> which is a
     * list of all the of the gadgeds.
     * This method is called by the main method in order to generate the output.
     */
    public void printToFile(String filename) {
        try {
            JSONArray messages = new JSONArray();
            for (String gadget : this.gadgets) {
                messages.add(gadget);
            }
            Files.write(Paths.get(filename), messages.toJSONString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}