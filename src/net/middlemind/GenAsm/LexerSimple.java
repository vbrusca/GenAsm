package net.middlemind.GenAsm;

import java.util.ArrayList;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 3:17 PM EST
 */
public class LexerSimple implements Lexer {

    @Override
    public ArrayList<Artifact> LineLexerize(String line, int lineNum) {
        if(Utils.IsStringEmpty(line)) {
            return new ArrayList<Artifact>();
        } else {
            ArrayList<Artifact> artifacts = new ArrayList<>();
            char[] chars = line.toCharArray();
            boolean inArtifact = false;
            Artifact artifact = null;
            int i = 0;
            for(; i < chars.length; i++) {
                if(inArtifact == true) {
                    if(chars[i] == ' ' || chars[i] == '\t') {
                        inArtifact = false;
                        artifact.posStop = (i - 1);
                        artifact.len = (i - artifact.posStart);
                        artifacts.add(artifact);
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
                artifacts.add(artifact);
            }
            
            return artifacts;
        }
    }    

    @Override
    public void Print() {
        Print("");
    }

    @Override
    public void Print(String prefix) {
        
    }
}
