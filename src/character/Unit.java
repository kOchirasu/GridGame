package character;

import graphics.Sprite;
import gridGame.Game;
import java.awt.Graphics;
import java.util.ArrayList;

public class Unit
{
    private int x, y, dX, dY, n[] = new int[3], xOff, yOff, count;
    private int walkTick, dmgTick;
    private int dmglen = 0;
    private long timer;
    private int LVL, EXP, HP, maxHP, MP, maxMP, FTG, MOV, ATK, MATK, DEF, ACC, AVO, CRIT, minRANGE, maxRANGE, TEAM;
    
    private boolean dead = false, moved = false;
    private short ClassID;
    private ArrayList<int[]> walkList = new ArrayList<>();
    private Sprite sprite;
    
    public static final int spDIM = Sprite.spDIM;
    public boolean moving = false;
    
    public Unit(int x, int y, Sprite sprite, int MOV, int minRANGE, int maxRANGE, int TEAM)
    {
        this.x = x;
        this.y = y;
        this.dX = x * spDIM;
        this.dY = y * spDIM;
        this.sprite = sprite;
        ClassID = 0;
        this.MOV = MOV != -1 ? MOV : 5;
        this.minRANGE = minRANGE != -1 ? minRANGE : 1;
        this.maxRANGE = maxRANGE != -1 ? maxRANGE : 2;
        this.TEAM = TEAM;
    }
    
    public void tick()
    {
        if(dmglen > 0)
        {
            dmgTick++;
            if(dmgTick > 120)
            {
                dmglen = 0;
                dmgTick = 0;
            }
        }
        
        if(moving)
        {
            walkTick++;
            if(count >= walkList.size())
            {
                walkList.clear();
                moving = false;
                count = 0;
                //System.out.println("Finished moving");
            }
            else
            {
                xOff += 4 * (walkList.get(count)[0] - this.dX / spDIM);//
                yOff += 4 * (walkList.get(count)[1] - this.dY / spDIM);//
                //dX += 4 * (walkList.get(count)[0] - this.dX / spDIM);
                //dY += 4 * (walkList.get(count)[1] - this.dY / spDIM);
                if(walkTick >= 8)
                {
                    xOff = 0;//
                    yOff = 0;//
                    walkTick = 0;
                    this.dX = walkList.get(count)[0] * spDIM;
                    this.dY = walkList.get(count)[1] * spDIM;
                    //System.out.println("x: " + x + " y: " + y);
                    count++;
                }
            }
        }
        else if(walkList.size() > 1)
        {
            moving = true;
            //System.out.println("Started moving");
        }
    }
    
    public void render(Graphics g)
    {
        g.drawImage(sprite.image[0][TEAM], dX + xOff, dY + yOff, spDIM, spDIM, null);
        //g.drawImage(sprite.image[0][0], dX, dY, spDIM, spDIM, null);
        if(dmglen == 1)
        {
            g.drawImage(sprite.damage[n[0]], dX + xOff + 11, dY + yOff + 5, Sprite.dmwDIM, Sprite.dmhDIM, null);
        }
        else if(dmglen == 2)
        {
            g.drawImage(sprite.damage[n[1]], dX + xOff + 6, dY + yOff + 5, Sprite.dmwDIM, Sprite.dmhDIM, null);
            g.drawImage(sprite.damage[n[1]], dX + xOff + 16, dY + yOff + 5, Sprite.dmwDIM, Sprite.dmhDIM, null);
        }
        else if(dmglen == 3)
        {
            g.drawImage(sprite.damage[n[2]], dX + xOff + 1, dY + yOff + 5, Sprite.dmwDIM, Sprite.dmhDIM, null);
            g.drawImage(sprite.damage[n[1]], dX + xOff + 11, dY + yOff + 5, Sprite.dmwDIM, Sprite.dmhDIM, null);
            g.drawImage(sprite.damage[n[0]], dX + xOff + 21, dY + yOff + 5, Sprite.dmwDIM, Sprite.dmhDIM, null);
        }
    }
    
    public boolean move(int x, int y, ArrayList<int[]> walkList)
    {
        if(x >= 0 && y >= 0 && x < Game.mapWidth && y < Game.mapHeight)
        {
            if(Game.paths.getMove(x, y) <= MOV && Game.getUnit(x, y) == null)// && Game.paths.getMove(x, y) <= MOV && MOV != 0)
            {
                Game.moveUnit(this.x, this.y, x, y, this);
                this.walkList = walkList;
                //moveAni(walkList);
                this.x = x;
                this.y = y;
                moved = true;
                //System.out.println("Moved to Grid(" + this.x + ", " + this.y + ")");
            }
            else
            {
                //System.out.println("Invalid movement.");
            }
        }
        return false;
    }
    
    private void moveAni(ArrayList<int[]> walkList)
    {
        if(walkList.size() > 0)
        {
            for(int i = 1; i < walkList.size(); i++)
            {
                while(walkTick <= 32)
                {
                    System.out.println("Moving?");
                    xOff = walkTick;
                }
                walkTick = 0;
                xOff = 0;
                this.x = walkList.get(i)[0];
                this.y = walkList.get(i)[1];
            }
        }
        else
        {
            System.out.println("Size is 0");
        }
    }
    
    public int attack(Unit enemy)
    {
        int expGain = Game.math.expGain(LVL, enemy.getLVL());
        int tDmg = Game.math.dmgAmt(ATK, enemy.getDEF());
        enemy.damage(tDmg);
        return expGain;
    }
    
    public void damage(int dmg)
    {
        dmg = dmg > 999 ? 999 : dmg;
        HP -= dmg;
        if(HP <= 0)
        {
            //Kill unit
            dead = true;
        }
        dmglen = dmg < 10 ? 1 : dmg < 100 ? 2 : 3;
        n[0] = dmg % 10;
        dmg -= n[0];
        n[1] = (dmg % 100) / 10;
        dmg -= n[1] * 10;
        n[2] = dmg / 100;
        //System.out.println(n0 + " " + n1 + " " + n2);
        timer = System.currentTimeMillis();
    }
    
    public int[] pathInfo() {
        return new int[]{x, y, MOV, minRANGE, maxRANGE, TEAM};
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getLVL() {
        return LVL;
    }
    public int getEXP() {
        return EXP;
    }
    public int getHP() {
        return HP;
    }
    public int getMaxHP() {
        return maxHP;
    }
    public int getMP() {
        return MP;
    }
    public int getMaxMP() {
        return maxMP;
    }
    public int getFTG() {
        return FTG;
    }
    public int getMOV() {
        return MOV;
    }
    public int getATK() {
        return ATK;
    }
    public int getMATK() {
        return MATK;
    }
    public int getDEF() {
        return DEF;
    }
    public int getACC() {
        return ACC;
    }
    public int getAVO() {
        return AVO;
    }
    public int getCRIT() {
        return CRIT;
    }
    public int getMinRANGE() {
        return minRANGE;
    }
    public int getMaxRANGE() {
        return maxRANGE;
    }
    public int getTEAM() {
        return TEAM;
    }
}
