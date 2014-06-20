package graphics;

import gridGame.Game;
import java.awt.image.BufferedImage;

//This class has every image you need.  Just pass it to the class and you can render from it
public class Sprite 
{
    public static final int dmwDIM = 10, dmhDIM = 14;
    
    private final int uROWS = 3, uCOLUMNS = 10;
    public BufferedImage[][] unit = new BufferedImage[uROWS][uCOLUMNS];
    private final int dCOLUMNS = 10;
    public BufferedImage[] damage = new BufferedImage[dCOLUMNS];
    private final int cROWS = 8, cCOLUMNS = 13;
    public BufferedImage[][] cursor = new BufferedImage[cROWS][cCOLUMNS];
    private final int tROWS = 4, tCOLUMNS = 10;
    public BufferedImage[][] tile = new BufferedImage[tROWS][tCOLUMNS];
    
    //All the solid color tiles are useless since you can just draw rectangles
    public Sprite(BufferedImage ss, BufferedImage ds, BufferedImage cs, BufferedImage ts)
    {
        for(int i = 0; i < uROWS; i++)
        {
            for(int j = 0; j < uCOLUMNS; j++)
            {
                unit[i][j] = ss.getSubimage(j * Game.TILESIZE, i * Game.TILESIZE, Game.TILESIZE, Game.TILESIZE);
            }
        }
        for(int i = 0; i < dCOLUMNS; i++)
        {
            damage[i] = ds.getSubimage(i * dmwDIM, 0, dmwDIM, dmhDIM);
        }
        for(int i = 0; i < cROWS; i++)
        {
            for(int j = 0; j < cCOLUMNS; j++)
            {
                cursor[i][j] = cs.getSubimage(j * 32, i * 32, 32, 32);
            }
        }
        for(int i = 0; i < tROWS; i++)
        {
            for(int j = 0; j < tCOLUMNS; j++)
            {
                tile[i][j] = ts.getSubimage(j * Game.TILESIZE, i * Game.TILESIZE, Game.TILESIZE, Game.TILESIZE);
            }
        }
    }
}