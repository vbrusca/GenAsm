package net.middlemind.GenAsm;

import java.util.List;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 9:28 AM EST
 */
public class JsonObjIsSets extends JsonObjBase {
    public String obj_name;
    public List<JsonObjIsSet> is_sets;
    
    @Override
    public void Print() {
        Print("");
    }    
    
    @Override
    public void Print(String prefix) {
        super.Print(prefix);
        Logger.wrl(prefix + "ObjectName: " + obj_name);
        Logger.wrl(prefix + "IsSets:");
        
        for(JsonObjIsSet entry : is_sets) {
            Logger.wrl("");
            entry.Print(prefix + "\t");
        }
    }
}
