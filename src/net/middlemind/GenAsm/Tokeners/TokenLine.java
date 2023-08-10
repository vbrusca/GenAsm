package net.middlemind.GenAsm.Tokeners;

import net.middlemind.GenAsm.Lexers.ArtifactLine;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsOpCode;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsDirective;
import java.util.List;
import net.middlemind.GenAsm.Assemblers.Thumb.BuildOpCodeThumb;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsValidLine;

/**
 * A class used to represent a token line which is a list of tokens derived from artifacts.
 * @author Victor G. Brusca, Middlemind Games 07/31/2021 5:29 PM EST
 */
public class TokenLine {
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */
    public String obj_name = "TokenLine";
    
    /**
     * A string representing the op-code of the associated tokens, if applicable.
     */    
    public String payloadOpCode;

    /**
     * A string representing the directive of the associated tokens, if applicable.
     */
    public String payloadDirective;

    /**
     * A string representing the token first line's binary encoding in big endian format.
     */
    public String payloadBinRepStrEndianBig1;

    /**
     * A string representing the token first line's binary encoding in little endian format.
     */
    public String payloadBinRepStrEndianLil1;

    /**
     * A string representing the token second line's binary encoding in big endian format.
     * Some branching op-codes require 4 bytes of encoding as opposed to 2 bytes.
     */
    public String payloadBinRepStrEndianBig2;

    /**
     * A string representing the token second line's binary encoding in little endian format.
     * Some branching op-codes require 4 bytes of encoding as opposed to 2 bytes.
     */
    public String payloadBinRepStrEndianLil2;

    /**
     * The number of bytes that are needed to represent this token line in binary format.
     */
    public int byteLength = 2;

    /**
     * A string representing the address of this token line in hex format.
     * This address only takes into account lines that end up in the final binary representation of the assembly source file.
     */
    public String addressHex;

    /**
     * A string representing the address of this token line in binary format.
     * This address only takes into account lines that end up in the final binary representation of the assembly source file.
     */
    public String addressBin;

    /**
     * An integer representation of this token line's address.
     */
    public int addressInt;

    /**
     * The active line number of this token line.
     * Active line numbers only take into account lines that end up in the final binary representation of the assembly source file.
     */
    public int lineNumActive;

    /**
     * The absolute line number of this token line.
     * Absolute line numbers take into account all lines in the assembly source file.
     */
    public int lineNumAbs;

    /**
     * An integer representing the number of tokens in the payload.
     */
    public int payloadLen;

    /**
     * An integer representing the number of arguments in the payload.
     */
    public int payloadLenArg;

    /**
     * A Boolean value indicating if the token line is considered empty.
     * An empty line will not have an assembly source that ends up in the final binary representation of the assembly source file.
     */
    public boolean isLineEmpty;

    /**
     * A Boolean value indicating if this token line is an op-code line.
     */    
    public boolean isLineOpCode;

    /**
     * A Boolean value indicating if this token line is a directive line.
     */
    public boolean isLineDirective;

    /**
     * A Boolean value indicating if this token line is a label definition token line.
     */
    public boolean isLineLabelDef;

    /**
     * An object instance that represents the artifact line this token line is based on.
     */
    public ArtifactLine source;

    /**
     * An object instance that represents the instruction set valid line entry associated with this token line.
     */
    public JsonObjIsValidLine validLineEntry;

    /**
     * A list of instruction set op-code entries that match the current token line's op-code, if applicable.
     */
    public List<JsonObjIsOpCode> matchesOpCode;

    /**
     * A list of directive entries that match the current token line's directive, if applicable.
     */
    public List<JsonObjIsDirective> matchesDirective;

    /**
     * A list of build entries used to represent the current assembly line code as a binary string.
     */
    public List<BuildOpCodeThumb> buildEntries;

    /**
     * A list of tokens associated with this token line.
     */
    public List<Token> payload;
}