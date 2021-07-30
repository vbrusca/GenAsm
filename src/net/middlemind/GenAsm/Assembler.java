package net.middlemind.GenAsm;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 10:18 AM EST
 */
public interface Assembler {
    public void LoadIsaData(JsonObjIsSet jsonIsSet);
    public void LoadAssemblyFile(String file);
    public void LineLexerize(String line, int lineNum);
    public void LineTokenize(String[] artifacts, int lineNum);
    public void LineParse(String[] tokens, int lineNum);
    public boolean GetIsIsaDataLoaded();
    public void SetIsIsaDataLoaded(boolean b);
    public boolean GetIsAssemblyFileLoaded();
    public void SetIsAssemblyFileLoaded(boolean b);
}
