package net.middlemind.GenAsm.Loaders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.InvocationTargetException;
import net.middlemind.GenAsm.Exceptions.ExceptionLoader;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsRegister;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsRegisters;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/02/2021 2:23 PM EST
 */
public class LoaderIsRegisters implements Loader {
    
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
