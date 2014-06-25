package algorithm;

import gridGame.Game;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
/*
Make it so that you can walk through allied units (done)
Can also walk through enemy units but take damage from each unit that you walk over
*/
public class Path
{
    /*
    x, y            - x, y, grid coordinates of unit
    mov             - Movement range of unit
    team            - Team of unit
    tick            - Tick counter for animation
    anim            - Offset to shrink and grow tiles
    movenent        - movement array (0 = can't move, 99 = attackable/starting tile, else = moves needed to get to tile)
    pathList        - List of tiles unit can potentially walk to
    attList         - List of tiles unit can potentially attack
    walkList        - List of tiles unit will go through to get to destination
    pathColor       - Color of path tiles
    attColor        - Color of attack tiles
    walkColor       - Color of walk tiles
    font            - Font of number on tiles
    */
    private final int sOffsetA = (int) ((Game.TILESIZE / 2) * (3 / 8.0f)), sOffsetB = Game.TILESIZE / 2 - sOffsetA;
    private int x, y, mov, team, tick, anim;
    private int[][] movement;
    private ArrayList<int[]> pathList = new ArrayList<>();
    private ArrayList<int[]> attList = new ArrayList<>();
    private ArrayList<int[]> walkList = new ArrayList<>();
    private Color pathColor, attColor, walkColor;
    private Font font;
            
    public Path()
    {
        movement = new int[Game.mapWidth][Game.mapHeight];
        pathColor = new Color(0, 75, 255, 128);
        attColor = new Color(255, 0, 0, 128);
        walkColor = new Color(0, 255, 144, 128);
        font = new Font("Arial", Font.PLAIN, 15);
    }
    
    //Flashes path and attack grids
    public void tick()
    {
        tick++;
        if(tick == 30) {
            anim = sOffsetA;
        }
        else if(tick == 60) {
            anim = 0;
            tick = 0;
        }
    }
    
    //Renders movement and attack tiles
    public void render(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        Color shine = new Color(225, 225, 225, 150);
        int x, y;
        //Renders path (blue) tiles
        for(int i = 0; i < pathList.size(); i++)
        {
            x = Game.MAPOFFX + pathList.get(i)[0] * Game.TILESIZE + 1;
            y = Game.MAPOFFY + pathList.get(i)[1] * Game.TILESIZE + 1;
            GradientPaint gp = new GradientPaint(x + Game.TILESIZE + anim + sOffsetB, y - anim - sOffsetB, shine, x, y + Game.TILESIZE, pathColor, true);
            g2.setPaint(gp);
            g2.fillRect(x, y, Game.TILESIZE - 1, Game.TILESIZE - 1);
        }
        //Renders attack (red) tiles
        for(int i = 0; i < attList.size(); i++)
        {
            x = Game.MAPOFFX + attList.get(i)[0] * Game.TILESIZE + 1;
            y = Game.MAPOFFY + attList.get(i)[1] * Game.TILESIZE + 1;
            GradientPaint gp = new GradientPaint(x + Game.TILESIZE + anim + sOffsetB, y - anim - sOffsetB, shine, x, y + Game.TILESIZE, attColor, true);
            g2.setPaint(gp);
            g2.fillRect(x, y, Game.TILESIZE - 1, Game.TILESIZE - 1);
        }
        //Renders walking path
        g.setColor(walkColor);
        for(int i = 1; i < walkList.size(); i++)
        {
            g.fillRect(Game.MAPOFFX + walkList.get(i)[0] * Game.TILESIZE + 1, Game.MAPOFFY + walkList.get(i)[1] * Game.TILESIZE + 1, Game.TILESIZE - 1, Game.TILESIZE - 1);
        }
        g.setColor(Color.BLACK);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics(font); 
        for(int i = 1; i < walkList.size(); i++)
        {
            g.drawString("" + i, (int) (Game.MAPOFFX + walkList.get(i)[0] * Game.TILESIZE + Game.TILESIZE/2 - fm.getStringBounds("" + i, g).getWidth()/2), (int) (Game.MAPOFFY + walkList.get(i)[1] * Game.TILESIZE + Game.TILESIZE/2 + fm.getStringBounds("" + i, g).getHeight()/4));
        }
        
    }
    
    //Clears movement and attack tiles
    public void clearPaths()
    {
        pathList.clear();
        attList.clear();
        walkList.clear();
    }
    
    //Calculates movement and attack tiles
    public void findPath(int[] pathInfo)
    {
    //Initialize 
        this.x = pathInfo[0];
        this.y = pathInfo[1];
        this.mov = pathInfo[2];
        this.team = pathInfo[5];
        
        int dat[];
        Queue<int[]> check = new LinkedList<>();
        
    //Reset everything
        clearPaths();
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
            if(dat[2] < mov)
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
        addAttack(x, y, pathInfo[3], pathInfo[4]);
        //Check using all possible movement tiles
        for(int[] k : pathList) {
            if(Game.getUnit(k[0], k[1]) == null) {
                addAttack(k[0], k[1], pathInfo[3], pathInfo[4]);
            }
        }
        
        walkList.add(new int[]{x, y});
    }
    
    //Adds all attackable tiles from specified x, y for specified range
    private void addAttack(int x, int y, int minRange, int maxRange)
    {
        int nX, nY;
        for(int i = -maxRange; i <= maxRange; i++) {
            for(int j = Math.abs(i) - maxRange; j <= maxRange - Math.abs(i); j++) {
                nX = x + i;
                nY = y + j;
                if(nX >= 0 && nX < Game.mapWidth && nY >= 0 && nY < Game.mapHeight) {
                    if(Math.abs(i) + Math.abs(j) >= minRange) {
                        if(movement[nX][nY] == 0) {
                            attList.add(new int[]{nX, nY});
                            movement[nX][nY] = 99;
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
            if((Game.getUnit(x, y) == null || Game.getUnit(x, y).getTEAM() == team) && movement[x][y] == 0 && Game.getMap()[x][y] == 1) //If tile is open
            {
                return true;
            }
        }  
        return false;
    }
    
    //Add tile to user selected walk path
    public void addPath(int x, int y)
    {
        if(x >= 0 && y >= 0 && x < Game.mapWidth && y < Game.mapHeight)
        {
            if(movement[x][y] < 1 || movement[x][y] == 99)
            {
                //System.out.println("Invalid case: 1\t not a valid movement tile");
                repath(x, y);
            }
            else if(walkList.size() > 1 && walkList.get(walkList.size() -  2)[0] == x && walkList.get(walkList.size() -  2)[1] == y)
            {
                //System.out.println("Invalid case: 2\t removed last move");
                walkList.remove(walkList.size() -  1);
            }
            else if(walkList.size() > mov)
            {
                //System.out.println("Invalid case: 3\t walkList.size() = " + walkList.size() + " > mov = " + mov);
                repath(x, y);
            }
            else if(walkList.size() > 0 && Math.abs(x - walkList.get(walkList.size() - 1)[0]) + Math.abs(y - walkList.get(walkList.size() - 1)[1]) != 1)
            {
                //System.out.println("Invalid case: 4\t distance > 1");
                repath(x, y);
            }
            else
            {
                for(int i = 0; i < walkList.size() - 2; i++)
                {
                    if(walkList.get(i)[0] == x && walkList.get(i)[1] == y)
                    {
                        //System.out.println("Invalid case: 5\t duplicate list entries");
                        repath(x, y);
                        return;
                    }
                }
                //System.out.println("Added: (" + x + ", " + y + ")");
                walkList.add(new int[]{x, y});
            }
        }
        else
        {
            walkList.clear();
            walkList.add(new int[]{this.x, this.y});
        }
    }
    
    //Returns walk path
    public ArrayList<int[]> getWalk() {
        return walkList;
    }
    
    //Automatically calculated walk path
    private void repath(int x, int y)
    {
        walkList.clear();
        //System.out.println("walkList CLEARED");
        //System.out.println("Added: (" + this.x + ", " + this.y + ")");
        walkList.add(new int[]{this.x, this.y});
        int cur = movement[x][y];
        if(cur > 0 && cur != 99)
        {
            //System.out.println("cur:" + cur + "\tx: " + x + ", y: " + y);
            
            for(int i = cur - 1; i >= 0; i--)
            {
                //System.out.println("Added: (" + x + ", " + y + ")");
                walkList.add(1, new int[]{x, y});
                if(x > 0 && movement[x - 1][y] == i) {
                    x--;  
                }
                else if(y > 0 && movement[x][y - 1] == i) {
                    y--;
                }
                else if(x < Game.mapWidth - 1 && movement[x + 1][y] == i) {
                    x++;
                }
                else if(y < Game.mapHeight - 1 && movement[x][y + 1] == i) {
                    y++;
                }
            }
        }
    }
    
    //Returns a specified index of the movement array
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
