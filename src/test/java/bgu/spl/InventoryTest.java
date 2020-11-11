package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {
    String[] gadgetToInsert;
    String[] getGadgetToInsert2;
    Inventory inventory;
    @BeforeAll
    public void setTest()
    {
        inventory=inventory.getInstance();
    }
    @BeforeEach
    public void setUp(){
        gadgetToInsert=new String[]{"Sky Hook","Geiger counter","X-ray glasses","Dagger shoe"};
        getGadgetToInsert2=new String[]{"shotgun","rope","grenade","explosive pen"};
    }

    @Test
    public void testLoad()
    {
        inventory.load(gadgetToInsert);

        for(String gadg: gadgetToInsert)
        {
            assertTrue(inventory.getItem(gadg),"the gadget is in the inventory");
        }
        assertFalse(inventory.getItem("knife"),"the gadget is not in the inventory");
    }

    @Test
    public void testGetItem()
    {
        inventory.load(getGadgetToInsert2);
        assertTrue(inventory.getItem("shotgun"),"the gadget is in the inventory");
        assertTrue(inventory.getItem("grenade"),"the gadget is in the inventory");
        assertTrue(inventory.getItem("explosive pen"),"the gadget is in the inventory");

        assertFalse(inventory.getItem("knife"),"the gadget is not in the inventory");

        //test that the getitem removes the gadget from the inventory map
        assertFalse(inventory.getItem("shotgun"),"item was not removed");
        assertFalse(inventory.getItem("granade"),"item was not removed");

    }


    @Test
    public void test(){

        //TODO: change this test and add more tests :)
        fail("Not a good test");
    }
}
