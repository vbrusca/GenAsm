package net.middlemind.GenAsm.JsonObjs.Thumb;

import net.middlemind.GenAsm.JsonObjs.JsonObjBase;
import net.middlemind.GenAsm.JsonObjs.JsonObjBitRep;
import net.middlemind.GenAsm.Logger;

/**
 * A class used to represent an instruction set register.
 * @author Victor G. Brusca, Middlemind Games 08/02/2021 12:58 PM EST
 */
public class JsonObjIsRegister extends JsonObjBase {
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */ 
    public String obj_name;
    
    /**
     * A string representing the name of this register.
     */    
    public String register_name;    

    /**
     * A string representing the name of the instruction set entry type associated with this register.
     */
    public String is_entry_type;

    /**
     * An object instance of the linked instruction set entry type.
     * Object links are performed by name, string comparison.
     */    
    public JsonObjIsEntryType linked_is_entry_type;

    /**
     * An object that represents the binary expression of this instruction set op-code.
     */
    public JsonObjBitRep bit_rep;

    /**
     * A string representation that provides a description of this op-code.
     */
    public String desc;
    
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
        Logger.wrl(prefix + "RegisterName: " + register_name);        
        Logger.wrl(prefix + "Description: " + desc);
        
        Logger.wrl(prefix + "BitRep:");
        bit_rep.Print(prefix + "\t");
    }     
}
