package net.middlemind.GenAsm;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/02/2021 10:21 AM EST
 */
public class JsonObjIsValidLines extends JsonObjBase {
    public String obj_name;
    public String set_name;
    public int min_line_entries;
    public int max_line_entries;    
    public List<JsonObjIsValidLine> is_valid_lines;
    
    public void LinkValidLines(JsonObjIsEntryTypes entryTypes) {
        for(JsonObjIsValidLine entry : is_valid_lines) {
            entry.linked_is_entry_types = new ArrayList<JsonObjIsEntryType>();
            for(String s : entry.is_entry_types) {
                boolean found = false;
                for(JsonObjIsEntryType lentry : entryTypes.is_entry_types) {
                    if(!Utils.IsStringEmpty(lentry.type_name) && lentry.type_name.equals(s)) {
                        entry.linked_is_entry_types.add(lentry);
                        found = true;
                        break;
                    }
                }
                
                if(!found) {
                    for(JsonObjIsEntryGroupType lentry : entryTypes.is_entry_group_types) {
                        if(!Utils.IsStringEmpty(lentry.type_name) && lentry.type_name.equals(s)) {
                            entry.linked_is_entry_types.add(lentry);
                            found = true;
                            break;
                        }
                    }
                }
                
                if(!found) {
                    Logger.wrl("JsonObjIsValidLines: LinkValidLines: Warning: Could not find JsonObjIsEntryType, group or single, object with name " + s);
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
        Logger.wrl(prefix + "MinLineEntries: " + min_line_entries);
        Logger.wrl(prefix + "MaxLineEntries: " + max_line_entries);
        
        Logger.wrl(prefix + "IsValidLines:");        
        for(JsonObjIsValidLine entry : is_valid_lines) {
            Logger.wrl("");
            entry.Print(prefix + "\t");
        }
    }    
}
