package net.middlemind.GenAsm.JsonObjs;

import net.middlemind.GenAsm.Logger;

/**
 * A class the represents a JSON number range object.
 * @author Victor G. Brusca, Middlemind Games 08/03/2021 5:13 AM EST
 */
public class JsonObjNumRange extends JsonObjBase {
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */
    public String obj_name = "JsonObjNumRange";
    
    /**
     * An integer representing the minimum value of the number range.
     */    
    public int min_value;
    
    /**
     * An integer representing the maximum value of the number range.
     */    
    public int max_value;
    
    /**
     * An integer representing the valid length of the binary representation of the number.
     */    
    public int bit_len;
    
    /**
     * A Boolean value indicating if the two's compliment of the number is required.
     */    
    public boolean twos_compliment;
    
    /**
     * A Boolean value indicating if the one's compliment of the number is required.
     */    
    public boolean ones_compliment;
    
    /**
     * A Boolean value indicating if the binary coded decimal value of the number is required.
     */    
    public boolean bcd_encoding;
    
    /**
     * A Boolean value indicating if the pre-fetch should be handled.
     */    
    public boolean handle_prefetch;
    
    /**
     * A Boolean value indicating if a half word offset should be enforced.
     */    
    public boolean use_halfword_offset;
    
    /**
     * A string representation of the alignment to be used for the num range, WORD, HALFWORD.
     */    
    public String alignment;
    
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
        Logger.wrl(prefix + "MinValue: " + min_value);
        Logger.wrl(prefix + "MaxValue: " + max_value);
        Logger.wrl(prefix + "BitLen: " + bit_len);
        Logger.wrl(prefix + "TwosCompliment: " + twos_compliment);
        Logger.wrl(prefix + "OnesCompliment: " + ones_compliment);
        Logger.wrl(prefix + "BcdEncoding: " + bcd_encoding);
        Logger.wrl(prefix + "HandlePrefetch: " + handle_prefetch);
        Logger.wrl(prefix + "UseHalfWordOffset: " + use_halfword_offset);        
        Logger.wrl(prefix + "Alignment: " + alignment);        
    }    
}
