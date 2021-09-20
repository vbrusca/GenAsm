package net.middlemind.GenAsm.JsonObjs;

import net.middlemind.GenAsm.Logger;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/03/2021 5:13 AM EST
 */
public class JsonObjNumRange extends JsonObjBase {
    public String obj_name = "JsonObjNumRange";
    public int min_value;
    public int max_value;
    public int bit_len;
    public boolean twos_compliment;
    public boolean ones_compliment;
    public boolean bcd_encoding;
    public boolean handle_prefetch;
    public boolean use_halfword_offset;
    public String alignment;
    
    @Override
    public void Print() {
        Print("");
    }    
    
    @Override
    public void Print(String prefix) {
        super.Print(prefix);
        Logger.wrl(prefix + "ObjName: " + obj_name);
        Logger.wrl(prefix + "MinValue: " + min_value);
        Logger.wrl(prefix + "MaxValue: " + max_value);
        Logger.wrl(prefix + "BitLen: " + bit_len);
        Logger.wrl(prefix + "TwosCompliment: " + twos_compliment);
        Logger.wrl(prefix + "OnesCompliment: " + ones_compliment);
        Logger.wrl(prefix + "BcdEncoding: " + bcd_encoding);
        Logger.wrl(prefix + "HandlePrefetch: " + handle_prefetch);
        Logger.wrl(prefix + "UseHalfWordOffset: " + use_halfword_offset);        
        Logger.wrl(prefix + "Alignment: " + alignment);        
    }    
}
