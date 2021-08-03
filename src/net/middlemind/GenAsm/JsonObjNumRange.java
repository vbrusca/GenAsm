package net.middlemind.GenAsm;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/03/2021 5:13 AM EST
 */
public class JsonObjNumRange extends JsonObjBase {
    public int min_value;
    public int max_value;
    public int bit_len;
    public boolean twos_compliment;
    public String alignment;
    
    @Override
    public void Print() {
        Print("");
    }    
    
    @Override
    public void Print(String prefix) {
        super.Print(prefix);
        Logger.wrl(prefix + "MinValue: " + min_value);
        Logger.wrl(prefix + "MaxValue: " + max_value);
        Logger.wrl(prefix + "BitLen: " + bit_len);
        Logger.wrl(prefix + "TwosCompliment: " + twos_compliment);
        Logger.wrl(prefix + "Alignment: " + alignment);        
    }    
}
