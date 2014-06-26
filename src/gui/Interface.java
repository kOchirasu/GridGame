package gui;

import block.Unit;
import graphics.Sprite;
import gridGame.Game;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class Interface
{
    public final int BCOUNT = 8; // Button count
    /*
    x, y            - x, y mouse coordinates
    cX, cY          - x, y grid coordinates
    mouse           - Color of mouse cursor (see cursor.png)
    buttonMap       - Map of buttons in interface (not including button array)
    barMap          - Map of bars in interface
    bg              - Background image
    line1,2,3,4,5   - Unit stat information
    cSelect        - Currently cSelect unit (will be null if no unit is actively cSelect)
    sprite          - Image array to render from
    selectColor     - Color of the highlight over cSelect grid
    oBt, nBt        - Old button, New button used for detection
    inWindow        - True while mouse cursor is in the window
    lockSelect      - Prevents interface from updating cSelect unit.  True when a unit has moved but 
                      has not finished its actions.
    slot            - Used for inventory slot number
    */
    private int x, y, cX, cY, mouse, slot;
    private final HashMap<Integer, Button> buttonMap;
    private final HashMap<Integer, Bar> barMap;
    private final BufferedImage bg;
    //private Button[] buttonArray = new Button[BCOUNT];
    private String line1, line2, line3, line4, line5;
    private Unit cSelect;
    private Sprite sprite;
    private Color selectColor;
    private Button oBt, nBt;
    private boolean inWindow = true, lockSelect;
    private Window window;
    
    public Interface(BufferedImage bg, Sprite sprite)
    {
        this.bg = bg;
        this.sprite = sprite;
        this.cX = Game.fieldWidth;
        this.cY = Game.fieldHeight;
        buttonMap = new HashMap<>();
        barMap = new HashMap<>();
        selectColor = new Color(255, 255, 255, 128);
        line1 = line2 = line3 = line4 = line5 = "";
        for(int i = 0; i < BCOUNT; i++)
        {
            buttonMap.put(i, addButton("", i, i));
            buttonMap.get(i).disabled = true;
        }
        slot = -1;
        
        //window = new Window(125, 75, 450, 300);
    }
    
    public void tick()
    {
        for(Integer i : barMap.keySet())
        {
            barMap.get(i).tick();
        }
    }
    
    //First render for buttons and bars
    public void render(Graphics g)
    {
        //Renders the background image
        g.drawImage(bg, 0, 0, null);
        //Renders all buttons
        for(Integer i : buttonMap.keySet())
        {
            buttonMap.get(i).render(g);
        }
        //Renders all bars
        for(Integer i : barMap.keySet())
        {
            barMap.get(i).render(g);
        }
        //Displays cSelect unit's info
        if(cSelect != null)
        {
            g.setColor(Color.BLACK);
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
        if(cSelect != null)
        {
            cSelect.getInventory().render(g);
            cSelect.getInventory().render2(g);
            int x = (cSelect.getX() - Game.xOff) * Game.TILESIZE;
            int y = (cSelect.getY() - Game.yOff) * Game.TILESIZE;
            if(x >= 0 && y >= 0 && x < Game.fieldWidth * Game.TILESIZE && y < Game.fieldHeight * Game.TILESIZE)
            {
                g.setColor(Color.BLUE);
                g.fillRect(Game.MAPOFFX + x, Game.MAPOFFY + y, Game.TILESIZE, Game.TILESIZE);
            }
        }
    }
    
    //Second render for mouse pointer and grid highlights
    public void render3(Graphics g)
    {
        if(window != null)
        {
            window.render(g);
        }
        else if(cX >= 0 && cY >= 0 && cX < Game.fieldWidth && cY < Game.fieldHeight) //Highlights the grid that the mouse is hovering over
        {
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
    
    //need to fix button iDs
    public void setButton(String text, int iD, int loc, boolean disabled)
    {
        buttonMap.get(loc).set(text, iD, disabled);
        //buttonMap.put(loc, buttonMap.get(loc));
    }
    
    public void hideButtons()
    {
        for(int i = 0; i < BCOUNT; i++) {
            setButton("", i, i, true);
        }
    }
    
    //Creates one of the 8 buttons on the right of gui
    public Button addButton(String text, int iD, int loc)
    {
        if(loc >= 0 && loc < BCOUNT) {
            return new Button(544, 41 + 42 * loc, 140, 32, text, iD);
        }
        return null;
    }
    
    //Creates a new button and adds it to the button ArrayList
    public Button addButton(int x, int y, int w, int h, String text, int iD)
    {
        Button bt = new Button(x, y, w, h, text, iD);
        buttonMap.put(iD, bt);
        return bt;
    }
    
    //Creates a new bar and adds it to the bar ArrayList
    public void addBar(int x, int y, int w, int h, int iD, Color barColorF)
    {
        barMap.put(iD, new Bar(x, y, w, h, iD, barColorF));
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
    public void update(int x, int y, boolean click, int button)
    {
        update(x, y);
        
        if(window == null)
        {
            switch(button)
            {
                case 1: //Left click
                    if(cSelect != null)
                    {
                        cSelect.getInventory().slotRequest(slot, whichSlot());
                        slot = click ? whichSlot() : -1;
                    }

                    //There is some glitch where the button wont register sometimes, need to debug...
                    oBt = nBt;
                    nBt = clicked();
                    if(oBt != null || nBt != null)    
                    {
                        if(oBt == nBt && oBt.pressed == true) {
                            nBt.click(cSelect);
                        }
                        else 
                        {
                            if(oBt != null) {
                                oBt.update(false, false);
                            }
                            if(nBt != null) {
                                nBt.update(true, click);
                            }
                        }
                    }
                    break;
                    
                case 2: //Middle click (wheel)
                    break;
                    
                case 3: //Right click
                    break;
            }
        }
    }
    
    public void click(int iD)
    {
        for(Integer i : buttonMap.keySet())
        {
            System.out.println(i + ": " + buttonMap.get(i).getText());
        }
        if(cSelect != null)
        {
            System.out.println(buttonMap.get(iD).getText());
            buttonMap.get(iD).click(cSelect);
        }
    }
    
    //Updates unit info display, small bug with hasMoved not changing but too lazy to fix
    public void update(Unit selected)
    {
        if((selected == null || selected.getClassID() >= 0) && window == null)
        {
            //lock select after unit moves
            //unlock select if move canceled or unit done
            lockSelect = cSelect != null && (cSelect.hasMoved() || cSelect.moving) && !cSelect.isDone();
            if(lockSelect)
            {
                if(cSelect.isAttacking())
                {
                    if(!cSelect.attack(selected)) {
                        System.out.println("Invalid target");
                    }
                }
                else
                {
                    //not attacking
                }
                unitUpdate(cSelect);
            }
            else
            {
                cSelect = selected;
                if(cSelect != null)
                {
                    cSelect.select();
                    unitUpdate(cSelect);
                }
                else
                {
                    hideButtons();
                }
            }
        }
    }
    
    private void unitUpdate(Unit unit)
    {
        //display info
        line1 = "Team: " + unit.getTEAM() + "     Coordinates: (" + unit.getX() + ", " + unit.getY() + ")     " + (unit.hasMoved() ? "[Can't Move]" : "[Can Move]");
        line2 = "HP: " + unit.getHP() +  "/" + unit.getMaxHP() + "     MP: " + unit.getMP() +  "/" + unit.getMaxMP() + "     LV: " + unit.getLV() + "     EXP: " + (int) (unit.getEXP() * 100) + "%";
        line3 = "Fatigue: " + unit.getFTG() + "     Movement: " + unit.getMOV() + "     Range: " + unit.getMinRANGE() + "~" + unit.getMaxRANGE();
        line4 = "Attack: " + unit.getATK() + "     Magic Attack: " + unit.getMATK() + "     Defense: " + unit.getDEF();
        line5 = "Accuracy: " + unit.getACC() + "     Avoid: " + unit.getAVO() + "     Critical: " + unit.getCRIT() + "%";
        barMap.get(0).set(unit.getHPRatio());
        barMap.get(1).set(unit.getMPRatio());
        barMap.get(2).set(unit.getEXP());
    }
    
    public boolean canSelect(Unit selected)
    {
        lockSelect = cSelect != null && (cSelect.hasMoved() || cSelect.moving) && !cSelect.isDone();
        return !lockSelect && selected != null && !selected.moving && !selected.hasMoved() && selected.getClassID() != -1 && window == null;
    }
    
    //Determines whether or not mouse is in the window
    public void update(boolean b)
    {
        inWindow = b;
    }
    
    //returns the inventory slot the mouse has clicked
    private int whichSlot()
    {
        for(int i = 0; i < cSelect.getInventory().getSize(); i++)
        {
            if(within(cSelect.getInventory().area(i))) {
                return i;
            }
        }
        return -1;
    }
    
    //Loops through button list and finds which button the mouse clicked on
    //Could implement with binary search if button coordinates are in order.
    private Button clicked()
    {
        for(Integer i : buttonMap.keySet())
        {
            if(within(buttonMap.get(i).area())) {
                return buttonMap.get(i);
            }
        }
        return null;
    }
    
    private boolean within(int[] area)
    {
        return area[2] <= y && area[3] >= y && area[0] <= x && area[1] >= x;
    }
}