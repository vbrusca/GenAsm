package net.middlemind.GenAsm.Lexers;

import java.util.List;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 4:14 PM EST
 */
public class ArtifactLine {
    public String obj_name = "ArtifactLine";
    public int lineNum;
    public int sourceLen;
    public String source;
    public List<Artifact> payload;
}