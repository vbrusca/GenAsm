package net.middlemind.GenAsm.JsonObjs;

import java.util.List;
import net.middlemind.GenAsm.Logger;

/**
 * A Java class the represents the is_sets.json file which holds data about all the JSON data files to load for the assembler run.
 * This class and associated JSON file must remain intact and is relied upon for the foundation used to build off of for customized assemblers.
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 9:50 AM EST
 */
public class JsonObjIsSet extends JsonObjBase {
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */
    public String obj_name = "JsonObjIsSet";
    
    /**
     * A string representation of the name of the set.
     * The GenAsm class looks for a set entry that matches the value of the ASM_TARGET_SET field.
     */    
    public String set_name;
    
    /**
     * A list of instruction set data file entries to load in order to assemble source files of that kind.
     */    
    public List<JsonObjIsFile> is_files;
    
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
        Logger.wrl(prefix + "SetName: " + set_name);
        
        Logger.wrl(prefix + "IsFiles:");
        for(JsonObjIsFile entry : is_files) {
            Logger.wrl("");
            entry.Print(prefix + "\t");
        }        
    }
}