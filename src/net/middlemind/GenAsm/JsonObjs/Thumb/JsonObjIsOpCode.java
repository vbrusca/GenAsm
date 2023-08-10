package net.middlemind.GenAsm.JsonObjs.Thumb;

import java.util.List;
import net.middlemind.GenAsm.JsonObjs.JsonObjBase;
import net.middlemind.GenAsm.JsonObjs.JsonObjBitRep;
import net.middlemind.GenAsm.JsonObjs.JsonObjBitSeries;
import net.middlemind.GenAsm.Logger;

/**
 * A class used to represent an instruction set op-code JSON object.
 * @author Victor G. Brusca, Middlemind Games 08/03/2021 5:07 AM EST
 */
public class JsonObjIsOpCode extends JsonObjBase {
    /**
     * A static string representing the default arguments separator for instruction set op-codes.
     */
    public static String DEFAULT_ARG_SEPARATOR = ",";
    
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */
    public String obj_name;

    /**
     * A string representation of the op-code's name.
     */    
    public String op_code_name;

    /**
     * A integer representing the index of this op-code in the list of available instruction set op-codes.
     */
    public int index;
    
    /**
     * A string representing the argument separator for this op-code.
     */    
    public String arg_separator;
    
    /**
     * An integer representing the number of op-code argument objects associated with this op-code.
     */    
    public int arg_len;

    /**
     * A Boolean value indicating if this op-code writes to memory.
     */
    public boolean is_write_op_code;

    /**
     * An object representing the binary structure of this op-code.
     */
    public JsonObjBitRep bit_rep;

    /**
     * An object representing the start, stop, and length of a binary string representing this op-code.
     */
    public JsonObjBitSeries bit_series;

    /**
     * A list of arguments that are associated with this op-code.
     */    
    public List<JsonObjIsOpCodeArg> args;
    
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
        Logger.wrl(prefix + "OpCodeName: " + op_code_name);
        Logger.wrl(prefix + "Index: " + index);
        Logger.wrl(prefix + "ArgSeparator: " + arg_separator);
        Logger.wrl(prefix + "ArgLen: " + arg_len);
        Logger.wrl(prefix + "IsWriteOpCode: " + is_write_op_code);
        
        Logger.wrl(prefix + "BitRep:");
        bit_rep.Print(prefix + "\t");
        
        Logger.wrl(prefix + "BitSeries:");
        bit_series.Print(prefix + "\t");
        
        Logger.wrl(prefix + "Args:");
        if(args != null) {
            for(JsonObjIsOpCodeArg entry : args) {
                Logger.wrl("");
                entry.Print(prefix + "\t");
            }
        } else {
            Logger.wrl(prefix + "\tnull");            
        }
    }    
}
