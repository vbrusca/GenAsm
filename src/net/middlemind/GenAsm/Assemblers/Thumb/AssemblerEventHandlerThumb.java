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
    public void PopulateDirectiveArgAndAreaDataPre(Assembler assembler);
    public void PopulateDirectiveArgAndAreaDataPost(Assembler assembler);
    public void PopulateDirectiveArgAndAreaDataLoopPre(Assembler assembler, TokenLine line);
    public void PopulateDirectiveArgAndAreaDataLoopPost(Assembler assembler, TokenLine line);    
    
    public void ValidateDirectiveLinesPre(Assembler assembler);
    public void ValidateDirectiveLinesPost(Assembler assembler);
    public void ValidateDirectiveLinesLoopPre(Assembler assembler, TokenLine line);
    public void ValidateDirectiveLinesLoopPost(Assembler assembler, TokenLine line);    
    
    public void PopulateOpCodeAndArgDataPre(Assembler assembler);
    public void PopulateOpCodeAndArgDataPost(Assembler assembler);
    public void PopulateOpCodeAndArgDataLoopPre(Assembler assembler, TokenLine line);
    public void PopulateOpCodeAndArgDataLoopPost(Assembler assembler, TokenLine line);
    
    public void ValidateOpCodeLinesPre(Assembler assembler);
    public void ValidateOpCodeLinesPost(Assembler assembler);
    public void ValidateOpCodeLinesLoopPre(Assembler assembler, TokenLine line);
    public void ValidateOpCodeLinesLoopPost(Assembler assembler, TokenLine line);
    
    public void CollapseListAndGroupTokensPre(Assembler assembler);
    public void CollapseListAndGroupTokensPost(Assembler assembler);
    public void CollapseListAndGroupTokensLoopPre(Assembler assembler, TokenLine line);
    public void CollapseListAndGroupTokensLoopPost(Assembler assembler, TokenLine line);    
    
    public void ExpandRegisterRangeTokensPre(Assembler assembler);
    public void ExpandRegisterRangeTokensPost(Assembler assembler);
    public void ExpandRegisterRangeTokensLoopPre(Assembler assembler, TokenLine line);
    public void ExpandRegisterRangeTokensLoopPost(Assembler assembler, TokenLine line);    
    
    public void CollapseCommentTokensPre(Assembler assembler);
    public void CollapseCommentTokensPost(Assembler assembler);
    public void CollapseCommentTokensLoopPre(Assembler assembler, TokenLine line);
    public void CollapseCommentTokensLoopPost(Assembler assembler, TokenLine line);    
    
    public void LoadAndParseJsonObjDataPre(Assembler assembler);
    public void LoadAndParseJsonObjDataPost(Assembler assembler);
    public void LoadAndParseJsonObjDataLoopPre(Assembler assembler, JsonObjIsFile entry);
    public void LoadAndParseJsonObjDataLoopPost(Assembler assembler, JsonObjIsFile entry);        
    
    public void LinkJsonObjDataPre(Assembler assembler);
    public void LinkJsonObjDataPost(Assembler assembler);
    public void LinkJsonObjDataLoopPre(Assembler assembler, String s);
    public void LinkJsonObjDataLoopPost(Assembler assembler, String s);            
    
    public void LexerizeAssemblySourcePre(Assembler assembler);
    public void LexerizeAssemblySourcePost(Assembler assembler);
    
    public void ValidateTokenizedLinePre(Assembler assembler, TokenLine line, JsonObjIsValidLines validLines, JsonObjIsValidLine validLineEmpty);
    public void ValidateTokenizedLinePost(Assembler assembler);
    
    public void ValidateTokenizedLinesPre(Assembler assembler);
    public void ValidateTokenizedLinesPost(Assembler assembler);
    public void ValidateTokenizedLinesLoopPre(Assembler assembler, TokenLine line);
    public void ValidateTokenizedLinesLoopPost(Assembler assembler, TokenLine line);        
    
    public void BuildBinLinesPre(Assembler assembler, List<TokenLine> areaLines, AreaThumb area);
    public void BuildBinLinesPost(Assembler assembler);
    
    public void BuildBinDirectivePre(Assembler assembler, TokenLine line);
    public void BuildBinDirectivePost(Assembler assembler);
    
    public void BuildBinOpCodePre(Assembler assembler, TokenLine line);
    public void BuildBinOpCodePost(Assembler assembler);
    
    public void TokenizeLexerArtifactsPre(Assembler assembler);
    public void TokenizeLexerArtifactsPost(Assembler assembler);    
}
