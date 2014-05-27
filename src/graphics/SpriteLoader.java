package graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class SpriteLoader 
{
    public BufferedImage load(String path)
    {
        try 
        {
            return ImageIO.read(getClass().getResource(path));
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(SpriteLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
