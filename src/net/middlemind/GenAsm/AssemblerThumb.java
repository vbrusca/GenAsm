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
@SuppressWarnings({"null", "CallToPrintStackTrace", "UnusedAssignment", "Convert2Diamond", "ConvertToStringSwitch"})
public class AssemblerThumb implements Assembler {

    public JsonObjIsSet isaDataSet;
    public Map<String, JsonObj> isaData;
    public Map<String, Loader> isaLoader;
    public Map<String, String> jsonSource;
    public JsonObjIsEntryTypes jsonObjIsEntryTypes;
    public JsonObjIsValidLines jsonObjIsValidLines;
    public JsonObjIsOpCodes jsonObjIsOpCodes;   
    public JsonObjIsDirectives jsonObjIsDirectives;
    
    public String asmSourceFile;
    public List<String> asmSourceData;
    public List<ArtifactLine> asmLexedData;
    public List<TokenLine> asmTokenedData;
    public Symbols symbols;
    
    public List<String> requiredDirectives;
    
    @Override
    public void RunAssembler(JsonObjIsSet jsonIsSet, String assemblySourceFile) {
        try {
            Logger.wrl("AssemblerThumb: RunAssembler: Start");
            jsonSource = new Hashtable<String, String>();
            isaLoader = new Hashtable<String, Loader>();        
            isaData = new Hashtable<String, JsonObj>();
            isaDataSet = jsonIsSet;
            asmSourceFile = assemblySourceFile;
            symbols = new Symbols();
            
            requiredDirectives = new ArrayList<String>();
            requiredDirectives.add("@AREA");
            requiredDirectives.add("@TTL");            
            requiredDirectives.add("@ENTRY");
            requiredDirectives.add("@END");            
            
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

            Logger.wrl("");
            WriteObject(asmTokenedData, "Assembly Tokenized Data", "/Users/victor/Documents/files/netbeans_workspace/GenAsm/cfg/THUMB/TESTS/output_tokened_phase0.json");            
            
            //Validate token lines
            Logger.wrl("");
            ValidateTokenizedLines();

            //Second level token processing            
            Logger.wrl("");
            WriteObject(asmTokenedData, "Assembly Tokenized Data", "/Users/victor/Documents/files/netbeans_workspace/GenAsm/cfg/THUMB/TESTS/output_tokened_phase1.json");            

            CollapseCommentTokens();
            ExpandRegisterRangeTokens();
            CollapseListAndGroupTokens();
            PopulateOpCodeAndArgData();
            PopulateDirectiveAndArgData();
            WriteObject(asmTokenedData, "Assembly Tokenized Data", "/Users/victor/Documents/files/netbeans_workspace/GenAsm/cfg/THUMB/TESTS/output_tokened_phase2.json");
            WriteObject(symbols, "Symbol Data", "/Users/victor/Documents/files/netbeans_workspace/GenAsm/cfg/THUMB/TESTS/output_symbols.json");            

            ValidateOpCodeLines();        
            ValidateDirectiveLines();
            WriteObject(asmTokenedData, "Assembly Tokenized Data", "/Users/victor/Documents/files/netbeans_workspace/GenAsm/cfg/THUMB/TESTS/output_tokened_phase3.json");            
        } catch(Exception e) {
            Logger.wrl("AssemblerThumb: RunAssembler: Assembler encountered an exception, exiting...");
            e.printStackTrace();
        }
    }
    
    public void PopulateDirectiveAndArgData() throws ExceptionNoOpCodeFound, ExceptionNoParentSymbolFound {
        Logger.wrl("AssemblerThumb: PopulateDirectiveAndArgData");        
        boolean directiveFound = false;
        String directiveName = null;
        int directiveIdx = -1;
        
        for(TokenLine line : asmTokenedData) {            
            directiveFound = false;
            directiveName = null;
            directiveIdx = -1;
            for(Token token : line.payload) {
                if(token.type_name.equals(JsonObjIsEntryTypes.ENTRY_TYPE_NAME_DIRECTIVE)) {
                    directiveFound = true;
                    directiveName = token.source;
                    directiveIdx = token.index;
                    
                }
            }
            
            if(directiveFound) {
                line.payloadDirective = directiveName;
                line.payloadLenArg = CountArgTokens(line.payload, 0, JsonObjIsEntryTypes.ENTRY_TYPE_NAME_DIRECTIVE_CAT_ARG);
                line.matchesDirective = FindDirectiveMatches(line.payloadDirective, line.payloadLenArg);
                
                if(line.matchesDirective == null || line.matchesDirective.isEmpty()) {
                    throw new ExceptionNoOpCodeFound("Could not find a matching directive entry for name '" + line.payloadDirective + "' with argument count " + line.payloadLenArg + " at line " + line.lineNum + " with source text '" + line.source.source + "'");
                }
            }
        }
    }    
    
    public void ValidateDirectiveLines() throws ExceptionNoDirectiveFound {
        Logger.wrl("AssemblerThumb: ValidateDirectiveLines"); 
        boolean directiveFound = false;
        String directiveName = null;
        Token directiveToken = null;
        int directiveIdx = -1;
        List<Token> args = null;
        JsonObjIsDirective directive = null; 
        
        for(TokenLine line : asmTokenedData) {
            if(line.matchesDirective != null && line.matchesDirective.size() > 1) {
                directiveFound = false;
                directiveName = null;
                directiveToken = null;
                directiveIdx = -1;
                args = null;
                for(Token token : line.payload) {
                    if(!directiveFound) {
                        if(token.type_name.equals(JsonObjIsEntryTypes.ENTRY_TYPE_NAME_DIRECTIVE)) {
                            directiveFound = true;
                            directiveName = token.source;
                            directiveToken = token;
                            directiveIdx = token.index;
                            args = new ArrayList<>();
                        }
                    } else {
                        //in args
                        args.add(token);
                    }
                }
                
                if(directiveFound && args != null && args.size() > 0) {
                    directive = FindDirectiveArgMatches(line.matchesDirective, args, directiveToken);
                    line.matchesDirective.clear();
                    line.matchesDirective.add(directive);
                }
            }
            
            if(line.matchesDirective != null && line.matchesDirective.size() > 1) {
                throw new ExceptionNoDirectiveFound("Could not find unique matching directive entry for directive '" + directiveName + "' and line number " + line.lineNum + " with source '" + line.source.source + "'");
            }
        }        
    }
    
    public JsonObjIsDirective FindDirectiveArgMatches(List<JsonObjIsDirective> directiveMatches, List<Token> args, Token directiveToken) throws ExceptionNoDirectiveFound {
        JsonObjIsDirectiveArg directiveArg = null;
        Token argToken = null;
        boolean argFound = false;
        int directiveArgIdx = -1;

        
        for(JsonObjIsDirective directive : directiveMatches) {            
            directiveArg = null;
            argToken = null;
            argFound = false;
            
            for(int i = 0; i < directive.args.size(); i++) {
                directiveArg = directive.args.get(i);
                directiveArgIdx = i;
                if(i < args.size()) {
                    argToken = args.get(i);
                } else {
                    break;
                }
                
                if(directiveArg != null && argToken != null) {
                    if(directiveArg.is_entry_types.contains(argToken.type_name)) {
                        argFound = true;
                    } else {
                        break;
                    }
                }
                
                if(argFound) {
                    break;
                }
            }
            
            if(argFound) {
                return directive;
            }
        }
        
        throw new ExceptionNoDirectiveFound("Could not find a directive that has matching arguments for line number " + directiveToken.lineNum + " with directive '" + directiveToken.source + "' at directive argument index " + directiveArgIdx);
    }    
    
    public JsonObjIsOpCode FindOpCodeArgMatches(List<JsonObjIsOpCode> opCodeMatches, List<Token> args, Token opCodeToken) throws ExceptionNoOpCodeFound {
        JsonObjIsOpCodeArg opCodeArg = null;
        JsonObjIsOpCodeArg opCodeArgSub = null;
        Token argToken = null;
        Token argTokenSub = null;
        boolean argFound = false;
        boolean argFoundSub = false;
        boolean hasArgsSub = false;
        int opCodeArgIdx = -1;
        int opCodeArgIdxSub = -1;
        
        for(JsonObjIsOpCode opCode : opCodeMatches) {            
            opCodeArg = null;
            argToken = null;
            argFound = false;
            argFoundSub = false;
            hasArgsSub = false;
            
            for(int i = 0; i < opCode.args.size(); i++) {
                opCodeArg = opCode.args.get(i);
                opCodeArgIdx = i;
                if(i < args.size()) {
                    argToken = args.get(i);
                } else {
                    break;
                }
                
                if(opCodeArg != null && argToken != null) {
                    if(argToken.type_name.contains("Label") || opCodeArg.is_entry_types.contains(argToken.type_name)) {
                        argFound = true;
                    } else {
                        break;
                    }
                    
                    if(opCodeArg.sub_args != null && opCodeArg.sub_args.size() > 0) {
                        opCodeArgSub = null;
                        argTokenSub = null;
                        argFoundSub = false;
                        hasArgsSub = true;
                        
                        for(int j = 0; j < opCodeArg.sub_args.size(); j++) {
                            opCodeArgSub = opCodeArg.sub_args.get(j);
                            opCodeArgIdxSub = j;
                            if(argToken.payload != null && j < argToken.payload.size()) {
                                argTokenSub = argToken.payload.get(j);
                            } else {
                                break;
                            }
                            
                            if(opCodeArgSub != null && argTokenSub != null) {
                                if(argTokenSub.type_name.contains("Label") || opCodeArg.is_entry_types.contains(argToken.type_name)) {
                                    argFoundSub = true;
                                } else {
                                    break;
                                }                     
                            }
                        }
                    }                    
                }
                
                if(argFound && hasArgsSub && !argFoundSub) {
                    break;
                }
            }
            
            if(argFound && !hasArgsSub && !argFoundSub) {
                return opCode;                
            } else if(argFound && hasArgsSub && argFoundSub) {
                return opCode;
            }
        }
        
        throw new ExceptionNoOpCodeFound("Could not find an opCode that has matching arguments for line number " + opCodeToken.lineNum + " with opCode '" + opCodeToken.source + "' at opCode argument index " + opCodeArgIdx + " with sub argument index " + opCodeArgIdxSub);
    }
    
    public void ValidateOpCodeLines() throws ExceptionNoOpCodeFound {
        Logger.wrl("AssemblerThumb: ValidateOpCodeLines"); 
        boolean opCodeFound = false;
        String opCodeName = null;
        Token opCodeToken = null;
        int opCodeIdx = -1;
        List<Token> args = null;
        JsonObjIsOpCode opCode = null; 
        
        for(TokenLine line : asmTokenedData) {
            if(line.matchesOpCode != null && line.matchesOpCode.size() > 1) {
                opCodeFound = false;
                opCodeName = null;
                opCodeToken = null;
                opCodeIdx = -1;
                args = null;
                for(Token token : line.payload) {
                    if(!opCodeFound) {
                        if(token.type_name.equals(JsonObjIsEntryTypes.ENTRY_TYPE_NAME_OPCODE)) {
                            opCodeFound = true;
                            opCodeName = token.source;
                            opCodeToken = token;
                            opCodeIdx = token.index;
                            args = new ArrayList<>();
                        }
                    } else {
                        //in args
                        args.add(token);
                    }
                }
                
                if(opCodeFound && args != null && args.size() > 0) {
                    opCode = FindOpCodeArgMatches(line.matchesOpCode, args, opCodeToken);
                    line.matchesOpCode.clear();
                    line.matchesOpCode.add(opCode);
                }
            }
            
            if(line.matchesOpCode != null && line.matchesOpCode.size() > 1) {
                throw new ExceptionNoOpCodeFound("Could not find unique matching opCode entry for opCode '" + opCodeName + "' and line number " + line.lineNum + " with source '" + line.source.source + "'");
            }
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
    
    public List<JsonObjIsDirective> FindDirectiveMatches(String directiveName, int argLen) {
        List<JsonObjIsDirective> ret = new ArrayList<>();
        for(JsonObjIsDirective directive : jsonObjIsDirectives.is_directives) {
            if(directive.directive_name.equals(directiveName) && directive.arg_len == argLen) {
                ret.add(directive);
            }
        }
        return ret;
    }    
    
    public int CountArgTokens(List<Token> payload, int argCount) {
        return CountArgTokens(payload, argCount, JsonObjIsEntryTypes.ENTRY_TYPE_NAME_OPCODE_CAT_ARG);
    }
    
    public int CountArgTokens(List<Token> payload, int argCount, String argCategory) {
        for(Token token : payload) {
            if(token.type != null && ((JsonObjIsEntryType)token.type).category.equals(argCategory)) {
                argCount++;
            }
            
            if(token.payload != null && token.payload.size() > 0) {
                if(token.type_name.equals(JsonObjIsEntryTypes.ENTRY_TYPE_NAME_START_LIST)) {
                    argCount++;
                } else {
                    argCount = CountArgTokens(token.payload, argCount, argCategory);
                }
            }
        }
        return argCount;
    }
    
    public void PopulateOpCodeAndArgData() throws ExceptionNoOpCodeFound, ExceptionNoParentSymbolFound {
        Logger.wrl("AssemblerThumb: PopulateOpCodeAndArgData");        
        boolean opCodeFound = false;
        String opCodeName = null;
        int opCodeIdx = -1;
        int labelArgs = -1;
        String lastLabel = null;
        TokenLine lastLabelLine = null;
        Symbol symbol = null;
        
        for(TokenLine line : asmTokenedData) {            
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
                        if(symbols.symbols.containsKey(token.source)) {
                            symbol = symbols.symbols.get(token.source);
                            symbols.symbols.remove(token.source);
                            Logger.wrl("AssemblerThumb: PopulateOpCodeAndArgData: Warning symbol '" + token.source + "' redefined on line " + token.lineNum + " originally defned on line " + symbol.lineNum);
                        } else {
                            Logger.wrl("AssemblerThumb: PopulateOpCodeAndArgData: Storing symbol with label '" + token.source + "' for line number " + line.lineNum);
                        }
                        symbol = new Symbol();
                        symbol.line = line;
                        symbol.lineNum = line.lineNum;
                        symbol.name = token.source;
                        symbol.token = token;
                        symbols.symbols.put(token.source, symbol);
                    }
                    
                } else if(token.type_name.equals(JsonObjIsEntryTypes.ENTRY_TYPE_NAME_LABEL_NUMERIC_LOCAL_REF)) {                    
                    if(Utils.IsStringEmpty(lastLabel)) {
                        Logger.wrl("AssemblerThumb: PopulateOpCodeAndArgData: Warning local symbol '" + token.source + "' on line " + token.lineNum + ", could not find parent label to associate with local label.");                        
                    } else {
                        if(symbols.symbols.containsKey(lastLabel)) {
                            symbol = symbols.symbols.get(lastLabel);
                            if(symbol.symbols.containsKey(token.source)) {
                                symbol.symbols.remove(token.source);
                                Logger.wrl("AssemblerThumb: PopulateOpCodeAndArgData: Warning local symbol '" + token.source + "' redefined on line " + token.lineNum + " originally defned on line " + symbol.lineNum);                            
                            } else {
                                Logger.wrl("AssemblerThumb: PopulateOpCodeAndArgData: Storing local symbol with label '" + token.source + "' for line number " + line.lineNum + " and parent label '" + lastLabel + "'");
                            }
                            token.parentLabel = lastLabel;
                            token.parentLine = lastLabelLine;
                            
                            Symbol lsymbol = new Symbol();
                            lsymbol.line = line;
                            lsymbol.lineNum = line.lineNum;
                            lsymbol.name = token.source;
                            lsymbol.token = token;                            
                            symbol.symbols.put(token.source, lsymbol);
                            
                        } else {
                            throw new ExceptionNoParentSymbolFound("Could not find a parent symbol for label '" + token.source + "' at line " + line.lineNum + " with source text '" + line.source.source + "'");
                        }
                    }
                }
            }
            
            if(opCodeFound) {
                line.payloadOpCode = opCodeName;
                line.payloadLenArg = CountArgTokens(line.payload, 0) + labelArgs;
                line.matchesOpCode = FindOpCodeMatches(line.payloadOpCode, line.payloadLenArg);
                
                if(line.matchesOpCode == null || line.matchesOpCode.isEmpty()) {
                    throw new ExceptionNoOpCodeFound("Could not find a matching opCode entry for name '" + line.payloadOpCode + "' with argument count " + line.payloadLenArg + " at line " + line.lineNum + " with source text '" + line.source.source + "'");
                }
            }
        }
    }
    
    public void CollapseListAndGroupTokens() throws ExceptionNoClosingBracketFound, ExceptionListAndGroup {
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
                throw new ExceptionNoClosingBracketFound("Could not find closing bracket for list.");
                
            } else if((rootStartIdxGroup != -1 && rootStopIdxGroup == -1) || (rootStartIdxGroup == -1 && rootStopIdxGroup != -1)) {
                throw new ExceptionNoClosingBracketFound("Could not find closing bracket for group.");
                
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
    
    public JsonObjIsEntryType FindEntryType(String entryName) throws ExceptionNoEntryFound {        
        for(JsonObjIsEntryType entry : jsonObjIsEntryTypes.is_entry_types) {
            if(entry.type_name.equals(entryName)) {
                return entry;
            }
        }
        
        throw new ExceptionNoEntryFound("Could not find entry by name, '" + entryName + "', in loaded entry types.");
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
        } catch (ExceptionNoEntryFound | ExceptionMalformedRange e) {
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
            Logger.wrl("AssemblerThumb: WriteObject: Could not write the target output file, '" + fileName + "'");
            e.printStackTrace();
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
                String json = null;
                String jsonName = null;
                JsonObj jsonObj = null;
                
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
                
                } else if(jsonObj.GetLoader().equals("net.middlemind.GenAsm.LoaderIsDirectives")) {
                    jsonObjIsDirectives = (JsonObjIsDirectives)jsonObj;
                    Logger.wrl("AssemblerThumb: RunAssembler: Found JsonObjIsDirectives object, storing it...");                    
                }
            }
            
            if(jsonObjIsEntryTypes == null) {
                throw new ExceptionNoEntryFound("Could not find required JsonObjIsEntryTypes instance.");
            
            } else if(jsonObjIsValidLines == null) {
                throw new ExceptionNoEntryFound("Could not find required JsonObjIsValidLines instance.");
            
            } else if(jsonObjIsOpCodes == null) {
                throw new ExceptionNoEntryFound("Could not find required JsonObjIsOpCodes instance.");                
            
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
        } catch (ExceptionNoTokenerFound e) {
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
        int[] res = null;
        int currentIndex = -1;
        int currentEntry = -1;
        int entries = -1;
        
        if(tokenCount == 0) {
            tokenLine.validLineEntry = validLineEmpty;
            return true;
        }
        
        for(JsonObjIsValidLine validLine : validLines.is_valid_lines) {
            //Logger.wrl("");
            //Logger.wrl("");
            //Logger.wrl("Index: " + validLine.index);
            res = null;
            currentIndex = -1;
            currentEntry = -1;
            tokenCount = tokenLine.payload.size();
            entries = -1;
            
            for(Token token : tokenLine.payload) {
                //Logger.wrl("TokenType: " + token.type_name);
                res = FindValidLineEntry(validLine, token, currentEntry, 0);
                if(res == null) {
                    //Logger.wrl("null");
                    break;
                } else {
                    //Logger.wrl("TokenCount: " + tokenCount + ", Entries: " + entries + ", CurrentEntry: " + currentEntry + ", CurrentIndex: " + currentIndex + ", Res[0]: " + res[0] + ", Res[1]: " + res[1]);
                }
                   
                /*
                if(currentIndex == -1) {
                    entries = 1;
                    currentIndex = res[1];
                    tokenCount--;
                } else {
                    if(res[0] > currentIndex) {
                        entries++;
                    }                    
                    
                    if(res[0] >= currentIndex) {
                        currentIndex = res[0];
                        tokenCount--;
                    } else {
                        break;
                    }                    
                }
                */
                
                if(currentEntry == -1) {
                    entries = 1;
                    currentEntry = res[0];
                    tokenCount--;
                } else {
                    if(res[0] > currentEntry) {
                        entries++;
                    }                    
                    
                    if(res[0] >= currentEntry) {
                        currentEntry = res[0];
                        tokenCount--;
                    } else {
                        break;
                    }
                }
            }
            //Logger.wrl("TokenCount: " + tokenCount + " Entries: " + entries + " ValidLineEntries: " + validLine.is_valid_line.size());
            
            if(tokenCount == 0 && entries == validLine.is_valid_line.size()) {
                tokenLine.validLineEntry = validLine;
                return true;
            }
        }
        return false;
    }
    
    public boolean ValidateTokenizedLines() throws ExceptionNoValidLineFound {
        Logger.wrl("AssemblerThumb: ValidateTokenizedLines");        
        for(TokenLine tokenLine : asmTokenedData) {
            if(!ValidateTokenizedLine(tokenLine, jsonObjIsValidLines, jsonObjIsValidLines.is_valid_lines.get(JsonObjIsValidLines.ENTRY_LINE_EMPTY))) {
                throw new ExceptionNoValidLineFound("Could not find a matching valid line for line number, " + tokenLine.lineNum + " with source text, '" + tokenLine.source.source + "'");
             }
        }
        return true;
    }
}
