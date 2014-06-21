package character;

import graphics.Sprite;
import gridGame.Game;
import item.Inventory;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class Unit
{
    /* Private Variables
    x, y                - Unit x, y grid coordinates
    pX, pY              - Unit previous x, y grid coordinates
    dX, dY              - Unit x, y coordinates where unit is displayed
    n[]                 - Damage values to display
    xOff, yOff          - Offsets to simulate movement
    count               - counter variable
    dmgLen              - Number of digits in damage
    "..."               - Unit stat variables
    team                - team which the unit is on
    walkTick, dmgTick   - Ticks to time walking and damage taking
    inventory           - Inventory of unit
    dead, moved         - True if unit is dead, or unit has moved for the turn
    ClassID             - Class ID of the unit
    walkList            - List of tiles that the unit will walk through to get to its destination
    sprite              - Array of sprites
    */
    private int x, y, pX, pY, dX, dY, n[] = new int[3], xOff, yOff, count, dmgLen;
    private int lv, exp, hp, maxHP, mp, maxMP, ftg, mov, atk, mAtk, def, acc, avoid, crit, minRange, maxRange, team;
    private int walkTick, dmgTick;
    private Inventory inventory;
    private boolean dead, moved, done, attacking;
    private short classID;
    private ArrayList<int[]> walkList = new ArrayList<>();
    private TreeMap<Integer, String> btList = new TreeMap<>();;
    private Sprite sprite;
    
    /* Public Variables
    moving              - True while unit is moving
    */
    public boolean moving;
    
    //Dummy unit
    public Unit(short classID)
    {
        this.x = -1000;
        this.y = -1000;
        this.pX = x;
        this.pY = y;
        this.classID = classID;
    }
    
    public Unit(int x, int y, Sprite sprite, int MOV, int minRANGE, int maxRANGE, int TEAM)
    {
        this.x = x;
        this.y = y;
        this.pX = x;
        this.pY = y;
        this.dX = x * Game.TILESIZE;
        this.dY = y * Game.TILESIZE;
        this.sprite = sprite;
        this.mov = MOV != -1 ? MOV : 5; //Defaults to 5
        this.minRange = minRANGE != -1 ? minRANGE : 1; //Defaults to 1
        this.maxRange = maxRANGE != -1 ? maxRANGE : 2; //Defaults to 2
        this.team = TEAM;
        this.inventory = new Inventory(5);
        classID = 0;
        
        
        this.maxHP = 50;
        this.hp = 40;
        this.maxMP = 20;
        this.mp = 15;
        this.atk = 20;
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
            if(dmgTick > 120)
            {
                dmgLen = 0;
                dmgTick = 0;
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
                    xOff = 0;
                    yOff = 0;
                    walkTick = 0;
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
    }
    
    //Renders unit and damage on unit
    public void render(Graphics g)
    {
        if(dead) {
            g.drawImage(sprite.unit[0][8], Game.MAPOFFX + dX + xOff, Game.MAPOFFY + dY + yOff, Game.TILESIZE, Game.TILESIZE, null);
        }
        else if(done){
            g.drawImage(sprite.unit[0][7], Game.MAPOFFX + dX + xOff, Game.MAPOFFY + dY + yOff, Game.TILESIZE, Game.TILESIZE, null);
        }
        else{
            g.drawImage(sprite.unit[0][team], Game.MAPOFFX + dX + xOff, Game.MAPOFFY + dY + yOff, Game.TILESIZE, Game.TILESIZE, null);
        }
        
        switch(dmgLen)
        {
            case 1:
                g.drawImage(sprite.damage[n[0]], Game.MAPOFFX + dX + xOff + 11, Game.MAPOFFY + dY + yOff + 5, Sprite.dmwDIM, Sprite.dmhDIM, null);
                break;
            case 2:
                g.drawImage(sprite.damage[n[1]], Game.MAPOFFX + dX + xOff + 6, Game.MAPOFFY + dY + yOff + 5, Sprite.dmwDIM, Sprite.dmhDIM, null);
                g.drawImage(sprite.damage[n[0]], Game.MAPOFFX + dX + xOff + 16, Game.MAPOFFY + dY + yOff + 5, Sprite.dmwDIM, Sprite.dmhDIM, null);
                break;
            case 3:
                g.drawImage(sprite.damage[n[2]], Game.MAPOFFX + dX + xOff + 1, Game.MAPOFFY + dY + yOff + 5, Sprite.dmwDIM, Sprite.dmhDIM, null);
                g.drawImage(sprite.damage[n[1]], Game.MAPOFFX + dX + xOff + 11, Game.MAPOFFY + dY + yOff + 5, Sprite.dmwDIM, Sprite.dmhDIM, null);
                g.drawImage(sprite.damage[n[0]], Game.MAPOFFX + dX + xOff + 21, Game.MAPOFFY + dY + yOff + 5, Sprite.dmwDIM, Sprite.dmhDIM, null);
                break;
        }
    }
    
    public void listButtons(boolean enable)
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
    
    //Finishes unit's turn, no more changes
    public void done()
    {
        if(!moving)
        {
            Game.paths.clearPaths();
            listButtons(false);
            done = true;
            moved = true;
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
        if(x >= 0 && y >= 0 && x < Game.mapWidth && y < Game.mapHeight)
        {
            if(Game.paths.getMove(x, y) <= mov && Game.getUnit(x, y) == null)// && Game.paths.getMove(x, y) <= mov && mov != 0)
            {
                pX = this.x;
                pY = this.y;
                Game.moveUnit(x, y, this);
                this.walkList = walkList;
                this.x = x;
                this.y = y;
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
        if(moved && !done)
        {
            Game.moveUnit(pX, pY, this);
            dX = pX * Game.TILESIZE;
            dY = pY * Game.TILESIZE;
            this.x = pX;
            this.y = pY;
            attacking = false;
            moved = false;
            return true;
        }
        return false;
    }
    
    //Shows attackable tiles for the unit
    public boolean showAttack()
    {
        if(!done)
        {
            Game.paths.findPath(new int[]{x, y, 0, minRange, maxRange, team});
            moved = true;
            attacking = true;
            return true;
        }
        return false;
    }
    
    //Attacks enemy unit
    public boolean attack(Unit enemy)
    {
        if(attacking && enemy != null && this != enemy && inRange(enemy) && team != enemy.getTEAM())
        {
            int expGain = Game.math.expGain(lv, enemy.getLV());
            int tDmg = Game.math.dmgAmt(atk, enemy.getDEF());
            enemy.damage(tDmg);
            attacking = false;
            done();
            //addExp(expGain);
            return true;
        }
        return false;
    }
    
    //Returns whether or not the unit is in range
    private boolean inRange(Unit unit)
    {
        int dist = Math.abs(unit.getX() - x) + Math.abs(unit.getY() - y);
        return dist >= minRange && dist <= maxRange;
    }
    
    //Kills the unit, called when unit's HP is <= 0
    private void kill() {
        hp = 0;
        dead = true;
        moved = true;
        done = true;
    }
    
    //Displays damage in the unit
    public void damage(int dmg)
    {
        dmg = dmg > 999 ? 999 : dmg;
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
    
    //Lots of 'get' functions because no public variables
    public boolean hasMoved() {
        return moved;
    }
    public boolean isAttacking() {
        return attacking;
    }
    public boolean isDone() {
        return done;
    }
    public int[] pathInfo() {
        return new int[]{x, y, mov, minRange, maxRange, team};
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
    public int getEXP() {
        return exp;
    }
    public int getHP() {
        return hp;
    }
    public int getMaxHP() {
        return maxHP;
    }
    public int getMP() {
        return mp;
    }
    public int getMaxMP() {
        return maxMP;
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
    public int getMinRANGE() {
        return minRange;
    }
    public int getMaxRANGE() {
        return maxRange;
    }
    public int getTEAM() {
        return team;
    }
    public short getClassID() {
        return classID;
    }
}
