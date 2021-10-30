package net.middlemind.GenAsm.Tokeners;

import net.middlemind.GenAsm.Lexers.Artifact;
import java.util.List;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsEntryType;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsRegister;

/**
 * A class used to represent a token or assembly source code converted to an artifact by a lexer which is then tokenized by a tokener.
 * @author Victor G. Brusca, Middlemind Games 07/31/2021 5:23 PM EST
 */
public class Token {
    /**
     * 
     */
    public String obj_name = "Token";
    
    /**
     * 
     */    
    public String type_name;

    /**
     * 
     */
    public String source;

    /**
     * 
     */
    public String parentLabel;

    /**
     * 
     */
    public Object value;

    /**
     * 
     */
    public int lineNumAbs;

    /**
     * 
     */
    public int index;
    
    /**
     * 
     */    
    public int payloadLen;

    /**
     * 
     */
    public int payloadArgLen;

    /**
     * 
     */
    public boolean isOpCodeArg;

    /**
     * 
     */
    public boolean isDirectiveArg;

    /**
     * 
     */
    public boolean isOpCode;

    /**
     * 
     */
    public boolean isDirective;

    /**
     * 
     */
    public boolean isComment;

    /**
     * 
     */
    public boolean isLabel;
    
    /**
     * 
     */    
    public boolean isLabelRef;

    /**
     * 
     */
    public Artifact artifact;

    /**
     * 
     */
    public JsonObjIsEntryType type;    

    /**
     * 
     */
    public TokenLine parentLine;

    /**
     * 
     */    
    public JsonObjIsRegister register;    

    /**
     * 
     */
    public List<Token> payload;    
}
