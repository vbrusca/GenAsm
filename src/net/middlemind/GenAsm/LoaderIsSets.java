package net.middlemind.GenAsm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 6:59 AM EST
 */
public class LoaderIsSets implements Loader {

    @Override
    public JsonObjIsSets ParseJson(String json, String targetClass, String fileName) throws LoaderException {
        GsonBuilder builder = new GsonBuilder(); 
        builder.setPrettyPrinting(); 
      
        Gson gson = builder.create();
        try {
            JsonObjIsSets jsonObj = (JsonObjIsSets)Class.forName(targetClass).getConstructor().newInstance();
            jsonObj = gson.fromJson(json, jsonObj.getClass());
            jsonObj.name = targetClass;
            jsonObj.fileName = fileName;
            
            for(JsonObjIsSet entry : jsonObj.is_sets) {
                entry.name = entry.getClass().getName();
                entry.fileName = fileName;
                
                for(JsonObjIsFile fentry : entry.is_files) {
                    fentry.name = fentry.getClass().getName();
                    fentry.fileName = fileName;
                }
            }
            
            return jsonObj;
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new LoaderException("Could not find target class, " + targetClass + ", in loader " + getClass().getName());
        }
    }
}
