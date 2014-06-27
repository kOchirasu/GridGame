package map;

//This does nothing.  Not sure if I will make it in the same project

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MapEditor
{
    private String path = "/test.mf";
    private byte[] data = {18, 17, 
                            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1,
                            1, 1, 1, 1, 1, 1, 1, 3, 1, 2, 1, 1, 3, 1, 1, 1, 1, 1,
                            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1,
                            1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 2, 1, 3, 1, 1, 1, 1, 1,
                            1, 1, 1, 1, 1, 1, 8, 4, 4, 4, 9, 1, 3, 1, 1, 1, 3, 1,
                            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1,
                            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1,
                            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1,
                            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1,
                            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 6, 7, 1, 1,
                            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 8, 9, 1, 1,
                            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1,
                            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1,
                            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1,
                            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1,
                            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1,
                            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1,
                            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1};

    public MapEditor()
    {
        
    }
    
    public void write()
    {
        try
        {
            OutputStream out = null;
            try
            {
                try
                {
                    String temp = Paths.get(getClass().getResource(path).toURI()).toFile().toString();
                    int x = temp.indexOf("\\GridGame\\");
                    temp = temp.substring(0, x + 10) + "res\\test.mf";
                    out = new BufferedOutputStream(new FileOutputStream(temp));
                    out.write(data);
                } catch (URISyntaxException ex) {
                    Logger.getLogger(MapEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            finally
            {
                //out.close();
            }
        }
        catch (IOException ex) 
        {
            System.out.println(ex.getMessage());
        }
    }
    public Map edit(Map map)
    {
        return null;
    }
}
