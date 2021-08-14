package net.middlemind.GenAsm;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/31/2021 5:46 PM EST
 */
public class JsonObjIsDirectives extends JsonObjBase {
    public String obj_name;
    public String set_name;
    public List<JsonObjIsDirective> is_directives;
    public static String DIRECTIVE_NAME_ENTRY = "@ENTRY";
    public static String DIRECTIVE_NAME_END = "@END";    
 
    @Override
    public void Link(JsonObj linkData) throws ExceptionJsonObjLink {
        for(JsonObjIsDirective entry : is_directives) {
            for(JsonObjIsDirectiveArg lentry : entry.args) {
                boolean found = false;
                String lastEntryType = "";
                lentry.linked_is_entry_types = new ArrayList<>();
                for(JsonObjIsEntryType llentry : ((JsonObjIsEntryTypes)linkData).is_entry_types) {
                    lastEntryType = llentry.type_name;
                    for(String s : lentry.is_entry_types) {
                        if(!Utils.IsStringEmpty(s) && lentry.is_entry_types.contains(llentry.type_name)) {
                            lentry.linked_is_entry_types.add(llentry);
                            found = true;
                            break;
                        }
                    }
                }
                
                if(!found) {
                    throw new ExceptionJsonObjLink("JsonObjIsDirectives: Link: Error: Could not find JsonObjIsEntryType object with name " + lastEntryType);
                }
            }
        }
    }    
        
    @Override
    public void Print() {
        Print("");
    }    
    
    @Override
    public void Print(String prefix) {
        super.Print(prefix);
        Logger.wrl(prefix + "ObjectName: " + obj_name);
        Logger.wrl(prefix + "SetName: " + set_name);
        
        Logger.wrl(prefix + "IsDirectives:");        
        for(JsonObjIsDirective entry : is_directives) {
            Logger.wrl("");
            entry.Print(prefix + "\t");
        }
    }
}