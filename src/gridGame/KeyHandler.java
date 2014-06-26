package gridGame;

import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.*;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener
{
    @Override
    public void keyPressed(KeyEvent e)
    {
        switch(e.getKeyCode())
        {
            case VK_UP:
                Game.offset(Game.xOff, Game.yOff - 1);
                break;
                
            case VK_RIGHT:
                Game.offset(Game.xOff + 1, Game.yOff);
                break;
                
            case VK_DOWN:
                Game.offset(Game.xOff, Game.yOff + 1);
                break;
            
            case VK_LEFT:
                Game.offset(Game.xOff - 1, Game.yOff);
                break;
            case VK_ESCAPE:
                Game.gui.click(1);
                break;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e)
    {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyTyped(KeyEvent e) 
    {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
