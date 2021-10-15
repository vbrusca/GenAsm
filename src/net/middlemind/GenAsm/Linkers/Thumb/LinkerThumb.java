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
    
        String tmp = null;
        TokenLine tmpLine = null;
        int count = 0;
        int prevLineNumAbs = -1;
        for(int i = 0; i < asm.asmDataTokened.size(); i++) {
            tmpLine = asm.asmDataTokened.get(i);
            tmp = tmpLine.lineNumAbs + "";
            tmp = Utils.FormatBinString(tmp, 3, true);
            tmp += "\t";
            if(fin.containsKey(tmpLine.lineNumAbs) == true && tmpLine.isLineEmpty == false) { 
                if(tmpLine.payloadBinRepStrEndianBig != null) {
                    tmp += Utils.FormatBinString(tmpLine.lineNumActive + "", 3, true) + "\t" + tmpLine.addressHex + "\t" + Utils.PrettyBin(Utils.Bin2Hex(tmpLine.payloadBinRepStrEndianBig), asm.lineLenBytes, true) + "\t" + Utils.PrettyHex(tmpLine.payloadBinRepStrEndianBig, asm.jsonObjIsOpCodes.bit_series.bit_len, true) + "\t" + tmpLine.source.source;
                } else {
                    tmp += Utils.FormatBinString(tmpLine.lineNumActive + "", 3, true) + "\t" + tmpLine.addressHex + "\t" + "                " + "\t\t\t" + tmpLine.source.source;
                }
            } else {
                tmp += "   \t    \t                \t" + tmpLine.source.source;
            }
            
            if(prevLineNumAbs != -1 && tmpLine.lineNumAbs != (prevLineNumAbs + 1)) {
                tmp += " ######ERROR";
            }
            
            prevLineNumAbs = tmpLine.lineNumAbs;            
            lstFile.add(tmp);
            count++;            
        }
        
        lstFile.add("");
        lstFile.add(";===== SYMBOL TABLE =====");        
        Symbol sym = null;
        tmp = null;
        for(String key : asm.symbols.symbols.keySet()) {
            sym = asm.symbols.symbols.get(key);
            if(sym.value != null) {
                tmp = ";Name: " + sym.name + "\tLineNumAbs: " + sym.lineNumAbs + "\tLineNumActive: " + sym.lineNumActive + "\tAddressHex: " + sym.addressHex + "\tValue: " + sym.value.toString() + "\tEmptyLineLabel: " + sym.isEmptyLineLabel + "\tIsLabel: " + sym.isLabel + "\tIsStaticValue: " + sym.isStaticValue;        
            } else {
                tmp = ";Name: " + sym.name + "\tLineNumAbs: " + sym.lineNumAbs + "\tLineNumActive: " + sym.lineNumActive + "\tAddressHex: " + sym.addressHex + "\tValue: " + "n/a" + "\tEmptyLineLabel: " + sym.isEmptyLineLabel + "\tIsLabel: " + sym.isLabel + "\tIsStaticValue: " + sym.isStaticValue;        
            }
            lstFile.add(tmp);
        }
        
        FileUnloader.WriteList(Paths.get(outputDir, "output_assembly_listing.list").toString(), lstFile);
    }
}
