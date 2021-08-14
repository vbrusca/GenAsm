package net.middlemind.GenAsm;

import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/11/2021 7:28 AM EST
 */
public class Symbol {
    public String obj_name = "Symbol";
    public String name;
    public TokenLine line;
    public Token token;
    public int lineNum;
    public Map<String, Symbol> symbols = new Hashtable<>();
    public int lineNumActive;
    public boolean isEmptyLineLabel;
    public boolean isLocalLabel;
}
