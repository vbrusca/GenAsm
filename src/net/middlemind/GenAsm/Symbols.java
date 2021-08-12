package net.middlemind.GenAsm;

import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/11/2021 7:27 AM EST
 */
public class Symbols {
    public static String RESERVED_LABEL_MAIN = "main";
    public static String RESERVED_LABEL_DATA = "data";
    
    public String obj_name = "Symbols";
    public Map<String, Symbol> symbols = new Hashtable<>();
}
