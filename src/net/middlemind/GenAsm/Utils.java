package net.middlemind.GenAsm;

import net.middlemind.GenAsm.Exceptions.ExceptionMalformedRange;
import net.middlemind.GenAsm.JsonObjs.JsonObjTxtMatch;

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
        
    public static int[] GetIntsFromRange(String range) throws ExceptionMalformedRange {
        return GetIntsFromRange(range, JsonObjTxtMatch.special_range);
    }
    
    public static int[] GetIntsFromRange(String range, String rangeDelim) throws ExceptionMalformedRange {
        int[] ret = new int[2];        
        String[] strInts = range.split(rangeDelim);
        if(strInts.length == 2) {
            try {
                ret[0] = Integer.parseInt(strInts[0]);
                ret[1] = Integer.parseInt(strInts[1]);                
            } catch(NumberFormatException e) {
                throw new ExceptionMalformedRange("The range string provided is not properly formed, " + range + ", with delimiter '" + rangeDelim + "'");
            }
            
            if(ret[0] > ret[1]) {
                throw new ExceptionMalformedRange("The range string provided is not properly formed, " + range + ", with delimiter '" + rangeDelim + "'");
            }
        } else {
            throw new ExceptionMalformedRange("The range string provided is not properly formed, " + range + ", with delimiter '" + rangeDelim + "'");
        }
        return ret;
    }
    
    public static int GetIntFromChar(char c) throws ExceptionMalformedRange {
        int ret = 0;
        try {
            ret = Integer.parseInt(c + "");
        } catch(NumberFormatException e) {
            throw new ExceptionMalformedRange("The character provided could not be converted to a digit, " + c);
        }
        return ret;
    }    
}
