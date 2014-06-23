package item;

import gridGame.Game;
import java.awt.Color;
import java.awt.Graphics;

//Untested, but should work logically
public class Inventory
{
    /*
    size        - Size of inventory
    count       - Current number of items in inventory
    itemList    - Array of items in inventory
    */
    int x, y, w, h;
    int size, count;
    Item[] itemList;
    
    public Inventory(int size)
    {
        this.size = size;
        itemList = new Item[size];
        x = 50;
        y = 470;
        w = 166;
        h = 38;
    }
    
    public void render(Graphics g)
    {
        //draw inventory here
        g.drawRect(x, y, w, h);
        for(int i = 0; i < 5; i++) {
            g.drawRect(x + 3 + Game.TILESIZE * i, y + 3, Game.TILESIZE, Game.TILESIZE);
        }
       //Draw acutal inventory
    }    
    
    public void render2(Graphics g)
    {
        //Draw all items
        g.setColor(Color.BLUE);
        for(int i = 0; i < 5; i++) {
            g.drawRect(x + 3 + Game.TILESIZE * i, y + 3, 10, 10);
        }
        for(int i = 0; i < itemList.length; i++)
        {
            if(itemList[i] != null)
            {
                //g.fillRect(10,10,160,32);
                //draw all spritesn in locations here
            }   
        }
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
