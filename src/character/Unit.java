package character;

import graphics.Sprite;
import gridGame.Game;
import java.awt.Graphics;

public class Unit
{
    private int x, y, n[] = new int[3];
    private int dmglen = 0;
    private long timer;
    private int LVL, EXP, HP, maxHP, MP, maxMP, FTG, MOV, ATK, MATK, DEF, ACC, AVO, CRIT, minRANGE, maxRANGE;
    
    private boolean dead = false;
    private short ClassID;
    private Sprite sprite;
    
    public static final int spDIM = Sprite.spDIM;
    
    public Unit(int x, int y, Sprite sprite)
    {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
        ClassID = 0;
        this.MOV = 5;
        this.minRANGE = 1;
        this.maxRANGE = 2;
    }
    
    public void tick()
    {
        if(System.currentTimeMillis() - timer >= 3000)
        {
            dmglen = 0;
        }
    }
    
    public void render(Graphics g)
    {
        g.drawImage(sprite.image[0][0], x * spDIM, y * spDIM, spDIM, spDIM, null);
        if(dmglen == 1)
        {
            g.drawImage(sprite.damage[n[0]], x * spDIM + 11, y * spDIM + 5, Sprite.dmwDIM, Sprite.dmhDIM, null);
        }
        else if(dmglen == 2)
        {
            g.drawImage(sprite.damage[n[1]], x * spDIM + 6, y * spDIM + 5, Sprite.dmwDIM, Sprite.dmhDIM, null);
            g.drawImage(sprite.damage[n[1]], x * spDIM + 16, y * spDIM + 5, Sprite.dmwDIM, Sprite.dmhDIM, null);
        }
        else if(dmglen == 3)
        {
            g.drawImage(sprite.damage[n[2]], x * spDIM + 1, y * spDIM + 5, Sprite.dmwDIM, Sprite.dmhDIM, null);
            g.drawImage(sprite.damage[n[1]], x * spDIM + 11, y * spDIM + 5, Sprite.dmwDIM, Sprite.dmhDIM, null);
            g.drawImage(sprite.damage[n[0]], x * spDIM + 21, y * spDIM + 5, Sprite.dmwDIM, Sprite.dmhDIM, null);
        }
    }
    
    public boolean move(int x, int y)
    {
        if(x >= 0 && x < Game.mapWidth && y >= 0 && y < Game.mapHeight)
        {
            if(Game.paths.getMove(x, y) <= MOV)//Game.getUnit(x, y) == null && Game.paths.getMove(x, y) <= MOV && MOV != 0)
            {
                Game.moveUnit(this.x, this.y, x, y, this);
                this.x = x;
                this.y = y;
                System.out.println("Moved to Grid(" + this.x + ", " + this.y + ")");
            }
            else
            {
                System.out.println("Invalid movement.");
            }
        }
        return false;
    }
    
    public int attack(Unit enemy)
    {
        int expGain = Game.math.expGain(LVL, enemy.getLVL());
        int tDmg = Game.math.damage(ATK, enemy.getDEF());
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
        return new int[]{x, y, MOV, minRANGE, maxRANGE};
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
}
