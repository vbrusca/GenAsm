package net.middlemind.GenAsm;

import net.middlemind.GenAsm.FileIO.FileLoader;
import net.middlemind.GenAsm.Assemblers.Assembler;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionLoader;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsSet;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsSets;
import net.middlemind.GenAsm.Loaders.LoaderIsSets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import net.middlemind.GenAsm.Linkers.Linker;
import net.middlemind.GenAsm.PreProcessors.PreProcessor;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07-19-2021 8:51 AM EST
 */
@SuppressWarnings({"null", "CallToPrintStackTrace", "UnusedAssignment", "Convert2Diamond", "ConvertToStringSwitch"})
public class GenAsm {
    public static String ASM_SETS_FILE_NAME = "";
    public static String ASM_TARGET_SET = "";
    public static String ASM_SETS_LOADER_CLASS = "";
    public static String ASM_SETS_TARGET_CLASS = "";
    public static JsonObjIsSets ASM_SETS = null;
    public static JsonObjIsSet ASM_SET = null;    
    public static String ASM_ASSEMBLER_CLASS = "";
    public static Assembler ASM_ASSEMBLER = null;
    public static String ASM_ASSEMBLY_SOURCE_FILE = "";
    public static String ASM_LINKER_CLASS = "";
    public static Linker ASM_LINKER = null;
    public static String ASM_PREPROCESSOR_CLASS = null;
    public static PreProcessor ASM_PREPROCESSOR = null;
    public static String ASM_ROOT_OUTPUT_DIR = "";
    public static int ARG_LEN_TARGET = 9;
    
    //TODO: Test ORG directive on test A, Compare GenAsm to vasm listing results for test programs A.
    //TODO: Compile test B-K in both GenAsm and vasm and store the listings in the proper cfg directory. Compare GenAsm to vasm listing results for test programs B-K.
    
    public static void main(String[] args) throws Exception {
        if(args == null || args.length < 8) {
            ASM_SETS_FILE_NAME = "./cfg/is_sets.json";
            ASM_TARGET_SET = "THUMB_ARM7TDMI";
            ASM_SETS_LOADER_CLASS = "net.middlemind.GenAsm.Loaders.LoaderIsSets";
            ASM_SETS_TARGET_CLASS = "net.middlemind.GenAsm.JsonObjs.JsonObjIsSets";
            ASM_SETS = null;
            ASM_SET = null;
            ASM_ASSEMBLER_CLASS = "net.middlemind.GenAsm.Assemblers.Thumb.AssemblerThumb";
            ASM_ASSEMBLER = null;
            ASM_ASSEMBLY_SOURCE_FILE = "C:\\Users\\variable\\Documents\\GitHub\\GenAsm\\cfg\\THUMB\\TESTS\\TEST_H_ShiftLeft\\genasm_source.txt";
            ASM_LINKER_CLASS = "net.middlemind.GenAsm.Linkers.Thumb.LinkerThumb";
            ASM_LINKER = null;
            ASM_PREPROCESSOR_CLASS = "net.middlemind.GenAsm.PreProcessors.Thumb.PreProcessorThumb";
            ASM_PREPROCESSOR = null;  
            ASM_ROOT_OUTPUT_DIR = "./cfg/THUMB/OUTPUT/";
            
        } else {
            ASM_SETS_FILE_NAME = args[0];
            ASM_TARGET_SET = args[1];
            ASM_SETS_LOADER_CLASS = args[2];
            ASM_SETS_TARGET_CLASS = args[3];
            ASM_SETS = null;
            ASM_SET = null;            
            ASM_ASSEMBLER_CLASS = args[4];
            ASM_ASSEMBLER = null;
            ASM_ASSEMBLY_SOURCE_FILE = args[5];
            ASM_LINKER_CLASS = args[6];
            ASM_LINKER = null;
            ASM_PREPROCESSOR_CLASS = args[7];
            ASM_PREPROCESSOR = null;
            ASM_ROOT_OUTPUT_DIR = args[8];
            
        }
        
        if(Utils.IsStringEmpty(ASM_SETS_FILE_NAME)) {
            Logger.wrlErr("GenAsm: Main: Error: No assembly source file provided.");
            
        } else if(Utils.IsStringEmpty(ASM_TARGET_SET)) {
            Logger.wrlErr("GenAsm: Main: Error: No assembly target set provided.");
            
        } else if(Utils.IsStringEmpty(ASM_SETS_LOADER_CLASS)) {
            Logger.wrlErr("GenAsm: Main: Error: No assembly set loader provided.");
            
        } else {
            String json;
            Class cTmp;
            LoaderIsSets ldrIsSets;
            
            try {
                json = FileLoader.LoadStr(ASM_SETS_FILE_NAME);
            } catch (IOException e) {
                Logger.wrl("GenAsm: Main: Error: Could not load is_sets file " + ASM_SETS_FILE_NAME);
                e.printStackTrace();
                return;
            }
            
            if(!Utils.IsStringEmpty(json)) {
                try {
                    cTmp = Class.forName(ASM_SETS_LOADER_CLASS);
                    ldrIsSets = (LoaderIsSets)cTmp.getDeclaredConstructor().newInstance();
                    ASM_SETS = ldrIsSets.ParseJson(json, ASM_SETS_TARGET_CLASS, ASM_SETS_FILE_NAME);

                    cTmp = Class.forName(ASM_PREPROCESSOR_CLASS);
                    ASM_PREPROCESSOR = (PreProcessor)cTmp.getDeclaredConstructor().newInstance();                    
                    
                    cTmp = Class.forName(ASM_ASSEMBLER_CLASS);
                    ASM_ASSEMBLER = (Assembler)cTmp.getDeclaredConstructor().newInstance();
                    
                    cTmp = Class.forName(ASM_LINKER_CLASS);
                    ASM_LINKER = (Linker)cTmp.getDeclaredConstructor().newInstance();                    
                    
                    for(JsonObjIsSet entry : ASM_SETS.is_sets) {
                        if(entry.set_name.equals(ASM_TARGET_SET)) {
                            Logger.wrl("GenAsm: Main: Found instruction set entry " + ASM_TARGET_SET);
                            ASM_SET = entry;
                            break;
                        }
                    }
                                        
                    if(ASM_SET != null) {
                        if(ASM_ASSEMBLER != null && ASM_PREPROCESSOR != null && ASM_LINKER != null) {
                            List<String> fileData = ASM_PREPROCESSOR.RunPreProcessor(ASM_ASSEMBLY_SOURCE_FILE, ASM_ROOT_OUTPUT_DIR, null);
                            ASM_ASSEMBLER.RunAssembler(ASM_SET, ASM_ASSEMBLY_SOURCE_FILE, fileData, ASM_ROOT_OUTPUT_DIR, null, null);
                            ASM_LINKER.RunLinker(ASM_ASSEMBLER, ASM_ASSEMBLY_SOURCE_FILE, ASM_ROOT_OUTPUT_DIR, null);
                        } else {
                            Logger.wrl("GenAsm: Main: Error: could not find properly loaded pre-processor, assembler, or linked");                            
                        }
                    } else {
                        Logger.wrl("GenAsm: Main: Error: could not find assembler set named " + ASM_TARGET_SET);
                    }
                } catch (ExceptionLoader | ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    Logger.wrl("GenAsm: Main: Error: could not instantiate loader class " + ASM_SETS_LOADER_CLASS);
                    e.printStackTrace();
                }
            }
        }
    }
}
