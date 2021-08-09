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
    public static String ENTRY_TYPE_NAME_NULL = "None";
    public static String ENTRY_TYPE_NAME_COMMENT = "Comment";
    public static String ENTRY_TYPE_NAME_REGISTER_RANGE_LOW = "RegisterRangeLow";
    public static String ENTRY_TYPE_NAME_REGISTER_RANGE_HI = "RegisterRangeHi";
    public static String ENTRY_TYPE_NAME_REGISTER_LOW = "RegisterLow";
    public static String ENTRY_TYPE_NAME_REGISTER_HI = "RegisterHi";
    public static String ENTRY_TYPE_NAME_START_LIST = "ListStart";
    public static String ENTRY_TYPE_NAME_START_GROUP = "GroupStart";
    public static String ENTRY_TYPE_NAME_STOP_LIST = "ListStop";
    public static String ENTRY_TYPE_NAME_STOP_GROUP = "GroupStop";    
        
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
    }
}
