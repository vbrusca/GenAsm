package net.middlemind.GenAsm.JsonObjs.Thumb;

import java.util.List;
import net.middlemind.GenAsm.JsonObjs.JsonObjBase;
import net.middlemind.GenAsm.Logger;

/**
 * A class used to represent the structure of a valid instruction set line.
 * @author Victor G. Brusca, Middlemind Games 08/02/2021 10:24 AM EST
 */
public class JsonObjIsValidLine extends JsonObjBase {
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */
    public String obj_name;
    
    /**
     * An integer representing the index of this object in the list of valid lines.
     */
    public int index;
    
    /**
     * A Boolean value indicating that this is an empty valid line.
     */
    public boolean empty_line;
    
    /**
     * A list of object instances representing this instruction set's valid lines.
     */
    public List<JsonObjIsValidLineEntry> is_valid_line;
    
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
                
        Logger.wrl(prefix + "IsValidLines:");
        for(JsonObjIsValidLineEntry entry : is_valid_line) {
            Logger.wrl("");
            entry.Print(prefix + "\t");
        }       
    }
}
