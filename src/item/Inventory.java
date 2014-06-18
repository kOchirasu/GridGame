package item;

public class Inventory
{
    int size, count;
    Item[] itemList;
    
    public Inventory(int size)
    {
        this.size = size;
        itemList = new Item[size];
    }
    
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
    
    public Item getItem(int index)
    {
        if(index < size)
        {
            return itemList[index];
        }
        return null;
    }
}
