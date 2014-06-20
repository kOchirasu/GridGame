package item;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//http://www.homeandlearn.co.uk/java/databases_and_java_forms.html
//Very untested, but at least reading from the database works
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
    
    private boolean editEntry()
    {
        try {
            rs.updateRow();
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }
    
    private boolean newEntry()
    {
        try {
            rs.moveToInsertRow();
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }
    
    private boolean deleteEntry()
    {
        try {
            rs.deleteRow();
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }
}
