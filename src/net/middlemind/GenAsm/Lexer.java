package net.middlemind.GenAsm;

import java.util.List;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07-19-2021 7:05 PM EST
 */
public interface Lexer {
    public ArtifactLine LineLexerize(String line, int lineNum);
    public List<ArtifactLine> FileLexerize(List<String> file);
    public void Print();
    public void Print(String prefix);
}
