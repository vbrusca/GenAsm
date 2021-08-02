package net.middlemind.GenAsm;

import java.util.Hashtable;
import java.util.List;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/31/2021 5:46 PM EST
 */
public class JsonObjIsEntryTypes extends JsonObjBase {
    public String obj_name;
    public String set_name;
    public List<JsonObjIsEntryType> is_entry_types;
    public List<JsonObjIsEntryGroupType> is_entry_group_types;    
    
    public void LinkGroups() {
        for(JsonObjIsEntryGroupType entry : is_entry_group_types) {
            entry.linkedContains = new Hashtable<String, JsonObjIsEntryType>();
            for(String s : entry.contains) {
                boolean found = false;
                for(JsonObjIsEntryType lentry : is_entry_types) {
                    if(!Utils.IsStringEmpty(lentry.type_name) && lentry.type_name.equals(s)) {
                        entry.linkedContains.put(s, lentry);
                        found = true;
                        break;
                    }
                }
                
                if(!found) {
                    Logger.wrl("JsonObjIsEntryTypes: LinkGroups: Warning: Could not find JsonObjIsEntryType object with name " + s);
                }
            }
        }
    }
    
    @Override
    public void Print() {
        Print("");
    }    
    
    @Override
    public void Print(String prefix) {
        super.Print(prefix);
        Logger.wrl(prefix + "ObjectName: " + obj_name);
        Logger.wrl(prefix + "SetName: " + set_name);
        
        Logger.wrl(prefix + "IsEntryTypes:");        
        for(JsonObjIsEntryType entry : is_entry_types) {
            Logger.wrl("");
            entry.Print(prefix + "\t");
        }
        
        Logger.wrl(prefix + "IsEntryGroupTypes:");        
        for(JsonObjIsEntryGroupType entry : is_entry_group_types) {
            Logger.wrl("");
            entry.Print(prefix + "\t");
        }        
    }
}
