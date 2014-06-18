package gridGame;

import algorithm.*;
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
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import map.*;

public class Game extends Canvas implements Runnable
{
    private static final long serialVersionUID = 1L; //I dont even know what this is
    /* Public Variables
    WIDTH, HEIGHT       - Width and Height of the game window
    mapWidth, mapHeight - dimensions of map grid
    running             - True while the game is running
    paths               - Pathfinding class
    math                - Calculating class
    gui                 - Game interface class
    gameThread          - Game thread
    */
    public static final int WIDTH = 700, HEIGHT = 575;
    public static int mapWidth, mapHeight, gameSpeed = 2;
    public static boolean running = false;
    public static Path paths;
    public static Calculator math;
    public static Interface gui;
    public Thread gameThread;
    
    /* Private Variables
    map                 - Map class, contains map data
    im                  - Contains all pictures
    mh                  - Mouse handler
    unitGrid            - Grid array that contains all units
    unitList            - List of all units
    */
    private static Map map;
    private Sprite im;
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
        BufferedImage spriteMain = loader.load("/spritesheet.png");
        BufferedImage dmgNum = loader.load("/number.png");
        BufferedImage cursorMain = loader.load("/cursor.png");
        im = new Sprite(spriteMain, dmgNum, cursorMain);
        gui = new Interface(loader.load("/gui.png"), im);
        paths = new Path(im);
        math = new Calculator();
        
        //Custom cursor: creates invisible cursor which is redrawn in MouseHandler
        Toolkit toolkit = Toolkit.getDefaultToolkit();  
        setCursor(toolkit.createCustomCursor(im.image[1][9], new Point(0, 0), "Invisible"));
        
        //Create Units
        addUnit(1, 0, 0, -1, -1, -1, 0);
        addUnit(1, 1, 0, -1, -1, -1, 0);
        addUnit(1, 2, 0, -1, -1, -1, 0);
        addUnit(0, 0, 0, -1, -1, -1, 0);
        addUnit(0, 3, 0, -1, -1, -1, 0);
        addUnit(5, 3, 0, -1, -1, -1, 0);
        
        for(int i = 0; i < 16; i++)
        {
            addUnit(i, 11, 0, 50, 1, 1, 1);
        }
        
        //Create Buttons
        for(int i = 0; i < 8; i++)
        {
            gui.addButton("Button " + i, i);
        }
        
        //MouseHandler
        mh = new MouseHandler();
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
    
    @Override
    public void run()
    {
        long time = System.nanoTime();
        final double maxTick = 30.0 * gameSpeed;
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
        Graphics g = bs.getDrawGraphics();
        //Render Here
        gui.render(g);
        paths.render(g);
        for(Unit u : unitList) {
            u.render(g);
        }
        gui.render2(g);
        
        g.dispose(); //Clean
        bs.show(); //Shows render
    }
    
    public void addUnit(int x, int y, int type, int mov, int minRg, int maxRg, int team)
    {
        if(getUnit(x, y) == null && x >= 0 && y >= 0 && x < mapWidth && y < mapHeight)
        {
            Unit temp = new Unit(x, y, im, mov, minRg, maxRg, team);
            unitGrid[x][y] = temp;
            unitList.add(temp);
        }
        else
        {
            System.out.println("Unable to add unit. Game.java @ Line " + Thread.currentThread().getStackTrace()[1].getLineNumber());
        }
    }
    
    public static Unit getUnit(int x, int y)
    {
        if(x >= 0 && y >= 0 && x < mapWidth && y < mapHeight)
        {
            return unitGrid[x][y];
        }
        return null;
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