package net.middlemind.GenAsm;

import java.util.List;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/02/2021 10:24 AM EST
 */
public class JsonObjIsValidLine extends JsonObjBase {
    public String obj_name;
    public List<JsonObjIsValidLineEntry> is_valid_line;
    
    @Override
    public void Print() {
        Print("");
    }
    
    @Override
    public void Print(String prefix) {
        super.Print(prefix);
        Logger.wrl(prefix + "ObjName: " + obj_name);
                
        Logger.wrl(prefix + "IsValidLines:");
        for(JsonObjIsValidLineEntry entry : is_valid_line) {
            Logger.wrl("");
            entry.Print(prefix + "\t");
        }       
    }
}
