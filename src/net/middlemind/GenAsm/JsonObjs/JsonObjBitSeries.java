package net.middlemind.GenAsm.JsonObjs;

import net.middlemind.GenAsm.Logger;

/**
 * A class used to represent a JSON bit series object.
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 8:16 AM EST
 */
public class JsonObjBitSeries extends JsonObjBase {
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */
    public String obj_name = "JsonObjBitSeries";
    
    /**
     * An integer representing the start index of the bit series representation.
     */
    public int bit_start;
    
    /**
     * An integer representing the stop index of the bit series representation.
     */
    public int bit_stop;
    
    /**
     * An integer representing the total length of the bit representation.
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
        Logger.wrl(prefix + "BitStart: " + bit_start);
        Logger.wrl(prefix + "BitStop: " + bit_stop);
        Logger.wrl(prefix + "BitLen: " + bit_len);
    }
    
    /**
     * A method that is used to print a short string representation of this JSON object to standard output.
     */
    public void PrintShort() {
        PrintShort("");
    }
    
    /**
     * A method that is used to print a short string representation of this JSON object to standard output with a string prefix.
     * @param prefix    A string that is used as a prefix to the string representation of this JSON object.
     */
    public void PrintShort(String prefix) {
        Logger.wrl(prefix + "BitStart: " + bit_start);
        Logger.wrl(prefix + "BitStop: " + bit_stop);
        Logger.wrl(prefix + "BitLen: " + bit_len);
    }    
}
