package net.middlemind.GenAsm;

import java.util.List;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/31/2021 5:29 PM EST
 */
public class TokenLine {
    public String obj_name = "TokenLine";
    public int lineNum;
    public int payloadLen;
    public String payloadOpCode;
    public int payloadLenArg;
    public ArtifactLine source;
    public JsonObj validLineEntry;
    public List<Token> payload;
    public List<JsonObjIsOpCode> opCodeMatches;
}
