package net.middlemind.GenAsm.JsonObjs;

import java.util.List;
import net.middlemind.GenAsm.Logger;

/**
 * A class used to represent sets of instruction set data files.
 * These JSON data files are necessary to assemble source files of different target types.
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 9:28 AM EST
 */
public class JsonObjLineHexReps extends JsonObjBase {
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */
    public String obj_name = "JsonObjLineHexReps";
    
    /**
     * The name of the program that we have test values for.
     */
    public String program = "";
    
    /**
     * A string representing the starting line of the range of lines that are checked
     * by unit tests from the VASM listing.
     */
    public String vasm_range_start = "";
            
    /**
     * A string representing the stopping line of the range of lines that are checked
     * by unit tests from the VASM listing.
     */
    public String vasm_range_stop = "";    
    
    /**
     * A string representing the starting line of the range of lines that are checked
     * by unit tests from the GenAsm listing.
     */
    public String genasm_range_start = "";
    
    /**
     * A string representing the stopping line of the range of lines that are checked
     * by unit tests from the GenAsm listing.
     */
    public String genasm_range_stop = "";    
    
    /**
     * An integer representing the line offset difference between the GenAsm listing
     * and the VASM listing.
     */
    public int genasm_line_offset = 0;
    
    /**
     * An integer representing the label offset difference between the GenAsm listing
     * and the VASM listing.
     */
    public int genasm_label_offset_bytes = 0;
    
    /**
     * A list of instruction sets to load in order to assemble source files of different types.
     */
    public List<JsonObjLineHexRep> line_hex_reps;
    
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
        
        Logger.wrl(prefix + "LineHexRep:");
        for(JsonObjLineHexRep entry : line_hex_reps) {
            Logger.wrl("");
            entry.Print(prefix + "\t");
        }
    }
}