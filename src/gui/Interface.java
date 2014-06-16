package gui;

import graphics.Sprite;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Interface
{
    private final ArrayList<Button> buttonList;
    private final ArrayList<Bar> barList;
    private final BufferedImage bg;
    private Sprite sprite;
    
    public Interface(BufferedImage bg, Sprite sprite)
    {
        this.bg = bg;
        this.sprite = sprite;
        buttonList = new ArrayList<>();
        barList = new ArrayList<>();
    }
    
    public void render(Graphics g)
    {
        g.drawImage(bg, 0, 0, null);
        for(Button i : buttonList)
        {
            i.render(g);
        }
        for(Bar i : barList)
        {
            i.render(g);
        }
    }
    
    public void addButton(int x, int y, int w, int h, String text, int iD)
    {
        buttonList.add(new Button(x, y, w, h, text, iD, sprite));
    }
    
    public void addBar()
    {
        barList.add(new Bar());
    }
}
