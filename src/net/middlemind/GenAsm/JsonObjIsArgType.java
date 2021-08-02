package net.middlemind.GenAsm;

import java.util.List;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/02/2021 11:04 AM EST
 */
public class JsonObjIsArgType extends JsonObjBase {
    public String obj_name;
    public String arg_name;
    public List<String> is_entry_types;
    public List<JsonObjIsEntryType> linked_is_entry_types;
    
    @Override
    public void Print() {
        Print("");
    }    
    
    @Override
    public void Print(String prefix) {
        super.Print(prefix);
        Logger.wrl(prefix + "ObjectName: " + obj_name);
        Logger.wrl(prefix + "ArgName: " + arg_name);        
        
        Logger.wrl(prefix + "IsEntryTypes:");        
        for(String s : is_entry_types) {
            Logger.wrl(prefix + "\t" + s);
        }

        Logger.wrl(prefix + "LinkedIsEntryTypes:");        
        for(JsonObjIsEntryType entry : linked_is_entry_types) {
            Logger.wrl(prefix + "\t" + entry.type_name);
        }
    }    
}
