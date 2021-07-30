package net.middlemind.GenAsm;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 12:08 PM EST
 */
public class AssemblerThumb implements Assembler {

    public boolean isaDataLoaded;
    public boolean assemblyFileLoaded;
    
    @Override
    public void LoadIsaData(JsonObjIsSet jsonIsSet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void LoadAssemblyFile(String file) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void LineLexerize(String line, int lineNum) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void LineTokenize(String[] artifacts, int lineNum) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void LineParse(String[] tokens, int lineNum) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean GetIsIsaDataLoaded() {
        return isaDataLoaded;
    }

    @Override
    public void SetIsIsaDataLoaded(boolean b) {
        isaDataLoaded = b;
    }

    @Override
    public boolean GetIsAssemblyFileLoaded() {
        return assemblyFileLoaded;
    }

    @Override
    public void SetIsAssemblyFileLoaded(boolean b) {
        assemblyFileLoaded = b;
    }
}
