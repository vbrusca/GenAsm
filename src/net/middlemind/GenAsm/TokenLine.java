package net.middlemind.GenAsm;

import java.util.ArrayList;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/31/2021 5:29 PM EST
 */
public class TokenLine {
    public int lineNum;
    public int sourceLen;
    public ArtifactLine source;
    public ArrayList<Token> payload;    
}
