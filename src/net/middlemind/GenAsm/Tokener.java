package net.middlemind.GenAsm;

import java.util.List;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/31/2021 5:13 PM EST
 */
public interface Tokener {
    public TokenLine LineTokenize(ArtifactLine line, int lineNum, JsonObj entryTypes);
    public List<TokenLine> FileTokenize(List<ArtifactLine> file, JsonObj entryTypes);
}
