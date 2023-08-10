package net.middlemind.GenAsm.Assemblers;

import java.util.List;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsSet;

/**
 * An interface used to describe the basic assembler event handler class.
 * @author Victor G. Brusca, Middlemind Games 10-07-2021 11:04 AM EST
 */
public interface AssemblerEventHandler {
    /**
     * A method designed to be called before the assembler process begins.
     * @param assembler             An instance of the assembler class used to assemble the source file.
     * @param jsonIsSet             An instance of the JsonObjsIsSet class used to hold references to all the instruction set JSON data files.
     * @param assemblySourceFile    The path to the assembly source file.
     * @param assemblySource        The contents of the assembly source file as a list of strings.
     * @param outputDir             The path to the output directory where output files will be written.
     * @param otherObj              A generic Java object that can be used to customize the assembly process.
     */
    public void RunAssemblerPre(Assembler assembler, JsonObjIsSet jsonIsSet, String assemblySourceFile, List<String> assemblySource, String outputDir, Object otherObj);
    
    /**
     * A method designed to be called after the assembler process ends.
     * @param assembler             An instance of the assembler class used to assemble the source file.
     */
    public void RunAssemblerPost(Assembler assembler);    
}
