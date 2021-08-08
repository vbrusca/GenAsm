package net.middlemind.GenAsm;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 3:17 PM EST
 */
public class LexerSimple implements Lexer {

    public static char[] SEPARATORS = { ',', ' ', '\t', ';' };
    public static char[] STICKY_SEPARATORS = { ',' };
    public static char[] NEW_ARTIFACT_SEPARATORS = { ';' };    
    public static char[] GROUP_START = { '[', '{' };
    public static char[] GROUP_STOP = { ']', '}' };    
    
    private boolean Contains(char[] array, char subj) {
        for(char c : array) {
            if(c == subj) {
                return true;
            }
        }
        return false;
    }
    
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
    
    @Override
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
                    if(Contains(SEPARATORS, chars[i])) {
                        inArtifact = false;
                        artifact.posStop = (i - 1);
                        artifact.len = (i - artifact.posStart);
                        artifact.index = count;
                        
                        if(Contains(STICKY_SEPARATORS, chars[i])) {
                            artifact.payload += chars[i];
                        }
                        
                        ret.payload.add(artifact);                        
                        count++;
                        artifact = null;
                        
                        if(Contains(NEW_ARTIFACT_SEPARATORS, chars[i])) {
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
                        if(Contains(GROUP_START, chars[i])) {
                            inArtifact = false;
                            artifact.posStop = (i - 1);
                            artifact.len = (i - artifact.posStart);
                            artifact.index = count;

                            if(Contains(STICKY_SEPARATORS, chars[i])) {
                                artifact.payload += chars[i];
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

                        } else if(Contains(GROUP_STOP, chars[i])) {
                            inArtifact = false;
                            artifact.posStop = (i - 1);
                            artifact.len = (i - artifact.posStart);
                            artifact.index = count;

                            if(Contains(STICKY_SEPARATORS, chars[i])) {
                                artifact.payload += chars[i];
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
                    if(chars[i] == ' ' || chars[i] == '\t') {
                        //ignore whitespace
                        continue;
                        
                    } else if(Contains(GROUP_START, chars[i])) {
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
                        
                    } else if(Contains(GROUP_STOP, chars[i])) {                        
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
                
                if(artifact.payload.indexOf(System.lineSeparator()) != (artifact.payload.length() - 1)) {
                    artifact.payload += System.lineSeparator();
                }
                
                ret.payload.add(artifact);
            }
            
            return ret;
        }
    }
}
