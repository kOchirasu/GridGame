package block;

import java.awt.Graphics;

public class Tile
{
    private int x, y, type;
    private Unit unit;
    
    public Tile(int x, int y, int t)
    {
        this.x = x;
        this.y = y;
        type = t;
    }
    
    public void tick()
    {
        unit.tick();
    }
    
    public void render(Graphics g)
    {
        unit.render(g);
    }
    
    public int getType()
    {
        return type;
    }
    
    public boolean addUnit(Unit u)
    {
        if (unit == null)
        {
            unit = u;
            //System.out.println("Unit added at: (" + x + ", " + y + ")");
            return true;
        }
        return false;
    }
    
    public void setUnit(Unit u)
    {
        unit = u;
    }
    
    public Unit getUnit()
    {
        return unit;
    }
}
