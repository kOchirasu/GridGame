package gridGame;

import algorithm.*;
import character.Unit;
import graphics.*;
import gui.Interface;
import item.ItemLoader;
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
    private static final long serialVersionUID = 1L; //I dont even know what this is
    /* Public Variables
    WIDTH, HEIGHT       - Width and Height of the game window
    MAPOFFX, MAPOFFY    - Map x, y offsets so that it doesnt need to start in top left corner
    TILESIZE            - Dimensions of a tile (Square)
    mapWidth, mapHeight - dimensions of map grid
    gameSpeed           - fps of game (1 = 30fps, 2 = 60fps)
    running             - True while the game is running
    paths               - Pathfinding class
    math                - Calculating class
    gui                 - Game interface class
    gameThread          - Game thread
    lookup              - Item database
    */
    public static final int WIDTH = 700, HEIGHT = 575;
    public static final int MAPOFFX = 9, MAPOFFY = 9, TILESIZE = 32;
    public static int mapWidth, mapHeight, gameSpeed = 2;
    public static boolean running = false;
    public static Path paths;
    public static Calculator math;
    public static Interface gui;
    public Thread gameThread;
    public static ItemLoader lookup;
    
    public static Unit theUnit = new Unit((short)-1); //Dummy unit used for keeping units selected, bad fix but w/e
    
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
    private static ArrayList<Unit> unitList;
    
    //Initializes the game
    public void init()
    {           
        //Sprites
        SpriteLoader loader = new SpriteLoader();
        BufferedImage spriteGrid = loader.load("/sprite.png");
        BufferedImage damageGrid = loader.load("/damage.png");
        BufferedImage cursorGrid = loader.load("/cursor.png");
        BufferedImage tileGrid = loader.load("/tile.png");
        im = new Sprite(spriteGrid, damageGrid, cursorGrid, tileGrid);
        
        //Map Data
        MapLoader mloader = new MapLoader();
        map = mloader.load("/map.txt", im);
        mapWidth = map.getWidth();
        mapHeight = map.getHeight();
        //map.printMap();
        
        //Unit Data
        unitList = new ArrayList<>();
        unitGrid = new Unit[mapWidth][mapHeight];
        
        //Mot initializtion
        gui = new Interface(loader.load("/gui.png"), im);
        paths = new Path();
        math = new Calculator();
        lookup = new ItemLoader("jdbc:derby://localhost:1527/Item", "gridgame", "maplestory");
        
        //Custom cursor: creates invisible cursor which is redrawn in Interface
        Toolkit toolkit = Toolkit.getDefaultToolkit();  
        setCursor(toolkit.createCustomCursor(im.unit[1][9], new Point(0, 0), "Invisible"));
        
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
        
        //MouseHandler
        mh = new MouseHandler();
        this.addMouseListener(mh);
        this.addMouseMotionListener(mh);
        
        //Create Buttons
        gui.addButton("Attack", 0);
        gui.addButton("Nothing", 1);
        gui.addButton("Trade", 2);
        gui.addButton("Inventory", 3);
        gui.addButton("Skill", 4);
        gui.addButton("Cancel", 5);
        gui.addButton("Done", 6);
        gui.addButton("Reset Moves", 7);
        
        
        gui.addButton(544, 14, 140, 16, "Up", 9);
        gui.addButton(544, 378, 140, 16, "Down", 10);
        //gui.addButton(50, 425, 500, 125, "random button", 11);
    }
    
    //Starts the game
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
    
    //Stops the game
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
    
    //Loops while game is running
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
    
    //Updates the game
    public void tick()
    {
        for(Unit u : unitList) 
        {
            u.tick();
        }
        paths.tick();
        render();
    }
    
    //Renders everything
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
        map.render(g);
        paths.render(g);
        gui.render2(g);
        for(Unit u : unitList) {
            u.render(g);
        }
        gui.render3(g);
        
        g.dispose(); //Clean
        bs.show(); //Shows render
    }
    
    //Ends turn, resets all units' "moved" parameter
    public static void newTurn()
    {
        for(Unit u : unitList) 
        {
            u.reset();
        }
    }
    
    //Add a unit to the map
    public void addUnit(int x, int y, int type, int mov, int minRg, int maxRg, int team)
    {
        if(x >= 0 && y >= 0 && x < mapWidth && y < mapHeight && getUnit(x, y) == null)
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
    
    //Gets the unit at specified x, y
    public static Unit getUnit(int x, int y)
    {
        if(x >= 0 && y >= 0 && x < mapWidth && y < mapHeight)
        {
            return unitGrid[x][y];
        }
        //return null;
        return theUnit;
    }
    
    //Moves a unit to specified x, y from current x, y
    public static void moveUnit(int x, int y, Unit u)
    {
        unitGrid[u.getX()][u.getY()] = null;
        unitGrid[x][y] = u;
    }
    
    //Returns map grid
    public static int[][] getMap()
    {
        return map.getGrid();
    }
    
    //Main function, sets up game window
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