package net.middlemind.GenAsm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 8:35 AM EST
 */
public class LoaderBitSeries implements Loader {

    @Override
    public JsonObjBitSeries ParseJson(String json, String targetClass, String fileName) throws LoaderException {
        GsonBuilder builder = new GsonBuilder(); 
        builder.setPrettyPrinting(); 
      
        Gson gson = builder.create();
        try {
            JsonObjBitSeries jsonObj = (JsonObjBitSeries)Class.forName(targetClass).getConstructor().newInstance();
            jsonObj = gson.fromJson(json, jsonObj.getClass());
            jsonObj.name = targetClass;
            jsonObj.fileName = fileName;
            jsonObj.loader = getClass().getName();
            return jsonObj;
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new LoaderException("Could not find target class, " + targetClass + ", in loader " + getClass().getName());
        }
    }    
}
