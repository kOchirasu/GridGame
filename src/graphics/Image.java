package graphics;

import java.awt.image.BufferedImage;

public class Image 
{
    private final int COLUMNS = 8, ROWS = 2;
    public BufferedImage[][] sprite = new BufferedImage[ROWS][COLUMNS];
    public BufferedImage[] damage = new BufferedImage[10];
    
    public Image(SpriteSheet ss, SpriteSheet ds)
    {
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLUMNS; j++)
            {
                sprite[i][j] = ss.crop(j, i, 32, 32);
            }
        }
        for(int i = 0; i < 10; i++)
        {
            damage[i] = ds.crop(i, 0, 10, 14);
        }
    }
}
