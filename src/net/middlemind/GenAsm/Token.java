package net.middlemind.GenAsm;

import java.util.List;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/31/2021 5:23 PM EST
 */
public class Token {
    public String name;
    public int lineNum;
    public int index;
    public Object value;
    public Artifact payloadArtifact;    
    public String payloadSource;
    public JsonObj payloadType;
    public List<Token> argTokens;
}
