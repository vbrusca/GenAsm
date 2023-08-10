package net.middlemind.GenAsm.Assemblers;

import java.util.List;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsSet;

/**
 * An interface used to describe the basic implementation of an assembler.
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 10:18 AM EST
 */
public interface Assembler {
    /**
     * A method used to run the current assembler implementation.
     * @param jsonIsSet             A JsonObjIsSet instance that represents all of the JSON data files to load and their associated loading class.
     * @param assemblySourceFile    A string representation of the target source file to assemble.
     * @param assemblySource        A List containing the contents of the provided assemblySourceFile.
     * @param outputDir             A string representing the desired output directory used by the assembler.
     * @param otherObj              An optional generic object used to provide customized arguments to the assembler.
     * @param asmEventHandler       An assembler event handler that if defined is used to respond to events during the assembly process.
     * @param verbose               A Boolean value indicating that verbose logging should be used.
     * @param quellOutput           A Boolean value indicating that file output should be disabled.
     * @throws Exception            An exception throw during the assembly process.
     */
    public void RunAssembler(JsonObjIsSet jsonIsSet, String assemblySourceFile, List<String> assemblySource, String outputDir, Object otherObj, AssemblerEventHandler asmEventHandler, boolean verbose, boolean quellOutput) throws Exception;
}
