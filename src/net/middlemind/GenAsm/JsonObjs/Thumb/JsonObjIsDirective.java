package net.middlemind.GenAsm.JsonObjs.Thumb;

import java.util.List;
import net.middlemind.GenAsm.JsonObjs.JsonObjBase;
import net.middlemind.GenAsm.Logger;

/**
 * A class that represents the JSON directive object.
 * @author Victor G. Brusca, Middlemind Games 08/13/2021 5:16 PM EST
 */
public class JsonObjIsDirective extends JsonObjBase {
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */ 
    public String obj_name;
    
    /**
     * A string representation of the directive name.
     */
    public String directive_name;
    
    /**
     * An integer value indicating the number of associated directive arguments.
     */
    public int arg_len;
    
    /**
     * A string representation of the description of this directive.
     */
    public String description;
    
    /**
     * A list of instruction set directive arguments that are associated with this directive.
     */
    public List<JsonObjIsDirectiveArg> args;
       
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
        Logger.wrl(prefix + "DirectiveName: " + directive_name);
        Logger.wrl(prefix + "ArgLen: " + arg_len);        
        
        Logger.wrl(prefix + "Args:");        
        for(JsonObjIsDirectiveArg entry : args) {
            Logger.wrl("");
            entry.Print(prefix + "\t");
        }
    }
}
