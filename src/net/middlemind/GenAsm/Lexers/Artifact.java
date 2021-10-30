package net.middlemind.GenAsm.Lexers;

/**
 * A class representing an extraction of grouped text from a line of assembly source code.
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 4:18 PM EST
 */
public class Artifact {
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */
    public String obj_name = "Artifact";

    /**
     * An integer representing the starting position of the extracted text.
     */    
    public int posStart;

    /**
     * An integer representing the ending position of the extracted text.
     */
    public int posStop;

    /**
     * An integer representing the length of the text artifact.
     */    
    public int len;

    /**
     * An integer representing the line number this artifact of extracted text is from.
     */
    public int lineNum;

    /**
     * An integer value representing the index of this artifact in a series of artifacts that represent a line of assembly source code.
     */
    public int index;

    /**
     * A string representation of the original assembly source text.
     */
    public String payload;
}
