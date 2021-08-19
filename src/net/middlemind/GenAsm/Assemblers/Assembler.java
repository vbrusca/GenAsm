package net.middlemind.GenAsm.Assemblers;

import net.middlemind.GenAsm.JsonObjs.JsonObjIsSet;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 10:18 AM EST
 */
public interface Assembler {
    public void RunAssembler(JsonObjIsSet jsonIsSet, String assemblySourceFile, Object other);
}
