package net.middlemind.GenAsm.JsonObjs.Thumb;

import net.middlemind.GenAsm.JsonObjs.JsonObjBase;
import net.middlemind.GenAsm.JsonObjs.JsonObjTxtMatch;
import net.middlemind.GenAsm.Logger;

/**
 * A class used to represent an instruction set entry type.
 * @author Victor G. Brusca, Middlemind Games  07/31/2021 5:47 PM EST
 */
public class JsonObjIsEntryType extends JsonObjBase {
    /**
     * 
     */
    public String obj_name;
    
    /**
     * 
     */    
    public String type_name;

    /**
     * 
     */
    public String type_category;

    /**
     * 
     */    
    public String category;
    
    /**
     * 
     */    
    public String category_class;

    /**
     * 
     */
    public JsonObjTxtMatch txt_match;
    
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
        Logger.wrl(prefix + "TypeName: " + type_name);
        Logger.wrl(prefix + "TypeCategory: " + type_category);
        
        Logger.wrl(prefix + "TxtMatch: ");
        txt_match.Print(prefix + "\t");        
    }    
}
