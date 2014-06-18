package item;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ItemLoader
{
    ResultSet rs;
    Statement state;
    
    public ItemLoader(String host, String user, String pass)
    {
        try
        {
            Connection connect = DriverManager.getConnection(host, user, pass);
            state = connect.createStatement();
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
    
    public ResultSet search(int iD)
    {
        try
        {
            String sql = "select * from GRIDGAME.ITEMS where ID = " + iD;
            rs = state.executeQuery(sql);
            rs.next();
            return rs;
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        return null;
    }
}
