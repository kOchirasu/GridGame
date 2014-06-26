package map;

//This does nothing.  Not sure if I will make it in the same project

import java.io.BufferedOutputStream;
import java.io.File;
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
                try {
                    //out = new BufferedOutputStream(new FileOutputStream(new File(getClass().getResource(path).toURI())));
                    System.out.println(Paths.get(getClass().getResource(path).toURI()).toFile());
                    //out = new BufferedOutputStream(new FileOutputStream(new File(getClass().getResource(path).toURI())));
                } catch (URISyntaxException ex) {
                    Logger.getLogger(MapEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
                out = new BufferedOutputStream(new FileOutputStream("C:\\Users\\Computer\\Documents\\NetBeansProjects\\GridGame\\res\\test.mf"));
                out.write(data);
            }
            finally
            {
                out.close();
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
