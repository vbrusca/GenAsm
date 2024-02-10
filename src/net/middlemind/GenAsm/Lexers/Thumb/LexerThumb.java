package net.middlemind.GenAsm.Lexers.Thumb;

import java.util.ArrayList;
import java.util.List;
import net.middlemind.GenAsm.Lexers.Artifact;
import net.middlemind.GenAsm.Lexers.ArtifactLine;
import net.middlemind.GenAsm.Lexers.Lexer;
import net.middlemind.GenAsm.Utils;

/**
 * An implementation of the lexer interface used to lexerize a source assembly file for the ARM Thumb instruction set.
 * This is a simple lexer implementation that can most likely be used for other instruction sets but is named as such because it belongs
 * to the Thumb implementation.
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 3:17 PM EST
 */
public class LexerThumb implements Lexer {
    /**
     * A static array of characters that are text separators.
     */
    public static char[] CHAR_SEPARATORS = { ',', ' ', '\t', ';' };
    
    /**
     * A static array of characters that are sticky text separators.
     */    
    public static char[] CHAR_STICKY_SEPARATORS = { };

    /**
     * A static array of characters that are new artifact separators
     */
    public static char[] CHAR_NEW_ARTIFACT_SEPARATORS = { ';' };    

    /**
     * A static array of characters that are group start characters.
     */
    public static char[] CHAR_GROUP_START = { '[', '{' };

    /**
     * A static array of characters that are group stop characters.
     */
    public static char[] CHAR_GROUP_STOP = { ']', '}' };

    /**
     * A static array of characters that are white space characters.
     */
    public static char[] CHAR_WHITE_SPACE = { ' ', '\t' };
    
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */   
    public String obj_name = "LexerThumb";

    /**
     * A method used to check if the specified subject char is contained by the array argument.
     * @param array An array of characters to search.
     * @param subj  A subject character to search for.
     * @return      A Boolean value indicating if the subj character is found.
     */
    private boolean Contains(char[] array, char subj) {
        for(char c : array) {
            if(c == subj) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * A method used to lexerize an entire file of assembly source code.
     * @param file  A list of strings that represent the assembly source code to process.
     * @return      An array list of artifact lines that are extracted from the assembly source code.
     */
    @Override
    public ArrayList<ArtifactLine> FileLexerize(List<String> file) {
        ArrayList<ArtifactLine> ret = new ArrayList<>();
        int count = 0;
        for(String s : file) {
            ret.add(LineLexerize(s, count));
            count++;
        }
        return ret;
    }    
    
    /**
     * A method that lexerizes a line of assembly source code.
     * @param line      A string representation of the line to lexerize.
     * @param lineNum   An integer representation of the line number of the string to lexerize.
     * @return          An artifact line extracted from the line of assembly source code.
     */
    @Override
    @SuppressWarnings({"UnnecessaryContinue", "UnusedAssignment", "null"})
    public ArtifactLine LineLexerize(String line, int lineNum) {
        ArtifactLine ret = new ArtifactLine();
        ret.source = line;
        ret.lineNum = lineNum;
            
        if(Utils.IsStringEmpty(line)) {            
            ret.payload = new ArrayList<>();
            ret.sourceLen = 0;
            return ret;
        } else {
            ret.sourceLen = line.length();
            ret.payload = new ArrayList<>();
            
            char[] chars = line.toCharArray();
            boolean inArtifact = false;
            Artifact artifact = null;
            int i = 0;
            int count = 0;
            
            for(; i < chars.length; i++) {
                if(inArtifact == true) {
                    if(Contains(CHAR_SEPARATORS, chars[i])) {
                        inArtifact = false;
                        artifact.posStop = (i - 1);
                        artifact.len = (i - artifact.posStart);
                        artifact.index = count;
                        
                        if(Contains(CHAR_STICKY_SEPARATORS, chars[i])) {
                            artifact.payload += chars[i];
                            artifact.posStop++;
                            artifact.len++;                            
                        }
                        
                        ret.payload.add(artifact);                        
                        count++;
                        artifact = null;
                        
                        if(Contains(CHAR_NEW_ARTIFACT_SEPARATORS, chars[i])) {
                            artifact = new Artifact();
                            artifact.lineNum = lineNum;
                            artifact.posStart = i;
                            artifact.posStop = i; 
                            artifact.len = 1;
                            artifact.index = count;
                            artifact.payload = "" + chars[i];
                            ret.payload.add(artifact);                        
                            count++;
                            artifact = null;                            
                        }
                        
                    } else {
                        if(Contains(CHAR_GROUP_START, chars[i])) {
                            inArtifact = false;
                            artifact.posStop = (i - 1);
                            artifact.len = (i - artifact.posStart);
                            artifact.index = count;

                            if(Contains(CHAR_STICKY_SEPARATORS, chars[i])) {
                                artifact.payload += chars[i];
                                artifact.posStop++;
                                artifact.len++;
                            }

                            ret.payload.add(artifact);                        
                            count++;
                            artifact = null;                            
                            
                            artifact = new Artifact();
                            artifact.lineNum = lineNum;
                            artifact.posStart = i;
                            artifact.posStop = i; 
                            artifact.len = 1;
                            artifact.index = count;
                            artifact.payload = "" + chars[i];
                            ret.payload.add(artifact);                        
                            count++;
                            artifact = null;

                        } else if(Contains(CHAR_GROUP_STOP, chars[i])) {
                            inArtifact = false;
                            artifact.posStop = (i - 1);
                            artifact.len = (i - artifact.posStart);
                            artifact.index = count;

                            if(Contains(CHAR_STICKY_SEPARATORS, chars[i])) {
                                artifact.payload += chars[i];
                                artifact.posStop++;
                                artifact.len++;                                
                            }

                            ret.payload.add(artifact);                        
                            count++;
                            artifact = null;

                            artifact = new Artifact();
                            artifact.lineNum = lineNum;
                            artifact.posStart = i;
                            artifact.posStop = i;
                            artifact.len = 1;
                            artifact.index = count;                        
                            artifact.payload = "" + chars[i];
                            ret.payload.add(artifact);                        
                            count++;
                            artifact = null;
                            
                        } else {
                            artifact.payload += chars[i];
                        
                        }
                    }
                } else {
                    if(Contains(CHAR_WHITE_SPACE, chars[i])) {
                        //ignore whitespace
                        continue;
                        
                    } else if(Contains(CHAR_GROUP_START, chars[i])) {
                        artifact = new Artifact();
                        artifact.lineNum = lineNum;
                        artifact.posStart = i;
                        artifact.posStop = i;
                        artifact.len = 1;
                        artifact.index = count;
                        artifact.payload = "" + chars[i];
                        ret.payload.add(artifact);                        
                        count++;
                        artifact = null;
                        
                    } else if(Contains(CHAR_GROUP_STOP, chars[i])) {                        
                        artifact = new Artifact();
                        artifact.lineNum = lineNum;
                        artifact.posStart = i;
                        artifact.posStop = i;
                        artifact.len = 1;
                        artifact.index = count;                        
                        artifact.payload = "" + chars[i];
                        ret.payload.add(artifact);                        
                        count++;
                        artifact = null;                        
                        
                    } else {
                        inArtifact = true;
                        artifact = new Artifact();
                        artifact.lineNum = lineNum;
                        artifact.posStart = i;
                        artifact.payload = "" + chars[i];
                        
                    }
                }
            }
            
            if(artifact != null) {
                artifact.posStop = (i - 1);
                artifact.len = (i - artifact.posStart);
                artifact.index = count;
                ret.payload.add(artifact);
            }
            
            return ret;
        }
    }
}
