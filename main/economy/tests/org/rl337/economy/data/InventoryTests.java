package org.rl337.economy.data;

import java.io.File;

import junit.framework.TestCase;

import org.rl337.economy.SerializationUtil;
import org.rl337.economy.data.Inventory.InventoryItem;

public class InventoryTests extends TestCase {
    private Inventory mInventory;
    private File mFile;
    
    
    public void setUp() throws Exception {
        mInventory = new Inventory();
        mFile = File.createTempFile("InventoryTests", ".txt");
    }
    
    public void tearDown() throws Exception {
        mFile.delete();
    }
    
    public void testGiveHasAndTake() {
        assertFalse("Initial Inventory shouldn't have anything", mInventory.has(Resource.Food, 1));
        assertEquals("Initial Inventory should return 0 for items it does not have", 0, mInventory.amount(Resource.Food));
        mInventory.give(Resource.Food, 3);
        assertTrue("Inventory should now have at least 1 food", mInventory.has(Resource.Food, 1));
        assertFalse("Inventory should not have 4 food", mInventory.has(Resource.Food, 4));
        
        InventoryItem item = mInventory.take(Resource.Food, 2);
        assertNotNull("We should have gotten an item back", item);
        assertEquals("Item should be of type food", Resource.Food, item.getType());
        assertEquals("Item should have quantity of 2", 2, item.getQuantity());
        assertEquals("inventory should now have exactly 1 food", 1, mInventory.amount(Resource.Food));
        
        InventoryItem nullItem = mInventory.take(Resource.Food, 4);
        assertNull("get 4 food should have returned null", nullItem);
        assertEquals("After failed get, we should still have 1 food", 1, mInventory.amount(Resource.Food));
        
        InventoryItem oneItem = mInventory.take(Resource.Food, 1);
        assertNotNull("Taking the last item shouldn't return null", oneItem);
        assertEquals("Quantity of last item should be 1", 1, oneItem.getQuantity());
        assertEquals("Type of last item should be food", Resource.Food, oneItem.getType());
        
        assertNull("Now that we have no more food, further gets should return null", mInventory.take(Resource.Food, 1));
        assertNull("Just for the hell of it, verify that a get of qty 0 returns null", mInventory.take(Resource.Food, 0));
    }
    
    public void testGiveBadInput() {
        mInventory.give(Resource.Food, -5);
        assertEquals("If we gave -5 we should still *have* 0", 0, mInventory.amount(Resource.Food));
        
        mInventory.give(Resource.Food, 2);
        assertEquals("If we had previously given -5 and then we give 2, we should have 2.", 2, mInventory.amount(Resource.Food));
    }
    
    public void testLoadAndSave() {
        mInventory.give(Resource.Food, 5);
        mInventory.give(Resource.Perishables, 3);
        
        SerializationUtil.writeJSON(mInventory, mFile);
        
        Inventory newInventory = SerializationUtil.loadJSON(Inventory.class, mFile);
        assertEquals("inventory should have 5 food", 5, newInventory.amount(Resource.Food));
        assertEquals("inventory should have 3 Perishables", 3, newInventory.amount(Resource.Perishables));
        assertEquals("inventory should not have unknowns", 0, newInventory.amount(Resource.Unknown));
    }

}
