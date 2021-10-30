package net.middlemind.GenAsm.JsonObjs;

import java.util.List;
import net.middlemind.GenAsm.Logger;

/**
 * A class that represents the JSON text match object.
 * @author Victor G. Brusca, Middlemind Games 08/02/2021 7:37 AM EST
 */
public class JsonObjTxtMatch extends JsonObjBase {
    /**
     * A static string representing the wildcard match string.
     * This will match on anything.
     */
    public static String special_wild_card = "*";
    
    /**
     * A static string representation of the end line match string.
     */
    public static String special_end_line = "endl";
    
    /**
     * A static string representation of the range delimiting string.
     */
    public static String special_range = "~";
    
    /**
     * A static string representation of the lower case character range.
     */
    public static String special_lowercase_range = "a~z";
    
    /**
     * A static representation of the upper case character range.
     */
    public static String special_uppercase_range = "A~Z";    
    
    /**
     * A static representation of the lowercase character and number range.
     */
    public static String special_lowercase_num_range = "a~z0~9";
    
    /**
     * A static representation of the uppercase character and number range.
     */
    public static String special_uppercase_num_range = "A~Z0~9";
    
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */
    public String obj_name = "JsonObjTxtMatch";
    
    /**
     * A list of strings used to match the start of the text.
     * Ranges supported.
     */
    public List<String> starts_with;
    
    /**
     * A list of strings used to match the middle of the text.
     * Ranges supported.
     */
    public List<String> contains;
    
    /**
     * A list of strings used to match the end of the text.
     * Ranges supported.
     */
    public List<String> ends_with;
    
    /**
     * A list of string used to match the middle of the text and must be matched.
     */
    public List<String> must_contain;
    
    /**
     * A list of string used to match the middle of the text and must NOT be matched.
     */
    public List<String> must_not_contain;    

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
        
        Logger.wrl(prefix + "StartsWith:");
        for(String s : starts_with) {
            Logger.wrl(prefix + "\t" + s);
        }
        
        Logger.wrl(prefix + "Contains:");
        for(String s : contains) {
            Logger.wrl(prefix + "\t" + s);
        }
        
        Logger.wrl(prefix + "MustContain:");
        for(String s : must_contain) {
            Logger.wrl(prefix + "\t" + s);
        }
        
        Logger.wrl(prefix + "MustNotContain:");
        for(String s : must_not_contain) {
            Logger.wrl(prefix + "\t" + s);
        }
        
        Logger.wrl(prefix + "EndsWith:");        
        for(String s : ends_with) {
            Logger.wrl(prefix + "\t" + s);
        }        
    }    
}
