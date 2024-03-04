package net.middlemind.GenAsm.Tests.Thumb;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Victor G. Brusca, Middlemind Games
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(
   {
      TestProgramA.class,
      TestProgramB.class,
      TestProgramC.class,
      TestProgramD.class,
      TestProgramE.class,
      TestProgramF.class,
      TestProgramG.class,
      TestProgramH.class,
      TestProgramI.class,
      TestProgramJ.class,
      TestProgramK.class      
      
      /*
      TestProgramL.class
      */
   }
)

public class TestProgramSuite {

   /**
    *
    */
   public static String CFG_DIR = "C:\\FILES\\DOCUMENTS\\GitHub\\GenAsm\\cfg\\";

   /**
    *
    */
   public static double DELTA_D = 0.00001;

   /**
    *
    * @throws Exception
    */
   @BeforeClass
   public static void setUpClass() throws Exception {

   }

   /**
    *
    * @throws Exception
    */
   @AfterClass
   public static void tearDownClass() throws Exception {
   }

   /**
    *
    * @throws Exception
    */
   @Before
   public void setUp() throws Exception {
   }

   /**
    *
    * @throws Exception
    */
   @After
   public void tearDown() throws Exception {
   }
}
