package gridGame;

import character.Unit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class MouseHandler extends MouseAdapter
{
    private int pX, pY;
    private boolean found = false;
    private Unit selected;

    @Override
    public void mousePressed(MouseEvent e) 
    {
        pX = e.getX()/32;
        pY = e.getY()/32;
        found = false;
    }
    
    @Override
    public void mouseDragged(MouseEvent e)
    {
        if(!found)
        {  
            selected = Game.getUnit(pX, pY);
            if(selected != null)
            {
                //Find paths
                /*System.out.println("Getting Paths...");
                long startTime = System.nanoTime();
                selected.getPaths();
                long halfTime = System.nanoTime();
                selected.printPaths();
                long finalTime = System.nanoTime();
                
                System.out.println("It took " + (halfTime - startTime) + " nanoseconds to find paths.");
                System.out.println("It took " + (finalTime - halfTime) + " nanoseconds to print paths.");
                System.out.println("Total time elapsed: " + (finalTime - startTime) + " nanoseconds");*/
                Game.paths.getPaths(selected.pathInfo());
                Game.paths.printPaths();
                //Display paths
                //selected.shadePaths();
                found = true;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) 
    {
        int x = e.getX();
        int y = e.getY();
        if(x <= 512 && y <= 384)
        {
            //System.out.println("X: " + x/32 + ", Y: " + y/32 + "\t (" + x + ", " + y + ")");
            System.out.print("X: " + x/32 + ", Y: " + y/32 + "\t");
            selected = Game.getUnit(pX, pY);
            
            if(selected == null)
            {
                System.out.println("There is no unit here.");
            }
            else
            {
                Game.paths.clearPaths();
                //selected.damage(153);
                if(pX == x/32 && pY == y/32) //Moused clicked
                {
                    //selected.move(1);
                }
                else
                {
                    selected.move(x/32, y/32);
                }
                System.out.println("There is a unit here.");
            }
        }
    }
}
