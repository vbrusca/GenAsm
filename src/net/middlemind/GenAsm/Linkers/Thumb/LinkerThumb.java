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
 *
 * @author Victor G. Brusca, Middlemind Games 09/24/2021 10:31 AM EST
 */
public class LinkerThumb implements Linker {
    @Override
    public void RunLinker(Assembler assembler, String assemblySourceFile, String outputDir, Object otherObj) throws Exception {
        Logger.wrl("LinkerThumb: RunLinker");
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
    
        String tmp1 = null;
        String tmp2 = null;        
        TokenLine tmpLine = null;
        int count = 0;
        int prevLineNumAbs = -1;
        ArrayList<Byte> binBe = new ArrayList<>();
        ArrayList<Byte> binLe = new ArrayList<>();
        int totalBytes = 0;
        
        for(int i = 0; i < asm.asmDataTokened.size(); i++) {
            tmpLine = asm.asmDataTokened.get(i);
            tmp1 = tmpLine.lineNumAbs + "";
            tmp1 = Utils.FormatBinString(tmp1, 10, true);
            tmp1 += "\t";            
            tmp2 = "";
            if(fin.containsKey(tmpLine.lineNumAbs) == true && tmpLine.isLineEmpty == false) { 
                if(tmpLine.payloadBinRepStrEndianBig1 != null) {
                    totalBytes += AddBytes(tmpLine.payloadBinRepStrEndianBig1, binBe);
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
        FileUnloader.WriteList(Paths.get(outputDir, "output_assembly_listing_endian_big.list").toString(), lstFile);
        FileUnloader.WriteBuffer(Paths.get(outputDir, "output_assembly_listing_endian_big.bin").toString(), data);
        
        //LITTLE ENDIAN
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
        FileUnloader.WriteList(Paths.get(outputDir, "output_assembly_listing_endian_lil.list").toString(), lstFile);        
        FileUnloader.WriteBuffer(Paths.get(outputDir, "output_assembly_listing_endian_lil.bin").toString(), data);
    }
    
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
        //Logger.wrl("DataSize: " + data.size());
        return ret;
    }
}
