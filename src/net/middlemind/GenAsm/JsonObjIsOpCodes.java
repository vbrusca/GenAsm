package net.middlemind.GenAsm;

import java.util.List;

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
