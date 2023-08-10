package net.middlemind.GenAsm.Loaders.Thumb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.InvocationTargetException;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionLoader;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsRegister;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsRegisters;
import net.middlemind.GenAsm.Loaders.Loader;

/**
 * A class used to load JSON instruction set registers object data.
 * @author Victor G. Brusca, Middlemind Games 08/02/2021 2:23 PM EST
 */
public class LoaderIsRegisters implements Loader {
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */    
    public String obj_name = "LoaderIsRegisters";

    /**
     * A method used to parse and load JSON data files.
     * @param json              The contents of the JSON file to load.
     * @param targetClass       A full java class representation of the Java class to load the JSON data into.
     * @param fileName          The full path to the JSON data file to load.
     * @return                  A JsonObj instance the represents the JSON data loaded.
     * @throws ExceptionLoader  An exception is thrown if there is an issue during the JSON data load.
     */          
    @Override
    public JsonObjIsRegisters ParseJson(String json, String targetClass, String fileName) throws ExceptionLoader {
        GsonBuilder builder = new GsonBuilder(); 
        builder.setPrettyPrinting(); 
      
        Gson gson = builder.create();
        try {
            JsonObjIsRegisters jsonObj = (JsonObjIsRegisters)Class.forName(targetClass).getConstructor().newInstance();
            jsonObj = gson.fromJson(json, jsonObj.getClass());
            jsonObj.name = targetClass;
            jsonObj.fileName = fileName;
            jsonObj.loader = getClass().getName();

            for(JsonObjIsRegister entry : jsonObj.is_registers) {
                entry.name = entry.getClass().getName();
                entry.fileName = fileName;
                entry.loader = getClass().getName();
                
                entry.bit_rep.name = entry.getClass().getName();
                entry.bit_rep.fileName = fileName;
                entry.bit_rep.loader = getClass().getName();                
            }
            
            return jsonObj;
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ExceptionLoader("Could not find target class, " + targetClass + ", in loader " + getClass().getName());
        }
    }    
}
