package net.middlemind.GenAsm.Assemblers;

import java.util.List;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsSet;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 10-07-2021 11:04 AM EST
 */
public interface AssemblerEventHandler {
    public void RunAssemblerPre(Assembler assembler, JsonObjIsSet jsonIsSet, String assemblySourceFile, List<String> assemblySource, String outputDir, Object otherObj);
    public void RunAssemblerPost(Assembler assembler);    
}
