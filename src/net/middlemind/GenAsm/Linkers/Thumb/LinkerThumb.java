package net.middlemind.GenAsm.Linkers.Thumb;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import net.middlemind.GenAsm.Assemblers.Assembler;
import net.middlemind.GenAsm.Assemblers.Symbol;
import net.middlemind.GenAsm.Assemblers.Thumb.AssemblerThumb;
import net.middlemind.GenAsm.FileIO.FileUnloader;
import net.middlemind.GenAsm.Linkers.Linker;
import net.middlemind.GenAsm.Logger;
import net.middlemind.GenAsm.Tokeners.TokenLine;
import net.middlemind.GenAsm.Utils;

/**
 * An implementation of the linker interface used to link areas of a source assembly file for the ARM Thumb instruction set.
 * @author Victor G. Brusca, Middlemind Games 09/24/2021 10:31 AM EST
 */
public class LinkerThumb implements Linker {
    /**
     * A Boolean value indicating that verbose logging should be used if available.
     */
    public boolean verboseLogs = false;
    /**
     * A Boolean value indicating file output should be turned off.
     */
    public boolean quellFileOutput = false;
    
    /**
     * 
     */
    public ArrayList<Byte> binBe = null;
    
    /**
     * 
     */
    public ArrayList<Byte> binLe = null;
    
    /**
     * 
     */
    public Hashtable<String, String> hexMapBe = null;
    
    /**
     * 
     */
    public Hashtable<String, String> hexMapLe = null;    
    
    /**
     * The main execution method used to link areas generated by an assembler.
     * @param assembler             An instance of an assembler that can assemble the target source file.
     * @param assemblySourceFile    The file path of the assembled source.
     * @param outputDir             The target directory for output files.
     * @param otherObj              An optional generic object that can be used to customize the linker if applicable.
     * @param verbose               A Boolean value indicating that verbose logging should be used.
     * @param quellOutput           A Boolean value indicating that file output should be disabled.
     * @throws Exception 
     */
    @Override
    public void RunLinker(Assembler assembler, String assemblySourceFile, String outputDir, Object otherObj, boolean verbose, boolean quellOutput) throws Exception {
        Logger.wrl("LinkerThumb: RunLinker");
        verboseLogs = verbose;
        quellFileOutput = quellOutput;
        AssemblerThumb asm = (AssemblerThumb)assembler;
        Map<Integer, TokenLine> fin = new Hashtable<>();
        List<String> lstFile = new ArrayList<>();
        TokenLine line = null;
        
        if(asm.areaThumbCode != null && asm.areaThumbData != null) {
            if(asm.areaThumbCode.lineNumEntry < asm.areaThumbData.lineNumEntry) {
                //code first
                for(int i = asm.areaThumbCode.lineNumEntry + 1; i < asm.areaThumbCode.lineNumEnd; i++) {
                    line = asm.asmDataTokened.get(i);
                    if(!line.isLineEmpty && !line.isLineLabelDef) {
                        fin.put(line.lineNumAbs, line);
                    }
                }
                
                for(int i = asm.areaThumbData.lineNumEntry + 1; i < asm.areaThumbData.lineNumEnd; i++) {
                    line = asm.asmDataTokened.get(i);
                    if(!line.isLineEmpty && line.isLineDirective && !line.isLineLabelDef) {
                        fin.put(line.lineNumAbs, line);
                    }
                }                
            } else {
                //data first
                for(int i = asm.areaThumbData.lineNumEntry + 1; i < asm.areaThumbData.lineNumEnd; i++) {
                    line = asm.asmDataTokened.get(i);
                    if(!line.isLineEmpty && line.isLineDirective && !line.isLineLabelDef) {
                        fin.put(line.lineNumAbs, line);
                    }
                }
                
                for(int i = asm.areaThumbCode.lineNumEntry + 1; i < asm.areaThumbCode.lineNumEnd; i++) {
                    line = asm.asmDataTokened.get(i);
                    if(!line.isLineEmpty && !line.isLineLabelDef) {
                        fin.put(line.lineNumAbs, line);
                    }
                }                
            }
            
        } else if(asm.areaThumbCode != null) {
            for(int i = asm.areaThumbCode.lineNumEntry + 1; i < asm.areaThumbCode.lineNumEnd; i++) {
                line = asm.asmDataTokened.get(i);
                if(!line.isLineEmpty && !line.isLineLabelDef) {
                    fin.put(line.lineNumAbs, line);
                }
            }
            
        } else if(asm.areaThumbData != null) {
            for(int i = asm.areaThumbData.lineNumEntry + 1; i < asm.areaThumbData.lineNumEnd; i++) {
                line = asm.asmDataTokened.get(i);
                if(!line.isLineEmpty && line.isLineDirective && !line.isLineLabelDef) {
                    fin.put(line.lineNumAbs, line);
                }
            }
        }
        
        Logger.wrl("LinkerThumb: RunLinker: Found " + fin.size() + " lines of linked assembly json objects");
        Utils.WriteObject(fin, "Linked Assembly Source Code Lines", "output_linked_area_lines_code.json", outputDir);
    
        String cleanHexAddr = null;
        String tmp1 = null;
        String tmp2 = null;        
        TokenLine tmpLine = null;
        int count = 0;
        int prevLineNumAbs = -1;
        binBe = new ArrayList<>();
        binLe = new ArrayList<>();
        hexMapBe = new Hashtable<>();
        hexMapLe = new Hashtable<>();
        int totalBytes = 0;
        boolean addToHexMapOn = false;
        
        for(int i = 0; i < asm.asmDataTokened.size(); i++) {
            tmpLine = asm.asmDataTokened.get(i);
            tmp1 = tmpLine.lineNumAbs + "";
            tmp1 = Utils.FormatBinString(tmp1, 10, true);
            tmp1 += "\t";            
            tmp2 = "";
            if(fin.containsKey(tmpLine.lineNumAbs) == true && tmpLine.isLineEmpty == false) { 
                if(tmpLine.payloadBinRepStrEndianBig1 != null) {
                    totalBytes += AddBytes(tmpLine.payloadBinRepStrEndianBig1, binBe);
                    cleanHexAddr = Utils.CleanHexPrefix(Utils.FormatHexString(tmpLine.addressHex, asm.lineLenBytes*4, true));
                    if(cleanHexAddr.equals("000000F4") == true || cleanHexAddr.equals("080000F4") == true) {
                        addToHexMapOn = true;
                    }
                    
                    if(addToHexMapOn == true) {
                        hexMapBe.put(cleanHexAddr, Utils.CleanHexPrefix(Utils.FormatHexString(Utils.Bin2Hex(tmpLine.payloadBinRepStrEndianBig1), asm.lineLenBytes*2, true)));
                    }
                    tmp1 += Utils.FormatBinString(tmpLine.lineNumActive + "", 10, true) + "\t" + Utils.FormatHexString(tmpLine.addressHex, asm.lineLenBytes*4, true) + "\t" + Utils.PrettyBin(tmpLine.payloadBinRepStrEndianBig1, asm.jsonObjIsOpCodes.bit_series.bit_len, true) + "\t" + Utils.PrettyHex(Utils.Bin2Hex(tmpLine.payloadBinRepStrEndianBig1), asm.lineLenBytes*2, true) + "\t" + tmpLine.source.source;
                } else {
                    if(!Utils.IsStringEmpty(tmpLine.addressHex)) {
                        tmp1 += Utils.FormatBinString(tmpLine.lineNumActive + "", 10, true) + "\t" + Utils.FormatHexString(tmpLine.addressHex, asm.lineLenBytes*4, true) + "\t" + "                " + "\t\t\t" + tmpLine.source.source;
                    } else {
                        tmp1 += Utils.FormatBinString(tmpLine.lineNumActive + "", 10, true) + "\t              \t" + "                " + "\t\t\t" + tmpLine.source.source;                        
                    }
                }
                
                if(Utils.IsStringEmpty(tmpLine.payloadBinRepStrEndianBig2) == false) {
                    tmp2 = tmpLine.lineNumAbs + "";
                    tmp2 = Utils.FormatBinString(tmp2, 10, true);
                    tmp2 += "\t";
                    Integer tt = Integer.parseInt(Utils.CleanHexPrefix(tmpLine.addressHex), 16);
                    tt += asm.lineLenBytes;
                    totalBytes += AddBytes(tmpLine.payloadBinRepStrEndianBig2, binBe);
                    cleanHexAddr = Utils.CleanHexPrefix(Utils.FormatHexString(Integer.toHexString(tt), asm.lineLenBytes*4, true));
                    if(cleanHexAddr.equals("000000F4") == true || cleanHexAddr.equals("080000F4") == true) {
                        addToHexMapOn = true;
                    }
                    
                    if(addToHexMapOn == true) {
                        hexMapBe.put(cleanHexAddr, Utils.CleanHexPrefix(Utils.FormatHexString(Utils.Bin2Hex(tmpLine.payloadBinRepStrEndianBig2), asm.lineLenBytes*2, true)));                    
                    }
                    tmp2 += Utils.FormatBinString((tmpLine.lineNumActive + 1) + "", 10, true) + "\t" + Utils.FormatHexString(Integer.toHexString(tt), asm.lineLenBytes*4, true) + "\t" + Utils.PrettyBin(tmpLine.payloadBinRepStrEndianBig2, asm.jsonObjIsOpCodes.bit_series.bit_len, true) + "\t" + Utils.PrettyHex(Utils.Bin2Hex(tmpLine.payloadBinRepStrEndianBig2), asm.lineLenBytes*2, true) + "\t;LINE 2";
                }                
            } else {
                tmp1 += "   \t    \t                \t" + tmpLine.source.source;
            }
            
            if(prevLineNumAbs != -1 && tmpLine.lineNumAbs != (prevLineNumAbs + 1)) {
                tmp1 += " ######ERROR";
            }
                        
            prevLineNumAbs = tmpLine.lineNumAbs;            
            lstFile.add(tmp1);
            count++;
            if(Utils.IsStringEmpty(tmp2) == false) {
                lstFile.add(tmp2);
                count++;
            }
        }
        
        lstFile.add("");
        lstFile.add(";===== SYMBOL TABLE =====");        
        Symbol sym = null;
        tmp1 = null;
        for(String key : asm.symbols.symbols.keySet()) {
            sym = asm.symbols.symbols.get(key);
            if(sym.value != null) {
                tmp1 = ";Name: " + sym.name + "\tLineNumAbs: " + Utils.FormatBinString(sym.lineNumAbs + "", 10, true) + "\tLineNumActive: " + Utils.FormatBinString(sym.lineNumActive + "", 10, true) + "\tAddressHex: " + Utils.FormatHexString(sym.addressHex, asm.lineLenBytes*4, true) + "\tValue: " + sym.value.toString() + "\tEmptyLineLabel: " + sym.isEmptyLineLabel + "\tIsLabel: " + sym.isLabel + "\tIsStaticValue: " + sym.isStaticValue;        
            } else {
                tmp1 = ";Name: " + sym.name + "\tLineNumAbs: " + Utils.FormatBinString(sym.lineNumAbs + "", 10, true) + "\tLineNumActive: " + Utils.FormatBinString(sym.lineNumActive + "", 10, true) + "\tAddressHex: " + Utils.FormatHexString(sym.addressHex, asm.lineLenBytes*4, true) + "\tValue: " + "n/a" + "\tEmptyLineLabel: " + sym.isEmptyLineLabel + "\tIsLabel: " + sym.isLabel + "\tIsStaticValue: " + sym.isStaticValue;        
            }
            lstFile.add(tmp1);
        }
                
        Logger.wrl("Found total bytes Big Endian: " + totalBytes);        
        int len = binBe.size();
        byte[] data = new byte[len];
        for(int i = 0; i < len; i++) {
            data[i] = (byte)binBe.get(i);
        }
        lstFile.add("");
        lstFile.add(";===== BINARY OUTPUT =====");         
        lstFile.add(";Expected file output size: " + data.length);        
        
        if(!quellFileOutput) {
            FileUnloader.WriteList(Paths.get(outputDir, "output_assembly_listing_endian_big.list").toString(), lstFile);
            FileUnloader.WriteBuffer(Paths.get(outputDir, "output_assembly_listing_endian_big.bin").toString(), data);
        }
        
        //LITTLE ENDIAN
        cleanHexAddr = null;
        addToHexMapOn = false;
        totalBytes = 0;
        lstFile.clear();
        tmp1 = null;
        tmp2 = null;
        tmpLine = null;
        count = 0;
        prevLineNumAbs = -1;
        for(int i = 0; i < asm.asmDataTokened.size(); i++) {
            tmpLine = asm.asmDataTokened.get(i);
            tmp1 = tmpLine.lineNumAbs + "";
            tmp1 = Utils.FormatBinString(tmp1, 10, true);
            tmp1 += "\t";
            tmp2 = "";
            if(fin.containsKey(tmpLine.lineNumAbs) == true && tmpLine.isLineEmpty == false) { 
                if(tmpLine.payloadBinRepStrEndianLil1 != null) {
                    totalBytes += AddBytes(tmpLine.payloadBinRepStrEndianLil1, binLe);
                    cleanHexAddr = Utils.CleanHexPrefix(Utils.FormatHexString(tmpLine.addressHex, asm.lineLenBytes*4, true));
                    if(cleanHexAddr.equals("000000F4") == true || cleanHexAddr.equals("080000F4") == true) {
                        addToHexMapOn = true;
                    }
                    
                    if(addToHexMapOn == true) {                    
                        hexMapLe.put(cleanHexAddr, Utils.CleanHexPrefix(Utils.FormatHexString(Utils.Bin2Hex(tmpLine.payloadBinRepStrEndianLil1), asm.lineLenBytes*2, true)));
                        //Logger.wrl("Putting: " + cleanHexAddr + " Value: " + Utils.CleanHexPrefix(Utils.FormatHexString(Utils.Bin2Hex(tmpLine.payloadBinRepStrEndianLil1), asm.lineLenBytes*2, true)));
                    }
                    tmp1 += Utils.FormatBinString(tmpLine.lineNumActive + "", 10, true) + "\t" + Utils.FormatHexString(tmpLine.addressHex, asm.lineLenBytes*4, true) + "\t" + Utils.PrettyBin(tmpLine.payloadBinRepStrEndianLil1, asm.jsonObjIsOpCodes.bit_series.bit_len, false) + "\t" + Utils.PrettyHex(Utils.Bin2Hex(tmpLine.payloadBinRepStrEndianLil1), asm.lineLenBytes*2, true) + "\t" + tmpLine.source.source;
                } else {
                    if(!Utils.IsStringEmpty(tmpLine.addressHex)) {
                        tmp1 += Utils.FormatBinString(tmpLine.lineNumActive + "", 10, true) + "\t" + Utils.FormatHexString(tmpLine.addressHex, asm.lineLenBytes*4, true) + "\t" + "                " + "\t\t\t" + tmpLine.source.source;    
                    } else {
                        tmp1 += Utils.FormatBinString(tmpLine.lineNumActive + "", 10, true) + "\t              \t" + "                " + "\t\t\t" + tmpLine.source.source;                            
                    }
                }
                
                if(Utils.IsStringEmpty(tmpLine.payloadBinRepStrEndianLil2) == false) {
                    tmp2 = tmpLine.lineNumAbs + "";
                    tmp2 = Utils.FormatBinString(tmp2, 10, true);
                    tmp2 += "\t";
                    Integer tt = Integer.parseInt(Utils.CleanHexPrefix(tmpLine.addressHex), 16);
                    tt += asm.lineLenBytes;
                    totalBytes += AddBytes(tmpLine.payloadBinRepStrEndianLil2, binLe);
                    cleanHexAddr = Utils.CleanHexPrefix(Utils.FormatHexString(Integer.toHexString(tt), asm.lineLenBytes*4, true));
                    if(cleanHexAddr.equals("000000F4") == true || cleanHexAddr.equals("080000F4") == true) {
                        addToHexMapOn = true;
                    }
                    
                    if(addToHexMapOn == true) {
                        //Logger.wrl("Putting: " + cleanHexAddr + " Value: " + Utils.CleanHexPrefix(Utils.FormatHexString(Utils.Bin2Hex(tmpLine.payloadBinRepStrEndianLil2), asm.lineLenBytes*2, true)));
                        hexMapLe.put(cleanHexAddr, Utils.CleanHexPrefix(Utils.FormatHexString(Utils.Bin2Hex(tmpLine.payloadBinRepStrEndianLil2), asm.lineLenBytes*2, true)));  
                    }
                    tmp2 += Utils.FormatBinString((tmpLine.lineNumActive + 1) + "", 10, true) + "\t" + Utils.FormatHexString(Integer.toHexString(tt), asm.lineLenBytes*4, true) + "\t" + Utils.PrettyBin(tmpLine.payloadBinRepStrEndianLil2, asm.jsonObjIsOpCodes.bit_series.bit_len, true) + "\t" + Utils.PrettyHex(Utils.Bin2Hex(tmpLine.payloadBinRepStrEndianLil2), asm.lineLenBytes*2, true) + "\t;LINE 2";
                }                
            } else {
                tmp1 += "   \t    \t                \t" + tmpLine.source.source;
            }
            
            if(prevLineNumAbs != -1 && tmpLine.lineNumAbs != (prevLineNumAbs + 1)) {
                tmp1 += " ######ERROR";
            }
                        
            prevLineNumAbs = tmpLine.lineNumAbs;            
            lstFile.add(tmp1);
            count++;
            if(Utils.IsStringEmpty(tmp2) == false) {
                lstFile.add(tmp2);
                count++;
            }
        }
        
        lstFile.add("");
        lstFile.add(";===== SYMBOL TABLE =====");        
        sym = null;
        tmp1 = null;
        for(String key : asm.symbols.symbols.keySet()) {
            sym = asm.symbols.symbols.get(key);
            if(sym.value != null) {
                tmp1 = ";Name: " + sym.name + "\tLineNumAbs: " + Utils.FormatBinString(sym.lineNumAbs + "", 10, true) + "\tLineNumActive: " + Utils.FormatBinString(sym.lineNumActive + "", 10, true) + "\tAddressInt: " + sym.addressInt + "\tAddressHex: " + Utils.FormatHexString(sym.addressHex, asm.lineLenBytes*4, true) + "\tValue: " + sym.value.toString() + "\tEmptyLineLabel: " + sym.isEmptyLineLabel + "\tIsLabel: " + sym.isLabel + "\tIsStaticValue: " + sym.isStaticValue;        
            } else {
                tmp1 = ";Name: " + sym.name + "\tLineNumAbs: " + Utils.FormatBinString(sym.lineNumAbs + "", 10, true) + "\tLineNumActive: " + Utils.FormatBinString(sym.lineNumActive + "", 10, true) + "\tAddressInt: " + sym.addressInt + "\tAddressHex: " + Utils.FormatHexString(sym.addressHex, asm.lineLenBytes*4, true) + "\tValue: " + "n/a" + "\tEmptyLineLabel: " + sym.isEmptyLineLabel + "\tIsLabel: " + sym.isLabel + "\tIsStaticValue: " + sym.isStaticValue;        
            }
            lstFile.add(tmp1);
        }
        
        Logger.wrl("Found total bytes Lil Endian: " + totalBytes);
        len = binLe.size();
        data = new byte[len];
        for(int i = 0; i < len; i++) {
            data[i] = (byte)binLe.get(i);
        }
        lstFile.add("");
        lstFile.add(";===== BINARY OUTPUT =====");         
        lstFile.add(";Expected file output size: " + data.length);
        
        if(!quellFileOutput) {
            FileUnloader.WriteList(Paths.get(outputDir, "output_assembly_listing_endian_lil.list").toString(), lstFile);        
            FileUnloader.WriteBuffer(Paths.get(outputDir, "output_assembly_listing_endian_lil.bin").toString(), data);
        }
    }
    
    /**
     * A method used to split a binary string into bytes and add them to the data argument.
     * @param binStr    The binary string to split into bytes to add to the data argument.
     * @param data      A data structure to store the extracted bytes.
     * @return          An integer representing the number of bytes added to the data argument.
     */
    public int AddBytes(String binStr, ArrayList<Byte> data) {
        int len = binStr.length()/8;
        String s = null;
        Byte b = null;
        int ret = 0;
        for(int i = 0; i < len; i++) {
            s = binStr.substring((i * 8), (i * 8) + 8);
            b = (byte)Integer.parseInt(s, 2);
            data.add(b);
            ret++;
        }
        return ret;
    }
}
