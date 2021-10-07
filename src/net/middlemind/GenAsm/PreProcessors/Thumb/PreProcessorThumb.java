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
import net.middlemind.GenAsm.FileIO.FileLoader;
import net.middlemind.GenAsm.FileIO.FileUnloader;
import net.middlemind.GenAsm.Logger;
import net.middlemind.GenAsm.PreProcessors.PreProcessor;
import net.middlemind.GenAsm.Utils;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 10-06-2021 10:04 AM EST
 */
@SuppressWarnings("UnusedAssignment")
public class PreProcessorThumb implements PreProcessor {
    public static String[] PP_DIRECTIVES = { "$INCBIN", "$INCASM" };
    public static int PPD_INCBIN_IDX = 0;
    public static int PPD_INCASM_IDX = 1;
    public static String OUTPUT_FILE_NAME = "output_pre_processed_assembly.txt";
    public static String[] EXT_ASM_FILES = { ".asm", ".txt" };
    public static String[] EXT_BIN_FILES = { ".bin", ".raw", ".dat" };  
    public static int MAX_EXT_BIN_FILE_LEN = 255;
    
    public String asmSourceFile = "";
    public String rootOutputDir = "";
    public Object other = null;
    
    private void AddAppendix(List<String> lines, String appendix) {
        for(int i = 0; i < lines.size(); i++) {
            lines.set(i, appendix + lines.get(i));
        }
    }
    
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
        File f = null;
        String lcFileName = null;
        byte[] incBin = null;
        int numBytes = -1;
        int numHalfWords = -1;
        boolean paddingOn = false;
        String whiteSpace = "";
        
        for(String s : ret) {
            if(s != null && s.equals("") == false) {
                idxs = Utils.StringContainsArrayEntry(PP_DIRECTIVES, s);
                if(idxs != null) {
                    directive = PP_DIRECTIVES[idxs[0]];
                    sIdx = idxs[1];
                    tmp = s.substring(s.indexOf("|") + 1, s.lastIndexOf("|")).trim();
                    fileName = tmp;
                    f = new File(fileName);
                    lcFileName = f.getName().toLowerCase();
                    
                    if(s.indexOf(";") != -1) {
                        whiteSpace = s.substring(0, s.indexOf(";"));
                    }
                        
                    if(idxs[0] == PPD_INCASM_IDX) {
                        if(Utils.StringContainsArrayEntry(EXT_ASM_FILES, lcFileName) != null) {                            
                            incAsm = FileLoader.Load(fileName);
                            incAsm.add(0, ";Found file with line count: " + incAsm.size() + ", byte count: " + f.length());
                            incAsm.add(1, "");
                            incAsm.add(incAsm.size(), "");
                            
                            asmFileAdj.put(s, incAsm);
                            AddAppendix(incAsm, whiteSpace);
                            asmFileAdjNames.put(s, fileName);
                            
                        } else {
                            throw new ExceptionUnsupportedAssemblyFileType("Cannot load assembly files without file extension .asm or .txt for file name, " + fileName);
                        }
                        
                    } else if(idxs[0] == PPD_INCBIN_IDX) {
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
                                binLine = Integer.toHexString((tCount * 2) & 0xffff);
                                binLine = Utils.FormatHexString(binLine.toUpperCase(), 4);
                                idxStr = Utils.FormatBinString((i + ""), 4, true);
                                
                                nret.add("@DCHW #" + hex + "\t\t;index: " + idxStr + "\taddress: " + binLine);
                                tCount++;
                            }

                            nret.add("");
                            asmFileAdj.put(s, nret);
                            AddAppendix(nret, whiteSpace);
                            asmFileAdjNames.put(s, fileName);
                            
                        } else {
                            throw new ExceptionUnsupportedAssemblyFileType("Cannot load binary files without file extension .bin or .raw for file name, " + fileName);
                        }
                    }
                }
            }
            count++;
        }

        int idxTmp = -1;
        int rowCountOrig = -1;
        int rowCountNew = -1;
        fileName = null;
        for(String key : asmFileAdj.keySet()) {
            rowCountOrig = ret.size();
            fileName = asmFileAdjNames.get(key);
            idxTmp = ret.indexOf(key);
            ret.addAll(idxTmp + 1, asmFileAdj.get(key));
            rowCountNew = ret.size();
            Logger.wrl("PreProcessorThumb: Adding inline assembly at line " + (idxTmp + 1) + " from file, " + fileName);
            Logger.wrl("PreProcessorThumb: Assembly file row count before include, " + rowCountOrig + ", and after include, " + rowCountNew + ".");                    
        }

        FileUnloader.WriteList(Paths.get(rootOutputDir, OUTPUT_FILE_NAME).toString(), ret);        
        return ret;
    }
    
}
