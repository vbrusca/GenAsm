package net.middlemind.GenAsm;

import java.util.List;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/02/2021 12:58 PM EST
 */
public class JsonObjIsRegisters extends JsonObjBase {
    public String obj_name;
    public String set_name;
    public List<JsonObjIsRegister> is_registers;
    
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
