package net.middlemind.GenAsm;

import java.util.List;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionMalformedRange;
import net.middlemind.GenAsm.JsonObjs.JsonObjTxtMatch;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 7:04 AM EST
 */
@SuppressWarnings({"null", "UnusedAssignment"})
public class Utils {
    
    public static String EndianFlip(String binStr) {
        int len = binStr.length();
        String[] bytes = new String[len / 8];
        int currentByteIdx = 0;
        String currentByte = "";
        
        for(int i = 0; i < len; i++) {
            currentByte += binStr.charAt(i);
            if(currentByte.length() == 8) {
                bytes[currentByteIdx] = currentByte;
                currentByteIdx++;
                currentByte = "";
            }
        }
        
        String res = "";
        for(int j = bytes.length - 1; j >= 0; j--) {
            res += bytes[j];
        }
        return res;
    }
    
    public static String ShiftBinStr(String binStr, int shiftCount, boolean shiftRight, boolean padZeros) {
        String binStr2 = binStr;
        for(int i = 0; i < shiftCount; i++) {
            String p1 = "";
            if(shiftRight) {
                p1 = binStr2.substring(0, binStr2.length() - 1);
                if(padZeros) {
                    p1 = "0" + p1;
                } else {
                    p1 = "1" + p1;
                }
                binStr2 = p1;
            } else {
                p1 = binStr2.substring(1);
                if(padZeros) {
                    p1 += "0";
                } else {
                    p1 += "1";
                }
                binStr2 = p1;                
            }
        }
        return binStr2;
    }
    
    public static boolean ArrayContainsString(String[] source, String target) {
        for(int i = 0; i < source.length; i++) {
            if(source[i].equals(target)) {
                return true;
            }
        }
        return false;
    }

    public static int[] StringContainsArrayEntry(String[] target, String source) {
        int idx = -1;
        for(int i = 0; i < target.length; i++) {
            idx = source.indexOf(target[i]);
            if(idx != -1) {
                return new int[] { i, idx };
            }
        }
        return null;
    }    
    
    public static boolean ArrayContainsInt(int[] source, int target) {
        for(int i = 0; i < source.length; i++) {
            if(source[i] == target) {
                return true;
            }
        }
        return false;
    }
    
    public static String FormatHexString(String s, int len) {
        String ret = s;
        if(!IsStringEmpty(s)) {
            if(s.length() < len) {
                for(int i = s.length(); i < len; i++) {
                    ret = "0" + ret;
                }
            }
        }
        return ret = "0x" + ret;
    }
    
    public static String FormatBinString(String s, int len) {    
        return FormatBinString(s, len, true);
    }
        
    public static String FormatBinString(String s, int len, boolean padLeft) {
        String ret = s;
        if(!IsStringEmpty(s)) {
            if(s.length() < len) {
                for(int i = s.length(); i < len; i++) {
                    if(padLeft) {
                        ret = "0" + ret;
                    } else {
                        ret += "0";
                    }
                }
            } else if(s.length() > len) {
                if(padLeft) {
                    ret = ret.substring(s.length() - len);
                } else {
                    ret = ret.substring(0, (s.length() - len));
                }
            }
        }
        return ret;
    }    
    
    public static boolean IsStringEmpty(String s) {
        if(s == null || s.equals("")) {
            return true;
        } else {
            return false;
        }
    }
    
    public static boolean IsListEmpty(List l) {
        if(l == null) {
            return true;
        } else if(l != null && l.isEmpty()) {
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

    public static int GetIntFromString(String c) throws ExceptionMalformedRange {
        int ret = 0;
        try {
            ret = Integer.parseInt(c + "");
        } catch(NumberFormatException e) {
            throw new ExceptionMalformedRange("The character provided could not be converted to a digit, " + c);
        }
        return ret;
    }    
}
