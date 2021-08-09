package net.middlemind.GenAsm;

import java.util.List;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/03/2021 5:07 AM EST
 */
public class JsonObjIsOpCode extends JsonObjBase {
    public static String DEFAULT_ARG_SEPARATOR = ",";
    public String obj_name;
    public String op_code_name;
    public JsonObjBitRep bit_rep;
    public JsonObjBitSeries bit_series;
    public String arg_separator;
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
        bit_rep.Print(prefix + "\t");
        
        Logger.wrl(prefix + "BitSeries:");
        bit_series.Print(prefix + "\t");
        
        Logger.wrl(prefix + "Args:");
        if(args != null) {
            for(JsonObjIsOpCodeArg entry : args) {
                Logger.wrl("");
                entry.Print(prefix + "\t");
            }
        } else {
            Logger.wrl(prefix + "\tnull");            
        }
    }    
}
