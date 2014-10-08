package map;

import block.Tile;
import block.Unit;
import graphics.Sprite;
import gridGame.Game;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;

/*
0 = Wall, cannot go over, cannot telepot over, can never go past! Endless pits/Edge of world
1 = Walkway, can walk on
2 = Wall, can teleport over
3 = Water?
4 = Mountains?
*/
public class Map 
{
    /* Private Variables
    xShift, yShift      - Used to animate map shift
    x, y                - Used to determine which parts of map to render
    sX, sY              - Determines the direction the map is shifting in
    grid                - Grid containing all tile information of map
    sprite              - Sprite array to load images from
    shifting            - True while the map is animating a shift
    */
    private int xShift, yShift;
    private int x, y;
    private int sX, sY;
    private Tile[][] grid;
    private Sprite sprite;
    private boolean shifting;
    
    /* Public Variables
    width, height       - Width and height of the map
    mX, mY              - The amount of pixels the map will shift every frame
    count               - Count used for animation
    */
    public final int width, height;
    public int mX, mY, count;
    
    //Creates a map object and loads data into array
    //public Map(BufferedReader br, Sprite sprite) throws IOException
    public Map(byte[] data, Sprite sprite, BufferedReader br) throws IOException
    {
        this.sprite = sprite;

        x = width = data[0];
        y = height = data[1];
        grid = new Tile[x][y];
        
        if(data.length < x * y + 2) {
            System.out.println("Invalid map file...");
            return;
        }
        for(int i = 0; i < y; i++)
        {
            for (int j = 0; j < x; j++)
            {
                //System.out.println("x:" + j + " y:" + i + " n:" + data[i*x + j + 2]);//(i*x + j + 2));
                grid[j][i] = new Tile(j, i, data[i*x + j + 2]);

            }
        }
        x = y = 0;
    }
    
    //Tick used for animation
    public void tick()
    {
        if(shifting)
        {
            count++;
            if(Game.xOff != x || Game.yOff != y)
            {                
                if(sX * (xShift + mX) > Game.TILESIZE)
                {
                    x -= sX;
                    xShift -= sX * Game.TILESIZE;
                }
                else
                {
                    xShift += mX;
                }
                
                if(sY * (yShift + mY) > Game.TILESIZE)
                {
                    y -= sY;
                    yShift -= sY * Game.TILESIZE;
                }
                else
                {
                    yShift += mY;
                }
                //System.out.println(yShift);
                
                if(count >= Game.TILESIZE)
                {
                    xShift = yShift = count = 0;
                    x = Game.xOff;
                    y = Game.yOff;
                    shifting = false;
                }
            }
            //System.out.println("shifting");
        }
        else if(Game.xOff != x || Game.yOff != y)
        {
            shifting = true;
            mX = x - Game.xOff;
            //System.out.println("mX: " + mX);
            mY = y - Game.yOff;
            //System.out.println("mY: " + mY);
            sX = x - Game.xOff < 0 ? -1 : 1;
            sY = y - Game.yOff < 0 ? -1 : 1;
        }
    }
    
    //Renders map tiles
    public void render(Graphics g)
    {
        for(int i = -1; i < Game.fieldHeight + 1; i++)//needs to be -1 and 1 to prevent ghosting
            {
            for(int j = -1; j < Game.fieldWidth + 1; j++)
            {
                //int n = getTile(j + x, i + y);
                Tile t = getTile(j + Game.xOff, i + Game.yOff);
                if (t != null && t.getType() > 0 && t.getType() < 10) {
                    //System.out.println("x:" + j + " y:" + i + " n:" + t.getType());
                    g.drawImage(sprite.tile[3][t.getType()], Game.MAPOFFX + j * Game.TILESIZE + xShift, Game.MAPOFFY + i * Game.TILESIZE + yShift, Game.TILESIZE, Game.TILESIZE, null);
                }
                else {
                    g.drawImage(sprite.tile[2][4], Game.MAPOFFX + j * Game.TILESIZE + xShift, Game.MAPOFFY + i * Game.TILESIZE + yShift, Game.TILESIZE, Game.TILESIZE, null);
                }
            }
        }
    }
    
    //Gets tile
    private Tile getTile(int x, int y){
        return x >= 0 && y >= 0 && x < width && y < height ? grid[x][y] : null;
    }
    //Returns map grid
    public Tile[][] getGrid() {
        return grid;
    }
    
    public boolean addUnit(int x, int y, Unit u)
    {
        return grid[x][y].addUnit(u);
    }
    
    public void setUnit(int x, int y, Unit u)
    {
        grid[x][y].setUnit(u);
    }
    
    //Gets the unit at specified x, y
    public Unit getUnit(int x, int y)
    {
        if(inMap(x, y)) //Make sure unit is in map
        {
            return grid[x][y].getUnit();
        }
        return null;
    }
    
    private boolean inMap(int x, int y)
    {
        return x >= 0 && y >= 0 && x < width && y < height;
    }
    
    //Prints out map
    public void printMap()
    {
        System.out.println("Map Dimensions: [" + x + ", " + y + "]");
        for(int i = 0; i < height; i++)
        {
            System.out.print("[");
            for(int j = 0; j < width; j++)
            {
                System.out.print(grid[j][i] + ", ");
            }
            System.out.println("]");
        }
    }
}