package net.middlemind.GenAsm.JsonObjs.Thumb;

import java.util.ArrayList;
import java.util.List;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionJsonObjLink;
import net.middlemind.GenAsm.JsonObjs.JsonObj;
import net.middlemind.GenAsm.JsonObjs.JsonObjBase;
import net.middlemind.GenAsm.Logger;
import net.middlemind.GenAsm.Utils;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/02/2021 10:21 AM EST
 */
@SuppressWarnings("Convert2Diamond")
public class JsonObjIsValidLines extends JsonObjBase {
    public static int ENTRY_LINE_EMPTY = 9;
    public static int[] ENTRY_LINES_LABEL_EMPTY = {1, 2, 3};
    public static int[] ENTRY_LINES_LABEL_DEF = {1, 2, 6, 8};
    
    public String obj_name;
    public String set_name;
    public int min_line_entries;
    public int max_line_entries;    
    public List<JsonObjIsValidLine> is_valid_lines;
    
    @Override
    public void Link(JsonObj linkData) throws ExceptionJsonObjLink {
        for(JsonObjIsValidLine entry : is_valid_lines) {
            for(JsonObjIsValidLineEntry lentry : entry.is_valid_line) {
                lentry.linked_is_entry_types = new ArrayList<JsonObjIsEntryType>();
                for(String s : lentry.is_entry_types) {
                    boolean found = false;
                    for(JsonObjIsEntryType llentry : ((JsonObjIsEntryTypes)linkData).is_entry_types) {
                        if(!Utils.IsStringEmpty(llentry.type_name) && llentry.type_name.equals(s)) {
                            lentry.linked_is_entry_types.add(llentry);
                            found = true;
                            break;
                        }
                    }
                                    
                    if(!found) {
                        throw new ExceptionJsonObjLink("JsonObjIsValidLines: Link: Error: Could not find JsonObjIsEntryType, group or single, object with name " + s);
                    }
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
