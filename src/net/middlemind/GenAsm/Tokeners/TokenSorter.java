package net.middlemind.GenAsm.Tokeners;

import java.util.Comparator;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/18/2021 10:20 AM EST
 */
public class TokenSorter  implements Comparator<Token> {
    public enum TokenSorterType {
        INDEX_ASC,
        INDEX_DSC
    }
    
    public TokenSorterType sortType = TokenSorterType.INDEX_ASC;
    public String obj_name = "TokenSorter";
    
    public TokenSorter() {
    }
    
    public TokenSorter(TokenSorterType sType) {
        sortType = sType;
    }
    
    @Override
    public int compare(Token a, Token b) {
        if(sortType == TokenSorterType.INDEX_ASC) {
            return (a.index - b.index);
        } else if(sortType == TokenSorterType.INDEX_DSC) {
            return (b.index - a.index);
        } else {
            return (a.index - b.index);
        }
    }  
    
}
