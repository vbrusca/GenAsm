package net.middlemind.GenAsm.JsonObjs;

import java.util.ArrayList;
import java.util.List;
import net.middlemind.GenAsm.Exceptions.ExceptionJsonObjLink;
import net.middlemind.GenAsm.Logger;
import net.middlemind.GenAsm.Utils;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/03/2021 5:01 AM EST
 */
public class JsonObjIsOpCodes extends JsonObjBase {
    public String obj_name;
    public String set_name;
    public String endian;
    public int pc_prefetch_bits;
    public int pc_prefetch_bytes;
    public int pc_prefetch_words;
    public String pc_alignment;
    public boolean pc_lsb_zeroed;
    public JsonObjBitSeries bit_series;
    public List<JsonObjIsOpCode> is_op_codes;
    
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
    
    @Override
    public void Print() {
        Print("");
    }    
    
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
