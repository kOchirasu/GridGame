package item;

import graphics.SpriteLoader;
import gridGame.Game;
import java.awt.Graphics;
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
    
    public void render(int x, int y, Graphics g)
    {
        g.drawImage(image, x, y, Game.TILESIZE, Game.TILESIZE, null);
    }
    
    @Override
    public String toString()
    {
        return "Item ID: \t" + iD + "\nName: \t\t" + name + "\nRange: \t\t" + minRange + "~" + maxRange + "\nAtt/M.Att: \t" + atk + "/" + mAtk + "\nAccuracy: \t" + acc + "\nCritical: \t" + crit + "\nSale Price: \t" + value;
    }
}