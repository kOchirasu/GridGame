package gridGame;

import character.Path;
import character.Unit;
import graphics.Image;
import graphics.ImageLoader;
import graphics.SpriteSheet;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import map.Map;
import map.MapLoader;

public class Game extends Canvas implements Runnable
{
    private static final long serialVersionUID = 1L;
    public static final int WIDTH = 700, HEIGHT = 575;
    public static int mapWidth, mapHeight;
    public static boolean running = false;
    public static Path paths;
    public Thread gameThread;
    
    private static Map map;
    private BufferedImage spriteMain, damageNum, gui;
    private Image im;
    
    private static final Unit[][] unitGrid = new Unit[16][12];
    private final ArrayList<Unit> unitList = new ArrayList<>();
    
    public void init()
    {
        MapLoader mloader = new MapLoader();
        map = mloader.load("/map.txt");
        mapWidth = map.getWidth();
        mapHeight = map.getHeight();
        ImageLoader loader = new ImageLoader();
        gui = loader.load("/gui.png");
        spriteMain = loader.load("/spritesheet.png");
        damageNum = loader.load("/number.png");
        SpriteSheet ss = new SpriteSheet(spriteMain);
        SpriteSheet ds = new SpriteSheet(damageNum);
        im = new Image(ss, ds);
        paths = new Path(im);
        
        addUnit(1, 0, 0);
        addUnit(1, 1, 0);
        addUnit(1, 2, 0);
        addUnit(0, 0, 0);
        addUnit(0, 3, 0);
        addUnit(5, 3, 0);
        
        MouseHandler mh = new MouseHandler();
        this.addMouseListener(mh);
        this.addMouseMotionListener(mh);
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
        final double maxTick = 20.0;
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
        g.drawImage(gui, 0, 0, null);
        paths.render(g);
        for(Unit u : unitList) {
            u.render(g);
        }
        
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