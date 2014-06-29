package gui;

import block.Unit;
import gridGame.Game;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Window implements guiObj
{
    private final int bW = 4;
    private int x, y, w, h, tick;
    private Unit ally, enemy;
    private Bar[] allyBar  = new Bar[2], enemyBar = new Bar[2];
    //Window type
    private int type;
    
    public Window(int x, int y, int w, int h)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.type = 0;
        
        allyBar[0] = new Bar(x + 15, y + h - 45, 150, 14, 10, Color.RED);
        allyBar[1] = new Bar(x + 15, y + h - 25, 150, 14, 11, Color.BLUE);
        enemyBar[0] = new Bar(x + w - 165, y + h - 45, 150, 14, 20, Color.RED);
        enemyBar[1] = new Bar(x + w - 165, y + h - 25, 150, 14, 21, Color.BLUE);
    }
    
    
    public Window(int x, int y, int w, int h, Unit ally, Unit enemy)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.ally = ally;
        this.enemy = enemy;
        this.type = 1;
        
        allyBar[0] = new Bar(x + 15, y + h - 45, 150, 14, 10, Color.RED);
        allyBar[1] = new Bar(x + 15, y + h - 25, 150, 14, 11, Color.BLUE);
        enemyBar[0] = new Bar(x + w - 165, y + h - 45, 150, 14, 20, Color.RED);
        enemyBar[1] = new Bar(x + w - 165, y + h - 25, 150, 14, 21, Color.BLUE);
    }
    
    public void tick()
    {
        tick++;
        switch(type)
        {
            case 1:
                allyBar[0].tick();
                allyBar[1].tick();
                enemyBar[0].tick();
                enemyBar[1].tick();
                if(tick == 180)
                {
                    tick += ally.autoAttack(enemy) * 141;
                    enemyBar[0].set(enemy.getHPRatio());
                    enemyBar[1].set(enemy.getMPRatio());
                }
                else if(tick == 320)
                {
                    tick += enemy.autoAttack(ally) * 141;
                    allyBar[0].set(ally.getHPRatio());
                    allyBar[1].set(ally.getMPRatio());
                }
                else if(tick == 600)
                {
                    ally.done();
                    Game.gui.closeWindow();
                }
                break;
        }
    }
    
    public void render(Graphics g)
    {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
        g.setColor(Color.BLACK);
        g.fillRect(x, y, w, h);
        g.setColor(Color.WHITE);
        g.fillRect(x + bW, y + bW, w - 2 * bW, h - 2 * bW);
        
        
        switch(type)
        {
            case 1:
                g.setFont(new Font("Arial", Font.PLAIN, 12));
                ally.render(x + 60, y + 130, 2, g);
                allyBar[0].render(g);
                allyBar[1].render(g);
                ally.getInventory().getItem(0).render(x + 20, y + 220, 1, g);
                
                enemy.render(x + w - 60 - Game.TILESIZE * 2, y + 130, 2, g);
                enemyBar[0].render(g);
                enemyBar[1].render(g);
                enemy.getInventory().getItem(0).render(x + w - 95 - Game.TILESIZE * 2, y + 220, 1, g);
                
                g.setColor(Color.BLACK);
                g.drawString(ally.getInventory().getItem(0).getNAME(), x + 60, y + 245);
                g.drawString(enemy.getInventory().getItem(0).getNAME(), x + w - 55 - Game.TILESIZE * 2, y + 245);
                break;
        }
    }
    
    public void start()
    {
        tick = 0;
        switch(type)
        {
            case 1:
                break;
        }
    }
    
    public void set(Unit ally, Unit enemy)
    {
        this.type = 1;
        this.ally = ally;
        this.enemy = enemy;
        
        //might want to render hp/mp values to 100 before loading so the bars decrease to current hp/mp instead of random
        allyBar[0].set(ally.getHPRatio());
        allyBar[1].set(ally.getMPRatio());
        enemyBar[0].set(enemy.getHPRatio());
        enemyBar[1].set(enemy.getMPRatio());
    }
}
