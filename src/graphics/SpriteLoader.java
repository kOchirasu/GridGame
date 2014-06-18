package graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;
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
            System.out.println(ex.getMessage());
        }
        return null;
    }
}