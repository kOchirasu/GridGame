package gui;

import block.Unit;
import gridGame.Game;
import java.awt.Color;
import java.awt.Graphics;

public class Window implements guiObj
{
    private final int bW = 4;
    private int x, y, w, h;
    private Unit unit, enemy;
    //Window type
    private int type;
    
    public Window(int x, int y, int w, int h)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.type = 0;
    }
    
    
    public Window(int x, int y, int w, int h, Unit unit, Unit enemy)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.unit = unit;
        this.enemy = enemy;
        this.type = 1;
    }
    
    public void tick()
    {
        
    }
    
    public void render(Graphics g)
    {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
        g.setColor(Color.BLACK);
        g.fillRect(x, y, w + 2 * bW, h + 2 * bW);
        g.setColor(Color.WHITE);
        g.fillRect(x + bW, y + bW, w, h);
    }
}
