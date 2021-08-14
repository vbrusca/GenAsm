package net.middlemind.GenAsm.JsonObjs;

import java.util.List;
import net.middlemind.GenAsm.Logger;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 9:50 AM EST
 */
public class JsonObjIsSet extends JsonObjBase {
    public String obj_name;
    public String set_name;
    public List<JsonObjIsFile> is_files;
    
    @Override
    public void Print() {
        Print("");
    }
    
    @Override
    public void Print(String prefix) {
        super.Print(prefix);
        Logger.wrl(prefix + "ObjectName: " + obj_name);
        Logger.wrl(prefix + "SetName: " + set_name);
        
        Logger.wrl(prefix + "IsFiles:");
        for(JsonObjIsFile entry : is_files) {
            Logger.wrl("");
            entry.Print(prefix + "\t");
        }        
    }
}
