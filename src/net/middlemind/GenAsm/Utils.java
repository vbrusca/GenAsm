package net.middlemind.GenAsm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionMalformedRange;
import net.middlemind.GenAsm.FileIO.FileUnloader;
import net.middlemind.GenAsm.JsonObjs.JsonObjTxtMatch;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 7:04 AM EST
 */
@SuppressWarnings({"null", "UnusedAssignment"})
public class Utils {
    public static void WriteObject(Object obj, String name, String fileName, String rootOutputDir) throws IOException {
        Logger.wrl("AssemblerThumb: WriteObject: Name: " + name);
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();            
        String jsonString = gson.toJson(obj);
        FileUnloader.WriteStr(Paths.get(rootOutputDir, fileName).toString(), jsonString);
    }
    
    public static void PrintObject(Object obj, String name) {
        Logger.wrl("AssemblerThumb: PrintObject: Name: '" + name + "'");
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();            
        String jsonString = gson.toJson(obj);
        Logger.wr(jsonString);        
    }    
    
    public static Integer ParseNumberString(String s) {
        Integer tInt = null;
        s = s.trim();
        if(s.contains("#0x")) {            
            tInt = Integer.parseInt(s.replace("#0x", ""), 16);                            
        } else if(s.contains("0x")) {
            tInt = Integer.parseInt(s.replace("0x", ""), 16);
        } else if(s.contains("&")) {
            tInt = Integer.parseInt(s.replace("&", ""), 16);
        } else if(s.contains("#0b")) {
            tInt = Integer.parseInt(s.replace("#0b", ""), 2);                            
        } else if(s.contains("#")) {
            tInt = Integer.parseInt(s.replace("#", ""), 10);
        } else {
            tInt = Integer.parseInt(s, 10);
        }
        return tInt;
    }    
    
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
        return FormatHexString(s, len, true);
    }
    
    public static String FormatHexString(String s, int len, boolean padLeft) {
        String ret = s;
        if(!IsStringEmpty(s)) {
            if(s.length() < len) {
                for(int i = s.length(); i < len; i++) {
                    if(padLeft == true) {
                        ret = "0" + ret;
                    } else {
                        ret += "0";
                    }
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

    public static String PrettyBin(String binStr, int len, boolean padLeft) {
        String s = Utils.SpaceString(binStr, len, true);
        String ret = "";
        int l = s.length();
        String tmp = "";
        for(int i = 0; i < l; i++) {
            tmp += s.charAt(i);
            if(i > 0 && i % 8 == 0) {
                ret += tmp;                
                tmp = "";
                if(i < l - 1) {
                    ret += " ";
                }
            }
        }
        return ret;        
    }
    
    public static String Bin2Hex(String binStr) {
        String s = binStr;
        if(!s.contains("0b")) {
            s = "#0b" + s;
        }
        Integer v = Utils.ParseNumberString(s);
        return Integer.toHexString(v);
    }
    
    public static String PrettyHex(String hexStr, int len, boolean padLeft) {
        String s = FormatHexString(hexStr, len, padLeft);
        String ret = "";
        int l = s.length();
        String tmp = "";
        for(int i = 0; i < l; i++) {
            tmp += s.charAt(i);
            if(i > 0 && i % 2 == 0) {
                ret += tmp;
                tmp = "";
                if(i < l - 1) {
                    ret += " ";
                }                
            }
        }
        return ret;
    }
    
    public static String SpaceString(String s, int len, boolean padLeft) {
        String ret = s;
        if(!IsStringEmpty(s)) {
            if(s.length() < len) {
                for(int i = s.length(); i < len; i++) {
                    if(padLeft) {
                        ret = " " + ret;
                    } else {
                        ret += " ";
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
