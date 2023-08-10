package net.middlemind.GenAsm.Loaders.Thumb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.InvocationTargetException;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionLoader;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsTestFileVal;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsTestFileVals;
import net.middlemind.GenAsm.Loaders.Loader;

/**
 * A class used to load JSON test data.
 * @author Victor G. Brusca, Middlemind Games 08/13/2021 5:11 PM EST
 */
public class LoaderIsTestFileVals implements Loader {
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */
    public String obj_name = "LoaderIsTestFileVals";

    /**
     * A method used to parse and load JSON data files.
     * @param json              The contents of the JSON file to load.
     * @param targetClass       A full java class representation of the Java class to load the JSON data into.
     * @param fileName          The full path to the JSON data file to load.
     * @return                  A JsonObj instance the represents the JSON data loaded.
     * @throws ExceptionLoader  An exception is thrown if there is an issue during the JSON data load.
     */       
    @Override
    public JsonObjIsTestFileVals ParseJson(String json, String targetClass, String fileName) throws ExceptionLoader {
        GsonBuilder builder = new GsonBuilder(); 
        builder.setPrettyPrinting(); 
      
        Gson gson = builder.create();
        try {
            JsonObjIsTestFileVals jsonObj = (JsonObjIsTestFileVals)Class.forName(targetClass).getConstructor().newInstance();
            jsonObj = gson.fromJson(json, jsonObj.getClass());
            jsonObj.name = targetClass;
            jsonObj.fileName = fileName;
            jsonObj.loader = getClass().getName();

            for(JsonObjIsTestFileVal entry : jsonObj.is_test_file_vals) {
                entry.name = entry.getClass().getName();
                entry.fileName = fileName;
                entry.loader = getClass().getName();
            }
            
            return jsonObj;
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ExceptionLoader("Could not find target class, " + targetClass + ", in loader " + getClass().getName());
        }
    }    
}
