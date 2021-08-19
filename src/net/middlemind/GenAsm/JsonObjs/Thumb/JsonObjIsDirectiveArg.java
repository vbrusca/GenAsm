package net.middlemind.GenAsm.JsonObjs.Thumb;

import java.util.List;
import net.middlemind.GenAsm.JsonObjs.JsonObjBase;
import net.middlemind.GenAsm.Logger;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/13/2021 5:23 PM EST
 */
public class JsonObjIsDirectiveArg extends JsonObjBase {
    public String obj_name;
    public int arg_index;
    public List<String> is_entry_types;
    public List<JsonObjIsEntryType> linked_is_entry_types;
    public String is_arg_type;
    public List<String> is_arg_value;
        
    @Override
    public void Print() {
        Print("");
    }    
    
    @Override
    public void Print(String prefix) {
        super.Print(prefix);
        Logger.wrl(prefix + "ObjectName: " + obj_name);
        Logger.wrl(prefix + "ArgIndex: " + arg_index);
        Logger.wrl(prefix + "ArgType: " + is_arg_type);
        
        Logger.wrl(prefix + "ArgValue:");        
        for(String s : is_arg_value) {
            Logger.wrl(prefix + "\t" + s);
        }        
        
        Logger.wrl(prefix + "IsEntryTypes:");        
        for(String s : is_entry_types) {
            Logger.wrl(prefix + "\t" + s);
        }
        
        if(linked_is_entry_types != null) {
            Logger.wrl(prefix + "LinkedIsEntryTypes:");
            for(JsonObjIsEntryType entry : linked_is_entry_types) {
                Logger.wrl("");
                entry.Print(prefix + "\t");
            }
        } else {
            Logger.wrl(prefix + "\tnull");            
        }        
    }
}
