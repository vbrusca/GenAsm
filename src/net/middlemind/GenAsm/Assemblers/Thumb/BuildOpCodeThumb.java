package net.middlemind.GenAsm.Assemblers.Thumb;

import net.middlemind.GenAsm.Tokeners.Token;
import net.middlemind.GenAsm.JsonObjs.JsonObjBitSeries;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsOpCode;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsOpCodeArg;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/17/2021 6:59 PM EST
 */
public class BuildOpCodeThumb {
    public String obj_name = "BuildOpCodeThumb";
    public JsonObjBitSeries bitSeries;
    public JsonObjIsOpCode opCode;
    public JsonObjIsOpCodeArg opCodeArg;
    public JsonObjIsOpCodeArg opCodeArgGroup;
    public JsonObjIsOpCodeArg opCodeArgList;    
    public Token tokenOpCode;
    public Token tokenOpCodeArg;
    public Token tokenOpCodeArgGroup;
    public Token tokenOpCodeArgList;
    public boolean isOpCode;
    public boolean isOpCodeArg;
    public boolean isOpCodeArgGroup;
    public boolean isOpCodeArgList;
    public String binRepStr;
}
