package net.middlemind.GenAsm;

import net.middlemind.GenAsm.JsonObjs.JsonObjIsOpCode;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsDirective;
import java.util.List;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsValidLine;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/31/2021 5:29 PM EST
 */
public class TokenLine {
    public String obj_name = "TokenLine";
    public String payloadOpCode;
    public String payloadDirective;
    public String payloadBinRepStr;    
    public String lineNumHex;
    public String lineNumBin;
    public int lineNum;
    public int payloadLen;
    public int payloadLenArg;
    public boolean isLineEmpty;
    public boolean isLineOpCode;
    public boolean isLineDirective;    
    public ArtifactLine source;
    public JsonObjIsValidLine validLineEntry;
    public List<JsonObjIsOpCode> matchesOpCode;
    public List<JsonObjIsDirective> matchesDirective;
    public List<BuildOpCodeEntry> buildEntries;
    public List<Token> payload;
}
