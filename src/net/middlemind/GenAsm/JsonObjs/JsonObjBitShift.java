package net.middlemind.GenAsm.JsonObjs;

import net.middlemind.GenAsm.Logger;

/**
 * A class used to represent a JSON bit shift object.
 * @author Victor G. Brusca, Middlemind Games  08/03/2021 5:14 AM EST
 */
public class JsonObjBitShift extends JsonObjBase {
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */
    public String obj_name = "JsonObjBitShift";
    
    /**
     * A string representation of the shift direction, LEFT or RIGHT.
     */
    public String shift_dir;
    
    /**
     * An integer representation of the number of bits to shift.
     */
    public int shift_amount;
    
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
        Logger.wrl(prefix + "ShiftDir: " + shift_dir);
        Logger.wrl(prefix + "ShiftAmount: " + shift_amount);
    }
}