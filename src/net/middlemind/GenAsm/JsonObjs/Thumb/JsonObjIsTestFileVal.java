package net.middlemind.GenAsm.JsonObjs.Thumb;

import net.middlemind.GenAsm.JsonObjs.JsonObjBase;
import net.middlemind.GenAsm.Logger;

/**
 * A mock class used as part of a project test.
 * @author Victor G. Brusca, Middlemind Games 01/01/2022 7:25 PM EDT
 */
public class JsonObjIsTestFileVal extends JsonObjBase {
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */  
    public String obj_name;
    
    /**
     * A boolean value indicating that this is a test file.
     */
    public boolean is_fake_data;

           
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
        Logger.wrl(prefix + "IsFakeData: " + is_fake_data);
    }
}