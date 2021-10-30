package net.middlemind.GenAsm.Assemblers;

import net.middlemind.GenAsm.Tokeners.TokenLine;
import net.middlemind.GenAsm.Tokeners.Token;

/**
 * A class used to represent a Symbol which is a value place holder defined in the assembly source.
 * @author Victor G. Brusca, Middlemind Games 08/11/2021 7:28 AM EST
 */
public class Symbol {
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */
    public String obj_name = "Symbol";
    
    /**
     * A string representing the name of this symbol entry.
     */
    public String name;
    
    /**
     * The line of text that is associated with this symbol.
     */
    public TokenLine line;
    
    /**
     * The text token associated with this symbol.
     */
    public Token token;
    
    /**
     * A hex representation of the line address associated with this symbol.
     */
    public String addressHex;
    
    /**
     * A binary representation of the line address associated with this symbol.
     */
    public String addressBin;
    
    /**
     * The integer offset of the line associated with this symbol, active lines only. Active lines are those that are represented by binary data.
     */
    public int addressInt;
    
    /**
     * The active line number of this symbol. Active lines are those that are represented by binary data.
     */
    public int lineNumActive;
    
    /**
     * The absolute line number of this symbol. Absolute lines are those that don't take into account if the line is active or not.
     */
    public int lineNumAbs;
    
    /**
     * A Boolean value indicating if the line associated with this symbol is an empty label line, i.e. a line with only a label.
     */
    public boolean isEmptyLineLabel;
    
    /**
     * A Boolean value indicating if the line associated with this symbol is a label line.
     */
    public boolean isLabel;
    
    /**
     * A Boolean value indicating if the line associated with this symbol is a static value.
     */
    public boolean isStaticValue;
    
    /**
     * An integer representing the static value of this symbol if it's a static value symbol.
     */
    public Integer value;    
}
