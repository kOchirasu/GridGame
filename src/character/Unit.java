package character;

import graphics.SpriteSheet;
import gridGame.Game;
import java.awt.Graphics;

public class Unit
{
    private int pX, pY; //Pixel based x, y
    private int x, y;
    private int LVL, EXP, HP, maxHP, MP, maxMP, ATK, MATK, DEF, ACC, AVO, CRIT;
    private boolean dead = false;
    private int ClassID;
    private SpriteSheet ss;
    
    public Unit(int x, int y, SpriteSheet ss)
    {
        this.pY = x;
        this.pY = y;
        this.ss = ss;
    }
    
    public void tick()
    {
        
    }
    
    public void render(Graphics g)
    {
        g.drawImage(ss.crop(0, 0, 16, 16), pX, pY, 16 * Game.SCALE, 16 * Game.SCALE, null);
    }
    
    public boolean move(int x, int y)
    {
        if(x >= 0 && y >= 0)
        {
            pX = 16 * Game.SCALE * x;
            pY = 16 * Game.SCALE * y;
            this.x = x;
            this.y = y;
            System.out.println("Moved to: " + pX + ", " + pY);
            System.out.println("Grid(" + this.x + ", " + this.y + ")");
        }
        return false;
    }
    
    public boolean move(int d)
    {
        if(d == 0) //Up
        {
            move(x, y - 1);
        }
        else if(d == 1) //Right
        {
            move(x + 1, y);
        }
        else if(d == 2) //Down
        {
            move(x, y + 1);
        }
        else if(d == 3) //Left
        {
            move(x - 1, y);
        }
        return false;
    }
    
    public int Attack(Unit atkd)
    {
        int expGain = 1; //EXP Gain Formula
        int tDmg = ATK-atkd.getDEF(); //Damage Formula
        atkd.Damage(tDmg);
        return expGain;
    }
    
    public void Damage(int dmg)
    {
        HP -= dmg;
        if(HP <= 0)
        {
            //Kill unit
        }
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
}
