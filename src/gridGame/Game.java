package gridGame;

import character.Path;
import character.Unit;
import graphics.*;
import gui.Interface;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import map.*;

public class Game extends Canvas implements Runnable
{
    private static final long serialVersionUID = 1L;
    public static final int WIDTH = 700, HEIGHT = 575;
    public static int mapWidth, mapHeight;
    public static boolean running = false;
    public static Path paths;
    public static Graphics g;
    public Thread gameThread;
    
    private static Map map;
    private BufferedImage spriteMain, damageNum, cursorMain;
    private Sprite im;
    private Interface gui;
    private MouseHandler mh;
    
    private static Unit[][] unitGrid;
    private ArrayList<Unit> unitList;
    
    public void init()
    {   
        //Map Data
        MapLoader mloader = new MapLoader();
        map = mloader.load("/map.txt");
        mapWidth = map.getWidth();
        mapHeight = map.getHeight();
        
        //Unit Data
        unitList = new ArrayList<>();
        unitGrid = new Unit[mapWidth][mapHeight];
        
        //Sprites
        SpriteLoader loader = new SpriteLoader();
        spriteMain = loader.load("/spritesheet.png");
        damageNum = loader.load("/number.png");
        cursorMain = loader.load("/cursor.png");
        im = new Sprite(spriteMain, damageNum, cursorMain);
        gui = new Interface(loader.load("/gui.png"), im);
        paths = new Path(im);
        
        //Custom cursor: creates invisible cursor which is redrawn in MouseHandler
        Toolkit toolkit = Toolkit.getDefaultToolkit();  
        setCursor(toolkit.createCustomCursor(im.image[1][7], new Point(0, 0), "Invisible"));
        
        //Create Units
        addUnit(1, 0, 0);
        addUnit(1, 1, 0);
        addUnit(1, 2, 0);
        addUnit(0, 0, 0);
        addUnit(0, 3, 0);
        addUnit(5, 3, 0);
        
        for(int i = 0; i < 16; i++)
        {
            addUnit(i, 11, 0);
        }
        
        //Create Buttons
        for(int i = 0; i < 8; i++)
        {
            gui.addButton(535, 22 + 46 * i, 149, 34, "Button " + i, i);
        }
        
        //MouseHandler
        mh = new MouseHandler(im);
        this.addMouseListener(mh);
        this.addMouseMotionListener(mh);
    }
    
    public synchronized void start() //Use synchronized when starting thread
    {
        init();
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
        //init();
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
        }
        stop();
    }
    
    public void tick()
    {
        for(Unit u : unitList) 
        {
            u.tick();
        }
        render();
    }
    
    public void render()
    {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null)
        {
            createBufferStrategy(3);
            return;
        }
        g = bs.getDrawGraphics();
        //Render Here
        gui.render(g);
        paths.render(g);
        for(Unit u : unitList) {
            u.render(g);
        }
        mh.render(g);
        
        g.dispose(); //Clean
        bs.show(); //Shows render
    }
    
    public void addUnit(int x, int y, int type)
    {
        Unit temp = new Unit(x, y, im);
        unitGrid[x][y] = temp;
        unitList.add(temp);
    }
    
    public static Unit getUnit(int x, int y)
    {
        return unitGrid[x][y];
    }
    
    public static void moveUnit(int oX, int oY, int nX, int nY, Unit u)
    {
        unitGrid[oX][oY] = null;
        unitGrid[nX][nY] = u;
    }
    
    public static int[][] getMap()
    {
        return map.getGrid();
    }

    public static void main(String[] args) 
    {
        Game game = new Game();
        game.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        game.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        game.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        
        JFrame frame = new JFrame("Grid Game");
        frame.pack();
        int nW = WIDTH + frame.getWidth() - frame.getContentPane().getWidth() - 10;
        int nH = HEIGHT + frame.getHeight() - frame.getContentPane().getHeight() - 10;
        frame.setSize(nW, nH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(game);
        frame.setVisible(true);
        
        game.start();
    }
}