package net.middlemind.GenAsm.JsonObjs;

import java.util.ArrayList;
import java.util.List;
import net.middlemind.GenAsm.Exceptions.ExceptionJsonObjLink;
import net.middlemind.GenAsm.Logger;
import net.middlemind.GenAsm.Utils;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/02/2021 11:04 AM EST
 */
public class JsonObjIsArgTypes extends JsonObjBase {
    public String obj_name;
    public String set_name;
    public List<JsonObjIsArgType> is_arg_types;
    
    @Override
    @SuppressWarnings("Convert2Diamond")
    public void Link(JsonObj linkData) throws ExceptionJsonObjLink {
        for(JsonObjIsArgType entry : is_arg_types) {
            entry.linked_is_entry_types = new ArrayList<JsonObjIsEntryType>();
            for(String s : entry.is_entry_types) {
                boolean found = false;
                for(JsonObjIsEntryType lentry : ((JsonObjIsEntryTypes)linkData).is_entry_types) {
                    if(!Utils.IsStringEmpty(lentry.type_name) && lentry.type_name.equals(s)) {
                        entry.linked_is_entry_types.add(lentry);
                        found = true;
                        break;
                    }
                }
                                
                if(!found) {
                    throw new ExceptionJsonObjLink("JsonObjIsArgTypes: Link: Error: Could not find JsonObjIsEntryType, group or single, object with name " + s);
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
        
        Logger.wrl(prefix + "IsArgTypes:");        
        for(JsonObjIsArgType entry : is_arg_types) {
            Logger.wrl("");
            entry.Print(prefix + "\t");
        }
    }
}
