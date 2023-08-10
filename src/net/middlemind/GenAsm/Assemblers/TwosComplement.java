package net.middlemind.GenAsm.Assemblers;

/**
 * A class that provides helper methods for calculating the two's complement of a binary string.
 * @author Victor G. Brusca, Middlemind Games 09/20/2021 11:50AM EST
 */
public class TwosComplement {
    /**
     * A static class that returns the two's complement of the provided binary string.
     * @param binStr    The binary string to return the two's complement for.
     * @return          The binary two's complement of the binStr parameter. 
     */
    public static String GetTwosCompliment(String binStr) {
        int n = binStr.length();
      
        // Traverse the string to get first '1' from
        // the last of string
        int i;
        for (i = n - 1; i >= 0; i--) {
            if (binStr.charAt(i) == '1') {
                break;
            }
        }
      
        // If there exists no '1' concat 1 at the
        // starting of string
        if (i == -1) {
            return "1" + binStr;
        }
      
        // Continue traversal after the position of
        // first '1'
        char[] cr = binStr.toCharArray();
        for (int k = i - 1; k >= 0; k--)
        {
            cr[k] = flip(cr[k]);
        }
        
        return String.valueOf(cr);
    }

    /**
     * A static helper method that flips a character from 0 to 1 or vice versa.
     * @param c     The character to flip, 0 or 1 only.
     * @return k    The flipped character representation of the c parameter.
     */
    public static char flip(char c) {
        return (c == '0') ? '1' : '0';
    }    
}
