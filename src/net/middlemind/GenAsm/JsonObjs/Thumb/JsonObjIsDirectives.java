package net.middlemind.GenAsm.JsonObjs.Thumb;

import java.util.ArrayList;
import java.util.List;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionJsonObjLink;
import net.middlemind.GenAsm.JsonObjs.JsonObj;
import net.middlemind.GenAsm.JsonObjs.JsonObjBase;
import net.middlemind.GenAsm.Logger;
import net.middlemind.GenAsm.Utils;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/31/2021 5:46 PM EST
 */
public class JsonObjIsDirectives extends JsonObjBase {
    public static String NAME_ENTRY = "@ENTRY";
    public static String NAME_AREA = "@AREA";    
    public static String NAME_END = "@END";
    public static String NAME_TITLE = "@TTL";    
    public static String NAME_CODE = "@CODE";
    public static String NAME_DATA = "@DATA";
    public static String NAME_DCW = "@DCHW";
    public static String NAME_DCB = "@DCB";
    public static String[] LABEL_DIRECTIVES = new String[] { "@DCHW", "@DCB", "@EQU" };
    public static String NAME_READONLY = "@READONLY";
    public static String NAME_READWRITE = "@READWRITE";
    public static String NAME_DIRECTIVE_TYPE_STRING = "DirectiveString";
    public static String NAME_EQU = "@EQU";
    public static String NAME_ORG = "@ORG";    
    
    public String obj_name;
    public String set_name;
    public List<JsonObjIsDirective> is_directives;
    
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
