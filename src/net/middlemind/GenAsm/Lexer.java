package net.middlemind.GenAsm;

import java.util.List;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07-19-2021 7:05 PM EST
 */
public interface Lexer {
    public List<Artifact> LineLexerize(String line, int lineNum);
    public void Print();
    public void Print(String prefix);
}
