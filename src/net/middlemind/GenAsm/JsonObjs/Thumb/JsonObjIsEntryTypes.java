package net.middlemind.GenAsm.JsonObjs.Thumb;

import java.util.List;
import net.middlemind.GenAsm.JsonObjs.JsonObjBase;
import net.middlemind.GenAsm.Logger;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/31/2021 5:46 PM EST
 */
public class JsonObjIsEntryTypes extends JsonObjBase {
    public static String ENTRY_TYPE_NAME_REGISTERWB = "RegisterWb";
    public static String[] ENTRY_TYPE_NAME_REGISTERS = {"RegisterWb", "RegisterLow", "RegisterHi", "RegisterSp", "RegisterPc", "RegisterLr"};
    public static String ENTRY_TYPE_NAME_OPCODE = "OpCode";
    public static String ENTRY_TYPE_NAME_DIRECTIVE = "Directive";    
    public static String ENTRY_TYPE_NAME_NULL = "None";
    public static String ENTRY_TYPE_NAME_LABEL = "Label";
    public static String ENTRY_TYPE_NAME_LABEL_NUMERIC_LOCAL = "LabelNumericLocal";
    public static String ENTRY_TYPE_NAME_LABEL_NUMERIC_LOCAL_REF = "LabelNumericLocalRef";    
    public static String ENTRY_TYPE_NAME_COMMENT = "Comment";
    public static String ENTRY_TYPE_NAME_REGISTER_RANGE_LOW = "RegisterRangeLow";
    public static String ENTRY_TYPE_NAME_REGISTER_RANGE_HI = "RegisterRangeHi";
    public static String ENTRY_TYPE_NAME_REGISTER_LOW = "RegisterLow";
    public static String ENTRY_TYPE_NAME_REGISTER_HI = "RegisterHi";
    public static String ENTRY_TYPE_NAME_REGISTER_PC = "RegisterPc";
    public static String ENTRY_TYPE_NAME_REGISTER_LR = "RegisterLr";
    public static String ENTRY_TYPE_NAME_REGISTER_SP = "RegisterSp";        
    public static String ENTRY_TYPE_NAME_START_LIST = "ListStart";
    public static String ENTRY_TYPE_NAME_START_GROUP = "GroupStart";
    public static String ENTRY_TYPE_NAME_STOP_LIST = "ListStop";
    public static String ENTRY_TYPE_NAME_STOP_GROUP = "GroupStop";
    public static String ENTRY_TYPE_NAME_CAT_ARG_OPCODE = "Arg";
    public static String ENTRY_TYPE_NAME_CAT_ARG_DIRECTIVE = "DirectiveArg";
    public static String ENTRY_TYPE_NAME_DIRECTIVE_STRING = "DirectiveString";
    public static String ENTRY_TYPE_NAME_NUMBER = "Number";
    
    public String obj_name;
    public String set_name;
    public List<JsonObjIsEntryType> is_entry_types;
            
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
