package net.middlemind.GenAsm.Tests.Thumb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.middlemind.GenAsm.GenAsm;
import net.middlemind.GenAsm.Linkers.Thumb.LinkerThumb;
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
    public static String TEST_ASEEMBLY_SOURCE_FILE = GenAsm.CFG_DIR_PATH + "";
    
    /**
     * 
     */
    public static String TEST_OUTPUT_DIR = GenAsm.CFG_DIR_PATH + "";

    /**
     * 
     */
    public String[] mainExeArgs = new String[GenAsm.ARG_LEN_TARGET];
    
    public TestProgramA() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        mainExeArgs[0] = "./cfg/is_sets.json";
        mainExeArgs[1] = "THUMB_ARM7TDMI";
        mainExeArgs[2] = "net.middlemind.GenAsm.Loaders.LoaderIsSets";
        mainExeArgs[3] = "net.middlemind.GenAsm.JsonObjs.JsonObjIsSets";
        mainExeArgs[4] = "net.middlemind.GenAsm.Assemblers.Thumb.AssemblerThumb";
        mainExeArgs[5] = GenAsm.CFG_DIR_PATH + "THUMB\\TESTS\\TEST_A_AllOpCodes\\genasm_source.txt";
        mainExeArgs[6] = "net.middlemind.GenAsm.Linkers.Thumb.LinkerThumb";
        mainExeArgs[7] = "net.middlemind.GenAsm.PreProcessors.Thumb.PreProcessorThumb";
        mainExeArgs[8] = GenAsm.CFG_DIR_PATH + "THUMB\\OUTPUT\\TEST_A_AllOpCodes\\";
        mainExeArgs[9] = "false";
        mainExeArgs[10] = "false";
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    @SuppressWarnings({"UnusedAssignment", "CallToPrintStackTrace"})
    public void test1() {
        try {
            GenAsm.main(mainExeArgs);
            LinkerThumb linkerThumb = (LinkerThumb)GenAsm.ASM_LINKER;
            //linkerThumb.hexMapLe;
            //Assert.assertArrayEquals(new int[] {6}, new int[] {6});
            //load json data file with answers
                        
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
        MmgBmp b1, b2 = null;
        Mmg9Slice n1, n2, n3 = null;

        b1 = MmgHelper.GetBasicBmp(MmgUnitTestSettings.RESOURCE_ROOT_DIR + "drawable/auto_load/popup_window_base.png");
        b2 = MmgHelper.GetBasicBmp(MmgUnitTestSettings.RESOURCE_ROOT_DIR + "drawable/auto_load/game_title.png");

        n1 = new Mmg9Slice(10, b1, 200, 200);
        n3 = new Mmg9Slice(12, b2, 300, 300);

        Assert.assertEquals(n1.GetOffset(), 10);
        Assert.assertEquals(true, n1.GetSrc().ApiEquals(b1));
        Assert.assertEquals(true, n1.GetSrc().equals(b1));
        Assert.assertEquals(n1.GetWidth(), 200);
        Assert.assertEquals(n1.GetHeight(), 200);
        Assert.assertEquals(true, n1.GetIsVisible());
        Assert.assertEquals(true, n1.GetPosition().ApiEquals(MmgVector2.GetOriginVec()));

        n1.SetDest(b2);
        n1.SetOffset(12);
        n1.SetSrc(b2);

        Assert.assertEquals(true, n1.GetDest().ApiEquals(b2));
        Assert.assertEquals(true, n1.GetDest().equals(b2));
        Assert.assertEquals(true, n1.GetSrc().ApiEquals(b2));
        Assert.assertEquals(true, n1.GetSrc().equals(b2));
        Assert.assertEquals(true, n1.GetDest().ApiEquals(b2));
        Assert.assertEquals(true, n1.GetOffset() == 12);

        n2 = n1.CloneTyped();

        Assert.assertEquals(true, n1.ApiEquals(n1));
        Assert.assertEquals(true, n1.ApiEquals(n2));
        Assert.assertEquals(true, n2.ApiEquals(n1));
        Assert.assertEquals(true, n2.ApiEquals(n1));
        Assert.assertEquals(false, n3.ApiEquals(n1));
        Assert.assertEquals(false, n1.ApiEquals(n3));
        */
    }    
}
