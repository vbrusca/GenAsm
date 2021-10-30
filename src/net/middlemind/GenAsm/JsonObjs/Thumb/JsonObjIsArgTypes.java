package net.middlemind.GenAsm.JsonObjs.Thumb;

import java.util.ArrayList;
import java.util.List;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionJsonObjLink;
import net.middlemind.GenAsm.JsonObjs.JsonObj;
import net.middlemind.GenAsm.JsonObjs.JsonObjBase;
import net.middlemind.GenAsm.Logger;
import net.middlemind.GenAsm.Utils;

/**
 * A class used to represent a list of JSON instruction set argument type objects.
 * @author Victor G. Brusca, Middlemind Games 08/02/2021 11:04 AM EST
 */
@SuppressWarnings("Convert2Diamond")
public class JsonObjIsArgTypes extends JsonObjBase {
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */
    public String obj_name;
    
    /**
     * A string representation of the name of the set this JSON data file belongs to.
     */
    public String set_name;
    
    /**
     * A list of instruction set argument type instances linked by name.
     */
    public List<JsonObjIsArgType> is_arg_types;
    
    /**
     * A method used to link this JSON object with another loaded JSON object.
     * @param linkData              A JsonObj instance used as the link data to connect two JSON objects.
     * @throws ExceptionJsonObjLink An exception is thrown if there is an error finding the JSON object link.
     */
    @Override
    public void Link(JsonObj linkData) throws ExceptionJsonObjLink {
        for(JsonObjIsArgType entry : is_arg_types) {
            entry.linked_is_entry_types = new ArrayList<JsonObjIsEntryType>();
            for(String s : entry.is_entry_types) {
                boolean found = false;
                for(JsonObjIsEntryType lentry : ((JsonObjIsEntryTypes)linkData).is_entry_types) {
                    if(!Utils.IsStringEmpty(lentry.type_name) && lentry.type_name.equals(s)) {
                        entry.linked_is_entry_types.add(lentry);
                        found = true;
                        break;
                    }
                }
                                
                if(!found) {
                    throw new ExceptionJsonObjLink("JsonObjIsArgTypes: Link: Error: Could not find JsonObjIsEntryType, group or single, object with name " + s);
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
        
        Logger.wrl(prefix + "IsArgTypes:");        
        for(JsonObjIsArgType entry : is_arg_types) {
            Logger.wrl("");
            entry.Print(prefix + "\t");
        }
    }
}
