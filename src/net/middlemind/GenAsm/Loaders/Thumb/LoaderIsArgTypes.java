package net.middlemind.GenAsm.Loaders.Thumb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.InvocationTargetException;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionLoader;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsArgType;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsArgTypes;
import net.middlemind.GenAsm.Loaders.Loader;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/02/2021 2:26 PM EST
 */
public class LoaderIsArgTypes implements Loader {
    public String obj_name = "LoaderIsArgTypes";
    
    @Override
    public JsonObjIsArgTypes ParseJson(String json, String targetClass, String fileName) throws ExceptionLoader {
        GsonBuilder builder = new GsonBuilder(); 
        builder.setPrettyPrinting(); 
      
        Gson gson = builder.create();
        try {
            JsonObjIsArgTypes jsonObj = (JsonObjIsArgTypes)Class.forName(targetClass).getConstructor().newInstance();
            jsonObj = gson.fromJson(json, jsonObj.getClass());
            jsonObj.name = targetClass;
            jsonObj.fileName = fileName;
            jsonObj.loader = getClass().getName();

            for(JsonObjIsArgType entry : jsonObj.is_arg_types) {
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