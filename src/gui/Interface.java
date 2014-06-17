package gui;

import graphics.Sprite;
import gridGame.Game;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Interface
{
    public final ArrayList<Button> buttonList;
    public final ArrayList<Bar> barList;
    private final BufferedImage bg;
    private Sprite sprite;
    private int x, y, cX, cY, bID = -1, bIDt;
    
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
    
    public void render2(Graphics g)
    {
        if(cX < Game.mapWidth && cY < Game.mapHeight) {
            g.drawImage(sprite.image[2][1], cX * Sprite.spDIM, cY * Sprite.spDIM, Sprite.spDIM, Sprite.spDIM, null);
        }
        if(bID != -1) {
            g.drawImage(sprite.cursor[0][1], x, y, 32, 32, null);
        }
        else {
            g.drawImage(sprite.cursor[0][0], x, y, 32, 32, null);
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
    
    public void update(int x, int y)
    {
        this.x = x;
        this.cX = x / sprite.spDIM;
        this.y = y;
        this.cY = y / sprite.spDIM;
    }
    
    public void update(int x, int y, boolean click)
    {
        //System.out.println("updated");
        this.x = x;
        this.cX = x / sprite.spDIM;
        this.y = y;
        this.cY = y / sprite.spDIM;
        
        bIDt = bID;
        bID = (y >= 22 && y <= 390 && x >= 535 && x <= 681 && (y - 22) % 46 <= 34) ? (y - 22) / 46 : -1;
        if(bIDt != -1 || bID != -1)    
        {
            if(bIDt == bID && buttonList.get(bIDt).pressed == true)
            {
                buttonList.get(bID).click();
                buttonList.get(bID).pressed = false;
            }
            else
            {
                if(bIDt != -1)
                {
                    buttonList.get(bIDt).shade = false;
                    buttonList.get(bIDt).pressed = false;
                }
                if(bID != -1)
                {
                    buttonList.get(bID).shade = true;
                    if(click){
                        buttonList.get(bID).pressed = true;
                    }
                }
            }
        }
    }
}
