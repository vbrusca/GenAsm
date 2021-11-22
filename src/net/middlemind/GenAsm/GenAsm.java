package net.middlemind.GenAsm;

import net.middlemind.GenAsm.FileIO.FileLoader;
import net.middlemind.GenAsm.Assemblers.Assembler;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionLoader;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsSet;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsSets;
import net.middlemind.GenAsm.Loaders.LoaderIsSets;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import net.middlemind.GenAsm.Linkers.Linker;
import net.middlemind.GenAsm.PreProcessors.PreProcessor;

/**
 * The static entry point class for the Carlo Bruscani General Assembler.
 * @author Victor G. Brusca, Middlemind Games 07-19-2021 8:51 AM EST
 */
@SuppressWarnings({"null", "CallToPrintStackTrace", "UnusedAssignment", "Convert2Diamond", "ConvertToStringSwitch"})
public class GenAsm {
    /**
     * A static string representing the JSON object sets file name. This file contains all the JSON object data to load for this assembler run.
     */
    public static String ASM_SETS_FILE_NAME = "";
    
    /**
     * A static string representing the target instruction set to use for this assembler run.
     */    
    public static String ASM_TARGET_SET = "";
    
    /**
     * A static string representing the full class name of the Java class used to load the JSON sets file name.
     */    
    public static String ASM_SETS_LOADER_CLASS = "";
    
    /**
     * A static string representing the full class name of the Java class used to hold the loaded sets JSON object.
     */    
    public static String ASM_SETS_TARGET_CLASS = "";

    /**
     * A static instance of the JSON object that holds all the instruction sets loaded for this assembler run. 
     */
    public static JsonObjIsSets ASM_SETS = null;

    /**
     * A static instance of the JSON object that holds the instruction set JSON data files for this assembler run. 
     */
    public static JsonObjIsSet ASM_SET = null;    

    /**
     * A static string representing the full class name of the assembler to use for this run.
     */
    public static String ASM_ASSEMBLER_CLASS = "";
    
    /**
     * A static instance of the assembler loaded for this run.
     */    
    public static Assembler ASM_ASSEMBLER = null;

    /**
     * A static string representation of the full file name of the assembly source file.
     */    
    public static String ASM_ASSEMBLY_SOURCE_FILE = "";

    /**
     * A static string representation of the full class name of the linker to use for this run.
     */
    public static String ASM_LINKER_CLASS = "";

    /**
     * A static instance of the linker to use for this run.
     */    
    public static Linker ASM_LINKER = null;
    
    /**
     * A static string representation of the full class name of the pre-processor to use for this run.
     */    
    public static String ASM_PREPROCESSOR_CLASS = null;
    
    /**
     * A static instance of the pre-processor to use for this run.
     */    
    public static PreProcessor ASM_PREPROCESSOR = null;
    
    /**
     * A static string representation of the target output directory for this run.
     */    
    public static String ASM_ROOT_OUTPUT_DIR = "";
    
    /**
     * A static integer representing the target length or arguments needed to perform a customized run.
     */    
    public static int ARG_LEN_TARGET = 11;
    
    /**
     * A Boolean value indicating if verbose logging is on, if available.
     */
    public static boolean ASM_VERBOSE = false;
    
    /**
     * A Boolean value indicating if file output should be turned off.
     */
    public static boolean ASM_QUELL_FILE_OUTPUT = false;
    
    /**
     * 
     */
    public static String CFG_DIR_PATH = "C:\\Users\\variable\\Documents\\GitHub\\GenAsm\\cfg\\";
    
    /**
     * The static main entry point for this assembler run.
     * @param args          An array of strings used as arguments for this assembler run.
     * @throws Exception    An exception is thrown if any are encountered during the assembler run.
     */
    public static void main(String[] args) throws Exception {
        //args = new String[] { "compare", "C:\\Users\\variable\\Documents\\GitHub\\GbaAssemblyChecks\\chibi_akumas\\ARMDevTools\\gba_example4_hello_world_thumb\\program.gba", "C:\\Users\\variable\\Documents\\GitHub\\GbaAssemblyChecks\\chibi_akumas\\ARMDevTools\\gba_example4_hello_world_thumb\\output_assembly_listing_endian_lil.bin" };
        if(args != null && args.length == 3) {
            String cmd = args[0];
            if(Utils.IsStringEmpty(cmd) == false && cmd.equals("compare") == true) {
                String f1 = args[1];
                String f2 = args[2];
                byte[] b1 = FileLoader.LoadBin(f1);
                byte[] b2 = FileLoader.LoadBin(f2);
                Logger.wrl("File length 1: " + b1.length);
                Logger.wrl("File length 2: " + b2.length);
                if(b1.length == b2.length) {
                    for(int i = 0; i < b1.length; i++) {
                        String h1 = Integer.toHexString(b1[i]);
                        String h2 = Integer.toHexString(b2[i]);                                        
                        if(b1[i] != b2[i]) {
                            Logger.wrl(i + " " + (i / 2) + " h1: " + Utils.FormatHexString(h1, 2, true) + " h2: " + Utils.FormatHexString(h2, 2, true) + " DIFF");
                        } else {
                            Logger.wrl(i + " " + (i / 2) + " h1: " + Utils.FormatHexString(h1, 2, true) + " h2: " + Utils.FormatHexString(h2, 2, true));
                        }
                    }
                }
                return;
            }
            
        } else if(args == null || args.length < ARG_LEN_TARGET) {
            String targetProgram = "TEST_M_YourName";
            ASM_SETS_FILE_NAME = "./cfg/is_sets.json";
            ASM_TARGET_SET = "THUMB_ARM7TDMI";
            ASM_SETS_LOADER_CLASS = "net.middlemind.GenAsm.Loaders.LoaderIsSets";
            ASM_SETS_TARGET_CLASS = "net.middlemind.GenAsm.JsonObjs.JsonObjIsSets";
            ASM_SETS = null;
            ASM_SET = null;
            ASM_ASSEMBLER_CLASS = "net.middlemind.GenAsm.Assemblers.Thumb.AssemblerThumb";
            ASM_ASSEMBLER = null;
            ASM_ASSEMBLY_SOURCE_FILE = CFG_DIR_PATH + "THUMB\\TESTS\\" + targetProgram + "\\genasm_source.txt";
            ASM_LINKER_CLASS = "net.middlemind.GenAsm.Linkers.Thumb.LinkerThumb";
            ASM_LINKER = null;
            ASM_PREPROCESSOR_CLASS = "net.middlemind.GenAsm.PreProcessors.Thumb.PreProcessorThumb";
            ASM_PREPROCESSOR = null;  
            ASM_ROOT_OUTPUT_DIR = CFG_DIR_PATH + "THUMB\\OUTPUT\\" + targetProgram + "\\";
            ASM_VERBOSE = false;
            ASM_QUELL_FILE_OUTPUT = false;
            
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
            ASM_VERBOSE = Boolean.parseBoolean(args[9]);
            ASM_QUELL_FILE_OUTPUT = Boolean.parseBoolean(args[10]);
            
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
                            ASM_ASSEMBLER.RunAssembler(ASM_SET, ASM_ASSEMBLY_SOURCE_FILE, fileData, ASM_ROOT_OUTPUT_DIR, null, null, ASM_VERBOSE, ASM_QUELL_FILE_OUTPUT);
                            ASM_LINKER.RunLinker(ASM_ASSEMBLER, ASM_ASSEMBLY_SOURCE_FILE, ASM_ROOT_OUTPUT_DIR, null, ASM_VERBOSE, ASM_QUELL_FILE_OUTPUT);
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
