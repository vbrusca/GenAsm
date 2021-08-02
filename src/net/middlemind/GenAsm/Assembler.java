package net.middlemind.GenAsm;

import java.util.List;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 10:18 AM EST
 */
public interface Assembler {
    public void RunAssembler(JsonObjIsSet jsonIsSet);
    
    public JsonObjIsSet FilesIsaDataLoad(JsonObjIsSet jsonIsSet);
    public JsonObjIsSet FilesIsaDataParse(JsonObjIsSet jsonIsSet);
    public JsonObjIsSet FilesIsaDataLink(JsonObjIsSet jsonIsSet);    

    public List<String> FileAssemblyLoad(String file);
    public List<ArtifactLine> FileAssemblyLexerize(List<String> file);
    public List<TokenLine> FileAssemblyTokenize(List<ArtifactLine> file);
    //public void FileAssemblyParse(List<TokenLine> file);    
    
    public ArtifactLine LineLexerize(String line, int lineNum);
    public TokenLine LineTokenize(ArtifactLine line, int lineNum);
    //public void LineParse(TokenLine, int lineNum);
        
    public boolean GetIsIsaDataLoaded();
    public void SetIsIsaDataLoaded(boolean b);
    
    public boolean GetIsIsaDataParsed();
    public void SetIsIsaDataParsed(boolean b);

    public boolean GetIsIsaDataLinked();
    public void SetIsIsaDataLinked(boolean b);    
    
    public boolean GetIsAssemblyFileLoaded();
    public void SetIsAssemblyFileLoaded(boolean b);
    
    public boolean GetIsAssemblyFileLexerized();
    public void SetIsAssemblyFileLexerized(boolean b);    
    
    public boolean GetIsAssemblyFileTokenized();
    public void SetIsAssemblyFileTokenized(boolean b);        
    
    public boolean GetIsAssemblyFileParsed();
    public void SetIsAssemblyFileParsed(boolean b);        
}
