package gui;

import character.Unit;
import gridGame.Game;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class Button
{
    /* Private Variables
    x, y            - x, y, coordinates of button
    w, h            - Width and height of button
    iD              - Button iD for identifying button
    text            - Text on button
    font            - Font of text on button
    buttonColor     - Color of button
    hoverColor      - Color of button when hovered
    shadeColor      - Color of button when pressed
    disabledColor   - Color of button when disabled
    */
    private int x, y, w, h, iD;
    private String text;
    private Font font;
    private Color buttonColor, hoverColor, shadeColor, disabledColor;
    
    /* Public Variables
    pressed         - True while button is being pressed
    shade           - True if button is shaded
    disabled        - True if button is disabled
    */
    public boolean pressed, shade, disabled;
    
    public Button(int x, int y, int w, int h, String text, int iD, Font font, Color hoverColor, Color shadeColor, Color buttonColor)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.text = text;
        this.iD = iD;
        this.font = font;
        this.hoverColor = hoverColor;
        this.shadeColor = shadeColor;
        this.buttonColor = buttonColor;
        disabledColor = new Color(75, 75, 75, 175);
    }
    
    public Button(int x, int y, int w, int h, String text, int iD)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.text = text;
        this.iD = iD;
        font = new Font("Arial", Font.PLAIN, h/5 + 8);
        hoverColor = new Color(255, 255, 255, 128);
        shadeColor = new Color(0, 0, 0, 128);
        buttonColor = new Color(0, 255, 0, 128);
        disabledColor = new Color(75, 75, 75, 175);
    }
    
    //Renders the button, button text, and any shading
    public void render(Graphics g)
    {
        g.setColor(buttonColor);
        g.fillRect(x, y, w, h);
        if(disabled) {
            g.setColor(disabledColor);
            g.fillRect(x, y, w, h);
        }
        else if(shade) {
            g.setColor(pressed ? shadeColor : hoverColor);
            g.fillRect(x, y, w, h);

        }
        g.setColor(Color.BLACK);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics(font);  
        g.drawString(text, (int) (x + w/2 - fm.getStringBounds(text, g).getWidth()/2), (int) (y + h/2 + fm.getStringBounds(text, g).getHeight()/4));
    }
    
    //All button functions go here
    public int click(Unit selected)
    {
        if(!disabled)
        {
            switch(iD)
            {
                case 0: 
                    selected.showAttack();
                    break;
                case 1: case 2: case 3: case 4:
                    System.out.println(text + " was clicked. Not yet implemented.");
                    break;
                case 5:
                    selected.cancelMove();
                    System.out.println("Cancel button was clicked");
                    break;
                case 6:
                    selected.done();
                    System.out.println("Wait button was clicked for unit on (" + selected.getX() + ", " + selected.getY() + ")");
                    break;
                case 7:
                    Game.newTurn();
                    System.out.println("Moves were reset");
                    break;
                default:
                    System.out.println(text + " was clicked. Not a main button.");
                    break;
            }
            return iD;
        }
        return -1;
    }
    
    //Returns the x, y of the 4 corners of the button
    public int[] getArea() {
        return new int[]{x, x + w, y, y + h};
    }
    
    //Returns the button ID
    public int getID() {
        return iD;
    }
    
    //Allows the text of the button to be changed
    public void set(String text) {
        this.text = text;
    }
    
    //Allows the ID of the button to be changed
    public void set(int iD) {
        this.iD = iD;
    }
    
    public void set(String text, int iD, boolean disabled)
    {
        this.text = text;
        this.iD = iD;
        this.disabled = disabled;
    }
    
    //Allows the dimensions and location of the button to be changed
    public void setBounds(int x, int y, int w, int h)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    
    
}