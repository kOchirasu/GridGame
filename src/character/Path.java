package character;

import graphics.Sprite;
import gridGame.Game;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
/*
Make it so that you can walk through allied units
Can also walk through enemy units but take damage from each unit that you walk over

*/
public class Path
{
    private int x, y, MOV, minRANGE, maxRANGE;
    private int[][] movement;
    private ArrayList<int[]> pathList = new ArrayList<>();
    private ArrayList<int[]> attList = new ArrayList<>();
    private Sprite sprite;
    
    public Path(Sprite sprite)
    {
        this.sprite = sprite;
        movement = new int[Game.mapWidth][Game.mapHeight];
    }
    
    public synchronized void render(Graphics g)
    {
        if(pathList.size() > 0)
        {
            for(int[] i : pathList)
            {
                g.drawImage(sprite.image[1][1], i[0] * Sprite.spDIM, i[1] * Sprite.spDIM, Sprite.spDIM, Sprite.spDIM, null);
            }
        }
        if(attList.size() > 0)
        {
            for(int[] i : attList)
            {
                g.drawImage(sprite.image[1][0], i[0] * Sprite.spDIM, i[1] * Sprite.spDIM, Sprite.spDIM, Sprite.spDIM, null);
            }
        }
    }
    
    public void clearPaths()
    {
        pathList.clear();
        attList.clear();
    }
    
    public void getPaths(int[] pathInfo)
    {
        this.x = pathInfo[0];
        this.y = pathInfo[1];
        this.MOV = pathInfo[2];
        this.minRANGE = pathInfo[3];
        this.maxRANGE = pathInfo[4];
        int dat[] = new int[]{0, 0, 0}, able;
        pathList.clear();
        attList.clear();
        
        for(int i = 0; i < Game.mapWidth; i++) {
            for(int j = 0; j < Game.mapHeight; j++) {
                movement[i][j] = 0;
            }
        }
        Queue<int[]> check = new LinkedList<>();
        Queue<int[]> checkAtt = new LinkedList<>();
        Queue<int[]> tempQueue = new LinkedList<>();
        
        check.add(new int[]{x - 1, y, 0});
        check.add(new int[]{x, y - 1, 0});
        check.add(new int[]{x + 1, y, 0});
        check.add(new int[]{x, y + 1, 0});
        while(check.peek() != null)
        {
            dat = check.remove();
            able = moveable(dat[0], dat[1]);
            if(dat[2] < MOV)
            {
                if(able == 1) //tile is empty
                {
                    movement[dat[0]][dat[1]] = ++dat[2];
                    pathList.add(dat);
                    //System.out.println("Labled:" + dat[0] + " & " + dat[1] + " as \t" + dat[2]);
                    if(dat[2] < MOV)
                    {
                        check.add(new int[]{dat[0] - 1, dat[1], dat[2]});
                        check.add(new int[]{dat[0], dat[1] - 1, dat[2]});
                        check.add(new int[]{dat[0] + 1, dat[1], dat[2]});
                        check.add(new int[]{dat[0], dat[1] + 1, dat[2]});
                    }
                    else
                    {
                        checkAtt.add(new int[]{dat[0] - 1, dat[1], dat[2]});
                        checkAtt.add(new int[]{dat[0], dat[1] - 1, dat[2]});
                        checkAtt.add(new int[]{dat[0] + 1, dat[1], dat[2]});
                        checkAtt.add(new int[]{dat[0], dat[1] + 1, dat[2]});
                    }
                }
                else if(able == 2) //Unit in checked tile
                {
                    if(MOV >= maxRANGE)
                    {
                        checkAtt.add(new int[]{dat[0] - 1, dat[1], MOV + 1});
                        checkAtt.add(new int[]{dat[0], dat[1] - 1, MOV + 1});
                        checkAtt.add(new int[]{dat[0] + 1, dat[1], MOV + 1});
                        checkAtt.add(new int[]{dat[0], dat[1] + 1, MOV + 1});
                    }
                    movement[dat[0]][dat[1]] = MOV + 1;
                    tempQueue.add(dat);
                    //System.out.println("Added unit at (" + dat[0] + ", " + dat[1] + ") to tempQueue.");
                }
            }
        }
        int maxwalk = dat[2];
        
        if(checkAtt.peek() == null)
        {
            checkAtt.add(new int[]{x - 1, y, MOV + 1});
            checkAtt.add(new int[]{x, y - 1, MOV + 1});
            checkAtt.add(new int[]{x + 1, y, MOV + 1});
            checkAtt.add(new int[]{x, y + 1, MOV + 1});
        }
        while(checkAtt.peek() != null)
        {
            dat = checkAtt.remove();
            able = moveable(dat[0], dat[1]);
            if(able > 0)
            {
                dat[2] = minSur(dat[0], dat[1]) + 1;
                //System.out.println("Min moves: " + dat[2]);
                movement[dat[0]][dat[1]] = dat[2];
                if(dat[2] == minRANGE - 1) {
                    tempQueue.add(dat);
                }
                if(dat[2] >= minRANGE && dat[2] <= MOV + maxRANGE) {
                    attList.add(dat);
                }
                if(dat[2] < MOV + maxRANGE)
                {
                    checkAtt.add(new int[]{dat[0] - 1, dat[1], dat[2]});
                    checkAtt.add(new int[]{dat[0], dat[1] - 1, dat[2]});
                    checkAtt.add(new int[]{dat[0] + 1, dat[1], dat[2]});
                    checkAtt.add(new int[]{dat[0], dat[1] + 1, dat[2]});
                }
            }
        }
        
        if(maxwalk < MOV || maxwalk < minRANGE)
        {
            //System.out.println("hit checking!");
            while(tempQueue.peek() != null)
            {
                dat = tempQueue.remove();
                if(hitable(dat[0], dat[1])) {
                    attList.add(dat);
                }
                //System.out.println(Arrays.toString(dat));
            }
        }
        else
        {
            while(tempQueue.peek() != null) {
                attList.add(tempQueue.remove());
            }
        }
    }
    
    private int minSur(int x, int y)
    {
        return Math.max(Math.min(Math.min(getMove(x - 1, y), getMove(x, y - 1)), Math.min(getMove(x + 1, y), getMove(x, y + 1))), 0);
    }
    
    public int getMove(int x, int y)
    {
        if(x >= 0 && y >= 0 && x < Game.mapWidth && y < Game.mapHeight) {
            if(movement[x][y] != 0) {
                return movement[x][y];
            }
        }
        return 2147483647;
    }

    private int moveable(int x, int y)
    {
        if(x >= 0 && y >= 0 && x < Game.mapWidth && y < Game.mapHeight)
        {
            if(!(Math.abs(x - this.x) + Math.abs(y - this.y) > MOV + maxRANGE) && movement[x][y] == 0)
            {
                if(Game.getUnit(x, y) == null)
                {
                    if(Game.getMap()[x][y] == 1) {
                        return 1;
                    }
                }
                else
                {
                    if(this.x != x || this.y != y) {
                        return 2;
                    }
                }
            }
            return 0;
        }  
        return -1;
    }
    
    private boolean hitable(int x, int y)
    {
        //System.out.println("Distance between (" + x + ", " + y + ") & (" + this.x + ", " + this.y + ")\t" + (Math.abs(this.x - x) + Math.abs(this.y - y)));
        if(Math.abs(this.x - x) + Math.abs(this.y - y) >= minRANGE) {
            //System.out.println("Success");
            return true;
        }
        for(int[] i : pathList)
        {
            //System.out.println("Distance between (" + x + ", " + y + ") & (" + i[0] + ", " + i[1] + ")\t" + (Math.abs(i[0] - x) + Math.abs(i[1] - y)));
            if(Math.abs(i[0] - x) + Math.abs(i[1] - y) >= minRANGE) {
                //System.out.println("Success");
                return true;
            }
        }
        return false;
    }
    
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
