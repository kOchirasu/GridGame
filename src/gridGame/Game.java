package gridGame;

import algorithm.*;
import block.Unit;
import graphics.*;
import gui.Interface;
import item.Item;
import item.ItemLoader;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JFrame;
import map.*;

public class Game extends Canvas implements Runnable
{
    private static final long serialVersionUID = 1L; //I dont even know what this is
    /* Public Variables
    WIDTH, HEIGHT           - Width and Height of the game window
    MAPOFFX, MAPOFFY        - Map x, y offsets to shift map window
    TILESIZE                - Dimensions of a tile (Square)
    fieldWidth, fieldHeight - dimensions of map window
    gameSpeed               - fps of game (1 = 30fps, 2 = 60fps)
    xOff, yOff              - x and y offset for shifting map location
    running                 - True while the game is running
    map                     - Map class, contains map data
    paths                   - Pathfinding class
    gui                     - Game interface class
    gameThread              - Game thread
    lookup                  - Item database
    */
    public static final int WIDTH = 700, HEIGHT = 575, MAPOFFX = 9, MAPOFFY = 9, TILESIZE = 32;
    public static final int fieldWidth = 16, fieldHeight = 12, gameSpeed = 2;
    public static int xOff = 0, yOff = 0;
    public static boolean running = false;
    public static Map map;
    public static Path paths;
    public static Interface gui;
    public Thread gameThread;
    public static ItemLoader lookup;
    public static Sprite tempim;
    
    /* Private Variables
    im                      - Contains all pictures
    unitGrid                - Grid array that contains all units
    unitList                - List of all units
    */
    private Sprite im;
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
        tempim = im;
        
        //Temporarily used to write map files (Doesn't work i broke it)
        //MapEditor me = new MapEditor();
        //me.write();
        
        //Map Data
        MapLoader mloader = new MapLoader();
        //map = mloader.load("/map.txt", im);
        map = mloader.load("/test.mf", im);
        //map.printMap();
        
        //Unit Data
        unitList = new ArrayList<>();
        unitGrid = new Unit[map.width][map.height];
        
        //Etc. initializtion
        gui = new Interface(loader.load("/gui.png"), im);
        paths = new Path();
        lookup = new ItemLoader("jdbc:derby://localhost:1527/Item", "gridgame", "maplestory");
        
        //Item load test
        //Item test = new Item(lookup, 100001);
        //System.out.println(test.toString());
        
        //Custom cursor: creates invisible cursor which is redrawn in Interface
        Toolkit toolkit = Toolkit.getDefaultToolkit();  
        setCursor(toolkit.createCustomCursor(im.unit[1][9], new Point(0, 0), "Invisible"));
        
        //Create Units
        addUnit(1, 0, 0, -1, 0);
        addUnit(1, 1, 0, -1, 0);
        addUnit(1, 2, 0, -1, 0);
        addUnit(0, 0, 0, -1, 0);
        addUnit(0, 3, 0, -1, 0);
        addUnit(5, 3, 0, -1, 0);
        addUnit(5, 4, 0, -1, 0);
        addUnit(13, 3, 0, -1, 0);
        
        for(int i = 0; i < 17; i++)
        {
            addUnit(i, 11, 0, 50, 1);
        }
                
        //Mouse Handler
        MouseHandler mh = new MouseHandler();
        this.addMouseListener(mh);
        this.addMouseMotionListener(mh);
        
        //Key Handler
        KeyHandler kh = new KeyHandler();
        this.addKeyListener(kh);
        
        //Create Buttons
        /*gui.addButton("Attack", 0, 0);
        gui.addButton("Nothing", 1, 1);
        gui.addButton("Trade", 2, 2);
        gui.addButton("Inventory", 3, 3);
        gui.addButton("Skill", 4, 4);
        gui.addButton("Cancel", 5, 5);
        gui.addButton("Done", 6, 6);
        gui.addButton("Reset Moves", 7, 7);*/
        
        
        gui.addButton(544, 14, 140, 16, "Up", 9);
        gui.addButton(544, 378, 140, 16, "Down", 10);
        
        gui.addBar(10, 412, 250, 16, 0, new Color(200, 0, 0, 255));
        gui.addBar(10, 430, 250, 16, 1, new Color(0, 75, 255, 255));
        gui.addBar(10, 448, 250, 16, 2, new Color(255, 225, 0, 255));
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
            try {
                gameThread.join();
            } 
            catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
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
        //map.tick();
        gui.tick();
        paths.tick();
        for(int i = 0; i < unitList.size(); i++) {
            unitList.get(i).tick();
        }
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
        map.render(g);
        gui.render(g);
        paths.render(g);
        gui.render2(g);
        
        for(int i = 0; i < unitList.size(); i++) {
            unitList.get(i).render(g);
        }
        gui.render3(g);
        
        g.dispose(); //Clean
        bs.show(); //Shows render
    }
    
    //Ends turn, resets all units' "moved" parameter
    public static void newTurn()
    {
        for(int i = 0; i < unitList.size(); i++) //Loops through all units
        {
            unitList.get(i).reset();
        }
    }
    
    //Add a unit to the map
    public void addUnit(int x, int y, int type, int mov, int team)
    {
        if(inMap(x, y) && getUnit(x, y) == null) //Make sure adding unit within map grid, and that there is no unit currently there
        {
            Unit temp = new Unit(x, y, im, mov, team);
            unitGrid[x][y] = temp;
            unitList.add(temp);
        }
        else
        {
            System.out.println("Unable to add unit at (" + x + ", " + y + ") . Game.java @ Line " + Thread.currentThread().getStackTrace()[1].getLineNumber());
        }
    }
    
    //Gets the unit at specified x, y
    public static Unit getUnit(int x, int y)
    {
        if(inMap(x, y)) //Make sure unit is in map
        {
            return unitGrid[x][y];
        }
        return null;
    }
    
    //Moves a unit to specified x, y from current x, y
    public static void moveUnit(int x, int y, Unit u)
    {
        unitGrid[u.getX()][u.getY()] = null;
        unitGrid[x][y] = u;
    }
    
    public static void offset(int x, int y)
    {
        xOff = x >= 0 && x <= map.width - fieldWidth ? x : xOff;
        yOff = y >= 0 && y <= map.height - fieldHeight ? y : yOff;
    }
    
    //checks if x, y grid based coordinates are in the window
    public static boolean inGrid(int x, int y)
    {
        return x >= xOff && y >= yOff && x < fieldWidth + xOff && y < fieldHeight + yOff;
    }
    
    //checks if x, y grid based coordinates are in the map
    public static boolean inMap(int x, int y)
    {
        return x >= 0 && y >= 0 && x < map.width && y < map.height;
    }
    
    //some glitch with "theUnit" placeholder unit.
    public static void center(Unit unit)
    {
        int lX = unit.getX() - 4;
        int hX = unit.getX() + 4;
        int lY = unit.getY() - 4;
        int hY = unit.getY() + 4;
        
        int tX = xOff, tY = yOff;
        
        if(lX < xOff) {
            tX = lX < 0 ? 0 : lX;
        }
        if(hX > fieldWidth + xOff) {
            tX = hX > map.width ? map.width - fieldWidth : hX - fieldWidth;
        }
        
        if(lY < yOff) {
            tY = lY < 0 ? 0 : lY;
        }
        if(hY > fieldHeight + yOff) {
            tY = hY > map.height ? map.width - fieldHeight : hY - fieldHeight;
        }

        System.out.printf("Low x: %d\tHigh x: %d\tOffset x: %d\n", lX, hX, tX);
        System.out.printf("Low y: %d\tHigh y: %d\tOffset y: %d\n", lY, hY, tY);
        offset(tX, tY);
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