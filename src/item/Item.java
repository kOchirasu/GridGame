package item;

import graphics.SpriteLoader;
import gridGame.Game;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Item 
{
    int iD, type, minRange, maxRange, ability, atk, mAtk, acc, crit, value;
    String name;
    BufferedImage image;
    
    public Item(ItemLoader lookup, int iD)
    {
        try
        {
            ResultSet rs = lookup.search(iD);
            this.iD         = iD;
            this.name       = rs.getString("NAME");
            this.minRange   = rs.getInt("MINRANGE");
            this.maxRange   = rs.getInt("MAXRANGE");
            this.ability    = rs.getInt("ABILITY");
            this.atk        = rs.getInt("ATTACK");
            this.mAtk       = rs.getInt("MAGIC_ATTACK");
            this.acc        = rs.getInt("ACCURACY");
            this.crit       = rs.getInt("CRITICAL");
            this.value      = rs.getInt("VALUE");
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        SpriteLoader loader = new SpriteLoader();
        this.image = loader.load("/Item/" + iD + ".png");
    }
    
    //Normal item rendering function
    public void render(int x, int y, Graphics g)
    {
        g.drawImage(image, x, y, Game.TILESIZE, Game.TILESIZE, null);
    }
    
    //used to render semi-transparent item when dragging
    public void render(int x, int y, int m, Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        Point2D center = new Point2D.Float(x + Game.TILESIZE / 2, y + Game.TILESIZE /2);
        float radius = 15;
        float[] dist = {0.1f, 1f};
        Color[] colors = {Color.DARK_GRAY, new Color(255, 255, 255, 0)};
        RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors, CycleMethod.NO_CYCLE);
        g2.setPaint(p);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.5));
        g2.fillOval(x, y, Game.TILESIZE, Game.TILESIZE);
        g2.drawImage(image, x, y, Game.TILESIZE * m, Game.TILESIZE * m, null);
    }
    
    @Override
    public String toString()
    {
        return "Item ID: \t" + iD + "\nName: \t\t" + name + "\nRange: \t\t" + minRange + "~" + maxRange + "\nAtt/M.Att: \t" + atk + "/" + mAtk + "\nAccuracy: \t" + acc + "\nCritical: \t" + crit + "\nSale Price: \t" + value;
    }
    
    public int getID() {
        return iD;
    }
    public String getNAME() {
        return name;
    }
    public int getATK() {
        return atk;
    }
    public int getMATK() {
        return mAtk;
    }
    public int getACC() {
        return acc;
    }
    public int getCRIT() {
        return crit;
    }
    public int getMinRANGE() {
        return minRange;
    }
    public int getMaxRANGE() {
        return maxRange;
    }
    public int getValue() {
        return value;
    }
}