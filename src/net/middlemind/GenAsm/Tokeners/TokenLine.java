package net.middlemind.GenAsm.Tokeners;

import net.middlemind.GenAsm.Lexers.ArtifactLine;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsOpCode;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsDirective;
import java.util.List;
import net.middlemind.GenAsm.Assemblers.Thumb.BuildOpCodeThumb;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsValidLine;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/31/2021 5:29 PM EST
 */
public class TokenLine {
    public String obj_name = "TokenLine";
    public String payloadOpCode;
    public String payloadDirective;
    public String payloadBinRepStrEndianBig1;
    public String payloadBinRepStrEndianLil1;
    public String payloadBinRepStrEndianBig2;
    public String payloadBinRepStrEndianLil2;
    public int bitLength = 2;
    public String addressHex;
    public String addressBin;
    public int addressInt;
    public int lineNumActive;
    public int lineNumAbs;
    public int payloadLen;
    public int payloadLenArg;
    public boolean isLineEmpty;
    public boolean isLineOpCode;
    public boolean isLineDirective;
    public boolean isLineLabelDef;
    public ArtifactLine source;
    public JsonObjIsValidLine validLineEntry;
    public List<JsonObjIsOpCode> matchesOpCode;
    public List<JsonObjIsDirective> matchesDirective;
    public List<BuildOpCodeThumb> buildEntries;
    public List<Token> payload;
}
