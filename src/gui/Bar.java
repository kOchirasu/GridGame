package gui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Bar implements guiObj
{
    /*
    bW          - border width of bar
    x, y        - x, y, coordinates of bar
    w, h        - Width and height of bar
    iD          - Bar iD for identifying bar
    barcolor    - Color of bar
    */
    private final int bW = 3;
    private int x, y, w, h, iD;
    private float value, dValue;
    private boolean increase;
    private Color barColorF, barColorB;
    
    public Bar(int x, int y, int w, int h, int iD)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.iD = iD;
        this.barColorF = new Color(255, 0, 0, 255);
        this.barColorB = new Color(255, 255, 255, 255);
        this.value = this.dValue = 1f;
    }
    
    public Bar(int x, int y, int w, int h, int iD, Color barColorF)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.iD = iD;
        this.barColorF = barColorF;
        this.barColorB = new Color(255, 255, 255, 255);
        this.value = this.dValue = 1f;
    }
    
    public void tick()
    {
        if(value > dValue) {
            dValue = increase ? dValue + 0.01f : value;
        }
        else if(value + 0.01 < dValue) {
            dValue = !increase ? dValue - 0.01f : value;
        }
    }
    
    public void render(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        //System.out.println("" + dValue + " " + value);
        int length = (int)((w - 2 * bW) * dValue);
        g.setColor(Color.BLACK);
        g.fillRect(x, y, w, h);
        g.setColor(barColorB);
        g.fillRect(x + bW, y + bW, w - 2 * bW, h - 2 * bW);
        GradientPaint gp = new GradientPaint(x, y, Color.WHITE, x, y + h - 2 * bW, barColorF, true);
        g2.setPaint(gp);
        g2.fillRect(x + bW, y + bW, length, h - 2 * bW);
    }
    
    public void set(float value)
    {
        increase = value > dValue;
        this.value = value;
    }
    
    public void get()
    {
        System.out.print(value);
    }
}
