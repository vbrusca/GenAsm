package net.middlemind.GenAsm.JsonObjs;

import java.util.List;
import net.middlemind.GenAsm.Logger;

/**
 * A class used to represent sets of instruction set data files.
 * These JSON data files are necessary to assemble source files of different target types.
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 9:28 AM EST
 */
public class JsonObjIsSets extends JsonObjBase {
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */
    public String obj_name = "JsonObjIsSets";
    
    /**
     * A list of instruction sets to load in order to assemble source files of different types.
     */
    public List<JsonObjIsSet> is_sets;
    
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
        
        Logger.wrl(prefix + "IsSets:");
        for(JsonObjIsSet entry : is_sets) {
            Logger.wrl("");
            entry.Print(prefix + "\t");
        }
    }
}