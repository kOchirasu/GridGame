package gridGame;

import character.Unit;
import graphics.SpriteLoader;
import graphics.SpriteSheet;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable
{
    private static final long serialVersionUID = 1L;
    public static final int WIDTH = 200, HEIGHT = 200, SCALE = 2;
    public static boolean running = false;
    public Thread gameThread;
    
    private BufferedImage spriteMain;
    
    private Unit firstUnit;
    
    public void init()
    {
        SpriteLoader loader = new SpriteLoader();
        spriteMain = loader.load("/spritesheet.png");
        
        SpriteSheet sheet = new SpriteSheet(spriteMain);
        
        firstUnit = new Unit(0, 0, sheet);
    }
    
    public synchronized void start() //Use synchronized when starting thread
    {
        if(!running)
        {
            running = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
    }
    
    public synchronized void stop() //Use synchronized when stopping thread
    {
        if(running)
        {
            running = false;
            try 
            {
                gameThread.join();
            } 
            catch (InterruptedException ex) 
            {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void run()
    {
        init();
        long time = System.nanoTime();
        final double maxTick = 60.0;
        double ns = 1000000000 / maxTick;
        double delta = 0;
        
        while(running)
        {
            long now = System.nanoTime();
            delta += (now - time) / ns;
            time = now;
            if(delta >= 1)
            {
                tick();
                delta--;
            }
            render();
        }
        stop();
    }
    
    public void tick()
    {
        firstUnit.tick();
    }
    
    public void render()
    {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null)
        {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        //Render Here
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE); //Draws rectangle to screen
        firstUnit.render(g);
        
        
        g.dispose(); //Clean
        bs.show(); //Shows render
    }

    public static void main(String[] args) 
    {
        Game game = new Game();
        game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        game.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        game.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        
        JFrame frame = new JFrame("Grid Game");
        frame.setSize(WIDTH * SCALE, HEIGHT * SCALE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(game);
        frame.setVisible(true);
        
        game.start();
    }
}