package net.middlemind.GenAsm.JsonObjs.Thumb;

import net.middlemind.GenAsm.JsonObjs.JsonObjBase;
import net.middlemind.GenAsm.Logger;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 9:51 AM EST
 */
public class JsonObjIsFile extends JsonObjBase {
    public String obj_name;
    public String path;
    public String loader_class;
    public String target_class;
    public String category;
    
    @Override
    public void Print() {
        Print("");
    }
    
    @Override
    public void Print(String prefix) {
        super.Print(prefix);
        Logger.wrl(prefix + "ObjectName: " + obj_name);
        Logger.wrl(prefix + "Path: " + path);
        Logger.wrl(prefix + "LoaderClass: " + loader_class);
        Logger.wrl(prefix + "TargetClass: " + target_class);
        Logger.wrl(prefix + "ObjectName: " + category);        
    }
}
