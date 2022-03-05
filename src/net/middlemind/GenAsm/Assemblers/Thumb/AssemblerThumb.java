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
import net.middlemind.GenAsm.JsonObjs.JsonObjIsFile;
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
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import net.middlemind.GenAsm.Assemblers.AssemblerEventHandler;
import net.middlemind.GenAsm.Assemblers.Thumb.BuildOpCodeEntryThumbSorter.BuildOpCodeEntryThumbSorterType;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionDirectiveArgNotSupported;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionInvalidArea;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionInvalidAssemblyLine;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionInvalidEntry;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionMissingDataDirective;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionNoNumberRangeFound;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionNumberInvalidShift;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionNoOpCodeLineFound;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionNoSymbolFound;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionNoSymbolValueFound;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionNumberOutOfRange;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionOpCodeAsArgument;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionUnexpectedTokenType;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionUnexpectedTokenWithSubArguments;
import net.middlemind.GenAsm.FileIO.FileLoader;
import net.middlemind.GenAsm.JsonObjs.JsonObjBitSeries;
import net.middlemind.GenAsm.JsonObjs.JsonObjNumRange;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsEmptyDataLines;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsOpCodeArgSorter;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsRegister;
import net.middlemind.GenAsm.Logger;
import net.middlemind.GenAsm.Tokeners.TokenSorter.TokenSorterType;
import net.middlemind.GenAsm.Utils;

/**
 * A class used to assemble ARM Thumb assembly source code.
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 12:08 PM EST
 */
@SuppressWarnings({"UseOfObsoleteCollectionType", "MismatchedQueryAndUpdateOfCollection", "UseSpecificCatch", "null", "CallToPrintStackTrace", "UnusedAssignment", "Convert2Diamond", "ConvertToStringSwitch"})
public class AssemblerThumb implements Assembler {
    /**
     * A static string representing the big indian binary encoding.
     */
    public static String ENDIAN_NAME_BIG = "BIG";
    
    /**
     * A static string representing the little indian binary encoding.
     */
    public static String ENDIAN_NAME_LITTLE = "LITTLE";
    
    /**
     * A static string representing the Java default endianess.
     */
    public static String ENDIAN_NAME_JAVA_DEFAULT = "BIG";
    
    /**
     * A static string representing the default instruction alignment, WORD or HALFWORD>
     */
    public static String INSTRUCTION_ALIGNMENT_NAME_WORD = "HALFWORD";
    
    /**
     * A static integer representing the number of bytes used to align the source code.
     */
    public static int INSTRUCTION_ALIGNMENT_BYTES = 2;
    
    /**
     * A static integer representing the number of bits used to describe one op-code instruction.
     */    
    public static int INSTRUCTION_ALIGNMENT_BITS = 16;
    
    /**
     * A static string representing the name of the left shift type, LEFT.
     */    
    public static String NUMBER_SHIFT_NAME_LEFT = "LEFT";
    
    /**
     * A static string representing the name of the right shift type, RIGHT.
     */    
    public static String NUMBER_SHIFT_NAME_RIGHT = "RIGHT";
    
    /**
     * A static string representing the special ADD op-code check.
     */    
    public static String SPECIAL_ADD_OP_CODE_CHECK = "101100000";
    
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */
    public String obj_name = "AssemblerThumb";

    /**
     * An object representing the instruction set data set of all loaded JSON data file.
     */    
    public JsonObjIsSet isaDataSet;

    /**
     * A data structure holding the loaded JSON data files by name.
     */    
    public Map<String, JsonObj> isaData;

    /**
     * A data structure holding the JSON data file loaders by name.
     */    
    public Map<String, Loader> isaLoader;

    /**
     * A data structure holding the source JSON data file by name.
     */    
    public Map<String, String> jsonSource;

    /**
     * An object representing the instruction set's entry type objects.
     */        
    public JsonObjIsEntryTypes jsonObjIsEntryTypes;

    /**
     * An object representing the instruction set's valid line objects
     */        
    public JsonObjIsValidLines jsonObjIsValidLines;

    /**
     * An object representing the instruction set's valid empty data line objects
     */
    public JsonObjIsEmptyDataLines jsonObjIsEmptyDataLines;
    
    /**
     * An object representing the instruction set's op-code objects
     */    
    public JsonObjIsOpCodes jsonObjIsOpCodes;

    /**
     * An object representing the instruction set's directive objects
     */    
    public JsonObjIsDirectives jsonObjIsDirectives;

    /**
     * An object representing the instruction set's register objects
     */    
    public JsonObjIsRegisters jsonObjIsRegisters;    

    /**
     * The full path to the assembly source file to process.
     */        
    public String asmSourceFile;

    /**
     * A list of strings representing the loaded assembly source lines.
     */    
    public List<String> asmDataSource;
    
    /**
     * A list of artifact lines representing the lexerized source lines.
     */        
    public List<ArtifactLine> asmDataLexed;

    /**
     * A list of artifact lines representing the tokenization of the lexerized lines.
     */    
    public List<TokenLine> asmDataTokened;

    /**
     * An object used to hold references to the assembly source file's symbols.
     */    
    public Symbols symbols;

    /**
     * A list of strings representing required directives.
     */        
    public List<String> requiredDirectives;    

    /**
     * An object used to represent the assembly source code from the code area.
     */    
    public AreaThumb areaThumbCode;

    /**
     * An object used to represent the assembly source code from the data area.
     */    
    public AreaThumb areaThumbData;

    /**
     * A list of token line objects that belong to the code area.
     */    
    public List<TokenLine> asmAreaLinesCode;

    /**
     * A list of token line objects that belong to the data area.
     */    
    public List<TokenLine> asmAreaLinesData;    

    /**
     * An integer representing the number of bytes in one line of assembly source.
     */        
    public int lineLenBytes = 2;

    /**
     * An integer representing the number of halfwords in one line of assembly source,
     */    
    public int lineLenHalfWords = 1;
    
    /**
     * An integer representing the number of words in one line of assembly source.
     */        
    public int lineLenWords = 0;

    /**
     * An object representing a bit series that describes a line of assembly code.
     */    
    public JsonObjBitSeries lineBitSeries;

    /**
     * An object representing a number range that describes a valid integer value of a line of assembly code.
     */    
    public JsonObjNumRange lineNumRange;
    
    /**
     * An integer representing the number of pre-fetch bytes ahead of the PC register value.
     */    
    public int pcPreFetchBytes;
    
    /**
     * An integer representing the number of pre-fetch words ahead of the PC register value.
     */        
    public int pcPreFetchWords;

    /**
     * An integer representing the number of pre-fetch half words ahead of the PC register value.
     */    
    public int pcPreFetchHalfwords;

    /**
     * An integer representing the starting line number of the assembly source, ORG directive will offset this value.
     */    
    public Integer asmStartLineNumber; 

    /**
     * A Boolean value indicating if this assembler should be considered little endian.
     */    
    public boolean isEndianBig = false;

    /**
     * A Boolean value indicating if this assembler should be considered big endian.
     */        
    public boolean isEndianLittle = true; 

    /**
     * An object representing the last line processed by the assembler.
     */        
    public TokenLine lastLine;

    /**
     * An object representing the last token processed by the assembler.
     */    
    public Token lastToken;

    /**
     * An integer value representing the last step run by the assembler.
     */    
    public int lastStep;

    /**
     * A string value representing the title of the assembly source file.
     */    
    public String assemblyTitle;

    /**
     * A string value representing the sub-title of the assembly source file.
     */    
    public String assemblySubTitle;    
    
    /**
     * A generic Java object used to customized the assembly process.
     */    
    public Object other;

    /**
     * A string representing root output directory used for writing output files.
     */    
    public String rootOutputDir;

    /**
     * An object used to respond to assembler events and customize the assembly process.
     */        
    public AssemblerEventHandlerThumb eventHandler;
    
    /**
     * A Boolean value indicating that verbose logging should be used if available.
     */
    public boolean verboseLogs = false;

    /**
     * A Boolean value indicating file output should be turned off.
     */
    public boolean quellFileOutput = false;
    
    //MAIN METHOD
    /**
     * The main assembler execution method called by the GenAsm static main entry point.
     * @param jsonIsSet             The set of JSON data files necessary to assemble the provided source file.
     * @param assemblySourceFile    The full file name of the assembly source file.
     * @param assemblySource        A list of strings representing the contents of the assembly source file.
     * @param outputDir             The full path to an output directory used to write output files to.
     * @param otherObj              An optional generic Java object used to customize the assembly process.
     * @param asmEventHandler       An optional event handler used to respond to and customize different steps in the assembly process.
     * @param verbose               A Boolean value indicating that verbose logging should be used.
     * @param quellOutput           A Boolean value indicating that file output should be disabled.
     * @throws Exception            Throws an exception if an error was encountered during the assembly process. 
     */
    @Override
    public void RunAssembler(JsonObjIsSet jsonIsSet, String assemblySourceFile, List<String> assemblySource, String outputDir, Object otherObj, AssemblerEventHandler asmEventHandler, boolean verbose, boolean quellOutput) throws Exception {
        try {
            verboseLogs = verbose;
            quellFileOutput = quellOutput;
            eventHandler = (AssemblerEventHandlerThumb)asmEventHandler;
            if(eventHandler != null) {
                eventHandler.RunAssemblerPre(this, jsonIsSet, assemblySourceFile, assemblySource, outputDir, otherObj);
            }
            
            Logger.wrl("AssemblerThumb: RunAssembler");
            other = otherObj;
            rootOutputDir = outputDir;
            asmStartLineNumber = 0;
            
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
            if(eventHandler != null) {
                eventHandler.RunAssemblerPreStep(lastStep, this);
            }
            LoadAndParseJsonObjData(lastStep);

            Logger.wrl("");
            lastStep = 2;
            Logger.wrl("STEP 2: Link loaded json object data");
            if(eventHandler != null) {
                eventHandler.RunAssemblerPreStep(lastStep, this);
            }
            LinkJsonObjData(lastStep);

            Logger.wrl("");
            lastStep = 3;
            Logger.wrl("STEP 3: Load and lexerize the assembly source file");
            if(eventHandler != null) {
                eventHandler.RunAssemblerPreStep(lastStep, this);
            }            
            LexerizeAssemblySource(lastStep);
            if(!quellFileOutput) {
                Utils.WriteObject(asmDataLexed, "Assembly Lexerized Data", "output_lexed.json", rootOutputDir);        
            }
            
            Logger.wrl("");
            lastStep = 4;
            Logger.wrl("STEP 4: Tokenize the lexerized artifacts");
            if(eventHandler != null) {
                eventHandler.RunAssemblerPreStep(lastStep, this);
            }            
            TokenizeLexerArtifacts(lastStep);
            if(!quellFileOutput) {
                Utils.WriteObject(asmDataTokened, "Assembly Tokenized Data", "output_tokened_phase0_tokenized.json", rootOutputDir);            
            }
            
            Logger.wrl("");
            lastStep = 5;
            Logger.wrl("STEP 5: Validate token lines");
            if(eventHandler != null) {
                eventHandler.RunAssemblerPreStep(lastStep, this);
            }            
            ValidateTokenizedLines(lastStep);
            if(!quellFileOutput) {
                Utils.WriteObject(asmDataTokened, "Assembly Tokenized Data", "output_tokened_phase1_valid_lines.json", rootOutputDir);            
            }
            
            Logger.wrl("");
            lastStep = 6;
            Logger.wrl("STEP 6: Combine comment tokens as children of the initial comment token");
            if(eventHandler != null) {
                eventHandler.RunAssemblerPreStep(lastStep, this);
            }            
            CollapseCommentTokens(lastStep);

            Logger.wrl("");
            lastStep = 7;
            Logger.wrl("STEP 7: Expand register ranges into individual register entries");
            if(eventHandler != null) {
                eventHandler.RunAssemblerPreStep(lastStep, this);
            }            
            ExpandRegisterRangeTokens(lastStep);

            Logger.wrl("");
            lastStep = 8;
            Logger.wrl("STEP 8: Combine list and group tokens as children of the initial list or group token");
            if(eventHandler != null) {
                eventHandler.RunAssemblerPreStep(lastStep, this);
            }            
            CollapseListAndGroupTokens(lastStep);

            Logger.wrl("");
            lastStep = 9;
            Logger.wrl("STEP 9: Mark OpCode, OpCode argument, and register tokens");
            if(eventHandler != null) {
                eventHandler.RunAssemblerPreStep(lastStep, this);
            }            
            PopulateOpCodeAndArgData(lastStep);

            Logger.wrl("");
            lastStep = 10;
            Logger.wrl("STEP 10: Mark directive and directive argument tokens, create area based line lists with hex numbering");
            if(eventHandler != null) {
                eventHandler.RunAssemblerPreStep(lastStep, this);
            }            
            PopulateDirectiveArgAndAreaData(lastStep);
            if(!quellFileOutput) {
                Utils.WriteObject(asmDataTokened, "Assembly Tokenized Data", "output_tokened_phase2_refactored.json", rootOutputDir);
            }
            
            Logger.wrl("");
            lastStep = 11;
            Logger.wrl("STEP 11: Validate OpCode lines against known OpCodes by comparing arguments");
            if(eventHandler != null) {
                eventHandler.RunAssemblerPreStep(lastStep, this);
            }            
            ValidateOpCodeLines(lastStep);

            Logger.wrl("");
            lastStep = 12;
            Logger.wrl("STEP 12: Validate directive lines against known directives by comparing arguments");
            if(eventHandler != null) {
                eventHandler.RunAssemblerPreStep(lastStep, this);
            }            
            ValidateDirectiveLines(lastStep);
            if(!quellFileOutput) {
                Utils.WriteObject(asmDataTokened, "Assembly Tokenized Data", "output_tokened_phase3_valid_lines.json", rootOutputDir);            
                Utils.WriteObject(symbols, "Symbol Data", "output_symbols.json", rootOutputDir);
            }
            
            Logger.wrl("");
            Logger.wrl("STEP 13: List Assembly Source Areas and Build Binary Output:");
            if(areaThumbCode != null) {
                Logger.wrl("AreaThumbCode: Title: " + areaThumbCode.title);                
                Logger.wrl("AreaThumbCode: AreaLine: " + areaThumbCode.lineNumArea + " EntryLine: " + areaThumbCode.lineNumEntry + " EndLine: " + areaThumbCode.lineNumEnd);
                Logger.wrl("AreaThumbCode: Attributes: IsCode: " + areaThumbCode.isCode + " IsData: " + areaThumbCode.isData + " IsReadOnly: " + areaThumbCode.isReadOnly + " IsReadWrite: " + areaThumbCode.isReadWrite);                
                if(!quellFileOutput) {
                    Utils.WriteObject(asmAreaLinesCode, "Assembly Source Area Code Lines", "output_area_lines_code.json", rootOutputDir);
                    Utils.WriteObject(areaThumbCode, "Assembly Source Area Code Desc", "output_area_desc_code.json", rootOutputDir);
                }
                BuildBinLines(lastStep, asmAreaLinesCode, areaThumbCode);
            } else {
                Logger.wrl("AreaThumbCode: is null");
            }

            if(areaThumbData != null) {
                Logger.wrl("AreaThumbData: Title: " + areaThumbData.title);
                Logger.wrl("AreaThumbData: AreaLine: " + areaThumbData.lineNumArea + " EntryLine: " + areaThumbData.lineNumEntry + " EndLine: " + areaThumbData.lineNumEnd);
                Logger.wrl("AreaThumbData: Attributes: IsCode: " + areaThumbData.isCode + " IsData: " + areaThumbData.isData + " IsReadOnly: " + areaThumbData.isReadOnly + " IsReadWrite: " + areaThumbData.isReadWrite);
                if(!quellFileOutput) {
                    Utils.WriteObject(asmAreaLinesData, "Assembly Source Area Data Lines", "output_area_lines_data.json", rootOutputDir);
                    Utils.WriteObject(areaThumbData, "Assembly Source Area Data Desc", "output_area_desc_data.json", rootOutputDir);
                }
                BuildBinLines(lastStep, asmAreaLinesData, areaThumbData);
            } else {
                Logger.wrl("AreaThumbData: is null");
            }

            if(!quellFileOutput) {
                Utils.WriteObject(asmDataTokened, "Assembly Tokenized Data", "output_tokened_phase4_bin_output.json", rootOutputDir);
            }
            
            Logger.wrl("");
            Logger.wrl("Assembler Meta Data:");
            Logger.wrl("Title: " + assemblyTitle);
            Logger.wrl("SubTitle: " + assemblySubTitle);            
            Logger.wrl("LineLengthBytes: " + lineLenBytes);
            Logger.wrl("LineLengthWords: " + lineLenWords);
            Logger.wrl("LineLengthHalfWords: " + lineLenHalfWords);
            Logger.wrl("LineBitSeries:");
            lineBitSeries.Print("\t");
            
            if(eventHandler != null) {
                eventHandler.RunAssemblerPost(this);
            }
            
        } catch(Exception e) {
            Logger.wrlErr("Error in RunAssembler method on step: " + lastStep);
            if(lastLine != null) {
                Logger.wrlErr("Last line processed: " + lastLine.lineNumAbs);
                if(lastLine.source != null) {
                    Logger.wrlErr("Last line source: " + lastLine.source.source);
                }
            } else {
                Logger.wrlErr("Last line processed: unknown");
                Logger.wrlErr("Last line source: unknown");                
            }
            
            if(lastToken != null) {
                Logger.wrlErr("Last token processed: " + lastToken.source + " with index " + lastToken.index + ", type name '" + lastToken.type_name + "', and line number " + lastToken.lineNumAbs);
            } else {
                Logger.wrlErr("Last token processed: unknown");
            }
            throw e;
        }
    }
            
    //DIRECTIVE METHODS
    /**
     * A method to mark directive arguments and populate area data.
     * @param step An integer representing the current assembly step.
     * @throws ExceptionMissingRequiredDirective
     * @throws ExceptionRedefinitionOfAreaDirective
     * @throws ExceptionNoDirectiveFound
     * @throws ExceptionNoParentSymbolFound
     * @throws ExceptionMalformedEntryEndDirectiveSet
     * @throws ExceptionNoAreaDirectiveFound
     * @throws ExceptionRedefinitionOfLabel
     * @throws ExceptionNoSymbolFound 
     */
    public void PopulateDirectiveArgAndAreaData(int step) throws ExceptionMissingRequiredDirective, ExceptionRedefinitionOfAreaDirective, ExceptionNoDirectiveFound, ExceptionNoParentSymbolFound, ExceptionMalformedEntryEndDirectiveSet, ExceptionNoAreaDirectiveFound, ExceptionRedefinitionOfLabel, ExceptionNoSymbolFound {
        if(eventHandler != null) {
            eventHandler.PopulateDirectiveArgAndAreaDataPre(step, this);
        }
        
        Logger.wrl("AssemblerThumb: PopulateDirectiveAndArgData");
        boolean dataDirectiveFound = false;
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
        boolean foundSubt = false;
        boolean foundArea = false;
        boolean foundOrg = false;
        
        String lastLabel = null;
        TokenLine lastLabelLine = null;
        Token lastLabelToken = null;
        Symbol symbol = null;
        
        for(TokenLine line : asmDataTokened) {
            lastLine = line;
            if(eventHandler != null) {
                eventHandler.PopulateDirectiveArgAndAreaDataLoopPre(step, this, line);
            }
            
            foundOrg = false;
            dataDirectiveFound= false;
            directiveFound = false;
            directiveName = null;
            directiveIdx = -1;            

            lastLabel = null;
            lastLabelLine = null;
            lastLabelToken = null;
            symbol = null;            
            
            if(lastData != -1 && line.isLineOpCode) {
                throw new ExceptionMalformedEntryEndDirectiveSet("Cannot have OpCode instructions when AREA type is DATA, found on line " + line.lineNumAbs + " with source " + line.source.source);                
            }
            
            for(Token token : line.payload) {
                lastToken = token;

                if(foundTtl && token.type_name.equals(JsonObjIsDirectives.NAME_DIRECTIVE_TYPE_STRING)) {
                    foundTtl = false;
                    assemblyTitle = token.source;
                
                } else if(foundSubt && token.type_name.equals(JsonObjIsDirectives.NAME_DIRECTIVE_TYPE_STRING)) {
                    foundSubt = false;
                    assemblySubTitle = token.source;                    
                    
                } else if(foundArea && token.type_name.equals(JsonObjIsDirectives.NAME_DIRECTIVE_TYPE_STRING)) {
                    tmpArea.title = token.source;
                    
                } else if(token.type_name.equals(JsonObjIsEntryTypes.NAME_LABEL)) {
                    if(token.index == 0) {
                        lastLabel = token.source;
                        lastLabelLine = line;
                        lastLabelToken = token;
                    }                    
                    
                } else if(token.type_name.equals(JsonObjIsEntryTypes.NAME_LABEL_REF)) {                    
                    if(dataDirectiveFound == true && directiveFound == true && foundArea == true) {
                        token.isDirectiveArg = true;
                    }
                    
                } else if(token.type_name.equals(JsonObjIsEntryTypes.NAME_NUMBER)) {
                    if(directiveFound == true && foundArea == true) {
                        token.isDirectiveArg = true;
                    }
                    
                    if(lastLabelToken != null && symbol != null) {
                        symbol.value = Utils.ParseNumberString(token.source);
                        symbols.symbols.put(lastLabel, symbol);
                        Logger.wrl("AssemblerThumb: PopulateDirectiveArgAndAreaData: Storing symbol with label '" + lastLabel + "' for line number " + lastLabelLine.lineNumAbs + " with value " + symbol.value);
                        
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
                        line.isLineEmpty = true;
                        
                    } else if(token.source.equals(JsonObjIsDirectives.NAME_SUB_TITLE)) {
                        foundSubt = true;
                        line.isLineEmpty = true;                        
                        
                    } else if(token.source.equals(JsonObjIsDirectives.NAME_ORG)) {                        
                        foundOrg = true;
                        line.isLineEmpty = true;
                        
                    } else if(token.source.equals(JsonObjIsDirectives.NAME_EQU)) {
                        line.isLineEmpty = true;
                        if(symbols.symbols.containsKey(lastLabel)) {
                            throw new ExceptionRedefinitionOfLabel("Found symbol '" + lastLabel + "' redefined on line " + lastLabelLine.lineNumAbs + " originally defned on line " + (symbols.symbols.get(lastLabel)).lineNumAbs);
                        }
                        symbol = new Symbol();
                        symbol.line = line;
                        symbol.lineNumAbs = line.lineNumAbs;
                        symbol.addressBin = line.addressBin;
                        symbol.addressHex = line.addressHex;
                        symbol.addressInt = line.addressInt;                       
                        symbol.name = lastLabel;
                        symbol.token = lastLabelToken;
                        symbol.isStaticValue = true;
                        
                    } else if(token.source.equals(JsonObjIsDirectives.NAME_AREA)) {
                        line.isLineEmpty = true;
                        if(lastArea == -1) {
                            foundArea = true;
                            lastArea = line.lineNumAbs;
                            lastAreaToken = token;
                            lastAreaTokenLine = line;

                            tmpArea = new AreaThumb();
                            tmpArea.area = lastAreaToken;
                            tmpArea.areaLine = lastAreaTokenLine;
                            tmpArea.lineNumArea = lastArea;
                        } else {
                            throw new ExceptionRedefinitionOfAreaDirective("Redefinition of AREA directive found on line " + line.lineNumAbs + " with source " + line.source.source);
                        }
                        
                    } else if(token.source.equals(JsonObjIsDirectives.NAME_CODE)) {
                        line.isLineEmpty = true;
                        if(lastCode == -1) {
                            lastCode = line.lineNumAbs;
                        }
                        tmpArea.isCode = true;
                        tmpArea.isReadOnly = true;
                        if(lastData != -1 && lastData == lastCode) {
                            throw new ExceptionMalformedEntryEndDirectiveSet("Cannot set AREA type to CODE when type is DATA, found on line " + line.lineNumAbs + " with source " + line.source.source);
                        }
                        
                    } else if(token.source.equals(JsonObjIsDirectives.NAME_DATA)) {
                        line.isLineEmpty = true;
                        if(lastData == -1) {
                            lastData = line.lineNumAbs;
                        }                        
                        tmpArea.isData = true;
                        tmpArea.isReadWrite = true;
                        if(lastCode != -1 && lastCode == lastData) {
                            throw new ExceptionMalformedEntryEndDirectiveSet("Cannot set AREA type to DATA when type is CODE, found on line " + line.lineNumAbs + " with source " + line.source.source);
                        }
                        
                    } else if(token.source.equals(JsonObjIsDirectives.NAME_READONLY)) {
                        line.isLineEmpty = true;
                        if(lastReadOnly == -1) {
                            lastReadOnly = line.lineNumAbs;
                        }
                        tmpArea.isReadOnly = true;
                        tmpArea.isReadWrite = false;
                        if(lastReadOnly != -1 && lastReadOnly == lastReadWrite) {
                            throw new ExceptionMalformedEntryEndDirectiveSet("Cannot set AREA type to READONLY when type is READWRITE, found on line " + line.lineNumAbs + " with source " + line.source.source);
                        }
                        
                    } else if(token.source.equals(JsonObjIsDirectives.NAME_READWRITE)) {
                        line.isLineEmpty = true;
                        if(lastReadWrite == -1) {
                            lastReadWrite = line.lineNumAbs;
                        }                        
                        tmpArea.isReadWrite = true;
                        tmpArea.isReadOnly = false;
                        if(lastReadWrite != -1 && lastReadWrite == lastReadOnly) {
                            throw new ExceptionMalformedEntryEndDirectiveSet("Cannot set AREA type to READWRITE when type is READONLY, found on line " + line.lineNumAbs + " with source " + line.source.source);
                        }
                        
                    } else if(token.source.equals(JsonObjIsDirectives.NAME_ENTRY)) {
                        line.isLineEmpty = true;
                        if(lastArea == -1) {
                            throw new ExceptionNoAreaDirectiveFound("Could not find AREA directive before ENTRY directive on line " + line.lineNumAbs + " with source " + line.source.source);
                        } else if(lastEntry == -1) {
                            lastEntry = line.lineNumAbs;
                            lastEntryToken = token;
                            lastEntryTokenLine = line;
                            
                            tmpArea.entry = lastEntryToken;
                            tmpArea.entryLine = lastEntryTokenLine;
                            tmpArea.lineNumEntry = lastEntry;
                        } else {
                            throw new ExceptionMalformedEntryEndDirectiveSet("Found multiple ENTRY directives with a new entry on line " + line.lineNumAbs + " with source " + line.source.source);
                        }
                        
                    } else if(token.source.equals(JsonObjIsDirectives.NAME_END)) {
                        line.isLineEmpty = true;
                        if(lastArea == -1) {
                            throw new ExceptionNoAreaDirectiveFound("Could not find AREA directive before ENTRY directive on line " + line.lineNumAbs + " with source " + line.source.source);
                        } else if(lastEntry == -1) {
                            throw new ExceptionMalformedEntryEndDirectiveSet("Could not find END directive before new ENTRY directive on line " + line.lineNumAbs + " with source " + line.source.source);
                        } else if(lastEnd == -1) {
                            lastEnd = line.lineNumAbs;
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
                                throw new ExceptionMalformedEntryEndDirectiveSet("Could not find END directive before new ENTRY directive on line " + line.lineNumAbs + " with source " + line.source.source);
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
                            throw new ExceptionMalformedEntryEndDirectiveSet("Could not find END directive before new ENTRY directive on line " + line.lineNumAbs + " with source " + line.source.source);
                        }
                    } else if(token.source.equals(JsonObjIsDirectives.NAME_DCHW) || token.source.equals(JsonObjIsDirectives.NAME_DCB) || token.source.equals(JsonObjIsDirectives.NAME_FLPDCHW) || token.source.equals(JsonObjIsDirectives.NAME_DCWBF) || token.source.equals(JsonObjIsDirectives.NAME_DCWBS)) {
                        if(lastArea == -1 || tmpArea == null) {
                            throw new ExceptionNoAreaDirectiveFound("Could not find AREA directive before directive '" + token.source + "' on line " + line.lineNumAbs + " with source " + line.source.source);
                        } else {
                            dataDirectiveFound = true;
                            token.isDirective = true;
                            if(!directiveFound) {
                                directiveFound = true;
                                directiveName = token.source;
                                directiveIdx = token.index;
                            }
                            
                            if(lastLabel != null) {
                                symbol = symbols.symbols.get(lastLabel);
                                if(symbol == null) {
                                    throw new ExceptionNoSymbolFound("Could not find a symbol with name " + lastLabel + " at line number " + line.lineNumAbs + " with source " + line.source.source);                                    
                                }
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
                    throw new ExceptionNoDirectiveFound("Could not find a matching directive entry for name '" + line.payloadDirective + "' with argument count " + line.payloadLenArg + " at line " + line.lineNumAbs + " with source text '" + line.source.source + "'");
                }
            }
            
            if(eventHandler != null) {
                eventHandler.PopulateDirectiveArgAndAreaDataLoopPost(step, this, line);
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
                    if(!line.isLineEmpty && !line.isLineLabelDef) {
                        line.addressHex = Utils.FormatHexString(Integer.toHexString(asmStartLineNumber + activeLineCount), lineLenBytes);
                        line.addressBin = Utils.FormatBinString(Integer.toBinaryString(asmStartLineNumber + activeLineCount), lineBitSeries.bit_len);
                        line.addressInt = (asmStartLineNumber + activeLineCount);
                        line.lineNumActive = (line.addressInt/lineLenBytes);
                        asmAreaLinesCode.add(line);
                        activeLineCount += lineLenBytes;
                        if(line.isLineOpCode && line.payloadOpCode.equals(JsonObjIsOpCodes.NAME_BL)) {
                            activeLineCount += lineLenBytes;
                            line.byteLength = lineLenBytes * 2;
                        }
                    }
                }

                int alcStart = activeLineCount;
                int firstDataLineActiveInt = -1;
                int firstDataLineAbsInt = -1;                
                int firstDataLineInt = -1;
                asmAreaLinesData = new ArrayList<TokenLine>();
                for(int z = areaThumbData.lineNumEntry + 1; z < areaThumbData.lineNumEnd; z++) {
                    TokenLine line = asmDataTokened.get(z);
                    if(firstDataLineInt == -1) {
                        firstDataLineInt = z;
                    }
                    
                    if(!line.isLineEmpty && !line.isLineLabelDef && line.isLineDirective) {
                        line.addressHex = Utils.FormatHexString(Integer.toHexString(asmStartLineNumber + activeLineCount), lineLenBytes);
                        line.addressBin = Utils.FormatBinString(Integer.toBinaryString(asmStartLineNumber + activeLineCount), lineBitSeries.bit_len); 
                        line.addressInt = (asmStartLineNumber + activeLineCount);
                        line.lineNumActive = (line.addressInt/lineLenBytes);
                        
                        if(firstDataLineActiveInt == -1) {
                            firstDataLineActiveInt = line.lineNumActive;
                        }
                        
                        if(firstDataLineAbsInt == -1) {
                            firstDataLineAbsInt = line.lineNumAbs;
                        }
                        
                        asmAreaLinesData.add(line);
                        activeLineCount += lineLenBytes;
                    }
                }
                
                int rmn = (firstDataLineActiveInt * 2) % 4;
                int lineAddedAt = -1;
                if(rmn == 2) {   
                    Logger.wrl("WARNING: Offset found to be " + rmn + " adding spacer line to align to 4 byte boundary.");
                    lineAddedAt = firstDataLineInt;
                    asmDataTokened.add(firstDataLineInt, jsonObjIsEmptyDataLines.is_empty_data_lines.get(0));
                    areaThumbData.lineNumEnd += 1;
                } else {
                    Logger.wrl("WARNING: Unexpected alignment with offset found to be " + rmn);
                }
               
                if(lineAddedAt != -1) {
                    TokenLine lastCodeLine = asmDataTokened.get(areaThumbCode.lineNumEnd - 1);
                    TokenLine firstDataLine = asmDataTokened.get(areaThumbData.lineNumEntry);
                    int countAbs = 1;
                    int countActive = 1;
                    int startLineAbs = firstDataLine.lineNumAbs;
                    int startLineActive = lastCodeLine.lineNumActive;                
                    for(int z = areaThumbData.lineNumEntry + 1; z <= areaThumbData.lineNumEnd; z++) {
                        TokenLine line = asmDataTokened.get(z);
                        line.lineNumAbs = startLineAbs + countAbs;
                        if(!line.isLineEmpty && !line.isLineLabelDef && line.isLineDirective) {
                            line.lineNumActive = startLineActive + countActive;
                            countActive++;
                        }
                        countAbs++;
                    }                

                    activeLineCount = alcStart;
                    asmAreaLinesData = new ArrayList<TokenLine>();
                    for(int z = areaThumbData.lineNumEntry + 1; z < areaThumbData.lineNumEnd; z++) {
                        TokenLine line = asmDataTokened.get(z);
                        if(!line.isLineEmpty && !line.isLineLabelDef && line.isLineDirective) {
                            line.addressHex = Utils.FormatHexString(Integer.toHexString(asmStartLineNumber + activeLineCount), lineLenBytes);
                            line.addressBin = Utils.FormatBinString(Integer.toBinaryString(asmStartLineNumber + activeLineCount), lineBitSeries.bit_len); 
                            line.addressInt = (asmStartLineNumber + activeLineCount);
                            line.lineNumActive = (line.addressInt/lineLenBytes);
                            asmAreaLinesData.add(line);
                            activeLineCount += lineLenBytes;
                        }
                    }

                    for(String key : symbols.symbols.keySet()) {
                        Symbol sym = symbols.symbols.get(key);
                        if(sym.lineNumAbs >= lineAddedAt) {
                            Logger.wrl("==============Need to adjust symbol: " + sym.name);
                            sym.lineNumAbs += 1;
                        }
                    }
                }

            } else {
                //process data area first
                asmAreaLinesData = new ArrayList<TokenLine>();
                activeLineCount = 0;
                for(int z = areaThumbData.lineNumEntry + 1; z < areaThumbData.lineNumEnd; z++) {
                    TokenLine line = asmDataTokened.get(z);
                    if(!line.isLineEmpty && !line.isLineLabelDef && line.isLineDirective ) {
                        line.addressHex = Utils.FormatHexString(Integer.toHexString(asmStartLineNumber + activeLineCount), lineLenBytes);
                        line.addressBin = Utils.FormatBinString(Integer.toBinaryString(asmStartLineNumber + activeLineCount), lineBitSeries.bit_len); 
                        line.addressInt = (asmStartLineNumber + activeLineCount);
                        line.lineNumActive = (line.addressInt/lineLenBytes);
                        asmAreaLinesData.add(line);
                        activeLineCount += lineLenBytes;
                    }
                }
                                
                asmAreaLinesCode = new ArrayList<TokenLine>();
                for(int z = areaThumbCode.lineNumEntry + 1; z < areaThumbCode.lineNumEnd; z++) {
                    TokenLine line = asmDataTokened.get(z);
                    if(!line.isLineEmpty && !line.isLineLabelDef) {
                        line.addressHex = Utils.FormatHexString(Integer.toHexString(asmStartLineNumber + activeLineCount), lineLenBytes);
                        line.addressBin = Utils.FormatBinString(Integer.toBinaryString(asmStartLineNumber + activeLineCount), lineBitSeries.bit_len); 
                        line.addressInt = (asmStartLineNumber + activeLineCount);
                        line.lineNumActive = (line.addressInt/lineLenBytes);
                        asmAreaLinesCode.add(line);
                        activeLineCount += lineLenBytes;
                        if(line.isLineOpCode && line.payloadOpCode.equals(JsonObjIsOpCodes.NAME_BL)) {
                            activeLineCount += lineLenBytes;
                            line.byteLength = lineLenBytes * 2;
                        }                        
                    }
                }
            }
            
        } else if(areaThumbCode != null) {
            //process code area first
            asmAreaLinesCode = new ArrayList<TokenLine>();
            activeLineCount = 0;
            for(int z = areaThumbCode.lineNumEntry + 1; z < areaThumbCode.lineNumEnd; z++) {
                TokenLine line = asmDataTokened.get(z);
                if(!line.isLineEmpty && !line.isLineLabelDef) {
                    line.addressHex = Utils.FormatHexString(Integer.toHexString(asmStartLineNumber + activeLineCount), lineLenBytes);
                    line.addressBin = Utils.FormatBinString(Integer.toBinaryString(asmStartLineNumber + activeLineCount), lineBitSeries.bit_len);
                    line.addressInt = (asmStartLineNumber + activeLineCount);
                    line.lineNumActive = (line.addressInt/lineLenBytes);
                    asmAreaLinesCode.add(line);
                    activeLineCount += lineLenBytes;
                    if(line.isLineOpCode && line.payloadOpCode.equals(JsonObjIsOpCodes.NAME_BL)) {
                        activeLineCount += lineLenBytes;
                        line.byteLength = lineLenBytes * 2;
                    }                    
                }
            }
            
        } else {
            throw new ExceptionMalformedEntryEndDirectiveSet("Cannot have only a DATA AREA, CODE AREA is required");
        }
        
        //clean symbol values
        List<String> del = new ArrayList<>();
        for(String key : symbols.symbols.keySet()) {
            Symbol sym = symbols.symbols.get(key);
            if(sym != null) {
                if(sym.isStaticValue == false) {
                    sym.value = null;
                }
            } else {
                del.add(key);
                Logger.wrl("WARNING: Found NULL symbol entry to remove at key: " + key);
            }
        }
        
        if(del != null && del.isEmpty() == false) {
            for(String key : del) {
                Logger.wrl("Removing NULL symbol from symbols table with key: " + key);
                symbols.symbols.remove(key);
            }
        }
        
        if(eventHandler != null) {
            eventHandler.PopulateDirectiveArgAndAreaDataPost(step, this);
        }        
    }    
    
    /**
     * A method to validate the signature of directive lines.
     * @param step An integer representing the current assembly step.
     * @throws ExceptionNoDirectiveFound 
     */
    public void ValidateDirectiveLines(int step) throws ExceptionNoDirectiveFound {
        if(eventHandler != null) {
            eventHandler.ValidateDirectiveLinesPre(step, this);
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
                eventHandler.ValidateDirectiveLinesLoopPre(step, this, line);
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
                    throw new ExceptionNoDirectiveFound("Could not find unique matching directive entry for directive '" + directiveName + "' and line number " + line.lineNumAbs + " with source '" + line.source.source + "'");
                }
            }
            
            if(eventHandler != null) {
                eventHandler.ValidateDirectiveLinesLoopPost(step, this, line);
            }            
        }
        
        if(eventHandler != null) {
            eventHandler.ValidateDirectiveLinesPost(step, this);
        }
    }
    
    /**
     * A helper method used to find directive argument matches for a list of tokens.
     * @param directiveMatches              A list of supported instruction sets.
     * @param args                          A list of tokens that represent the arguments for a directive.
     * @param directiveToken                A token representing the current directive.
     * @return                              The matched directive.
     * @throws ExceptionNoDirectiveFound 
     */
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
        
        throw new ExceptionNoDirectiveFound("Could not find a directive that has matching arguments for line number " + directiveToken.lineNumAbs + " with directive '" + directiveToken.source + "' at directive argument index " + directiveArgIdx);
    }    
    
    /**
     * A helper method used to find directive matches for a directive name.
     * @param directiveName A string representing the name of the directive.
     * @param argLen        An integer representing the number of arguments.
     * @return              A list of matched directives.
     */
    public List<JsonObjIsDirective> FindDirectiveMatches(String directiveName, int argLen) {
        List<JsonObjIsDirective> ret = new ArrayList<>();
        for(JsonObjIsDirective directive : jsonObjIsDirectives.is_directives) {
            if(directive.directive_name.equals(directiveName) && directive.arg_len == argLen) {
                ret.add(directive);
            }
        }
        return ret;
    }
    
    /**
     * A helper method used to determine if the given token line has a directive token.
     * @param line  The token line to scan for directives.
     * @return      The directive if found or null if none is found.
     */
    public Token FindFirstDirective(TokenLine line) {
        for(Token token : line.payload) {
            if(token.type_name.equals(JsonObjIsEntryTypes.NAME_DIRECTIVE)) {
                return token;
            }
        }
        return null;
    }
    
    //OPCODE METHODS
    /**
     * A method used to mark op-code and op-code argument tokens.
     * @param step An integer representing the current assembly step.
     * @throws ExceptionRedefinitionOfLabel
     * @throws ExceptionNoOpCodeFound
     * @throws ExceptionNoParentSymbolFound
     * @throws ExceptionOpCodeAsArgument
     * @throws ExceptionMissingRequiredDirective 
     */
    public void PopulateOpCodeAndArgData(int step) throws ExceptionRedefinitionOfLabel, ExceptionNoOpCodeFound, ExceptionNoParentSymbolFound, ExceptionOpCodeAsArgument, ExceptionMissingRequiredDirective {
        if(eventHandler != null) {
            eventHandler.PopulateOpCodeAndArgDataPre(step, this);
        }
        
        Logger.wrl("AssemblerThumb: PopulateOpCodeAndArgData");        
        boolean opCodeFound = false;
        String opCodeName = null;
        int opCodeIdx = -1;
        int labelArgs = -1;
        String lastLabel = null;
        TokenLine lastLabelLine = null;
        Symbol symbol = null;
        boolean directiveFound = false;
        boolean labelFound = false;
        
        for(TokenLine line : asmDataTokened) {
            lastLine = line;
            if(eventHandler != null) {
                eventHandler.PopulateOpCodeAndArgDataLoopPre(step, this, line);
            }             
            
            if(line.validLineEntry.index == 0 || line.validLineEntry.index == 9) {
                line.isLineEmpty = true;
            }
            
            opCodeFound = false;
            opCodeName = null;
            opCodeIdx = -1;
            labelArgs = 0;
            directiveFound = false;
            labelFound = false;
            
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
                        throw new ExceptionOpCodeAsArgument("Found OpCode token entry where a sub-argument should be on line " + line.lineNumAbs + " with argument index " + token.index);
                    }
                    
                } else if(token.type_name.equals(JsonObjIsEntryTypes.NAME_DIRECTIVE)) {    
                    if(!directiveFound) {
                        directiveFound = true;
                    }
                    
                } else if(token.type_name.equals(JsonObjIsEntryTypes.NAME_START_LIST) || token.type_name.equals(JsonObjIsEntryTypes.NAME_START_GROUP) || token.type_name.equals(JsonObjIsEntryTypes.NAME_STOP_LIST) || token.type_name.equals(JsonObjIsEntryTypes.NAME_STOP_GROUP)) {                    
                    token.isOpCodeArg = true;
                                        
                } else if(token.type_name.equals(JsonObjIsEntryTypes.NAME_LABEL)) {                    
                    if(token.index == 0) {
                        labelFound = true;
                        Token ltTmp = FindFirstDirective(line);
                        if(ltTmp == null || (ltTmp != null && Utils.ArrayContainsString(JsonObjIsDirectives.LABEL_DIRECTIVES, ltTmp.source) == true)) {
                            if(ltTmp == null || (ltTmp != null && ltTmp.source.equals(JsonObjIsDirectives.NAME_EQU) == false)) {
                                lastLabel = token.source;
                                lastLabelLine = line;
                                if(symbols.symbols.containsKey(lastLabel)) {
                                    throw new ExceptionRedefinitionOfLabel("Found symbol '" + lastLabel + "' redefined on line " + lastLabelLine.lineNumAbs + " originally defned on line " + (symbols.symbols.get(lastLabel)).lineNumAbs);
                                } else {
                                    Logger.wrl("AssemblerThumb: PopulateOpCodeAndArgData: Storing symbol with label '" + token.source + "' for line number " + line.lineNumAbs);
                                }
                                symbol = new Symbol();
                                symbol.line = line;
                                symbol.lineNumAbs = line.lineNumAbs;
                                symbol.addressBin = line.addressBin;
                                symbol.addressHex = line.addressHex;
                                symbol.addressInt = line.addressInt;
                                symbol.name = token.source;
                                symbol.token = token;
                                symbol.isLabel = true;
                                symbol.value = null;
                                symbols.symbols.put(token.source, symbol);
                            }
                            
                        } else {
                            throw new ExceptionMissingRequiredDirective("Found symbol '" + lastLabel + "' found on line " + lastLabelLine.lineNumAbs + " is missing required directive EQU that is expected when no OpCode is used.");
                            
                        }
                    }
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
                        throw new ExceptionOpCodeAsArgument("Found OpCode token entry where a sub-argument should be on line " + line.lineNumAbs + " with argument index " + ltoken.index + " and parent argument index " + token.index);

                    } else if(ltoken.type_name.equals(JsonObjIsEntryTypes.NAME_LABEL_REF) || ltoken.type_name.equals(JsonObjIsEntryTypes.NAME_START_LIST) || ltoken.type_name.equals(JsonObjIsEntryTypes.NAME_START_GROUP) || ltoken.type_name.equals(JsonObjIsEntryTypes.NAME_STOP_LIST) || ltoken.type_name.equals(JsonObjIsEntryTypes.NAME_STOP_GROUP)) {                    
                        ltoken.isOpCodeArg = true;

                    }
                }
            }
            
            if(opCodeFound) {
                line.payloadOpCode = opCodeName;
                line.isLineOpCode = true;
                line.payloadLenArg = CountArgTokens(line.payload, 0); // + labelArgs;  /* No longer needed */
                line.matchesOpCode = FindOpCodeMatches(line.payloadOpCode, line.payloadLenArg);
                
                if(line.matchesOpCode == null || line.matchesOpCode.isEmpty()) {
                    throw new ExceptionNoOpCodeFound("Could not find a matching opCode entry for name '" + line.payloadOpCode + "' with argument count " + line.payloadLenArg + " at line " + line.lineNumAbs + " with source text '" + line.source.source + "'");
                }
            }

            if(!opCodeFound && !directiveFound && labelFound) {
                line.isLineLabelDef = true;
            }            
            
            if(eventHandler != null) {
                eventHandler.PopulateOpCodeAndArgDataLoopPost(step, this, line);
            }            
        }
        
        if(eventHandler != null) {
            eventHandler.PopulateOpCodeAndArgDataPost(step, this);
        }        
    }    
    
    /**
     * A method used to validate the signature of the op-code line.
     * @param step An integer representing the current assembly step.
     * @throws ExceptionNoOpCodeFound
     * @throws ExceptionNoOpCodeLineFound 
     */
    public void ValidateOpCodeLines(int step) throws ExceptionNoOpCodeFound, ExceptionNoOpCodeLineFound {
        if(eventHandler != null) {
            eventHandler.ValidateOpCodeLinesPre(step, this);
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
                eventHandler.ValidateOpCodeLinesLoopPre(step, this, line);
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
                    throw new ExceptionNoOpCodeFound("Could not find unique matching opCode entry for opCode '" + opCodeName + "' and line number " + line.lineNumAbs + " with source '" + line.source.source + "'");
                }
            }
            
            if(eventHandler != null) {
                eventHandler.ValidateOpCodeLinesLoopPost(step, this, line);
            }            
        }
        
        for(String key : symbols.symbols.keySet()) {
            Symbol symbol = symbols.symbols.get(key);
            TokenLine line = asmDataTokened.get(symbol.lineNumAbs);
            TokenLine tmpLine = null;
            if(Utils.ArrayContainsInt(JsonObjIsValidLines.LINES_LABEL_EMPTY, line.validLineEntry.index)) {
                tmpLine = FindNextOpCodeLine(symbol.lineNumAbs, key);
                symbol.addressInt = tmpLine.addressInt;
                symbol.addressBin = tmpLine.addressBin;
                symbol.addressHex = tmpLine.addressHex;
                symbol.lineNumActive = tmpLine.lineNumActive;
                symbol.isEmptyLineLabel = true;
                Logger.wrl("Adjusting symbol, '" + symbol.name + "', line number from " + symbol.lineNumAbs + " to " + symbol.addressInt + " due to symbol marking an empty line");
            } else {
                symbol.addressInt = line.addressInt;
                symbol.addressBin = line.addressBin;
                symbol.addressHex = line.addressHex;
                symbol.lineNumActive = line.lineNumActive;                
            }
        }
        
        if(eventHandler != null) {
            eventHandler.ValidateOpCodeLinesPost(step, this);
        }        
    }
    
    /**
     * A helper method that finds the next op-code line.
     * @param lineNum   An integer representing the line number of the line to start searching on.
     * @param label     A string representing the label that initiated the op-code line search. 
     * @return          A token line if a match is found otherwise null.
     * @throws ExceptionNoOpCodeLineFound 
     */
    public TokenLine FindNextOpCodeLine(int lineNum, String label) throws ExceptionNoOpCodeLineFound {
        if(lineNum + 1 < asmDataTokened.size()) {
            for(int i = lineNum + 1; i < asmDataTokened.size(); i++) {
                TokenLine line = asmDataTokened.get(i);
                if((line.isLineOpCode || line.isLineDirective) && !line.isLineLabelDef && !line.isLineEmpty) {
                    return line;
                }
            }
        }
        throw new ExceptionNoOpCodeLineFound("Could not find an OpCode line for label '" + label + "' on empty line " + lineNum);
    }
    
    /**
     * A helper method that finds an op-code argument match.
     * @param opCodeMatches             A list of op-codes that match the token line op-code.
     * @param args                      A list of tokens representing the op-code arguments.
     * @param opCodeToken               A token object representing the op-code.
     * @return                          A matching op-code entry.
     * @throws ExceptionNoOpCodeFound 
     */
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
                    if(!(opCodeArg.is_entry_types.contains(argToken.type_name))) {
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
                                    for(int k = j + 1; k < argToken.payload.size(); k++) {
                                        if(argToken.payload.get(k).type_name.equals("RegisterLow")) {
                                            regRangeOffset++;
                                        }
                                    }
                                } else if(opCodeArgSub.is_entry_types.contains("RegisterRangeHi") && argTokenSub.type.type_category.equals("RegisterHi")) {
                                    for(int k = j + 1; k < argToken.payload.size(); k++) {
                                        if(argToken.payload.get(k).type.type_category.equals("RegisterHi")) {
                                            regRangeOffset++;
                                        }
                                    }
                                } else {
                                    if(!(opCodeArgSub.is_entry_types.contains(argTokenSub.type_name))) {
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
        
        throw new ExceptionNoOpCodeFound("Could not find an opCode that has matching arguments for line number " + opCodeToken.lineNumAbs + " with opCode '" + opCodeToken.source + "' at opCode argument index " + opCodeArgIdx + " with sub argument index " + opCodeArgIdxSub);
    }    
    
    /**
     * A helper method that finds an op-code match.
     * @param opCodeName    A string representation of the op-code.
     * @param argLen        An integer indicating the number of arguments.
     * @return              A list of matching op-code entries.
     */
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
    /**
     * A helper method used to count the number of matching argument tokens.
     * @param payload   A list of argument tokens.
     * @param argCount  An integer representing the number argument tokens.
     * @return          The number of matching argument tokens found.
     */
    public int CountArgTokens(List<Token> payload, int argCount) {
        return CountArgTokens(payload, argCount, JsonObjIsEntryTypes.NAME_CAT_ARG_OPCODE, true);
    }
    
    /**
     * A helper method used to count the number of matching argument tokens.
     * @param payload       A list of argument tokens.
     * @param argCount      An integer representing the number argument tokens.
     * @param argCategory   A string representing the argument category.
     * @param isOpCodeArg   A Boolean value indicating if the argument is an op-code argument.
     * @return              The number of matching argument tokens found.
     */
    public int CountArgTokens(List<Token> payload, int argCount, String argCategory, boolean isOpCodeArg) {
        for(Token token : payload) {
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
    /**
     * A helper method used to collapse group and list tokens into sub-tokens.
     * @param step An integer representing the current assembly step.
     * @throws ExceptionNoClosingBracketFound
     * @throws ExceptionListAndGroup 
     */
    public void CollapseListAndGroupTokens(int step) throws ExceptionNoClosingBracketFound, ExceptionListAndGroup {
        if(eventHandler != null) {
            eventHandler.CollapseListAndGroupTokensPre(step, this);
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
                eventHandler.CollapseListAndGroupTokensLoopPre(step, this, line);
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
                eventHandler.CollapseListAndGroupTokensLoopPost(step, this, line);
            }            
        }
        
        if(eventHandler != null) {
            eventHandler.CollapseListAndGroupTokensPost(step, this);
        }
    }    
    
    /**
     * A helper method used to find a matching entry type by name.
     * @param entryName A string representing the entry to match.
     * @return          The matching entry type entry.
     * @throws ExceptionNoEntryFound 
     */
    public JsonObjIsEntryType FindEntryType(String entryName) throws ExceptionNoEntryFound {        
        for(JsonObjIsEntryType entry : jsonObjIsEntryTypes.is_entry_types) {
            if(entry.type_name.equals(entryName)) {
                return entry;
            }
        }
        throw new ExceptionNoEntryFound("Could not find entry by name, '" + entryName + "', in loaded entry types.");
    }
    
    /**
     * A helper method used to clean the register range string by parsing it and reassembling it.
     * @param range         A string representation of the register range.
     * @param rangeDelim    A string representing the range delimiter.
     * @return              A string representing the clean register range.
     */
    public String CleanRegisterRangeString(String range, String rangeDelim) {
        String ret = "";
        for(char c : range.toCharArray()) {
            if((c + "").equals(rangeDelim) || Character.isDigit(c)) {
                ret += c;
            }
        }
        return ret;
    }
    
    /**
     * A helper method used to clean the token source field of line and argument separators.
     * @param token An object representing the token to clean the source for.
     */
    public void CleanTokenSource(Token token) {
        if(token != null && !Utils.IsStringEmpty(token.source)) {
            token.source = token.source.replace(JsonObjIsOpCode.DEFAULT_ARG_SEPARATOR, "");
            token.source = token.source.replace(System.lineSeparator(), "");
        }
    }
    
    /**
     * A helper method used to expand the register range into individual register token entries.
     * @param step An integer representing the current assembly step.
     * @throws ExceptionNoEntryFound
     * @throws ExceptionMalformedRange 
     */
    public void ExpandRegisterRangeTokens(int step) throws ExceptionNoEntryFound, ExceptionMalformedRange {
        if(eventHandler != null) {
            eventHandler.ExpandRegisterRangeTokensPre(step, this);
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
                eventHandler.ExpandRegisterRangeTokensLoopPre(step, this, line);
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
                        newToken.lineNumAbs = rangeRootLow.lineNumAbs;
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
                        newToken.lineNumAbs = rangeRootHi.lineNumAbs;
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
                eventHandler.ExpandRegisterRangeTokensLoopPost(step, this, line);
            }            
        }
        
        if(eventHandler != null) {
            eventHandler.ExpandRegisterRangeTokensPost(step, this);
        }        
    }
    
    /**
     * A helper method used to collapse the comment tokens into sub-tokens of the first comment token.
     * @param step An integer representing the current assembly step.
     */
    public void CollapseCommentTokens(int step) {
        if(eventHandler != null) {
            eventHandler.CollapseCommentTokensPre(step, this);
        }
        
        Logger.wrl("AssemblerThumb: CollapseCommentTokens");
        boolean inComment = false;
        Token commentRoot = null;
        List<Token> clearTokens = null;  
            
        for(TokenLine line : asmDataTokened) {
            lastLine = line;
            if(eventHandler != null) {
                eventHandler.CollapseCommentTokensLoopPre(step, this, line);
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
                eventHandler.CollapseCommentTokensLoopPost(step, this, line);
            }            
        }
        
        if(eventHandler != null) {
            eventHandler.CollapseCommentTokensPost(step, this);
        }        
    }
    
    //LOAD, PARSE, LINK JSONOBJ DATA METHODS
    /**
     * A helper method that is responsible for loading and parsing of the instruction set JSON data file.
     * These data files define the instruction set.
     * @param step An integer representing the current assembly step.
     * @throws ExceptionNoEntryFound
     * @throws ExceptionLoader
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException 
     */
    public void LoadAndParseJsonObjData(int step) throws ExceptionNoEntryFound, ExceptionLoader, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if(eventHandler != null) {
            eventHandler.LoadAndParseJsonObjDataPre(step, this);
        }
        
        Logger.wrl("AssemblerThumb: LoadAndParseJsonObjData");
        Class cTmp = null;
        Loader ldr = null;
        String json = null;
        String jsonName = null;
        JsonObj jsonObj = null;
        
        for(JsonObjIsFile entry : isaDataSet.is_files) {
            if(eventHandler != null) {
                eventHandler.LoadAndParseJsonObjDataLoopPre(step, this, entry);
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
            Logger.wrl("AssemblerThumb: RunAssembler: " + jsonObj.GetName());
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
                
            } else if(jsonObj.GetLoader().equals("net.middlemind.GenAsm.Loaders.Thumb.LoaderIsEmptyDataLines")) {
                jsonObjIsEmptyDataLines = (JsonObjIsEmptyDataLines)jsonObj;
                Logger.wrl("AssemblerThumb: RunAssembler: Found JsonObjIsEmptyDataLines object, storing it...");                

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
                eventHandler.LoadAndParseJsonObjDataLoopPost(step, this, entry);
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
            eventHandler.LoadAndParseJsonObjDataPost(step, this);
        }        
    }
    
    /**
     * A helper method that is used to link loaded JSON object data to other loaded JSON object data.
     * @param step An integer representing the current assembly step.
     * @throws ExceptionJsonObjLink 
     */
    public void LinkJsonObjData(int step) throws ExceptionJsonObjLink {
        if(eventHandler != null) {
            eventHandler.LinkJsonObjDataPre(step, this);
        }
        
        Logger.wrl("AssemblerThumb: LinkJsonObjData");
        for(String s : isaData.keySet()) {
            if(eventHandler != null) {
                eventHandler.LinkJsonObjDataLoopPre(step, this, s);
            }            
            
            JsonObj jsonObj = isaData.get(s);
            jsonObj.Link(jsonObjIsEntryTypes);
            
            if(eventHandler != null) {
                eventHandler.LinkJsonObjDataLoopPost(step, this, s);
            }            
        }
        
        if(eventHandler != null) {
            eventHandler.LinkJsonObjDataPost(step, this);
        }        
    }
    
    //LEX SOURCE CODE
    /**
     * A main method used to lexerize the assembly source code. 
     * Lexerizing, is the process of converting assembly source text to artifacts.
     * @param step An integer representing the current assembly step.
     * @throws IOException 
     */
    public void LexerizeAssemblySource(int step) throws IOException {
        if(eventHandler != null) {
            eventHandler.LexerizeAssemblySourcePre(step, this);
        }        
        
        Logger.wrl("AssemblerThumb: LoadAndLexAssemblySource: Lexerize assembly source file");
        LexerThumb lex = new LexerThumb();
        asmDataLexed = lex.FileLexerize(asmDataSource);
        
        if(eventHandler != null) {
            eventHandler.LexerizeAssemblySourcePost(step, this);
        }        
    }
    
    //TOKENIZE AND VALIDATE LEXERIZED LINES
    /**
     * A main method used to tokenize the lexerized assembly source code.
     * Tokenizing, is the process of converting the artifacts to tokens.
     * @param step An integer representing the current assembly step.
     * @throws ExceptionNoTokenerFound 
     */
    public void TokenizeLexerArtifacts(int step) throws ExceptionNoTokenerFound {
        if(eventHandler != null) {
            eventHandler.TokenizeLexerArtifactsPre(step, this);
        }         
        
        Logger.wrl("AssemblerThumb: TokenizeLexerArtifacts");
        TokenerThumb tok = new TokenerThumb();
        asmDataTokened = tok.FileTokenize(asmDataLexed, jsonObjIsEntryTypes);
        
        if(eventHandler != null) {
            eventHandler.TokenizeLexerArtifactsPost(step, this);
        }         
    }
    
    /**
     * A helper method used to find a valid line entry with matching token entry type.
     * @param validLine A set of valid line entries to search through for a matching entry type.
     * @param token     The token instance entry type to search for.
     * @param entry     An integer representing the minimum index of the match.
     * @param index     An integer representing the valid line entry index. Currently unused.
     * @return 
     */
    public int[] FindValidLineEntryWithMatchingTokenEntryType(JsonObjIsValidLine validLine, Token token, int entry, int index) {
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
    
    /**
     * A main method that validates a token line.
     * @param step              An integer representing the current assembly step.
     * @param line              The token line to validate.
     * @param validLines        An object the represents all the valid lines for this instruction set.
     * @param validLineEmpty    An object that represents the empty line.
     * @return 
     */
    public boolean ValidateTokenizedLine(int step, TokenLine line, JsonObjIsValidLines validLines, JsonObjIsValidLine validLineEmpty) {
        if(eventHandler != null) {
            eventHandler.ValidateTokenizedLinePre(step, this, line, validLines, validLineEmpty);
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

                res = FindValidLineEntryWithMatchingTokenEntryType(validLine, token, currentEntry, 0);
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
            eventHandler.ValidateTokenizedLinePost(step, this);
        }        
        
        return false;
    }
    
    /**
     * A main method that validates all token lines
     * @param step              An integer representing the current assembly step.
     * @return                  A Boolean value indicating that the tokenized lines are valid.
     * @throws ExceptionNoValidLineFound 
     */
    public boolean ValidateTokenizedLines(int step) throws ExceptionNoValidLineFound {
        if(eventHandler != null) {
            eventHandler.ValidateTokenizedLinesPre(step, this);
        }

        Logger.wrl("AssemblerThumb: ValidateTokenizedLines");
        for(TokenLine line : asmDataTokened) {
            lastLine = line;
            if(eventHandler != null) {
                eventHandler.ValidateTokenizedLinesLoopPre(step, this, line);
            }
            
            if(!ValidateTokenizedLine(step, line, jsonObjIsValidLines, jsonObjIsValidLines.is_valid_lines.get(JsonObjIsValidLines.LINE_EMPTY))) {
                throw new ExceptionNoValidLineFound("Could not find a matching valid line for line number, " + line.lineNumAbs + " with source text, '" + line.source.source + "'");
            }
            
            if(eventHandler != null) {
                eventHandler.ValidateTokenizedLinesLoopPost(step, this, line);
            }            
        }
        
        if(eventHandler != null) {
            eventHandler.ValidateTokenizedLinesPost(step, this);
        }        
        
        return true;
    }
    
    //BUILD OPCODES AND DIRECTIVES
    /**
     * A main method that builds a binary representation of all tokenized lines.
     * @param step          An integer representing the current assembly step.
     * @param areaLines     A list of token lines that belong to the current area.
     * @param area          The area thumb object that the lines belong to.
     * @throws ExceptionOpCodeAsArgument
     * @throws ExceptionNoSymbolFound
     * @throws ExceptionUnexpectedTokenWithSubArguments
     * @throws ExceptionNumberInvalidShift
     * @throws ExceptionNumberOutOfRange
     * @throws ExceptionNoNumberRangeFound
     * @throws ExceptionUnexpectedTokenType
     * @throws ExceptionInvalidEntry
     * @throws ExceptionInvalidArea
     * @throws ExceptionInvalidAssemblyLine
     * @throws ExceptionDirectiveArgNotSupported
     * @throws ExceptionMissingDataDirective
     * @throws ExceptionNoSymbolValueFound 
     */
    public void BuildBinLines(int step, List<TokenLine> areaLines, AreaThumb area) throws ExceptionOpCodeAsArgument, ExceptionNoSymbolFound, ExceptionUnexpectedTokenWithSubArguments, ExceptionNumberInvalidShift, ExceptionNumberOutOfRange, ExceptionNoNumberRangeFound, ExceptionUnexpectedTokenType, ExceptionInvalidEntry, ExceptionInvalidArea, ExceptionInvalidAssemblyLine, ExceptionDirectiveArgNotSupported, ExceptionMissingDataDirective, ExceptionNoSymbolValueFound {
        if(eventHandler != null) {
            eventHandler.BuildBinLinesPre(step, this, areaLines, area);
        }

        if(area.isCode) {
            for(TokenLine line : areaLines) {
                lastLine = line;
                BuildBinOpCode(step, line);
                BuildBinDirective(step, line);
            }
            
        } else if(area.isData) {
            for(TokenLine line : areaLines) {
                lastLine = line;
                BuildBinDirective(step, line);
            }
            
        } else {
            throw new ExceptionInvalidArea("Found an invalid area entry at line number " + area.areaLine + " width code: " + area.isCode + " and data: " + area.isData);
        }
        
        if(eventHandler != null) {
            eventHandler.BuildBinLinesPost(step, this);
        }        
    }
        
    /**
     * A main method used to build binary representations of directive assembly source code lines.
     * @param step  An integer representing the current assembly step.
     * @param line  The directive token line to build a binary representation for.
     * @throws ExceptionOpCodeAsArgument
     * @throws ExceptionNoSymbolFound
     * @throws ExceptionUnexpectedTokenWithSubArguments
     * @throws ExceptionNumberInvalidShift
     * @throws ExceptionNumberOutOfRange
     * @throws ExceptionNoNumberRangeFound
     * @throws ExceptionUnexpectedTokenType
     * @throws ExceptionInvalidEntry
     * @throws ExceptionInvalidAssemblyLine
     * @throws ExceptionDirectiveArgNotSupported
     * @throws ExceptionMissingDataDirective 
    * @throws net.middlemind.GenAsm.Exceptions.Thumb.ExceptionNoSymbolValueFound 
     */
    public void BuildBinDirective(int step, TokenLine line) throws ExceptionOpCodeAsArgument, ExceptionNoSymbolFound, ExceptionUnexpectedTokenWithSubArguments, ExceptionNumberInvalidShift, ExceptionNumberOutOfRange, ExceptionNoNumberRangeFound, ExceptionUnexpectedTokenType, ExceptionInvalidEntry, ExceptionInvalidAssemblyLine, ExceptionDirectiveArgNotSupported, ExceptionMissingDataDirective, ExceptionNoSymbolValueFound {   
        if(eventHandler != null) {
            eventHandler.BuildBinDirectivePre(step, this, line);
        }
        
        if(!line.isLineEmpty && line.isLineDirective && !line.isLineOpCode) {
            boolean isDirDchw = false;
            boolean isDirDcb = false;
            boolean isDirFlpDchw = false;
            boolean isDirDcw0 = false;
            boolean isDirDcw1 = false;
            
            for(Token token : line.payload) {
                lastToken = token;                
                if(token.isDirective) {
                    if(token.source.equals(JsonObjIsDirectives.NAME_DCHW)) {
                        isDirDchw = true;                        
                        isDirDcb = false;
                        isDirFlpDchw = false;
                        isDirDcw0 = false;
                        isDirDcw1 = false;
                    } else if(token.source.equals(JsonObjIsDirectives.NAME_DCB)) {
                        isDirDcb = true;
                        isDirDchw = false;
                        isDirFlpDchw = false;
                        isDirDcw0 = false;
                        isDirDcw1 = false;                        
                    } else if(token.source.equals(JsonObjIsDirectives.NAME_FLPDCHW)) {
                        isDirDchw = true;
                        isDirFlpDchw = true;                        
                        isDirDcb = false;
                        isDirDcw0 = false;
                        isDirDcw1 = false;                        
                        
                    } else if(token.source.equals(JsonObjIsDirectives.NAME_DCWBF)) {
                        isDirDchw = true;
                        isDirDcw0 = true;                        
                        isDirFlpDchw = false;                       
                        isDirDcb = false;
                        isDirDcw1 = false;    
                    } else if(token.source.equals(JsonObjIsDirectives.NAME_DCWBS)) {
                        isDirDchw = true;
                        isDirDcw1 = true;                        
                        isDirFlpDchw = false;                       
                        isDirDcb = false;
                        isDirDcw0 = false;
                    }
                    
                } else if(token.isDirectiveArg && (isDirDchw || isDirDcb)) {                    
                    if(token.type_name.equals(JsonObjIsEntryTypes.NAME_NUMBER) == true) {
                        String resTmp;
                        Integer tInt = Utils.ParseNumberString(token.source);
                        resTmp = Integer.toBinaryString(tInt);
                        resTmp = Utils.FormatBinString(resTmp, lineBitSeries.bit_len);                        
                        tInt = Integer.parseInt(resTmp, 2);

                        if(lineNumRange != null) {
                            if(tInt < lineNumRange.min_value || tInt > lineNumRange.max_value) {
                                throw new ExceptionNumberOutOfRange("Integer value " + tInt + " is outside of the specified range " + lineNumRange.min_value + " to " + lineNumRange.max_value + " for source '" + token.source + "' with line number " + token.lineNumAbs);
                            }
                        } else {
                            throw new ExceptionNoNumberRangeFound("Could not find number range for source '" + token.source + "' with line number " + token.lineNumAbs);
                        }

                        if(isDirFlpDchw == true) {
                            resTmp = Utils.EndianFlipBin(resTmp);
                        }
                        
                        token.value = tInt;
                        if(isDirDchw == true) {
                            if(isDirDcw0 == true || isDirDcw1 == true) {
                                byte[] b = Utils.ToBytes(tInt);
                                short[] shorts = new short[2];
                                ByteBuffer.wrap(b).asShortBuffer().get(shorts);                                
                                byte bt;
                                
                                if(isDirDcw0 == true) {
                                    bt = b[b.length - 1];
                                } else {
                                    bt = b[b.length - 2];
                                }
                                
                                if((int)bt < 0) {
                                    tInt = bt & 0xFF;
                                } else {
                                    tInt = (int)bt;
                                }                         
                                
                                token.value = tInt;
                                resTmp = Integer.toBinaryString(tInt);
                                resTmp = Utils.FormatBinString(resTmp, lineBitSeries.bit_len);                                                            
                                line.payloadBinRepStrEndianBig1 = resTmp;
                                line.payloadBinRepStrEndianLil1 = Utils.EndianFlipBin(resTmp);                                                                    

                            } else {
                                line.payloadBinRepStrEndianBig1 = resTmp;
                                line.payloadBinRepStrEndianLil1 = Utils.EndianFlipBin(resTmp);
                            }
                            
                        } else if(isDirDcb == true) {
                            line.payloadBinRepStrEndianBig1 = resTmp;
                            line.payloadBinRepStrEndianLil1 = Utils.EndianFlipBin(resTmp);
                            
                        } else {
                            throw new ExceptionMissingDataDirective("Could not find supported data directive '" + token.source + "' with line number " + token.lineNumAbs);
                        
                        }
                    } else if (token.type_name.equals(JsonObjIsEntryTypes.NAME_LABEL_REF) == true) {
                        String resTmp = "";
                        char c = token.source.charAt(0);
                        String label = token.source.substring(1);                        
                        Symbol sym = symbols.symbols.get(label);
                        JsonObjIsDirectiveArg entry = line.matchesDirective.get(0).args.get(0);
                        if(sym != null) {
                            resTmp = Utils.FormatBinString(Integer.toBinaryString(sym.addressInt), entry.bit_series.bit_len);
                        } else {
                            throw new ExceptionNoSymbolFound("Could not find symbol for label '" + label + "' with line number " + token.lineNumAbs);
                        }
                    
                        Integer tInt = null;
                        if(c == JsonObjIsEntryTypes.NAME_LABEL_REF_START_ADDRESS) {
                            //label address, =
                            tInt = sym.addressInt;
                            Logger.wrl("Symbol lookup: Found start address, '" + tInt + "', for symbol, '" + label + "'.");
                            
                        } else if(c == JsonObjIsEntryTypes.NAME_LABEL_REF_START_VALUE) {
                            //label value, ~
                            if(sym.value != null && sym.isStaticValue) {
                                tInt = sym.value;
                                Logger.wrl("Symbol lookup: Found start value, '" + tInt + "', for symbol, '" + label + "'.");                                
                            } else {
                                throw new ExceptionNoSymbolValueFound("Could not find symbol value for label '" + label + "' with line number " + token.lineNumAbs);
                            }
                            
                        } else if(c == JsonObjIsEntryTypes.NAME_LABEL_REF_START_OFFSET) {
                            //label address offset, -
                            if(line.addressInt < sym.addressInt) {
                                tInt = (sym.addressInt - line.addressInt) + jsonObjIsOpCodes.pc_prefetch_bytes;
                            } else {
                                tInt = (line.addressInt - sym.addressInt) - jsonObjIsOpCodes.pc_prefetch_bytes;
                            }
                            Logger.wrl("Symbol lookup: Found REF start offset, '" + tInt + "', for symbol, '" + label + "'.");
                            
                        } else if(c == JsonObjIsEntryTypes.NAME_LABEL_REF_START_OFFSET_LESS_PREFETCH) {
                            if(line.addressInt < sym.addressInt) {
                                tInt = ((sym.addressInt - line.addressInt) - jsonObjIsOpCodes.pc_prefetch_bytes);
                            } else {
                                tInt =  -1 * ((line.addressInt - sym.addressInt) + jsonObjIsOpCodes.pc_prefetch_bytes);
                            }
                            Logger.wrl("Symbol lookup: Found REF start offset less pre-fetch, '" + tInt + "', for symbol, '" + label + "'.");
                            
                        } else {
                            throw new ExceptionNoSymbolFound("Could not find symbol for label '" + label + "' with line number " + token.lineNumAbs + " and label prefix " + c);
                        }
                       
                        line.source.source += "; [" + tInt + "]";           

                        if(entry.num_range != null && entry.num_range.ones_compliment == true) {
                            tInt = ~tInt;
                        }                        
                        
                        resTmp = Integer.toBinaryString(tInt);
                        Integer tInt2 = tInt;
                        if(entry.bit_shift != null) {
                            if(entry.bit_shift.shift_amount > 0) {
                                if(!Utils.IsStringEmpty(entry.bit_shift.shift_dir) && entry.bit_shift.shift_dir.equals(NUMBER_SHIFT_NAME_LEFT)) {
                                    resTmp = Utils.ShiftBinStr(resTmp, entry.bit_shift.shift_amount, false, true);
                                } else if(!Utils.IsStringEmpty(entry.bit_shift.shift_dir) && entry.bit_shift.shift_dir.equals(NUMBER_SHIFT_NAME_RIGHT)) {
                                    resTmp = Utils.ShiftBinStr(resTmp, entry.bit_shift.shift_amount, true, true);
                                } else {
                                    throw new ExceptionNumberInvalidShift("Invalid number shift found for source '" + token.source + "' with line number " + token.lineNumAbs);
                                }
                                
                                Integer t = Integer.parseInt(resTmp, 2);
                                if(tInt2 % 4 == 2 && entry.bit_shift.shift_amount == 2 && entry.bit_shift.shift_dir.equals(NUMBER_SHIFT_NAME_RIGHT) == true) {
                                    t++;
                                    resTmp = Integer.toBinaryString(t);
                                }                                
                            }
                        }
                                                
                        resTmp = Utils.FormatBinString(resTmp, entry.bit_series.bit_len * 2);
                        tInt = Integer.parseInt(resTmp, 2);
                                                
                        if(entry.num_range != null && isDirDcw0 == false && isDirDcw1 == false) {
                            if(tInt < entry.num_range.min_value || tInt >  entry.num_range.max_value) {
                                throw new ExceptionNumberOutOfRange("Integer value " + tInt + " is outside of the specified range " + entry.num_range.min_value + " to " + entry.num_range.max_value + " for source '" + token.source + "' with line number " + token.lineNumAbs);
                            }
                        }
                                                
                        Logger.wrl("Symbol lookup: Found final symbol value: '" + tInt + "' for symbol, '" + label + "' at line " + line.lineNumAbs);
                        line.source.source += " (" + Utils.Bin2Hex(Integer.toBinaryString(tInt)) + ") {" + tInt + "}";
                                     
                        if(isDirDcb == true && tInt > Byte.MAX_VALUE) {
                            throw new ExceptionMissingDataDirective("Could not find supported data directive, cannot use a LabelRef that is greater than 255 bytes away, '" + token.source + "' with line number " + token.lineNumAbs);                            
                        }
                        
                        token.value = tInt;                        
                        if(isDirFlpDchw == true) {
                            resTmp = Utils.EndianFlipBin(resTmp);
                        }                        
                        
                        if(isDirDchw == true) {
                            if(isDirDcw0 == true || isDirDcw1 == true) {
                                byte[] b = Utils.ToBytes(tInt);
                                short[] shorts = new short[2];
                                ByteBuffer.wrap(b).asShortBuffer().get(shorts);                                
                                
                                if(isDirDcw0 == true) {
                                    tInt = (int)shorts[0];
                                } else {
                                    tInt = (int)shorts[1];
                                }
                                
                                if(entry.num_range != null) {

                                    if(tInt < entry.num_range.min_value || tInt >  entry.num_range.max_value) {
                                        throw new ExceptionNumberOutOfRange("Integer value " + tInt + " is outside of the specified range " + entry.num_range.min_value + " to " + entry.num_range.max_value + " for source '" + token.source + "' with line number " + token.lineNumAbs);
                                    }
                                }
                                
                                token.value = tInt;
                                resTmp = Integer.toBinaryString(tInt);
                                resTmp = Utils.FormatBinString(resTmp, lineBitSeries.bit_len);                                                            
                                line.payloadBinRepStrEndianBig1 = resTmp;
                                line.payloadBinRepStrEndianLil1 = Utils.EndianFlipBin(resTmp);                                                                    

                            } else {
                                resTmp = Utils.FormatBinString(resTmp, entry.bit_series.bit_len);                        
                                line.payloadBinRepStrEndianBig1 = resTmp;
                                line.payloadBinRepStrEndianLil1 = Utils.EndianFlipBin(resTmp);
                            }                            

                        } else if(isDirDcb == true) {
                            resTmp = Utils.FormatBinString(resTmp, entry.bit_series.bit_len);                        
                            line.payloadBinRepStrEndianBig1 = resTmp;
                            line.payloadBinRepStrEndianLil1 = Utils.EndianFlipBin(resTmp);
                            
                        } else {
                            throw new ExceptionMissingDataDirective("Could not find supported data directive '" + token.source + "' with line number " + token.lineNumAbs);
                        
                        }                        
                    } else {
                        throw new ExceptionDirectiveArgNotSupported("Could not find supported data directive '" + token.source + "' with line number " + token.lineNumAbs);                            
                    }
                }
            }
        }
        
        if(eventHandler != null) {
            eventHandler.BuildBinDirectivePost(step, this);
        }        
    }
    
   /**
    *
    * @param step
    * @param line
    * @throws ExceptionOpCodeAsArgument
    * @throws
    * net.middlemind.GenAsm.Exceptions.Thumb.ExceptionUnexpectedTokenWithSubArguments
    */
   public void BuildBinOpCodePrep(int step, TokenLine line) throws ExceptionOpCodeAsArgument, ExceptionUnexpectedTokenWithSubArguments {
      if (!line.isLineEmpty && !line.isLineDirective && line.isLineOpCode) {
         JsonObjIsOpCode opCode = line.matchesOpCode.get(0);
         List<JsonObjIsOpCodeArg> opCodeArgs = opCode.args;
         List<BuildOpCodeThumb> buildEntries = new ArrayList<>();
         List<JsonObjIsOpCodeArg> opCodeArgsSub = null;
         Collections.sort(opCodeArgs, new JsonObjIsOpCodeArgSorter(JsonObjIsOpCodeArgSorter.JsonObjIsOpCodeArgSorterType.ARG_INDEX_ASC));
         Collections.sort(line.payload, new TokenSorter(TokenSorterType.INDEX_ASC));
         BuildOpCodeThumb tmpB = null;
         int opCodeArgIdx = 0;
         int lOpCodeArgIdx = 0;

         //Prepare BuilOpCodeEntry list based on tokens and json arguments
         for (Token token : line.payload) {
            lastToken = token;
            if (token.isOpCodeArg) {
               tmpB = new BuildOpCodeThumb();
               tmpB.isOpCodeArg = true;
               tmpB.opCodeArg = (JsonObjIsOpCodeArg) opCodeArgs.get(opCodeArgIdx);
               tmpB.bitSeries = tmpB.opCodeArg.bit_series;
               tmpB.tokenOpCodeArg = token;
               if (tmpB.opCodeArg.bit_index != -1) {
                  buildEntries.add(tmpB);
               }
               opCodeArgIdx++;

               if (!Utils.IsListEmpty(token.payload) && !Utils.IsListEmpty(tmpB.opCodeArg.sub_args)) {
                  if (token.type_name.equals(JsonObjIsEntryTypes.NAME_START_LIST)) {
                     // <editor-fold defaultstate="collapsed" desc="List">
                     lOpCodeArgIdx = 0;
                     JsonObjIsOpCodeArg opCodeArgList = tmpB.opCodeArg;
                     opCodeArgsSub = tmpB.opCodeArg.sub_args;
                     Collections.sort(opCodeArgsSub, new JsonObjIsOpCodeArgSorter(JsonObjIsOpCodeArgSorter.JsonObjIsOpCodeArgSorterType.ARG_INDEX_ASC));
                     Collections.sort(token.payload, new TokenSorter(TokenSorterType.INDEX_ASC));
                     boolean regRangeLow = false;
                     boolean regRangeHi = false;

                     if (opCodeArgsSub.get(0).is_entry_types.contains(JsonObjIsEntryTypes.NAME_REGISTER_RANGE_LOW)) {
                        regRangeLow = true;
                     } else if (opCodeArgsSub.get(0).is_entry_types.contains(JsonObjIsEntryTypes.NAME_REGISTER_RANGE_HI)) {
                        regRangeHi = true;
                     }

                     for (Token ltoken : token.payload) {
                        if (ltoken.isOpCodeArg) {
                           if (regRangeLow && !ltoken.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_LOW)) {
                              lOpCodeArgIdx++;
                           } else if (regRangeHi && !ltoken.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_HI)) {
                              lOpCodeArgIdx++;
                           }

                           tmpB = new BuildOpCodeThumb();
                           tmpB.isOpCodeArgList = true;

                           if (ltoken.type_name.equals(JsonObjIsEntryTypes.NAME_STOP_LIST)) {
                              tmpB.opCodeArgList = opCodeArgList;
                           } else {
                              tmpB.opCodeArgList = (JsonObjIsOpCodeArg) opCodeArgsSub.get(lOpCodeArgIdx);
                           }

                           tmpB.bitSeries = tmpB.opCodeArgList.bit_series;
                           tmpB.tokenOpCodeArgList = ltoken;

                           if (tmpB.opCodeArgList.bit_index != -1) {
                              buildEntries.add(tmpB);
                           }

                        } else if (ltoken.isOpCode) {
                           throw new ExceptionOpCodeAsArgument("Found OpCode token entry where a sub-argument should be on line " + line.lineNumAbs + " with argument index " + ltoken.index + " and parent argument index " + token.index);
                        }
                     }
                     // </editor-fold>                            
                  } else if (token.type_name.equals(JsonObjIsEntryTypes.NAME_START_GROUP)) {
                     // <editor-fold defaultstate="collapsed" desc="Group">
                     lOpCodeArgIdx = 0;
                     JsonObjIsOpCodeArg opCodeArgGroup = tmpB.opCodeArg;
                     opCodeArgsSub = tmpB.opCodeArg.sub_args;
                     Collections.sort(opCodeArgsSub, new JsonObjIsOpCodeArgSorter(JsonObjIsOpCodeArgSorter.JsonObjIsOpCodeArgSorterType.ARG_INDEX_ASC));
                     Collections.sort(token.payload, new TokenSorter(TokenSorterType.INDEX_ASC));

                     for (Token ltoken : token.payload) {
                        if (ltoken.isOpCodeArg) {
                           tmpB = new BuildOpCodeThumb();
                           tmpB.isOpCodeArgGroup = true;

                           if (ltoken.type_name.equals(JsonObjIsEntryTypes.NAME_STOP_GROUP)) {
                              tmpB.opCodeArgGroup = opCodeArgGroup;
                           } else {
                              tmpB.opCodeArgGroup = (JsonObjIsOpCodeArg) opCodeArgsSub.get(lOpCodeArgIdx);
                           }

                           tmpB.bitSeries = tmpB.opCodeArgGroup.bit_series;
                           tmpB.tokenOpCodeArgGroup = ltoken;

                           if (tmpB.opCodeArgGroup.bit_index != -1) {
                              buildEntries.add(tmpB);
                           }
                           lOpCodeArgIdx++;
                        } else if (ltoken.isOpCode) {
                           throw new ExceptionOpCodeAsArgument("Found OpCode token entry where a sub-argument should be on line " + line.lineNumAbs + " with argument index " + ltoken.index + " and parent argument index " + token.index);
                        }
                     }
                     // </editor-fold>
                  } else {
                     throw new ExceptionUnexpectedTokenWithSubArguments("Found unexpected token with sub-arguments on line " + line.lineNumAbs + " with argument index " + token.index);
                  }
               }

            } else if (token.isOpCode) {
               tmpB = new BuildOpCodeThumb();
               tmpB.isOpCode = true;
               tmpB.bitSeries = opCode.bit_series;
               tmpB.opCode = opCode;
               tmpB.tokenOpCode = token;
               buildEntries.add(tmpB);
            }
         }

         line.buildEntries = buildEntries;
      }
   }    
    
    /**
     * A main method used to build binary representations of op-code assembly source code lines.
     * @param step  An integer representing the current assembly step.
     * @param line  The directive token line to build a binary representation for.
     * @throws ExceptionOpCodeAsArgument
     * @throws ExceptionNoSymbolFound
     * @throws ExceptionUnexpectedTokenWithSubArguments
     * @throws ExceptionNumberInvalidShift
     * @throws ExceptionNumberOutOfRange
     * @throws ExceptionNoNumberRangeFound
     * @throws ExceptionUnexpectedTokenType
     * @throws ExceptionInvalidEntry
     * @throws ExceptionInvalidAssemblyLine
     * @throws ExceptionNoSymbolValueFound 
     */
    public void BuildBinOpCode(int step, TokenLine line) throws ExceptionOpCodeAsArgument, ExceptionNoSymbolFound, ExceptionUnexpectedTokenWithSubArguments, ExceptionNumberInvalidShift, ExceptionNumberOutOfRange, ExceptionNoNumberRangeFound, ExceptionUnexpectedTokenType, ExceptionInvalidEntry, ExceptionInvalidAssemblyLine, ExceptionNoSymbolValueFound {
        if(eventHandler != null) {
            eventHandler.BuildBinOpCodePre(step, this, line);
        }        
        
        if(!line.isLineEmpty && !line.isLineDirective && line.isLineOpCode) {
            //Process BuildOpCodeEntry list creating a binary string representation for each entry                   
            BuildBinOpCodePrep(step, line);
            List<BuildOpCodeThumb> buildEntries = line.buildEntries;            
            String res1 = "";
            String resTmp1 = "";
            String res2 = "";
            String resTmp2 = "";            
            boolean inList = false;
            boolean inGroup = false;
            boolean inBxHi = false;
            boolean inBxLo = false;
            boolean inBl = false;
            int[] inListRegisters = null;
            BuildOpCodeThumb inListEntry = null;
            BuildOpCodeThumb inGroupEntry = null;            
            BuildOpCodeThumb opCodeEntry = null;
            
            for(BuildOpCodeThumb entry : buildEntries) {
                resTmp1 = "";
                resTmp2 = null;
                if(entry.isOpCode) {
                    // <editor-fold defaultstate="collapsed" desc="OpCode">
                    opCodeEntry = entry;
                    resTmp1 = entry.opCode.bit_rep.bit_string;
                    
                    if(entry.opCode.index == JsonObjIsOpCodes.BX_HI_INDEX) {
                        inBxHi = true;
                    } else if(entry.opCode.index == JsonObjIsOpCodes.BX_LO_INDEX) {
                        inBxLo = true;
                    } else if(entry.opCode.index == JsonObjIsOpCodes.BL_INDEX) {
                        inBl = true;
                    }
                    // </editor-fold>
                }else if(entry.isOpCodeArgList) {
                    // <editor-fold defaultstate="collapsed" desc="OpCode Arg List">
                    if(entry.tokenOpCodeArgList.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_LOW)) {
                        if(entry.tokenOpCodeArgList.source.equals(JsonObjIsRegisters.R_0)) {
                            inListRegisters[0] = 1;
                            
                        } else if(entry.tokenOpCodeArgList.source.equals(JsonObjIsRegisters.R_1)) {
                            inListRegisters[1] = 1;
                        
                        } else if(entry.tokenOpCodeArgList.source.equals(JsonObjIsRegisters.R_2)) {
                            inListRegisters[2] = 1;
                        
                        } else if(entry.tokenOpCodeArgList.source.equals(JsonObjIsRegisters.R_3)) {
                            inListRegisters[3] = 1;
                        
                        } else if(entry.tokenOpCodeArgList.source.equals(JsonObjIsRegisters.R_4)) {
                            inListRegisters[4] = 1;
                        
                        } else if(entry.tokenOpCodeArgList.source.equals(JsonObjIsRegisters.R_5)) {
                            inListRegisters[5] = 1;
                        
                        } else if(entry.tokenOpCodeArgList.source.equals(JsonObjIsRegisters.R_6)) {
                            inListRegisters[6] = 1;
                        
                        } else if(entry.tokenOpCodeArgList.source.equals(JsonObjIsRegisters.R_7)) {
                            inListRegisters[7] = 1;
                        
                        } else {
                            throw new ExceptionInvalidEntry("Found invalid LOW register entry '" + entry.tokenOpCodeArgList.source + "' for line source '" + line.source.source + " and line number " + line.lineNumAbs);
                        }
                        
                    } else if(entry.tokenOpCodeArgList.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_HI)) {
                        if(entry.tokenOpCodeArgList.source.equals(JsonObjIsRegisters.R_8)) {
                            inListRegisters[0] = 1;
                        
                        } else if(entry.tokenOpCodeArgList.source.equals(JsonObjIsRegisters.R_9)) {
                            inListRegisters[1] = 1;
                        
                        } else if(entry.tokenOpCodeArgList.source.equals(JsonObjIsRegisters.R_10)) {
                            inListRegisters[2] = 1;
                        
                        } else if(entry.tokenOpCodeArgList.source.equals(JsonObjIsRegisters.R_11)) {
                            inListRegisters[3] = 1;
                        
                        } else if(entry.tokenOpCodeArgList.source.equals(JsonObjIsRegisters.R_12)) {
                            inListRegisters[4] = 1;
                        
                        } else if(entry.tokenOpCodeArgList.source.equals(JsonObjIsRegisters.R_13) || entry.tokenOpCodeArgList.source.equals(JsonObjIsRegisters.R_SP)) {
                            inListRegisters[5] = 1;
                        
                        } else if(entry.tokenOpCodeArgList.source.equals(JsonObjIsRegisters.R_14) || entry.tokenOpCodeArgList.source.equals(JsonObjIsRegisters.R_LR)) {
                            inListRegisters[6] = 1;
                        
                        } else if(entry.tokenOpCodeArgList.source.equals(JsonObjIsRegisters.R_15) || entry.tokenOpCodeArgList.source.equals(JsonObjIsRegisters.R_PC)) {
                            inListRegisters[7] = 1;
                        } else {
                            throw new ExceptionInvalidEntry("Found invalid HI register entry '" + entry.tokenOpCodeArgList.source + "' for line source '" + line.source.source + " and line number " + line.lineNumAbs);
                        }
                        
                    } else if(entry.tokenOpCodeArgList.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_PC)) {
                        if(entry.tokenOpCodeArgList.source.equals(JsonObjIsRegisters.R_15) || entry.tokenOpCodeArgList.source.equals(JsonObjIsRegisters.R_LR)) {
                            inListRegisters[7] = 1;
                        } else {
                            throw new ExceptionInvalidEntry("Found invalid PC register entry '" + entry.tokenOpCodeArgList.source + "' for line source '" + line.source.source + " and line number " + line.lineNumAbs); 
                        }
                        
                    } else if(entry.tokenOpCodeArgList.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_SP)) {
                        if(entry.tokenOpCodeArgList.source.equals(JsonObjIsRegisters.R_13) || entry.tokenOpCodeArgList.source.equals(JsonObjIsRegisters.R_SP)) {
                            inListRegisters[5] = 1;
                        } else {
                            throw new ExceptionInvalidEntry("Found invalid SP register entry '" + entry.tokenOpCodeArgList.source + "' for line source '" + line.source.source + " and line number " + line.lineNumAbs); 
                        }
                        
                    } else if(entry.tokenOpCodeArgList.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_LR)) {
                        if(entry.tokenOpCodeArgList.source.equals(JsonObjIsRegisters.R_14) || entry.tokenOpCodeArgList.source.equals(JsonObjIsRegisters.R_LR)) {
                            inListRegisters[6] = 1;
                        } else {
                            throw new ExceptionInvalidEntry("Found invalid LR register entry '" + entry.tokenOpCodeArgList.source + "' for line source '" + line.source.source + " and line number " + line.lineNumAbs); 
                        }
                        
                    } else if(entry.tokenOpCodeArgList.type_name.equals(JsonObjIsEntryTypes.NAME_LABEL_REF)) {
                        throw new ExceptionInvalidEntry("Found invalid LABEL entry '" + entry.tokenOpCodeArgList.source + "' for line source '" + line.source.source + " and line number " + line.lineNumAbs);
                                        
                    } else if(entry.tokenOpCodeArgList.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTERWB)) {
                        throw new ExceptionInvalidEntry("Found invalid REGISTERWB entry '" + entry.tokenOpCodeArgList.source + "' for line source '" + line.source.source + " and line number " + line.lineNumAbs); 
                    
                    } else if(entry.tokenOpCodeArgList.type_name.equals(JsonObjIsEntryTypes.NAME_NUMBER)) {
                        throw new ExceptionInvalidEntry("Found invalid NUMBER entry '" + entry.tokenOpCodeArgList.source + "' for line source '" + line.source.source + " and line number " + line.lineNumAbs); 
                    
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
                        inListEntry.binRepStr1 = ts;
                        inListRegisters = null;
                        
                    } else if(entry.tokenOpCodeArgList.type_name.equals(JsonObjIsEntryTypes.NAME_STOP_GROUP)) {
                        throw new ExceptionInvalidEntry("Found invalid STOP GROUP entry '" + entry.tokenOpCodeArgList.source + "' for line source '" + line.source.source + " and line number " + line.lineNumAbs); 
                    
                    } else {
                        throw new ExceptionUnexpectedTokenType("Found unexpected LIST sub-token type '" + entry.tokenOpCodeArgList.type_name + "' for line source '" + line.source.source + " and line number " + line.lineNumAbs);
                    }
                    //</editor-fold>
                    
                }else if(entry.isOpCodeArgGroup) {
                    // <editor-fold defaultstate="collapsed" desc="OpCode Arg Group">
                    if(entry.tokenOpCodeArgGroup.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_LOW)) {
                        resTmp1 = entry.tokenOpCodeArgGroup.register.bit_rep.bit_string;
                    
                    } else if(entry.tokenOpCodeArgGroup.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_HI)) {
                        resTmp1 = entry.tokenOpCodeArgGroup.register.bit_rep.bit_string;
                    
                    } else if(entry.tokenOpCodeArgGroup.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_PC)) {
                        resTmp1 = entry.tokenOpCodeArgGroup.register.bit_rep.bit_string;
                    
                    } else if(entry.tokenOpCodeArgGroup.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_SP)) {
                        resTmp1 = entry.tokenOpCodeArgGroup.register.bit_rep.bit_string;
                    
                    } else if(entry.tokenOpCodeArgGroup.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_LR)) {
                        resTmp1 = entry.tokenOpCodeArgGroup.register.bit_rep.bit_string;
                    
                    } else if(entry.tokenOpCodeArgGroup.type_name.equals(JsonObjIsEntryTypes.NAME_LABEL_REF)) {
                        char c = entry.tokenOpCodeArgGroup.source.charAt(0);
                        String label = entry.tokenOpCodeArgGroup.source.substring(1);                        
                        Symbol sym = symbols.symbols.get(label);
                        
                        if(sym != null) {
                            resTmp1 = Utils.FormatBinString(Integer.toBinaryString(sym.addressInt), entry.opCodeArgGroup.bit_series.bit_len);
                        } else {
                            throw new ExceptionNoSymbolFound("Could not find symbol for label '" + label + "' with line number " + entry.tokenOpCodeArgGroup.lineNumAbs);
                        }
                    
                        Integer tInt = null;
                        if(c == JsonObjIsEntryTypes.NAME_LABEL_REF_START_ADDRESS) {
                            //label address =
                            tInt = sym.addressInt;
                            Logger.wrl("Symbol lookup: Found start address, '" + tInt + "', for symbol, '" + label + "'.");
                            
                        } else if(c == JsonObjIsEntryTypes.NAME_LABEL_REF_START_VALUE) {
                            //label value ~
                            if(sym.value != null && sym.isStaticValue) {
                                tInt = sym.value;
                                Logger.wrl("Symbol lookup: Found start value, '" + tInt + "', for symbol, '" + label + "'.");
                            } else {
                                throw new ExceptionNoSymbolValueFound("Could not find symbol value for label '" + label + "' with line number " + entry.tokenOpCodeArgGroup.lineNumAbs);
                            }
                            
                        } else if(c == JsonObjIsEntryTypes.NAME_LABEL_REF_START_OFFSET) {
                            //label address offset -
                            if(line.addressInt < sym.addressInt) {
                                tInt = (sym.addressInt - line.addressInt) + jsonObjIsOpCodes.pc_prefetch_bytes;
                            } else {
                                tInt = (line.addressInt - sym.addressInt) - jsonObjIsOpCodes.pc_prefetch_bytes;
                            }                            
                            Logger.wrl("Symbol lookup: Found REF start offset, '" + tInt + "', for symbol, '" + label + "'.");
                            
                        } else if(c == JsonObjIsEntryTypes.NAME_LABEL_REF_START_OFFSET_LESS_PREFETCH) {
                            //label address offset minus prefetch ~
                            if(line.addressInt < sym.addressInt) {
                                tInt = ((sym.addressInt - line.addressInt) - jsonObjIsOpCodes.pc_prefetch_bytes);
                            } else {
                                tInt =  -1 * ((line.addressInt - sym.addressInt) + jsonObjIsOpCodes.pc_prefetch_bytes);
                            }
                            Logger.wrl("Symbol lookup: Found REF start offset less pre-fetch, '" + tInt + "', for symbol, '" + label + "'.");
                            
                        } else {
                            throw new ExceptionNoSymbolFound("Could not find symbol for label '" + label + "' with line number " + entry.tokenOpCodeArgGroup.lineNumAbs + " and label prefix " + c);
                        }
                        
                        line.source.source += "; [" + tInt + "]";
                        
                        //special rule for ADD OpCode
                        if(opCodeEntry.binRepStr1.equals(SPECIAL_ADD_OP_CODE_CHECK) && tInt < 0) {
                            opCodeEntry.binRepStr1 = JsonObjIsOpCodes.ADD_OP_CODE_SPECIAL; //"101100001";
                            tInt *= -1;
                        }
                        
                        if(entry.opCodeArgGroup.num_range != null && entry.opCodeArgGroup.num_range.ones_compliment == true) {
                            tInt = ~tInt;
                        }                        
                        
                        resTmp1 = Integer.toBinaryString(tInt);
                        Integer tInt2 = tInt;
                        if(entry.opCodeArgGroup.bit_shift != null) {
                            if(entry.opCodeArgGroup.bit_shift.shift_amount > 0) {
                                if(!Utils.IsStringEmpty(entry.opCodeArgGroup.bit_shift.shift_dir) && entry.opCodeArgGroup.bit_shift.shift_dir.equals(NUMBER_SHIFT_NAME_LEFT)) {
                                    resTmp1 = Utils.ShiftBinStr(resTmp1, entry.opCodeArgGroup.bit_shift.shift_amount, false, true);
                                } else if(!Utils.IsStringEmpty(entry.opCodeArgGroup.bit_shift.shift_dir) && entry.opCodeArgGroup.bit_shift.shift_dir.equals(NUMBER_SHIFT_NAME_RIGHT)) {
                                    resTmp1 = Utils.ShiftBinStr(resTmp1, entry.opCodeArgGroup.bit_shift.shift_amount, true, true);
                                } else {
                                    throw new ExceptionNumberInvalidShift("Invalid number shift found for source '" + entry.tokenOpCodeArgGroup.source + "' with line number " + entry.tokenOpCodeArgGroup.lineNumAbs);
                                }
                                
                                Integer t = Integer.parseInt(resTmp1, 2);
                                if(tInt2 % 4 == 2 && entry.opCodeArgGroup.bit_shift.shift_amount == 2 && entry.opCodeArgGroup.bit_shift.shift_dir.equals(NUMBER_SHIFT_NAME_RIGHT) == true) {
                                    t++;
                                    resTmp1 = Integer.toBinaryString(t);
                                }
                            }
                        }
                        
                        resTmp1 = Utils.FormatBinString(resTmp1, entry.opCodeArgGroup.bit_series.bit_len);
                        tInt = Integer.parseInt(resTmp1, 2);
                        
                        if(entry.opCodeArgGroup.num_range != null) {
                            if(tInt < entry.opCodeArgGroup.num_range.min_value || tInt >  entry.opCodeArgGroup.num_range.max_value) {
                                throw new ExceptionNumberOutOfRange("Integer value " + tInt + " is outside of the specified range " + entry.opCodeArgGroup.num_range.min_value + " to " + entry.opCodeArgGroup.num_range.max_value + " for source '" + entry.tokenOpCodeArgGroup.source + "' with line number " + entry.tokenOpCodeArgGroup.lineNumAbs);
                            }
                        }
                        
                        Logger.wrl("Symbol lookup: Found final symbol value: '" + tInt + "' for symbol, '" + label + "' at line " + line.lineNumAbs);
                        line.source.source += " (" + Utils.Bin2Hex(Integer.toBinaryString(tInt)) + ") {" + tInt + "}";
                        entry.tokenOpCodeArgGroup.value = tInt;
                        
                    } else if(entry.tokenOpCodeArgGroup.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTERWB)) {
                        resTmp1 = entry.tokenOpCodeArgGroup.register.bit_rep.bit_string;
                    
                    } else if(entry.tokenOpCodeArgGroup.type_name.equals(JsonObjIsEntryTypes.NAME_NUMBER)) {
                        Integer tInt = Utils.ParseNumberString(entry.tokenOpCodeArgGroup.source);
                        
                        //special rule for ADD OpCode
                        if(opCodeEntry.binRepStr1.equals(SPECIAL_ADD_OP_CODE_CHECK) && tInt < 0) {
                            opCodeEntry.binRepStr1 = JsonObjIsOpCodes.ADD_OP_CODE_SPECIAL; //"101100001";
                            tInt *= -1;
                        }
                        
                        if(entry.opCodeArgGroup.num_range != null && entry.opCodeArgGroup.num_range.ones_compliment == true) {
                            tInt = ~tInt;
                        }                        
                        
                        resTmp1 = Integer.toBinaryString(tInt);
                        Integer tInt2 = tInt;
                        if(entry.opCodeArgGroup.bit_shift != null) {
                            if(entry.opCodeArgGroup.bit_shift.shift_amount > 0) {
                                if(!Utils.IsStringEmpty(entry.opCodeArgGroup.bit_shift.shift_dir) && entry.opCodeArgGroup.bit_shift.shift_dir.equals(NUMBER_SHIFT_NAME_LEFT)) {
                                    resTmp1 = Utils.ShiftBinStr(resTmp1, entry.opCodeArgGroup.bit_shift.shift_amount, false, true);
                                } else if(!Utils.IsStringEmpty(entry.opCodeArgGroup.bit_shift.shift_dir) && entry.opCodeArgGroup.bit_shift.shift_dir.equals(NUMBER_SHIFT_NAME_RIGHT)) {
                                    resTmp1 = Utils.ShiftBinStr(resTmp1, entry.opCodeArgGroup.bit_shift.shift_amount, true, true);
                                } else {
                                    throw new ExceptionNumberInvalidShift("Invalid number shift found for source '" + entry.tokenOpCodeArgGroup.source + "' with line number " + entry.tokenOpCodeArgGroup.lineNumAbs);
                                }
                                
                                Integer t = Integer.parseInt(resTmp1, 2);
                                if(tInt2 % 4 == 2 && entry.opCodeArgGroup.bit_shift.shift_amount == 2 && entry.opCodeArgGroup.bit_shift.shift_dir.equals(NUMBER_SHIFT_NAME_RIGHT) == true) {
                                    t++;
                                    resTmp1 = Integer.toBinaryString(t);
                                }
                            }
                        }
                        
                        resTmp1 = Utils.FormatBinString(resTmp1, entry.opCodeArgGroup.bit_series.bit_len);
                        tInt = Integer.parseInt(resTmp1, 2);
                                                
                        if(entry.opCodeArgGroup.num_range != null) {
                            if(tInt < entry.opCodeArgGroup.num_range.min_value || tInt >  entry.opCodeArgGroup.num_range.max_value) {
                                throw new ExceptionNumberOutOfRange("Integer value " + tInt + " is outside of the specified range " + entry.opCodeArgGroup.num_range.min_value + " to " + entry.opCodeArgGroup.num_range.max_value + " for source '" + entry.tokenOpCodeArgGroup.source + "' with line number " + entry.tokenOpCodeArgGroup.lineNumAbs);
                            }
                        }                        
                        entry.tokenOpCodeArgGroup.value = tInt;
                        
                    } else if(entry.tokenOpCodeArgGroup.type_name.equals(JsonObjIsEntryTypes.NAME_STOP_LIST)) {
                        throw new ExceptionInvalidEntry("Found invalid STOP LIST entry '" + entry.tokenOpCodeArgList.source + "' for line source '" + line.source.source + " and line number " + line.lineNumAbs);                         
                    
                    } else if(entry.tokenOpCodeArgGroup.type_name.equals(JsonObjIsEntryTypes.NAME_STOP_GROUP)) {
                        inGroup = false;
                        inGroupEntry = null;
                    
                    } else {
                        throw new ExceptionUnexpectedTokenType("Found unexpected GROUP sub-token type '" + entry.tokenOpCodeArgGroup.type_name + "' for line source '" + line.source.source + " and line number " + line.lineNumAbs);
                    }
                    //</editor-fold>
                                    
                }else if(entry.isOpCodeArg) {
                    // <editor-fold defaultstate="collapsed" desc="OpCode Arg">
                    if(entry.tokenOpCodeArg.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_LOW)) {                        
                        resTmp1 = entry.tokenOpCodeArg.register.bit_rep.bit_string;
                        if(inBxLo) {
                            resTmp1 += "000"; //empty register
                            inBxLo = false;
                        }
                    
                    } else if(entry.tokenOpCodeArg.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_HI)) {
                        resTmp1 = entry.tokenOpCodeArg.register.bit_rep.bit_string;
                        if(inBxHi) {
                            resTmp1 += "000"; //empty register
                            inBxHi = false;
                        }                        
                    
                    } else if(entry.tokenOpCodeArg.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_PC)) {
                        resTmp1 = entry.tokenOpCodeArg.register.bit_rep.bit_string;
                        if(inBxHi) {
                            resTmp1 += "000"; //empty register
                            inBxHi = false;
                        }                    
                        
                    } else if(entry.tokenOpCodeArg.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_SP)) {
                        resTmp1 = entry.tokenOpCodeArg.register.bit_rep.bit_string;
                        if(inBxHi) {
                            resTmp1 += "000"; //empty register
                            inBxHi = false;
                        }                    
                        
                    } else if(entry.tokenOpCodeArg.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTER_LR)) {
                        resTmp1 = entry.tokenOpCodeArg.register.bit_rep.bit_string;
                        if(inBxHi) {
                            resTmp1 += "000"; //empty register
                            inBxHi = false;
                        }                    
                        
                    } else if(entry.tokenOpCodeArg.type_name.equals(JsonObjIsEntryTypes.NAME_LABEL_REF)) {
                        char c = entry.tokenOpCodeArg.source.charAt(0);
                        String label = entry.tokenOpCodeArg.source.substring(1);                        
                        Symbol sym = symbols.symbols.get(label);
                        
                        if(sym != null) {
                            resTmp1 = Utils.FormatBinString(Integer.toBinaryString(sym.addressInt), entry.opCodeArg.bit_series.bit_len);
                        } else {
                            throw new ExceptionNoSymbolFound("Could not find symbol for label '" + label + "' with line number " + entry.tokenOpCodeArg.lineNumAbs);
                        }
                    
                        Integer tInt = null;
                        if(c == JsonObjIsEntryTypes.NAME_LABEL_REF_START_ADDRESS) {
                            //label address =
                            tInt = sym.addressInt;
                            Logger.wrl("Symbol lookup: Found start address, '" + tInt + "', for symbol, '" + label + "'.");
                            
                        } else if(c == JsonObjIsEntryTypes.NAME_LABEL_REF_START_VALUE) {
                            //label value ~
                            if(sym.value != null && sym.isStaticValue) {
                                tInt = sym.value;
                                Logger.wrl("Symbol lookup: Found start value, '" + tInt + "', for symbol, '" + label + "'.");                                
                            } else {
                                throw new ExceptionNoSymbolValueFound("Could not find symbol value for label '" + label + "' with line number " + entry.tokenOpCodeArg.lineNumAbs);
                            }
                            
                        } else if(c == JsonObjIsEntryTypes.NAME_LABEL_REF_START_OFFSET) {
                            //label address offset -
                            if(line.addressInt < sym.addressInt) {
                                tInt = (sym.addressInt - line.addressInt) + jsonObjIsOpCodes.pc_prefetch_bytes;
                            } else {
                                tInt = (line.addressInt - sym.addressInt) - jsonObjIsOpCodes.pc_prefetch_bytes;
                            }
                            Logger.wrl("Symbol lookup: Found REF start offset, '" + tInt + "', for symbol, '" + label + "'.");
                            
                        } else if(c == JsonObjIsEntryTypes.NAME_LABEL_REF_START_OFFSET_LESS_PREFETCH) {
                            //label address offset minus prefetch `
                            if(line.addressInt < sym.addressInt) {
                                tInt = ((sym.addressInt - line.addressInt) - jsonObjIsOpCodes.pc_prefetch_bytes);
                            } else {
                                tInt =  -1 * ((line.addressInt - sym.addressInt) + jsonObjIsOpCodes.pc_prefetch_bytes);
                            }
                            Logger.wrl("Symbol lookup: Found REF start offset less pre-fetch, '" + tInt + "', for symbol, '" + label + "'.");
                            
                        } else {
                            throw new ExceptionNoSymbolFound("Could not find symbol for label '" + label + "' with line number " + entry.tokenOpCodeArg.lineNumAbs + " and label prefix " + c);
                        }
                       
                        line.source.source += "; [" + tInt + "]";           
                        
                        //special rule for ADD OpCode
                        if(opCodeEntry.binRepStr1.equals(SPECIAL_ADD_OP_CODE_CHECK) && tInt < 0) {
                            opCodeEntry.binRepStr1 = JsonObjIsOpCodes.ADD_OP_CODE_SPECIAL; //"101100001";
                            tInt *= -1;
                        }
                        
                        if(entry.opCodeArg.num_range != null && entry.opCodeArg.num_range.ones_compliment == true) {
                            tInt = ~tInt;
                        }                        
                        
                        resTmp1 = Integer.toBinaryString(tInt);                        
                        //Specific for the OpCode BL and its 2 line, 4 byte encoding
                        Integer bltInt = tInt;
                        Integer tInt2 = tInt;
                        String blResTmp1 = resTmp1;                        
                        if(entry.opCodeArg.bit_shift != null) {
                            if(entry.opCodeArg.bit_shift.shift_amount > 0) {
                                if(!Utils.IsStringEmpty(entry.opCodeArg.bit_shift.shift_dir) && entry.opCodeArg.bit_shift.shift_dir.equals(NUMBER_SHIFT_NAME_LEFT)) {
                                    resTmp1 = Utils.ShiftBinStr(resTmp1, entry.opCodeArg.bit_shift.shift_amount, false, true);
                                } else if(!Utils.IsStringEmpty(entry.opCodeArg.bit_shift.shift_dir) && entry.opCodeArg.bit_shift.shift_dir.equals(NUMBER_SHIFT_NAME_RIGHT)) {
                                    resTmp1 = Utils.ShiftBinStr(resTmp1, entry.opCodeArg.bit_shift.shift_amount, true, true);
                                } else {
                                    throw new ExceptionNumberInvalidShift("Invalid number shift found for source '" + entry.tokenOpCodeArg.source + "' with line number " + entry.tokenOpCodeArg.lineNumAbs);
                                }
                                
                                Integer t = Integer.parseInt(resTmp1, 2);
                                if(tInt2 % 4 == 2 && entry.opCodeArg.bit_shift.shift_amount == 2 && entry.opCodeArg.bit_shift.shift_dir.equals(NUMBER_SHIFT_NAME_RIGHT) == true) {
                                    t++;
                                    resTmp1 = Integer.toBinaryString(t);
                                }                                
                            }
                        }
                        
                        resTmp1 = Utils.FormatBinString(resTmp1, entry.opCodeArg.bit_series.bit_len);
                        tInt = Integer.parseInt(resTmp1, 2);
                        
                        if(entry.opCodeArg.num_range != null) {
                            if(tInt < entry.opCodeArg.num_range.min_value || tInt >  entry.opCodeArg.num_range.max_value) {
                                throw new ExceptionNumberOutOfRange("Integer value " + tInt + " is outside of the specified range " + entry.opCodeArg.num_range.min_value + " to " + entry.opCodeArg.num_range.max_value + " for source '" + entry.tokenOpCodeArg.source + "' with line number " + entry.tokenOpCodeArg.lineNumAbs);
                            }
                        }
                        
                        if(inBl == true) {                            
                            //in BL OpCode which generates 4 bytes of instructions instead of two
                            if(bltInt < 0) {
                                resTmp1 = Utils.FormatBinString(blResTmp1, 23, true, "1");
                            } else {
                                resTmp1 = Utils.FormatBinString(blResTmp1, 23, true, "0");                                
                            }
                            String nResTmp = resTmp1.substring(0, resTmp1.length() - 1);
                            String halfHi = nResTmp.substring(0, 11);
                            String halfLo = nResTmp.substring(11);
                            resTmp1 = halfHi;
                            resTmp2 = JsonObjIsOpCodes.BL_OP_CODE_BIN_ENTRY_2 + halfLo;
                            tInt = Integer.parseInt(resTmp1, 2);
                            inBl = false;
                            line.byteLength = 4;
                        }
                                                
                        Logger.wrl("Symbol lookup: Found final symbol value: '" + tInt + "' for symbol, '" + label + "' at line " + line.lineNumAbs);
                        line.source.source += " (" + Utils.Bin2Hex(Integer.toBinaryString(tInt)) + ") {" + tInt + "}";
                        entry.tokenOpCodeArg.value = tInt;
                    
                    } else if(entry.tokenOpCodeArg.type_name.equals(JsonObjIsEntryTypes.NAME_REGISTERWB)) {
                        resTmp1 = entry.tokenOpCodeArg.register.bit_rep.bit_string;
                        
                    } else if(entry.tokenOpCodeArg.type_name.equals(JsonObjIsEntryTypes.NAME_NUMBER)) {
                        Integer tInt = Utils.ParseNumberString(entry.tokenOpCodeArg.source);
                        
                        if(entry.opCodeArg.num_range.handle_prefetch) {
                            tInt -= pcPreFetchHalfwords;
                        }
                        
                        //special rule for ADD OpCode '101100000'
                        if(opCodeEntry.binRepStr1.equals(SPECIAL_ADD_OP_CODE_CHECK) && tInt < 0) {
                            opCodeEntry.binRepStr1 = JsonObjIsOpCodes.ADD_OP_CODE_SPECIAL; //"101100001";
                            tInt *= -1;
                        }
                        
                        if(entry.opCodeArg.num_range != null && entry.opCodeArg.num_range.ones_compliment == true) {
                            tInt = ~tInt;
                        }                        
                        
                        resTmp1 = Integer.toBinaryString(tInt);
                        Integer tInt2 = tInt;
                        if(entry.opCodeArg.bit_shift != null) {
                            if(entry.opCodeArg.bit_shift.shift_amount > 0) {
                                if(!Utils.IsStringEmpty(entry.opCodeArg.bit_shift.shift_dir) && entry.opCodeArg.bit_shift.shift_dir.equals(NUMBER_SHIFT_NAME_LEFT)) {
                                    resTmp1 = Utils.ShiftBinStr(resTmp1, entry.opCodeArg.bit_shift.shift_amount, false, true);
                                } else if(!Utils.IsStringEmpty(entry.opCodeArg.bit_shift.shift_dir) && entry.opCodeArg.bit_shift.shift_dir.equals(NUMBER_SHIFT_NAME_RIGHT)) {
                                    resTmp1 = Utils.ShiftBinStr(resTmp1, entry.opCodeArg.bit_shift.shift_amount, true, true);
                                } else {
                                    throw new ExceptionNumberInvalidShift("Invalid number shift found for source '" + entry.tokenOpCodeArg.source + "' with line number " + entry.tokenOpCodeArg.lineNumAbs);
                                }
                                
                                Integer t = Integer.parseInt(resTmp1, 2);
                                if(tInt2 % 4 == 2 && entry.opCodeArg.bit_shift.shift_amount == 2 && entry.opCodeArg.bit_shift.shift_dir.equals(NUMBER_SHIFT_NAME_RIGHT) == true) {
                                    t++;
                                    resTmp1 = Integer.toBinaryString(t);                                    
                                }                                
                            }
                        }
                                                
                        resTmp1 = Utils.FormatBinString(resTmp1, entry.opCodeArg.bit_series.bit_len);                        
                        tInt = Integer.parseInt(resTmp1, 2);
                        
                        if(entry.opCodeArg.num_range != null) {
                            if(tInt < entry.opCodeArg.num_range.min_value || tInt >  entry.opCodeArg.num_range.max_value) {
                                throw new ExceptionNumberOutOfRange("Integer value " + tInt + " is outside of the specified range " + entry.opCodeArg.num_range.min_value + " to " + entry.opCodeArg.num_range.max_value + " for source '" + entry.tokenOpCodeArg.source + "' with line number " + entry.tokenOpCodeArg.lineNumAbs);
                            }
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
                        throw new ExceptionUnexpectedTokenType("Found unexpected token type '" + entry.tokenOpCodeArg.type_name + "' for line source '" + line.source.source + " and line number " + line.lineNumAbs);
                    }
                    
                    // </editor-fold>
                }
                entry.binRepStr1 = resTmp1;
                res1 += resTmp1;

                if(Utils.IsStringEmpty(resTmp2) == false) {
                    entry.binRepStr2 = resTmp2;
                    res2 += resTmp2;
                } else {
                    entry.binRepStr2 = null;
                }
            }
        
            //Clean out non-bit series entries from the build entries list and sort by bit series desc
            BuildOpCodeEntryThumbSorter buildEntriesSorter = new BuildOpCodeEntryThumbSorter(BuildOpCodeEntryThumbSorterType.BIT_SERIES_DSC);
            buildEntriesSorter.Clean(buildEntries);
            Collections.sort(buildEntries, buildEntriesSorter);

            res1 = "";
            res2 = "";
            for(BuildOpCodeThumb entry : buildEntries) {
                res1 += entry.binRepStr1;
                if(Utils.IsStringEmpty(entry.binRepStr2) == false) {
                    res2 += entry.binRepStr2;
                }
            }

            line.payloadBinRepStrEndianBig1 = res1;
            line.payloadBinRepStrEndianLil1 = Utils.EndianFlipBin(res1);
            
            if(Utils.IsStringEmpty(res2) == false) {
                line.payloadBinRepStrEndianBig2 = res2;
                line.payloadBinRepStrEndianLil2 = Utils.EndianFlipBin(res2);
            }            
            
        } else if(line.isLineLabelDef || line.isLineDirective && line.isLineOpCode) {
            throw new ExceptionInvalidAssemblyLine("Could not find a valid assembly line entry for the given AREA with OpCode line source '" + line.source.source + "' and line number " + line.lineNumAbs + ", " + line.isLineEmpty + ", " + line.isLineDirective + ", " + line.isLineOpCode);
        }
        
        if(eventHandler != null) {
            eventHandler.BuildBinOpCodePost(step, this);
        }        
    }
}