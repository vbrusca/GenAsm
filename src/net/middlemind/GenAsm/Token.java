package net.middlemind.GenAsm;

import java.util.List;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/31/2021 5:23 PM EST
 */
public class Token {
    public String obj_name = "Token";
    public String type_name;
    public int lineNum;
    public int index;
    public Object value;
    public Artifact artifact;    
    public String source;
    public JsonObj type;
    public int payloadLen;
    public List<Token> payload;
}
