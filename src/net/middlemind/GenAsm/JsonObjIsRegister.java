package net.middlemind.GenAsm;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/02/2021 12:58 PM EST
 */
public class JsonObjIsRegister extends JsonObjBase {
    public String register_name;
    public String obj_name;
    public String is_entry_type;
    public JsonObjIsEntryType linked_is_entry_type;
    public JsonObjBitRep bit_rep;
    public String desc;
    
    @Override
    public void Print() {
        Print("");
    }    
    
    @Override
    public void Print(String prefix) {
        super.Print(prefix);
        Logger.wrl(prefix + "RegisterName: " + register_name);
        Logger.wrl(prefix + "ObjName: " + obj_name);
        Logger.wrl(prefix + "Description: " + desc);
        
        Logger.wrl(prefix + "BitRep:");
        bit_rep.Print(prefix + "\t");
    }     
}
