package net.middlemind.GenAsm;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 7:04 AM EST
 */
public class Utils {
    public static boolean IsStringEmpty(String s) {
        if(s == null || s.equals("")) {
            return true;
        } else {
            return false;
        }
    }
    
    public static int[] GetIntsFromRange(String range) throws MalformedRangeException {
        int[] ret = new int[2];
        String[] strInts = range.split(JsonObjTxtMatch.special_range);
        if(strInts.length == 2) {
            try {
                ret[0] = Integer.parseInt(strInts[0]);
                ret[1] = Integer.parseInt(strInts[1]);                
            } catch(NumberFormatException e) {
                throw new MalformedRangeException("The range string provided is not properly formed, " + range);
            }
            
            if(ret[0] > ret[1]) {
                throw new MalformedRangeException("The range string provided is not properly formed, " + range);
            }
        } else {
            throw new MalformedRangeException("The range string provided is not properly formed, " + range);
        }
        
        return ret;
    }
    
    public static int GetIntFromChar(char c) throws MalformedRangeException {
        int ret = 0;
        try {
            ret = Integer.parseInt(c + "");
        } catch(NumberFormatException e) {
            throw new MalformedRangeException("The character provided could not be converted to a digit, " + c);
        }
        return ret;
    }    
}
