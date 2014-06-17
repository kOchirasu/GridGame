package gridGame;

import character.Unit;
import graphics.Sprite;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class MouseHandler extends MouseAdapter
{
    private int pX, pY, cX, cY, mX, mY, bID = -1, bIDt;
    private boolean found = false, pressed = false;
    private Sprite sprite;
    private Unit selected;
    
    public MouseHandler(Sprite sprite)
    {
        this.sprite = sprite;
        cX = Game.mapWidth;
        cY = Game.mapWidth;
    }
    
    public void render(Graphics g)
    {
        if(cX < Game.mapWidth && cY < Game.mapHeight) {
            g.drawImage(sprite.image[2][1], cX * Sprite.spDIM, cY * Sprite.spDIM, Sprite.spDIM, Sprite.spDIM, null);
        }
        if(bID != -1) {
            g.drawImage(sprite.cursor[0][1], mX, mY, 32, 32, null);
        }
        else {
            g.drawImage(sprite.cursor[0][0], mX, mY, 32, 32, null);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) 
    {
        int x = e.getX(), y = e.getY();
        pX = x/Sprite.spDIM;
        pY = y/Sprite.spDIM;
        //System.out.println(bID);
        found = false;
        if(bID != -1) {
            Game.gui.buttonList.get(bID).pressed = true;
        }
    }
    
    @Override
    public void mouseDragged(MouseEvent e)
    {
        mX = e.getX();
        mY = e.getY();
        cX = mX / Sprite.spDIM;
        cY = mY / Sprite.spDIM;
        //bID = (mY >= 22 && mX >= 535 && mX <= 681 && (mY - 22) % 46 <= 34) ? (mY - 22) / 46 : -1;
        if(!found && pX < Game.mapWidth && pY < Game.mapHeight)
        {  
            selected = Game.getUnit(pX, pY);
            if(selected != null)
            {
                //Find paths
                /*System.out.println("Getting Paths...");
                long startTime = System.nanoTime();
                Game.paths.getPaths(selected.pathInfo());
                long halfTime = System.nanoTime();
                Game.paths.printPaths();
                long finalTime = System.nanoTime();
                
                System.out.println("It took " + (halfTime - startTime) + " nanoseconds to find paths.");
                System.out.println("It took " + (finalTime - halfTime) + " nanoseconds to print paths.");
                System.out.println("Total time elapsed: " + (finalTime - startTime) + " nanoseconds");*/
                Game.paths.getPaths(selected.pathInfo());
                Game.paths.printPaths();

                found = true;
            }
        }
    }
    
    @Override
    
    public void mouseMoved(MouseEvent e)
    {
        mX = e.getX();
        mY = e.getY();
        cX = mX / Sprite.spDIM;
        cY = mY / Sprite.spDIM;
        
        //Button calculations
        bIDt = bID;
        bID = (mY >= 22 && mY <= 390 && mX >= 535 && mX <= 681 && (mY - 22) % 46 <= 34) ? (mY - 22) / 46 : -1;
        if(bID != bIDt)
        {
            if(bIDt != -1) {
                Game.gui.buttonList.get(bIDt).shade = false;
            }
            if(bID != -1) {
                Game.gui.buttonList.get(bID).shade = true;
            }
        }
        //System.out.println("X: " + cX + "Y: " + cY);
        //g.drawImage(im.sprite[1][3], tempX * Sprite.spDIM, tempY * Sprite.spDIM, Sprite.spDIM, Sprite.spDIM, null);
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
                    //selected.move(1);
                }
                else
                {
                    selected.move(cX, cY);
                }
                //System.out.println("There is a unit here.");
            }
        }
        
        //Button calculations
        if(bID != -1) {
            Game.gui.buttonList.get(bID).pressed = false;
        }
        bIDt = bID;
        bID = (e.getY() >= 22 && e.getY() <= 390 && e.getX() >= 535 && e.getX() <= 681 && (e.getY() - 22) % 46 <= 34) ? (e.getY() - 22) / 46 : -1;
        if(bID != bIDt)
        {
            if(bIDt != -1) {
                Game.gui.buttonList.get(bIDt).shade = false;
            }
            if(bID != -1) {
                Game.gui.buttonList.get(bID).shade = true;
            }
        }
        else
        {
            if(bID != -1) {
                Game.gui.buttonList.get(bID).click();
            }
        }
    }
}
