package net.middlemind.GenAsm.JsonObjs;

import java.util.List;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsFile;
import net.middlemind.GenAsm.Logger;

/**
 * A Java class the represents the is_sets.json file which holds data about all the JSON data files to load for the assembler run.
 * This class and associated JSON file must remain intact and is relied upon for the foundation used to build off of for customized assemblers.
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 9:50 AM EST
 */
public class JsonObjIsSet extends JsonObjBase {
    /**
     * 
     */
    public String obj_name = "JsonObjIsSet";
    
    /**
     * 
     */    
    public String set_name;
    
    /**
     * 
     */    
    public List<JsonObjIsFile> is_files;
    
    /**
     * 
     */
    @Override
    public void Print() {
        Print("");
    }

    /**
     * 
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
