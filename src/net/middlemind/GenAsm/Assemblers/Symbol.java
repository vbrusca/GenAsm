package net.middlemind.GenAsm.Assemblers;

import net.middlemind.GenAsm.Tokeners.TokenLine;
import net.middlemind.GenAsm.Tokeners.Token;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/11/2021 7:28 AM EST
 */
public class Symbol {
    public String obj_name = "Symbol";
    public String name;
    public TokenLine line;
    public Token token;
    public String addressHex;
    public String addressBin;
    public int addressInt;
    public int lineNumActive;
    public int lineNumAbs;    
    public boolean isEmptyLineLabel;
    public boolean isLocalLabel;
    public boolean isParentLabel;
    public boolean isLabel;
    public boolean isStaticValue;
    public Integer value;    
}
