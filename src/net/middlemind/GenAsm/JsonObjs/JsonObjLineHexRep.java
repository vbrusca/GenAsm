package net.middlemind.GenAsm.JsonObjs;

import net.middlemind.GenAsm.Logger;

/**
 * A class that represents a JSON line hex rep object.
 * @author Victor G. Brusca, Middlemind Games 11/301/2021 2:18 PM EST
 */
public class JsonObjLineHexRep extends JsonObjBase {
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */
    public String obj_name = "JsonObjLineHexRep";
    
    /**
     * A hex address.
     */
    public String addressHex;
    
    /**
     * The value at the associated hex address.
     */
    public String valueHex;
        
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
        Logger.wrl(prefix + "AddressHex: " + addressHex);
        Logger.wrl(prefix + "ValueHex: " + valueHex);
    }    
}