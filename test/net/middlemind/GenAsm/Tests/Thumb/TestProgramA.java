package net.middlemind.GenAsm.Tests.Thumb;

import net.middlemind.GenAsm.FileIO.FileLoader;
import net.middlemind.GenAsm.GenAsm;
import net.middlemind.GenAsm.JsonObjs.JsonObj;
import net.middlemind.GenAsm.JsonObjs.JsonObjLineHexReps;
import net.middlemind.GenAsm.Linkers.Thumb.LinkerThumb;
import net.middlemind.GenAsm.Loaders.Loader;
import net.middlemind.GenAsm.Logger;
import net.middlemind.GenAsm.Utils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 10-07-2021 12:25 PM EST
 */
public class TestProgramA {
    /**
     * 
     */
    public String testName = "TEST_A_AllOpCodes";
    
    /**
     * 
     */
    public String cfgDir = TestProgramSuite.CFG_DIR; //"C:\\FILES\\DOCUMENTS\\GitHub\\GenAsm\\cfg\\";
    
    /**
     * 
     */
    public String assemblySourceFile = cfgDir + "THUMB\\TESTS\\" + testName + "\\genasm_source.txt";
    
    /**
     * 
     */
    public String[] mainExeArgs = new String[GenAsm.ARG_LEN_TARGET];
    
    /**
     * 
     */
    public String answersLoaderClass = "net.middlemind.GenAsm.Loaders.LoaderLineHexReps";
    
    /**
     * 
     */
    public String answersTargetClass = "net.middlemind.GenAsm.JsonObjs.JsonObjLineHexReps";    
    
    /**
     * 
     */
    public String jsonAnswersDataFile = cfgDir + "THUMB\\TESTS\\" + testName + "\\vasm_answer.json";
    
    /**
     * 
     */
    public TestProgramA() {
    }
    
    /**
     * 
     */
    @BeforeClass
    public static void setUpClass() {
    }
    
    /**
     * 
     */
    @AfterClass
    public static void tearDownClass() {
    }
    
    /**
     * 
     */
    @Before
    public void setUp() {
        mainExeArgs[0] = cfgDir + "is_sets.json";
        mainExeArgs[1] = "THUMB_ARM7TDMI";
        mainExeArgs[2] = "net.middlemind.GenAsm.Loaders.LoaderIsSets";
        mainExeArgs[3] = "net.middlemind.GenAsm.JsonObjs.JsonObjIsSets";
        mainExeArgs[4] = "net.middlemind.GenAsm.Assemblers.Thumb.AssemblerThumb";
        mainExeArgs[5] = assemblySourceFile;
        mainExeArgs[6] = "net.middlemind.GenAsm.Linkers.Thumb.LinkerThumb";
        mainExeArgs[7] = "net.middlemind.GenAsm.PreProcessors.Thumb.PreProcessorThumb";
        mainExeArgs[8] = cfgDir + "THUMB\\OUTPUT\\" + testName + "\\";
        mainExeArgs[9] = "false";
        mainExeArgs[10] = "false";
        mainExeArgs[11] = cfgDir;
        
        /*
         String targetProgram = "TEST_A_AllOpCodes";
         11 CFG_DIR_PATH = ".\\cfg\\";
         0 ASM_SETS_FILE_NAME = "./cfg/is_sets.json";
         1 ASM_TARGET_SET = "THUMB_ARM7TDMI";
         2 ASM_SETS_LOADER_CLASS = "net.middlemind.GenAsm.Loaders.LoaderIsSets";
         3 ASM_SETS_TARGET_CLASS = "net.middlemind.GenAsm.JsonObjs.JsonObjIsSets";
         ASM_SETS = null;
         ASM_SET = null;
         4 ASM_ASSEMBLER_CLASS = "net.middlemind.GenAsm.Assemblers.Thumb.AssemblerThumb";
         ASM_ASSEMBLER = null;
         5 ASM_ASSEMBLY_SOURCE_FILE = CFG_DIR_PATH + "THUMB\\TESTS\\" + targetProgram + "\\genasm_source.txt";
         6 ASM_LINKER_CLASS = "net.middlemind.GenAsm.Linkers.Thumb.LinkerThumb";
         ASM_LINKER = null;
         7 ASM_PREPROCESSOR_CLASS = "net.middlemind.GenAsm.PreProcessors.Thumb.PreProcessorThumb";
         ASM_PREPROCESSOR = null;  
         8 ASM_ROOT_OUTPUT_DIR = CFG_DIR_PATH + "THUMB\\OUTPUT\\" + targetProgram + "\\";
         9 ASM_VERBOSE = false;
         10 ASM_QUELL_FILE_OUTPUT = false;        
        */        
    }
    
    /**
     * 
     */
    @After
    public void tearDown() {
    }
    
    /**
     * 
     */
    @Test
    @SuppressWarnings({"UnusedAssignment", "CallToPrintStackTrace"})
    public void test1() {
        boolean res = true;
        try {
            Logger.LOGGING_ON = false;
            GenAsm.main(mainExeArgs);
            LinkerThumb linkerThumb = (LinkerThumb)GenAsm.ASM_LINKER;
            
            Class cTmp = Class.forName(answersLoaderClass);
            Loader ldr = (Loader)cTmp.getDeclaredConstructor().newInstance();
            String json = null;
            String jsonName = null;
            JsonObj jsonObj = null;

            json = FileLoader.LoadStr(jsonAnswersDataFile);
            jsonObj = ldr.ParseJson(json, answersTargetClass, jsonAnswersDataFile);
            jsonName = jsonObj.GetName();
            JsonObjLineHexReps hexDataLines = (JsonObjLineHexReps)jsonObj;
            Logger.LOGGING_ON = true;
            Logger.wrl(testName + ": Found " + hexDataLines.line_hex_reps.size() + " test program answers in the loaded JSON data file, '" + jsonName + "'.");
            Logger.wrl(testName + ": Found " + linkerThumb.hexMapLe.size() + " test program lines entries in the resulting linked data, '" + assemblySourceFile + "'.");
            res = Utils.CheckAssemblerTestProgramAgainstAnswers(hexDataLines, linkerThumb.hexMapLe);
            Logger.wrl("CheckResults: " + res);            
        } catch (Exception e) {
            res = false;
            e.printStackTrace();
        }
        Assert.assertTrue(res);
    }     
}
