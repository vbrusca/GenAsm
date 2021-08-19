package net.middlemind.GenAsm.Loaders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.InvocationTargetException;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionLoader;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsFile;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsSet;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsSets;
import net.middlemind.GenAsm.Logger;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 6:59 AM EST
 */
public class LoaderIsSets implements Loader {
    public String obj_name = "LoaderIsSets";
    
    @Override
    public JsonObjIsSets ParseJson(String json, String targetClass, String fileName) throws ExceptionLoader {
        Logger.wrl("LoaderIsSets: ParseJson");
        GsonBuilder builder = new GsonBuilder(); 
        builder.setPrettyPrinting(); 
      
        Gson gson = builder.create();
        try {
            JsonObjIsSets jsonObj = (JsonObjIsSets)Class.forName(targetClass).getConstructor().newInstance();
            jsonObj = gson.fromJson(json, jsonObj.getClass());
            jsonObj.name = targetClass;
            jsonObj.fileName = fileName;
            jsonObj.loader = getClass().getName();

            for(JsonObjIsSet entry : jsonObj.is_sets) {
                entry.name = entry.getClass().getName();
                entry.fileName = fileName;
                entry.loader = getClass().getName();
                
                for(JsonObjIsFile fentry : entry.is_files) {
                    fentry.name = fentry.getClass().getName();
                    fentry.fileName = fileName;
                    fentry.loader = getClass().getName();
                }
            }
            
            return jsonObj;
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ExceptionLoader("Could not find target class, " + targetClass + ", in loader " + getClass().getName());
        }
    }
}
