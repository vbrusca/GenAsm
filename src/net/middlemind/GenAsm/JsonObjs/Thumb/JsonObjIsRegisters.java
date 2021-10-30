package net.middlemind.GenAsm.JsonObjs.Thumb;

import java.util.List;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionJsonObjLink;
import net.middlemind.GenAsm.JsonObjs.JsonObj;
import net.middlemind.GenAsm.JsonObjs.JsonObjBase;
import net.middlemind.GenAsm.Logger;
import net.middlemind.GenAsm.Utils;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/02/2021 12:58 PM EST
 */
public class JsonObjIsRegisters extends JsonObjBase {
    /**
     * A static string representing the starting character of a register op-code argument.
     */
    public static String CHAR_START = "R";
    
    /**
     * A static string representing the character used in defining a register range.
     */    
    public static String CHAR_RANGE = "-";

    /**
     * A static string representing the character used to represent the instruction set's op-code arguments.
     */
    public static String CHAR_SEPARATOR = ",";

    /**
     * A static string representing the register zero op-code argument.
     */
    public static String R_0 = "R0";

    /**
     * A static string representing the register one op-code argument.
     */
    public static String R_1 = "R1";

    /**
     * A static string representing the register two op-code argument.
     */
    public static String R_2 = "R2";    

    /**
     * A static string representing the register three op-code argument.
     */
    public static String R_3 = "R3";    

    /**
     * A static string representing the register four op-code argument.
     */
    public static String R_4 = "R4";    

    /**
     * A static string representing the register five op-code argument.
     */
    public static String R_5 = "R5";    

    /**
     * A static string representing the register six op-code argument.
     */
    public static String R_6 = "R6";    

    /**
     * A static string representing the register seven op-code argument.
     */
    public static String R_7 = "R7";    

    /**
     * A static string representing the register eight op-code argument.
     */
    public static String R_8 = "R8";    

    /**
     * A static string representing the register nine op-code argument.
     */
    public static String R_9 = "R9";

    /**
     * A static string representing the register ten op-code argument.
     */
    public static String R_10 = "R10";

    /**
     * A static string representing the register eleven op-code argument.
     */
    public static String R_11 = "R11";

    /**
     * A static string representing the register twelve op-code argument.
     */
    public static String R_12 = "R12";
    
    /**
     * A static string representing the register thirteen op-code argument.
     */    
    public static String R_13 = "R13";

    /**
     * A static string representing the register fourteen op-code argument.
     */
    public static String R_14 = "R14";    

    /**
     * A static string representing the register fifteen op-code argument.
     */
    public static String R_15 = "R15";

    /**
     * A static string representing the register PC op-code argument.
     */
    public static String R_PC = "PC";    

    /**
     * A static string representing the register LR op-code argument.
     */
    public static String R_LR = "LR";

    /**
     * A static string representing the register SP op-code argument.
     */
    public static String R_SP = "SP";    

    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */   
    public String obj_name;
 
    /**
     * A string representation of the name of the set this JSON data file belongs to.
     */ 
    public String set_name;

    /**
     * A list of instruction set register instances.
     */
    public List<JsonObjIsRegister> is_registers;
    
    /**
     * A method used to link this JSON object with another loaded JSON object.
     * @param linkData              A JsonObj instance used as the link data to connect two JSON objects.
     * @throws ExceptionJsonObjLink An exception is thrown if there is an error finding the JSON object link.
     */     
    @Override
    public void Link(JsonObj linkData) throws ExceptionJsonObjLink {
        for(JsonObjIsRegister entry : is_registers) {
            boolean found = false;
            for(JsonObjIsEntryType lentry : ((JsonObjIsEntryTypes)linkData).is_entry_types) {
                if(!Utils.IsStringEmpty(lentry.type_name) && lentry.type_name.equals(entry.is_entry_type)) {
                    entry.linked_is_entry_type = lentry;
                    found = true;
                    break;
                }
            }

            if(!found) {
                throw new ExceptionJsonObjLink("JsonObjIsRegisters: Link: Error: Could not find JsonObjIsEntryType, group or single, object with name " + entry.is_entry_type);
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
        
        Logger.wrl(prefix + "IsRegisters:");
        for(JsonObjIsRegister entry : is_registers) {
            Logger.wrl("");
            entry.Print(prefix + "\t");
        }
    }    
}