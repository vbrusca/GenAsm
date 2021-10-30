package net.middlemind.GenAsm.JsonObjs.Thumb;

import java.util.List;
import net.middlemind.GenAsm.JsonObjs.JsonObjBase;
import net.middlemind.GenAsm.Logger;

/**
 * A class used to represent a JSON instruction set argument type object.
 * @author Victor G. Brusca, Middlemind Games 08/02/2021 11:04 AM EST
 */
public class JsonObjIsArgType extends JsonObjBase {
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */
    public String obj_name;
    
    /**
     * A string representing the name of the instruction set argument type.
     */
    public String arg_name;
    
    /**
     * A list of strings representing the valid entry types that can be used to fill this argument. 
     */
    public List<String> is_entry_types;
    
    /**
     * A list of JsonObj instances that are the linked object version of the is_entry_types field.
     */
    public List<JsonObjIsEntryType> linked_is_entry_types;
    
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
        Logger.wrl(prefix + "ArgName: " + arg_name);        
        
        Logger.wrl(prefix + "IsEntryTypes:");        
        for(String s : is_entry_types) {
            Logger.wrl(prefix + "\t" + s);
        }

        if(linked_is_entry_types != null) {
            Logger.wrl(prefix + "LinkedIsEntryTypes:");
            for(JsonObjIsEntryType entry : linked_is_entry_types) {
                Logger.wrl(prefix + "\t" + entry.type_name);
            }
        }
    }
}
