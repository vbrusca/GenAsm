package net.middlemind.GenAsm.JsonObjs;

import net.middlemind.GenAsm.Logger;

/**
 * A class used to represent a JSON bit rep object.
 * @author Victor G. Brusca, Middlemind Games 08/02/2021 2:32 PM EST
 */
public class JsonObjBitRep extends JsonObjBase {
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */
    public String obj_name = "JsonObjBitRep";
    
    /**
     * A string representation of a binary number.
     */
    public String bit_string;
    
    /**
     * An integer representation of a binary number.
     */
    public int bit_int;
    
    /**
     * An integer representing the length of this binary representation.
     */
    public int bit_len;
    
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
        Logger.wrl(prefix + "BitString: " + bit_string);
        Logger.wrl(prefix + "BitInt: " + bit_int);
        Logger.wrl(prefix + "BitLen: " + bit_len);
    }    
}
