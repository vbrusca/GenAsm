package net.middlemind.GenAsm.Assemblers.Thumb;

import java.util.List;
import net.middlemind.GenAsm.Assemblers.Assembler;
import net.middlemind.GenAsm.Assemblers.AssemblerEventHandler;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsFile;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsValidLine;
import net.middlemind.GenAsm.JsonObjs.Thumb.JsonObjIsValidLines;
import net.middlemind.GenAsm.Tokeners.TokenLine;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 10-07-2021 9:58 AM EST
 */
public interface AssemblerEventHandlerThumb extends AssemblerEventHandler {
    public void PopulateDirectiveArgAndAreaDataPre(int step, Assembler assembler);
    public void PopulateDirectiveArgAndAreaDataPost(int step, Assembler assembler);
    public void PopulateDirectiveArgAndAreaDataLoopPre(int step, Assembler assembler, TokenLine line);
    public void PopulateDirectiveArgAndAreaDataLoopPost(int step, Assembler assembler, TokenLine line);    
    
    public void ValidateDirectiveLinesPre(int step, Assembler assembler);
    public void ValidateDirectiveLinesPost(int step, Assembler assembler);
    public void ValidateDirectiveLinesLoopPre(int step, Assembler assembler, TokenLine line);
    public void ValidateDirectiveLinesLoopPost(int step, Assembler assembler, TokenLine line);    
    
    public void PopulateOpCodeAndArgDataPre(int step, Assembler assembler);
    public void PopulateOpCodeAndArgDataPost(int step, Assembler assembler);
    public void PopulateOpCodeAndArgDataLoopPre(int step, Assembler assembler, TokenLine line);
    public void PopulateOpCodeAndArgDataLoopPost(int step, Assembler assembler, TokenLine line);
    
    public void ValidateOpCodeLinesPre(int step, Assembler assembler);
    public void ValidateOpCodeLinesPost(int step, Assembler assembler);
    public void ValidateOpCodeLinesLoopPre(int step, Assembler assembler, TokenLine line);
    public void ValidateOpCodeLinesLoopPost(int step, Assembler assembler, TokenLine line);
    
    public void CollapseListAndGroupTokensPre(int step, Assembler assembler);
    public void CollapseListAndGroupTokensPost(int step, Assembler assembler);
    public void CollapseListAndGroupTokensLoopPre(int step, Assembler assembler, TokenLine line);
    public void CollapseListAndGroupTokensLoopPost(int step, Assembler assembler, TokenLine line);    
    
    public void ExpandRegisterRangeTokensPre(int step, Assembler assembler);
    public void ExpandRegisterRangeTokensPost(int step, Assembler assembler);
    public void ExpandRegisterRangeTokensLoopPre(int step, Assembler assembler, TokenLine line);
    public void ExpandRegisterRangeTokensLoopPost(int step, Assembler assembler, TokenLine line);    
    
    public void CollapseCommentTokensPre(int step, Assembler assembler);
    public void CollapseCommentTokensPost(int step, Assembler assembler);
    public void CollapseCommentTokensLoopPre(int step, Assembler assembler, TokenLine line);
    public void CollapseCommentTokensLoopPost(int step, Assembler assembler, TokenLine line);    
    
    public void LoadAndParseJsonObjDataPre(int step, Assembler assembler);
    public void LoadAndParseJsonObjDataPost(int step, Assembler assembler);
    public void LoadAndParseJsonObjDataLoopPre(int step, Assembler assembler, JsonObjIsFile entry);
    public void LoadAndParseJsonObjDataLoopPost(int step, Assembler assembler, JsonObjIsFile entry);        
    
    public void LinkJsonObjDataPre(int step, Assembler assembler);
    public void LinkJsonObjDataPost(int step, Assembler assembler);
    public void LinkJsonObjDataLoopPre(int step, Assembler assembler, String s);
    public void LinkJsonObjDataLoopPost(int step, Assembler assembler, String s);            
    
    public void LexerizeAssemblySourcePre(int step, Assembler assembler);
    public void LexerizeAssemblySourcePost(int step, Assembler assembler);
    
    public void ValidateTokenizedLinePre(int step, Assembler assembler, TokenLine line, JsonObjIsValidLines validLines, JsonObjIsValidLine validLineEmpty);
    public void ValidateTokenizedLinePost(int step, Assembler assembler);
    
    public void ValidateTokenizedLinesPre(int step, Assembler assembler);
    public void ValidateTokenizedLinesPost(int step, Assembler assembler);
    public void ValidateTokenizedLinesLoopPre(int step, Assembler assembler, TokenLine line);
    public void ValidateTokenizedLinesLoopPost(int step, Assembler assembler, TokenLine line);        
    
    public void BuildBinLinesPre(int step, Assembler assembler, List<TokenLine> areaLines, AreaThumb area);
    public void BuildBinLinesPost(int step, Assembler assembler);
    
    public void BuildBinDirectivePre(int step, Assembler assembler, TokenLine line);
    public void BuildBinDirectivePost(int step, Assembler assembler);
    
    public void BuildBinOpCodePre(int step, Assembler assembler, TokenLine line);
    public void BuildBinOpCodePost(int step, Assembler assembler);
    
    public void TokenizeLexerArtifactsPre(int step, Assembler assembler);
    public void TokenizeLexerArtifactsPost(int step, Assembler assembler);
    
    public void RunAssemblerPreStep(int step, Assembler assembler);
}
