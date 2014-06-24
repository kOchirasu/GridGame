package gui;

import block.Unit;
import graphics.Sprite;
import gridGame.Game;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Interface
{
    public final int BCOUNT = 8; // Button count
    /*
    x, y            - x, y mouse coordinates
    cX, cY          - x, y grid coordinates
    mouse           - Color of mouse cursor (see cursor.png)
    buttonList      - List of buttons in interface
    barList         - List of bars in interface
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
    private final ArrayList<Button> buttonList;
    private final ArrayList<Bar> barList;
    private final BufferedImage bg;
    private Button[] buttonArray = new Button[BCOUNT];
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
        this.cX = Game.mapWidth;
        this.cY = Game.mapHeight;
        buttonList = new ArrayList<>();
        barList = new ArrayList<>();
        selectColor = new Color(255, 255, 255, 128);
        line1 = line2 = line3 = line4 = line5 = "";
        for(int i = 0; i < BCOUNT; i++)
        {
            buttonArray[i] = addButton("", i, i);
            buttonArray[i].disabled = true;
        }
        slot = -1;
        
        //window = new Window(125, 75, 450, 300);
    }
    
    public void tick()
    {
        for(int i = 0; i < barList.size(); i++)
        {
            barList.get(i).tick();
        }
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
        for(int i = 0; i < BCOUNT; i++)
        {
            buttonArray[i].render(g);
        }
        //Renders all bars
        for(int i = 0; i < barList.size(); i++)
        {
            barList.get(i).render(g);
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
            g.setColor(Color.BLUE);
            g.fillRect(Game.MAPOFFX + cSelect.getX() * Game.TILESIZE, Game.MAPOFFY + cSelect.getY() * Game.TILESIZE, Game.TILESIZE, Game.TILESIZE);
        }
    }
    
    //Second render for mouse pointer and grid highlights
    public void render3(Graphics g)
    {
        if(window != null)
        {
            window.render(g);
        }
        else if(cX >= 0 && cY >= 0 && cX < Game.mapWidth && cY < Game.mapHeight) //Highlights the grid that the mouse is hovering over
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
    
    public void setButton(String text, int iD, int loc, boolean disabled)
    {
        buttonArray[loc].set(text, iD, disabled);
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
        buttonList.add(bt);
        return bt;
    }
    
    //Creates a new bar and adds it to the bar ArrayList
    public void addBar(int x, int y, int w, int h, int iD, Color barColorF)
    {
        barList.add(new Bar(x, y, w, h, iD, barColorF)); //Unfinished
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
        
        if(window == null)
        {
            if(cSelect != null)
            {
                cSelect.getInventory().slotRequest(slot, whichSlot());
                slot = click ? whichSlot() : -1;
            }
            
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
        //need to make this ID based instead of index based
        barList.get(0).set(unit.getHPRatio());
        barList.get(1).set(unit.getMPRatio());
        barList.get(2).set(unit.getEXP());
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
        for(int i = 0; i < BCOUNT; i++)
        {
            if(within(buttonArray[i].area()))
            {
                return buttonArray[i];
            }
        }
        for(int i = 0; i < buttonList.size(); i++)
        {
            if(within(buttonList.get(i).area())) {
                return buttonList.get(i);
            }
        }
        return null;
    }
    
    private boolean within(int[] area)
    {
        return area[2] <= y && area[3] >= y && area[0] <= x && area[1] >= x;
    }
}