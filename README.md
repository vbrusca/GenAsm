# GenAsm: Intro

An extensible assembler project with an ARM Thumb-1 instruction set example implementation.
This project is meant as a learning tool to help people understand how to write an assembler. 
The assembler uses multiple scans to simplify the amount of work done in each scan. It also generates a 
number of output files so that the state of the assembler can be analyzed at different points in the assembly process.
I wrote this project for my brother, Carlo.

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
         -> /* instruction set data files here */  
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

# Description of Output Files


## In Process: Output Generated

<pre>
output_pre_processed_assembly.txt: Output after preprocessor runs.
output_lexed.json: Output after the lexer runs.
output_tokened_phase0_tokenized.json: Output after phase0 runs, tokenizer results.
output_tokened_phase1_valid_lines.json: Output after phase1 runs, opcode valid lines.
output_tokened_phase2_refactored.json: Output after phase2 runs, refactoring groups, lists, comments.
output_tokened_phase3_valid_lines.json: Output after phase3 runs, directive valid lines.
output_tokened_phase4_bin_output.json: Output after phase4, ready for generating binary output.
</pre>


## Building Binary Files: Output Generated

<pre>
output_symbols.json: A listing of the symbols found during the assembly process.
output_area_desc_code.json: A listing of area descriptions.
output_area_lines_code.json: A listing of area associated lines.
output_linked_area_lines_code.json: A listing of linked area lines of source code.
output_assembly_listing_endian_big.list: Big endian listing.
output_assembly_listing_endian_lil.list: Little endian listing.
output_assembly_listing_endian_big.bin: Big endian binary output.
output_assembly_listing_endian_lil.bin: Little endian binary output.
</pre>

# Supported Preprocessor Directives

<pre>
Include Binary File: $INCBIN, $FLPINCBIN 
Include Assembly File: $INCASM 
Non-Operation: $NOP 
Include String Data: $STRING, $FLIPSTRING 
</pre>

# Supported Directives

<pre>
@DCHW: A data directive used to write two bytes of data at the current memory location. 
@FLPDCHW: A data directive used to write two bytes of data, with flipped endianness, at the current memory location. 
@DCWBF: A data directive used to write the first byte of a two-byte value as a halfword, at the current memory location. 
@DCWBS: A data directive used to write the second byte of a two-byte value as a halfword, at the current memory location. 
@DCB: A data directive used to write one byte of data as a halfword at the current memory location. 
@EQU: A label directive used to set a label to a specific value, creating a label value entry that can be referenced by subsequent assembly instructions. 
@ORG: A memory directive used to set the starting memory address to be used in all listing and memory address calculations. 
@TTL: An area definition directive used to define a program’s title. 
@SUBT: A program definition directive used to define a program’s subtitle. 
@ENTRY: An area definition directive used to define the start of an area. 
@END: An area definition directive used to define the end of an area. 
@AREA: An area definition directive used to indicate the start of an area definition. 
@READONLY: An area definition directive used to indicate if the area is read-only. Not currently in use. 
@READWRITE: An area definition directive used to indicate if the area is read-write. Not currently in use.  
@CODE: An area definition directive used to indicate if the area is a code area and not a data area. 
@DATA: An area definition directive used to indicate if the area is a data area and not a code area.
</pre>

# Instruction Set File Registration

You can define an instruction set by adding entries to the is_sets.json data file. An example of the file is as follows.

<pre>
File: is_sets.json
Description: Holds the definition of an instruction set through the description of associated data files.
Example:
{
    "obj_name": "is_sets",
    "is_sets": [
        {
            "obj_name": "is_set",
            "set_name": "THUMB_ARM7TDMI",
            "is_files": [
                {
                    "obj_name": "is_file",
                    "path": "./cfg/THUMB/is_arg_types.json",
                    "loader_class": "net.middlemind.GenAsm.Loaders.Thumb.LoaderIsArgTypes",
                    "target_class": "net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsArgTypes",
                    "category": "arguments"
                }
               ...
</pre>

# Thumb Instruction Set Data Files

A description of the data files used to define, drive the ARM Thumb-1 instruction set implementation. You can define your own files, 
preprocessor, lexer, tokener, assmebler, and linker to implement a new instruction set or you can build off of what's available in the project.

<pre>
File: is_arg_types.json
Description: Holds a definition of all the Thumb-1 instruction set's argument types.
Example:
{
    "obj_name": "is_arg_types",  
    "set_name": "THUMB_ARM7TDMI",
    "is_arg_types": [
        {
            "obj_name": "is_arg_type",
            "arg_name": "Group",
            "is_entry_types": ["GroupStart", "GroupStop"]
        }
        ...
</pre>

<pre>
File: is_directives.json
Description: Holds a definition of all the Thumb-1 instruction set's supported directives.
Example:
{
    "obj_name": "is_directives",
    "set_name": "THUMB_ARM7TDMI",
    "is_directives": [
        {
            "obj_name": "is_directive",
            "directive_name": "@DCB",
            "arg_len": 1,
            "args": [
                {
                    "obj_name": "is_directive_arg",
                    "arg_index": 0,
                    "bit_index": 0,
                    "is_entry_types": ["Number", "LabelRef"],
                    "is_arg_type": "Number",
                    "bit_series": {
                        "bit_start": 0,
                        "bit_stop": 15,
                        "bit_len": 16
                    },
                    "num_range": {
                        "min_value": 0,
                        "max_value": 255,
                        "bit_len": 8,
                        "twos_compliment": false,
                        "alignment": "HALFWORD"
                    },
                    "bit_shift": {
                        "shift_dir": "NONE",
                        "shift_amount": -1
                    }
                }                
            ],
            "description": "The DCB directive is used to write data, one byte written as a halfword, directly into the data AREA of an assembly file."
        }
        ...
</pre>

<pre>
File: is_empty_data_lines.json
Description: Holds a JSON definition of an empty line of assembly used when process a 2-line branch instruction.
Example:
See the associated file.
</pre>

<pre>
File: is_entry_types.json
Description: Holds a definition of all the Thumb-1 instruction set's entry types.
Example:
{
    "obj_name": "is_entry_types",  
    "set_name": "THUMB_ARM7TDMI",
    "is_entry_types": [
        {
            "obj_name": "is_entry_type",
            "type_name": "Comment",
            "type_category": "Comment",
            "category": "Comment",
            "category_class": "java.lang.String",
            "txt_match": {
                "starts_with": [";"],
                "contains": ["*"],
                "must_contain": [],
                "must_not_contain": [],
                "ends_with": ["*"]
            }
        }
        ...
</pre>

<pre>
File: is_op_codes.json
Description: Holds a definition of all the Thumb-1 instruction set's opcodes.
Example:
{
    "obj_name": "is_op_codes",
    "set_name": "THUMB_ARM7TDMI",
    "endian": "BIG",
    "pc_prefetch_bits": 32,
    "pc_prefetch_bytes": 4,
    "pc_prefetch_halfwords": 2,    
    "pc_prefetch_words": 1,
    "pc_alignment": "WORD",
    "pc_lsb_zeroed": true,
    "nop_assembly": "MOV R8, R8 ;nop instruction",
    "nop_binary": "0100011011000000",
    "nop_hex": "46C0",
    "bit_series": {
        "bit_start": 0,
        "bit_stop": 15,
        "bit_len": 16
    },
    "is_op_codes": [
        {
            "obj_name": "is_op_code",
            "op_code_name": "LSL",
            "index": 0,
            "arg_len": 3,
            "arg_separator": ",",
            "bit_rep": {
                "bit_string": "00000",
                "bit_int": 0,
                "bit_len": 5
            },
            "bit_series": {
                "bit_start": 11,
                "bit_stop": 15,
                "bit_len": 5
            },
            "args": [
                {
                    "obj_name": "is_op_code_arg",
                    "arg_index": 0,
                    "bit_index": 0,
                    "is_entry_types": ["RegisterLow"],
                    "is_arg_type": "Rd",
                    "bit_series": {
                        "bit_start": 0,
                        "bit_stop": 2,
                        "bit_len": 3
                    }
                },
                {
                    "obj_name": "is_op_code_arg",            
                    "arg_index": 1,
                    "bit_index": 1,
                    "is_entry_types": ["RegisterLow"],
                    "is_arg_type": "Rs",
                    "bit_series": {
                        "bit_start": 3,
                        "bit_stop": 5,
                        "bit_len": 3
                    }
                },
                {
                    "obj_name": "is_op_code_arg",  
                    "arg_index": 2,
                    "bit_index": 2,
                    "is_entry_types": ["Number", "LabelRef"],                    
                    "is_arg_type": "Offset5",
                    "bit_series": {
                        "bit_start": 6,
                        "bit_stop": 10,
                        "bit_len": 5
                    },
                    "num_range": {
                        "min_value": 0,
                        "max_value": 31,
                        "bit_len": 5,
                        "twos_compliment": false,
                        "alignment": "NATURAL"
                    },
                    "bit_shift": {
                        "shift_dir": "NONE",
                        "shift_amount": 0
                    }
                }
            ]
        }
        ...
</pre>

<pre>
File: is_registers.json
Description: Holds a definition of all the Thumb-1 instruction set's registers.
Example:
{
    "obj_name": "is_registers",
    "set_name": "THUMB_ARM7TDMI",
    "is_registers": [
        {
            "register_name": "R0",
            "obj_name": "is_register",
            "is_entry_type": "RegisterLow",
            "bit_rep": {
                "bit_string": "000",
                "bit_int": 0,
                "bit_len": 3
            },
            "type": "LOW",
            "desc": "general purpose"
        }
        ...
</pre>

<pre>
File: is_valid_lines.json
Description: Holds a definition of all the Thumb-1 instruction set's valid lines.
Example:
{
    "obj_name": "is_valid_lines",
    "set_name": "THUMB_ARM7TDMI",
    "min_line_entries": 1,
    "max_line_entries": 4,
    "is_valid_lines": [
        {
            "obj_name": "is_valid_line",
            "index": 0,
            "empty_line": true,
            "is_valid_line": [
                {
                    "obj_name": "is_valid_line_entry",
                    "is_entry_types": ["Comment"],
                    "index": 0
                }
            ]
        }
        ...
</pre>













