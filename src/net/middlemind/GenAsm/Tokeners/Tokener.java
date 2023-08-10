package net.middlemind.GenAsm.Tokeners;

import net.middlemind.GenAsm.Lexers.ArtifactLine;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionNoTokenerFound;
import net.middlemind.GenAsm.JsonObjs.JsonObj;
import java.util.List;

/**
 * An interface used to define the basic tokener class implementation.
 * @author Victor G. Brusca, Middlemind Games 07/31/2021 5:13 PM EST
 */
public interface Tokener {
    /**
     * A method used to tokenize an artifact line.
     * @param line                      The artifact line to tokenize.
     * @param lineNum                   The line number of the artifact line being tokenized.
     * @param entryTypes                An object representing the instruction sets entry types.
     * @return                          A token line derived from the artifact lines.
     * @throws ExceptionNoTokenerFound  An exception is thrown if there is an error during the tokening process.
     */
    public TokenLine LineTokenize(ArtifactLine line, int lineNum, JsonObj entryTypes) throws ExceptionNoTokenerFound;

    /**
     * A method used to tokenize a lexerized assembly source file.
     * @param file                      A list of artifact lines that represent the assembly source file to process.
     * @param entryTypes                An object representing the instruction sets entry types.
     * @return                          A list of token line instances that represent the processed list of artifact lines.
     * @throws ExceptionNoTokenerFound  An exception is thrown if there is an error during the file tokening process.
     */
    public List<TokenLine> FileTokenize(List<ArtifactLine> file, JsonObj entryTypes) throws ExceptionNoTokenerFound;
}
