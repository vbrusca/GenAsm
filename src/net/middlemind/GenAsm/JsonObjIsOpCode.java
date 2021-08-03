package net.middlemind.GenAsm;

import java.util.List;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/03/2021 5:07 AM EST
 */
public class JsonObjIsOpCode extends JsonObjBase {
    public String obj_name;
    public String op_code_name;
    public JsonObjBitRep bit_rep;
    public JsonObjBitSeries bit_series;
    public List<JsonObjIsOpCodeArg> args;
    
    @Override
    public void Print() {
        Print("");
    }    
    
    @Override
    public void Print(String prefix) {
        super.Print(prefix);
        Logger.wrl(prefix + "ObjName: " + obj_name);
        Logger.wrl(prefix + "OpCodeName: " + op_code_name);
        
        Logger.wrl(prefix + "BitRep:");
        bit_rep.Print(prefix + "\n");
        
        Logger.wrl(prefix + "BitSeries:");
        bit_series.Print(prefix + "\n");
        
        Logger.wrl(prefix + "Args:");
        for(JsonObjIsOpCodeArg entry : args) {
            Logger.wrl("");
            entry.Print(prefix + "\n");
        }
    }    
}
