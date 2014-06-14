package character;

import graphics.Image;
import gridGame.Game;
import java.awt.Graphics;

public class Unit
{
    private int x, y, n0, n1, n2;
    private int dmglen = 0;
    private long timer;
    private int LVL, EXP, HP, maxHP, MP, maxMP, FTG, MOV, ATK, MATK, DEF, ACC, AVO, CRIT, minRANGE = 1, maxRANGE = 2;
    
    private boolean dead = false;
    private short ClassID;
    private Image image;
    
    public Unit(int x, int y, Image image)
    {
        this.x = x;
        this.y = y;
        this.image = image;
        ClassID = 0;
        this.MOV = 5;
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
        g.drawImage(image.sprite[0][0], x * 32, y * 32, 32, 32, null);
        if(dmglen == 1)
        {
            g.drawImage(image.damage[n0], x * 32 + 11, y * 32 + 5, 10, 14, null);
        }
        else if(dmglen == 2)
        {
            g.drawImage(image.damage[n1], x * 32 + 6, y * 32 + 5, 10, 14, null);
            g.drawImage(image.damage[n0], x * 32 + 16, y * 32 + 5, 10, 14, null);
        }
        else if(dmglen == 3)
        {
            g.drawImage(image.damage[n2], x * 32 + 1, y * 32 + 5, 10, 14, null);
            g.drawImage(image.damage[n1], x * 32 + 11, y * 32 + 5, 10, 14, null);
            g.drawImage(image.damage[n0], x * 32 + 21, y * 32 + 5, 10, 14, null);
        }
    }
    
    public boolean move(int x, int y)
    {
        if(x >= 0 && x < 16 && y >= 0 && y < 12)
        {
            if(Game.getUnit(x, y) == null && Game.getMap()[x][y] == 1)
            {
                Game.moveUnit(this.x, this.y, x, y, this);
                this.x = x;
                this.y = y;
                //System.out.println("Moved to: " + x * 32 + ", " + y * 32);
                System.out.println("Moved to Grid(" + this.x + ", " + this.y + ")");
            }
            else
            {
                System.out.println("There is already a unit there.");
            }
        }
        return false;
    }
    
    public boolean move(int d)
    {
        if(d == 0) { //Up
            move(x, y - 1);
        }
        else if(d == 1) { //Right
            move(x + 1, y);
        }
        else if(d == 2) { //Down
            move(x, y + 1);
        }
        else if(d == 3) { //Left
            move(x - 1, y);
        }
        return false;
    }
    
    public int attack(Unit atkd)
    {
        int expGain = 1; //EXP Gain Formula
        int tDmg = ATK - atkd.getDEF(); //Damage Formula
        atkd.damage(tDmg);
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
        n0 = dmg % 10;
        dmg -= n0;
        n1 = (dmg % 100) / 10;
        dmg -= n1 * 10;
        n2 = dmg / 100;
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
