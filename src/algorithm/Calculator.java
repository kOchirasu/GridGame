package algorithm;

public class Calculator
{
    public Calculator()
    {
        //Doesn't really need a constructor right now
    }
    
    //Exp formula
    public int expGain(int uLevel, int eLevel)
    {
        return 0;
    }
    
    //Damage formula
    public int dmgAmt(int uAtk, int eDef)
    {
        return uAtk - eDef;
    }
    
    //Crit rate formula
    public int critRate(int uCrit, int eLuk)
    {
        return Math.min(uCrit - eLuk, 100);
    }
}
