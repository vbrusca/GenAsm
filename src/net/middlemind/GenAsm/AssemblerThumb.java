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
    public JsonObjIsOpCodes jsonObjIsOpCodes;    
    
    public String asmSourceFile;
    public List<String> asmSourceData;
    public List<ArtifactLine> asmLexedData;
    public List<TokenLine> asmTokenedData;

    public List<String> symbols;
    public List<Integer> symbols2LineNum;    
    public Map<String, Token> symbols2Token;
    public Map<String, TokenLine> symbols2Line;
    
    public List<String> symbolsLocal;
    public List<Integer> symbols2LineNumLocal;
    public Map<String, Token> symbols2TokenLocal;
    public Map<String, TokenLine> symbols2LineLocal;

    public Map<String, Hashtable<String, TokenLine>> symbols2ChildrenLocal;    

    public static String DEFAULT_PARENT_LABEL_NAME = "main";
    
    @Override
    public void RunAssembler(JsonObjIsSet jsonIsSet, String assemblySourceFile) {
        try {
            Logger.wrl("AssemblerThumb: RunAssembler: Start");
            jsonSource = new Hashtable<String, String>();
            isaLoader = new Hashtable<String, Loader>();        
            isaData = new Hashtable<String, JsonObj>();
            isaDataSet = jsonIsSet;
            asmSourceFile = assemblySourceFile;
            
            symbols = new ArrayList<String>();
            symbols2LineNum = new ArrayList<Integer>(); 
            symbols2Line = new Hashtable<String, TokenLine>();
            symbols2Token = new Hashtable<String, Token>();
            
            symbolsLocal = new ArrayList<String>();
            symbols2LineNumLocal = new ArrayList<Integer>();            
            symbols2LineLocal = new Hashtable<String, TokenLine>();
            symbols2TokenLocal = new Hashtable<String, Token>();
            
            symbols2ChildrenLocal = new Hashtable<String, Hashtable<String, TokenLine>>();
            
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

            //Second level token processing            
            Logger.wrl("");
            CollapseCommentTokens();
            ExpandRegisterRangeTokens();
            CollapseListAndGroupTokens();
            PopulateOpCodeAndArgData();
            WriteObject(asmTokenedData, "Assembly Tokenized Data", "/Users/victor/Documents/files/netbeans_workspace/GenAsm/cfg/THUMB/TESTS/output_tokened.json");

            Logger.wrl("");
            Logger.wrl("");
            //PrintObject(symbols2Line.keySet(), "Symbol To Line");
            for(int i = 0; i < symbols.size(); i++) {
                String source = symbols.get(i);
                int lineNum = symbols2LineNum.get(i);
                Token token = symbols2Token.get(source);
                TokenLine line = symbols2Line.get(source);
                
                Hashtable<String, TokenLine> childLines = symbols2ChildrenLocal.get(source);
                int childCount = 0;
                if(childLines != null) {
                    childCount = childLines.size();
                }                
                Logger.wrl("Label: " + source + " LineNum: " + lineNum + " TokenSource: " + token.source + " LineSource: " + line.source.source + " ChildLocalLabelCount: " + childCount);
            }
            
            Logger.wrl("");
            Logger.wrl("");
            //PrintObject(symbols2LineLocal.keySet(), "Symbol To Line Local");
            for(int i = 0; i < symbolsLocal.size(); i++) {
                String source = symbolsLocal.get(i);
                int lineNum = symbols2LineNumLocal.get(i);
                Token token = symbols2TokenLocal.get(source);
                TokenLine line = symbols2LineLocal.get(source);
                Logger.wrl("Label: " + source + " LineNum: " + lineNum + " ParentLabel: " + token.parentLabel + " ParentLineNum: " + token.parentLine.lineNum + " TokenSource: " + token.source + " LineSource: " + line.source.source);
            }
        } catch(Exception e) {
            Logger.wrl("AssemblerThumb: RunAssembler: Assembler encountered an exception, exiting...");
            e.printStackTrace();
        }
    }
    
    public List<JsonObjIsOpCode> FindOpCodeMatches(String opCodeName, int argLen) {
        List<JsonObjIsOpCode> ret = new ArrayList<>();
        for(JsonObjIsOpCode opCode : jsonObjIsOpCodes.is_op_codes) {
            if(opCode.op_code_name.equals(opCodeName) && opCode.arg_len == argLen) {
                ret.add(opCode);
            }
        }
        return ret;
    }
    
    public int CountArgTokens(List<Token> payload, int argCount) {
        for(Token token : payload) {
            if(token.type != null && ((JsonObjIsEntryType)token.type).category.equals(JsonObjIsEntryTypes.ENTRY_TYPE_NAME_CAT_ARG)) {
                argCount++;
            }
            
            if(token.payload != null && token.payload.size() > 0) {
                if(token.type_name.equals(JsonObjIsEntryTypes.ENTRY_TYPE_NAME_START_LIST)) {
                    argCount++;
                } else {
                    argCount = CountArgTokens(token.payload, argCount);
                }
            }
        }
        return argCount;
    }
    
    public void PopulateOpCodeAndArgData() throws ExceptionOpCodeNotFound {
        Logger.wrl("AssemblerThumb: PopulateOpCodeAndArgData");        
        boolean opCodeFound;
        String opCodeName;
        int opCodeIdx;
        int labelArgs;
        String lastLabel = null;
        TokenLine lastLabelLine = null;
        TokenLine firstLine = null;
        
        for(TokenLine line : asmTokenedData) {
            if(firstLine == null) {
                firstLine = line;
                symbols2Line.put(DEFAULT_PARENT_LABEL_NAME, firstLine);
                lastLabel = DEFAULT_PARENT_LABEL_NAME;
                lastLabelLine = firstLine;
            }
            
            opCodeFound = false;
            opCodeName = null;
            opCodeIdx = -1;
            labelArgs = 0;
            for(Token token : line.payload) {
                if(token.type_name.equals(JsonObjIsEntryTypes.ENTRY_TYPE_NAME_OPCODE)) {
                    opCodeFound = true;
                    opCodeName = token.source;
                    opCodeIdx = token.index;
                    
                } else if(token.type_name.equals(JsonObjIsEntryTypes.ENTRY_TYPE_NAME_LABEL)) {
                    if(opCodeIdx != -1 && token.index > opCodeIdx) {
                        labelArgs++;
                    }
                    
                    if(token.index == 0) {
                        lastLabel = token.source;
                        lastLabelLine = line;
                        if(symbols2Line.containsKey(token.source)) {
                            TokenLine tmp = symbols2Line.get(token.source);
                            symbols2Line.remove(token.source);
                            Logger.wrl("AssemblerThumb: PopulateOpCodeAndArgData: Warning symbol '" + token.source + "' redefined on line " + token.lineNum + " originally defned on line " + tmp.lineNum);
                        } else {
                            Logger.wrl("AssemblerThumb: PopulateOpCodeAndArgData: Storing symbol with label '" + token.source + "' for line number " + line.lineNum);
                        }
                        symbols.add(token.source);
                        symbols2LineNum.add(token.lineNum);
                        symbols2Token.put(token.source, token);
                        symbols2Line.put(token.source, line);
                    }
                    
                } else if(token.type_name.equals(JsonObjIsEntryTypes.ENTRY_TYPE_NAME_LABEL_NUMERIC_LOCAL_REF)) {                    
                    if(Utils.IsStringEmpty(lastLabel)) {
                        Logger.wrl("AssemblerThumb: PopulateOpCodeAndArgData: Warning local symbol '" + token.source + "' on line " + token.lineNum + ", could not find parent label to associate with local label.");                        
                    } else {
                        if(symbols2ChildrenLocal.containsKey(lastLabel)) {
                            Hashtable<String, TokenLine> parent = (Hashtable)symbols2ChildrenLocal.get(lastLabel);
                            if(parent.containsKey(token.source)) {
                                TokenLine tmp = parent.get(token.source);
                                parent.remove(token.source);
                                Logger.wrl("AssemblerThumb: PopulateOpCodeAndArgData: Warning local symbol '" + token.source + "' redefined on line " + token.lineNum + " originally defned on line " + tmp.lineNum);                            
                            } else {
                                Logger.wrl("AssemblerThumb: PopulateOpCodeAndArgData: Storing local symbol with label '" + token.source + "' for line number " + line.lineNum + " and parent label '" + lastLabel + "'");
                            }
                            token.parentLabel = lastLabel;
                            token.parentLine = lastLabelLine;
                            parent.put(token.source, line);
                            
                        } else {
                            token.parentLabel = lastLabel;
                            token.parentLine = lastLabelLine;                            
                            Hashtable<String, TokenLine> tmpHash = new Hashtable<>();
                            tmpHash.put(token.source, line);
                            symbols2ChildrenLocal.put(lastLabel, tmpHash);
                            Logger.wrl("AssemblerThumb: PopulateOpCodeAndArgData: Creating local symbol with label '" + token.source + "' for line number " + line.lineNum + " and parent label '" + lastLabel + "'");
                        
                        }
                        
                        symbolsLocal.add(token.source);
                        symbols2LineNumLocal.add(token.lineNum);
                        symbols2TokenLocal.put(token.source, token);
                        symbols2LineLocal.put(token.source, line);
                    }
                }
            }
            
            if(opCodeFound) {
                line.payloadOpCode = opCodeName;
                line.payloadLenArg = CountArgTokens(line.payload, 0) + labelArgs;
                line.opCodeMatches = FindOpCodeMatches(line.payloadOpCode, line.payloadLenArg);
                
                if(line.opCodeMatches == null || line.opCodeMatches.size() == 0) {
                    throw new ExceptionOpCodeNotFound("Could not find a matching opCode entry for name '" + line.payloadOpCode + "' with argument count " + line.payloadLenArg + " at line " + line.lineNum + " with source text '" + line.source.source + "'");
                }
            }
        }
    }
    
    public void CollapseListAndGroupTokens() throws ExceptionMissingClosingBracket, ExceptionListAndGroup {
        Logger.wrl("AssemblerThumb: CollapseListAndGroupTokens");        
        for(TokenLine line : asmTokenedData) {
            Token rootStartList = null;
            int rootStartIdxList = -1;
            Token rootStartGroup = null;
            int rootStartIdxGroup = -1;

            Token rootStopList = null;
            int rootStopIdxList = -1;
            Token rootStopGroup = null;
            int rootStopIdxGroup = -1;
            
            List<Token> clearTokensList = new ArrayList<>();
            List<Token> clearTokensGroup = new ArrayList<>();
            int copyStart = -1;
            int copyEnd = -1;
            int copyLen = -1;
            
            for(Token token : line.payload) {
                if(token.type_name.equals(JsonObjIsEntryTypes.ENTRY_TYPE_NAME_START_LIST)) {
                    rootStartList = token;
                    rootStartIdxList = rootStartList.index;
                    
                } else if(token.type_name.equals(JsonObjIsEntryTypes.ENTRY_TYPE_NAME_START_GROUP)) {                    
                    rootStartGroup = token;
                    rootStartIdxGroup = rootStartGroup.index;
                    
                } else if(token.type_name.equals(JsonObjIsEntryTypes.ENTRY_TYPE_NAME_STOP_LIST)) {
                    rootStopList = token;
                    rootStopIdxList = rootStopList.index;
                    
                } else if(token.type_name.equals(JsonObjIsEntryTypes.ENTRY_TYPE_NAME_STOP_GROUP)) {                    
                    rootStopGroup = token;
                    rootStopIdxGroup = rootStopGroup.index;                    
                    
                }
            }
            
            if((rootStartIdxList != -1 && rootStopIdxList == -1) || (rootStartIdxList == -1 && rootStopIdxList != -1)) {
                throw new ExceptionMissingClosingBracket("Could not find closing bracket for list.");
                
            } else if((rootStartIdxGroup != -1 && rootStopIdxGroup == -1) || (rootStartIdxGroup == -1 && rootStopIdxGroup != -1)) {
                throw new ExceptionMissingClosingBracket("Could not find closing bracket for group.");
                
            } else if(rootStartIdxList != -1 && rootStopIdxList != -1 && rootStartIdxGroup != -1 && rootStopIdxGroup != -1) {
                throw new ExceptionListAndGroup("Found list and group entries when only one is allowed.");

            } else if(rootStartIdxList != -1 && rootStopIdxList != -1) {
                copyStart = (rootStartIdxList + 1);
                copyEnd = rootStopIdxList;
                copyLen = (copyEnd - copyStart);
                for(int i = copyStart; i <= (copyStart + copyLen); i++) {
                    clearTokensList.add(line.payload.get(i));
                }
                line.payload.removeAll(clearTokensList);                
                line.payloadLen = line.payload.size();
                rootStartList.payload.addAll(clearTokensList);
                rootStartList.payloadLen = clearTokensList.size();
                
                int count = 0;
                for(Token token : rootStartList.payload) {
                    token.index = count;
                    count++;
                }
                
            } else if(rootStartIdxGroup != -1 && rootStopIdxGroup != -1) {
                copyStart = (rootStartIdxGroup + 1);                
                copyEnd = rootStopIdxGroup;
                copyLen = (copyEnd - copyStart);
                for(int i = copyStart; i <= (copyStart + copyLen); i++) {
                    clearTokensGroup.add(line.payload.get(i));
                }
                line.payload.removeAll(clearTokensGroup);                
                line.payloadLen = line.payload.size();
                rootStartGroup.payload.addAll(clearTokensGroup);
                rootStartGroup.payloadLen = clearTokensGroup.size();
                
                int count = 0;
                for(Token token : rootStartGroup.payload) {
                    token.index = count;
                    count++;
                }                
                
            }
        }
    }    
    
    public JsonObjIsEntryType FindEntryType(String entryName) throws ExceptionEntryNotFound {        
        for(JsonObjIsEntryType entry : jsonObjIsEntryTypes.is_entry_types) {
            if(entry.type_name.equals(entryName)) {
                return entry;
            }
        }
        
        throw new ExceptionEntryNotFound("Could not find entry by name, '" + entryName + "', in loaded entry types.");
    }
    
    public String CleanRegisterRangeString(String range, String rangeDelim) {
        String ret = "";
        for(char c : range.toCharArray()) {
            if((c + "").equals(rangeDelim) || Character.isDigit(c)) {
                ret += c;
            }
        }
        return ret;
    }
    
    public void CleanTokenSource(Token token) {
        if(token != null && !Utils.IsStringEmpty(token.source)) {
            token.source = token.source.replace(JsonObjIsOpCode.DEFAULT_ARG_SEPARATOR, "");
            token.source = token.source.replace(System.lineSeparator(), "");
        }
    }
    
    public void ExpandRegisterRangeTokens() throws Exception {
        try {
            Logger.wrl("AssemblerThumb: ExpandRegisterRangeToken");
            for(TokenLine line : asmTokenedData) {
                int rangeRootIdxLow = 0;
                int rangeRootIdxHi = 0;
                Token rangeRootLow = null;                
                Token rangeRootHi = null;
                String rangeStr = "";
                int[] range = null;
                int count = 0;
                Token newToken = null;
                int i = 0;
                
                List<Token> rangeAddTokensLow = new ArrayList<>();
                List<Token> rangeAddTokensHi = new ArrayList<>();                
                JsonObjIsEntryType entryTypeRegLow = FindEntryType(JsonObjIsEntryTypes.ENTRY_TYPE_NAME_REGISTER_LOW);
                JsonObjIsEntryType entryTypeRegHi = FindEntryType(JsonObjIsEntryTypes.ENTRY_TYPE_NAME_REGISTER_HI);                

                for(Token token : line.payload) {
                    CleanTokenSource(token);
                    if(token.type_name.equals(JsonObjIsEntryTypes.ENTRY_TYPE_NAME_REGISTER_RANGE_LOW)) {
                        rangeRootLow = token;
                        rangeRootIdxLow = rangeRootLow.index;
                        rangeStr = rangeRootLow.source;
                        rangeStr = CleanRegisterRangeString(rangeStr, JsonObjIsRegisters.REGISTER_CHAR_RANGE);
                        range = Utils.GetIntsFromRange(rangeStr, JsonObjIsRegisters.REGISTER_CHAR_RANGE);
                        count = 0;
                        for(i = range[0]; i < range[1]; i++) {
                            newToken = new Token();
                            newToken.artifact = rangeRootLow.artifact;
                            newToken.index = rangeRootLow.index + count;
                            newToken.lineNum = rangeRootLow.lineNum;
                            newToken.payload = new ArrayList<>();
                            newToken.payloadLen = 0;
                            newToken.source = JsonObjIsRegisters.REGISTER_CHAR_START + i;
                            newToken.type = entryTypeRegLow;
                            newToken.type_name = entryTypeRegLow.type_name;
                            rangeAddTokensLow.add(newToken);
                            count++;
                        }

                    } else if(token.type_name.equals(JsonObjIsEntryTypes.ENTRY_TYPE_NAME_REGISTER_RANGE_HI)) {
                        rangeRootHi = token;
                        rangeRootIdxHi = rangeRootHi.index;
                        rangeStr = rangeRootHi.source;
                        rangeStr = CleanRegisterRangeString(rangeStr, JsonObjIsRegisters.REGISTER_CHAR_RANGE);
                        range = Utils.GetIntsFromRange(rangeStr, JsonObjIsRegisters.REGISTER_CHAR_RANGE);
                        count = 0;
                        for(i = range[0]; i < range[1]; i++) {
                            newToken = new Token();
                            newToken.artifact = rangeRootHi.artifact;
                            newToken.index = rangeRootHi.index + count;
                            newToken.lineNum = rangeRootHi.lineNum;
                            newToken.payload = new ArrayList<>();
                            newToken.payloadLen = 0;
                            newToken.source = JsonObjIsRegisters.REGISTER_CHAR_START + i;
                            newToken.type = entryTypeRegHi;
                            newToken.type_name = entryTypeRegHi.type_name;
                            rangeAddTokensHi.add(newToken);
                            count++;
                        }
                        
                    }
                }
                
                if(rangeRootHi != null && rangeAddTokensHi != null && rangeAddTokensHi.size() > 0) {
                    line.payload.addAll(rangeRootIdxHi + 1, rangeAddTokensHi);
                    line.payload.remove(rangeRootHi);
                    line.payloadLen = line.payload.size();

                    count = 0;
                    for(Token token : line.payload) {
                        token.index = count;
                        count++;
                    }
                }
                
                if(rangeRootLow != null && rangeAddTokensLow != null && rangeAddTokensLow.size() > 0) {
                    line.payload.addAll(rangeRootIdxLow + 1, rangeAddTokensLow);
                    line.payload.remove(rangeRootLow);
                    line.payloadLen = line.payload.size();   
                    
                    count = 0;
                    for(Token token : line.payload) {
                        token.index = count;
                        count++;
                    }                    
                }                
                
            }
        } catch (ExceptionEntryNotFound | ExceptionMalformedRange e) {
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
            Logger.wrl("AssemblerThumb: WriteObject: Could not write the target output file, '" + fileName + "'");
            throw e;
        }
    }
    
    public void PrintObject(Object obj, String name) {
        Logger.wrl("AssemblerThumb: PrintObject: Name: '" + name + "'");
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
                Logger.wrl("AssemblerThumb: RunAssembler: Loader created '" + entry.loader_class + "'");
                
                json = FileLoader.LoadStr(entry.path);
                jsonSource.put(entry.path, json);
                Logger.wrl("AssemblerThumb: RunAssembler: Json loaded '" + entry.path + "'");
                                
                jsonObj = ldr.ParseJson(json, entry.target_class, entry.path);
                jsonName = jsonObj.GetName();
                isaData.put(jsonName, jsonObj);
                Logger.wrl("AssemblerThumb: RunAssembler: Json parsed as '" + entry.target_class + "'");
                Logger.wrl("AssemblerThumb: RunAssembler: Loading isaData with entry '" + jsonName + "'");
                
                if(jsonObj.GetLoader().equals("net.middlemind.GenAsm.LoaderIsEntryTypes")) {
                    jsonObjIsEntryTypes = (JsonObjIsEntryTypes)jsonObj;
                    Logger.wrl("AssemblerThumb: RunAssembler: Found JsonObjIsEntryTypes object, storing it...");
                
                } else if(jsonObj.GetLoader().equals("net.middlemind.GenAsm.LoaderIsValidLines")) {
                    jsonObjIsValidLines = (JsonObjIsValidLines)jsonObj;
                    Logger.wrl("AssemblerThumb: RunAssembler: Found JsonObjIsValidLines object, storing it...");
                
                } else if(jsonObj.GetLoader().equals("net.middlemind.GenAsm.LoaderIsOpCodes")) {
                    jsonObjIsOpCodes = (JsonObjIsOpCodes)jsonObj;
                    Logger.wrl("AssemblerThumb: RunAssembler: Found JsonObjIsOpCodes object, storing it...");                    
                
                }
            }
            
            if(jsonObjIsEntryTypes == null) {
                throw new ExceptionEntryNotFound("Could not find required JsonObjIsEntryTypes instance.");
            
            } else if(jsonObjIsValidLines == null) {
                throw new ExceptionEntryNotFound("Could not find required JsonObjIsValidLines instance.");
            
            } else if(jsonObjIsOpCodes == null) {
                throw new ExceptionEntryNotFound("Could not find required JsonObjIsOpCodes instance.");                
            
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
                Logger.wrl("AssemblerThumb: RunAssembler: Error: Could not link json object, '" + s + "'");
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
            Logger.wrl("AssemblerThumb: LoadAndLexAssemblySource: Error: Could not load and lex assembly source file, '" + asmSourceFile + "'");
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
                Logger.wrl("AssemblerThumb: ValidateTokenizedLines: Error: Could not find a matching valid line for line number, " + tokenLine.lineNum + " with source text, '" + tokenLine.source.source + "'");
                return false;
            }
        }
        return true;
    }
}
