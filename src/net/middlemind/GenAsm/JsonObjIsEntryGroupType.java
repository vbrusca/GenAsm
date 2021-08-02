package net.middlemind.GenAsm;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Victor G. Brusca, Middlemind Games  08/02/2021 7:27 PM EST
 */
public class JsonObjIsEntryGroupType extends JsonObjBase {
    public String obj_name;
    public String type_name;
    public String category_class;
    public JsonObjTxtMatch txt_match;
    public List<String> contains;
    public Map<String, JsonObjIsEntryType> linkedContains;
    
    @Override
    public void Print() {
        Print("");
    }    
    
    @Override
    public void Print(String prefix) {
        super.Print(prefix);
        Logger.wrl(prefix + "ObjectName: " + obj_name);
        Logger.wrl(prefix + "TxtMatch: ");
        txt_match.Print(prefix + "\t");
        
        Logger.wrl(prefix + "Contains:");        
        for(String s : contains) {
            Logger.wrl(prefix + "\t" + s);
        }

        Logger.wrl(prefix + "LinkedContains:");        
        for(String s : linkedContains.keySet()) {
            Logger.wrl(prefix + "\t" + s);
        }        
    }    
}
