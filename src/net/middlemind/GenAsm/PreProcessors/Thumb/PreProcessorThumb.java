package net.middlemind.GenAsm.PreProcessors.Thumb;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionUnsupportedAssemblyFileType;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionUnsupportedBinaryFileLength;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionUnsupportedStringLength;
import net.middlemind.GenAsm.FileIO.FileLoader;
import net.middlemind.GenAsm.FileIO.FileUnloader;
import net.middlemind.GenAsm.Logger;
import net.middlemind.GenAsm.PreProcessors.PreProcessor;
import net.middlemind.GenAsm.Utils;

/**
 * An implementation of the preprocessor interface used to process thumb assembly source code.
 * @author Victor G. Brusca, Middlemind Games 10-06-2021 10:04 AM EST
 */
@SuppressWarnings({"UnusedAssignment", "UseOfObsoleteCollectionType"})
public class PreProcessorThumb implements PreProcessor {
    /**
     * A static array of strings representing the supported pre-processor directives.
     */
    public static String[] PP_DIRECTIVES = { "$INCBIN", "$INCASM", "$NOP", "$STRING", "$FLPINCBIN", "$FLIPSTRING" };

    /**
     * A static integer representing the array index of the include bin pre-processor directive.
     */
    public static int PPD_INCBIN_IDX = 0;

    /**
     * A static integer representing the array index of the include assembly pre-processor directive.
     */
    public static int PPD_INCASM_IDX = 1;
    
    /**
     * A static integer representing the array index of the no op-code assembly pre-processor directive.
     */
    public static int PPD_NOP_IDX = 2;
    
    /**
     * A static integer representing the array index of the string assembly pre-processor directive.
     */
    public static int PPD_STRING_IDX = 3;    
    
    /**
     * A static integer representing the array index of the include flip bin pre-processor directive.
     */
    public static int PPD_FLPINCBIN_IDX = 4;    
    
    /**
     * A static integer representing the array index of the flip string assembly pre-processor directive.
     */
    public static int PPD_FLIPSTRING_IDX = 5;        
    
    /**
     * A static byte representing the padding character used to mark the end of a line of text as well as to align the text to 16bit.
     */
    public static byte PPD_EOL_BYTE = (byte)255;
    
    /**
     * A static string representing the name of the output file for the pre-processor.
     */
    public static String OUTPUT_FILE_NAME = "output_pre_processed_assembly.txt";

    /**
     * A static string array that represents the supported assembly file extensions.
     */
    public static String[] EXT_ASM_FILES = { ".asm", ".txt", ".s" };
    
    /**
     * A static string array that represents the supported binary file extensions.
     */    
    public static String[] EXT_BIN_FILES = { ".bin", ".raw", ".dat" };  

    /**
     * An integer representing the maximum file size supported for external binary files.
     */
    public static int MAX_EXT_BIN_FILE_LEN = 255;

    /**
     * An integer representing the maximum string length supported for in-line strings..
     */
    public static int MAX_STRING_LEN = 32;
    
    /**
     * The full file name of the assembly source file.
     */
    public String asmSourceFile = "";
    
    /**
     * The full file path to the root of the output directory.
     */
    public String rootOutputDir = "";
    
    /**
     * An optional generic object used to customize the pre-processor.
     */
    public Object other = null;
    
    /**
     * A method used to append a string to each entry in a list of strings.
     * @param lines     A list of strings representing the lines to alter.
     * @param prefix  The string value to apply as a prefix to the lines arguments.
     */
    public void AddPrefix(List<String> lines, String prefix) {
        for(int i = 0; i < lines.size(); i++) {
            lines.set(i, prefix + lines.get(i));
        }
    }
    
    /**
     * A method used to run the pre-processor on an assembly source file.
     * @param assemblySourceFile    The full path to the assembly source file to process.
     * @param outputDir             The output directory to use to write output files to.
     * @param otherObj              An optional generic Java class used to customize the pre-processor.
     * @return                      A list of string representing the pre-processed file.
     * @throws Exception            An exception is thrown if an error is encountered during the pre-processor run.
     */    
    @Override
    @SuppressWarnings("IndexOfReplaceableByContains")
    public List<String> RunPreProcessor(String assemblySourceFile, String outputDir, Object otherObj) throws Exception {
        Logger.wrl("PreProcessorThumb: RunPreProcessor: Start");
        asmSourceFile = assemblySourceFile;
        other = otherObj;
        rootOutputDir = outputDir;
        
        List<String> ret = FileLoader.Load(assemblySourceFile);
        int[] idxs = null;
        String directive = null;
        int sIdx = -1;
        String tmp = null;
        String fileName = null;
        int count = 0;
        List<String> incAsm;
        Map<String, List<String>> asmFileAdj = new Hashtable<>();
        Map<String, String> asmFileAdjNames = new Hashtable<>();
        Map<String, Integer> asmFileAdjTypes = new Hashtable<>();
        File f = null;
        String lcFileName = null;
        byte[] incBin = null;
        int numBytes = -1;
        int numHalfWords = -1;
        boolean paddingOn = false;
        String whiteSpace = "";
        Map<Integer, String> asmFileReplace = new Hashtable<>();
        
        for(String s : ret) {
            if(s != null && s.equals("") == false) {
                idxs = Utils.StringContainsArrayEntry(PP_DIRECTIVES, s);
                if(idxs != null) {
                    directive = PP_DIRECTIVES[idxs[0]];
                    
                    if(s.indexOf(";") != -1) {
                        whiteSpace = s.substring(0, s.indexOf(";"));
                    }
                    
                    if(idxs[0] == PPD_NOP_IDX) {
                        asmFileReplace.put(count, whiteSpace + "MOV\tR8, R8\t\t;NOP preprocessor directive");

                    } else if(idxs[0] == PPD_INCASM_IDX) {
                        sIdx = idxs[1];
                        tmp = s.substring(s.indexOf("|") + 1, s.lastIndexOf("|")).trim();
                        
                        fileName = tmp;
                        f = new File(fileName);
                        lcFileName = f.getName().toLowerCase();
                    
                        if(Utils.StringContainsArrayEntry(EXT_ASM_FILES, lcFileName) != null) {                            
                            incAsm = FileLoader.Load(fileName);
                            incAsm.add(0, ";Found file with line count: " + incAsm.size() + ", byte count: " + f.length());
                            incAsm.add(1, "");
                            incAsm.add(incAsm.size(), "");
                            
                            asmFileAdjTypes.put(s, idxs[0]);
                            asmFileAdj.put(s, incAsm);
                            AddPrefix(incAsm, whiteSpace);
                            asmFileAdjNames.put(s, fileName);
                            
                        } else {
                            throw new ExceptionUnsupportedAssemblyFileType("Cannot load assembly files without file extension .asm or .txt for file name, " + fileName);
                        }
                        
                    } else if(idxs[0] == PPD_INCBIN_IDX || idxs[0] == PPD_FLPINCBIN_IDX) {
                        sIdx = idxs[1];
                        tmp = s.substring(s.indexOf("|") + 1, s.lastIndexOf("|")).trim();
                        
                        fileName = tmp;
                        f = new File(fileName);
                        lcFileName = f.getName().toLowerCase();
                    
                        if(Utils.StringContainsArrayEntry(EXT_BIN_FILES, lcFileName) != null) {
                            incBin = FileLoader.LoadBin(fileName);                            
                            numBytes = incBin.length;
                            numHalfWords = numBytes / 2;
                            paddingOn = false;
                            
                            if(numBytes > MAX_EXT_BIN_FILE_LEN) {
                                throw new ExceptionUnsupportedBinaryFileLength("Cannot load an external assembly file with length greater than " + MAX_EXT_BIN_FILE_LEN + " bytes");
                            }
                            
                            if(numBytes % 2 == 1) {
                                paddingOn = true;
                            }
                            
                            short[] shorts = new short[numHalfWords];
                            ByteBuffer.wrap(incBin).asShortBuffer().get(shorts);
                            List<String> nret = new ArrayList<>();
                            String hex = null;
                            byte[] tIncBin = null;
                            int tCount = 0;
                            String binLine = null;
                            int i = 0;
                            String idxStr = null;
                            nret.add(";Found file with byte count: " + numBytes + ", word count: " + numHalfWords + ", and padding on: " + paddingOn);
                            nret.add("");
                            
                            for(i = 0; i < shorts.length; i++) {
                                hex = Integer.toHexString(shorts[i] & 0xffff);
                                hex = Utils.FormatHexString(hex.toUpperCase(), 4);
                                if(idxs[0] == PPD_FLPINCBIN_IDX) {
                                    hex = Utils.EndianFlipHex(hex);
                                }
                                hex = Utils.FormatHexString(hex.toUpperCase(), 4);
                                binLine = Integer.toHexString((tCount * 2) & 0xffff);
                                binLine = Utils.FormatHexString(binLine.toUpperCase(), 4);
                                idxStr = Utils.FormatBinString((i + ""), 4, true);
                                
                                nret.add("@DCHW #" + hex + "\t\t;index: " + idxStr + "\taddress: " + binLine);
                                tCount++;
                            }
                            
                            i++;
                            
                            if(paddingOn) {
                                shorts = new short[1];
                                tIncBin = new byte[] { incBin[incBin.length - 1], 0 };
                                ByteBuffer.wrap(tIncBin).asShortBuffer().get(shorts);
                                
                                hex = Integer.toHexString(shorts[0] & 0xffff);
                                hex = Utils.FormatHexString(hex.toUpperCase(), 4);
                                if(idxs[0] == PPD_FLPINCBIN_IDX) {
                                    Logger.wrl("Flipping bin entry: " + hex);
                                    hex = Utils.EndianFlipHex(hex);
                                    Logger.wrl("Flipping bin entry: " + hex);                                    
                                }                                
                                hex = Utils.FormatHexString(hex.toUpperCase(), 4);
                                binLine = Integer.toHexString((tCount * 2) & 0xffff);
                                binLine = Utils.FormatHexString(binLine.toUpperCase(), 4);
                                idxStr = Utils.FormatBinString((i + ""), 4, true);
                                
                                nret.add("@DCHW #" + hex + "\t\t;index: " + idxStr + "\taddress: " + binLine);
                                tCount++;
                            }

                            nret.add("");
                            asmFileAdjTypes.put(s, idxs[0]);                            
                            asmFileAdj.put(s, nret);
                            AddPrefix(nret, whiteSpace);
                            asmFileAdjNames.put(s, fileName);
                            
                        } else {
                            throw new ExceptionUnsupportedAssemblyFileType("Cannot load binary files without file extension .bin or .raw for file name, " + fileName);
                        }
                    } else if(idxs[0] == PPD_STRING_IDX || idxs[0] == PPD_FLIPSTRING_IDX) {
                        sIdx = idxs[1];
                        tmp = s.substring(s.indexOf("|") + 1, s.lastIndexOf("|")).trim();
                        
                        fileName = "String replacement: " + tmp;                        
                        tmp += ((char)PPD_EOL_BYTE) + "";
                        char[] crs = tmp.toCharArray();
                        int len = crs.length;
                        if(len % 2 != 0) {
                            paddingOn = true;
                            len++;
                        }
                        
                        byte[] binCrs = new byte[len];
                        for(int i = 0; i < len; i++) {
                            if(i < crs.length) {
                                binCrs[i] = ((byte)crs[i]);
                                Logger.wrl("Char: " + tmp.charAt(i) + " Val: " + crs[i]);                                        
                            } else {
                                binCrs[i] = PPD_EOL_BYTE;
                            }
                        }
                        
                        numBytes = len;
                        numHalfWords = numBytes / 2;
                        if(numBytes > MAX_STRING_LEN) {
                            throw new ExceptionUnsupportedStringLength("Cannot load an in-line string with length greater than " + MAX_STRING_LEN + " characters");
                        }
                                                        
                        short[] shorts = new short[numHalfWords];
                        ByteBuffer.wrap(binCrs).asShortBuffer().get(shorts);
                        List<String> nret = new ArrayList<>();
                        String hex = null;
                        int tCount = 0;
                        String binLine = null;
                        int i = 0;
                        String idxStr = null;
                        nret.add(";Found string byte count: " + numBytes + ", word count: " + numHalfWords + ", and padding on: " + paddingOn);
                        nret.add("");
                            
                        for(i = 0; i < shorts.length; i++) {
                            hex = Integer.toHexString(shorts[i] & 0xffff);
                            
                            if(idxs[0] == PPD_FLIPSTRING_IDX) {
                                hex = Utils.EndianFlipHex(hex);
                            }
                            
                            hex = Utils.FormatHexString(hex.toUpperCase(), 4);
                            binLine = Integer.toHexString((tCount * 2) & 0xffff);
                            binLine = Utils.FormatHexString(binLine.toUpperCase(), 4);
                            idxStr = Utils.FormatBinString((i * 2 + ""), 4, true);

                            if((i*2 + 1) < binCrs.length) {
                                nret.add("@DCHW #" + hex + "\t\t;index: " + idxStr + "\t" + (char)binCrs[i*2] + " " + (char)binCrs[i*2 + 1]);
                            } else {
                                nret.add("@DCHW #" + hex + "\t\t;index: " + idxStr + "\t" + (char)binCrs[i*2]);                                
                            }
                            tCount++;
                        }
                        
                        nret.add("");
                        asmFileAdj.put(s, nret);
                        AddPrefix(nret, whiteSpace);
                        asmFileAdjNames.put(s, fileName);                        
                    }
                }
            }
            count++;
        }

        String tmpLine = null;
        for(Integer key : asmFileReplace.keySet()) {
            tmpLine = asmFileReplace.get(key);
            ret.remove(key.intValue());
            ret.add(key, tmpLine);
        }        
                
        int idxTmp = -1;
        int rowCountOrig = -1;
        int rowCountNew = -1;
        int type = -1;
        fileName = null;
        for(String key : asmFileAdj.keySet()) {
            rowCountOrig = ret.size();
            fileName = asmFileAdjNames.get(key);
            idxTmp = ret.indexOf(key);
            ret.addAll(idxTmp + 1, asmFileAdj.get(key));
            rowCountNew = ret.size();
            
            if(asmFileAdjTypes.containsKey(key) == true) {
                type = asmFileAdjTypes.get(key);
            } else {
                type = -1;
            }
            
            if(type == PPD_INCBIN_IDX || type == PPD_INCASM_IDX) {
                Logger.wrl("PreProcessorThumb: Adding inline assembly at line " + (idxTmp + 1) + " from file, " + fileName);
                Logger.wrl("PreProcessorThumb: Assembly file row count before include, " + rowCountOrig + ", and after include, " + rowCountNew + ".");
            } else {
                Logger.wrl("PreProcessorThumb: Adding inline assembly at line " + (idxTmp + 1) + " from string, " + fileName);
                Logger.wrl("PreProcessorThumb: Assembly file row count before include, " + rowCountOrig + ", and after include, " + rowCountNew + ".");                                    
            }
        }

        FileUnloader.WriteList(Paths.get(rootOutputDir, OUTPUT_FILE_NAME).toString(), ret);        
        return ret;
    }
}