package algorithm;

public class Calculator
{
    public Calculator()
    {
        
    }
    
    public int expGain(int uLevel, int eLevel)
    {
        return 0;
    }
    
    public int dmgAmt(int uAtk, int eDef)
    {
        return uAtk - eDef;
    }
    
    public int critRate(int uCrit, int eLuk)
    {
        return Math.min(uCrit - eLuk, 100);
    }
}
