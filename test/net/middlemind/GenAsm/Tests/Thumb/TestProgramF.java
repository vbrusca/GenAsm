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
public class TestProgramF {
    /**
     * 
     */
    public String testName = "TEST_F_CompareNum";

    /**
     * 
     */
    public String assemblySourceFile = GenAsm.CFG_DIR_PATH + "THUMB\\TESTS\\TEST_F_CompareNum\\genasm_source.txt";
    
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
    public String jsonAnswersDataFile = GenAsm.CFG_DIR_PATH + "THUMB\\TESTS\\TEST_F_CompareNum\\vasm_answer.json";
    
    /**
     * 
     */    
    public TestProgramF() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        mainExeArgs[0] = GenAsm.CFG_DIR_PATH + "\\is_sets.json";
        mainExeArgs[1] = "THUMB_ARM7TDMI";
        mainExeArgs[2] = "net.middlemind.GenAsm.Loaders.LoaderIsSets";
        mainExeArgs[3] = "net.middlemind.GenAsm.JsonObjs.JsonObjIsSets";
        mainExeArgs[4] = "net.middlemind.GenAsm.Assemblers.Thumb.AssemblerThumb";
        mainExeArgs[5] = assemblySourceFile;
        mainExeArgs[6] = "net.middlemind.GenAsm.Linkers.Thumb.LinkerThumb";
        mainExeArgs[7] = "net.middlemind.GenAsm.PreProcessors.Thumb.PreProcessorThumb";
        mainExeArgs[8] = GenAsm.CFG_DIR_PATH + "THUMB\\OUTPUT\\TEST_F_CompareNum\\";
        mainExeArgs[9] = "false";
        mainExeArgs[10] = "false";           
    }
    
    @After
    public void tearDown() {
    }
    
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
