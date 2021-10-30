package net.middlemind.GenAsm.Assemblers;

import java.util.Hashtable;
import java.util.Map;

/**
 * A class that holds all the symbols loaded from an assembly source file.
 * @author Victor G. Brusca, Middlemind Games 08/11/2021 7:27 AM EST
 */
public class Symbols {
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */
    public String obj_name = "Symbols";
    
    /**
     * A string to symbol map that holds a reference to all loaded symbols by their name.
     */
    public Map<String, Symbol> symbols = new Hashtable<>();
}
