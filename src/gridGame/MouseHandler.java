package gridGame;

import character.Unit;
import graphics.Sprite;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class MouseHandler extends MouseAdapter
{
    private int pX, pY, cX, cY, mX, mY;
    private boolean found = false;
    private Unit selected;
    
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
        cX = mX / Sprite.spDIM;
        cY = mY / Sprite.spDIM;
        
        if(!found && pX < Game.mapWidth && pY < Game.mapHeight)
        {  
            selected = Game.getUnit(pX, pY);
            if(selected != null)
            {
                //Find Paths
                Game.paths.getPaths(selected.pathInfo());
                //Game.paths.printPaths();

                found = true;
            }
        }
        Game.gui.update(mX, mY);
    }
    
    @Override
    
    public void mouseMoved(MouseEvent e)
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
        if(cX < Game.mapWidth && cY < Game.mapHeight && pX < Game.mapWidth && pY < Game.mapHeight)
        {
            //System.out.println("X: " + cX + ", Y: " + cY + "\t (" + x + ", " + y + ")");
            System.out.print("X: " + cX + ", Y: " + cY + "\t");
            selected = Game.getUnit(pX, pY);
            
            if(selected == null)
            {
                System.out.println("There is no unit here.");
            }
            else
            {
                Game.paths.clearPaths();
                //selected.damage(153);
                if(pX == cX && pY == cY) //Moused clicked
                {
                    
                }
                else
                {
                    selected.move(cX, cY);
                }
                System.out.println("");
            }
        }
        Game.gui.update(e.getX(), e.getY(), false);
    }
}
