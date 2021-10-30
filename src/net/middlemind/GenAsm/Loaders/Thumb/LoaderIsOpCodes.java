package net.middlemind.GenAsm.Loaders.Thumb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionLoader;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsOpCode;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsOpCodeArg;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsOpCodes;
import net.middlemind.GenAsm.Loaders.Loader;

/**
 * A class used to load JSON instruction set op-code object data.
 * @author Victor G. Brusca, Middlemind Games 08/04/2021 7:33 AM EST
 */
public class LoaderIsOpCodes implements Loader {
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */    
    public String obj_name = "LoaderIsOpCodes";

    /**
     * A method used to parse and load JSON data files.
     * @param json              The contents of the JSON file to load.
     * @param targetClass       A full java class representation of the Java class to load the JSON data into.
     * @param fileName          The full path to the JSON data file to load.
     * @return                  A JsonObj instance the represents the JSON data loaded.
     * @throws ExceptionLoader  An exception is thrown if there is an issue during the JSON data load.
     */       
    @Override
    public JsonObjIsOpCodes ParseJson(String json, String targetClass, String fileName) throws ExceptionLoader {
        GsonBuilder builder = new GsonBuilder(); 
        builder.setPrettyPrinting(); 
      
        Gson gson = builder.create();
        try {
            JsonObjIsOpCodes jsonObj = (JsonObjIsOpCodes)Class.forName(targetClass).getConstructor().newInstance();
            jsonObj = gson.fromJson(json, jsonObj.getClass());
            jsonObj.name = targetClass;
            jsonObj.fileName = fileName;
            jsonObj.loader = getClass().getName();

            jsonObj.bit_series.name = jsonObj.bit_series.getClass().getName();
            jsonObj.bit_series.fileName = fileName;
            jsonObj.bit_series.loader = getClass().getName();
            
            for(JsonObjIsOpCode entry : jsonObj.is_op_codes) {
                entry.name = entry.getClass().getName();
                entry.fileName = fileName;
                entry.loader = getClass().getName();
                entry.bit_rep.name = entry.bit_rep.getClass().getName();
                entry.bit_rep.fileName = fileName;
                entry.bit_rep.loader = getClass().getName();
                entry.bit_series.name = entry.bit_rep.getClass().getName();
                entry.bit_series.fileName = fileName;
                entry.bit_series.loader = getClass().getName();                

                if(entry.args != null) {
                    for(JsonObjIsOpCodeArg lentry : entry.args) {
                        lentry.name = lentry.getClass().getName();
                        lentry.fileName = fileName;
                        lentry.loader = getClass().getName();
                        lentry.bit_series.name = lentry.bit_series.getClass().getName();
                        lentry.bit_series.fileName = fileName;
                        lentry.bit_series.loader = getClass().getName();
                        
                        if(lentry.num_range != null) {
                            lentry.num_range.name = lentry.num_range.getClass().getName();
                            lentry.num_range.fileName = fileName;
                            lentry.num_range.loader = getClass().getName();
                        }
                        
                        if(lentry.bit_shift != null) {
                            lentry.bit_shift.name = lentry.bit_shift.getClass().getName();
                            lentry.bit_shift.fileName = fileName;
                            lentry.bit_shift.loader = getClass().getName();
                        }

                        if(lentry.sub_args != null) {
                            RecursiveSubArgProcessing(lentry.sub_args, fileName);
                        }
                    }
                }
            }
                                    
            return jsonObj;
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ExceptionLoader("Could not find target class, " + targetClass + ", in loader " + getClass().getName());
        }
    }
    
    /**
     * A method used to recursively process op-code sub-arguments.
     * @param sub_args  A list of op-code sub-arguments to process.
     * @param fileName  A string representing the file name of the JSON data file.
     */
    private void RecursiveSubArgProcessing(List<JsonObjIsOpCodeArg> sub_args, String fileName) {
        if(sub_args != null) {
            for(JsonObjIsOpCodeArg llentry : sub_args) {
                llentry.name = llentry.getClass().getName();
                llentry.fileName = fileName;
                llentry.loader = getClass().getName();
                llentry.bit_series.name = llentry.bit_series.getClass().getName();
                llentry.bit_series.fileName = fileName;
                llentry.bit_series.loader = getClass().getName();

                if(llentry.num_range != null) {
                    llentry.num_range.name = llentry.num_range.getClass().getName();
                    llentry.num_range.fileName = fileName;
                    llentry.num_range.loader = getClass().getName();
                }

                if(llentry.bit_shift != null) {
                    llentry.bit_shift.name = llentry.bit_shift.getClass().getName();
                    llentry.bit_shift.fileName = fileName;
                    llentry.bit_shift.loader = getClass().getName();
                }
                
                RecursiveSubArgProcessing(llentry.sub_args, fileName);
            }
        }
    }
}
