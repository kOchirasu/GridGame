package gui;

import character.Unit;
import graphics.Sprite;
import gridGame.Game;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Interface
{
    /*
    x, y            - x, y mouse coordinates
    cX, cY          - x, y grid coordinates
    mouse           - Color of mouse cursor (see cursor.png)
    buttonList      - List of buttons in interface
    barList         - List of bars in interface
    bg              - Background image
    line1,2,3,4,5   - Unit stat information
    lastSelected    - Last selected unit (will not be null after first unit selected
    selected        - Currently selected unit (will be null if no unit is actively selected)
    sprite          - Image array to render from
    selectColor     - Color of the highlight over selected grid
    oBt, nBt        - Old button, New button used for detection
    inWindow        - True while mouse cursor is in the window
    */
    private int x, y, cX, cY, mouse;
    private final ArrayList<Button> buttonList;
    private final ArrayList<Bar> barList;
    private final BufferedImage bg;
    private String line1, line2, line3, line4, line5;
    private Unit lastSelected, selected;
    private Sprite sprite;
    private Color selectColor;
    private Button oBt, nBt;
    private boolean inWindow = true;
    
    public Interface(BufferedImage bg, Sprite sprite)
    {
        this.bg = bg;
        this.sprite = sprite;
        this.cX = Game.mapWidth;
        this.cY = Game.mapHeight;
        buttonList = new ArrayList<>();
        barList = new ArrayList<>();
        selectColor = new Color(255, 255, 255, 128);
    }
    
    //First render for buttons and bars
    public void render(Graphics g)
    {
        //Renders the background image
        g.drawImage(bg, 0, 0, null);
        //Renders all buttons
        for(int i = 0; i < buttonList.size(); i++)
        {
            buttonList.get(i).render(g);
        }
        //Renders all bars
        for(int i = 0; i < barList.size(); i++)
        {
            barList.get(i).render(g);
        }
        //Displays selected unit's info
        if(selected != null)
        {
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.drawString(line1, 400, 430);
            g.drawString(line2, 400, 450);
            g.drawString(line3, 400, 470);
            g.drawString("Stats:", 400, 500);
            g.drawString(line4, 440, 510);
            g.drawString(line5, 440, 530);
        }
    }
    
    //Renders Square below unit
    public void render2(Graphics g)
    {
        if(selected != null)
        {
            g.setColor(Color.BLUE);
            g.fillRect(Game.MAPOFFX + selected.getX() * Game.TILESIZE, Game.MAPOFFY + selected.getY() * Game.TILESIZE, Game.TILESIZE, Game.TILESIZE);
        }
    }
    
    //Second render for mouse pointer and grid highlights
    public void render3(Graphics g)
    {
        //Highlights the grid that the mouse is hovering over
        if(cX >= 0 && cY >= 0 && cX < Game.mapWidth && cY < Game.mapHeight) {
            g.setColor(selectColor);
            g.fillRect(Game.MAPOFFX + cX * Game.TILESIZE, Game.MAPOFFY + cY * Game.TILESIZE, Game.TILESIZE, Game.TILESIZE);
        }
        //Renders mouse pointer
        if(inWindow)
        {
            if(nBt != null) {
                //Hand pointer
                g.drawImage(sprite.cursor[mouse][1], x - 7, y, 32, 32, null);
            }
            else {
                //Normal pointer
                g.drawImage(sprite.cursor[mouse][0], x, y, 32, 32, null);
            }
        }
    }
    
    //Creates a new button and adds it to the button ArrayList
    public Button addButton(String text, int iD)
    {
        return addButton(544, 41 + 42 * iD, 140, 32, text, iD);
    }
    
    public Button addButton(int x, int y, int w, int h, String text, int iD)
    {
        Button bt = new Button(x, y, w, h, text, iD);
        buttonList.add(bt);
        return bt;
    }
    
    //Creates a new bar and adds it to the bar ArrayList
    public void addBar()
    {
        barList.add(new Bar(0, 0, 0, 0, 0)); //Unfinished
    }
    
    //Updates mouse location
    public void update(int x, int y)
    {
        this.x = x;
        this.cX = (int)Math.floor((x - Game.MAPOFFX) / (double)Game.TILESIZE);
        this.y = y;
        this.cY = (int)Math.floor((y - Game.MAPOFFY) / (double)Game.TILESIZE);
    }
    
    //Updates mouse location to highlight button and whether or not mouse is clicked
    public void update(int x, int y, boolean click)
    {
        update(x, y);
        
        oBt = nBt;
        nBt = clicked(x, y);
        if(oBt != null || nBt != null)    
        {
            if(oBt == nBt && oBt.pressed == true) {
                //Dumb stuff
                mouse = nBt.click(lastSelected);
                mouse = mouse < 0 || mouse > 7 ? 0 : mouse;
                
                nBt.pressed = false;
            }
            else {
                if(oBt != null) {
                    oBt.shade = false;
                    oBt.pressed = false;
                }
                if(nBt != null) {
                    nBt.shade = true;
                    if(click){
                        nBt.pressed = true;
                    }
                }
            }
        }
    }
    
    //Updates unit info display, small bug with hasMoved not changing but too lazy to fix
    public void update(Unit selected)
    {
        this.selected = selected;
        if(selected != null)
        {
            line1 = "Team: " + selected.getTEAM() + "     Coordinates: (" + selected.getX() + ", " + selected.getY() + ")     " + (selected.hasMoved() ? "[Can't Move]" : "[Can Move]");
            line2 = "HP: " + selected.getHP() +  "/" + selected.getMaxHP() + "     MP: " + selected.getMP() +  "/" + selected.getMaxMP() + "     LV: " + selected.getLV() + "     EXP: " + selected.getEXP() + "/" + 100;
            line3 = "Fatigue: " + selected.getFTG() + "     Movement: " + selected.getMOV() + "     Range: " + selected.getMinRANGE() + "~" + selected.getMaxRANGE();
            line4 = "Attack: " + selected.getATK() + "     Magic Attack: " + selected.getMATK() + "     Defense: " + selected.getDEF();
            line5 = "Accuracy: " + selected.getACC() + "     Avoid: " + selected.getAVO() + "     Critical: " + selected.getCRIT() + "%";
            lastSelected = selected;
        }
    }
    
    //Determines whether or not mouse is in the window
    public void update(boolean b)
    {
        inWindow = b;
    }
    
    //Loops through button list and finds which button the mouse clicked on
    private Button clicked(int x, int y)
    {
        for(int i = 0; i < buttonList.size(); i++)
        {
            if(buttonList.get(i).getArea()[2] <= y && buttonList.get(i).getArea()[3] >= y && buttonList.get(i).getArea()[0] <= x && buttonList.get(i).getArea()[1] >= x)
            {
                return buttonList.get(i);
            }
        }
        return null;
    }
}