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
    public int lineNum;
    public String lineNumMemCode = "-1";
    public int payloadLen;
    public String payloadOpCode;
    public String payloadDirective;
    public int payloadLenArg;
    public ArtifactLine source;
    public JsonObjIsValidLine validLineEntry;
    public List<Token> payload;
    public List<JsonObjIsOpCode> matchesOpCode;
    public List<JsonObjIsDirective> matchesDirective;
    public boolean isLineEmpty;
    public boolean isLineOpCode;
    public boolean isLineDirective;
}
