package net.middlemind.GenAsm.Tokeners;

import java.util.Comparator;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/18/2021 10:20 AM EST
 */
public class TokenSorter  implements Comparator<Token> {
    /**
     * An enumeration that represents the different sort types for this comparator implementation.
     */
    public enum TokenSorterType {
        INDEX_ASC,
        INDEX_DSC
    }
    
    /**
     * 
     */
    public TokenSorterType sortType = TokenSorterType.INDEX_ASC;
    
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */
    public String obj_name = "TokenSorter";
    
    /**
     * 
     */
    public TokenSorter() {
    }
    
    /**
     * 
     * @param sType 
     */
    public TokenSorter(TokenSorterType sType) {
        sortType = sType;
    }
    
    /**
     * 
     * @param a
     * @param b
     * @return 
     */
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
