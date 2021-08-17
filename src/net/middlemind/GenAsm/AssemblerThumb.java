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
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import net.middlemind.GenAsm.Exceptions.ExceptionNoOpCodeLineFound;
import net.middlemind.GenAsm.JsonObjs.JsonObjBitSeries;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsOpCodeArgSorter;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsRegister;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 12:08 PM EST
 */
@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "UseSpecificCatch", "null", "CallToPrintStackTrace", "UnusedAssignment", "Convert2Diamond", "ConvertToStringSwitch"})
public class AssemblerThumb implements Assembler {
    public static String ENDIAN_NAME_BIG = "BIG";
    public static String ENDIAN_NAME_LITTLE = "LITTLE";    
    
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
    public List<String> asmDataSource;
    public List<ArtifactLine> asmDataLexed;
    public List<TokenLine> asmDataTokened;
    public Symbols symbols;
    
    public List<String> requiredDirectives;    
    public AreaThumb areaThumbCode;
    public AreaThumb areaThumbData;
    public List<TokenLine> asmAreaLinesCode;
    public List<TokenLine> asmAreaLinesData;    
    
    public int lineLenBytes = 2;
    public int lineLenWords = 1;
    public JsonObjBitSeries lineBitSeries;
    public int pcPreFetchBytes;
    public int pcPreFetchWords;
    public boolean isEndianBig = false;
    public boolean isEndianLittle = true;    
    
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
            
            Logger.wrl("");
            Logger.wrl("STEP1: Process JsonObjIsSet's file entries and load then parse the json object data");
            LoadAndParseJsonObjData();

            Logger.wrl("");
            Logger.wrl("STEP2: Link loaded json object data");
            LinkJsonObjData();

            Logger.wrl("");
            Logger.wrl("STEP3: Load and lexerize the assembly source file");           
            LoadAndLexerizeAssemblySource();
            WriteObject(asmDataLexed, "Assembly Lexerized Data", "/Users/victor/Documents/files/netbeans_workspace/GenAsm/cfg/THUMB/TESTS/output_lexed.json");        

            Logger.wrl("");
            Logger.wrl("STEP4: Tokenize the lexerized artifacts");
            TokenizeLexerArtifacts();
            WriteObject(asmDataTokened, "Assembly Tokenized Data", "/Users/victor/Documents/files/netbeans_workspace/GenAsm/cfg/THUMB/TESTS/output_tokened_phase0.json");            
            
            Logger.wrl("");
            Logger.wrl("STEP5: Validate token lines");
            ValidateTokenizedLines();
            WriteObject(asmDataTokened, "Assembly Tokenized Data", "/Users/victor/Documents/files/netbeans_workspace/GenAsm/cfg/THUMB/TESTS/output_tokened_phase1.json");            

            Logger.wrl("");
            Logger.wrl("STEP6: Combine comment tokens as children of the initial comment token");
            CollapseCommentTokens();
            
            Logger.wrl("");
            Logger.wrl("STEP7: Expand register ranges into individual register entries");
            ExpandRegisterRangeTokens();
            
            Logger.wrl("");
            Logger.wrl("STEP8: Combine list and group tokens as children of the initial list or group token");
            CollapseListAndGroupTokens();
            
            Logger.wrl("");
            Logger.wrl("STEP9: Mark OpCode, OpCode argument, and register tokens");
            PopulateOpCodeAndArgData();
            
            Logger.wrl("");
            Logger.wrl("STEP10: Mark directive and directive argument tokens, create area based line lists with hex numbering");
            PopulateDirectiveArgAndAreaData();
            WriteObject(asmDataTokened, "Assembly Tokenized Data", "/Users/victor/Documents/files/netbeans_workspace/GenAsm/cfg/THUMB/TESTS/output_tokened_phase2.json");

            Logger.wrl("");
            Logger.wrl("STEP11: Validate OpCode lines against known OpCodes by comparing arguments");
            ValidateOpCodeLines();
            
            Logger.wrl("");
            Logger.wrl("STEP12: Validate directive lines against known directives by comparing arguments");
            ValidateDirectiveLines();
            WriteObject(asmDataTokened, "Assembly Tokenized Data", "/Users/victor/Documents/files/netbeans_workspace/GenAsm/cfg/THUMB/TESTS/output_tokened_phase3.json");            
            WriteObject(symbols, "Symbol Data", "/Users/victor/Documents/files/netbeans_workspace/GenAsm/cfg/THUMB/TESTS/output_symbols.json");
        
            Logger.wrl("");
            Logger.wrl("List Assembly Source Areas:");
            if(areaThumbCode != null) {
                Logger.wrl("AreaThumbCode: AreaLine: " + areaThumbCode.lineNumArea + " EntryLine: " + areaThumbCode.lineNumEntry + " EndLine: " + areaThumbCode.lineNumEnd);
                Logger.wrl("AreaThumbCode: Attributes: IsCode: " + areaThumbCode.isCode + " IsData: " + areaThumbCode.isData + " IsReadOnly: " + areaThumbCode.isReadOnly + " IsReadWrite: " + areaThumbCode.isReadWrite);
                WriteObject(asmAreaLinesCode, "Assembly Source Area Code Lines", "/Users/victor/Documents/files/netbeans_workspace/GenAsm/cfg/THUMB/TESTS/output_area_code_lines.json");
                WriteObject(areaThumbCode, "Assembly Source Area Code Desc", "/Users/victor/Documents/files/netbeans_workspace/GenAsm/cfg/THUMB/TESTS/output_area_code_desc.json");
            } else {
                Logger.wrl("AreaThumbCode: is null");
            }
            
            if(areaThumbData != null) {
                Logger.wrl("AreaThumbData: AreaLine: " + areaThumbData.lineNumArea + " EntryLine: " + areaThumbData.lineNumEntry + " EndLine: " + areaThumbData.lineNumEnd);
                Logger.wrl("AreaThumbData: Attributes: IsCode: " + areaThumbData.isCode + " IsData: " + areaThumbData.isData + " IsReadOnly: " + areaThumbData.isReadOnly + " IsReadWrite: " + areaThumbData.isReadWrite);
                WriteObject(asmAreaLinesData, "Assembly Source Area Data Lines", "/Users/victor/Documents/files/netbeans_workspace/GenAsm/cfg/THUMB/TESTS/output_area_data_lines.json");
                WriteObject(areaThumbData, "Assembly Source Area Data Desc", "/Users/victor/Documents/files/netbeans_workspace/GenAsm/cfg/THUMB/TESTS/output_area_data_desc.json");                
            } else {
                Logger.wrl("AreaThumbData: is null");
            }
            
            Logger.wrl("");
            Logger.wrl("Assembler Line Data:");
            Logger.wrl("LineLengthBytes: " + lineLenBytes);
            Logger.wrl("LineLengthWords: " + lineLenWords);
            Logger.wrl("LineBitSeries:");
            lineBitSeries.Print("\t");
            
            BuildOpCode(asmDataTokened.get(5), areaThumbCode);
        } catch(Exception e) {
            Logger.wrl("AssemblerThumb: RunAssembler: Assembler encountered an exception, exiting...");
            e.printStackTrace();
        }
    }
    
    //DIRECTIVE METHODS
    private void PopulateDirectiveArgAndAreaData() throws ExceptionMissingRequiredDirective, ExceptionRedefinitionOfAreaDirective, ExceptionNoDirectiveFound, ExceptionNoParentSymbolFound, ExceptionMalformedEntryEndDirectiveSet, ExceptionNoAreaDirectiveFound {
        Logger.wrl("AssemblerThumb: PopulateDirectiveAndArgData");        
        boolean directiveFound = false;
        String directiveName = null;
        int directiveIdx = -1;
        int reqDirectiveCount = requiredDirectives.size() - 1;
        List<String> reqDirectives = new ArrayList<>(requiredDirectives);
        
        int lastEntry = -1;
        Token lastEntryToken = null;
        TokenLine lastEntryTokenLine = null;
        
        int lastEnd = -1;
        Token lastEndToken = null;
        TokenLine lastEndTokenLine = null;
        
        int lastArea = -1;
        Token lastAreaToken = null;
        TokenLine lastAreaTokenLine = null;
        
        int lastCode = -1;
        int lastData = -1;
        int lastReadOnly = -1;
        int lastReadWrite = -1;
        int activeLineCount = 0;
        AreaThumb tmpArea = null;
        
        for(TokenLine line : asmDataTokened) {            
            directiveFound = false;
            directiveName = null;
            directiveIdx = -1;

            if(lastData != -1 && line.isLineOpCode) {
                throw new ExceptionMalformedEntryEndDirectiveSet("Cannot have OpCode instructions when AREA type is DATA, found on line " + line.lineNum + " with source " + line.source.source);                
            }
            
            directiveFound = false;
            for(Token token : line.payload) {                
                if(token.type_name.equals(JsonObjIsEntryTypes.ENTRY_TYPE_NAME_DIRECTIVE)) {
                    if(!directiveFound) {
                        directiveFound = true;
                        directiveName = token.source;
                        directiveIdx = token.index;
                    }
                 
                    if(reqDirectives.contains(token.source)) {
                        reqDirectives.remove(token.source);
                        reqDirectiveCount--;
                    }
                    
                    if(token.source.equals(JsonObjIsDirectives.DIRECTIVE_NAME_AREA)) {
                        if(lastArea == -1) {
                            lastArea = line.lineNum;
                            lastAreaToken = token;
                            lastAreaTokenLine = line;

                            tmpArea = new AreaThumb();
                            tmpArea.area = lastAreaToken;
                            tmpArea.areaLine = lastAreaTokenLine;
                            tmpArea.lineNumArea = lastArea;
                        } else {
                            throw new ExceptionRedefinitionOfAreaDirective("Redefinition of AREA directive found on line " + line.lineNum + " with source " + line.source.source);
                        }
                        
                    } else if(token.source.equals(JsonObjIsDirectives.DIRECTIVE_NAME_CODE)) {
                        if(lastCode == -1) {
                            lastCode = line.lineNum;
                        }
                        tmpArea.isCode = true;
                        tmpArea.isReadOnly = true;
                        if(lastData != -1 && lastData == lastCode) {
                            throw new ExceptionMalformedEntryEndDirectiveSet("Cannot set AREA type to CODE when type is DATA, found on line " + line.lineNum + " with source " + line.source.source);
                        }
                        
                    } else if(token.source.equals(JsonObjIsDirectives.DIRECTIVE_NAME_DATA)) {
                       if(lastData == -1) {
                            lastData = line.lineNum;
                        }                        
                        tmpArea.isData = true;
                        tmpArea.isReadWrite = true;
                        if(lastCode != -1 && lastCode == lastData) {
                            throw new ExceptionMalformedEntryEndDirectiveSet("Cannot set AREA type to DATA when type is CODE, found on line " + line.lineNum + " with source " + line.source.source);
                        }
                        
                    } else if(token.source.equals(JsonObjIsDirectives.DIRECTIVE_NAME_READONLY)) {
                        if(lastReadOnly == -1) {
                            lastReadOnly = line.lineNum;
                        }
                        tmpArea.isReadOnly = true;
                        tmpArea.isReadWrite = false;
                        if(lastReadOnly != -1 && lastReadOnly == lastReadWrite) {
                            throw new ExceptionMalformedEntryEndDirectiveSet("Cannot set AREA type to READONLY when type is READWRITE, found on line " + line.lineNum + " with source " + line.source.source);
                        }
                        
                    } else if(token.source.equals(JsonObjIsDirectives.DIRECTIVE_NAME_READWRITE)) {
                        if(lastReadWrite == -1) {
                            lastReadWrite = line.lineNum;
                        }                        
                        tmpArea.isReadWrite = true;
                        tmpArea.isReadOnly = false;
                        if(lastReadWrite != -1 && lastReadWrite == lastReadOnly) {
                            throw new ExceptionMalformedEntryEndDirectiveSet("Cannot set AREA type to READWRITE when type is READONLY, found on line " + line.lineNum + " with source " + line.source.source);
                        }
                        
                    } else if(token.source.equals(JsonObjIsDirectives.DIRECTIVE_NAME_ENTRY)) {
                        if(lastArea == -1) {
                            throw new ExceptionNoAreaDirectiveFound("Could not find AREA directive before ENTRY directive on line " + line.lineNum + " with source " + line.source.source);
                        } else if(lastEntry == -1) {
                            lastEntry = line.lineNum;
                            lastEntryToken = token;
                            lastEntryTokenLine = line;
                            
                            tmpArea.entry = lastEntryToken;
                            tmpArea.entryLine = lastEntryTokenLine;
                            tmpArea.lineNumEntry = lastEntry;
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
                            lastEndToken = token;
                            lastEndTokenLine = line;

                            tmpArea.end = lastEndToken;
                            tmpArea.endLine = lastEndTokenLine;
                            tmpArea.lineNumEnd = lastEnd;
                            
                            if(tmpArea.isData) {
                                if(areaThumbData == null) {
                                    areaThumbData = tmpArea;
                                } else {
                                    throw new ExceptionMalformedEntryEndDirectiveSet("Can only define one DATA AREA, second definition on line " + tmpArea.lineNumArea + " with source " + tmpArea.areaLine.source.source);
                                }
                            } else {
                                if(areaThumbCode == null) {
                                    areaThumbCode = tmpArea;
                                } else {
                                    throw new ExceptionMalformedEntryEndDirectiveSet("Can only define one CODE AREA, second definition on line " + tmpArea.lineNumArea + " with source " + tmpArea.areaLine.source.source);
                                }
                            }
                            
                            if(lastEnd <= lastEntry) {
                                throw new ExceptionMalformedEntryEndDirectiveSet("Could not find END directive before new ENTRY directive on line " + line.lineNum + " with source " + line.source.source);
                            }
                            
                            tmpArea = null;
                            lastArea = -1;
                            lastAreaToken = null;
                            lastAreaTokenLine = null;

                            lastEntry = -1;
                            lastEntryToken = null;
                            lastEntryTokenLine = null;                                

                            lastEnd = -1;
                            lastEndToken = null;
                            lastEndTokenLine = null;
                            
                            lastCode = -1;
                            lastData = -1;
                            lastReadOnly = -1;
                            lastReadWrite = -1;
                        } else {
                            throw new ExceptionMalformedEntryEndDirectiveSet("Could not find END directive before new ENTRY directive on line " + line.lineNum + " with source " + line.source.source);
                        }
                    }
                }
                
                if(directiveFound) {
                    line.payloadDirective = directiveName;
                    line.isLineDirective = true;
                    line.payloadLenArg = CountArgTokens(line.payload, 0, JsonObjIsEntryTypes.ENTRY_TYPE_NAME_CAT_ARG_DIRECTIVE, false);
                    line.matchesDirective = FindDirectiveMatches(line.payloadDirective, line.payloadLenArg);
                }
            }
            
            if(directiveFound) {                
                if(line.matchesDirective == null || line.matchesDirective.isEmpty()) {
                    throw new ExceptionNoDirectiveFound("Could not find a matching directive entry for name '" + line.payloadDirective + "' with argument count " + line.payloadLenArg + " at line " + line.lineNum + " with source text '" + line.source.source + "'");
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
        
        if(areaThumbCode != null && areaThumbData != null) {
            if(areaThumbCode.lineNumEntry < areaThumbData.lineNumEntry) {
                //process code area first
                asmAreaLinesCode = new ArrayList<TokenLine>();
                activeLineCount = 0;
                for(int z = areaThumbCode.lineNumEntry + 1; z < areaThumbCode.lineNumEnd; z++) {
                    TokenLine line = asmDataTokened.get(z);
                    if(line.isLineOpCode && !line.isLineEmpty && !line.isLineDirective) {
                        line.lineNumMemCode = Utils.FormatHexString(Integer.toHexString(activeLineCount), lineLenBytes);
                        asmAreaLinesCode.add(line);
                        activeLineCount += lineBitSeries.bit_len;
                    }
                }
                
                asmAreaLinesData = new ArrayList<TokenLine>();
                for(int z = areaThumbData.lineNumEntry + 1; z < areaThumbData.lineNumEnd; z++) {
                    TokenLine line = asmDataTokened.get(z);
                    if(line.isLineDirective && !line.isLineOpCode && !line.isLineEmpty) {
                        line.lineNumMemCode = Utils.FormatHexString(Integer.toHexString(activeLineCount), lineLenBytes);
                        asmAreaLinesData.add(line);
                        activeLineCount += lineBitSeries.bit_len;
                    }
                }
                
            } else {
                //process data area first
                asmAreaLinesData = new ArrayList<TokenLine>();
                activeLineCount = 0;
                for(int z = areaThumbData.lineNumEntry + 1; z < areaThumbData.lineNumEnd; z++) {
                    TokenLine line = asmDataTokened.get(z);
                    if(line.isLineDirective && !line.isLineOpCode && !line.isLineEmpty) {
                        line.lineNumMemCode = Utils.FormatHexString(Integer.toHexString(activeLineCount), lineLenBytes);
                        asmAreaLinesData.add(line);
                        activeLineCount += lineBitSeries.bit_len;
                    }
                }
                
                asmAreaLinesCode = new ArrayList<TokenLine>();
                for(int z = areaThumbCode.lineNumEntry + 1; z < areaThumbCode.lineNumEnd; z++) {
                    TokenLine line = asmDataTokened.get(z);
                    if(line.isLineOpCode && !line.isLineEmpty && !line.isLineDirective) {
                        line.lineNumMemCode = Utils.FormatHexString(Integer.toHexString(activeLineCount), lineLenBytes);
                        asmAreaLinesCode.add(line);
                        activeLineCount += lineBitSeries.bit_len;
                    }
                }                
            }
        } else if(areaThumbCode != null) {
            //process code area first
            asmAreaLinesCode = new ArrayList<TokenLine>();
            activeLineCount = 0;
            for(int z = areaThumbCode.lineNumEntry + 1; z < areaThumbCode.lineNumEnd; z++) {
                TokenLine line = asmDataTokened.get(z);
                if(line.isLineOpCode && !line.isLineEmpty && !line.isLineDirective) {
                    line.lineNumMemCode = Utils.FormatHexString(Integer.toHexString(activeLineCount), lineLenBytes);
                    asmAreaLinesCode.add(line);
                    activeLineCount += lineBitSeries.bit_len;
                }
            }
                
        } else {
            throw new ExceptionMalformedEntryEndDirectiveSet("Cannot have only a DATA AREA, CODE AREA is required");
        }
    }    
    
    private void ValidateDirectiveLines() throws ExceptionNoDirectiveFound {
        Logger.wrl("AssemblerThumb: ValidateDirectiveLines"); 
        boolean directiveFound = false;
        String directiveName = null;
        Token directiveToken = null;
        int directiveIdx = -1;
        List<Token> args = null;
        JsonObjIsDirective directive = null; 
        
        for(TokenLine line : asmDataTokened) {
            //if(line.matchesDirective != null && line.matchesDirective.size() > 1) {
            if(line.isLineDirective) {
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
    
    private JsonObjIsDirective FindDirectiveArgMatches(List<JsonObjIsDirective> directiveMatches, List<Token> args, Token directiveToken) throws ExceptionNoDirectiveFound {
        JsonObjIsDirectiveArg directiveArg = null;
        Token argToken = null;
        boolean argFound = true;
        int directiveArgIdx = -1;
        
        for(JsonObjIsDirective directive : directiveMatches) {            
            directiveArg = null;
            argToken = null;
            argFound = true;
            
            for(int i = 0; i < directive.args.size(); i++) {
                directiveArg = directive.args.get(i);
                directiveArgIdx = i;
                
                if(i < args.size()) {
                    argToken = args.get(i);
                } else {
                    argFound = false;
                    break;
                }
                
                if(directiveArg != null && argToken != null) {
                    if(directiveArg.is_entry_types.contains(argToken.type_name)) {
                        if(!Utils.IsListEmpty(directiveArg.is_arg_value)) {
                            if(!directiveArg.is_arg_value.contains(argToken.source)) {
                                argFound = false;
                                break;
                            }
                        }
                    } else {
                        argFound = false;
                        break;
                    }
                } else {
                    argFound = false;
                    break;
                }
            }
            
            if(argFound) {
                return directive;
            }
        }
        
        throw new ExceptionNoDirectiveFound("Could not find a directive that has matching arguments for line number " + directiveToken.lineNum + " with directive '" + directiveToken.source + "' at directive argument index " + directiveArgIdx);
    }    
    
    private List<JsonObjIsDirective> FindDirectiveMatches(String directiveName, int argLen) {
        List<JsonObjIsDirective> ret = new ArrayList<>();
        for(JsonObjIsDirective directive : jsonObjIsDirectives.is_directives) {
            if(directive.directive_name.equals(directiveName) && directive.arg_len == argLen) {
                ret.add(directive);
            }
        }
        return ret;
    }
    
    //OPCODE METHODS
    private void PopulateOpCodeAndArgData() throws ExceptionRedefinitionOfLabel, ExceptionNoOpCodeFound, ExceptionNoParentSymbolFound {
        Logger.wrl("AssemblerThumb: PopulateOpCodeAndArgData");        
        boolean opCodeFound = false;
        String opCodeName = null;
        int opCodeIdx = -1;
        int labelArgs = -1;
        String lastLabel = null;
        TokenLine lastLabelLine = null;
        Symbol symbol = null;
        
        for(TokenLine line : asmDataTokened) {            
            opCodeFound = false;
            opCodeName = null;
            opCodeIdx = -1;
            labelArgs = 0;
            
            for(Token token : line.payload) {
                //MATCH REGISTERS
                if(Utils.ContainsStr(JsonObjIsEntryTypes.ENTRY_TYPE_NAME_REGISTERS, token.type_name)) {
                    String regCode = token.source;
                    if(token.type_name.equals(JsonObjIsEntryTypes.ENTRY_TYPE_NAME_REGISTERWB)) {
                        regCode = regCode.replace("!", "");
                    }
                    regCode = regCode.replace(" ", "");
                    regCode = regCode.replace(",", "");
                    
                    for(JsonObjIsRegister register : jsonObjIsRegisters.is_registers) {
                        if(register.register_name.equals(regCode)) {
                            token.register = register;
                            break;
                        }
                    }
                }
                
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
    
    private void ValidateOpCodeLines() throws ExceptionNoOpCodeFound, ExceptionNoOpCodeLineFound {
        Logger.wrl("AssemblerThumb: ValidateOpCodeLines"); 
        boolean opCodeFound = false;
        String opCodeName = null;
        Token opCodeToken = null;
        int opCodeIdx = -1;
        List<Token> args = null;
        JsonObjIsOpCode opCode = null; 
        
        for(TokenLine line : asmDataTokened) {
            //if(line.matchesOpCode != null && line.matchesOpCode.size() > 1) {
            if(line.isLineOpCode) {
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
            TokenLine line = asmDataTokened.get(symbol.lineNum);
            if(Utils.ContainsInt(JsonObjIsValidLines.ENTRY_LINES_LABEL_EMPTY, line.validLineEntry.index)) {    
                symbol.lineNumActive = FindNextOpCodeLine(symbol.lineNum, key);
                symbol.isEmptyLineLabel = true;
                Logger.wrl("Adjusting symbol line number from " + symbol.lineNum + " to " + symbol.lineNumActive + " due to symbol marking an empty line");
            } else {
                symbol.lineNumActive = symbol.lineNum;
            }
        }
    }
    
    private int FindNextOpCodeLine(int lineNum, String label) throws ExceptionNoOpCodeLineFound {
        if(lineNum + 1 < asmDataTokened.size()) {
            for(int i = lineNum + 1; i < asmDataTokened.size(); i++) {
                TokenLine line = asmDataTokened.get(i);
                if(line.isLineOpCode) {
                    return i;
                }
            }
        }
        throw new ExceptionNoOpCodeLineFound("Could not find an OpCode line for label '" + label + "' on empty line " + lineNum);
    }
    
    private JsonObjIsOpCode FindOpCodeArgMatches(List<JsonObjIsOpCode> opCodeMatches, List<Token> args, Token opCodeToken) throws ExceptionNoOpCodeFound {
        JsonObjIsOpCodeArg opCodeArg = null;
        JsonObjIsOpCodeArg opCodeArgSub = null;
        Token argToken = null;
        Token argTokenSub = null;
        boolean argFound = true;
        boolean argFoundSub = false;
        boolean hasArgsSub = false;
        int opCodeArgIdx = -1;
        int opCodeArgIdxSub = -1;
        
        for(JsonObjIsOpCode opCode : opCodeMatches) {            
            opCodeArg = null;
            argToken = null;
            argFound = true;
            argFoundSub = false;
            hasArgsSub = false;
            
            //Sort Json OpCode arguments so that they are arg_index ascending
            Collections.sort(opCode.args, new JsonObjIsOpCodeArgSorter());
            
            for(int i = 0; i < opCode.args.size(); i++) {
                opCodeArg = opCode.args.get(i);
                opCodeArgIdx = i;
                                
                if(i < args.size()) {
                    argToken = args.get(i);
                } else {
                    //Logger.wrl("Exit: AAA");
                    argFound = false;
                    break;
                }
                
                //Logger.wrl(opCode.op_code_name + ", " + opCodeArg.arg_index + ", " + argToken.type_name);                
                
                if(opCodeArg != null && argToken != null) {
                    //if(!(argToken.type_name.contains("Label") || opCodeArg.is_entry_types.contains(argToken.type_name))) {
                    if(!(argToken.isLabel || argToken.isLabelLocal || argToken.isLabelLocalRef || opCodeArg.is_entry_types.contains(argToken.type_name))) {
                        //Logger.wrl("Exit: BBB");
                        argFound = false;
                        break;
                    }
                                        
                    if(opCodeArg.sub_args != null && opCodeArg.sub_args.size() > 0) {
                        opCodeArgSub = null;
                        argTokenSub = null;
                        argFoundSub = true;
                        hasArgsSub = true;
                        
                        for(int j = 0; j < opCodeArg.sub_args.size(); j++) {
                            opCodeArgSub = opCodeArg.sub_args.get(j);
                            opCodeArgIdxSub = j;
                            if(argToken.payload != null && j < argToken.payload.size()) {
                                argTokenSub = argToken.payload.get(j);
                            } else {
                                //Logger.wrl("Exit: CCC");
                                argFound = false;
                                argFoundSub = false;
                                break;
                            }
                            
                            if(opCodeArgSub != null && argTokenSub != null) {
                                if(!(argTokenSub.isLabel || argTokenSub.isLabelLocal || argTokenSub.isLabelLocalRef || opCodeArg.is_entry_types.contains(argToken.type_name))) {
                                    //Logger.wrl("Exit: DDD");
                                    argFound = false;
                                    argFoundSub = false;
                                    break;
                                }
                            } else {
                                //Logger.wrl("Exit: EEE");
                                argFound = false;
                                argFoundSub = false;
                                break;
                            }
                        }
                    }
                } else {
                    //Logger.wrl("Exit: GGG");
                    argFound = false;
                    argFoundSub = false;
                    break;
                }
            }            
            //Logger.wrl("ArgFound: " + argFound + ", HasArgsSub: " + hasArgsSub + ", ArgFoundSub: " + argFoundSub);
            
            if(argFound && !hasArgsSub && !argFoundSub) {
                return opCode;                
            } else if(argFound && hasArgsSub && argFoundSub) {
                return opCode;
            }
        }
        
        throw new ExceptionNoOpCodeFound("Could not find an opCode that has matching arguments for line number " + opCodeToken.lineNum + " with opCode '" + opCodeToken.source + "' at opCode argument index " + opCodeArgIdx + " with sub argument index " + opCodeArgIdxSub);
    }    
    
    private List<JsonObjIsOpCode> FindOpCodeMatches(String opCodeName, int argLen) {
        List<JsonObjIsOpCode> ret = new ArrayList<>();
        for(JsonObjIsOpCode opCode : jsonObjIsOpCodes.is_op_codes) {
            if(opCode.op_code_name.equals(opCodeName) && opCode.arg_len == argLen) {
                ret.add(opCode);
            }
        }
        return ret;
    }
    
    //ARG TOKEN COUNTER METHODS
    private int CountArgTokens(List<Token> payload, int argCount) {
        return CountArgTokens(payload, argCount, JsonObjIsEntryTypes.ENTRY_TYPE_NAME_CAT_ARG_OPCODE, true);
    }
    
    private int CountArgTokens(List<Token> payload, int argCount, String argCategory, boolean isOpCodeArg) {
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
    private void CollapseListAndGroupTokens() throws ExceptionNoClosingBracketFound, ExceptionListAndGroup {
        Logger.wrl("AssemblerThumb: CollapseListAndGroupTokens");
        Token rootStartList = null;
        int rootStartIdxList = -1;
        Token rootStartGroup = null;
        int rootStartIdxGroup = -1;

        Token rootStopList = null;
        int rootStopIdxList = -1;
        Token rootStopGroup = null;
        int rootStopIdxGroup = -1;

        List<Token> clearTokensList = null;
        List<Token> clearTokensGroup = null;
        int copyStart = -1;
        int copyEnd = -1;
        int copyLen = -1;        
        
        for(TokenLine line : asmDataTokened) {
            rootStartList = null;
            rootStartIdxList = -1;
            rootStartGroup = null;
            rootStartIdxGroup = -1;

            rootStopList = null;
            rootStopIdxList = -1;
            rootStopGroup = null;
            rootStopIdxGroup = -1;
            
            clearTokensList = new ArrayList<>();
            clearTokensGroup = new ArrayList<>();
            copyStart = -1;
            copyEnd = -1;
            copyLen = -1;
            
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
    
    private JsonObjIsEntryType FindEntryType(String entryName) throws ExceptionNoEntryFound {        
        for(JsonObjIsEntryType entry : jsonObjIsEntryTypes.is_entry_types) {
            if(entry.type_name.equals(entryName)) {
                return entry;
            }
        }
        
        throw new ExceptionNoEntryFound("Could not find entry by name, '" + entryName + "', in loaded entry types.");
    }
    
    private String CleanRegisterRangeString(String range, String rangeDelim) {
        String ret = "";
        for(char c : range.toCharArray()) {
            if((c + "").equals(rangeDelim) || Character.isDigit(c)) {
                ret += c;
            }
        }
        return ret;
    }
    
    private void CleanTokenSource(Token token) {
        if(token != null && !Utils.IsStringEmpty(token.source)) {
            token.source = token.source.replace(JsonObjIsOpCode.DEFAULT_ARG_SEPARATOR, "");
            token.source = token.source.replace(System.lineSeparator(), "");
        }
    }
    
    private void ExpandRegisterRangeTokens() throws ExceptionNoEntryFound, ExceptionMalformedRange {
        Logger.wrl("AssemblerThumb: ExpandRegisterRangeToken");
        int rangeRootIdxLow = -1;
        int rangeRootIdxHi = -1;
        Token rangeRootLow = null;                
        Token rangeRootHi = null;
        String rangeStr = null;
        int[] range = null;
        int count = -1;
        Token newToken = null;
        int i = -1;

        List<Token> rangeAddTokensLow = null;
        List<Token> rangeAddTokensHi = null;                
        JsonObjIsEntryType entryTypeRegLow = null;
        JsonObjIsEntryType entryTypeRegHi = null;
        
        for(TokenLine line : asmDataTokened) {
            rangeRootIdxLow = 0;
            rangeRootIdxHi = 0;
            rangeRootLow = null;                
            rangeRootHi = null;
            rangeStr = "";
            range = null;
            count = 0;
            newToken = null;
            i = 0;

            rangeAddTokensLow = new ArrayList<>();
            rangeAddTokensHi = new ArrayList<>();                
            entryTypeRegLow = FindEntryType(JsonObjIsEntryTypes.ENTRY_TYPE_NAME_REGISTER_LOW);
            entryTypeRegHi = FindEntryType(JsonObjIsEntryTypes.ENTRY_TYPE_NAME_REGISTER_HI);                

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
    }
    
    private void CollapseCommentTokens() {
        Logger.wrl("AssemblerThumb: CollapseCommentTokens");
        boolean inComment = false;
        Token commentRoot = null;
        List<Token> clearTokens = null;  
            
        for(TokenLine line : asmDataTokened) {
            inComment = false;
            commentRoot = null;
            clearTokens = new ArrayList<>();  
            
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
    private void WriteObject(Object obj, String name, String fileName) throws IOException {
        Logger.wrl("AssemblerThumb: WriteObject: Name: " + name);
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();            
        String jsonString = gson.toJson(obj);
        FileUnloader.WriteStr(fileName, jsonString);
    }
    
    private void PrintObject(Object obj, String name) {
        Logger.wrl("AssemblerThumb: PrintObject: Name: '" + name + "'");
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();            
        String jsonString = gson.toJson(obj);
        Logger.wr(jsonString);        
    }
    
    //LOAD, PARSE, LINK JSONOBJ DATA METHODS
    private void LoadAndParseJsonObjData() throws ExceptionNoEntryFound, ExceptionLoader, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Logger.wrl("AssemblerThumb: LoadAndParseJsonObjData");
        Class cTmp = null;
        Loader ldr = null;
        String json = null;
        String jsonName = null;
        JsonObj jsonObj = null;
        
        for(JsonObjIsFile entry : isaDataSet.is_files) {
            cTmp = Class.forName(entry.loader_class);
            ldr = (Loader)cTmp.getDeclaredConstructor().newInstance();
            json = null;
            jsonName = null;
            jsonObj = null;

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
                lineBitSeries = jsonObjIsOpCodes.bit_series;
                pcPreFetchBytes = jsonObjIsOpCodes.pc_prefetch_bytes;
                pcPreFetchWords = jsonObjIsOpCodes.pc_prefetch_words;
                if(jsonObjIsOpCodes.endian.equals(AssemblerThumb.ENDIAN_NAME_BIG)) {
                    isEndianBig = true;
                    isEndianLittle = false;
                } else {
                    isEndianBig = false;
                    isEndianLittle = true;
                }
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
    }
    
    private void LinkJsonObjData() throws ExceptionJsonObjLink {
        Logger.wrl("AssemblerThumb: LinkJsonObjData");
        for(String s : isaData.keySet()) {
            JsonObj jsonObj = isaData.get(s);
            jsonObj.Link(jsonObjIsEntryTypes);
        }        
    }
    
    //LEX SOURCE CODE
    private void LoadAndLexerizeAssemblySource() throws IOException {
        Logger.wrl("AssemblerThumb: LoadAndLexAssemblySource: Load assembly source file");
        asmDataSource = FileLoader.Load(asmSourceFile);

        Logger.wrl("AssemblerThumb: LoadAndLexAssemblySource: Lexerize assembly source file");
        LexerSimple lex = new LexerSimple();
        asmDataLexed = lex.FileLexerize(asmDataSource);
    }
    
    //TOKENIZE AND VALIDATE LEXERIZED LINES
    private void TokenizeLexerArtifacts() throws ExceptionNoTokenerFound {
        Logger.wrl("AssemblerThumb: TokenizeLexerArtifacts");
        TokenerThumb tok = new TokenerThumb();
        asmDataTokened = tok.FileTokenize(asmDataLexed, jsonObjIsEntryTypes);
    }
    
    private int[] FindValidLineEntry(JsonObjIsValidLine validLine, Token token, int entry, int index) {
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
    
    private boolean ValidateTokenizedLine(TokenLine line, JsonObjIsValidLines validLines, JsonObjIsValidLine validLineEmpty) {
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
    
    private boolean ValidateTokenizedLines() throws ExceptionNoValidLineFound {
        Logger.wrl("AssemblerThumb: ValidateTokenizedLines");
        for(TokenLine line : asmDataTokened) {
            if(!ValidateTokenizedLine(line, jsonObjIsValidLines, jsonObjIsValidLines.is_valid_lines.get(JsonObjIsValidLines.ENTRY_LINE_EMPTY))) {
                throw new ExceptionNoValidLineFound("Could not find a matching valid line for line number, " + line.lineNum + " with source text, '" + line.source.source + "'");
            }
        }
        return true;
    }

    //TODO: Create AREA specific line listings and output them to json files
    //TODO: Create AREA specific build entries and output them to json files
    //TODO: Build OpCode instructions and output them to a linked listing file 
    
    //BUILD OPCODE
    private String BuildOpCode(TokenLine line, AreaThumb area) throws IOException {
        if(area.isCode) {
            if(!line.isLineEmpty && !line.isLineDirective && line.isLineOpCode) {
                JsonObjIsOpCode opCode = line.matchesOpCode.get(0);                
                List<JsonObjIsOpCodeArg> opCodeArgs = opCode.args;
                List<BuildOpCodeEntry> buildEntries = new ArrayList<>();
                
                BuildOpCodeEntry tmpB = null;
                int opCodeArgIdx = 0;
                for(Token token : line.payload) {
                    if(token.isArgOpCode) {
                        tmpB = new BuildOpCodeEntry();
                        tmpB.isOpCodeArg = true;
                        tmpB.opCodeArg = opCodeArgs.get(opCodeArgIdx);
                        tmpB.opCodeArg.arg_index++;
                        tmpB.bitSeries = tmpB.opCodeArg.bit_series;
                        tmpB.tokenOpCodeArg = token;
                        buildEntries.add(tmpB);
                        opCodeArgIdx++;
                        
                    } else if(token.isOpCode) {
                        tmpB = new BuildOpCodeEntry();
                        tmpB.isOpCode = true;
                        tmpB.bitSeries = opCode.bit_series;
                        tmpB.opCode = opCode;
                        tmpB.tokenOpCode = token;
                        buildEntries.add(tmpB);
                    }
                }
                
                WriteObject(buildEntries, "BuildOpCode: Build Entries", "/Users/victor/Documents/files/netbeans_workspace/GenAsm/cfg/THUMB/TESTS/output_build_entries.json");

            } else {
                //TODO: throw invalid assemblly line exception
            }
            
        } else if(area.isData) {
            if(!line.isLineEmpty && line.isLineDirective && !line.isLineOpCode) {
                
            } else {
                //TODO: throw invalid assemblly line exception
            }
            
        }
        return null;
    }
}
