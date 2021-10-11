package net.middlemind.GenAsm.Assemblers;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 09/20/2021 11:50AM EST
 */
public class TwosCompliment {
    public static String GetTwosCompliment(String str) {
        int n = str.length();
      
        // Traverse the string to get first '1' from
        // the last of string
        int i;
        for (i = n - 1; i >= 0; i--) {
            if (str.charAt(i) == '1') {
                break;
            }
        }
      
        // If there exists no '1' concat 1 at the
        // starting of string
        if (i == -1) {
            return "1" + str;
        }
      
        // Continue traversal after the position of
        // first '1'
        char[] cr = str.toCharArray();
        for (int k = i - 1; k >= 0; k--)
        {
            cr[k] = flip(cr[k]);
        }
        
        return String.valueOf(cr);
    }

    private static char flip(char c) {
        return (c == '0') ? '1' : '0';
    }    
}
