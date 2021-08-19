package net.middlemind.GenAsm.JsonObjs.Thumb;

import net.middlemind.GenAsm.JsonObjs.JsonObjBase;
import net.middlemind.GenAsm.JsonObjs.JsonObjTxtMatch;
import net.middlemind.GenAsm.Logger;

/**
 *
 * @author Victor G. Brusca, Middlemind Games  07/31/2021 5:47 PM EST
 */
public class JsonObjIsEntryType extends JsonObjBase {
    public String obj_name;
    public String type_name;
    public String type_category;
    public String category;
    public String category_class;
    public JsonObjTxtMatch txt_match;
    
    @Override
    public void Print() {
        Print("");
    }    
    
    @Override
    public void Print(String prefix) {
        super.Print(prefix);
        Logger.wrl(prefix + "ObjectName: " + obj_name);
        Logger.wrl(prefix + "TypeName: " + type_name);
        Logger.wrl(prefix + "TypeCategory: " + type_category);
        
        Logger.wrl(prefix + "TxtMatch: ");
        txt_match.Print(prefix + "\t");        
    }    
}
