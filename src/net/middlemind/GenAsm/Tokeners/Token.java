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
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */
    public String obj_name = "Token";
    
    /**
     * A string representation of the name of this type of token.
     */    
    public String type_name;

    /**
     * A string representation of the source code behind this token.
     */
    public String source;

    /**
     * A generic object used to hold a value associated with this token.
     * This is mainly used for static value labels.
     */
    public Object value;

    /**
     * An integer representing the line number of this token in the assembly source file.
     */
    public int lineNumAbs;

    /**
     * An integer representing the index of this token in a series of tokens that form a token line.
     */
    public int index;
    
    /**
     * An integer representing the length of the payload of child tokens associated with this token.
     * List and group tokens will have child tokens.
     */    
    public int payloadLen;

    /**
     * An integer representing the number of argument tokens in the payload.
     */
    public int payloadArgLen;

    /**
     * A Boolean value indicating if this token is an op-code argument.
     */
    public boolean isOpCodeArg;

    /**
     * A Boolean value indicating if this token is a directive argument.
     */
    public boolean isDirectiveArg;

    /**
     * A Boolean value indicating if this token is an op-code.
     */
    public boolean isOpCode;

    /**
     * A Boolean value indicating if this is a directive.
     */
    public boolean isDirective;

    /**
     * A Boolean value indicating if this is a comment.
     */
    public boolean isComment;

    /**
     * A Boolean value indicating if this is a label.
     */
    public boolean isLabel;
    
    /**
     * A Boolean value indicating if this is a label reference.
     */    
    public boolean isLabelRef;

    /**
     * An artifact object instance that's the basis for this token.
     */
    public Artifact artifact;

    /**
     * An object representing the instruction set entry type for this token, if applicable.
     */
    public JsonObjIsEntryType type;    

    /**
     * An object representing the instruction set register for this token, if applicable.
     */    
    public JsonObjIsRegister register;    

    /**
     * A list of token instances that represent the payload of this token.
     * List and group tokens have their own payloads.
     */
    public List<Token> payload;    
}