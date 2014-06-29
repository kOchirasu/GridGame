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
    private int x, y, w, h;
    private int size, count;
    private Item[] itemList;
    
    public Inventory(int size)
    {
        this.size = size;
        itemList = new Item[size];
        x = 50;
        y = 470;
        w = 176;
        h = 40;
        int n = 100001 + (int)(Math.floor(Math.random() * 8));
        addItem(n);
        n = 110001 + (int)(Math.floor(Math.random() * 7));
        addItem(n);
        n = 120001 + (int)(Math.floor(Math.random() * 7));
        addItem(n);
    }
    
    public void render(Graphics g)
    {
        //draw inventory here
        g.drawRect(x, y, w, h);
        for(int i = 0; i < size; i++) {
            g.drawRect(x + 3 + (Game.TILESIZE + 2) * i, y + 3, Game.TILESIZE + 2, Game.TILESIZE + 2);
        }
        if(this.size > 0)
        {
            g.drawString(itemList[0].getNAME(), x + 10, y + 60);
        }
        //Draw all items
        g.setColor(Color.BLUE);
        for(int i = 0; i < size; i++) {
            g.drawRect(x + 3 + (Game.TILESIZE + 2) * i, y + 3, 10, 10);
        }
        for(int i = 0; i < size; i++) //Change back to count later if you want to be able to not move to null
        {   
            if(itemList[i] != null) {
                itemList[i].render(x + 4 + (Game.TILESIZE + 2) * i, y + 4, 1, g);
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
            for(int i = index; i < size; i++) {
                itemList[i] = itemList[i + 1];
            }
            return ret;
        }
        return null;
    }
    
    //Retrives item at specified index if it exists
    public Item getItem(int index)
    {
        if(index < size) {
            return itemList[index];
        }
        return null;
    }
    
    //Swaps two items from their respective slot.  If items have gap between them, it shifts up all the items.
    public void swap(int s1, int s2)
    {
        if(s1 >= 0 && s2 >= 0 && s1 < size && s2 < size)
        {
            Item temp = itemList[s1];
            itemList[s1] = itemList[s2];
            itemList[s2] = temp;
            int n = size;
            for(int i = Math.min(s1, s2); i < n; i++)
            {
                if(itemList[i] == null)
                {
                    for(int j = i; j < n - 1; j++) {
                        itemList[j] = itemList[j + 1];
                        itemList[j + 1] = null;
                    }
                    i--;
                    n--;
                }
            }
        }
    }
    
    public int[] area(int n) {
        return new int []{x + 4 + (Game.TILESIZE + 2) * n, x + 4 + (Game.TILESIZE + 2) * n + Game.TILESIZE, y + 4, y + 4 + Game.TILESIZE};
    }
    public int getX() {
        return x;    
    }
    public int getY() {
        return y;
    }
    public int getSize() {
        return size;
    }
}
