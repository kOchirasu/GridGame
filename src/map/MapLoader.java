package map;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MapLoader 
{
    public Map load(String path)
    {
        try 
        {
            return new Map(path);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(MapLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
