package gridGame;

import character.Unit;
import graphics.Sprite;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class MouseHandler extends MouseAdapter
{
    private int pX, pY, cX, cY, mX, mY;
    private boolean found = false;
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
        if(cX < 16 && cY < 12) {
            g.drawImage(sprite.image[2][1], cX * Sprite.spDIM, cY * Sprite.spDIM, Sprite.spDIM, Sprite.spDIM, null);
        }
        g.drawImage(sprite.cursor[0][0], mX, mY, 32, 32, null);
    }

    @Override
    public void mousePressed(MouseEvent e) 
    {
        pX = e.getX()/Sprite.spDIM;
        pY = e.getY()/Sprite.spDIM;
        found = false;
    }
    
    @Override
    public void mouseDragged(MouseEvent e)
    {
        mX = e.getX();
        mY = e.getY();
        cX = mX / Sprite.spDIM;
        cY = mY / Sprite.spDIM;
        if(!found)
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
        //System.out.println("X: " + cX + "Y: " + cY);
        //g.drawImage(im.sprite[1][3], tempX * Sprite.spDIM, tempY * Sprite.spDIM, Sprite.spDIM, Sprite.spDIM, null);
    }
    
    @Override
    public void mouseReleased(MouseEvent e) 
    {
        if(cX < 16 && cY < 12)
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
    }
}
