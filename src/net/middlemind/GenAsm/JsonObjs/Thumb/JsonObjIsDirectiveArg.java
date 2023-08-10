package net.middlemind.GenAsm.JsonObjs.Thumb;

import java.util.List;
import net.middlemind.GenAsm.JsonObjs.JsonObjBase;
import net.middlemind.GenAsm.JsonObjs.JsonObjBitSeries;
import net.middlemind.GenAsm.JsonObjs.JsonObjBitShift;
import net.middlemind.GenAsm.JsonObjs.JsonObjNumRange;
import net.middlemind.GenAsm.Logger;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/13/2021 5:23 PM EST
 */
public class JsonObjIsDirectiveArg extends JsonObjBase {
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */
    public String obj_name;
    
    /**
     * An integer representing the index of this directive argument.
     */
    public int arg_index;
    
    /**
     * A list of strings representing the valid entry types that can fill this argument.
     */
    public List<String> is_entry_types;
    
    /**
     * A list of instruction set entry type instances that are linked by the names in the is_entry_types field.
     */
    public List<JsonObjIsEntryType> linked_is_entry_types;
    
    /**
     * A string representation of the instruction set argument type.
     */
    public String is_arg_type;
    
    /**
     * A list of strings that are allowed values for this directive argument.
     */
    public List<String> is_arg_value;
       
    /**
     * An object that represents the start, stop, and length of the binary representation of this instruction set op-code argument.
     */
    public JsonObjBitSeries bit_series;

    /**
     * An object that represents the valid number range of this instruction set argument if applicable.
     */
    public JsonObjNumRange num_range;

    /**
     * An object that represents the bit shift, if applicable, applied to this instruction set op-code argument.
     */
    public JsonObjBitShift bit_shift;
    
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
