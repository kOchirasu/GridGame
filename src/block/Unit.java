package block;

import algorithm.Calculator;
import graphics.Sprite;
import gridGame.Game;
import item.Inventory;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.TreeMap;

public class Unit
{
    /* Private Variables
    x, y                - Unit x, y grid coordinates
    pX, pY              - Unit previous x, y grid coordinates
    dX, dY              - Unit x, y coordinates where unit is displayed
    n[]                 - Damage values to display
    xOff, yOff          - Offsets to simulate movement
    xOff2, yOff2        - Going to be used to shift unit when animation map shifting
    count               - counter variable
    dmgLen              - Number of digits in damage
    "..."               - Unit stat variables
    team                - team which the unit is on
    walkTick, dmgTick   - Ticks to time walking and damage taking
    inventory           - Inventory of unit
    dead, moved         - True if unit is dead, or unit has moved for the turn
    moving              - True while unit is moving
    done                - True is unit has performed an action for the turn
    attacking           - True while user is slecting a target for the unit to attack
    ClassID             - Class ID of the unit
    walkList            - List of tiles that the unit will walk through to get to its destination
    btList              - List of buttons of possible actions for the unit
    sprite              - Array of sprites
    */
    private int x, y, pX, pY, dX, dY, n[] = new int[3], xOff, yOff, xOff2, yOff2, count, dmgLen;
    private int lv, exp, hp, maxHP, mp, maxMP, ftg, mov, atk, mAtk, def, acc, avoid, crit, team;
    private int walkTick, dmgTick;
    private Inventory inventory;
    private boolean dead, moved, moving, done, attacking;
    private short classID;
    private ArrayList<int[]> walkList = new ArrayList<>();
    private TreeMap<Integer, String> btList = new TreeMap<>();;
    private Sprite sprite;
    
    public Unit(int x, int y, Sprite sprite, int MOV, int TEAM)
    {
        this.x = this.pX = x;
        this.y = this.pY = y;
        this.dX = x * Game.TILESIZE;
        this.dY = y * Game.TILESIZE;
        this.sprite = sprite;
        this.mov = MOV != -1 ? MOV : 5; //Defaults to 5
        this.team = TEAM;
        this.inventory = new Inventory(5);
        classID = 0;
        
        this.exp = 15;
        this.maxHP = 50;
        this.hp = 40;
        this.maxMP = 20;
        this.mp = 15;
        this.atk = 25-10*team;
        this.def = 5;
        
        btList.put(0, "Attack " + x);
        btList.put(5, "Cancel " + y);
        btList.put(6, "Done " + dX);
        if(TEAM == 1)
        {
            btList.put(7, "Reset Moves " + dY);
        }
    }
    
    //Tick, updates the unit.  Used for animation timing
    public void tick()
    {
        if(dmgLen > 0)
        {
            dmgTick++;
            if(dmgTick > 120) //120 = 2 seconds
            {
                dmgLen = dmgTick = 0;
            }
        }
        
        if(moving)
        {
            walkTick++;
            if(count >= walkList.size())
            {
                walkList.clear();
                listButtons(true);
                moving = false;
                moved = true;
                count = 0;
                //System.out.println("Finished moving");
            }
            else
            {
                xOff += 4 * (walkList.get(count)[0] - this.dX / Game.TILESIZE);
                yOff += 4 * (walkList.get(count)[1] - this.dY / Game.TILESIZE);
                if(walkTick >= 8)
                {
                    xOff = yOff = walkTick = 0;
                    this.dX = walkList.get(count)[0] * Game.TILESIZE;
                    this.dY = walkList.get(count)[1] * Game.TILESIZE;
                    //System.out.println("x: " + x + " y: " + y);
                    count++;
                }
            }
        }
        else if(walkList.size() > 1)
        {
            listButtons(false);
            moving = true;
            //System.out.println("Started moving");
        }
        
        /*if(Game.map.count > 0)
        {
            xOff2 = Game.map.mX * Game.map.count + Game.xOff * Game.TILESIZE;
            yOff2 = Game.map.mY * Game.map.count + Game.yOff * Game.TILESIZE;
        }
        else {
            xOff2 = yOff2 = 0;
        }*/
    }
    
    //Renders unit and damage on unit
    public void render(Graphics g)
    {
        int x = dX - Game.xOff * Game.TILESIZE;
        int y = dY - Game.yOff * Game.TILESIZE;
        if(x >= 0 && y >= 0 && x < Game.fieldWidth * Game.TILESIZE && y < Game.fieldHeight * Game.TILESIZE)
        {
            x += Game.MAPOFFX + xOff + xOff2;
            y += Game.MAPOFFY + yOff + yOff2;
                
            if(dead) { //Gravestone
                g.drawImage(sprite.unit[0][8], x, y, Game.TILESIZE, Game.TILESIZE, null);
            }
            else if(done) { //Grayed out unit
                g.drawImage(sprite.unit[0][7], x, y, Game.TILESIZE, Game.TILESIZE, null);
            }
            else { //Normal unit depending on team
                g.drawImage(sprite.unit[0][team], x, y, Game.TILESIZE, Game.TILESIZE, null);
            }

            switch(dmgLen)
            {
                case 1:
                    g.drawImage(sprite.damage[n[0]], x + 11, y + 5, Sprite.dmwDIM, Sprite.dmhDIM, null);
                    break;
                case 2:
                    g.drawImage(sprite.damage[n[1]], x + 6, y + 5, Sprite.dmwDIM, Sprite.dmhDIM, null);
                    g.drawImage(sprite.damage[n[0]], x + 16, y + 5, Sprite.dmwDIM, Sprite.dmhDIM, null);
                    break;
                case 3:
                    g.drawImage(sprite.damage[n[2]], x + 1, y + 5, Sprite.dmwDIM, Sprite.dmhDIM, null);
                    g.drawImage(sprite.damage[n[1]], x + 11, y + 5, Sprite.dmwDIM, Sprite.dmhDIM, null);
                    g.drawImage(sprite.damage[n[0]], x + 21, y + 5, Sprite.dmwDIM, Sprite.dmhDIM, null);
                    break;
            }
        }
    }
    
    public void render(int x, int y, int m, Graphics g)
    {
        g.drawImage(sprite.unit[0][team], x, y, Game.TILESIZE * m, Game.TILESIZE * m, null);
        
        switch(dmgLen)
        {
            case 1:
                g.drawImage(sprite.damage[n[0]], x + 11 * m, y + 5 * m, Sprite.dmwDIM * m, Sprite.dmhDIM * m, null);
                break;
            case 2:
                g.drawImage(sprite.damage[n[1]], x + 6 * m, y + 5 * m, Sprite.dmwDIM * m, Sprite.dmhDIM * m, null);
                g.drawImage(sprite.damage[n[0]], x + 16 * m, y + 5 * m, Sprite.dmwDIM * m, Sprite.dmhDIM * m, null);
                break;
            case 3:
                g.drawImage(sprite.damage[n[2]], x + 1 * m, y + 5 * m, Sprite.dmwDIM * m, Sprite.dmhDIM * m, null);
                g.drawImage(sprite.damage[n[1]], x + 11 * m, y + 5 * m, Sprite.dmwDIM * m, Sprite.dmhDIM * m, null);
                g.drawImage(sprite.damage[n[0]], x + 21 * m, y + 5 * m, Sprite.dmwDIM * m, Sprite.dmhDIM * m, null);
                break;
        }
    }
    
    
    public void select()
    {
        listButtons(true);
    }
    
    //Finishes unit's turn, no more changes
    public void done()
    {
        if(!moving)
        {
            Game.paths.clearPaths();
            listButtons(false);
            done = true;
            moved = true;
            Game.gui.unitUpdate(this);
        }
    }
    
    //Resets the units move
    public void reset()
    {
        if(!dead)
        {
            done = false;
            moved = false;
        }
    }
    
    //Moves the unit to x, y through each tile in the walkList array
    public boolean move(int x, int y, ArrayList<int[]> walkList)
    {
        if(Game.inMap(x, y))
        {
            if(Game.paths.getMove(x, y) <= mov && Game.getUnit(x, y) == null)// && Game.paths.getMove(x, y) <= mov && mov != 0)
            {
                pX = this.x;
                pY = this.y;
                Game.moveUnit(x, y, this);
                this.walkList = walkList;
                this.x = x;
                this.y = y;
                //moved is set to true once the animation has completed, but can't I set it to true here?
                //System.out.println("Moved to Grid(" + this.x + ", " + this.y + ")");
                return true;
            }
        }
        return false;
    }
    
    //Cancels the units move
    public boolean cancelMove()
    {
        Game.paths.clearPaths();
        if(moved && !done) //If unit has made a move, but has not confirmed the action
        {
            Game.moveUnit(pX, pY, this);
            dX = pX * Game.TILESIZE;
            dY = pY * Game.TILESIZE;
            this.x = pX;
            this.y = pY;
            attacking = moved = false;
            return true;
        }
        return false;
    }
    
    //Shows attackable tiles for the unit
    public boolean showAttack()
    {
        if(!done)
        {
            Game.paths.findPath(new int[]{x, y, 0, getMinRANGE(), getMaxRANGE(), team});
            moved = true;
            attacking = true;
            return true;
        }
        return false;
    }
    
    //Attacks enemy unit
    public boolean attack(Unit enemy)
    {
        //this != enemy: this cannot be enemy if different teams, should be redundant check
        if(canAttack(enemy))
        {
            int expGain = Calculator.expGain(lv, enemy.getLV());
            int tDmg = Calculator.dmgAmt(atk + wepATK(), enemy.getDEF()) * 0;
            enemy.damage(tDmg);
            attacking = false;
            addEXP(expGain + 50);
            done();
            return true;
        }
        return false;
    }
    
    //temp attacking function, return 0 and 1 to skip certain amount of ticks
    public int autoAttack(Unit enemy)
    {
        if(enemy != null && this.getHP() > 0 && enemy.getHP() > 0 && team != enemy.getTEAM() && inRange(enemy))
        {
            int tDmg = Calculator.dmgAmt(atk + wepATK(), enemy.getDEF());
            enemy.damage(tDmg);
            return 0;
        }
        return 1;
    }
    
    //Lots of 'get' functions because no public variables
    public boolean canAttack(Unit enemy) {
        return enemy != null && enemy.getHP() > 0 && team != enemy.getTEAM() && inRange(enemy) && attacking;
    }
    public boolean hasMoved() {
        return moved;
    }
    public boolean isMoving() {
        return moving;
    }
    public boolean isAttacking() {
        return attacking;
    }
    public boolean isDone() {
        return done;
    }
    public int[] pathInfo() {
        return new int[]{x, y, mov, getMinRANGE(), getMaxRANGE(), team};
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getpX() {
        return pX;
    }
    public int getpY() {
        return pY;
    }
    public int getLV() {
        return lv;
    }
    public float getEXP() {
        return exp / 100.0f;
    }
    public int getHP() {
        return hp;
    }
    public int getMaxHP() {
        return maxHP;
    }
    public float getHPRatio() {
        return (float) hp / maxHP;
    }
    public int getMP() {
        return mp;
    }
    public int getMaxMP() {
        return maxMP;
    }
    public float getMPRatio() {
        return (float) mp / maxMP;
    }
    public int getFTG() {
        return ftg;
    }
    public int getMOV() {
        return mov;
    }
    public int getATK() {
        return atk;
    }
    public int getMATK() {
        return mAtk;
    }
    public int getDEF() {
        return def;
    }
    public int getACC() {
        return acc;
    }
    public int getAVO() {
        return avoid;
    }
    public int getCRIT() {
        return crit;
    }
    public int getTEAM() {
        return team;
    }
    public short getClassID() {
        return classID;
    }
    public Inventory getInventory() {
        return inventory;
    }
    //Should loop to find first compatible weap
    public int wepATK() {
        return inventory.getItem(0).getATK();
    }
    public int getMinRANGE() {
        return inventory.getItem(0).getMinRANGE();
    }
    public int getMaxRANGE() {
        return inventory.getItem(0).getMaxRANGE();
    }
    
    //Kills the unit, called when unit's HP is <= 0
    private void kill()
    {
        hp = mp = exp = 0;
        dead = moved = done = true;
    }
    
    //Displays damage in the unit
    private void damage(int dmg)
    {
        dmg = dmg > 999 ? 999 : dmg < 0 ? 0 : dmg;
        hp -= dmg;
        if(hp <= 0){
            kill();
        }
        dmgLen = dmg < 10 ? 1 : dmg < 100 ? 2 : 3;
        n[0] = dmg % 10;
        dmg -= n[0];
        n[1] = (dmg % 100) / 10;
        dmg -= n[1] * 10;
        n[2] = dmg / 100;
        //System.out.println(n[2] + " " + n[1] + " " + n[0]);
    }
    
    //Adds EXP to a unit, and causes level up if exp is >= 100
    private void addEXP(int a)
    {
        exp += a;
        if(exp >= 100)
        {
            exp -= 100;
            lv++;
        }
    }
    
    //Returns whether or not the unit is in range
    private boolean inRange(Unit unit)
    {
        int dist = Math.abs(unit.getX() - x) + Math.abs(unit.getY() - y);
        return dist >= getMinRANGE() && dist <= getMaxRANGE();
    }
    
    //Shows buttons that represent possible selections for unit
    private void listButtons(boolean enable)
    {
        int i = 0;
        for(Integer j : btList.keySet())
        {
            Game.gui.setButton(btList.get(j), j, i, !enable || done);
            i++;
        }
        for(; i < Game.gui.BCOUNT; i++)
        {
            Game.gui.setButton("", i, i, true);
        }
    }
    
    public void cloak()
    {
        
    }
    
    public void teleport()
    {
        
    }
    
    public void dash()
    {
        
    }
    
    public void gamble()
    {
        
    }
    
    public void eleShift()
    {
        
    }
    
    public void imbue()
    {
        
    }
    
    public void sacrifice()
    {
        
    }
    
    public void turtle()
    {
        
    }
    
    public void illusion()
    {
        
    }
    
    //Target skills
    public void mindControl()
    {
        
    }
    
    public void capture()
    {
        
    }
    
    public void setup()
    {
        
    }
    
    public void blockade()
    {
        
    }
    
    public void mend()
    {
        
    }
    
    public void reinforce()
    {
        
    }
    
    public void pickpocket()
    {
        
    }
    
    public void smash()
    {
        
    }
}
