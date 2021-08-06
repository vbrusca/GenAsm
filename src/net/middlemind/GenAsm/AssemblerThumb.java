package net.middlemind.GenAsm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 12:08 PM EST
 */
public class AssemblerThumb implements Assembler {

    public JsonObjIsSet isaDataSet;
    public Map<String, JsonObj> isaData;
    public Map<String, Loader> isaLoader;
    public Map<String, String> jsonSource;
    public JsonObjIsEntryTypes jsonObjIsEntryTypes;

    public String asmSourceFile;
    public List<String> asmSourceData;
    public List<ArtifactLine> asmLexedData;
    public List<TokenLine> asmTokenedData;
    
    @Override
    public void RunAssembler(JsonObjIsSet jsonIsSet, String assemblySourceFile) {
        Logger.wrl("AssemblerThumb: RunAssembler: Start");
        jsonSource = new Hashtable<String, String>();
        isaLoader = new Hashtable<String, Loader>();        
        isaData = new Hashtable<String, JsonObj>();
        isaDataSet = jsonIsSet;
        asmSourceFile = assemblySourceFile;
        
        //Process JsonObjIsSet's file entries and load then parse the json object data
        LoadAndParseJsonObjData();
        
        //Link loaded json object data
        LinkJsonObjData();
        
        //Load and lexerize the assembly source file
        LoadAndLexerizeAssemblySource();
        Logger.wrl("");
        Logger.wrl("");        
        PrintObject(asmLexedData, "Assembly Lexerized Data");
        
        //Tokenize the lexerized artifacts
        TokenizeLexerArtifacts();
        Logger.wrl("");
        Logger.wrl("");
        PrintObject(asmTokenedData, "Assembly Tokenized Data");        
    }
    
    public void PrintObject(Object obj, String name) {
        Logger.wrl("AssemblerThumb: PrintObject: Name: " + name);
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
            
        Gson gson = builder.create();            
        String jsonString = gson.toJson(obj);
        Logger.wr(jsonString);        
    }
    
    public void LoadAndParseJsonObjData() {
        for(JsonObjIsFile entry : isaDataSet.is_files) {
            try {
                Class cTmp = Class.forName(entry.loader_class);
                Loader ldr = (Loader)cTmp.getDeclaredConstructor().newInstance();
                String json;
                String jsonName;
                JsonObj jsonObj;
                
                isaLoader.put(entry.loader_class, ldr);
                Logger.wrl("AssemblerThumb: RunAssembler: Loader created " + entry.loader_class);
                
                json = FileLoader.LoadStr(entry.path);
                jsonSource.put(entry.path, json);
                Logger.wrl("AssemblerThumb: RunAssembler: Json loaded " + entry.path);
                                
                jsonObj = ldr.ParseJson(json, entry.target_class, entry.path);
                jsonName = jsonObj.GetName();
                isaData.put(jsonName, jsonObj);
                Logger.wrl("AssemblerThumb: RunAssembler: Json parsed as " + entry.target_class);
                
                if(jsonObj.GetLoader().equals("net.middlemind.GenAsm.LoaderIsEntryTypes")) {
                    jsonObjIsEntryTypes = (JsonObjIsEntryTypes)jsonObj;
                    Logger.wrl("AssemblerThumb: RunAssembler: Found JsonObjIsEntryTypes object, storing it...");
                }                
            } catch (LoaderException | IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                Logger.wrl("AssemblerThumb: RunAssembler: Error: Could not instantiate loader class " + entry.loader_class);
                e.printStackTrace();
                return;
            }
        }        
    }
    
    public void LinkJsonObjData() {
        Logger.wrl("AssemblerThumb: LinkJsonObjData");
        for(String s : isaData.keySet()) {
            try {
                JsonObj jsonObj = isaData.get(s);
                jsonObj.Link(jsonObjIsEntryTypes);
            } catch (JsonObjLinkException e) {
                Logger.wrl("AssemblerThumb: RunAssembler: Error: Could not link " + s);
                e.printStackTrace();
                return;                
            }
        }        
    }
    
    public void LoadAndLexerizeAssemblySource() {        
        try {
            Logger.wrl("AssemblerThumb: LoadAndLexAssemblySource: Load assembly source file");
            asmSourceData = FileLoader.Load(asmSourceFile);
            
            Logger.wrl("AssemblerThumb: LoadAndLexAssemblySource: Lex assembly source file");
            LexerSimple lex = new LexerSimple();
            asmLexedData = lex.FileLexerize(asmSourceData);
        } catch (IOException e) {
            Logger.wrl("AssemblerThumb: LoadAndLexAssemblySource: Error: Could not load and lex assembly source file " + asmSourceFile);
            e.printStackTrace();
            return;            
        }
    }
    
    public void TokenizeLexerArtifacts() {
        try {
            TokenerThumb tok = new TokenerThumb();
            asmTokenedData = tok.FileTokenize(asmLexedData, jsonObjIsEntryTypes);
        } catch (TokenerNotFoundException e) {
            Logger.wrl("AssemblerThumb: TokenizeLexerArtifacts: Error: Could not tokenize lexed artifacts");
            e.printStackTrace();
            
        }
    }
}
