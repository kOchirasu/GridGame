package character;

import graphics.Image;
import gridGame.Game;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Path 
{
    private int x, y, MOV, RANGE;
    private int[][] movement;
    private ArrayList<int[]> pathList = new ArrayList<>();
    private ArrayList<int[]> attList = new ArrayList<>();
    private Image image;
    
    public Path(Image image)
    {
        this.image = image;
    }
    
    public void render(Graphics g)
    {
        if(pathList.size() > 0)
        {
            for(int[] i : pathList)
            {
                g.drawImage(image.sprite[1][1], i[0] * 32, i[1] * 32, 32, 32, null);
            }
        }
        if(attList.size() > 0)
        {
            for(int[] i : attList)
            {
                g.drawImage(image.sprite[1][0], i[0] * 32, i[1] * 32, 32, 32, null);
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
        this.RANGE = pathInfo[3];
        int dat[], able, dist;
        pathList.clear();
                
        movement = new int[Game.mapWidth][Game.mapHeight];
        Queue<int[]> check = new LinkedList<>();
        Queue checkAtt = new LinkedList();
        
        movement[x][y] = 0;
        //System.out.println("Labled: " + x + " & " + y + " as \t" + 0);
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
                if(able == 1)
                {
                    movement[dat[0]][dat[1]] = ++dat[2];
                    pathList.add(dat);
                    //System.out.println("Labled:" + dat[0] + " & " + dat[1] + " as \t" + dat[2]);
                    check.add(new int[]{dat[0] - 1, dat[1], dat[2]});
                    check.add(new int[]{dat[0], dat[1] - 1, dat[2]});
                    check.add(new int[]{dat[0] + 1, dat[1], dat[2]});
                    check.add(new int[]{dat[0], dat[1] + 1, dat[2]});
                }
                else if(able == 2)
                {
                    if(RANGE > 1)
                    {
                        check.add(new int[]{dat[0] - 1, dat[1], MOV + 1});
                        check.add(new int[]{dat[0], dat[1] - 1, MOV + 1});
                        check.add(new int[]{dat[0] + 1, dat[1], MOV + 1});
                        check.add(new int[]{dat[0], dat[1] + 1, MOV + 1});
                    }
                    movement[dat[0]][dat[1]] = MOV + 1;
                    attList.add(dat);
                }
            }
            else
            {
                if(able != 0)
                {
                    if(!checkAtt.contains(hash(dat)))
                    {
                        System.out.println(Arrays.toString(dat));
                        checkAtt.add(hash(dat));
                    }
                }
            }
            /*
            else if(dat[2] < MOV + RANGE)
            {
                if(able == 1)
                {
                    movement[dat[0]][dat[1]] = ++dat[2];
                    attList.add(dat);
                    //System.out.println("Labled:" + dat[0] + " & " + dat[1] + " as \t" + dat[2]);
                    check.add(new int[]{dat[0] - 1, dat[1], dat[2]});
                    check.add(new int[]{dat[0], dat[1] - 1, dat[2]});
                    check.add(new int[]{dat[0] + 1, dat[1], dat[2]});
                    check.add(new int[]{dat[0], dat[1] + 1, dat[2]});
                }
                else if(able == 2)
                {
                    printPaths();
                    System.out.println(movement[dat[0]][dat[1]]);
                        movement[dat[0]][dat[1]] = dat[2];
                        attList.add(dat);
                        check.add(new int[]{dat[0] - 1, dat[1], dat[2]});
                        check.add(new int[]{dat[0], dat[1] - 1, dat[2]});
                        check.add(new int[]{dat[0] + 1, dat[1], dat[2]});
                        check.add(new int[]{dat[0], dat[1] + 1, dat[2]});
                }
            }
            else if(dat[2] == MOV + RANGE && able == 1)
            {
                movement[dat[0]][dat[1]] = MOV + RANGE;
                attList.add(dat);
            }*/
        }
        System.out.println("Pringin");
    }
    
    private int hash(int[] i)
    {
        return i[0]*10000+i[1]*100+i[0];
    }
    
    private int moveable(int x, int y)
    {
        if(x >= 0 && y >= 0 && x < Game.mapWidth && y < Game.mapHeight && !(Math.abs(x - this.x) + Math.abs(y - this.y) > MOV + RANGE) && movement[x][y] == 0)
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