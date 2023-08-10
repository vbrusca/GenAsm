package net.middlemind.GenAsm.Assemblers.Thumb;

import net.middlemind.GenAsm.Tokeners.Token;
import net.middlemind.GenAsm.JsonObjs.JsonObjBitSeries;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsOpCode;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsOpCodeArg;

/**
 * A class that holds a complete binary representation of a line of assembly code, ie OpCode entry.
 * @author Victor G. Brusca, Middlemind Games 08/17/2021 6:59 PM EST
 */
public class BuildOpCodeThumb {
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */
    public String obj_name = "BuildOpCodeThumb";
    
    /**
     * The bitSeries associated with this entry, if applicable.
     */
    public JsonObjBitSeries bitSeries;
    
    /**
     * The OpCode associated with this entry, if applicable.
     */    
    public JsonObjIsOpCode opCode;

    /**
     * The OpCodeArg associated with this entry, if applicable.
     */
    public JsonObjIsOpCodeArg opCodeArg;

    /**
     * An OpCodeArg for a group object associated with this entry, if applicable.
     */
    public JsonObjIsOpCodeArg opCodeArgGroup;
    
    /**
     * An OpCodeArg for a list object associated with this entry, if applicable.
     */    
    public JsonObjIsOpCodeArg opCodeArgList;    

    /**
     * An OpCode Token associated with this entry, if applicable.
     */
    public Token tokenOpCode;

    /**
     * An OpCodeArg Token associated with this entry, if applicable.
     */    
    public Token tokenOpCodeArg;
    
    /**
     * An OpCodeArg for a group object associated with this entry, if applicable.
     */    
    public Token tokenOpCodeArgGroup;
    
    /**
     * An OpCodeArg for a list object associated with this entry, if applicable.
     */    
    public Token tokenOpCodeArgList;

    /**
     * A Boolean value indicating if this is an OpCode entry if so it should have the tokenOpCode and opCode fields populated.
     */
    public boolean isOpCode;

    /**
     * A Boolean value indicating if this is an OpCodeArg entry if so it should have the tokenOpCodeArg and opCodeArg fields populated.
     */    
    public boolean isOpCodeArg;

    /**
     * A Boolean value indicating if this is an OpCodeArgGroup entry if so it should have the tokenOpCodeArgGroup and opCodeArgGroup fields populated.
     */
    public boolean isOpCodeArgGroup;

    /**
     * A Boolean value indicating if this is an OpCodeArgList entry if so it should have the tokenOpCodeArgList and opCodeArgList fields populated.
     */
    public boolean isOpCodeArgList;

    /**
     * A string that contains the binary representation of this entry.
     */
    public String binRepStr1;

    /**
     * A string that contains the second line binary representation of this entry. This only occurs for some branch OpCodes.
     */
    public String binRepStr2;
}
