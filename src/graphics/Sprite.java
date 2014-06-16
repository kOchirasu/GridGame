package graphics;

import java.awt.image.BufferedImage;

public class Sprite 
{
    private final int ROWS = 3, COLUMNS = 8;
    public static final int spDIM = 32, dmwDIM = 10, dmhDIM = 14;
    public BufferedImage[][] image = new BufferedImage[ROWS][COLUMNS];
    public BufferedImage[] damage = new BufferedImage[10];
    public BufferedImage[][] cursor = new BufferedImage[1][13];
    
    public Sprite(BufferedImage ss, BufferedImage ds, BufferedImage cs)
    {
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLUMNS; j++)
            {
                image[i][j] = ss.getSubimage(j * spDIM, i * spDIM, spDIM, spDIM);
            }
        }
        for(int i = 0; i < 10; i++)
        {
            damage[i] = ds.getSubimage(i * dmwDIM, 0, dmwDIM, dmhDIM);
        }
        for(int i = 0; i < 1; i++)
        {
            for(int j = 0; j < 13; j++)
            {
                cursor[i][j] = cs.getSubimage(j * 32, i * 32, 32, 32);
            }
        }
    }
}