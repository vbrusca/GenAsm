package net.middlemind.GenAsm;

import net.middlemind.GenAsm.JsonObjs.JsonObjBitSeries;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsOpCode;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsOpCodeArg;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/17/2021 6:59 PM EST
 */
public class BuildOpCodeEntry {
    public JsonObjBitSeries bitSeries;
    public JsonObjIsOpCode opCode;
    public JsonObjIsOpCodeArg opCodeArg;
    public JsonObjIsOpCodeArg opCodeArgSub;
    public JsonObjIsOpCodeArg opCodeArgSubList;    
    public Token tokenOpCode;
    public Token tokenOpCodeArg;
    public Token tokenOpCodeArgSub;
    public Token tokenOpCodeArgSubList;
    public boolean isOpCode;
    public boolean isOpCodeArg;
    public boolean isOpCodeArgSub;
    public boolean isOpCodeArgSubList;    
    public String binRepStr;
}
