# GenAsm: Intro

An extensible assembler project with an ARM Thumb-1 instruction set example implementation.
This project is meant as a learning tool to help people understand how to write an assembler. 
The assembler uses multiple scans to simplify the amount of work done in each scan. It also generates a 
number of output files so that the state of the assembler can be analyzed at different points in the assembly process.

# Example Output Showing GameBoy Advance Hello World
The assembler can build binaries the the GameBoy Advance can run. Many of the test programs are designed to run on the emulator.  

![image info](./storage/images/arm_hw.jpg)


# Directory Setup

If you're running the GenAsm assembler outside of the NetBeans project the directory structure should look like this:

<pre>
-> Assembler Install Directory  
   -> GenAsm.jar  
   -> cfg  
      -> is_sets.json  
      -> THUMB  
         -> <instruction set data files here>  
         -> OUTPUT  
            -> TEST_N_AsmChecks  
         -> TESTS  
            -> TEST_N_AsmChecks  
               -> genasm_source.txt 
</pre>

# Example Command Line Execution

The arguments have been listed on individual lines for readability.
The paths shown here are only relevant to my development environment.
You have to adjust the paths shown here or those hardcoded in the GenAsm.java class.

<pre>
java -jar GenAsm.jar   
 "./cfg/is_sets.json"   
 "THUMB_ARM7TDMI"   
 "net.middlemind.GenAsm.Loaders.LoaderIsSets"  
 "net.middlemind.GenAsm.JsonObjs.JsonObjIsSets"  
 "net.middlemind.GenAsm.Assemblers.Thumb.AssemblerThumb"  
 "C:\Users\variable\Documents\GitHub\GenAsm\cfg\THUMB\TESTS\TEST_N_AsmChecks\genasm_source.txt"  
 "net.middlemind.GenAsm.Linkers.Thumb.LinkerThumb"  
 "net.middlemind.GenAsm.PreProcessors.Thumb.PreProcessorThumb"  
 "C:\Users\variable\Documents\GitHub\GenAsm\cfg\THUMB\OUTPUT\TEST_N_AsmChecks\"  
 "false"  
 "false"  
 "C:\Users\variable\Documents\GitHub\GenAsm\cfg\" 
</pre> 
 
# Conversion of Command Line Arguments to Class Fields

<pre>
CFG_DIR_PATH = args[11];  
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
</pre>

# Example of Hardcoded Arguments

An example of using hardcoded arguments directly in the static main during development to speed up iterations.
The paths shown here are only relevant to my development environment.
You have to adjust the paths shown here or those hardcoded in the GenAsm.java class.

<pre>
String targetProgram = "TEST_N_AsmChecks"; 
CFG_DIR_PATH = "C:\\Users\\variable\\Documents\\GitHub\\GenAsm\\cfg\\"; 
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
</pre>
