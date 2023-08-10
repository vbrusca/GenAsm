package net.middlemind.GenAsm.JsonObjs.Thumb;

import java.util.List;
import net.middlemind.GenAsm.JsonObjs.JsonObjBase;
import net.middlemind.GenAsm.JsonObjs.JsonObjBitSeries;
import net.middlemind.GenAsm.JsonObjs.JsonObjBitShift;
import net.middlemind.GenAsm.JsonObjs.JsonObjNumRange;
import net.middlemind.GenAsm.Logger;

/**
 * A class used to represent an instruction set op-code argument.
 * @author Victor G. Brusca, Middlemind Games 08/03/2021 5:08 AM EST
 */
public class JsonObjIsOpCodeArg extends JsonObjBase {
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */  
    public String obj_name;
    
    /**
     * An integer representing the argument index.
     * This is the index of the argument when expressed in source code.
     */
    public int arg_index;
    
    /**
     * An integer representing the arguments bit representation index.
     * This is the index in the final binary representation of the op-code.
     */    
    public int bit_index;

    /**
     * A list of strings that represent the valid entry types for this instruction set op-code argument.
     */
    public List<String> is_entry_types;

    /**
     * A list of instances that represent the valid entry types for this instruction set op-code argument.
     */
    public List<JsonObjIsEntryType> linked_is_entry_types;

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
     * A string that represents the character separator used for sub-arguments.
     * These type of arguments are typically found in group and list arguments.
     */
    public String sub_arg_separator;

    /**
     * A list of instances that represent the sub-arguments associated with this op-code argument.
     */
    public List<JsonObjIsOpCodeArg> sub_args;
    
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
        Logger.wrl(prefix + "ObjName: " + obj_name);
        Logger.wrl(prefix + "ArgIndex: " + arg_index);
        Logger.wrl(prefix + "BitIndex: " + bit_index);
        Logger.wrl(prefix + "IsEntryType:");
        if(is_entry_types != null) {
            for(String s : is_entry_types) {
                Logger.wrl(prefix + "\t" + s);
            }
        } else {
            Logger.wrl(prefix + "\tnull");
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
        
        Logger.wrl(prefix + "BitSeries:");
        bit_series.Print(prefix + "\t");
        
        Logger.wrl(prefix + "NumRange:");
        if(num_range != null) {
            num_range.Print(prefix + "\t");
        } else {
            Logger.wrl(prefix + "\tnull");            
        }
        
        Logger.wrl(prefix + "BitShift:");
        if(bit_shift != null) {
            bit_shift.Print(prefix + "\t");
        } else {
            Logger.wrl(prefix + "\tnull");            
        }
        
        Logger.wrl(prefix + "SubArgSeparator: " + sub_arg_separator);

        Logger.wrl(prefix + "SubArgs:");
        if(sub_args != null) {
            for(JsonObjIsOpCodeArg entry : sub_args) {
                Logger.wrl("");
                entry.Print(prefix + "\t");
            }
        } else {
            Logger.wrl(prefix + "\tnull");
        }
    }
}
