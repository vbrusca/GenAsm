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
    public static String CHAR_START = "R";
    public static String CHAR_RANGE = "-";
    public static String CHAR_SEPARATOR = ",";
    public static String R_0 = "R0";
    public static String R_1 = "R1";
    public static String R_2 = "R2";    
    public static String R_3 = "R3";    
    public static String R_4 = "R4";    
    public static String R_5 = "R5";    
    public static String R_6 = "R6";    
    public static String R_7 = "R7";    
    public static String R_8 = "R8";    
    public static String R_9 = "R9";
    public static String R_10 = "R10";
    public static String R_11 = "R11";
    public static String R_12 = "R12";
    public static String R_13 = "R13";
    public static String R_14 = "R14";    
    public static String R_15 = "R15";
    public static String R_PC = "PC";    
    public static String R_LR = "LR";
    public static String R_SP = "SP";    
    
    public String obj_name;
    public String set_name;
    public List<JsonObjIsRegister> is_registers;
    
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
    
    @Override
    public void Print() {
        Print("");
    }    
    
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