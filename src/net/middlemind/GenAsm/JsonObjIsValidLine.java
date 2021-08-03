package net.middlemind.GenAsm;

import java.util.List;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/02/2021 10:24 AM EST
 */
public class JsonObjIsValidLine extends JsonObjBase {
    public int index;
    public String obj_name;
    public List<JsonObjIsEntryType> linked_is_entry_types;
    public List<String> is_entry_types;
    
    @Override
    public void Print() {
        Print("");
    }
    
    @Override
    public void Print(String prefix) {
        super.Print(prefix);
        Logger.wrl(prefix + "Index: " + index);
        Logger.wrl(prefix + "ObjName: " + obj_name);
        
        Logger.wrl(prefix + "IsEntryTypes:");
        for(String s : is_entry_types) {
            Logger.wrl(prefix + "\t" + s);
        }
        
        if(linked_is_entry_types != null) {
            Logger.wrl(prefix + "IsEntryTypes:");
            for(JsonObjIsEntryType entry : linked_is_entry_types) {
                Logger.wrl("");
                entry.Print(prefix + "\t");
            }
        }        
    }
}
