package net.middlemind.GenAsm.JsonObjs.Thumb;

import java.util.ArrayList;
import java.util.List;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionJsonObjLink;
import net.middlemind.GenAsm.JsonObjs.JsonObj;
import net.middlemind.GenAsm.JsonObjs.JsonObjBase;
import net.middlemind.GenAsm.Logger;
import net.middlemind.GenAsm.Utils;

/**
 * A class used to represent the set of available valid lines for this instruction set.
 * @author Victor G. Brusca, Middlemind Games 08/02/2021 10:21 AM EST
 */
@SuppressWarnings("Convert2Diamond")
public class JsonObjIsValidLines extends JsonObjBase {
    /***
     * An integer representing the index of the empty line entry.
     */
    public static int LINE_EMPTY = 9;
    
    /**
     * An array of integers representing the indexes of all the label definition empty lines.
     */
    public static int[] LINES_LABEL_EMPTY = {1, 2, 3};
    
    /**
     * An array of integers representing the indexes of all the label definition lines.
     */
    public static int[] LINES_LABEL_DEF = {1, 2, 6, 8};
    
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */ 
    public String obj_name;
    
    /**
     * A string representation of the name of the set this JSON data file belongs to.
     */   
    public String set_name;

    /**
     * An integer representing the minimum number of line artifacts.
     */
    public int min_line_entries;

    /**
     * An integer representing the maximum number of line artifacts.
     */
    public int max_line_entries;    

    /**
     * A list of instances representing the set of valid lines for this instruction set.
     */
    public List<JsonObjIsValidLine> is_valid_lines;
    
    /**
     * A method used to link this JSON object with another loaded JSON object.
     * @param linkData              A JsonObj instance used as the link data to connect two JSON objects.
     * @throws ExceptionJsonObjLink An exception is thrown if there is an error finding the JSON object link.
     */     
    @Override
    public void Link(JsonObj linkData) throws ExceptionJsonObjLink {
        for(JsonObjIsValidLine entry : is_valid_lines) {
            for(JsonObjIsValidLineEntry lentry : entry.is_valid_line) {
                lentry.linked_is_entry_types = new ArrayList<JsonObjIsEntryType>();
                for(String s : lentry.is_entry_types) {
                    boolean found = false;
                    for(JsonObjIsEntryType llentry : ((JsonObjIsEntryTypes)linkData).is_entry_types) {
                        if(!Utils.IsStringEmpty(llentry.type_name) && llentry.type_name.equals(s)) {
                            lentry.linked_is_entry_types.add(llentry);
                            found = true;
                            break;
                        }
                    }
                                    
                    if(!found) {
                        throw new ExceptionJsonObjLink("JsonObjIsValidLines: Link: Error: Could not find JsonObjIsEntryType, group or single, object with name " + s);
                    }
                }
            }
        }
    }
    
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
        Logger.wrl(prefix + "MinLineEntries: " + min_line_entries);
        Logger.wrl(prefix + "MaxLineEntries: " + max_line_entries);
        
        Logger.wrl(prefix + "IsValidLines:");        
        for(JsonObjIsValidLine entry : is_valid_lines) {
            Logger.wrl("");
            entry.Print(prefix + "\t");
        }
    }    
}
