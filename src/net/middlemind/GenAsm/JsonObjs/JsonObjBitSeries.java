package net.middlemind.GenAsm.JsonObjs;

import net.middlemind.GenAsm.Logger;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 8:16 AM EST
 */
public class JsonObjBitSeries extends JsonObjBase {
    public String obj_name = "JsonObjBitSeries";
    public int bit_start;
    public int bit_stop;
    public int bit_len;
    
    @Override
    public void Print() {
        Print("");
    }    
    
    @Override
    public void Print(String prefix) {
        super.Print(prefix);
        Logger.wrl(prefix + "ObjName: " + obj_name);
        Logger.wrl(prefix + "BitStart: " + bit_start);
        Logger.wrl(prefix + "BitStop: " + bit_stop);
        Logger.wrl(prefix + "BitLen: " + bit_len);
    }
    
    public void PrintShort() {
        PrintShort("");
    }
    
    public void PrintShort(String prefix) {
        Logger.wrl(prefix + "BitStart: " + bit_start);
        Logger.wrl(prefix + "BitStop: " + bit_stop);
        Logger.wrl(prefix + "BitLen: " + bit_len);
    }    
}
