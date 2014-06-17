package algorithm;

import graphics.Sprite;
import gridGame.Game;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
/*
Make it so that you can walk through allied units
Can also walk through enemy units but take damage from each unit that you walk over

*/

/* Potential approach note
Normal walk path searching, easy already done.
Get a list of all of the edges
label all of the tiles that are maxRANGE away from edges
run hitable on all remaining tiles between walk path and maxRANGE.
*/
public class Path
{
    private int x, y;
    private int[][] movement;
    private ArrayList<int[]> pathList = new ArrayList<>();
    private ArrayList<int[]> attList = new ArrayList<>();
    private Sprite sprite;
            
    public Path(Sprite sprite)
    {
        this.sprite = sprite;
        movement = new int[Game.mapWidth][Game.mapHeight];
    }
    
    //Renders movement and attack tiles
    public void render(Graphics g)
    {
        //Renders path (blue) tiles
        if(pathList.size() > 0)
        {
            for(int i = 0; i < pathList.size(); i++)
            {
                g.drawImage(sprite.image[1][1], pathList.get(i)[0] * Sprite.spDIM, pathList.get(i)[1] * Sprite.spDIM, Sprite.spDIM, Sprite.spDIM, null);
            }
        }
        //Renders attack (red) tiles
        if(attList.size() > 0)
        {
            for(int i = 0; i < attList.size(); i++)
            {
                g.drawImage(sprite.image[1][0], attList.get(i)[0] * Sprite.spDIM, attList.get(i)[1] * Sprite.spDIM, Sprite.spDIM, Sprite.spDIM, null);
            }
        }
    }
    
    //Clears movement and attack tiles
    public void clearPaths()
    {
        pathList.clear();
        attList.clear();
    }
    
    //Calculates movement and attack tiles
    public void getPaths(int[] pathInfo)
    {
    //Initialize 
        this.x = pathInfo[0];
        this.y = pathInfo[1];
        
        int dat[], nX, nY;
        Queue<int[]> check = new LinkedList<>();
        
    //Reset everything
        pathList.clear();
        attList.clear();
        for(int i = 0; i < Game.mapWidth; i++) {
            for(int j = 0; j < Game.mapHeight; j++) {
                movement[i][j] = 0;
            }
        }
        
    //Finds all possible movement tiles
        //Initial add of four surrounding tiles
        check.add(new int[]{x - 1, y, 0});
        check.add(new int[]{x, y - 1, 0});
        check.add(new int[]{x + 1, y, 0});
        check.add(new int[]{x, y + 1, 0});
        
        //while queue still has tiles to check
        while(check.peek() != null)
        {
            dat = check.remove();
            if(dat[2] < pathInfo[2])
            {
                if(moveable(dat[0], dat[1])) //Adds to path if unit can move to tile
                {
                    movement[dat[0]][dat[1]] = ++dat[2];
                    //Adds four surrounding tiles of previously added tile
                    check.add(new int[]{dat[0] - 1, dat[1], dat[2]});
                    check.add(new int[]{dat[0], dat[1] - 1, dat[2]});
                    check.add(new int[]{dat[0] + 1, dat[1], dat[2]});
                    check.add(new int[]{dat[0], dat[1] + 1, dat[2]});
                    pathList.add(dat);
                }
            }
        }
        
    //Loops through all possible movement tiles and adds all possible attack tiles
        movement[x][y] = 99; //Sets unit location to 99 to prevent use
        
        //Check from starting tile
        for(int i = -pathInfo[4]; i <= pathInfo[4]; i++) {
            for(int j = Math.abs(i) - pathInfo[4]; j <= pathInfo[4] - Math.abs(i); j++) {
                nX = x + i;
                nY = y + j;
                if(nX >= 0 && nX < Game.mapWidth && nY >= 0 && nY < Game.mapHeight) {
                    if(Math.abs(i) + Math.abs(j) >= pathInfo[3]) {
                        if(movement[nX][nY] == 0) {
                            attList.add(new int[]{nX, nY});
                            movement[nX][nY] = 99;
                        }
                    }
                }
            }
        }
        
        //Check using all possible movement tiles
        for(int[] k : pathList) {
            for(int i = -pathInfo[4]; i <= pathInfo[4]; i++) {
                for(int j = Math.abs(i) - pathInfo[4]; j <= pathInfo[4] - Math.abs(i); j++) {
                    nX = k[0] + i;
                    nY = k[1] + j;
                    if(nX >= 0 && nX < Game.mapWidth && nY >= 0 && nY < Game.mapHeight) {
                        if(Math.abs(i) + Math.abs(j) >= pathInfo[3]) {
                            if(movement[nX][nY] == 0) {
                                attList.add(new int[]{nX, nY});
                                movement[nX][nY] = 99;
                            }
                        }
                    }
                }
            }
        }
    }
    
    //Checks if a tile can be moved to
    private boolean moveable(int x, int y)
    {
        if(x >= 0 && y >= 0 && x < Game.mapWidth && y < Game.mapHeight) //If within board
        {
            if(Game.getUnit(x, y) == null && movement[x][y] == 0 && Game.getMap()[x][y] == 1) //If tile is open
            {
                return true;
            }
        }  
        return false;
    }
    
    //Gets a specified index of the movement array
    public int getMove(int x, int y)
    {
        if(x >= 0 && y >= 0 && x < Game.mapWidth && y < Game.mapHeight) {
            if(movement[x][y] != 0) {
                return movement[x][y];
            }
        }
        return 2147483647;
    }
    
    //Prints out the movement array
    public void printPaths()
    {
        for(int i = 0; i < Game.mapHeight; i++)
        {
            System.out.print("[");
            for(int j = 0; j < Game.mapWidth; j++)
            {
                if(movement[j][i] > 0) {
                    System.out.print(movement[j][i] + ",\t");
                }
                else if(movement[j][i] == 0) {
                    System.out.print("■■■■,\t");
                }
                else {
                    System.out.print("0,\t");
                }
            }
            System.out.println("]");
        }
    }
}
