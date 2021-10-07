package net.middlemind.GenAsm.Assemblers.Thumb;

import net.middlemind.GenAsm.Assemblers.Symbol;
import net.middlemind.GenAsm.Assemblers.Symbols;
import net.middlemind.GenAsm.Assemblers.Assembler;
import net.middlemind.GenAsm.Tokeners.Thumb.TokenerThumb;
import net.middlemind.GenAsm.Tokeners.TokenLine;
import net.middlemind.GenAsm.Tokeners.Token;
import net.middlemind.GenAsm.Tokeners.TokenSorter;
import net.middlemind.GenAsm.Lexers.ArtifactLine;
import net.middlemind.GenAsm.Lexers.Thumb.LexerThumb;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionRedefinitionOfAreaDirective;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionMissingRequiredDirective;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionNoEntryFound;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionLoader;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionRedefinitionOfLabel;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionMalformedRange;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionNoClosingBracketFound;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionMalformedEntryEndDirectiveSet;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionNoValidLineFound;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionNoAreaDirectiveFound;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionNoTokenerFound;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionNoParentSymbolFound;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionListAndGroup;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionNoOpCodeFound;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionJsonObjLink;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionNoDirectiveFound;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsEntryTypes;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsValidLineEntry;
import net.middlemind.GenAsm.JsonObjs.JsonObjIsSet;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsOpCodeArg;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsEntryType;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsFile;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsValidLine;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsDirective;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsOpCodes;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsOpCode;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsDirectiveArg;
import net.middlemind.GenAsm.JsonObjs.JsonObj;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsValidLines;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsRegisters;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsDirectives;
import net.middlemind.GenAsm.Loaders.Loader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import net.middlemind.GenAsm.Assemblers.AssemblerEventHandler;
import net.middlemind.GenAsm.Assemblers.Thumb.BuildOpCodeEntryThumbSorter.BuildOpCodeEntryThumbSorterType;
import net.middlemind.GenAsm.Assemblers.TwosCompliment;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionDirectiveArgNotSupported;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionInvalidArea;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionInvalidAssemblyLine;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionInvalidEntry;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionMissingDataDirective;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionNoNumberRangeFound;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionNumberInvalidShift;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionNoOpCodeLineFound;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionNoSymbolFound;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionNumberOutOfRange;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionOpCodeAsArgument;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionUnexpectedTokenType;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionUnexpectedTokenWithSubArguments;
import net.middlemind.GenAsm.FileIO.FileLoader;
import net.middlemind.GenAsm.FileIO.FileUnloader;
import net.middlemind.GenAsm.JsonObjs.JsonObjBitSeries;
import net.middlemind.GenAsm.JsonObjs.JsonObjNumRange;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsOpCodeArgSorter;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsRegister;
import net.middlemind.GenAsm.Logger;
import net.middlemind.GenAsm.Tokeners.TokenSorter.TokenSorterType;
import net.middlemind.GenAsm.Utils;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 12:08 PM EST
 */
@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "UseSpecificCatch", "null", "CallToPrintStackTrace", "UnusedAssignment", "Convert2Diamond", "ConvertToStringSwitch"})
public class AssemblerThumb implements Assembler {
    public static String ENDIAN_NAME_BIG = "BIG";
    public static String ENDIAN_NAME_LITTLE = "LITTLE";
    public static String ENDIAN_NAME_JAVA_DEFAULT = "BIG";    
    public static String INSTRUCTION_ALIGNMENT_NAME_WORD = "WORD";
    public static int INSTRUCTION_ALIGNMENT_BYTES = 2;
    public static int INSTRUCTION_ALIGNMENT_BITS = 16;
    public static String NUMBER_SHIFT_NAME_LEFT = "LEFT";
    public static String NUMBER_SHIFT_NAME_RIGHT = "RIGHT";    
    
    public String obj_name = "AssemblerThumb";
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
    public JsonObjNumRange lineNumRange;
    
    public int pcPreFetchBytes;
    public int pcPreFetchWords;
    public int pcPreFetchHalfwords;
    public Integer asmStartLineNumber; 
    public boolean isEndianBig = false;
    public boolean isEndianLittle = true; 
    
    public TokenLine lastLine;
    public Token lastToken;
    public int lastStep;
    public String assemblyTitle;
    public Object other;
    public String rootOutputDir;
    
    public AssemblerEventHandlerThumb eventHandler;
    
    //MAIN METHOD
    @Override
    public void RunAssembler(JsonObjIsSet jsonIsSet, String assemblySourceFile, List<String> assemblySource, String outputDir, Object otherObj, AssemblerEventHandler asmEventHandler) throws Exception {
        try {
            eventHandler = (AssemblerEventHandlerThumb)asmEventHandler;
            if(eventHandler != null) {
                eventHandler.RunAssemblerPre(this, jsonIsSet, assemblySourceFile, assemblySource, outputDir, otherObj);
            }
            
            Logger.wrl("AssemblerThumb: RunAssembler: Start");
            other = otherObj;
            rootOutputDir = outputDir;
            
            jsonSource = new Hashtable<String, String>();
            isaLoader = new Hashtable<String, Loader>();        
            isaData = new Hashtable<String, JsonObj>();
            isaDataSet = jsonIsSet;
            asmSourceFile = assemblySourceFile;
            asmDataSource = assemblySource;
            symbols = new Symbols();

            requiredDirectives = new ArrayList<String>();
            requiredDirectives.add("@AREA");
            requiredDirectives.add("@TTL");            
            requiredDirectives.add("@ENTRY");
            requiredDirectives.add("@END");            

            Logger.wrl("");
            lastStep = 1;
            Logger.wrl("STEP 1: Process JsonObjIsSet's file entries and load then parse the json object data");
            LoadAndParseJsonObjData();

            Logger.wrl("");
            lastStep = 2;
            Logger.wrl("STEP 2: Link loaded json object data");
            LinkJsonObjData();

            Logger.wrl("");
            lastStep = 3;
            Logger.wrl("STEP 3: Load and lexerize the assembly source file");           
            LexerizeAssemblySource();
            Utils.WriteObject(asmDataLexed, "Assembly Lexerized Data", "output_lexed.json", rootOutputDir);        

            Logger.wrl("");
            lastStep = 4;
            Logger.wrl("STEP 4: Tokenize the lexerized artifacts");
            TokenizeLexerArtifacts();
            Utils.WriteObject(asmDataTokened, "Assembly Tokenized Data", "output_tokened_phase0_tokenized.json", rootOutputDir);            

            Logger.wrl("");
            lastStep = 5;
            Logger.wrl("STEP 5: Validate token lines");
            ValidateTokenizedLines();
            Utils.WriteObject(asmDataTokened, "Assembly Tokenized Data", "output_tokened_phase1_valid_lines.json", rootOutputDir);            

            Logger.wrl("");
            lastStep = 6;
            Logger.wrl("STEP 6: Combine comment tokens as children of the initial comment token");
            CollapseCommentTokens();

            Logger.wrl("");
            lastStep = 7;
            Logger.wrl("STEP 7: Expand register ranges into individual register entries");
            ExpandRegisterRangeTokens();

            Logger.wrl("");
            lastStep = 8;
            Logger.wrl("STEP 8: Combine list and group tokens as children of the initial list or group token");
            CollapseListAndGroupTokens();

            Logger.wrl("");
            lastStep = 9;
            Logger.wrl("STEP 9: Mark OpCode, OpCode argument, and register tokens");
            PopulateOpCodeAndArgData();

            Logger.wrl("");
            lastStep = 10;
            Logger.wrl("STEP 10: Mark directive and directive argument tokens, create area based line lists with hex numbering");
            PopulateDirectiveArgAndAreaData();
            Utils.WriteObject(asmDataTokened, "Assembly Tokenized Data", "output_tokened_phase2_refactored.json", rootOutputDir);

            Logger.wrl("");
            lastStep = 11;
            Logger.wrl("STEP 11: Validate OpCode lines against known OpCodes by comparing arguments");
            ValidateOpCodeLines();

            Logger.wrl("");
            lastStep = 12;
            Logger.wrl("STEP 12: Validate directive lines against known directives by comparing arguments");
            ValidateDirectiveLines();
            Utils.WriteObject(asmDataTokened, "Assembly Tokenized Data", "output_tokened_phase3_valid_lines.json", rootOutputDir);            
            Utils.WriteObject(symbols, "Symbol Data", "output_symbols.json", rootOutputDir);

            Logger.wrl("");
            Logger.wrl("List Assembly Source Areas:");
            if(areaThumbCode != null) {
                Logger.wrl("AreaThumbCode: Title: " + areaThumbCode.title);                
                Logger.wrl("AreaThumbCode: AreaLine: " + areaThumbCode.lineNumArea + " EntryLine: " + areaThumbCode.lineNumEntry + " EndLine: " + areaThumbCode.lineNumEnd);
                Logger.wrl("AreaThumbCode: Attributes: IsCode: " + areaThumbCode.isCode + " IsData: " + areaThumbCode.isData + " IsReadOnly: " + areaThumbCode.isReadOnly + " IsReadWrite: " + areaThumbCode.isReadWrite);
                
                Utils.WriteObject(asmAreaLinesCode, "Assembly Source Area Code Lines", "output_area_lines_code.json", rootOutputDir);
                Utils.WriteObject(areaThumbCode, "Assembly Source Area Code Desc", "output_area_desc_code.json", rootOutputDir);
                BuildBinLines(asmAreaLinesCode, areaThumbCode);
                Utils.WriteObject(asmDataTokened, "Assembly Tokenized Data", "output_tokened_phase4_bin_output.json", rootOutputDir);
            } else {
                Logger.wrl("AreaThumbCode: is null");
            }

            if(areaThumbData != null) {
                Logger.wrl("AreaThumbData: Title: " + areaThumbData.title);
                Logger.wrl("AreaThumbData: AreaLine: " + areaThumbData.lineNumArea + " EntryLine: " + areaThumbData.lineNumEntry + " EndLine: " + areaThumbData.lineNumEnd);
                Logger.wrl("AreaThumbData: Attributes: IsCode: " + areaThumbData.isCode + " IsData: " + areaThumbData.isData + " IsReadOnly: " + areaThumbData.isReadOnly + " IsReadWrite: " + areaThumbData.isReadWrite);
                
                Utils.WriteObject(asmAreaLinesData, "Assembly Source Area Data Lines", "output_area_lines_data.json", rootOutputDir);
                Utils.WriteObject(areaThumbData, "Assembly Source Area Data Desc", "output_area_desc_data.json", rootOutputDir);                
                BuildBinLines(asmAreaLinesData, areaThumbData);
                Utils.WriteObject(asmDataTokened, "Assembly Tokenized Data", "output_tokened_phase4_bin_output.json", rootOutputDir);                
            } else {
                Logger.wrl("AreaThumbData: is null");
            }

            Logger.wrl("");
            Logger.wrl("Assembler Meta Data:");
            Logger.wrl("Title: " + assemblyTitle);
            Logger.wrl("LineLengthBytes: " + lineLenBytes);
            Logger.wrl("LineLengthWords: " + lineLenWords);
            Logger.wrl("LineBitSeries:");
            lineBitSeries.Print("\t");
            
            if(eventHandler != null) {
                eventHandler.RunAssemblerPost(this);
            }
            
        } catch(Exception e) {
            Logger.wrlErr("Error in RunAssembler method on step: " + lastStep);
            if(lastLine != null) {
                Logger.wrlErr("Last line processed: " + lastLine.lineNum);
                if(lastLine.source != null) {
                    Logger.wrlErr("Last line source: " + lastLine.source.source);
                }
            } else {
                Logger.wrlErr("Last line processed: unknown");
                Logger.wrlErr("Last line source: unknown");                
            }
            
            if(lastToken != null) {
                Logger.wrlErr("Last token processed: " + lastToken.source + " with index " + lastToken.index + ", type name '" + lastToken.type_name + "', and line number " + lastToken.lineNum);
            } else {
                Logger.wrlErr("Last token processed: unknown");
            }
            throw e;
        }
    }
            
    //DIRECTIVE METHODS
    public void PopulateDirectiveArgAndAreaData() throws ExceptionMissingRequiredDirective, ExceptionRedefinitionOfAreaDirective, ExceptionNoDirectiveFound, ExceptionNoParentSymbolFound, ExceptionMalformedEntryEndDirectiveSet, ExceptionNoAreaDirectiveFound, ExceptionRedefinitionOfLabel {
        if(eventHandler != null) {
            eventHandler.PopulateDirectiveArgAndAreaDataPre(this);
        }
        
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
        
        boolean foundTtl = false;
        boolean foundArea = false;
        boolean foundOrg = false;
        
        String lastLabel = null;
        TokenLine lastLabelLine = null;
        Token lastLabelToken = null;
        Symbol symbol = null;
        
        for(TokenLine line : asmDataTokened) {
            lastLine = line;
            if(eventHandler != null) {
                eventHandler.PopulateDirectiveArgAndAreaDataLoopPre(this, line);
            }
            
            foundOrg = false;
            directiveFound = false;
            directiveName = null;
            directiveIdx = -1;            

            lastLabel = null;
            lastLabelLine = null;
            lastLabelToken = null;
            symbol = null;            
            
            if(lastData != -1 && line.isLineOpCode) {
                throw new ExceptionMalformedEntryEndDirectiveSet("Cannot have OpCode instructions when AREA type is DATA, found on line " + line.lineNum + " with source " + line.source.source);                
            }
            
            for(Token token : line.payload) {
                lastToken = token;

                if(foundTtl && token.type_name.equals(JsonObjIsDirectives.NAME_DIRECTIVE_TYPE_STRING)) {
                    foundTtl = false;
                    assemblyTitle = token.source;
                
                } else if(foundArea && token.type_name.equals(JsonObjIsDirectives.NAME_DIRECTIVE_TYPE_STRING)) {
                    tmpArea.title = token.source;
                    
                } else if(token.type_name.equals(JsonObjIsEntryTypes.NAME_LABEL)) {
                    if(token.index == 0) {
                        lastLabel = token.source;
                        lastLabelLine = line;
                        lastLabelToken = token;
                    }                    
                    
                } else if(token.type_name.equals(JsonObjIsEntryTypes.NAME_NUMBER)) {
                    if(directiveFound == true && foundArea == true) {
                        token.isDirectiveArg = true;
                    }
                    //else {
                    //    throw new ExceptionNoAreaDirectiveFound("Could not find AREA directive or line directive before NUMBER on line " + line.lineNum + " with source " + line.source.source + ", directive found: " + directiveFound + ", found area: " + foundArea + ", last area: " + lastArea);
                    //}
                    
                    if(lastLabelToken != null && symbol != null) {
                        symbol.value = Utils.ParseNumberString(token.source);
                        symbols.symbols.put(lastLabel, symbol);
                        Logger.wrl("AssemblerThumb: PopulateDirectiveArgAndAreaData: Storing symbol with label '" + lastLabel + "' for line number " + lastLabelLine.lineNum);
                        
                        lastLabel = null;
                        lastLabelToken = null;
                        lastLabelLine = null;
                        symbol = null;
                    
                    } else if(foundOrg == true) {
                        asmStartLineNumber = Utils.ParseNumberString(token.source);
                        foundOrg = false;
                    }
                    
                } else if(token.type_name.equals(JsonObjIsEntryTypes.NAME_DIRECTIVE)) {
                    if(!directiveFound) {
                        directiveFound = true;
                        directiveName = token.source;
                        directiveIdx = token.index;
                    }
                 
                    if(reqDirectives.contains(token.source)) {
                        reqDirectives.remove(token.source);
                        reqDirectiveCount--;
                    }
                                                                    
                    if(token.source.equals(JsonObjIsDirectives.NAME_TITLE)) {
                        foundTtl = true;
                        
                    } else if(token.source.equals(JsonObjIsDirectives.NAME_ORG)) {                        
                        foundOrg = true;
                        
                    } else if(token.source.equals(JsonObjIsDirectives.NAME_EQU)) {
                        if(symbols.symbols.containsKey(lastLabel)) {
                            throw new ExceptionRedefinitionOfLabel("Found symbol '" + lastLabel + "' redefined on line " + lastLabelLine.lineNum + " originally defned on line " + (symbols.symbols.get(lastLabel)).lineNum);
                        }
                        symbol = new Symbol();
                        symbol.line = line;
                        symbol.lineNum = line.lineNum;
                        symbol.name = lastLabel;
                        symbol.token = lastLabelToken;
                        symbol.isStaticValue = true;
                        
                    } else if(token.source.equals(JsonObjIsDirectives.NAME_AREA)) {
                        if(lastArea == -1) {
                            foundArea = true;
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
                        
                    } else if(token.source.equals(JsonObjIsDirectives.NAME_CODE)) {
                        if(lastCode == -1) {
                            lastCode = line.lineNum;
                        }
                        tmpArea.isCode = true;
                        tmpArea.isReadOnly = true;
                        if(lastData != -1 && lastData == lastCode) {
                            throw new ExceptionMalformedEntryEndDirectiveSet("Cannot set AREA type to CODE when type is DATA, found on line " + line.lineNum + " with source " + line.source.source);
                        }
                        
                    } else if(token.source.equals(JsonObjIsDirectives.NAME_DATA)) {
                       if(lastData == -1) {
                            lastData = line.lineNum;
                        }                        
                        tmpArea.isData = true;
                        tmpArea.isReadWrite = true;
                        if(lastCode != -1 && lastCode == lastData) {
                            throw new ExceptionMalformedEntryEndDirectiveSet("Cannot set AREA type to DATA when type is CODE, found on line " + line.lineNum + " with source " + line.source.source);
                        }
                        
                    } else if(token.source.equals(JsonObjIsDirectives.NAME_READONLY)) {
                        if(lastReadOnly == -1) {
                            lastReadOnly = line.lineNum;
                        }
                        tmpArea.isReadOnly = true;
                        tmpArea.isReadWrite = false;
                        if(lastReadOnly != -1 && lastReadOnly == lastReadWrite) {
                            throw new ExceptionMalformedEntryEndDirectiveSet("Cannot set AREA type to READONLY when type is READWRITE, found on line " + line.lineNum + " with source " + line.source.source);
                        }
                        
                    } else if(token.source.equals(JsonObjIsDirectives.NAME_READWRITE)) {
                        if(lastReadWrite == -1) {
                            lastReadWrite = line.lineNum;
                        }                        
                        tmpArea.isReadWrite = true;
                        tmpArea.isReadOnly = false;
                        if(lastReadWrite != -1 && lastReadWrite == lastReadOnly) {
                            throw new ExceptionMalformedEntryEndDirectiveSet("Cannot set AREA type to READWRITE when type is READONLY, found on line " + line.lineNum + " with source " + line.source.source);
                        }
                        
                    } else if(token.source.equals(JsonObjIsDirectives.NAME_ENTRY)) {
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
                        
                    } else if(token.source.equals(JsonObjIsDirectives.NAME_END)) {
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
                            
                            foundArea = false;
                        } else {
                            throw new ExceptionMalformedEntryEndDirectiveSet("Could not find END directive before new ENTRY directive on line " + line.lineNum + " with source " + line.source.source);
                        }
                    } else if(token.source.equals(JsonObjIsDirectives.NAME_DCHW) || token.source.equals(JsonObjIsDirectives.NAME_DCB)) {
                        if(lastArea == -1 || tmpArea == null) { // || (tmpArea != null && (tmpArea.isCode == true || tmpArea.isData == false))) {
                            throw new ExceptionNoAreaDirectiveFound("Could not find DATA AREA directive before directive '" + token.source + "' on line " + line.lineNum + " with source " + line.source.source);
                        } else {
                            token.isDirective = true;
                            if(!directiveFound) {
                                directiveFound = true;
                                directiveName = token.source;
                                directiveIdx = token.index;
                            }                            
                        }                       
                    }
                }
                
                if(directiveFound) {
                    line.payloadDirective = directiveName;
                    line.isLineDirective = true;

                    //Allow for Directive args and OpCode args since we can't define two Entry Types that are identical but have different categories
                    line.payloadLenArg = CountArgTokens(line.payload, 0, JsonObjIsEntryTypes.NAME_CAT_ARG_DIRECTIVE, false) + CountArgTokens(line.payload, 0, JsonObjIsEntryTypes.NAME_CAT_ARG_OPCODE, false);
                    line.matchesDirective = FindDirectiveMatches(line.payloadDirective, line.payloadLenArg);
                }
            }
            
            if(directiveFound) {                
                if(line.matchesDirective == null || line.matchesDirective.isEmpty()) {
                    throw new ExceptionNoDirectiveFound("Could not find a matching directive entry for name '" + line.payloadDirective + "' with argument count " + line.payloadLenArg + " at line " + line.lineNum + " with source text '" + line.source.source + "'");
                }
            }
            
            if(eventHandler != null) {
                eventHandler.PopulateDirectiveArgAndAreaDataLoopPost(this, line);
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
                        line.lineNumHex = Utils.FormatHexString(Integer.toHexString(activeLineCount), lineLenBytes);
                        line.lineNumBin = Utils.FormatBinString(Integer.toBinaryString(activeLineCount), lineBitSeries.bit_len);
                        asmAreaLinesCode.add(line);
                        activeLineCount += lineLenBytes;
                    }
                }
                
                asmAreaLinesData = new ArrayList<TokenLine>();
                for(int z = areaThumbData.lineNumEntry + 1; z < areaThumbData.lineNumEnd; z++) {
                    TokenLine line = asmDataTokened.get(z);
                    if(line.isLineDirective && !line.isLineOpCode && !line.isLineEmpty) {
                        line.lineNumHex = Utils.FormatHexString(Integer.toHexString(activeLineCount), lineLenBytes);
                        line.lineNumBin = Utils.FormatBinString(Integer.toBinaryString(activeLineCount), lineBitSeries.bit_len); 
                        asmAreaLinesData.add(line);
                        activeLineCount += lineLenBytes;
                    }
                }
            } else {
                //process data area first
                asmAreaLinesData = new ArrayList<TokenLine>();
                activeLineCount = 0;
                for(int z = areaThumbData.lineNumEntry + 1; z < areaThumbData.lineNumEnd; z++) {
                    TokenLine line = asmDataTokened.get(z);
                    if(line.isLineDirective && !line.isLineOpCode && !line.isLineEmpty) {
                        line.lineNumHex = Utils.FormatHexString(Integer.toHexString(activeLineCount), lineLenBytes);
                        line.lineNumBin = Utils.FormatBinString(Integer.toBinaryString(activeLineCount), lineBitSeries.bit_len); 
                        asmAreaLinesData.add(line);
                        activeLineCount += lineLenBytes;
                    }
                }
                
                asmAreaLinesCode = new ArrayList<TokenLine>();
                for(int z = areaThumbCode.lineNumEntry + 1; z < areaThumbCode.lineNumEnd; z++) {
                    TokenLine line = asmDataTokened.get(z);
                    if(line.isLineOpCode && !line.isLineEmpty && !line.isLineDirective) {
                        line.lineNumHex = Utils.FormatHexString(Integer.toHexString(activeLineCount), lineLenBytes);
                        line.lineNumBin = Utils.FormatBinString(Integer.toBinaryString(activeLineCount), lineBitSeries.bit_len); 
                        asmAreaLinesCode.add(line);
                        activeLineCount += lineLenBytes;
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
                    line.lineNumHex = Utils.FormatHexString(Integer.toHexString(activeLineCount), lineLenBytes);
                    line.lineNumBin = Utils.FormatBinString(Integer.toBinaryString(activeLineCount), lineBitSeries.bit_len);
                    asmAreaLinesCode.add(line);
                    activeLineCount += lineLenBytes;
                }
            }
            
        } else {
            throw new ExceptionMalformedEntryEndDirectiveSet("Cannot have only a DATA AREA, CODE AREA is required");
        }
        
        if(eventHandler != null) {
            eventHandler.PopulateDirectiveArgAndAreaDataPost(this);
        }        
    }    
    
    public void ValidateDirectiveLines() throws ExceptionNoDirectiveFound {
        if(eventHandler != null) {
            eventHandler.ValidateDirectiveLinesPre(this);
        }        
        
        Logger.wrl("AssemblerThumb: ValidateDirectiveLines"); 
        boolean directiveFound = false;
        String directiveName = null;
        Token directiveToken = null;
        int directiveIdx = -1;
        List<Token> args = null;
        JsonObjIsDirective directive = null; 
        
        for(TokenLine line : asmDataTokened) {
            lastLine = line;
            if(eventHandler != null) {
                eventHandler.ValidateDirectiveLinesLoopPre(this, line);
            }            
            
            if(line.isLineDirective) {
                directiveFound = false;
                directiveName = null;
                directiveToken = null;
                directiveIdx = -1;
                args = null;
                
                for(Token token : line.payload) {
                    lastToken = token;
                    if(!directiveFound) {
                        if(token.type_name.equals(JsonObjIsEntryTypes.NAME_DIRECTIVE)) {
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
                
                if((line.matchesDirective != null && line.matchesDirective.size() > 1) || Utils.IsListEmpty(line.matchesDirective)) {
                    throw new ExceptionNoDirectiveFound("Could not find unique matching directive entry for directive '" + directiveName + "' and line number " + line.lineNum + " with source '" + line.source.source + "'");
                }
            }
            
            if(eventHandler != null) {
                eventHandler.ValidateDirectiveLinesLoopPost(this, line);
            }            
        }
        
        if(eventHandler != null) {
            eventHandler.ValidateDirectiveLinesPost(this);
        }
    }
    
    public JsonObjIsDirective FindDirectiveArgMatches(List<JsonObjIsDirective> directiveMatches, List<Token> args, Token directiveToken) throws ExceptionNoDirectiveFound {
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
    
    public List<JsonObjIsDirective> FindDirectiveMatches(String directiveName, int argLen) {
        List<JsonObjIsDirective> ret = new ArrayList<>();
        for(JsonObjIsDirective directive : jsonObjIsDirectives.is_directives) {
            if(directive.directive_name.equals(directiveName) && directive.arg_len == argLen) {
                ret.add(directive);
            }
        }
        return ret;
    }
    
    public Token FindDirectives(TokenLine line) {
        for(Token token : line.payload) {
            if(token.type_name.equals(JsonObjIsEntryTypes.NAME_DIRECTIVE)) {
                return token;
            }
        }
        return null;
    }
    
    //OPCODE METHODS
    public void PopulateOpCodeAndArgData() throws ExceptionRedefinitionOfLabel, ExceptionNoOpCodeFound, ExceptionNoParentSymbolFound, ExceptionOpCodeAsArgument, ExceptionMissingRequiredDirective {
        if(eventHandler != null) {
            eventHandler.PopulateOpCodeAndArgDataPre(this);
        }
        
        Logger.wrl("AssemblerThumb: PopulateOpCodeAndArgData");        
        boolean opCodeFound = false;
        String opCodeName = null;
        int opCodeIdx = -1;
        int labelArgs = -1;
        String lastLabel = null;
        TokenLine lastLabelLine = null;
        Symbol symbol = null;
        
        for(TokenLine line : asmDataTokened) {
            lastLine = line;
            if(eventHandler != null) {
                eventHandler.PopulateOpCodeAndArgDataLoopPre(this, line);
            }             
            
            opCodeFound = false;
            opCodeName = null;
            opCodeIdx = -1;
            labelArgs = 0;
            
            for(Token token : line.payload) {
                lastToken = token;
                //MATCH REGISTERS
                if(Utils.ArrayContainsString(JsonObjIsEntryTypes.NAME_REGISTERS, token.type_name)) {
                    String regCode = token.source;
                    if(token.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTERWB)) {
                        regCode = regCode.replace("!", "");
                    }
                    regCode = regCode.replace(" ", "");
                    regCode = regCode.replace(",", "");
                    
                    for(JsonObjIsRegister register : jsonObjIsRegisters.is_registers) {
                        if(register.register_name.equals(regCode)) {
                            token.register = register;
                            token.isOpCodeArg = true;
                            break;
                        }
                    }
                }
                
                if(token.type_name.equals(JsonObjIsEntryTypes.NAME_OPCODE)) {
                    if(!opCodeFound) {
                        opCodeFound = true;
                        opCodeName = token.source;
                        opCodeIdx = token.index;
                    } else {
                        throw new ExceptionOpCodeAsArgument("Found OpCode token entry where a sub-argument should be on line " + line.lineNum + " with argument index " + token.index);
                    }
                    
                } else if(token.type_name.equals(JsonObjIsEntryTypes.NAME_START_LIST) || token.type_name.equals(JsonObjIsEntryTypes.NAME_START_GROUP) || token.type_name.equals(JsonObjIsEntryTypes.NAME_STOP_LIST) || token.type_name.equals(JsonObjIsEntryTypes.NAME_STOP_GROUP)) {                    
                    token.isOpCodeArg = true;
                    
                //} else if(token.type_name.equals(JsonObjIsEntryTypes.NAME_LABEL_NUMERIC_LOCAL_REF) || token.type_name.equals(JsonObjIsEntryTypes.NAME_LABEL_REF)) {
                //    token.isOpCodeArg = true;                    
                    
                } else if(token.type_name.equals(JsonObjIsEntryTypes.NAME_LABEL)) {                    
                    if(token.index == 0) {
                        Token ltTmp = FindDirectives(line);
                        if(ltTmp == null || (ltTmp != null && Utils.ArrayContainsString(JsonObjIsDirectives.LABEL_DIRECTIVES, ltTmp.source) == true)) {
                            if(ltTmp == null || (ltTmp != null && ltTmp.source.equals(JsonObjIsDirectives.NAME_EQU) == false)) {
                                lastLabel = token.source;
                                lastLabelLine = line;
                                if(symbols.symbols.containsKey(lastLabel)) {
                                    throw new ExceptionRedefinitionOfLabel("Found symbol '" + lastLabel + "' redefined on line " + lastLabelLine.lineNum + " originally defned on line " + (symbols.symbols.get(lastLabel)).lineNum);
                                } else {
                                    Logger.wrl("AssemblerThumb: PopulateOpCodeAndArgData: Storing symbol with label '" + token.source + "' for line number " + line.lineNum);
                                }
                                symbol = new Symbol();
                                symbol.line = line;
                                symbol.lineNum = line.lineNum;
                                symbol.name = token.source;
                                symbol.token = token;
                                symbol.isLabel = true;
                                symbols.symbols.put(token.source, symbol);
                            }
                            
                        } else {
                            throw new ExceptionMissingRequiredDirective("Found symbol '" + lastLabel + "' found on line " + lastLabelLine.lineNum + " is missing required directive EQU that is expected when no OpCode is used.");
                            
                        }
                    }
                /*    
                } else if(token.type_name.equals(JsonObjIsEntryTypes.NAME_LABEL_NUMERIC_LOCAL)) {                    
                    if(Utils.IsStringEmpty(lastLabel)) {
                        throw new ExceptionNoParentSymbolFound("No parent label found for local symbol '" + token.source + "' on line " + token.lineNum + ", could not find parent label to associate with local label.");
                    } else {
                        if(symbols.symbols.containsKey(lastLabel)) {
                            symbol = symbols.symbols.get(lastLabel);
                            if(symbol.symbols.containsKey(token.source)) {
                                throw new ExceptionRedefinitionOfLabel("Found symbol '" + token.source + "' redefined on line " + token.lineNum + " originally defned on line " + (symbol.symbols.get(token.source)).lineNum);
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
                            lsymbol.isLocalLabel = true;
                            
                            symbol.isParentLabel = true;
                            symbol.symbols.put(token.source, lsymbol);
                            
                        } else {
                            throw new ExceptionNoParentSymbolFound("Could not find a parent symbol for label '" + token.source + "' at line " + line.lineNum + " with source text '" + line.source.source + "'");
                        }
                    }
                */
                }

                //Process sub args
                for(Token ltoken : token.payload) {
                    if(Utils.ArrayContainsString(JsonObjIsEntryTypes.NAME_REGISTERS, ltoken.type_name)) {
                        String regCode = ltoken.source;
                        if(ltoken.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTERWB)) {
                            regCode = regCode.replace("!", "");
                        }
                        regCode = regCode.replace(" ", "");
                        regCode = regCode.replace(",", "");

                        for(JsonObjIsRegister register : jsonObjIsRegisters.is_registers) {
                            if(register.register_name.equals(regCode)) {
                                ltoken.register = register;
                                ltoken.isOpCodeArg = true;
                                break;
                            }
                        }
                    }

                    if(ltoken.type_name.equals(JsonObjIsEntryTypes.NAME_OPCODE)) {
                        throw new ExceptionOpCodeAsArgument("Found OpCode token entry where a sub-argument should be on line " + line.lineNum + " with argument index " + ltoken.index + " and parent argument index " + token.index);

                    //} else if(ltoken.type_name.equals(JsonObjIsEntryTypes.NAME_LABEL_REF) || ltoken.type_name.equals(JsonObjIsEntryTypes.NAME_LABEL_NUMERIC_LOCAL_REF) || ltoken.type_name.equals(JsonObjIsEntryTypes.NAME_START_LIST) || ltoken.type_name.equals(JsonObjIsEntryTypes.NAME_START_GROUP) || ltoken.type_name.equals(JsonObjIsEntryTypes.NAME_STOP_LIST) || ltoken.type_name.equals(JsonObjIsEntryTypes.NAME_STOP_GROUP)) {
                    } else if(ltoken.type_name.equals(JsonObjIsEntryTypes.NAME_LABEL_REF) || ltoken.type_name.equals(JsonObjIsEntryTypes.NAME_START_LIST) || ltoken.type_name.equals(JsonObjIsEntryTypes.NAME_START_GROUP) || ltoken.type_name.equals(JsonObjIsEntryTypes.NAME_STOP_LIST) || ltoken.type_name.equals(JsonObjIsEntryTypes.NAME_STOP_GROUP)) {                    
                        ltoken.isOpCodeArg = true;

                    } else if(ltoken.type_name.equals(JsonObjIsEntryTypes.NAME_LABEL)) {
                        if(ltoken.index == 0) {
                            lastLabel = ltoken.source;
                            lastLabelLine = line;
                            if(symbols.symbols.containsKey(ltoken.source)) {
                                throw new ExceptionRedefinitionOfLabel("Found symbol '" + ltoken.source + "' redefined on line " + ltoken.lineNum + " originally defned on line " + symbol.lineNum);
                            } else {
                                Logger.wrl("AssemblerThumb: PopulateOpCodeAndArgData: Storing symbol with label '" + ltoken.source + "' for line number " + line.lineNum);
                            }
                            symbol = new Symbol();
                            symbol.line = line;
                            symbol.lineNum = line.lineNum;
                            symbol.name = ltoken.source;
                            symbol.token = ltoken;
                            symbols.symbols.put(ltoken.source, symbol);
                        }

                    /*
                    } else if(ltoken.type_name.equals(JsonObjIsEntryTypes.NAME_LABEL_NUMERIC_LOCAL)) {                    
                        if(Utils.IsStringEmpty(lastLabel)) {
                            throw new ExceptionNoParentSymbolFound("No parent label found for local symbol '" + ltoken.source + "' on line " + ltoken.lineNum + ", could not find parent label to associate with local label.");
                        } else {
                            if(symbols.symbols.containsKey(lastLabel)) {
                                symbol = symbols.symbols.get(lastLabel);
                                if(symbol.symbols.containsKey(ltoken.source)) {
                                    throw new ExceptionRedefinitionOfLabel("Found symbol '" + ltoken.source + "' redefined on line " + ltoken.lineNum + " originally defned on line " + symbol.lineNum);
                                } else {
                                    Logger.wrl("AssemblerThumb: PopulateOpCodeAndArgData: Storing local symbol with label '" + ltoken.source + "' for line number " + line.lineNum + " and parent label '" + lastLabel + "'");
                                }
                                ltoken.parentLabel = lastLabel;
                                ltoken.parentLine = lastLabelLine;

                                Symbol lsymbol = new Symbol();
                                lsymbol.line = line;
                                lsymbol.lineNum = line.lineNum;
                                lsymbol.name = ltoken.source;
                                lsymbol.token = ltoken;                            
                                symbol.symbols.put(ltoken.source, lsymbol);

                            } else {
                                throw new ExceptionNoParentSymbolFound("Could not find a parent symbol for label '" + ltoken.source + "' at line " + line.lineNum + " with source text '" + line.source.source + "'");
                            }
                        }
                    */
                    }
                }
            }
            
            if(opCodeFound) {
                line.payloadOpCode = opCodeName;
                line.isLineOpCode = true;
                line.payloadLenArg = CountArgTokens(line.payload, 0); // + labelArgs;  /* No longer needed */
                line.matchesOpCode = FindOpCodeMatches(line.payloadOpCode, line.payloadLenArg);
                
                if(line.matchesOpCode == null || line.matchesOpCode.isEmpty()) {
                    throw new ExceptionNoOpCodeFound("Could not find a matching opCode entry for name '" + line.payloadOpCode + "' with argument count " + line.payloadLenArg + " at line " + line.lineNum + " with source text '" + line.source.source + "'");
                }
            }
            
            if(eventHandler != null) {
                eventHandler.PopulateOpCodeAndArgDataLoopPost(this, line);
            }            
        }
        
        if(eventHandler != null) {
            eventHandler.PopulateOpCodeAndArgDataPost(this);
        }        
    }    
    
    public void ValidateOpCodeLines() throws ExceptionNoOpCodeFound, ExceptionNoOpCodeLineFound {
        if(eventHandler != null) {
            eventHandler.ValidateOpCodeLinesPre(this);
        }
        
        Logger.wrl("AssemblerThumb: ValidateOpCodeLines"); 
        boolean opCodeFound = false;
        String opCodeName = null;
        Token opCodeToken = null;
        int opCodeIdx = -1;
        List<Token> args = null;
        JsonObjIsOpCode opCode = null; 
        
        for(TokenLine line : asmDataTokened) {
            lastLine = line;
            if(eventHandler != null) {
                eventHandler.ValidateOpCodeLinesLoopPre(this, line);
            }
            
            if(line.isLineOpCode) {
                opCodeFound = false;
                opCodeName = null;
                opCodeToken = null;
                opCodeIdx = -1;
                args = null;
                
                for(Token token : line.payload) {
                    lastToken = token;
                    if(!opCodeFound) {
                        if(token.type_name.equals(JsonObjIsEntryTypes.NAME_OPCODE)) {
                            opCodeFound = true;
                            opCodeName = token.source;
                            opCodeToken = token;
                            opCodeIdx = token.index;
                            args = new ArrayList<>();
                        }
                    } else {
                        args.add(token);
                    }
                    
                    //Adjust if label is defined or referenced
                    if(opCodeFound && (token.type_name.equals(JsonObjIsEntryTypes.NAME_LABEL_REF))) {
                        token.isLabelRef = true;
                        token.isLabel = false;
                    }
                }
                
                if(opCodeFound && args != null && args.size() > 0) {
                    opCode = FindOpCodeArgMatches(line.matchesOpCode, args, opCodeToken);
                    line.matchesOpCode.clear();
                    line.matchesOpCode.add(opCode);
                }
                
                if((line.matchesOpCode != null && line.matchesOpCode.size() > 1) || Utils.IsListEmpty(line.matchesOpCode)) {
                    throw new ExceptionNoOpCodeFound("Could not find unique matching opCode entry for opCode '" + opCodeName + "' and line number " + line.lineNum + " with source '" + line.source.source + "'");
                }
            }
            
            if(eventHandler != null) {
                eventHandler.ValidateOpCodeLinesLoopPost(this, line);
            }            
        }
        
        for(String key : symbols.symbols.keySet()) {
            Symbol symbol = symbols.symbols.get(key);
            TokenLine line = asmDataTokened.get(symbol.lineNum);
            if(Utils.ArrayContainsInt(JsonObjIsValidLines.LINES_LABEL_EMPTY, line.validLineEntry.index)) {    
                symbol.lineNumActive = FindNextOpCodeLine(symbol.lineNum, key);
                symbol.isEmptyLineLabel = true;
                Logger.wrl("Adjusting symbol line number from " + symbol.lineNum + " to " + symbol.lineNumActive + " due to symbol marking an empty line");
            } else {
                symbol.lineNumActive = symbol.lineNum;
            }
        }
        
        if(eventHandler != null) {
            eventHandler.ValidateOpCodeLinesPost(this);
        }        
    }
    
    public int FindNextOpCodeLine(int lineNum, String label) throws ExceptionNoOpCodeLineFound {
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
    
    public JsonObjIsOpCode FindOpCodeArgMatches(List<JsonObjIsOpCode> opCodeMatches, List<Token> args, Token opCodeToken) throws ExceptionNoOpCodeFound {
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
                    argFound = false;
                    break;
                }
                                
                if(opCodeArg != null && argToken != null) {
                    //if(!(argToken.isLabelRef || argToken.isLabelLocalRef || opCodeArg.is_entry_types.contains(argToken.type_name))) {
                    if(!(argToken.isLabelRef || opCodeArg.is_entry_types.contains(argToken.type_name))) {                    
                        argFound = false;
                        break;
                    }
                                                            
                    if(opCodeArg.sub_args != null && opCodeArg.sub_args.size() > 0) {
                        opCodeArgSub = null;
                        argTokenSub = null;
                        argFoundSub = true;
                        hasArgsSub = true;
                        int regRangeOffset = 0;
                        
                        for(int j = 0; j < opCodeArg.sub_args.size(); j++) {
                            opCodeArgSub = opCodeArg.sub_args.get(j);
                            opCodeArgIdxSub = j;
                            
                            if(argToken.payload != null && (j + regRangeOffset) < argToken.payload.size()) {
                                argTokenSub = argToken.payload.get(j + regRangeOffset);
                            } else {
                                argFound = false;
                                argFoundSub = false;
                                break;
                            }
                            
                            if(opCodeArgSub != null && argTokenSub != null) {
                                if(opCodeArgSub.is_entry_types.contains("RegisterRangeLow") && argTokenSub.type_name.equals("RegisterLow")) {
                                    //regRangeOffset = 1;
                                    for(int k = j + 1; k < argToken.payload.size(); k++) {
                                        if(argToken.payload.get(k).type_name.equals("RegisterLow")) {
                                            regRangeOffset++;
                                        }
                                    }
                                } else if(opCodeArgSub.is_entry_types.contains("RegisterRangeHi") && argTokenSub.type.type_category.equals("RegisterHi")) {
                                    //regRangeOffset = 1;
                                    for(int k = j + 1; k < argToken.payload.size(); k++) {
                                        if(argToken.payload.get(k).type.type_category.equals("RegisterHi")) {
                                            regRangeOffset++;
                                        }
                                    }
                                } else {
                                    //if(!(argTokenSub.isLabelRef || argTokenSub.isLabelLocalRef || opCodeArgSub.is_entry_types.contains(argTokenSub.type_name))) {
                                    if(!(argTokenSub.isLabelRef || opCodeArgSub.is_entry_types.contains(argTokenSub.type_name))) {
                                        argFound = false;
                                        argFoundSub = false;
                                        break;
                                    }
                                }
                            } else {
                                argFound = false;
                                argFoundSub = false;
                                break;
                            }
                        }
                        
                        if(regRangeOffset > 0) {
                            //Register range offset is lower by 1 to allow for RegisterRange type, -1 for list close
                            if((argToken.payload.size() - regRangeOffset - 1) != opCodeArg.sub_args.size()) {
                                argFound = false;
                                argFoundSub = false;
                                break;
                            }
                        } else {
                            //-1 for group close
                            if((argToken.payload.size() - 1) != opCodeArg.sub_args.size()) {
                                argFound = false;
                                argFoundSub = false;
                                break;
                            }                            
                        }
                    }
                } else {
                    argFound = false;
                    argFoundSub = false;
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
        return CountArgTokens(payload, argCount, JsonObjIsEntryTypes.NAME_CAT_ARG_OPCODE, true);
    }
    
    public int CountArgTokens(List<Token> payload, int argCount, String argCategory, boolean isOpCodeArg) {
        for(Token token : payload) {
            //lastToken = token;
            if(token.type != null && ((JsonObjIsEntryType)token.type).category.equals(argCategory)) {
                argCount++;
                if(isOpCodeArg) {
                    token.isOpCodeArg = true;
                } else {
                    token.isDirectiveArg = true;
                }
            }
            
            if(token.payload != null && token.payload.size() > 0) {
                //List needs to be counted as an OpCode argument but Group does not
                if(token.type_name.equals(JsonObjIsEntryTypes.NAME_START_LIST)) {
                    argCount++;
                    if(isOpCodeArg) {
                        token.isOpCodeArg = true;
                    } else {
                        token.isDirectiveArg = true;
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
        if(eventHandler != null) {
            eventHandler.CollapseListAndGroupTokensPre(this);
        }        
        
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
            lastLine = line;
            if(eventHandler != null) {
                eventHandler.CollapseListAndGroupTokensLoopPre(this, line);
            }            
            
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
                lastToken = token;
                if(token.type_name.equals(JsonObjIsEntryTypes.NAME_START_LIST)) {
                    rootStartList = token;
                    rootStartIdxList = rootStartList.index;
                    
                } else if(token.type_name.equals(JsonObjIsEntryTypes.NAME_START_GROUP)) {                    
                    rootStartGroup = token;
                    rootStartIdxGroup = rootStartGroup.index;
                    
                } else if(token.type_name.equals(JsonObjIsEntryTypes.NAME_STOP_LIST)) {
                    rootStopList = token;
                    rootStopIdxList = rootStopList.index;
                    
                } else if(token.type_name.equals(JsonObjIsEntryTypes.NAME_STOP_GROUP)) {                    
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
                    lastToken = token;
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
                    lastToken = token;
                    token.index = count;
                    count++;
                }                
            }
            
            if(eventHandler != null) {
                eventHandler.CollapseListAndGroupTokensLoopPost(this, line);
            }            
        }
        
        if(eventHandler != null) {
            eventHandler.CollapseListAndGroupTokensPost(this);
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
    
    public void ExpandRegisterRangeTokens() throws ExceptionNoEntryFound, ExceptionMalformedRange {
        if(eventHandler != null) {
            eventHandler.ExpandRegisterRangeTokensPre(this);
        }
        
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
            lastLine = line;
            if(eventHandler != null) {
                eventHandler.ExpandRegisterRangeTokensLoopPre(this, line);
            }            
            
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
            entryTypeRegLow = FindEntryType(JsonObjIsEntryTypes.NAME_REGISTER_LOW);
            entryTypeRegHi = FindEntryType(JsonObjIsEntryTypes.NAME_REGISTER_HI);                

            for(Token token : line.payload) {
                lastToken = token;
                CleanTokenSource(token);
                
                if(token.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_RANGE_LOW)) {
                    rangeRootLow = token;
                    rangeRootIdxLow = rangeRootLow.index;
                    rangeStr = rangeRootLow.source;
                    rangeStr = CleanRegisterRangeString(rangeStr, JsonObjIsRegisters.CHAR_RANGE);
                    range = Utils.GetIntsFromRange(rangeStr, JsonObjIsRegisters.CHAR_RANGE);
                    count = 0;
                    
                    for(i = range[0]; i <= range[1]; i++) {
                        newToken = new Token();
                        newToken.artifact = rangeRootLow.artifact;
                        newToken.index = rangeRootLow.index + count;
                        newToken.lineNum = rangeRootLow.lineNum;
                        newToken.payload = new ArrayList<>();
                        newToken.payloadLen = 0;
                        newToken.source = JsonObjIsRegisters.CHAR_START + i;
                        newToken.type = entryTypeRegLow;
                        newToken.type_name = entryTypeRegLow.type_name;  
                        newToken.isOpCodeArg = true;
                        rangeAddTokensLow.add(newToken);
                        count++;
                    }
                    
                } else if(token.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_RANGE_HI)) {
                    rangeRootHi = token;
                    rangeRootIdxHi = rangeRootHi.index;
                    rangeStr = rangeRootHi.source;
                    rangeStr = CleanRegisterRangeString(rangeStr, JsonObjIsRegisters.CHAR_RANGE);
                    range = Utils.GetIntsFromRange(rangeStr, JsonObjIsRegisters.CHAR_RANGE);
                    count = 0;
                    
                    for(i = range[0]; i <= range[1]; i++) {
                        newToken = new Token();
                        newToken.artifact = rangeRootHi.artifact;
                        newToken.index = rangeRootHi.index + count;
                        newToken.lineNum = rangeRootHi.lineNum;
                        newToken.payload = new ArrayList<>();
                        newToken.payloadLen = 0;
                        newToken.source = JsonObjIsRegisters.CHAR_START + i;
                        newToken.type = entryTypeRegHi;
                        newToken.type_name = entryTypeRegHi.type_name;
                        newToken.isOpCodeArg = true;
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
                    lastToken = token;
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
                    lastToken = token;
                    token.index = count;
                    count++;
                }                    
            }
            
            if(eventHandler != null) {
                eventHandler.ExpandRegisterRangeTokensLoopPost(this, line);
            }            
        }
        
        if(eventHandler != null) {
            eventHandler.ExpandRegisterRangeTokensPost(this);
        }        
    }
    
    public void CollapseCommentTokens() {
        if(eventHandler != null) {
            eventHandler.CollapseCommentTokensPre(this);
        }
        
        Logger.wrl("AssemblerThumb: CollapseCommentTokens");
        boolean inComment = false;
        Token commentRoot = null;
        List<Token> clearTokens = null;  
            
        for(TokenLine line : asmDataTokened) {
            lastLine = line;
            if(eventHandler != null) {
                eventHandler.CollapseCommentTokensLoopPre(this, line);
            }            
            
            inComment = false;
            commentRoot = null;
            clearTokens = new ArrayList<>();  
            
            for(Token token : line.payload) {
                lastToken = token;
                if(token.type_name.equals(JsonObjIsEntryTypes.NAME_COMMENT)) {
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
            
            if(eventHandler != null) {
                eventHandler.CollapseCommentTokensLoopPost(this, line);
            }            
        }
        
        if(eventHandler != null) {
            eventHandler.CollapseCommentTokensPost(this);
        }        
    }
    
    //LOAD, PARSE, LINK JSONOBJ DATA METHODS
    public void LoadAndParseJsonObjData() throws ExceptionNoEntryFound, ExceptionLoader, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if(eventHandler != null) {
            eventHandler.LoadAndParseJsonObjDataPre(this);
        }
        
        Logger.wrl("AssemblerThumb: LoadAndParseJsonObjData");
        Class cTmp = null;
        Loader ldr = null;
        String json = null;
        String jsonName = null;
        JsonObj jsonObj = null;
        
        for(JsonObjIsFile entry : isaDataSet.is_files) {
            if(eventHandler != null) {
                eventHandler.LoadAndParseJsonObjDataLoopPre(this, entry);
            }            
            
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

            if(jsonObj.GetLoader().equals("net.middlemind.GenAsm.Loaders.Thumb.LoaderIsEntryTypes")) {
                jsonObjIsEntryTypes = (JsonObjIsEntryTypes)jsonObj;
                Logger.wrl("AssemblerThumb: RunAssembler: Found JsonObjIsEntryTypes object, storing it...");

            } else if(jsonObj.GetLoader().equals("net.middlemind.GenAsm.Loaders.Thumb.LoaderIsValidLines")) {
                jsonObjIsValidLines = (JsonObjIsValidLines)jsonObj;
                Logger.wrl("AssemblerThumb: RunAssembler: Found JsonObjIsValidLines object, storing it...");

            } else if(jsonObj.GetLoader().equals("net.middlemind.GenAsm.Loaders.Thumb.LoaderIsOpCodes")) {
                jsonObjIsOpCodes = (JsonObjIsOpCodes)jsonObj;
                lineBitSeries = jsonObjIsOpCodes.bit_series;
                
                lineNumRange = new JsonObjNumRange();
                lineNumRange.alignment = "WORD";
                lineNumRange.bcd_encoding = false;
                lineNumRange.bit_len = lineBitSeries.bit_len;
                lineNumRange.obj_name = "JsonObjNumRange";
                lineNumRange.min_value = 0;
                lineNumRange.max_value = 65536;               
                lineNumRange.ones_compliment = false;
                lineNumRange.twos_compliment = false;
                
                pcPreFetchBytes = jsonObjIsOpCodes.pc_prefetch_bytes;
                pcPreFetchHalfwords = jsonObjIsOpCodes.pc_prefetch_halfwords;                
                pcPreFetchWords = jsonObjIsOpCodes.pc_prefetch_words;
                
                if(jsonObjIsOpCodes.endian.equals(AssemblerThumb.ENDIAN_NAME_BIG)) {
                    isEndianBig = true;
                    isEndianLittle = false;
                } else {
                    isEndianBig = false;
                    isEndianLittle = true;
                }
                Logger.wrl("AssemblerThumb: RunAssembler: Found JsonObjIsOpCodes object, storing it...");

            } else if(jsonObj.GetLoader().equals("net.middlemind.GenAsm.Loaders.Thumb.LoaderIsDirectives")) {
                jsonObjIsDirectives = (JsonObjIsDirectives)jsonObj;
                Logger.wrl("AssemblerThumb: RunAssembler: Found JsonObjIsDirectives object, storing it...");

            } else if(jsonObj.GetLoader().equals("net.middlemind.GenAsm.Loaders.Thumb.LoaderIsRegisters")) {
                jsonObjIsRegisters = (JsonObjIsRegisters)jsonObj;
                Logger.wrl("AssemblerThumb: RunAssembler: Found JsonObjIsRegisters object, storing it...");                     
            }
            
            if(eventHandler != null) {
                eventHandler.LoadAndParseJsonObjDataLoopPost(this, entry);
            }            
        }

        if(jsonObjIsEntryTypes == null) {
            throw new ExceptionNoEntryFound("Could not find required JsonObjIsEntryTypes instance.");

        } else if(jsonObjIsValidLines == null) {
            throw new ExceptionNoEntryFound("Could not find required JsonObjIsValidLines instance.");

        } else if(jsonObjIsOpCodes == null) {
            throw new ExceptionNoEntryFound("Could not find required JsonObjIsOpCodes instance.");
        }
        
        if(eventHandler != null) {
            eventHandler.LoadAndParseJsonObjDataPost(this);
        }        
    }
    
    public void LinkJsonObjData() throws ExceptionJsonObjLink {
        if(eventHandler != null) {
            eventHandler.LinkJsonObjDataPre(this);
        }
        
        Logger.wrl("AssemblerThumb: LinkJsonObjData");
        for(String s : isaData.keySet()) {
            if(eventHandler != null) {
                eventHandler.LinkJsonObjDataLoopPre(this, s);
            }            
            
            JsonObj jsonObj = isaData.get(s);
            jsonObj.Link(jsonObjIsEntryTypes);
            
            if(eventHandler != null) {
                eventHandler.LinkJsonObjDataLoopPost(this, s);
            }            
        }
        
        if(eventHandler != null) {
            eventHandler.LinkJsonObjDataPost(this);
        }        
    }
    
    //LEX SOURCE CODE
    public void LexerizeAssemblySource() throws IOException {
        if(eventHandler != null) {
            eventHandler.LexerizeAssemblySourcePre(this);
        }        
        
        //Logger.wrl("AssemblerThumb: LoadAndLexAssemblySource: Load assembly source file");
        //asmDataSource = FileLoader.Load(asmSourceFile);
        Logger.wrl("AssemblerThumb: LoadAndLexAssemblySource: Lexerize assembly source file");
        LexerThumb lex = new LexerThumb();
        asmDataLexed = lex.FileLexerize(asmDataSource);
        
        if(eventHandler != null) {
            eventHandler.LexerizeAssemblySourcePost(this);
        }        
    }
    
    //TOKENIZE AND VALIDATE LEXERIZED LINES
    public void TokenizeLexerArtifacts() throws ExceptionNoTokenerFound {
        if(eventHandler != null) {
            eventHandler.TokenizeLexerArtifactsPre(this);
        }         
        
        Logger.wrl("AssemblerThumb: TokenizeLexerArtifacts");
        TokenerThumb tok = new TokenerThumb();
        asmDataTokened = tok.FileTokenize(asmDataLexed, jsonObjIsEntryTypes);
        
        if(eventHandler != null) {
            eventHandler.TokenizeLexerArtifactsPost(this);
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
        if(eventHandler != null) {
            eventHandler.ValidateTokenizedLinePre(this, line, validLines, validLineEmpty);
        }
        
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
                lastToken = token;
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
        
        if(eventHandler != null) {
            eventHandler.ValidateTokenizedLinePost(this);
        }        
        
        return false;
    }
    
    public boolean ValidateTokenizedLines() throws ExceptionNoValidLineFound {
        if(eventHandler != null) {
            eventHandler.ValidateTokenizedLinesPre(this);
        }

        Logger.wrl("AssemblerThumb: ValidateTokenizedLines");
        for(TokenLine line : asmDataTokened) {
            lastLine = line;
            if(eventHandler != null) {
                eventHandler.ValidateTokenizedLinesLoopPre(this, line);
            }
            
            if(!ValidateTokenizedLine(line, jsonObjIsValidLines, jsonObjIsValidLines.is_valid_lines.get(JsonObjIsValidLines.LINE_EMPTY))) {
                throw new ExceptionNoValidLineFound("Could not find a matching valid line for line number, " + line.lineNum + " with source text, '" + line.source.source + "'");
            }
            
            if(eventHandler != null) {
                eventHandler.ValidateTokenizedLinesLoopPost(this, line);
            }            
        }
        
        if(eventHandler != null) {
            eventHandler.ValidateTokenizedLinesPost(this);
        }        
        
        return true;
    }
    
    //BUILD OPCODE
    public void BuildBinLines(List<TokenLine> areaLines, AreaThumb area) throws ExceptionOpCodeAsArgument, ExceptionNoSymbolFound, ExceptionUnexpectedTokenWithSubArguments, ExceptionNumberInvalidShift, ExceptionNumberOutOfRange, ExceptionNoNumberRangeFound, ExceptionUnexpectedTokenType, ExceptionInvalidEntry, ExceptionInvalidArea, ExceptionInvalidAssemblyLine, ExceptionDirectiveArgNotSupported, ExceptionMissingDataDirective {
        if(eventHandler != null) {
            eventHandler.BuildBinLinesPre(this, areaLines, area);
        }

        if(area.isCode) {
            for(TokenLine line : areaLines) {
                lastLine = line;
                BuildBinOpCode(line);
            }
        } else if(area.isData) {
            for(TokenLine line : areaLines) {
                lastLine = line;
                BuildBinDirective(line);
            }
        } else {
            throw new ExceptionInvalidArea("Found an invalid area entry at line number " + area.areaLine + " width code: " + area.isCode + " and data: " + area.isData);
        }
        
        if(eventHandler != null) {
            eventHandler.BuildBinLinesPost(this);
        }        
    }
        
    public void BuildBinDirective(TokenLine line) throws ExceptionOpCodeAsArgument, ExceptionNoSymbolFound, ExceptionUnexpectedTokenWithSubArguments, ExceptionNumberInvalidShift, ExceptionNumberOutOfRange, ExceptionNoNumberRangeFound, ExceptionUnexpectedTokenType, ExceptionInvalidEntry, ExceptionInvalidAssemblyLine, ExceptionDirectiveArgNotSupported, ExceptionMissingDataDirective {   
        if(eventHandler != null) {
            eventHandler.BuildBinDirectivePre(this, line);
        }
        
        if(!line.isLineEmpty && line.isLineDirective && !line.isLineOpCode) {
            boolean isDirDcw = false;
            boolean isDirDcb = false;
            
            for(Token token : line.payload) {
                lastToken = token;
                if(token.isDirective) {
                    if(token.source.equals(JsonObjIsDirectives.NAME_DCHW)) {
                        isDirDcw = true;
                        isDirDcb = false;
                    } else if(token.source.equals(JsonObjIsDirectives.NAME_DCB)) {
                        isDirDcw = false;
                        isDirDcb = true;                        
                    }
                    
                } else if(token.isDirectiveArg && (isDirDcw || isDirDcb)) {
                    if(token.type_name.equals(JsonObjIsEntryTypes.NAME_NUMBER) == false) {
                        throw new ExceptionDirectiveArgNotSupported("Could not find supported data directive '" + token.source + "' with line number " + token.lineNum);
                    } else {
                        String resTmp;
                        Integer tInt = Utils.ParseNumberString(token.source);
                        resTmp = Integer.toBinaryString(tInt);
                        resTmp = Utils.FormatBinString(resTmp, lineBitSeries.bit_len);                        
                        tInt = Integer.parseInt(resTmp, 2);

                        if(lineNumRange != null) {
                            if(tInt < lineNumRange.min_value || tInt > lineNumRange.max_value) {
                                throw new ExceptionNumberOutOfRange("Integer value " + tInt + " is outside of the specified range " + lineNumRange.min_value + " to " + lineNumRange.max_value + " for source '" + token.source + "' with line number " + token.lineNum);
                            }
                        } else {
                            throw new ExceptionNoNumberRangeFound("Could not find number range for source '" + token.source + "' with line number " + token.lineNum);
                        }

                        token.value = tInt;
                        if(isDirDcw == true) {
                            line.payloadBinRepStrEndianBig = resTmp;
                            line.payloadBinRepStrEndianLil = Utils.EndianFlip(resTmp);
                            
                        } else if(isDirDcb == true) {
                            line.payloadBinRepStrEndianBig = resTmp;
                            line.payloadBinRepStrEndianLil = Utils.EndianFlip(resTmp);
                            
                        } else {
                            throw new ExceptionMissingDataDirective("Could not find supported data directive '" + token.source + "' with line number " + token.lineNum);
                        
                        }
                    }
                }
            }
        } else {
            throw new ExceptionInvalidAssemblyLine("Could not find a valid assembly line entry for the given AREA with Directive line source '" + line.source.source + "' and line number " + line.lineNum);
        }
        
        if(eventHandler != null) {
            eventHandler.BuildBinDirectivePost(this);
        }        
    }
    
    public void BuildBinOpCode(TokenLine line) throws ExceptionOpCodeAsArgument, ExceptionNoSymbolFound, ExceptionUnexpectedTokenWithSubArguments, ExceptionNumberInvalidShift, ExceptionNumberOutOfRange, ExceptionNoNumberRangeFound, ExceptionUnexpectedTokenType, ExceptionInvalidEntry, ExceptionInvalidAssemblyLine {
        if(eventHandler != null) {
            eventHandler.BuildBinOpCodePre(this, line);
        }        
        
        if(!line.isLineEmpty && !line.isLineDirective && line.isLineOpCode) {            
            JsonObjIsOpCode opCode = line.matchesOpCode.get(0);                
            List<JsonObjIsOpCodeArg> opCodeArgs = opCode.args;
            List<BuildOpCodeThumb> buildEntries = new ArrayList<>();
            List<JsonObjIsOpCodeArg> opCodeArgsSub = null;
            Collections.sort(opCodeArgs, new JsonObjIsOpCodeArgSorter(JsonObjIsOpCodeArgSorter.JsonObjIsOpCodeArgSorterType.ARG_INDEX_ASC));
            Collections.sort(line.payload, new TokenSorter(TokenSorterType.INDEX_ASC));
            BuildOpCodeThumb tmpB = null;
            int opCodeArgIdx = 0;
            
            //Prepare BuilOpCodeEntry list based on tokens and json arguments
            for(Token token : line.payload) {
                lastToken = token;
                if(token.isOpCodeArg) {
                    tmpB = new BuildOpCodeThumb();
                    tmpB.isOpCodeArg = true;
                    tmpB.opCodeArg = (JsonObjIsOpCodeArg)opCodeArgs.get(opCodeArgIdx);
                    tmpB.bitSeries = tmpB.opCodeArg.bit_series;
                    tmpB.tokenOpCodeArg = token;
                    if(tmpB.opCodeArg.bit_index != -1) {
                        buildEntries.add(tmpB);
                    }
                    opCodeArgIdx++;
                    
                    if(!Utils.IsListEmpty(token.payload) && !Utils.IsListEmpty(tmpB.opCodeArg.sub_args)) {
                        if(token.type_name.equals(JsonObjIsEntryTypes.NAME_START_LIST)) {
                            int lOpCodeArgIdx = 0;
                            int llOpCodeArgIdx = 0;
                            JsonObjIsOpCodeArg opCodeArgList = tmpB.opCodeArg;
                            opCodeArgsSub = tmpB.opCodeArg.sub_args;
                            Collections.sort(opCodeArgsSub, new JsonObjIsOpCodeArgSorter(JsonObjIsOpCodeArgSorter.JsonObjIsOpCodeArgSorterType.ARG_INDEX_ASC));                           
                            Collections.sort(token.payload, new TokenSorter(TokenSorterType.INDEX_ASC));
                            boolean regRangeLow = false;
                            boolean regRangeHi = false;
                            
                            if(opCodeArgsSub.get(0).is_entry_types.contains(JsonObjIsEntryTypes.NAME_REGISTER_RANGE_LOW)) {
                                regRangeLow = true;
                            } else if(opCodeArgsSub.get(0).is_entry_types.contains(JsonObjIsEntryTypes.NAME_REGISTER_RANGE_HI)) {
                                regRangeHi = true;
                            }
                            
                            for(Token ltoken : token.payload) {
                                if(ltoken.isOpCodeArg) {
                                    if(regRangeLow && !ltoken.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_LOW)) {
                                        llOpCodeArgIdx++;
                                    } else if(regRangeHi && !ltoken.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_HI)) {
                                        llOpCodeArgIdx++;
                                    }
                                    
                                    tmpB = new BuildOpCodeThumb();
                                    tmpB.isOpCodeArgList = true;
                                    
                                    if(ltoken.type_name.equals(JsonObjIsEntryTypes.NAME_STOP_LIST)) {
                                        tmpB.opCodeArgList = opCodeArgList;
                                    } else {
                                        tmpB.opCodeArgList = (JsonObjIsOpCodeArg)opCodeArgsSub.get(llOpCodeArgIdx);
                                    }
                                    
                                    tmpB.bitSeries = tmpB.opCodeArgList.bit_series;
                                    tmpB.tokenOpCodeArgList = ltoken;
                                    
                                    if(tmpB.opCodeArgList.bit_index != -1) {
                                        buildEntries.add(tmpB);
                                    }
                                    
                                    lOpCodeArgIdx++;
                                } else if(ltoken.isOpCode) {
                                    throw new ExceptionOpCodeAsArgument("Found OpCode token entry where a sub-argument should be on line " + line.lineNum + " with argument index " + ltoken.index + " and parent argument index " + token.index);
                                }
                            }
                            
                        } else if(token.type_name.equals(JsonObjIsEntryTypes.NAME_START_GROUP)) {
                            int lOpCodeArgIdx = 0;
                            JsonObjIsOpCodeArg opCodeArgGroup = tmpB.opCodeArg;
                            opCodeArgsSub = tmpB.opCodeArg.sub_args;
                            Collections.sort(opCodeArgsSub, new JsonObjIsOpCodeArgSorter(JsonObjIsOpCodeArgSorter.JsonObjIsOpCodeArgSorterType.ARG_INDEX_ASC));
                            Collections.sort(token.payload, new TokenSorter(TokenSorterType.INDEX_ASC));

                            for(Token ltoken : token.payload) {
                                if(ltoken.isOpCodeArg) {
                                    tmpB = new BuildOpCodeThumb();
                                    tmpB.isOpCodeArgGroup = true;
                                    
                                    if(ltoken.type_name.equals(JsonObjIsEntryTypes.NAME_STOP_GROUP)) {                                    
                                        tmpB.opCodeArgGroup = opCodeArgGroup;
                                    } else {
                                        tmpB.opCodeArgGroup = (JsonObjIsOpCodeArg)opCodeArgsSub.get(lOpCodeArgIdx);
                                    }
                                    
                                    tmpB.bitSeries = tmpB.opCodeArgGroup.bit_series;
                                    tmpB.tokenOpCodeArgGroup = ltoken;
                                    
                                    if(tmpB.opCodeArgGroup.bit_index != -1) {
                                        buildEntries.add(tmpB);
                                    }
                                    lOpCodeArgIdx++;
                                } else if(ltoken.isOpCode) {
                                    throw new ExceptionOpCodeAsArgument("Found OpCode token entry where a sub-argument should be on line " + line.lineNum + " with argument index " + ltoken.index + " and parent argument index " + token.index);
                                }
                            }
                            
                        } else {
                            throw new ExceptionUnexpectedTokenWithSubArguments("Found unexpected token with sub-arguments on line " + line.lineNum + " with argument index " + token.index);
                        }
                    }
                    
                } else if(token.isOpCode) {
                    tmpB = new BuildOpCodeThumb();
                    tmpB.isOpCode = true;
                    tmpB.bitSeries = opCode.bit_series;
                    tmpB.opCode = opCode;
                    tmpB.tokenOpCode = token;
                    buildEntries.add(tmpB);
                }
            }
            
            //Process BuildOpCodeEntry list creating a binary string representation for each entry
            line.buildEntries = buildEntries;
            String res = "";
            String resTmp = "";
            boolean inList = false;
            boolean inGroup = false;
            int[] inListRegisters = null;
            BuildOpCodeThumb inListEntry = null;
            BuildOpCodeThumb inGroupEntry = null;            
            BuildOpCodeThumb opCodeEntry = null;
            
            for(BuildOpCodeThumb entry : buildEntries) {
                resTmp = "";                
                if(entry.isOpCode) {
                    opCodeEntry = entry;
                    resTmp = entry.opCode.bit_rep.bit_string;
                    
                }else if(entry.isOpCodeArgList) {
                    // <editor-fold defaultstate="collapsed" desc="OpCode Arg List">
                    if(entry.tokenOpCodeArgList.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_LOW)) {
                        if(entry.tokenOpCodeArgList.source.equals("R0")) {
                            inListRegisters[0] = 1;
                            
                        } else if(entry.tokenOpCodeArgList.source.equals("R1")) {
                            inListRegisters[1] = 1;
                        
                        } else if(entry.tokenOpCodeArgList.source.equals("R2")) {
                            inListRegisters[2] = 1;
                        
                        } else if(entry.tokenOpCodeArgList.source.equals("R3")) {
                            inListRegisters[3] = 1;
                        
                        } else if(entry.tokenOpCodeArgList.source.equals("R4")) {
                            inListRegisters[4] = 1;
                        
                        } else if(entry.tokenOpCodeArgList.source.equals("R5")) {
                            inListRegisters[5] = 1;
                        
                        } else if(entry.tokenOpCodeArgList.source.equals("R6")) {
                            inListRegisters[6] = 1;
                        
                        } else if(entry.tokenOpCodeArgList.source.equals("R7")) {
                            inListRegisters[7] = 1;
                        
                        } else {
                            throw new ExceptionInvalidEntry("Found invalid LOW register entry '" + entry.tokenOpCodeArgList.source + "' for line source '" + line.source.source + " and line number " + line.lineNum);
                        }
                        
                    } else if(entry.tokenOpCodeArgList.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_HI)) {
                        if(entry.tokenOpCodeArgList.source.equals("R8")) {
                            inListRegisters[0] = 1;
                        
                        } else if(entry.tokenOpCodeArgList.source.equals("R9")) {
                            inListRegisters[1] = 1;
                        
                        } else if(entry.tokenOpCodeArgList.source.equals("R10")) {
                            inListRegisters[2] = 1;
                        
                        } else if(entry.tokenOpCodeArgList.source.equals("R11")) {
                            inListRegisters[3] = 1;
                        
                        } else if(entry.tokenOpCodeArgList.source.equals("R12")) {
                            inListRegisters[4] = 1;
                        
                        } else if(entry.tokenOpCodeArgList.source.equals("R13") || entry.tokenOpCodeArgList.source.equals("SP")) {
                            inListRegisters[5] = 1;
                        
                        } else if(entry.tokenOpCodeArgList.source.equals("R14") || entry.tokenOpCodeArgList.source.equals("LR")) {
                            inListRegisters[6] = 1;
                        
                        } else if(entry.tokenOpCodeArgList.source.equals("R15") || entry.tokenOpCodeArgList.source.equals("PC")) {
                            inListRegisters[7] = 1;
                        } else {
                            throw new ExceptionInvalidEntry("Found invalid HI register entry '" + entry.tokenOpCodeArgList.source + "' for line source '" + line.source.source + " and line number " + line.lineNum);
                        }
                        
                    } else if(entry.tokenOpCodeArgList.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_PC)) {
                        if(entry.tokenOpCodeArgList.source.equals("R15") || entry.tokenOpCodeArgList.source.equals("PC")) {
                            inListRegisters[7] = 1;
                        } else {
                            throw new ExceptionInvalidEntry("Found invalid PC register entry '" + entry.tokenOpCodeArgList.source + "' for line source '" + line.source.source + " and line number " + line.lineNum); 
                        }
                        
                    } else if(entry.tokenOpCodeArgList.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_SP)) {
                        if(entry.tokenOpCodeArgList.source.equals("R13") || entry.tokenOpCodeArgList.source.equals("SP")) {
                            inListRegisters[5] = 1;
                        } else {
                            throw new ExceptionInvalidEntry("Found invalid SP register entry '" + entry.tokenOpCodeArgList.source + "' for line source '" + line.source.source + " and line number " + line.lineNum); 
                        }
                        
                    } else if(entry.tokenOpCodeArgList.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_LR)) {
                        if(entry.tokenOpCodeArgList.source.equals("R14") || entry.tokenOpCodeArgList.source.equals("LR")) {
                            inListRegisters[6] = 1;
                        } else {
                            throw new ExceptionInvalidEntry("Found invalid LR register entry '" + entry.tokenOpCodeArgList.source + "' for line source '" + line.source.source + " and line number " + line.lineNum); 
                        }
                        
                    } else if(entry.tokenOpCodeArgList.type_name.equals(JsonObjIsEntryTypes.NAME_LABEL_REF)) {
                        throw new ExceptionInvalidEntry("Found invalid LABEL entry '" + entry.tokenOpCodeArgList.source + "' for line source '" + line.source.source + " and line number " + line.lineNum);
                    
                    //} else if(entry.tokenOpCodeArgList.type_name.equals(JsonObjIsEntryTypes.NAME_LABEL_NUMERIC_LOCAL_REF)) {
                    //    throw new ExceptionInvalidEntry("Found invalid LABEL_NUMERIC_LOCAL_REF entry '" + entry.tokenOpCodeArgList.source + "' for line source '" + line.source.source + " and line number " + line.lineNum); 
                    
                    } else if(entry.tokenOpCodeArgList.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTERWB)) {
                        throw new ExceptionInvalidEntry("Found invalid REGISTERWB entry '" + entry.tokenOpCodeArgList.source + "' for line source '" + line.source.source + " and line number " + line.lineNum); 
                    
                    } else if(entry.tokenOpCodeArgList.type_name.equals(JsonObjIsEntryTypes.NAME_NUMBER)) {
                        throw new ExceptionInvalidEntry("Found invalid NUMBER entry '" + entry.tokenOpCodeArgList.source + "' for line source '" + line.source.source + " and line number " + line.lineNum); 
                    
                    } else if(entry.tokenOpCodeArgList.type_name.equals(JsonObjIsEntryTypes.NAME_STOP_LIST)) {
                        inList = false;
                        String ts = "";
                        for(int z = 0; z < inListRegisters.length; z++) {
                            if(inListRegisters[z] == 1) {
                                ts = ("1" + ts);
                            } else {
                                ts = ("0" + ts);                                
                            }
                        }
                        inListEntry.binRepStr = ts;
                        inListRegisters = null;
                        
                    } else if(entry.tokenOpCodeArgList.type_name.equals(JsonObjIsEntryTypes.NAME_STOP_GROUP)) {
                        throw new ExceptionInvalidEntry("Found invalid STOP GROUP entry '" + entry.tokenOpCodeArgList.source + "' for line source '" + line.source.source + " and line number " + line.lineNum); 
                    
                    } else {
                        throw new ExceptionUnexpectedTokenType("Found unexpected LIST sub-token type '" + entry.tokenOpCodeArgList.type_name + "' for line source '" + line.source.source + " and line number " + line.lineNum);
                    }
                    //</editor-fold>
                    
                }else if(entry.isOpCodeArgGroup) {
                    // <editor-fold defaultstate="collapsed" desc="OpCode Arg Group">
                    if(entry.tokenOpCodeArgGroup.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_LOW)) {
                        resTmp = entry.tokenOpCodeArgGroup.register.bit_rep.bit_string;
                    
                    } else if(entry.tokenOpCodeArgGroup.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_HI)) {
                        resTmp = entry.tokenOpCodeArgGroup.register.bit_rep.bit_string;
                    
                    } else if(entry.tokenOpCodeArgGroup.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_PC)) {
                        resTmp = entry.tokenOpCodeArgGroup.register.bit_rep.bit_string;
                    
                    } else if(entry.tokenOpCodeArgGroup.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_SP)) {
                        resTmp = entry.tokenOpCodeArgGroup.register.bit_rep.bit_string;
                    
                    } else if(entry.tokenOpCodeArgGroup.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_LR)) {
                        resTmp = entry.tokenOpCodeArgGroup.register.bit_rep.bit_string;
                    
                    } else if(entry.tokenOpCodeArgGroup.type_name.equals(JsonObjIsEntryTypes.NAME_LABEL_REF)) {
                        Symbol sym = symbols.symbols.get(entry.tokenOpCodeArgGroup.source.replace("=", ""));
                        if(sym != null) {
                            resTmp = Utils.FormatBinString(Integer.toBinaryString(sym.lineNumActive), entry.opCodeArgGroup.bit_series.bit_len);
                        } else {
                            throw new ExceptionNoSymbolFound("Could not find symbol for label '" + entry.tokenOpCodeArgGroup.source.replace("=", "") + "' with line number " + entry.tokenOpCodeArgGroup.lineNum);
                        }

                    /*
                    } else if(entry.tokenOpCodeArgGroup.type_name.equals(JsonObjIsEntryTypes.NAME_LABEL_NUMERIC_LOCAL_REF)) {
                        Symbol sym = symbols.symbols.get(entry.tokenOpCodeArgGroup.source.replace("=", ""));
                        if(sym != null) {
                            resTmp = Utils.FormatBinString(Integer.toBinaryString(sym.lineNumActive), entry.opCodeArgGroup.bit_series.bit_len);
                        } else {
                            throw new ExceptionNoSymbolFound("Could not find symbol for local label '" + entry.tokenOpCodeArgGroup.source.replace("=", "") + "' with line number " + entry.tokenOpCodeArgGroup.lineNum);
                        }
                    */
                    
                    } else if(entry.tokenOpCodeArgGroup.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTERWB)) {
                        resTmp = entry.tokenOpCodeArgGroup.register.bit_rep.bit_string;
                    
                    } else if(entry.tokenOpCodeArgGroup.type_name.equals(JsonObjIsEntryTypes.NAME_NUMBER)) {
                        Integer tInt = Utils.ParseNumberString(entry.tokenOpCodeArgGroup.source);
                        
                        //special rule for ADD OpCode
                        if(opCodeEntry.binRepStr.equals("101100000") && tInt < 0) {
                            opCodeEntry.binRepStr = "101100001";
                            tInt *= -1;
                        }
                        
                        if(entry.opCodeArgGroup.num_range.ones_compliment) {
                            tInt = ~tInt;
                        }                        
                        
                        resTmp = Integer.toBinaryString(tInt);
                        
                        if(entry.opCodeArgGroup.num_range.twos_compliment) {
                            resTmp = TwosCompliment.GetTwosCompliment(resTmp);
                        }
                        
                        //TODO: Check alignment
                        //entry.opCodeArg.num_range.alignment                        
                        
                        resTmp = Integer.toBinaryString(tInt);
                        if(entry.opCodeArgGroup.bit_shift != null) {
                            if(entry.opCodeArgGroup.bit_shift.shift_amount > 0) {
                                if(!Utils.IsStringEmpty(entry.opCodeArgGroup.bit_shift.shift_dir) && entry.opCodeArgGroup.bit_shift.shift_dir.equals(NUMBER_SHIFT_NAME_LEFT)) {
                                    resTmp = Utils.ShiftBinStr(resTmp, entry.opCodeArgGroup.bit_shift.shift_amount, false, true);
                                } else if(!Utils.IsStringEmpty(entry.opCodeArgGroup.bit_shift.shift_dir) && entry.opCodeArgGroup.bit_shift.shift_dir.equals(NUMBER_SHIFT_NAME_RIGHT)) {
                                    resTmp = Utils.ShiftBinStr(resTmp, entry.opCodeArgGroup.bit_shift.shift_amount, true, true);
                                } else {
                                    throw new ExceptionNumberInvalidShift("Invalid number shift found for source '" + entry.tokenOpCodeArgGroup.source + "' with line number " + entry.tokenOpCodeArgGroup.lineNum);
                                }
                            }
                        }
                        
                        resTmp = Utils.FormatBinString(resTmp, entry.opCodeArgGroup.bit_series.bit_len);
                        tInt = Integer.parseInt(resTmp, 2);
                                                
                        if(entry.opCodeArgGroup.num_range != null) {
                            if(tInt < entry.opCodeArgGroup.num_range.min_value || tInt >  entry.opCodeArgGroup.num_range.max_value) {
                                throw new ExceptionNumberOutOfRange("Integer value " + tInt + " is outside of the specified range " + entry.opCodeArgGroup.num_range.min_value + " to " + entry.opCodeArgGroup.num_range.max_value + " for source '" + entry.tokenOpCodeArgGroup.source + "' with line number " + entry.tokenOpCodeArgGroup.lineNum);
                            }
                        } else {
                            throw new ExceptionNoNumberRangeFound("Could not find number range for source '" + entry.tokenOpCodeArgGroup.source + "' with line number " + entry.tokenOpCodeArgGroup.lineNum);
                        }
                        
                        entry.tokenOpCodeArgGroup.value = tInt;
                        
                    } else if(entry.tokenOpCodeArgGroup.type_name.equals(JsonObjIsEntryTypes.NAME_STOP_LIST)) {
                        throw new ExceptionInvalidEntry("Found invalid STOP LIST entry '" + entry.tokenOpCodeArgList.source + "' for line source '" + line.source.source + " and line number " + line.lineNum);                         
                    
                    } else if(entry.tokenOpCodeArgGroup.type_name.equals(JsonObjIsEntryTypes.NAME_STOP_GROUP)) {
                        inGroup = false;
                        inGroupEntry = null;
                    
                    } else {
                        throw new ExceptionUnexpectedTokenType("Found unexpected GROUP sub-token type '" + entry.tokenOpCodeArgGroup.type_name + "' for line source '" + line.source.source + " and line number " + line.lineNum);
                    }
                    //</editor-fold>
                    
                //Process OpCode argument tokens
                }else if(entry.isOpCodeArg) {
                    // <editor-fold defaultstate="collapsed" desc="OpCode Arg">
                    if(entry.tokenOpCodeArg.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_LOW)) {
                        resTmp = entry.tokenOpCodeArg.register.bit_rep.bit_string;
                    
                    } else if(entry.tokenOpCodeArg.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_HI)) {
                        resTmp = entry.tokenOpCodeArg.register.bit_rep.bit_string;
                    
                    } else if(entry.tokenOpCodeArg.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_PC)) {
                        resTmp = entry.tokenOpCodeArg.register.bit_rep.bit_string;
                    
                    } else if(entry.tokenOpCodeArg.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_SP)) {
                        resTmp = entry.tokenOpCodeArg.register.bit_rep.bit_string;
                    
                    } else if(entry.tokenOpCodeArg.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_LR)) {
                        resTmp = entry.tokenOpCodeArg.register.bit_rep.bit_string;
                    
                    } else if(entry.tokenOpCodeArg.type_name.equals(JsonObjIsEntryTypes.NAME_LABEL_REF)) {
                        Symbol sym = symbols.symbols.get(entry.tokenOpCodeArg.source.replace("=", ""));
                        if(sym != null) {
                            resTmp = Utils.FormatBinString(Integer.toBinaryString(sym.lineNumActive), entry.opCodeArg.bit_series.bit_len);
                        } else {
                            throw new ExceptionNoSymbolFound("Could not find symbol for label '" + entry.tokenOpCodeArg.source.replace("=", "") + "' with line number " + entry.tokenOpCodeArg.lineNum);
                        }
                        //TODO: Process numeric label ref
                        //TODO: Handle if OpCode/OpCode Arg uses a halword offset                        
                    
                    /*
                    } else if(entry.tokenOpCodeArg.type_name.equals(JsonObjIsEntryTypes.NAME_LABEL_NUMERIC_LOCAL_REF)) {
                        Symbol sym = symbols.symbols.get(entry.tokenOpCodeArg.source.replace("=", ""));
                        if(sym != null) {
                            resTmp = Utils.FormatBinString(Integer.toBinaryString(sym.lineNumActive), entry.opCodeArg.bit_series.bit_len);
                        } else {
                            throw new ExceptionNoSymbolFound("Could not find symbol for local label '" + entry.tokenOpCodeArg.source.replace("=", "") + "' with line number " + entry.tokenOpCodeArg.lineNum);
                        }
                        //TODO: Process numeric label local ref
                        //TODO: Handle if OpCode/OpCode Arg uses a halword offset
                    */
                    
                    } else if(entry.tokenOpCodeArg.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTERWB)) {
                        resTmp = entry.tokenOpCodeArg.register.bit_rep.bit_string;
                        
                    } else if(entry.tokenOpCodeArg.type_name.equals(JsonObjIsEntryTypes.NAME_NUMBER)) {
                        Integer tInt = Utils.ParseNumberString(entry.tokenOpCodeArg.source);
                        
                        if(entry.opCodeArg.num_range.handle_prefetch) {
                            tInt -= pcPreFetchHalfwords;
                        }
                        
                        //special rule for ADD OpCode '101100000'
                        if(opCodeEntry.binRepStr.equals("101100000") && tInt < 0) {
                            opCodeEntry.binRepStr = "101100001";
                            tInt *= -1;
                        }
                        
                        if(entry.opCodeArg.num_range.ones_compliment) {
                            tInt = ~tInt;
                        }                        
                        
                        resTmp = Integer.toBinaryString(tInt);
                        
                        if(entry.opCodeArg.num_range.twos_compliment) {
                            resTmp = TwosCompliment.GetTwosCompliment(resTmp);
                        }
                        
                        //TODO: Check alignment
                        //entry.opCodeArg.num_range.alignment                        

                        if(entry.opCodeArg.bit_shift != null) {
                            if(entry.opCodeArg.bit_shift.shift_amount > 0) {
                                if(!Utils.IsStringEmpty(entry.opCodeArg.bit_shift.shift_dir) && entry.opCodeArg.bit_shift.shift_dir.equals(NUMBER_SHIFT_NAME_LEFT)) {
                                    resTmp = Utils.ShiftBinStr(resTmp, entry.opCodeArg.bit_shift.shift_amount, false, true);
                                } else if(!Utils.IsStringEmpty(entry.opCodeArg.bit_shift.shift_dir) && entry.opCodeArg.bit_shift.shift_dir.equals(NUMBER_SHIFT_NAME_RIGHT)) {
                                    resTmp = Utils.ShiftBinStr(resTmp, entry.opCodeArg.bit_shift.shift_amount, true, true);
                                } else {
                                    throw new ExceptionNumberInvalidShift("Invalid number shift found for source '" + entry.tokenOpCodeArg.source + "' with line number " + entry.tokenOpCodeArg.lineNum);
                                }
                            }
                        }
                                                
                        resTmp = Utils.FormatBinString(resTmp, entry.opCodeArg.bit_series.bit_len);                        
                        tInt = Integer.parseInt(resTmp, 2);
                        
                        if(entry.opCodeArg.num_range != null) {
                            if(tInt < entry.opCodeArg.num_range.min_value || tInt >  entry.opCodeArg.num_range.max_value) {
                                throw new ExceptionNumberOutOfRange("Integer value " + tInt + " is outside of the specified range " + entry.opCodeArg.num_range.min_value + " to " + entry.opCodeArg.num_range.max_value + " for source '" + entry.tokenOpCodeArg.source + "' with line number " + entry.tokenOpCodeArg.lineNum);
                            }
                        } else {
                            throw new ExceptionNoNumberRangeFound("Could not find number range for source '" + entry.tokenOpCodeArg.source + "' with line number " + entry.tokenOpCodeArg.lineNum);
                        }
                        
                        entry.tokenOpCodeArg.value = tInt;
                        
                    } else if(entry.tokenOpCodeArg.type_name.equals(JsonObjIsEntryTypes.NAME_START_LIST)) {
                        inList = true;
                        inListRegisters = new int[8];
                        inListEntry = entry;
                    
                    } else if(entry.tokenOpCodeArg.type_name.equals(JsonObjIsEntryTypes.NAME_START_GROUP)) {
                        inGroup = true;
                        inGroupEntry = entry;
                    
                    } else {
                        throw new ExceptionUnexpectedTokenType("Found unexpected token type '" + entry.tokenOpCodeArg.type_name + "' for line source '" + line.source.source + " and line number " + line.lineNum);
                    }
                    
                    // </editor-fold>
                }
                entry.binRepStr = resTmp;
                res += resTmp;
            }
        
            //Clean out non-bit series entries from the build entries list and sort by bit series desc
            BuildOpCodeEntryThumbSorter buildEntriesSorter = new BuildOpCodeEntryThumbSorter(BuildOpCodeEntryThumbSorterType.BIT_SERIES_DSC);
            buildEntriesSorter.Clean(buildEntries);
            Collections.sort(buildEntries, buildEntriesSorter);

            res = "";
            for(BuildOpCodeThumb entry : buildEntries) {
                res += entry.binRepStr;
            }
            line.payloadBinRepStrEndianBig = res;
            line.payloadBinRepStrEndianLil = Utils.EndianFlip(res);
            
            //Build final string representation for this assembly line
        } else {
            throw new ExceptionInvalidAssemblyLine("Could not find a valid assembly line entry for the given AREA with OpCode line source '" + line.source.source + "' and line number " + line.lineNum);
        }
        
        if(eventHandler != null) {
            eventHandler.BuildBinOpCodePost(this);
        }        
    }
}
