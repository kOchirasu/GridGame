package map;

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
    /*
    x, y        - x, y dimensions of the map
    grid        - Grid containing all tile information of map
    sprite      - Sprite array to load images from
    */
    public final int width, height;
    private int xShift, yShift;
    private int x, y;
    private byte[][] grid;
    private Sprite sprite;
    private boolean shifting;
    
    public int mX, mY, count;
    
    private int sX, sY; //Used for shifting map
    
    //Creates a map object and loads data into array
    //public Map(BufferedReader br, Sprite sprite) throws IOException
    public Map(byte[] data, Sprite sprite, BufferedReader br) throws IOException
    {
        this.sprite = sprite;

        x = width = data[0];
        y = height = data[1];
        grid = new byte[x][y];
        
        if(data.length < x * y + 2) {
            System.out.println("Invalid map file...");
            return;
        }
        for(int i = 0; i < y; i++)
        {
            for (int j = 0; j < x; j++)
            {
                //System.out.println("x:" + j + " y:" + i + " n:" + (i*x + j + 2));
                grid[j][i] = data[i*x + j + 2];

            }
        }
        x = y = 0;
    }
    
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
                    xShift = yShift = count = 0;;
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
                int n = getTile(j + Game.xOff, i + Game.yOff);
                if(n > 0 && n < 10) {
                    g.drawImage(sprite.tile[3][n], Game.MAPOFFX + j * Game.TILESIZE + xShift, Game.MAPOFFY + i * Game.TILESIZE + yShift, Game.TILESIZE, Game.TILESIZE, null);
                }
                else {
                    g.drawImage(sprite.tile[2][4], Game.MAPOFFX + j * Game.TILESIZE + xShift, Game.MAPOFFY + i * Game.TILESIZE + yShift, Game.TILESIZE, Game.TILESIZE, null);
                }
            }
        }
    }
    
    //Gets tile
    private byte getTile(int x, int y){
        return x >= 0 && y >= 0 && x < width && y < height ? grid[x][y] : -1;
    }
    //Returns map grid
    public byte[][] getGrid() {
        return grid;
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