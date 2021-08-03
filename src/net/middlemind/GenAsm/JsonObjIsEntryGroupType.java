package net.middlemind.GenAsm;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Victor G. Brusca, Middlemind Games  08/02/2021 7:27 PM EST
 */
public class JsonObjIsEntryGroupType extends JsonObjIsEntryType {
    public List<String> contains;
    public Map<String, JsonObjIsEntryType> linked_contains;
    
    @Override
    public void Print() {
        Print("");
    }    
    
    @Override
    public void Print(String prefix) {
        super.Print(prefix);
        Logger.wrl(prefix + "Contains:");        
        for(String s : contains) {
            Logger.wrl(prefix + "\t" + s);
        }

        if(linked_contains != null) {
            Logger.wrl(prefix + "LinkedContains:");        
            for(String s : linked_contains.keySet()) {
                Logger.wrl(prefix + "\t" + s);
            }
        }
    }    
}
