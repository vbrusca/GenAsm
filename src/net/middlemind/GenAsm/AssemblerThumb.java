package net.middlemind.GenAsm;

import java.util.List;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 12:08 PM EST
 */
public class AssemblerThumb implements Assembler {

    public boolean isaDataLoaded;
    public boolean isaDataParsed;
    public boolean isaDataLinked;
    public boolean assemblyFileLoaded;
    public boolean assemblyFileLexerized;
    public boolean assemblyFileTokenized;
    public boolean assemblyFileParsed;    
    
    /* DATA FILE METHODS */
    @Override
    public JsonObjIsSet FilesIsaDataLoad(JsonObjIsSet jsonIsSet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JsonObjIsSet FilesIsaDataParse(JsonObjIsSet jsonIsSet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JsonObjIsSet FilesIsaDataLink(JsonObjIsSet jsonIsSet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /* ASSEMBLY FILE METHODS */
    @Override
    public List<String> FileAssemblyLoad(String file) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ArtifactLine> FileAssemblyLexerize(List<String> file) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TokenLine> FileAssemblyTokenize(List<ArtifactLine> file) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /* ASSEMBLY LINE METHODS */
    @Override
    public ArtifactLine LineLexerize(String line, int lineNum) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TokenLine LineTokenize(ArtifactLine line, int lineNum) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /* BOOLEAN INDICATORS GET/SET METHODS */
    @Override
    public boolean GetIsAssemblyFileLexerized() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void SetIsAssemblyFileLexerized(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean GetIsAssemblyFileLoaded() {
        return assemblyFileLoaded;
    }

    @Override
    public void SetIsAssemblyFileLoaded(boolean b) {
        assemblyFileLoaded = b;
    }    
    
    @Override
    public boolean GetIsAssemblyFileTokenized() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void SetIsAssemblyFileTokenized(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }    
    
    @Override
    public boolean GetIsAssemblyFileParsed() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void SetIsAssemblyFileParsed(boolean b) {
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
    public boolean GetIsIsaDataParsed() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void SetIsIsaDataParsed(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    @Override
    public boolean GetIsIsaDataLinked() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void SetIsIsaDataLinked(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }   
}
