package net.middlemind.GenAsm.JsonObjs.Thumb;

import java.util.ArrayList;
import java.util.List;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionJsonObjLink;
import net.middlemind.GenAsm.JsonObjs.JsonObj;
import net.middlemind.GenAsm.JsonObjs.JsonObjBase;
import net.middlemind.GenAsm.JsonObjs.JsonObjBitSeries;
import net.middlemind.GenAsm.Logger;
import net.middlemind.GenAsm.Utils;

/**
 * A class used to represent a list of instruction set op-codes.
 * @author Victor G. Brusca, Middlemind Games 08/03/2021 5:01 AM EST
 */
public class JsonObjIsOpCodes extends JsonObjBase {
    /**
     * A static integer representing the instruction set's BX hi op-code indx.
     */
    public static int BX_HI_INDEX = 37;
    
    /**
     * A static integer representing the instruction set's BX low op-code indx.
     */
    public static int BX_LO_INDEX = 36;

    /**
     * A static integer representing the instruction set's BL op-code index.
     */
    public static int BL_INDEX = 81;

    /**
     * A static string representing the bin code entry, version one, of the instruction set's BL op-code.
     */
    public static String BL_OP_CODE_BIN_ENTRY_1 = "11110";
    
    /**
     * A static string representing the bin code entry, version two, of the instruction set's BL op-code.
     */
    public static String BL_OP_CODE_BIN_ENTRY_2 = "11111";
    
    /**
     * A string representing the name of the instruction set's BL op-code.
     */    
    public static String NAME_BL = "BL";
    
    /**
     * A string representing the binary representation of a particular instruction set ADD op-code.
     */    
    public static String ADD_OP_CODE_SPECIAL = "101100001";

    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */   
    public String obj_name;

    /**
     * A string representation of the name of the set this JSON data file belongs to.
     */   
    public String set_name;
    
    /**
     * A string representing the natural endianess of the instruction set, BIG and LITTLE are valid values.
     */    
    public String endian;
    
    /**
     * An integer representation of the number of pre-fetch bits.
     */    
    public int pc_prefetch_bits;
    
    /**
     * An integer representation of the number of pre-fetch bytes.
     */    
    public int pc_prefetch_bytes;
    
    /**
     * An integer representation of the number of pre-fetch half words.
     */    
    public int pc_prefetch_halfwords;
    
    /**
     * An integer representation of the number of pre-fetch words.
     */    
    public int pc_prefetch_words;
    
    /**
     * A string representation of the no op-code assembly representation.
     */    
    public String nop_assembly;
    
    /**
     * A string representation of the no op-code binary representation.
     */    
    public String nop_binary;
    
    /**
     * A string representation of the no op-code hex representation.
     */    
    public String nop_hex;
    
    /**
     * A string representation of the natural binary alignment, WORD or HALFWORD.
     */    
    public String pc_alignment;
    
    /**
     * A Boolean value indicating if the least significant bit should be zeroed.
     */    
    public boolean pc_lsb_zeroed;
    
    /**
     * An object representing the binary series needed to represent op-codes.
     */    
    public JsonObjBitSeries bit_series;
    
    /**
     * A list of instances representing the op-codes of this instruction set.
     */    
    public List<JsonObjIsOpCode> is_op_codes;
    
    /**
     * A method used to link this JSON object with another loaded JSON object.
     * @param linkData              A JsonObj instance used as the link data to connect two JSON objects.
     * @throws ExceptionJsonObjLink An exception is thrown if there is an error finding the JSON object link.
     */    
    @Override
    public void Link(JsonObj linkData) throws ExceptionJsonObjLink {
        for(JsonObjIsOpCode entry : is_op_codes) {
            for(JsonObjIsOpCodeArg lentry : entry.args) {
                boolean found = false;
                String lastEntryType = "";
                lentry.linked_is_entry_types = new ArrayList<>();
                for(JsonObjIsEntryType llentry : ((JsonObjIsEntryTypes)linkData).is_entry_types) {
                    lastEntryType = llentry.type_name;
                    for(String s : lentry.is_entry_types) {
                        if(!Utils.IsStringEmpty(s) && lentry.is_entry_types.contains(llentry.type_name)) {
                            lentry.linked_is_entry_types.add(llentry);
                            found = true;
                            break;
                        }
                    }
                }
                
                if(!found) {
                    throw new ExceptionJsonObjLink("JsonObjIsOpCodes: Link: Error: Could not find JsonObjIsEntryType object with name " + lastEntryType);
                }
                
                RecursiveSubArgLinking(lentry.sub_args, (JsonObjIsEntryTypes)linkData);
            }
        }
    }    
    
    /**
     * A method that recursively processes sub-arguments and links key class fields in string format to object instances with the same name.
     * @param sub_args              A list of op-code sub-arguments.
     * @param linkData              A data structure used in linking object instances by name.
     * @throws ExceptionJsonObjLink An exception is thrown if there is an issue during the recursive linking process.
     */
    private void RecursiveSubArgLinking(List<JsonObjIsOpCodeArg> sub_args, JsonObjIsEntryTypes linkData) throws ExceptionJsonObjLink {
        if(sub_args != null) {
            for(JsonObjIsOpCodeArg entry : sub_args) {
                boolean found = false;
                String lastEntryType = "";
                entry.linked_is_entry_types = new ArrayList<>();
                for(JsonObjIsEntryType lentry : ((JsonObjIsEntryTypes)linkData).is_entry_types) {
                    lastEntryType = lentry.type_name;
                    for(String s : entry.is_entry_types) {
                        if(!Utils.IsStringEmpty(s) && entry.is_entry_types.contains(lentry.type_name)) {
                            entry.linked_is_entry_types.add(lentry);
                            found = true;
                            break;
                        }
                    }
                }
                                
                if(!found) {
                    throw new ExceptionJsonObjLink("JsonObjIsOpCodes: RecursiveSubArgLinking: Error: Could not find JsonObjIsEntryType object with name " + lastEntryType);
                }
                
                RecursiveSubArgLinking(entry.sub_args, linkData);                
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
        Logger.wrl(prefix + "ObjName: " + obj_name);
        Logger.wrl(prefix + "SetName: " + set_name);
        Logger.wrl(prefix + "Endian: " + endian);
        Logger.wrl(prefix + "PcPrefetchBits: " + pc_prefetch_bits);
        Logger.wrl(prefix + "PcPrefetchBytes: " + pc_prefetch_bytes);        
        Logger.wrl(prefix + "PcPrefetchWords: " + pc_prefetch_words);
        Logger.wrl(prefix + "PcAlignment: " + pc_alignment);
        Logger.wrl(prefix + "PcLsbZeroed: " + pc_lsb_zeroed);        
        
        Logger.wrl(prefix + "BitSeries:");        
        bit_series.Print(prefix + "\t");
        
        Logger.wrl(prefix + "IsOpCodes:");
        for(JsonObjIsOpCode entry : is_op_codes) {
            Logger.wrl("");
            entry.Print(prefix + "\t");
        }
    }    
}