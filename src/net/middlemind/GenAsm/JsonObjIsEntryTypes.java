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
    //public List<JsonObjIsEntryGroupTypeOLD> is_entry_group_types;
    public static String ENTRY_TYPE_NAME_NULL = "None";
    public static String ENTRY_TYPE_NAME_COMMENT = "Comment";
    public static String ENTRY_TYPE_NAME_REGISTER_RANGE_LOW = "RangisterRangeLow";
    public static String ENTRY_TYPE_NAME_REGISTER_RANGE_HI = "RangisterRangeHi";    
    
    /*
    @Override
    public void Link(JsonObj linkData) throws ExceptionJsonObjLink {
        for(JsonObjIsEntryGroupTypeOLD entry : is_entry_group_types) {
            entry.linked_contains = new Hashtable<String, JsonObjIsEntryType>();
            for(String s : entry.contains) {
                boolean found = false;
                for(JsonObjIsEntryType lentry : ((JsonObjIsEntryTypes)linkData).is_entry_types) {
                    if(!Utils.IsStringEmpty(lentry.type_name) && lentry.type_name.equals(s)) {
                        entry.linked_contains.put(s, lentry);
                        found = true;
                        break;
                    }
                }
                
                if(!found) {
                    throw new ExceptionJsonObjLink("JsonObjIsEntryTypes: Link: Error: Could not find JsonObjIsEntryType object with name " + s);
                }
            }
        }
    }
    */
    
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
        
        //Logger.wrl(prefix + "IsEntryGroupTypes:");        
        //for(JsonObjIsEntryGroupTypeOLD entry : is_entry_group_types) {
        //    Logger.wrl("");
        //    entry.Print(prefix + "\t");
        //}        
    }
}
