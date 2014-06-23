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
    
    int invX, invY, invSmallX, invSmallY, invWidth, invHeight;
    
    int size, count;
    Item[] itemList;
    
    public Inventory(int size)
    {
        this.size = size;
        itemList = new Item[size];
        invX = 80;
        invY = 470;
        invWidth = 166;
        invHeight = 38;
        invSmallX = invX + 3;
        invSmallY = invY + 3;
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
    
    public void render(Graphics g)
    {
    //draw inventory here
        g.drawRect(invX,invY,invWidth,invHeight);
        g.drawRect(invSmallX,invSmallY,32,32);
        g.drawRect(invSmallX+32,invSmallY,32,32);
        g.drawRect(invSmallX+64,invSmallY,32,32);
        g.drawRect(invSmallX+96,invSmallY,32,32);
        g.drawRect(invSmallX+128,invSmallY,32,32);
       //Draw acutal inventory
    }    
        public void render2(Graphics g)
        {
        //Draw all items
        g.setColor(Color.blue);
        g.fillRect(invSmallX,invSmallY,10,10);
        g.fillRect(invSmallX+32,invSmallY,10,10);
        g.fillRect(invSmallX+64,invSmallY,10,10);
        g.fillRect(invSmallX+96,invSmallY,10,10);
        g.fillRect(invSmallX+128,invSmallY,10,10);
        for(int i = 0;i< itemList.length;i++)
            if(itemList[i] != null)
            {
                //g.fillRect(10,10,160,32);
                //draw all spritesn in locations here
            }   
        }
    
    
}
