package net.middlemind.GenAsm;

import java.util.List;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/02/2021 7:37 AM EST
 */
public class JsonObjTxtMatch extends JsonObjBase {
    public List<String> starts_with;
    public List<String> contains;
    public List<String> ends_with;
    public static String special_wild_card = "*";
    public static String special_end_line = "endl";
    public static String special_range = "~";
    public static String special_lowercase_range = "a~z";
    public static String special_uppercase_range = "A~Z";    
    
    @Override
    public void Print() {
        Print("");
    }
    
    @Override
    public void Print(String prefix) {
        super.Print(prefix);
        
        Logger.wrl(prefix + "StartsWith:");
        for(String s : starts_with) {
            Logger.wrl(prefix + "\t" + s);
        }
        
        Logger.wrl(prefix + "Contains:");
        for(String s : contains) {
            Logger.wrl(prefix + "\t" + s);
        }
        
        Logger.wrl(prefix + "EndsWith:");        
        for(String s : ends_with) {
            Logger.wrl(prefix + "\t" + s);
        }        
    }    
}
