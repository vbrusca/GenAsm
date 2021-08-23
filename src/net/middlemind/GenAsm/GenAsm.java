package net.middlemind.GenAsm;

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
    
    public static void main(String[] args) throws Exception {
        if(args == null || args.length < 5) {
            ASM_SETS_FILE_NAME = "/Users/victor/Documents/files/netbeans_workspace/GenAsm/cfg/is_sets.json";
            ASM_TARGET_SET = "THUMB_ARM7TDMI";
            ASM_SETS_LOADER_CLASS = "net.middlemind.GenAsm.Loaders.LoaderIsSets";
            ASM_SETS_TARGET_CLASS = "net.middlemind.GenAsm.JsonObjs.JsonObjIsSets";
            ASM_SETS = null;
            ASM_SET = null;
            ASM_ASSEMBLER_CLASS = "net.middlemind.GenAsm.Assemblers.Thumb.AssemblerThumb";
            ASM_ASSEMBLER = null;
            ASM_ASSEMBLY_SOURCE_FILE = "/Users/victor/Documents/files/netbeans_workspace/GenAsm/cfg/THUMB/TESTS/test_asm_all.txt";
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
        }
        
        if(Utils.IsStringEmpty(ASM_SETS_FILE_NAME)) {
            Logger.wrlErr("GenAsm: Main: Error: No assembly source file provided.");
            
        } else if(Utils.IsStringEmpty(ASM_TARGET_SET)) {
            Logger.wrlErr("GenAsm: Main: Error: No assembly target set provided.");
            
        } else if(Utils.IsStringEmpty(ASM_SETS_LOADER_CLASS)) {
            Logger.wrlErr("GenAsm: Main: Error: No assembly set loader provided.");
            
        } else {
            /*
            //JSON LOADING TEST IS OP CODES
            String jsonTestFile = "/Users/victor/Documents/files/netbeans_workspace/GenAsm/cfg/THUMB/is_op_codes.json";
            String json = "";
            try {
                json = FileLoader.LoadStr(jsonTestFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            LoaderIsOpCodes loader = new LoaderIsOpCodes();
            JsonObjIsOpCodes entryTypes = null;
            try {            
                entryTypes = loader.ParseJson(json, "net.middlemind.GenAsm.JsonObjIsOpCodes", jsonTestFile);
            } catch (Exception e) {
                e.printStackTrace();
            }            
            
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            
            Gson gson = builder.create();            
            String jsonString = gson.toJson(entryTypes);
            Logger.wr(jsonString);
            Logger.wrl("");
            Logger.wrl("");
            entryTypes.Print();
            */
            
            /*
            //JSON LOADING TEST IS VALID LINES
            String jsonTestFile = "/Users/victor/Documents/files/netbeans_workspace/GenAsm/cfg/THUMB/is_valid_lines.json";
            String json = "";
            try {
                json = FileLoader.LoadStr(jsonTestFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            LoaderIsValidLines loader = new LoaderIsValidLines();
            JsonObjIsValidLines entryTypes = null;
            try {            
                entryTypes = loader.ParseJson(json, "net.middlemind.GenAsm.JsonObjIsValidLines", jsonTestFile);
            } catch (Exception e) {
                e.printStackTrace();
            }            
            
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            
            Gson gson = builder.create();            
            String jsonString = gson.toJson(entryTypes);
            Logger.wr(jsonString);
            Logger.wrl("");
            Logger.wrl("");
            entryTypes.Print();
            */

            /*
            //JSON LOADING TEST IS ARG TYPES
            String jsonTestFile = "/Users/victor/Documents/files/netbeans_workspace/GenAsm/cfg/THUMB/is_registers.json";
            String json = "";
            try {
                json = FileLoader.LoadStr(jsonTestFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            LoaderIsRegisters loader = new LoaderIsRegisters();
            JsonObjIsRegisters entryTypes = null;
            try {            
                entryTypes = loader.ParseJson(json, "net.middlemind.GenAsm.JsonObjIsRegisters", jsonTestFile);
            } catch (Exception e) {
                e.printStackTrace();
            }            
            
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            
            Gson gson = builder.create();            
            String jsonString = gson.toJson(entryTypes);
            Logger.wr(jsonString);
            Logger.wrl("");
            Logger.wrl("");
            entryTypes.Print();
            */
            
            /*
            //JSON LOADING TEST IS ARG TYPES
            String jsonTestFile = "/Users/victor/Documents/files/netbeans_workspace/GenAsm/cfg/THUMB/is_arg_types.json";
            String json = "";
            try {
                json = FileLoader.LoadStr(jsonTestFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            LoaderIsArgTypes loader = new LoaderIsArgTypes();
            JsonObjIsArgTypes entryTypes = null;
            try {            
                entryTypes = loader.ParseJson(json, "net.middlemind.GenAsm.JsonObjIsArgTypes", jsonTestFile);
            } catch (Exception e) {
                e.printStackTrace();
            }            
            
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            
            Gson gson = builder.create();            
            String jsonString = gson.toJson(entryTypes);
            Logger.wr(jsonString);
            Logger.wrl("");
            Logger.wrl("");
            entryTypes.Print();
            */
            
            /*
            //JSON LOADING TEST IS ENTRY TYPES
            String jsonTestFile = "/Users/victor/Documents/files/netbeans_workspace/GenAsm/cfg/THUMB/is_entry_types.json";
            String json = "";
            try {
                json = FileLoader.LoadStr(jsonTestFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            LoaderIsEntryTypes loader = new LoaderIsEntryTypes();
            JsonObjIsEntryTypes entryTypes = null;
            try {            
                entryTypes = loader.ParseJson(json, "net.middlemind.GenAsm.JsonObjIsEntryTypes", jsonTestFile);
            } catch (Exception e) {
                e.printStackTrace();
            }            
            
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            
            Gson gson = builder.create();            
            String jsonString = gson.toJson(entryTypes);
            Logger.wr(jsonString);            
            Logger.wrl("");
            Logger.wrl("");
            entryTypes.Print();
            */
            
            /*
            //SIMPLE LEXER FILE TEST
            String asmTestFile = "/Users/victor/Documents/files/netbeans_workspace/GenAsm/cfg/THUMB/TESTS/test_asm_short.txt";
            LexerSimple lex = new LexerSimple();
            ArrayList<ArtifactLine> lexedFile = null;
            try {
                lexedFile = lex.FileLexerize(FileLoader.Load(asmTestFile));
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            
            Gson gson = builder.create();            
            String jsonString = gson.toJson(lexedFile);
            Logger.wr(jsonString);
            */
            
            /*
            //SIMPLE LEXER TEST
            String source = "LSR   R2, R5, #27   ; Logical shift right the contents";
            int sourceLineNum = 0;
            LexerSimple lex = new LexerSimple();
            ArtifactLine line = lex.LineLexerize(source, sourceLineNum);
            
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            
            Gson gson = builder.create();            
            String jsonString = gson.toJson(line);
            Logger.wr(jsonString);
            */
            
            /*
            //JSON LOADING TEST BIT SERIES
            String json = null;
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
            
            //MAIN PROCESSING
            String json;
            Class cTmp;
            LoaderIsSets ldrIsSets;
            Assembler assm;
            
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
                            
                    cTmp = Class.forName(ASM_ASSEMBLER_CLASS);
                    assm = (Assembler)cTmp.getDeclaredConstructor().newInstance();
                    
                    for(JsonObjIsSet entry : ASM_SETS.is_sets) {
                        if(entry.set_name.equals(ASM_TARGET_SET)) {
                            Logger.wrl("GenAsm: Main: Found instruction set entry " + ASM_TARGET_SET);
                            ASM_SET = entry;
                            break;
                        }
                    }
                    
                    if(ASM_SET != null) {
                        assm.RunAssembler(ASM_SET, ASM_ASSEMBLY_SOURCE_FILE, null);
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
