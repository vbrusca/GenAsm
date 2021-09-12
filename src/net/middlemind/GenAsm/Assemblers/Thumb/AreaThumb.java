package net.middlemind.GenAsm.Assemblers.Thumb;

import net.middlemind.GenAsm.Tokeners.Token;
import net.middlemind.GenAsm.Tokeners.TokenLine;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/16/2021 1:29 PM EST
 */
public class AreaThumb {
    public String obj_name = "AreaThumb";
    public TokenLine areaLine;
    public Token area;
    public Token entry;
    public TokenLine entryLine;
    public Token end;
    public TokenLine endLine;
    public int lineNumArea;
    public int lineNumEntry;
    public int lineNumEnd;
    public boolean isCode;
    public boolean isReadOnly;
    public boolean isData;
    public boolean isReadWrite;
    public String title;
}
