package net.middlemind.GenAsm.Lexers;

import java.util.List;

/**
 * An interface that defines a Lexer used in the assembly process.
 * The lexer is responsible for the initial extraction of artifacts, grouped text, from the assembly source file.
 * @author Victor G. Brusca, Middlemind Games 07-19-2021 7:05 PM EST
 */
public interface Lexer {
    /**
     * A method used to lexerize a line of assembly source code.
     * @param line      The line of assembly source code.
     * @param lineNum   The line number of the assembly source code.
     * @return          An instance of the ArtifactLine class that represents the extracted text.
     */
    public ArtifactLine LineLexerize(String line, int lineNum);
    
    /**
     * A method used to lexerize a list of String where each string is a line of assembly source code.
     * @param file      A list of strings representing the assembly source file's contents.
     * @return          A list of ArtifactLine instances that represent the text extracted from each line of assembly source code.
     */
    public List<ArtifactLine> FileLexerize(List<String> file);
}
