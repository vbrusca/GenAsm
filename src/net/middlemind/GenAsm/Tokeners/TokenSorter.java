package net.middlemind.GenAsm.Tokeners;

import java.util.Comparator;

/**
 * An implementation of the comparator class used to sort a list of tokens.
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
     * An instance of the token sorter type that defines how to sort the tokens.
     */
    public TokenSorterType sortType = TokenSorterType.INDEX_ASC;
    
    /**
     * A string representing the name of this class. This is used to define the class in JSON output files.
     */
    public String obj_name = "TokenSorter";
    
    /**
     * A simple constructor.
     */
    public TokenSorter() {
    }
    
    /**
     * A constructor that takes a token sorter type instance as an argument.
     * @param sType The token sorter type used to define how to sort tokens.
     */
    public TokenSorter(TokenSorterType sType) {
        sortType = sType;
    }
    
    /**
     * A method used to sort two token instances.
     * @param a The first token to compare.
     * @param b The second token to compare.
     * @return  An integer representing the sort order of the tokens provided.
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
