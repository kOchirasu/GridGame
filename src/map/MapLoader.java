package map;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MapLoader 
{
    private FileInputStream inputStream;
    
    //Takes in path to a map, returns a Map object
    public Map load(String path)
    {
        try 
        {
            inputStream = new FileInputStream(new File(getClass().getResource(path).toURI())); //Some crap to read file
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            
            return new Map(br);
        }
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(MapLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException | URISyntaxException ex) 
        {
            Logger.getLogger(MapLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
