package net.middlemind.GenAsm;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 3:17 PM EST
 */
public class LexerSimple implements Lexer {

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
            ret.payload = new ArrayList<Artifact>();
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
                    if(chars[i] == ' ' || chars[i] == '\t') {
                        inArtifact = false;
                        artifact.posStop = (i - 1);
                        artifact.len = (i - artifact.posStart);
                        artifact.index = count;
                        ret.payload.add(artifact);
                        
                        count++;
                        artifact = null;
                    } else {
                        artifact.payload += chars[i];                
                    }
                } else {
                    if(chars[i] == ' ' || chars[i] == '\t') {
                        //ignore whitespace
                        continue;
                    } else {
                        inArtifact = true;
                        artifact = new Artifact();
                        artifact.lineNum = lineNum;
                        artifact.posStart = i;
                        artifact.payload = "";
                        artifact.payload += chars[i];
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
