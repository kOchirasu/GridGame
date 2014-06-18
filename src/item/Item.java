package item;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Item 
{
    int iD, type, minRange, maxRange, ability, atk, mAtk, acc, crit, value;
    String name;
    
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
    }
    
    @Override
    public String toString()
    {
        return "Item ID: \t" + iD + "\nName: \t\t" + name + "\nRange: \t\t" + minRange + "~" + maxRange + "\nAtt/M.Att: \t" + atk + "/" + mAtk + "\nAccuracy: \t" + acc + "\nCritical: \t" + crit + "\nSale Price: \t" + value;
    }
}