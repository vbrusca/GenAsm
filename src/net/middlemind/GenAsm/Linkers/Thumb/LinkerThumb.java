package net.middlemind.GenAsm.Linkers.Thumb;

import java.util.ArrayList;
import java.util.List;
import net.middlemind.GenAsm.Assemblers.Assembler;
import net.middlemind.GenAsm.Assemblers.Thumb.AssemblerThumb;
import net.middlemind.GenAsm.Linkers.Linker;
import net.middlemind.GenAsm.Logger;
import net.middlemind.GenAsm.Tokeners.TokenLine;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 09/24/2021 10:31 AM EST
 */
public class LinkerThumb implements Linker {
    @Override
    public void RunLinker(Assembler assembledFiles, String assemblySourceFile, String outputDir, Object otherObj) throws Exception {
        AssemblerThumb asm = (AssemblerThumb)assembledFiles;
        List<TokenLine> fin = new ArrayList<>();
        TokenLine line = null;
        if(asm.areaThumbCode != null && asm.areaThumbData != null) {
            if(asm.areaThumbCode.lineNumEntry < asm.areaThumbData.lineNumEntry) {
                //code first
                for(int i = asm.areaThumbCode.lineNumEntry + 1; i < asm.areaThumbCode.lineNumEnd; i++) {
                    line = asm.asmDataTokened.get(i);
                    if(!line.isLineEmpty && !line.isLineLabelDef) {
                        fin.add(line);
                    }
                }
                
                for(int i = asm.areaThumbData.lineNumEntry + 1; i < asm.areaThumbData.lineNumEnd; i++) {
                    line = asm.asmDataTokened.get(i);
                    if(!line.isLineEmpty && line.isLineDirective && !line.isLineLabelDef) {
                        fin.add(line);
                    }
                }                
            } else {
                //data first
                for(int i = asm.areaThumbData.lineNumEntry + 1; i < asm.areaThumbData.lineNumEnd; i++) {
                    line = asm.asmDataTokened.get(i);
                    if(!line.isLineEmpty && line.isLineDirective && !line.isLineLabelDef) {
                        fin.add(line);
                    }
                }
                
                for(int i = asm.areaThumbCode.lineNumEntry + 1; i < asm.areaThumbCode.lineNumEnd; i++) {
                    line = asm.asmDataTokened.get(i);
                    if(!line.isLineEmpty && !line.isLineLabelDef) {
                        fin.add(line);
                    }
                }                
            }
            
        } else if(asm.areaThumbCode != null) {
            for(int i = asm.areaThumbCode.lineNumEntry + 1; i < asm.areaThumbCode.lineNumEnd; i++) {
                line = asm.asmDataTokened.get(i);
                if(!line.isLineEmpty && !line.isLineLabelDef) {
                    fin.add(line);
                }
            }
            
        } else if(asm.areaThumbData != null) {
            for(int i = asm.areaThumbData.lineNumEntry + 1; i < asm.areaThumbData.lineNumEnd; i++) {
                line = asm.asmDataTokened.get(i);
                if(!line.isLineEmpty && line.isLineDirective && !line.isLineLabelDef) {
                    fin.add(line);
                }
            }
                
        }
        
        Logger.wrl("Found " + fin.size() + " lines of linked assembly json objects");
    }
}
