package net.middlemind.GenAsm.Assemblers.Thumb;

import net.middlemind.GenAsm.Tokeners.Token;
import net.middlemind.GenAsm.JsonObjs.JsonObjBitSeries;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsOpCode;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsOpCodeArg;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/17/2021 6:59 PM EST
 */
public class BuildOpCodeEntryThumb {
    public String obj_name = "BuildOpCodeEntryThumb";
    public JsonObjBitSeries bitSeries;
    public JsonObjIsOpCode opCode;
    public JsonObjIsOpCodeArg opCodeArg;
    public JsonObjIsOpCodeArg opCodeArgSubGroup;
    public JsonObjIsOpCodeArg opCodeArgSubList;    
    public Token tokenOpCode;
    public Token tokenOpCodeArg;
    public Token tokenOpCodeArgSubGroup;
    public Token tokenOpCodeArgSubList;
    public boolean isOpCode;
    public boolean isOpCodeArg;
    public boolean isOpCodeArgSubGroup;
    public boolean isOpCodeArgSubList;    
    public String binRepStr;
}
