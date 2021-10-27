package net.middlemind.GenAsm.Assemblers;

import java.util.List;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsSet;

/**
 * An interface used to describe the basic assembler event handler class.
 * @author Victor G. Brusca, Middlemind Games 10-07-2021 11:04 AM EST
 */
public interface AssemblerEventHandler {
    /**
     * 
     * @param assembler
     * @param jsonIsSet
     * @param assemblySourceFile
     * @param assemblySource
     * @param outputDir
     * @param otherObj 
     */
    public void RunAssemblerPre(Assembler assembler, JsonObjIsSet jsonIsSet, String assemblySourceFile, List<String> assemblySource, String outputDir, Object otherObj);
    
    /**
     * 
     * @param assembler 
     */
    public void RunAssemblerPost(Assembler assembler);    
}
