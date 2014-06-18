package gui;

import graphics.Sprite;
import java.awt.Font;
import java.awt.Graphics;

public class Button 
{
    private int x, y, w, h, iD;
    private String text;
    private Sprite sprite;
    private Font font;
    public boolean pressed = false, shade = false;
    
    public Button(int x, int y, int w, int h, String text, int iD, Sprite sprite)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.text = text;
        this.iD = iD;
        this.sprite = sprite;
        font = new Font("Calibri", Font.BOLD, 20);
    }
    
    public void render(Graphics g)
    {
        g.drawImage(sprite.image[1][2], x, y, w, h, null);
        g.setFont(font);
        g.drawString(text, x + 40, y + 22);
        if(shade) {
            g.drawImage(sprite.image[1][pressed ? 4 : 1], x, y, w, h, null);
        }
    }
    
    public int click()
    {
        System.out.println(text + " was clicked.");
        return Integer.parseInt(text.substring(7, 8));
    }
    
    public void changeText(String text)
    {
        this.text = text;
    }
    
    public void changeID(int iD)
    {
        this.iD = iD;
    }
}