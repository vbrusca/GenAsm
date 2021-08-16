package net.middlemind.GenAsm;

import java.util.List;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsEntryType;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsRegister;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/31/2021 5:23 PM EST
 */
public class Token {
    public String obj_name = "Token";
    public String type_name;
    public String source;
    public String parentLabel;
    public Object value;
    public int lineNum;
    public int index;
    public int payloadLen;
    public int payloadArgLen;
    public boolean isArgOpCode;
    public boolean isArgDirective;
    public boolean isOpCode;
    public boolean isDirective;
    public boolean isComment;
    public boolean isLabel;
    public boolean isLabelLocal;
    public boolean isLabelLocalRef;    
    public Artifact artifact;
    public JsonObjIsEntryType type;    
    public TokenLine parentLine;
    public JsonObjIsRegister register;    
    public List<Token> payload;    
}
