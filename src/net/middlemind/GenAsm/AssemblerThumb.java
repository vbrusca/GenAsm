package net.middlemind.GenAsm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
    public JsonObjIsValidLines jsonObjIsValidLines;
    
    public String asmSourceFile;
    public List<String> asmSourceData;
    public List<ArtifactLine> asmLexedData;
    public List<TokenLine> asmTokenedData;
    
    @Override
    public void RunAssembler(JsonObjIsSet jsonIsSet, String assemblySourceFile) {
        try {
            Logger.wrl("AssemblerThumb: RunAssembler: Start");
            jsonSource = new Hashtable<String, String>();
            isaLoader = new Hashtable<String, Loader>();        
            isaData = new Hashtable<String, JsonObj>();
            isaDataSet = jsonIsSet;
            asmSourceFile = assemblySourceFile;

            //Process JsonObjIsSet's file entries and load then parse the json object data
            LoadAndParseJsonObjData();

            //Link loaded json object data
            Logger.wrl("");
            LinkJsonObjData();

            //Load and lexerize the assembly source file
            Logger.wrl("");
            LoadAndLexerizeAssemblySource();
            WriteObject(asmLexedData, "Assembly Lexerized Data", "/Users/victor/Documents/files/netbeans_workspace/GenAsm/cfg/THUMB/TESTS/output_lexed.json");        

            //Tokenize the lexerized artifacts
            Logger.wrl("");
            TokenizeLexerArtifacts();

            //Validate token lines
            Logger.wrl("");
            if(ValidateTokenizedLines()) {
                Logger.wrl("Assembly lines validated successfully.");
            } else {
                Logger.wrl("Assembly lines are NOT valid.");            
            }

            Logger.wrl("");
            //Second level token processing
            CollapseCommentTokens();
            ExpandRegisterRangeTokens();
            //CollapseListAndGroupTokens();
            WriteObject(asmTokenedData, "Assembly Tokenized Data", "/Users/victor/Documents/files/netbeans_workspace/GenAsm/cfg/THUMB/TESTS/output_tokened.json");

        } catch(Exception e) {
            Logger.wrl("AssemblerThumb: RunAssembler: Assembler encountered an exception, exiting...");
            e.printStackTrace();
        }
    }
    
    public JsonObjIsEntryType FindEntryType(String entryName) throws ExceptionEntryNotFound {        
        for(JsonObjIsEntryType entry : jsonObjIsEntryTypes.is_entry_types) {
            if(entry.type_name.equals(entryName)) {
                return entry;
            }
        }
        
        throw new ExceptionEntryNotFound("Could not find entry by name, " + entryName + ", in loaded entry types.");
    }
    
    public void ExpandRegisterRangeTokens() throws Exception {
        try {
            Logger.wrl("AssemblerThumb: ExpandRegisterRangeToken");
            for(TokenLine line : asmTokenedData) {
                boolean inRangeLow = false;
                boolean inRangeHi = false;
                Token rangeLowRoot = null;
                Token rangeHiRoot = null;
                List<Token> rangeLowAddTokens = new ArrayList<>();
                List<Token> rangeHiAddTokens = new ArrayList<>();
                JsonObjIsEntryType entryTypeRegLow = FindEntryType(JsonObjIsEntryTypes.ENTRY_TYPE_NAME_REGISTER_LOW);
                JsonObjIsEntryType entryTypeRegHi = FindEntryType(JsonObjIsEntryTypes.ENTRY_TYPE_NAME_REGISTER_HI);                

                for(Token token : line.payload) {
                    if(token.type_name.equals(JsonObjIsEntryTypes.ENTRY_TYPE_NAME_REGISTER_RANGE_LOW)) {
                        if(!inRangeLow) {
                            rangeLowRoot = token;
                            rangeLowRoot.payload = new ArrayList<>();                        

                            String rangeStr = rangeLowRoot.source;
                            rangeStr = rangeStr.replace(JsonObjIsRegisters.REGISTER_START_CHAR, "");
                            int[] range = null;
                            try {
                                range = Utils.GetIntsFromRange(rangeStr, JsonObjIsRegisters.REGISTER_RANGE_CHAR);
                                for(int i = range[0]; i < range[1]; i++) {

                                }
                            } catch (ExceptionMalformedRange e) {

                            }

                            inRangeLow = true;
                        } else {
                            token.index = rangeLowRoot.payload.size();
                            rangeLowRoot.payload.add(token);
                            //rangeLowAddTokens.add(token);
                        }
                    } else if(token.type_name.equals(JsonObjIsEntryTypes.ENTRY_TYPE_NAME_REGISTER_RANGE_HI)) {
                        if(!inRangeHi) {
                            rangeHiRoot = token;
                            rangeHiRoot.payload = new ArrayList<>();                        
                            inRangeHi = true;
                        } else {
                            token.index = rangeHiRoot.payload.size();
                            rangeHiRoot.payload.add(token);
                            //rangeHiAddTokens.add(token);
                        }                     
                    }
                }

                if(inRangeLow) {
                    //rangeLowRoot.payloadLen = commentRoot.payload.size();
                    //line.payload.removeAll(clearTokens);
                    //line.payloadLen = line.payload.size();
                } else if(inRangeHi) {
                    //rangeLowRoot.payloadLen = commentRoot.payload.size();
                    //line.payload.removeAll(clearTokens);
                    //line.payloadLen = line.payload.size();                
                }
            }
        } catch (ExceptionEntryNotFound e) {
            Logger.wrl("AssemblerThumb: ExpandRegisterRangeToken: Could not find required entry type.");
            e.printStackTrace();
            throw e;
        }
    }
    
    public void CollapseCommentTokens() {
        Logger.wrl("AssemblerThumb: CollapseCommentTokens");        
        for(TokenLine line : asmTokenedData) {
            boolean inComment = false;
            Token commentRoot = null;
            List<Token> clearTokens = new ArrayList<>();            
            for(Token token : line.payload) {
                if(token.type_name.equals(JsonObjIsEntryTypes.ENTRY_TYPE_NAME_COMMENT)) {
                    if(!inComment) {
                        commentRoot = token;
                        commentRoot.payload = new ArrayList<>();                        
                        inComment = true;
                    } else {
                        token.index = commentRoot.payload.size();
                        commentRoot.payload.add(token);
                        clearTokens.add(token);
                    }                   
                }
            }
            
            if(inComment) {
                commentRoot.payloadLen = commentRoot.payload.size();
                line.payload.removeAll(clearTokens);
                line.payloadLen = line.payload.size();
            }
        }
    }
    
    public void WriteObject(Object obj, String name, String fileName) throws Exception {
        Logger.wrl("AssemblerThumb: WriteObject: Name: " + name);
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
            
        Gson gson = builder.create();            
        String jsonString = gson.toJson(obj);

        try {
            FileUnloader.WriteStr(fileName, jsonString);
        } catch(IOException e) {
            e.printStackTrace();
            Logger.wrl("AssemblerThumb: WriteObject: Could not write the target output file, " + fileName);
            throw e;
        }
    }
    
    public void PrintObject(Object obj, String name) {
        Logger.wrl("AssemblerThumb: PrintObject: Name: " + name);
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
            
        Gson gson = builder.create();            
        String jsonString = gson.toJson(obj);
        Logger.wr(jsonString);        
    }
    
    public void LoadAndParseJsonObjData() throws Exception {
        try {
            Logger.wrl("AssemblerThumb: LoadAndParseJsonObjData");
            for(JsonObjIsFile entry : isaDataSet.is_files) {
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
                Logger.wrl("AssemblerThumb: RunAssembler: Loading isaData with entry " + jsonName);
                
                if(jsonObj.GetLoader().equals("net.middlemind.GenAsm.LoaderIsEntryTypes")) {
                    jsonObjIsEntryTypes = (JsonObjIsEntryTypes)jsonObj;
                    Logger.wrl("AssemblerThumb: RunAssembler: Found JsonObjIsEntryTypes object, storing it...");
                } else if(jsonObj.GetLoader().equals("net.middlemind.GenAsm.LoaderIsValidLines")) {
                    jsonObjIsValidLines = (JsonObjIsValidLines)jsonObj;
                    Logger.wrl("AssemblerThumb: RunAssembler: Found JsonObjIsValidLines object, storing it...");
                }
            }
            
            if(jsonObjIsEntryTypes == null) {
                throw new ExceptionEntryNotFound("Could not find required JsonObjIsEntryTypes instance.");
            } else if(jsonObjIsValidLines == null) {
                throw new ExceptionEntryNotFound("Could not find required JsonObjIsValidLines instance.");                    
            }
            
        } catch (ExceptionLoader | IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            Logger.wrl("AssemblerThumb: RunAssembler: Error: Could not load and parse json data files.");
            e.printStackTrace();
            throw e;
        }        
    }
    
    public void LinkJsonObjData() throws Exception {
        Logger.wrl("AssemblerThumb: LinkJsonObjData");
        for(String s : isaData.keySet()) {
            try {
                JsonObj jsonObj = isaData.get(s);
                jsonObj.Link(jsonObjIsEntryTypes);
            } catch (ExceptionJsonObjLink e) {
                Logger.wrl("AssemblerThumb: RunAssembler: Error: Could not link " + s);
                e.printStackTrace();
                throw e;
            }
        }        
    }
    
    public void LoadAndLexerizeAssemblySource() throws Exception {        
        try {
            Logger.wrl("AssemblerThumb: LoadAndLexAssemblySource: Load assembly source file");
            asmSourceData = FileLoader.Load(asmSourceFile);
            
            Logger.wrl("AssemblerThumb: LoadAndLexAssemblySource: Lexerize assembly source file");
            LexerSimple lex = new LexerSimple();
            asmLexedData = lex.FileLexerize(asmSourceData);
        } catch (IOException e) {
            Logger.wrl("AssemblerThumb: LoadAndLexAssemblySource: Error: Could not load and lex assembly source file " + asmSourceFile);
            e.printStackTrace();
            throw e;
        }
    }
    
    public void TokenizeLexerArtifacts() throws Exception {
        try {
            Logger.wrl("AssemblerThumb: TokenizeLexerArtifacts");
            TokenerThumb tok = new TokenerThumb();
            asmTokenedData = tok.FileTokenize(asmLexedData, jsonObjIsEntryTypes);
        } catch (ExceptionTokenerNotFound e) {
            Logger.wrl("AssemblerThumb: TokenizeLexerArtifacts: Error: Could not tokenize lexed artifacts");
            e.printStackTrace();
            throw e;
        }
    }
    
    public int[] FindValidLineEntry(JsonObjIsValidLine validLine, Token token, int entry, int index) {
        int count = 0;
        for(JsonObjIsValidLineEntry validLineEntry : validLine.is_valid_line) {
            for(String validLineEntryType : validLineEntry.is_entry_types) {
                if(token.type_name.equals(validLineEntryType)) {
                    if(count >= entry) {
                        return new int[] { count, validLineEntry.index };
                    } else {
                        break;
                    }
                }
            }
            count++;
        }
        return null;
    }
    
    public boolean ValidateTokenizedLine(TokenLine tokenLine, JsonObjIsValidLines validLines, JsonObjIsValidLine validLineEmpty) {
        int tokenCount = tokenLine.payload.size();
        int[] res;
        int currentIndex;
        int currentEntry;
        int count = 0;
        
        if(tokenCount == 0) {
            tokenLine.validLineEntry = validLineEmpty;
            return true;
        }
        
        for(JsonObjIsValidLine validLine : validLines.is_valid_lines) {
            res = null;
            currentIndex = -1;
            currentEntry = -1;
            tokenCount = tokenLine.payload.size();
            
            for(Token token : tokenLine.payload) {            
                res = FindValidLineEntry(validLine, token, currentEntry, currentIndex);
                if(res == null) {
                    break;
                } else {
                    //Logger.wrl("CurrentEntry: " + currentEntry + ", CurrentIndex: " + currentIndex + ", Res[0]: " + res[0] + ", Res[1]: " + res[1]);
                }
                    
                if(currentIndex == -1) {
                    currentIndex = res[1];
                } 
                    
                if(currentEntry == -1) {
                    currentEntry = res[0];
                    tokenCount--;
                } else {
                    if(res[0] >= currentEntry) {
                        currentIndex = res[0];
                        tokenCount--;
                    } else {
                        break;
                    }
                }
            }
            count++;
            //Logger.wrl("TokenCount: " + tokenCount);
            
            if(tokenCount == 0) {
                tokenLine.validLineEntry = validLine;
                return true;
            }
        }
        return false;
    }
    
    public boolean ValidateTokenizedLines() {
        Logger.wrl("AssemblerThumb: ValidateTokenizedLines");        
        for(TokenLine tokenLine : asmTokenedData) {
            if(!ValidateTokenizedLine(tokenLine, jsonObjIsValidLines, jsonObjIsValidLines.is_valid_lines.get(JsonObjIsValidLines.ENTRY_LINE_EMPTY))) {
                Logger.wrl("AssemblerThumb: ValidateTokenizedLines: Error: Could not find a matching valid line for line number, " + tokenLine.lineNum + ", '" + tokenLine.source.source + "'");
                return false;
            }
        }
        return true;
    }
}
