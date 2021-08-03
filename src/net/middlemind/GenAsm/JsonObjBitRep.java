package net.middlemind.GenAsm;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/02/2021 2:32 PM EST
 */
public class JsonObjBitRep extends JsonObjBase {
    public String bit_string;
    public int bit_int;
    public int bit_len;
    
    @Override
    public void Print() {
        Print("");
    }    
    
    @Override
    public void Print(String prefix) {
        super.Print(prefix);
        Logger.wrl(prefix + "BitString: " + bit_string);
        Logger.wrl(prefix + "BitInt: " + bit_int);
        Logger.wrl(prefix + "BitLen: " + bit_len);
    }    
}
