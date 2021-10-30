package net.middlemind.GenAsm.JsonObjs.Thumb;

import java.util.List;
import net.middlemind.GenAsm.JsonObjs.JsonObjBase;
import net.middlemind.GenAsm.Logger;

/**
 * A class used to represent valid line entries for this instruction set.
 * @author Victor G. Brusca, Middlemind Games 08/02/2021 8:28 AM EST
 */
public class JsonObjIsValidLineEntry extends JsonObjBase {
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */
    public String obj_name;
    
    /**
     * A list of string representing the entry types allowed in this valid line entry.
     */
    public List<String> is_entry_types;
    
    /**
     * A list of instances representing the entry type objects associated with this valid line entry.
     */
    public List<JsonObjIsEntryType> linked_is_entry_types;
    
    /**
     * An integer representing the index of this valid line entry in the list of valid lines.
     */
    public int index;
    
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
        Logger.wrl(prefix + "Index: " + index);
        
        Logger.wrl(prefix + "IsEntryTypes:");
        for(String s : is_entry_types) {
            Logger.wrl(prefix + "\t" + s);
        }
        
        if(linked_is_entry_types != null) {
            Logger.wrl(prefix + "IsEntryTypes:");
            for(JsonObjIsEntryType entry : linked_is_entry_types) {
                Logger.wrl("");
                entry.Print(prefix + "\t");
            }
        }        
    }
}
