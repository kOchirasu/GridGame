package gridGame;

import character.Unit;
import graphics.Sprite;
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
    private boolean found = false, pathed = false;
    private Unit selected;
    private ArrayList<int[]> walkList;
    
    public MouseHandler()
    {
        cX = Game.mapWidth;
        cY = Game.mapWidth;
    }

    @Override
    public void mousePressed(MouseEvent e) 
    {
        int x = e.getX(), y = e.getY();
        pX = x/Sprite.spDIM;
        pY = y/Sprite.spDIM;
        
        found = false;
        Game.gui.update(x, y, true);
    }
    
    @Override
    public void mouseDragged(MouseEvent e)
    {
        mX = e.getX();
        mY = e.getY();
        if(mX / Sprite.spDIM != cX || mY /Sprite.spDIM != cY) {
            pathed = false;
        }
        cX = mX / Sprite.spDIM;
        cY = mY / Sprite.spDIM;

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
            if(selected != null && !selected.moving)
            {
                //Find Paths
                Game.paths.findPath(selected.pathInfo());
                //Game.paths.printPaths();
                found = true;
            }
        }
        Game.gui.update(mX, mY);
    }
    
    @Override
    public void mouseMoved(MouseEvent e) //Always positive coordinates
    {
        mX = e.getX();
        mY = e.getY();
        cX = mX / Sprite.spDIM;
        cY = mY / Sprite.spDIM;

        Game.gui.update(mX, mY, false);
    }
    
    @Override
    public void mouseReleased(MouseEvent e) 
    {
        //System.out.print("X: " + cX + ", Y: " + cY + "\t");
        selected = Game.getUnit(pX, pY);
        if(selected != null && !selected.moving)
        {
            walkList = new ArrayList<>(Game.paths.getWalk());
            Game.paths.clearPaths();
            //selected.damage(153);
            selected.move(cX, cY, walkList);
        }
        Game.gui.update(e.getX(), e.getY(), false);
    }
}
