package net.middlemind.GenAsm;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07-19-2021 8:51 AM EST
 */
public class GenAsm {
    public static String ASM_SETS_FILE_NAME = "";
    public static String ASM_TARGET_SET = "";
    public static String ASM_SETS_LOADER_CLASS = "";
    public static String ASM_SETS_TARGET_CLASS = "";
    public static JsonObjIsSets JSON_OBJ_IS_SETS = null;
    
    public static void main(String[] args) {
        if(args == null || args.length < 2) {
            ASM_SETS_FILE_NAME = "/Users/victor/Documents/files/netbeans_workspace/GenAsm/cfg/is_sets.json";
            ASM_TARGET_SET = "THUMB";
            ASM_SETS_LOADER_CLASS = "net.middlemind.GenAsm.LoaderIsSets";
            ASM_SETS_TARGET_CLASS = "net.middlemind.GenAsm.JsonObjIsSets";
        } else {
            ASM_SETS_FILE_NAME = args[0];
            ASM_TARGET_SET = args[1];
            ASM_SETS_LOADER_CLASS = args[2];
            ASM_SETS_TARGET_CLASS = args[3];
        }
        
        if(Utils.IsStringEmpty(ASM_SETS_FILE_NAME)) {
            Logger.wrlErr("GenAsm: Main: Error: No assembly source file provided.");            
        } else if(Utils.IsStringEmpty(ASM_TARGET_SET)) {
            Logger.wrlErr("GenAsm: Main: Error: No assembly target set provided.");            
        } else if(Utils.IsStringEmpty(ASM_SETS_LOADER_CLASS)) {
            Logger.wrlErr("GenAsm: Main: Error: No assembly set loader provided.");            
        } else {
            String json = null;
            /*
            String test1 = "/Users/victor/Documents/files/netbeans_workspace/GenAsm/cfg/THUMB/TESTS/test_bit_series.json";
            
            try {
                json = FileLoader.LoadStr(test1);
            } catch (IOException e) {
                Logger.wr("Error parsing test1 json. Exiting...");
                return;
            }
            
            LoaderBitSeries ldrBs = null;
            JsonObjBitSeries jsonBs = null;
            
            try {
                if(!Utils.IsStringEmpty(json)) {
                    ldrBs = new LoaderBitSeries();
                    jsonBs = (JsonObjBitSeries)ldrBs.ParseJson(json, "net.middlemind.GenAsm.JsonObjBitSeries", "test_bit_series.json");
                }
            } catch (LoaderException e) {
                e.printStackTrace();
                return;
            }
            
            if(jsonBs != null) {
                Logger.wr("JsonBs: bit_start: " + jsonBs.bit_start);
                Logger.wr("JsonBs: bit_stop: " + jsonBs.bit_stop);
                Logger.wr("JsonBs: bit_len: " + jsonBs.bit_len);
                Logger.wr("JsonBs: name: " + jsonBs.name);
                Logger.wr("JsonBs: fileName: " + jsonBs.fileName);
            }
            */
            
            Class cTmp;
            LoaderIsSets ldrIsSets;
            
            try {
                json = FileLoader.LoadStr(ASM_SETS_FILE_NAME);
            } catch (IOException e) {
                json = null;
            }
            
            if(!Utils.IsStringEmpty(json)) {
                try {
                    cTmp = Class.forName(ASM_SETS_LOADER_CLASS);
                    ldrIsSets = (LoaderIsSets)cTmp.getDeclaredConstructor().newInstance();
                    JSON_OBJ_IS_SETS = (JsonObjIsSets)ldrIsSets.ParseJson(json, ASM_SETS_TARGET_CLASS, ASM_SETS_FILE_NAME);
                } catch (LoaderException | ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    Logger.wrl("Error could not instantiate loader class " + ASM_SETS_LOADER_CLASS);
                    e.printStackTrace();
                    return;
                }
                
                if(JSON_OBJ_IS_SETS != null) {
                    JSON_OBJ_IS_SETS.Print();
                }
            }
        }
    }
}
