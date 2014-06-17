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
    private int x, y, cX, cY, bIDt, bID = -1, mouse;
    
    public Interface(BufferedImage bg, Sprite sprite)
    {
        this.bg = bg;
        this.sprite = sprite;
        this.cX = Game.mapWidth;
        this.cY = Game.mapHeight;
        buttonList = new ArrayList<>();
        barList = new ArrayList<>();
    }
    
    //First render for buttons and bars
    public void render(Graphics g)
    {
        g.drawImage(bg, 0, 0, null);
        for (Button i : buttonList) {
            i.render(g);
        }
        for(Bar i : barList) {
            i.render(g);
        }
    }
    
    //Second render for mouse pointer and grid highlights
    public void render2(Graphics g)
    {
        if(cX < Game.mapWidth && cY < Game.mapHeight) {
            g.drawImage(sprite.image[2][1], cX * Sprite.spDIM, cY * Sprite.spDIM, Sprite.spDIM, Sprite.spDIM, null);
        }
        if(bID != -1) {
            g.drawImage(sprite.cursor[mouse][1], x, y, 32, 32, null);
        }
        else {
            g.drawImage(sprite.cursor[mouse][0], x, y, 32, 32, null);
        }
    }
    
    //Creates a new button and adds it to the button ArrayList
    public void addButton(String text, int iD)
    {
        addButton(535, 22 + 46 * iD, 149, 34, text, iD);
    }
    public void addButton(int x, int y, int w, int h, String text, int iD)
    {
        buttonList.add(new Button(x, y, w, h, text, iD, sprite));
    }
    
    //Creates a new bar and adds it to the bar ArrayList
    public void addBar()
    {
        barList.add(new Bar()); //Unfinished
    }
    
    //Updates mouse location
    public void update(int x, int y)
    {
        this.x = x;
        this.cX = x / sprite.spDIM;
        this.y = y;
        this.cY = y / sprite.spDIM;
    }
    
    //Updates mouse location and whether or not mouse is clicked
    public void update(int x, int y, boolean click)
    {
        update(x, y);
        
        bIDt = bID;
        bID = (y >= 22 && y <= 390 && x >= 535 && x <= 681 && (y - 22) % 46 <= 34) ? (y - 22) / 46 : -1; //Alien Language
        if(bIDt != -1 || bID != -1)    
        {
            if(bIDt == bID && buttonList.get(bIDt).pressed == true) {
                mouse = buttonList.get(bID).click();
                buttonList.get(bID).pressed = false;
            }
            else {
                if(bIDt != -1) {
                    buttonList.get(bIDt).shade = false;
                    buttonList.get(bIDt).pressed = false;
                }
                if(bID != -1) {
                    buttonList.get(bID).shade = true;
                    if(click){
                        buttonList.get(bID).pressed = true;
                    }
                }
            }
        }
    }
}