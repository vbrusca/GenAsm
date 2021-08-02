package net.middlemind.GenAsm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/02/2021 9:28 AM EST
 */
public class LoaderIsEntryTypes implements Loader {

    @Override
    public JsonObjIsEntryTypes ParseJson(String json, String targetClass, String fileName) throws LoaderException {
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
            
            for(JsonObjIsEntryGroupType entry : jsonObj.is_entry_group_types) {
                entry.name = entry.getClass().getName();
                entry.fileName = fileName;
                entry.loader = getClass().getName();
                entry.txt_match.name = entry.txt_match.getClass().getName();
                entry.txt_match.fileName = fileName;
                entry.txt_match.loader = getClass().getName();
            }
            
            jsonObj.LinkGroups();
            
            return jsonObj;
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new LoaderException("Could not find target class, " + targetClass + ", in loader " + getClass().getName());
        }
    }
}
