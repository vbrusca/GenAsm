package net.middlemind.GenAsm.Lexers;

import java.util.List;

/**
 * A class used to represent a line of extracted text.
 * The individually extracted groups of text are stored in Artifact instances.
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 4:14 PM EST
 */
public class ArtifactLine {
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */
    public String obj_name = "ArtifactLine";
    
    /**
     * An integer representation of the line number of the assembly source code that this artifact line is based on.
     */
    public int lineNum;
    
    /**
     * An integer representing the string length of the line of assembly source code this artifact line is based on.
     */
    public int sourceLen;
    
    /**
     * A string representation of the source text, assembly source code, that this artifact line is based on.
     */
    public String source;
    
    /**
     * A list of Artifact instances that are used to represent the groups of extracted text from a line of assembly source code.
     */
    public List<Artifact> payload;
}
