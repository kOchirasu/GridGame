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
    private int x, y;
    private int[][] grid;
    private Sprite sprite;
    
    //Creates a map object and loads data into array
    public Map(BufferedReader br, Sprite sprite) throws IOException
    {
        this.sprite = sprite;
        
        String s = br.readLine(); //Read first line
        String[] dim = s.split(","); //Separate x & y coordinate values
        if(dim.length == 2) //Check that file is correctl formatted then assigns x & y values
        {
            x = Integer.parseInt(dim[0]);
            y = Integer.parseInt(dim[1]);
        }   
        //System.out.println("x:" + x + " y:" + y);
        grid = new int[x][y];
        s = br.readLine();
        String[] trans;

        for(int i = 0; i < y; i++)
        {
            if(s == null) break;
            trans = s.split(",");
            //System.out.println(trans.length);
            for (int j = 0; j < x; j++)
            {
                //System.out.println("i:" + i + " j:" + j + " n:" + Integer.parseInt(trans[j]));
                grid[j][i] = Integer.parseInt(trans[j]);

            }
            s = br.readLine();
        }
    }
    
    //Renders map tiles
    public void render(Graphics g)
    {
        for(int i = 0; i < y; i++)
        {
            for(int j = 0; j < x; j++)
            {
                switch(grid[j][i])
                {
                    case 1:
                        g.drawImage(sprite.tile[3][0], Game.MAPOFFX + j * Game.TILESIZE, Game.MAPOFFY + i * Game.TILESIZE, Game.TILESIZE, Game.TILESIZE, null);
                        break;
                    case 2:
                        g.drawImage(sprite.tile[3][1], Game.MAPOFFX + j * Game.TILESIZE, Game.MAPOFFY + i * Game.TILESIZE, Game.TILESIZE, Game.TILESIZE, null);
                        break;
                    default:
                        g.drawImage(sprite.tile[2][4], Game.MAPOFFX + j * Game.TILESIZE, Game.MAPOFFY + i * Game.TILESIZE, Game.TILESIZE, Game.TILESIZE, null);
                        break;
                }
            }
        }
    }
    
    //Returns map grid
    public int[][] getGrid() {
        return grid;
    }
    
    //Get Width/Height functions
    public int getWidth() {
        return x;
    }
    
    public int getHeight() {
        return y;
    }
    
    //Prints out map
    public void printMap()
    {
        System.out.println("Map Dimensions: [" + x + ", " + y + "]");
        for(int i = 0; i < y; i++)
        {
            System.out.print("[");
            for(int j = 0; j < x; j++)
            {
                System.out.print(grid[j][i] + ", ");
            }
            System.out.println("]");
        }
    }
}