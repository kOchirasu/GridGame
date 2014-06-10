package map;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
0 = Wall, cannot go over, cannot telepot over, can never go past!
1 = Walkway, can walk on
2 = Wall, can teleport over
3 = Water?
4 = Mountains?
*/
public class Map 
{
    private int x, y;
    private int[][] grid;
    public Map(String file) throws IOException
    {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(getClass().getResource(file).toURI())); //Some crap to read file
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String s = br.readLine(); //Read first line
            String[] dim = s.split(","); //Separate x & y coordinate values
            if(dim.length == 2) //Check that file is correctl formatted then assigns x & y values
            {
                x = Integer.parseInt(dim[0]);
                y = Integer.parseInt(dim[1]);
            }   
            //System.out.println("x:" + x + " y:" + y);
            grid = new int[y][x];
            s = br.readLine();
            String[] trans = new String[x];
            
            for(int i = 0; i < y; i++)
            {
                if(s == null) break;
                trans = s.split(",");
                //System.out.println(trans.length);
                for (int j = 0; j < trans.length; j++)
                {
                    //System.out.println("i:" + i + " j:" + j + " n:" + Integer.parseInt(trans[j]));
                    grid[i][j] = Integer.parseInt(trans[j]);
                    
                }
                s = br.readLine();
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
    }
    
    public int[][] getGrid()
    {
        return grid;
    }
    
    public int getWidth()
    {
        return y;
    }
    
    public int getHeight()
    {
        return x;
    }
    
    /*public static void main(String[] args) throws IOException
    {
        Map test = new Map("/map.txt");
        for(int i = 0; i < test.y; i++)
        {
            System.out.println(Arrays.toString(test.grid[i]));
        }
    }*/
}