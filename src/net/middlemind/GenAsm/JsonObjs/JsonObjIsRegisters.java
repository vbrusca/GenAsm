package net.middlemind.GenAsm.JsonObjs;

import java.util.List;
import net.middlemind.GenAsm.Exceptions.ExceptionJsonObjLink;
import net.middlemind.GenAsm.Logger;
import net.middlemind.GenAsm.Utils;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/02/2021 12:58 PM EST
 */
public class JsonObjIsRegisters extends JsonObjBase {
    public String obj_name;
    public String set_name;
    public List<JsonObjIsRegister> is_registers;
    public static String REGISTER_CHAR_START = "R";
    public static String REGISTER_CHAR_RANGE = "-";
    public static String REGISTER_CHAR_SEPARATOR = ",";
    
    @Override
    public void Link(JsonObj linkData) throws ExceptionJsonObjLink {
        for(JsonObjIsRegister entry : is_registers) {
            boolean found = false;
            for(JsonObjIsEntryType lentry : ((JsonObjIsEntryTypes)linkData).is_entry_types) {
                if(!Utils.IsStringEmpty(lentry.type_name) && lentry.type_name.equals(entry.is_entry_type)) {
                    entry.linked_is_entry_type = lentry;
                    found = true;
                    break;
                }
            }

            if(!found) {
                throw new ExceptionJsonObjLink("JsonObjIsRegisters: Link: Error: Could not find JsonObjIsEntryType, group or single, object with name " + entry.is_entry_type);
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
        
        Logger.wrl(prefix + "IsRegisters:");
        for(JsonObjIsRegister entry : is_registers) {
            Logger.wrl("");
            entry.Print(prefix + "\t");
        }
    }    
}