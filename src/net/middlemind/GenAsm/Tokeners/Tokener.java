package net.middlemind.GenAsm.Tokeners;

import net.middlemind.GenAsm.Lexers.ArtifactLine;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionNoTokenerFound;
import net.middlemind.GenAsm.JsonObjs.JsonObj;
import java.util.List;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/31/2021 5:13 PM EST
 */
public interface Tokener {
    public TokenLine LineTokenize(ArtifactLine line, int lineNum, JsonObj entryTypes) throws ExceptionNoTokenerFound;
    public List<TokenLine> FileTokenize(List<ArtifactLine> file, JsonObj entryTypes) throws ExceptionNoTokenerFound;
}