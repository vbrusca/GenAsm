package net.middlemind.GenAsm.JsonObjs.Thumb;

import java.util.List;
import net.middlemind.GenAsm.JsonObjs.JsonObjBase;
import net.middlemind.GenAsm.Logger;
import net.middlemind.GenAsm.Tokeners.TokenLine;

/**
 * A class used to represent the set of available valid lines for this instruction set.
 * @author Victor G. Brusca, Middlemind Games 11/12/2021 5:01 AM EST
 */
@SuppressWarnings("Convert2Diamond")
public class JsonObjIsEmptyDataLines extends JsonObjBase implements Cloneable {    
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */ 
    public String obj_name;
    
    /**
     * A string representation of the name of the set this JSON data file belongs to.
     */   
    public String set_name;

    /**
     * A list of instances representing the set of valid lines for this instruction set.
     */
    public List<TokenLine> is_empty_data_lines;
        
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
        Logger.wrl(prefix + "IsEmptyDataLines: " + is_empty_data_lines.size());        
    }
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }    
}
