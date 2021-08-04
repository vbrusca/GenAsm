package net.middlemind.GenAsm;

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
    public List<ArtifactLine> asmSourceLexed;
    
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
        LoadAndLexAssemblySource();
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
    
    public void LoadAndLexAssemblySource() {
        
    }
}
