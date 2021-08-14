package net.middlemind.GenAsm;

import net.middlemind.GenAsm.Exceptions.ExceptionRedefinitionOfAreaDirective;
import net.middlemind.GenAsm.Exceptions.ExceptionMissingRequiredDirective;
import net.middlemind.GenAsm.Exceptions.ExceptionNoEntryFound;
import net.middlemind.GenAsm.Exceptions.ExceptionLoader;
import net.middlemind.GenAsm.Exceptions.ExceptionRedefinitionOfLabel;
import net.middlemind.GenAsm.Exceptions.ExceptionMalformedRange;
import net.middlemind.GenAsm.Exceptions.ExceptionNoClosingBracketFound;
import net.middlemind.GenAsm.Exceptions.ExceptionMalformedEntryEndDirectiveSet;
import net.middlemind.GenAsm.Exceptions.ExceptionNoValidLineFound;
import net.middlemind.GenAsm.Exceptions.ExceptionNoAreaDirectiveFound;
import net.middlemind.GenAsm.Exceptions.ExceptionNoTokenerFound;
import net.middlemind.GenAsm.Exceptions.ExceptionNoParentSymbolFound;
import net.middlemind.GenAsm.Exceptions.ExceptionListAndGroup;
import net.middlemind.GenAsm.Exceptions.ExceptionNoOpCodeFound;
import net.middlemind.GenAsm.Exceptions.ExceptionJsonObjLink;
import net.middlemind.GenAsm.Exceptions.ExceptionNoDirectiveFound;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsEntryTypes;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsValidLineEntry;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsSet;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsOpCodeArg;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsEntryType;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsFile;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsValidLine;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsDirective;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsOpCodes;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsOpCode;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsDirectiveArg;
import net.middlemind.GenAsm.JsonObjs.JsonObj;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsValidLines;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsRegisters;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsDirectives;
import net.middlemind.GenAsm.Loaders.Loader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import net.middlemind.GenAsm.Exceptions.ExceptionNoOpCodeLineFound;

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
    public JsonObjIsRegisters jsonObjIsRegisters;    
    
    public String asmSourceFile;
    public List<String> asmSourceData;
    public List<ArtifactLine> asmLexedData;
    public List<TokenLine> asmTokenedData;
    public Symbols symbols;
    
    public List<String> requiredDirectives;
    public int directiveLineEntry;
    public int directiveLineEnd;
    
    @Override
    public void RunAssembler(JsonObjIsSet jsonIsSet, String assemblySourceFile, Object other) {
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

            ValidateOpCodeLines();        
            ValidateDirectiveLines();
            WriteObject(asmTokenedData, "Assembly Tokenized Data", "/Users/victor/Documents/files/netbeans_workspace/GenAsm/cfg/THUMB/TESTS/output_tokened_phase3.json");            
            WriteObject(symbols, "Symbol Data", "/Users/victor/Documents/files/netbeans_workspace/GenAsm/cfg/THUMB/TESTS/output_symbols.json");
        } catch(Exception e) {
            Logger.wrl("AssemblerThumb: RunAssembler: Assembler encountered an exception, exiting...");
            e.printStackTrace();
        }
    }
    
    //DIRECTIVE METHODS
    public void PopulateDirectiveAndArgData() throws ExceptionMissingRequiredDirective, ExceptionRedefinitionOfAreaDirective, ExceptionNoOpCodeFound, ExceptionNoParentSymbolFound, ExceptionMalformedEntryEndDirectiveSet, ExceptionNoAreaDirectiveFound {
        Logger.wrl("AssemblerThumb: PopulateDirectiveAndArgData");        
        boolean directiveFound = false;
        String directiveName = null;
        int directiveIdx = -1;
        int reqDirectiveCount = requiredDirectives.size() - 1;
        List<String> reqDirectives = new ArrayList<>(requiredDirectives);
        int lastEntry = -1;
        int lastEnd = -1;
        int lastArea = -1;
        int activeLineCount = 0;
        
        for(TokenLine line : asmTokenedData) {            
            directiveFound = false;
            directiveName = null;
            directiveIdx = -1;
            
            if(lastArea != -1 && lastEntry != -1 && lastEnd == -1) {
                if(line.payloadLen != 0 && !line.validLineEntry.empty_line) {
                    line.lineNumMemCode = Utils.FormatHexString(Integer.toHexString(activeLineCount), jsonObjIsOpCodes.pc_prefetch_words);
                    activeLineCount += this.jsonObjIsOpCodes.bit_series.bit_len;
                }
            }
            
            for(Token token : line.payload) {
                if(token.type_name.equals(JsonObjIsEntryTypes.ENTRY_TYPE_NAME_DIRECTIVE)) {
                    directiveFound = true;
                    directiveName = token.source;
                    directiveIdx = token.index;
                 
                    if(reqDirectives.contains(token.source)) {
                        reqDirectives.remove(token.source);
                        reqDirectiveCount--;
                    }
                    
                    if(token.source.equals(JsonObjIsDirectives.DIRECTIVE_NAME_AREA)) {
                        if(lastArea == -1) {
                            lastArea = line.lineNum;
                        } else {
                            throw new ExceptionRedefinitionOfAreaDirective("Redefinition of AREA directive found on line " + line.lineNum + " with source " + line.source.source);
                        }
                    } else if(token.source.equals(JsonObjIsDirectives.DIRECTIVE_NAME_ENTRY)) {
                        if(lastArea == -1) {
                            throw new ExceptionNoAreaDirectiveFound("Could not find AREA directive before ENTRY directive on line " + line.lineNum + " with source " + line.source.source);
                        } else if(lastEntry == -1) {
                            lastEntry = line.lineNum;
                        } else {
                            throw new ExceptionMalformedEntryEndDirectiveSet("Found multiple ENTRY directives with a new entry on line " + line.lineNum + " with source " + line.source.source);
                        }
                    } else if(token.source.equals(JsonObjIsDirectives.DIRECTIVE_NAME_END)) {
                        if(lastArea == -1) {
                            throw new ExceptionNoAreaDirectiveFound("Could not find AREA directive before ENTRY directive on line " + line.lineNum + " with source " + line.source.source);
                        } else if(lastEntry == -1) {
                            throw new ExceptionMalformedEntryEndDirectiveSet("Could not find END directive before new ENTRY directive on line " + line.lineNum + " with source " + line.source.source);
                        } else if(lastEnd == -1) {
                            lastEnd = line.lineNum;
                        } else {
                            throw new ExceptionMalformedEntryEndDirectiveSet("Could not find END directive before new ENTRY directive on line " + line.lineNum + " with source " + line.source.source);
                        }
                        
                        if(lastEnd <= lastEntry) {
                            throw new ExceptionMalformedEntryEndDirectiveSet("Could not find END directive before new ENTRY directive on line " + line.lineNum + " with source " + line.source.source);
                        }                        
                    }
                }
            }
            
            if(directiveFound) {
                line.payloadDirective = directiveName;
                line.isLineDirective = true;
                line.payloadLenArg = CountArgTokens(line.payload, 0, JsonObjIsEntryTypes.ENTRY_TYPE_NAME_CAT_ARG_DIRECTIVE, false);
                line.matchesDirective = FindDirectiveMatches(line.payloadDirective, line.payloadLenArg);
                
                if(line.matchesDirective == null || line.matchesDirective.isEmpty()) {
                    throw new ExceptionNoOpCodeFound("Could not find a matching directive entry for name '" + line.payloadDirective + "' with argument count " + line.payloadLenArg + " at line " + line.lineNum + " with source text '" + line.source.source + "'");
                }
            }
        }
        
        if(reqDirectiveCount > 0) {
            String lmissing = "";
            for(int i = 0; i < reqDirectives.size(); i++) {
                lmissing += reqDirectives.get(i);
                if(i < reqDirectives.size() - 1) {
                    lmissing += ",";
                }
            }
            throw new ExceptionMissingRequiredDirective("Could not find required directive in the source file, '" + lmissing + "'");
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
    
    public List<JsonObjIsDirective> FindDirectiveMatches(String directiveName, int argLen) {
        List<JsonObjIsDirective> ret = new ArrayList<>();
        for(JsonObjIsDirective directive : jsonObjIsDirectives.is_directives) {
            if(directive.directive_name.equals(directiveName) && directive.arg_len == argLen) {
                ret.add(directive);
            }
        }
        return ret;
    }
    
    //OPCODE METHODS
    public void PopulateOpCodeAndArgData() throws ExceptionRedefinitionOfLabel, ExceptionNoOpCodeFound, ExceptionNoParentSymbolFound {
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
                        token.isArgOpCode = true;
                    }
                    
                    if(token.index == 0) {
                        lastLabel = token.source;
                        lastLabelLine = line;
                        if(symbols.symbols.containsKey(token.source)) {
                            throw new ExceptionRedefinitionOfLabel("Found symbol '" + token.source + "' redefined on line " + token.lineNum + " originally defned on line " + symbol.lineNum);
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
                    
                } else if(token.type_name.equals(JsonObjIsEntryTypes.ENTRY_TYPE_NAME_LABEL_NUMERIC_LOCAL)) {                    
                    if(Utils.IsStringEmpty(lastLabel)) {
                        throw new ExceptionNoParentSymbolFound("No parent label found for local symbol '" + token.source + "' on line " + token.lineNum + ", could not find parent label to associate with local label.");
                    } else {
                        if(symbols.symbols.containsKey(lastLabel)) {
                            symbol = symbols.symbols.get(lastLabel);
                            if(symbol.symbols.containsKey(token.source)) {
                                throw new ExceptionRedefinitionOfLabel("Found symbol '" + token.source + "' redefined on line " + token.lineNum + " originally defned on line " + symbol.lineNum);
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
                line.isLineOpCode = true;
                line.payloadLenArg = CountArgTokens(line.payload, 0) + labelArgs;
                line.matchesOpCode = FindOpCodeMatches(line.payloadOpCode, line.payloadLenArg);
                
                if(line.matchesOpCode == null || line.matchesOpCode.isEmpty()) {
                    throw new ExceptionNoOpCodeFound("Could not find a matching opCode entry for name '" + line.payloadOpCode + "' with argument count " + line.payloadLenArg + " at line " + line.lineNum + " with source text '" + line.source.source + "'");
                }
            }
        }
    }    
    
    public void ValidateOpCodeLines() throws ExceptionNoOpCodeFound, ExceptionNoOpCodeLineFound {
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
        
        for(String key : symbols.symbols.keySet()) {
            Symbol symbol = symbols.symbols.get(key);
            TokenLine line = asmTokenedData.get(symbol.lineNum);
            if(line.validLineEntry.index == 1 || line.validLineEntry.index == 2 || line.validLineEntry.index == 3) {
                symbol.lineNumActive = FindNextOpCodeLine(symbol.lineNum, key);
                symbol.isEmptyLineLabel = true;
                Logger.wrl("Adjusting symbol line number from " + symbol.lineNum + " to " + symbol.lineNumActive + " due to symbol marking an empty line");
            } else {
                symbol.lineNumActive = symbol.lineNum;
            }
        }
    }
    
    public int FindNextOpCodeLine(int lineNum, String label) throws ExceptionNoOpCodeLineFound {
        if(lineNum + 1 < asmTokenedData.size()) {
            for(int i = lineNum + 1; i < asmTokenedData.size(); i++) {
                TokenLine line = asmTokenedData.get(i);
                if(line.isLineOpCode) {
                    return i;
                }
            }
        }
        throw new ExceptionNoOpCodeLineFound("Could not find an OpCode line for label '" + label + "' on empty line " + lineNum);
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
    
    public List<JsonObjIsOpCode> FindOpCodeMatches(String opCodeName, int argLen) {
        List<JsonObjIsOpCode> ret = new ArrayList<>();
        for(JsonObjIsOpCode opCode : jsonObjIsOpCodes.is_op_codes) {
            if(opCode.op_code_name.equals(opCodeName) && opCode.arg_len == argLen) {
                ret.add(opCode);
            }
        }
        return ret;
    }
    
    //ARG TOKEN COUNTER METHODS
    public int CountArgTokens(List<Token> payload, int argCount) {
        return CountArgTokens(payload, argCount, JsonObjIsEntryTypes.ENTRY_TYPE_NAME_CAT_ARG_OPCODE, true);
    }
    
    public int CountArgTokens(List<Token> payload, int argCount, String argCategory, boolean isOpCodeArg) {
        for(Token token : payload) {
            if(token.type != null && ((JsonObjIsEntryType)token.type).category.equals(argCategory)) {
                argCount++;
                if(isOpCodeArg) {
                    token.isArgOpCode = true;
                } else {
                    token.isArgDirective = true;
                }
            }
            
            if(token.payload != null && token.payload.size() > 0) {
                if(token.type_name.equals(JsonObjIsEntryTypes.ENTRY_TYPE_NAME_START_LIST)) {
                    argCount++;
                    if(isOpCodeArg) {
                        token.isArgOpCode = true;
                    } else {
                        token.isArgDirective = true;
                    }
                } else {
                    argCount = CountArgTokens(token.payload, argCount, argCategory, isOpCodeArg);
                }
            }
        }
        return argCount;
    }
       
    //CLEAN TOKEN STRUCTURE
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
    
    //OUTPUT OBJECT DATA METHODS
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
    
    //LOAD, PARSE, LINK JSONOBJ DATA METHODS
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
                
                if(jsonObj.GetLoader().equals("net.middlemind.GenAsm.Loaders.LoaderIsEntryTypes")) {
                    jsonObjIsEntryTypes = (JsonObjIsEntryTypes)jsonObj;
                    Logger.wrl("AssemblerThumb: RunAssembler: Found JsonObjIsEntryTypes object, storing it...");
                
                } else if(jsonObj.GetLoader().equals("net.middlemind.GenAsm.Loaders.LoaderIsValidLines")) {
                    jsonObjIsValidLines = (JsonObjIsValidLines)jsonObj;
                    Logger.wrl("AssemblerThumb: RunAssembler: Found JsonObjIsValidLines object, storing it...");
                
                } else if(jsonObj.GetLoader().equals("net.middlemind.GenAsm.Loaders.LoaderIsOpCodes")) {
                    jsonObjIsOpCodes = (JsonObjIsOpCodes)jsonObj;
                    Logger.wrl("AssemblerThumb: RunAssembler: Found JsonObjIsOpCodes object, storing it...");
                
                } else if(jsonObj.GetLoader().equals("net.middlemind.GenAsm.Loaders.LoaderIsDirectives")) {
                    jsonObjIsDirectives = (JsonObjIsDirectives)jsonObj;
                    Logger.wrl("AssemblerThumb: RunAssembler: Found JsonObjIsDirectives object, storing it...");

                } else if(jsonObj.GetLoader().equals("net.middlemind.GenAsm.Loaders.LoaderIsRegisters")) {
                    jsonObjIsRegisters = (JsonObjIsRegisters)jsonObj;
                    Logger.wrl("AssemblerThumb: RunAssembler: Found JsonObjIsRegisters object, storing it...");                     
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
    
    //LEX SOURCE CODE
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
    
    //TOKENIZE AND VALIDATE LEXERIZED LINES
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
    
    public boolean ValidateTokenizedLine(TokenLine line, JsonObjIsValidLines validLines, JsonObjIsValidLine validLineEmpty) {
        int tokenCount = line.payload.size();
        int[] res = null;
        int currentEntry = -1;
        int entries = -1;
        
        if(tokenCount == 0) {
            line.validLineEntry = validLineEmpty;
            line.isLineEmpty = true;
            return true;
        }
        
        for(JsonObjIsValidLine validLine : validLines.is_valid_lines) {
            res = null;
            currentEntry = -1;
            tokenCount = line.payload.size();
            entries = -1;
            
            for(Token token : line.payload) {
                res = FindValidLineEntry(validLine, token, currentEntry, 0);
                if(res == null) {
                    break;
                }

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
            
            if(tokenCount == 0 && entries == validLine.is_valid_line.size()) {
                line.validLineEntry = validLine;
                return true;
            }
        }
        return false;
    }
    
    public boolean ValidateTokenizedLines() throws ExceptionNoValidLineFound {
        Logger.wrl("AssemblerThumb: ValidateTokenizedLines");
        for(TokenLine line : asmTokenedData) {
            if(!ValidateTokenizedLine(line, jsonObjIsValidLines, jsonObjIsValidLines.is_valid_lines.get(JsonObjIsValidLines.ENTRY_LINE_EMPTY))) {
                throw new ExceptionNoValidLineFound("Could not find a matching valid line for line number, " + line.lineNum + " with source text, '" + line.source.source + "'");
            }
        }
        return true;
    }

    //BUILD OPCODE
    public String BuildOpCode(TokenLine line) {
        if(!Utils.IsStringEmpty(line.payloadOpCode) && !line.isLineEmpty && !line.isLineDirective && line.isLineOpCode) {
            JsonObjIsOpCode opCode = line.matchesOpCode.get(0);
        } else {
            //TODO: throw invalid opcode exception
        }
        return null;
    }
}
