package net.middlemind.GenAsm;

import java.util.List;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/03/2021 5:08 AM EST
 */
public class JsonObjIsOpCodeArg extends JsonObjBase {
    public String obj_name;
    public int arg_index;
    public int bit_index;
    public String is_entry_type;
    public JsonObjIsEntryType linked_is_entry_type;
    public JsonObjBitSeries bit_series;
    public JsonObjNumRange num_range;
    public JsonObjBitShift bit_shift;
    public String sub_arg_separator;
    public List<JsonObjIsOpCodeArg> sub_args;
    
    @Override
    public void Print() {
        Print("");
    }    
    
    @Override
    public void Print(String prefix) {
        super.Print(prefix);
        Logger.wrl(prefix + "ObjName: " + obj_name);
        Logger.wrl(prefix + "ArgIndex: " + arg_index);
        Logger.wrl(prefix + "BitIndex: " + bit_index);
        Logger.wrl(prefix + "IsEntryType: " + is_entry_type);
        
        if(linked_is_entry_type != null) {
            Logger.wrl(prefix + "LinkedIsEntryType:");
            linked_is_entry_type.Print(prefix + "\t");
        }
        
        Logger.wrl(prefix + "BitSeries:");
        bit_series.Print(prefix + "\t");
        
        Logger.wrl(prefix + "NumRange:");
        if(num_range != null) {
            num_range.Print(prefix + "\t");
        } else {
            Logger.wrl(prefix + "\tnull");            
        }
        
        Logger.wrl(prefix + "BitShift:");
        if(bit_shift != null) {
            bit_shift.Print(prefix + "\t");
        } else {
            Logger.wrl(prefix + "\tnull");            
        }
        
        Logger.wrl(prefix + "SubArgSeparator: " + sub_arg_separator);

        Logger.wrl(prefix + "SubArgs:");
        if(sub_args != null) {
            for(JsonObjIsOpCodeArg entry : sub_args) {
                Logger.wrl("");
                entry.Print(prefix + "\t");
            }
        } else {
            Logger.wrl(prefix + "\tnull");
        }
    }
}
