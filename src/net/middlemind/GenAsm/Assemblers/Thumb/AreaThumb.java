package net.middlemind.GenAsm.Assemblers.Thumb;

import net.middlemind.GenAsm.Tokeners.Token;
import net.middlemind.GenAsm.Tokeners.TokenLine;

/**
 * A class that tracks the specifics of an assembly source area.
 * An area is a block of assembly source lines.
 * @author Victor G. Brusca, Middlemind Games 08/16/2021 1:29 PM EST
 */
public class AreaThumb {
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */    
    public String obj_name = "AreaThumb";
    
    /**
     * The TokenLine that the area directive is located on.
     */
    public TokenLine areaLine;
    
    /**
     * The Token that the area directive is located in.
     */
    public Token area;

    /**
     * The TokenLine that the entry directive is located on.
     */
    public TokenLine entryLine;
    
    /**
     * The Token that the entry directive is located in.
     */
    public Token entry;

    /**
     * The TokenLine that the end directive is located on.
     */
    public TokenLine endLine;    
    
    /**
     * The Token that the end directive is located in.
     */
    public Token end;

    /**
     * The absolute line number of the area directive.
     */
    public int lineNumArea;
    
    /**
     * The absolute line number of the entry directive.
     */
    public int lineNumEntry;
    
    /**
     * The absolute line number of the end directive.
     */
    public int lineNumEnd;
    
    /**
     * A Boolean value indicating if this is a code area.
     */
    public boolean isCode;
    
    /**
     * A Boolean value indicating if this is a read-only area.
     */
    public boolean isReadOnly;
    
    /**
     * A Boolean value indicating if this is a data area.
     */
    public boolean isData;
    
    /**
     * A Boolean value indicating if this is a read-write area.
     */    
    public boolean isReadWrite;
    
    /**
     * A string representing the title of this directive.
     */
    public String title;
}
