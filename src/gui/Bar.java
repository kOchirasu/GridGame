package gui;

import java.awt.Color;
import java.awt.Graphics;

public class Bar 
{
    /*
    x, y        - x, y, coordinates of bar
    w, h        - Width and height of bar
    iD          - Bar iD for identifying bar
    barcolor    - Color of bar
    */
    private int x, y, w, h, iD;
    private Color barColor;
    
    public Bar(int x, int y, int w, int h, int iD)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.iD = iD;
        this.barColor = new Color(255, 0, 0, 255);
    }
    
    public void tick()
    {
        
    }
    
    public void render(Graphics g)
    {
        
    }
}
