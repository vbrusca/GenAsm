package net.middlemind.GenAsm.JsonObjs.Thumb;

import java.util.List;
import net.middlemind.GenAsm.JsonObjs.JsonObjBase;
import net.middlemind.GenAsm.Logger;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/13/2021 5:16 PM EST
 */
public class JsonObjIsDirective extends JsonObjBase {
    public String obj_name;
    public String directive_name;
    public int arg_len;
    public String description;
    public List<JsonObjIsDirectiveArg> args;
       
    @Override
    public void Print() {
        Print("");
    }    
    
    @Override
    public void Print(String prefix) {
        super.Print(prefix);
        Logger.wrl(prefix + "ObjectName: " + obj_name);
        Logger.wrl(prefix + "DirectiveName: " + directive_name);
        Logger.wrl(prefix + "ArgLen: " + arg_len);        
        
        Logger.wrl(prefix + "Args:");        
        for(JsonObjIsDirectiveArg entry : args) {
            Logger.wrl("");
            entry.Print(prefix + "\t");
        }
    }
}
