package net.middlemind.GenAsm;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/16/2021 1:29 PM EST
 */
public class AreaThumb {
    public TokenLine areaDirective;
    public Token entry;
    public Token end;
    public int lineNumEntry;
    public int lineNumEnd;
    public boolean isCode;
    public boolean isReadonly;
    public boolean isData;
    public boolean isReadwrite;
}
