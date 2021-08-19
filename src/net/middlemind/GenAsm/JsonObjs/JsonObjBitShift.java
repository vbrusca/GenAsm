package net.middlemind.GenAsm.JsonObjs;

import net.middlemind.GenAsm.Logger;

/**
 *
 * @author Victor G. Brusca, Middlemind Games  08/03/2021 5:14 AM EST
 */
public class JsonObjBitShift extends JsonObjBase {
    public String obj_name = "JsonObjBitSHift";
    public String shift_dir;
    public int shift_amount;
    
    @Override
    public void Print() {
        Print("");
    }    
    
    @Override
    public void Print(String prefix) {
        super.Print(prefix);
        Logger.wrl(prefix + "ObjName: " + obj_name);
        Logger.wrl(prefix + "ShiftDir: " + shift_dir);
        Logger.wrl(prefix + "ShiftAmount: " + shift_amount);
    }
}
