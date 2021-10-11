package net.middlemind.GenAsm.Linkers;

import net.middlemind.GenAsm.Assemblers.Assembler;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 10:18 AM EST
 */
public interface Linker {
    public void RunLinker(Assembler assembler, String assemblySourceFile, String outputDir, Object otherObj) throws Exception;    
}
