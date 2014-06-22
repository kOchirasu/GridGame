package gridGame;

import block.Unit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


public class MouseHandler extends MouseAdapter
{
    /* Private Variables
    pX, pY   - The selected units x and y grid coordinates
    cX, cY   - The current mouse x and y grid coordinates
    mX, mY   - The mouse x and y coordinates
    found    - True when the walk path has been found
    pathed   - True when the movement path has been found
    selected - Unit that is currently selected (Unit at [pX][pY])
    walkList - List of tiles that the unit will walk on
    */
    private int pX, pY, cX, cY, mX, mY;
    private boolean found, pathed;
    private Unit selected;
    private ArrayList<int[]> walkList;
    
    //Initializes the selected grid to one that does not exist
    public MouseHandler()
    {
        cX = Game.mapWidth;
        cY = Game.mapWidth;
    }

    //Updates the mouse coordinates, and interface
    @Override
    public void mousePressed(MouseEvent e) 
    {
        int x = e.getX(), y = e.getY();
        pX = (x - Game.MAPOFFX) / Game.TILESIZE;
        pY = (y - Game.MAPOFFY) / Game.TILESIZE;
        
        found = false;
        Game.gui.update(x, y, true);
    }
    
    //Updates the mouse coordinates, and interface.  Also does pathfinding
    @Override
    public void mouseDragged(MouseEvent e)
    {
        mX = e.getX();
        mY = e.getY();
        if((mX - Game.MAPOFFX) / Game.TILESIZE != cX || (mY - Game.MAPOFFY) / Game.TILESIZE != cY) {
            pathed = false;
        }
        cX = (int)Math.floor((mX - Game.MAPOFFX) / (double)Game.TILESIZE);
        cY = (int)Math.floor((mY - Game.MAPOFFY) / (double)Game.TILESIZE);
        
        if(found)
        {
            if(!pathed)
            {
                Game.paths.addPath(cX, cY);
                pathed = true;
            }
        }
        else
        {  
            selected = Game.getUnit(pX, pY);
            //if(selected != null && !selected.moving && !selected.hasMoved() && selected.getClassID() != -1)
            if(Game.gui.canSelect(selected))
            {
                //Find Paths
                Game.paths.findPath(selected.pathInfo());
                //Game.paths.printPaths();
                found = true;
            }
        }
        Game.gui.update(selected);
        Game.gui.update(mX, mY);
    }
    
    //Updates the mouse coordinates, and interface
    @Override
    public void mouseMoved(MouseEvent e)
    {
        mX = e.getX();
        mY = e.getY();
        //Might have to floor these if there are future problems
        cX = (mX - Game.MAPOFFX) / Game.TILESIZE;
        cY = (mY - Game.MAPOFFY) / Game.TILESIZE;

        Game.gui.update(mX, mY, false);
    }
    
    //Updates the interface, and also moves unit if applicable
    @Override
    public void mouseReleased(MouseEvent e) 
    {
        //System.out.print("X: " + cX + ", Y: " + cY + "\t");
        selected = Game.getUnit(pX, pY);
        if(Game.gui.canSelect(selected))
        {
            walkList = new ArrayList<>(Game.paths.getWalk());
            Game.paths.clearPaths();
            selected.move(cX, cY, walkList);
            //selected.damage(77);
        }
        Game.gui.update(selected);
        Game.gui.update(e.getX(), e.getY(), false);
    }
    
    //Hides mouse if you move out of the window
    @Override
    public void mouseExited(MouseEvent e)
    {
        Game.gui.update(false);
        Game.gui.update(0, 0, false);
    }
    
    //Brings mouse back
    @Override
    public void mouseEntered(MouseEvent e)
    {
        Game.gui.update(true);
    }
}
