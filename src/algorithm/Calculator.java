package algorithm;

public final class Calculator
{   
    //Exp formula
    public static int expGain(int uLevel, int eLevel)
    {
        return 20;
    }
    
    //Damage formula
    public static int dmgAmt(int uAtk, int eDef)
    {
        return uAtk - eDef;
    }
    
    //Crit rate formula
    public static int critRate(int uCrit, int eLuk)
    {
        return Math.min(uCrit - eLuk, 100);
    }
}
