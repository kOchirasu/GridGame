package item;

import gridGame.Game;

//Untested, but should work logically
public class Inventory
{
    /*
    size        - Size of inventory
    count       - Current number of items in inventory
    itemList    - Array of items in inventory
    */
    int size, count;
    Item[] itemList;
    
    public Inventory(int size)
    {
        this.size = size;
        itemList = new Item[size];
    }
    
    //Adds an item to inventory (by ID) if it is not full
    public boolean addItem(int iD)
    {
        if(count < size)
        {
            itemList[count] = new Item(Game.lookup, iD);
            count++;
            return true;
        }
        return false;
    }
    
    //Adds an item to inventory (by Item instance) if it is not full
    public boolean addItem(Item item)
    {
        if(count < size)
        {
            itemList[count] = item;
            count++;
            return true;
        }
        return false;
    }
    
    //Removes an item from inventory (by index) if it exists
    public Item removeItem(int index)
    {
        if(index < size)
        {
            Item ret = itemList[index];
            itemList[index] = null;
            return ret;
        }
        return null;
    }
    
    //Removes item at specified index if it exists
    public Item getItem(int index)
    {
        if(index < size)
        {
            return itemList[index];
        }
        return null;
    }
}
