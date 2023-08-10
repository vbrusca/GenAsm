package net.middlemind.GenAsm.JsonObjs;

import net.middlemind.GenAsm.Logger;

/**
 * A class the is used to represent an instruction set file entry.
 * The object defines the loader and target full class names.
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 9:51 AM EST
 */
public class JsonObjIsFile extends JsonObjBase {
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */
    public String obj_name;

    /**
     * A string representing the path to the JSON data file to load.
     */
    public String path;
    
    /**
     * A string representing the full class name of the JSON object loader class.
     */
    public String loader_class;
    
    /**
     * A string representing the full class name of the target java class that will hold the JSON object data.
     */
    public String target_class;

    /**
     * A string representation of the category associated with this instruction set data file.
     */
    public String category;
    
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
        Logger.wrl(prefix + "Path: " + path);
        Logger.wrl(prefix + "LoaderClass: " + loader_class);
        Logger.wrl(prefix + "TargetClass: " + target_class);
        Logger.wrl(prefix + "ObjectName: " + category);        
    }
}