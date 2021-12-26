package net.middlemind.GenAsm.JsonObjs.Thumb;

import java.util.ArrayList;
import java.util.List;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionJsonObjLink;
import net.middlemind.GenAsm.JsonObjs.JsonObj;
import net.middlemind.GenAsm.JsonObjs.JsonObjBase;
import net.middlemind.GenAsm.Logger;
import net.middlemind.GenAsm.Utils;

/**
 * A class used to hold a set of instruction set directive arguments instances for a given directive.
 * @author Victor G. Brusca, Middlemind Games 07/31/2021 5:46 PM EST
 */
public class JsonObjIsDirectives extends JsonObjBase {
    /**
     * A static string that represents the full name of an ENTRY directive.
     */
    public static String NAME_ENTRY = "@ENTRY";
    
    /**
     * A static string that represents the full name of an AREA directive.
     */    
    public static String NAME_AREA = "@AREA";    
    
    /**
     * A static string that represents the full name of an END directive.
     */    
    public static String NAME_END = "@END";
    
    /**
     * A static string that represents the full name of a TTL directive.
     */    
    public static String NAME_TITLE = "@TTL";    
    
    /**
     * A static string that represents the full name of a SUBT directive.
     */    
    public static String NAME_SUB_TITLE = "@SUBT";
    
    /**
     * A static string that represents the full name of a CODE directive.
     */    
    public static String NAME_CODE = "@CODE";
    
    /**
     * A static string that represents the full name of a DATA directive.
     */    
    public static String NAME_DATA = "@DATA";
    
    /**
     * A static string that represents the full name of a DCHW directive.
     */    
    public static String NAME_DCHW = "@DCHW";
    
    /**
     * A static string that represents the full name of a DCW0 directive.
     */    
    public static String NAME_DCWBF = "@DCWBF";    
    
    /**
     * A static string that represents the full name of a DCW1 directive.
     */    
    public static String NAME_DCWBS = "@DCWBS";        
    
    /**
     * A static string that represents the full name of a DCB directive.
     */    
    public static String NAME_DCB = "@DCB";
    
    /**
     * A static string that represents the full name of a flipped DHCW directive.
     */    
    public static String NAME_FLPDCHW = "@FLPDCHW";
        
    /**
     * A static string that represents which directives can be associated with labels.
     */    
    public static String[] LABEL_DIRECTIVES = new String[] { "@DCHW", "@DCB", "@EQU", "@FLPDCHW", "@DCWBF", "@DCWBS" };
    
    /**
     * A static string that represents the full name of a READONLY directive.
     */    
    public static String NAME_READONLY = "@READONLY";

    /**
     * A static string that represents the full name of a READWRITE directive.
     */    
    public static String NAME_READWRITE = "@READWRITE";

    /**
     * A static string that represents the name of the directive string type.
     */
    public static String NAME_DIRECTIVE_TYPE_STRING = "DirectiveString";

    /**
     * A static string that represents the full name of a EQU directive.
     */
    public static String NAME_EQU = "@EQU";

    /**
     * A static string that represents the full name of a ORG directive.
     */
    public static String NAME_ORG = "@ORG";    

    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */  
    public String obj_name;

    /**
     * A string representation of the name of the set this JSON data file belongs to.
     */
    public String set_name;

    /**
     * A list of instruction set directives used to link each individual directive to an object instance.
     */
    public List<JsonObjIsDirective> is_directives;

    /**
     * A method used to link this JSON object with another loaded JSON object.
     * @param linkData              A JsonObj instance used as the link data to connect two JSON objects.
     * @throws ExceptionJsonObjLink An exception is thrown if there is an error finding the JSON object link.
     */  
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
        
    /**
     * A method that is used to print a string representation of this JSON object to standard output.
     */   
    @Override
    public void Print() {
        Print("");
    }    

    /**
     * A method that is used to print a string representation of this JSON object to standard output with a string prefix.
     * @param prefix    A string that is used as a prefix to the string representation of this JSON object.
     */ 
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
