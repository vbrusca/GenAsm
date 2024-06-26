package net.middlemind.GenAsm.Loaders.Thumb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.InvocationTargetException;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionLoader;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsEntryType;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsEntryTypes;
import net.middlemind.GenAsm.Loaders.Loader;

/**
 * A class used to load JSON instruction set entry types object data.
 * @author Victor G. Brusca, Middlemind Games 08/02/2021 9:28 AM EST
 */
public class LoaderIsEntryTypes implements Loader {
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */    
    public String obj_name = "LoaderIsEntryTypes";

    /**
     * A method used to parse and load JSON data files.
     * @param json              The contents of the JSON file to load.
     * @param targetClass       A full java class representation of the Java class to load the JSON data into.
     * @param fileName          The full path to the JSON data file to load.
     * @return                  A JsonObj instance the represents the JSON data loaded.
     * @throws ExceptionLoader  An exception is thrown if there is an issue during the JSON data load.
     */     
    @Override
    public JsonObjIsEntryTypes ParseJson(String json, String targetClass, String fileName) throws ExceptionLoader {
        GsonBuilder builder = new GsonBuilder(); 
        builder.setPrettyPrinting(); 
      
        Gson gson = builder.create();
        try {
            JsonObjIsEntryTypes jsonObj = (JsonObjIsEntryTypes)Class.forName(targetClass).getConstructor().newInstance();
            jsonObj = gson.fromJson(json, jsonObj.getClass());
            jsonObj.name = targetClass;
            jsonObj.fileName = fileName;
            jsonObj.loader = getClass().getName();

            for(JsonObjIsEntryType entry : jsonObj.is_entry_types) {
                entry.name = entry.getClass().getName();
                entry.fileName = fileName;
                entry.loader = getClass().getName();
                entry.txt_match.name = entry.txt_match.getClass().getName();
                entry.txt_match.fileName = fileName;
                entry.txt_match.loader = getClass().getName();
            }
            
            return jsonObj;
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ExceptionLoader("Could not find target class, " + targetClass + ", in loader " + getClass().getName());
        }
    }
}
