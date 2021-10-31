package net.middlemind.GenAsm.JsonObjs.Thumb;

import java.util.List;
import net.middlemind.GenAsm.JsonObjs.JsonObjBase;
import net.middlemind.GenAsm.Logger;

/**
 * A class used to store a list of instruction set entry types.
 * @author Victor G. Brusca, Middlemind Games 07/31/2021 5:46 PM EST
 */
public class JsonObjIsEntryTypes extends JsonObjBase {
    /**
     * A static string that represents the write back register entry type name.
     */
    public static String NAME_REGISTERWB = "RegisterWb";
    
    /**
     * A static array of strings that represent the names of all register entry types.
     */
    public static String[] NAME_REGISTERS = {"RegisterWb", "RegisterLow", "RegisterHi", "RegisterSp", "RegisterPc", "RegisterLr"};

    /**
     * A static string representation of the op-code entry type name.
     */
    public static String NAME_OPCODE = "OpCode";
    
    /**
     * A static string representation of the directive entry type name.
     */
    public static String NAME_DIRECTIVE = "Directive";
    
    /**
     * A static string representation of the null or none entry type name.
     */
    public static String NAME_NULL = "None";
    
    /**
     * A static string representation of the label entry type name.
     */
    public static String NAME_LABEL = "Label";
    
    /**
     * A static string representation of the label reference entry type.
     */
    public static String NAME_LABEL_REF = "LabelRef";
    
    /**
     * A static string that represents the label reference address character.
     * Used to return the address of the label referenced.
     */
    public static char NAME_LABEL_REF_START_ADDRESS = '=';

    /**
     * A static string that represents the label reference value character.
     * Used to return the value, if available, of the label referenced.
     */
    public static char NAME_LABEL_REF_START_VALUE = '~';
    
    /**
     * A static string that represents the label address offset, from the current line, ignoring any pre-fetch values.
     */
    public static char NAME_LABEL_REF_START_OFFSET = '-';

    /**
     * A static string that represents the label address offset, from the current line, taking into account the pre-fetch.
     */
    public static char NAME_LABEL_REF_START_OFFSET_LESS_PREFETCH = '`';    
    
    /**
     * A static string that represents the comments entry type.
     */
    public static String NAME_COMMENT = "Comment";
    
    /**
     * A static string that represents the register range low entry type.
     */
    public static String NAME_REGISTER_RANGE_LOW = "RegisterRangeLow";
    
    /**
     * A static string that represents the register range hi entry type.
     */
    public static String NAME_REGISTER_RANGE_HI = "RegisterRangeHi";
    
    /**
     * A static string that represents the register low entry type.
     */
    public static String NAME_REGISTER_LOW = "RegisterLow";

    /**
     * A static string that represents the register hi entry type.
     */    
    public static String NAME_REGISTER_HI = "RegisterHi";
    
    /**
     * A static string that represents the register pc entry type.
     */    
    public static String NAME_REGISTER_PC = "RegisterPc";
    
    /**
     * A static string that represents the register lr entry type.
     */    
    public static String NAME_REGISTER_LR = "RegisterLr";
    
    /**
     * A static string that represents the register sp entry type.
     */    
    public static String NAME_REGISTER_SP = "RegisterSp";
    
    /**
     * A static string that represents the list start entry type.
     */    
    public static String NAME_START_LIST = "ListStart";
    
    /**
     * A static string that represents the group start entry type.
     */    
    public static String NAME_START_GROUP = "GroupStart";

    /**
     * A static string that represents the list stop entry type.
     */
    public static String NAME_STOP_LIST = "ListStop";
    
    /**
     * A static string that represents the group stop entry type.
     */    
    public static String NAME_STOP_GROUP = "GroupStop";
    
    /**
     * A static string that represents the op-code argument category.
     */
    public static String NAME_CAT_ARG_OPCODE = "Arg";

    /**
     * A static string that represents the argument directive category.
     */    
    public static String NAME_CAT_ARG_DIRECTIVE = "DirectiveArg";
    
    /**
     * A static string that represents the directive string entry type.
     */    
    public static String NAME_DIRECTIVE_STRING = "DirectiveString";
    
    /**
     * A static string that represents the number entry type.
     */    
    public static String NAME_NUMBER = "Number";
    
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */     
    public String obj_name;
    
    /**
     * A string representation of the name of the set this JSON data file belongs to.
     */    
    public String set_name;
    
    /**
     * A list of instruction set entry types instances used to link individual entry types to object instances by name.
     */
    public List<JsonObjIsEntryType> is_entry_types;
          
    /**
     * A method that is used to print a string representation of this JSON object to standard output.
     */    
    @Override
    public void Print() {
        Print("");
    }    
    
    /**
     * A method that is used to print a string representation of this JSON object to standard output with a string prefix.
     * @param prefix    A string that is used as a prefix to the string representation of this JSON object.
     */     
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
