package net.middlemind.GenAsm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.List;
import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionMalformedRange;
import net.middlemind.GenAsm.FileIO.FileUnloader;
import net.middlemind.GenAsm.JsonObjs.JsonObjLineHexRep;
import net.middlemind.GenAsm.JsonObjs.JsonObjLineHexReps;
import net.middlemind.GenAsm.JsonObjs.JsonObjTxtMatch;

/**
 * A centralized utility class that provides access to a number of helpful static methods.
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 7:04 AM EST
 */
@SuppressWarnings({"null", "UnusedAssignment"})
public class Utils {
    
    public static boolean CheckAssemblerTestProgramAgainstAnswers(JsonObjLineHexReps hexDataLines, Hashtable<String, String> hashMap) {
        for(String key : hashMap.keySet()) {
            String val = hashMap.get(key);
            key = key.toUpperCase();
            val = val.toUpperCase();
            boolean found = false;
            for(JsonObjLineHexRep hexLine : hexDataLines.line_hex_reps) {
                String aKey = hexLine.addressHex.toUpperCase();
                String aVal = hexLine.valueHex.toUpperCase();
                if(aKey.equals(key) && aVal.equals(val)) {
                    found = true;
                    break;
                }
            }
            
            if(!found) {
                Logger.wrl("Could not find a match for key: " + key + " with value: " + val);
                return false;
            }
        }
        return true;
    }
    
    /**
     * A static method used to write the specified object, in JSON format, to the specified file.
     * @param obj           The object to be converted and written in JSON format.
     * @param name          The name of the object that's being written in JSON format.
     * @param fileName      The file name of the destination output file.
     * @param rootOutputDir The root directory of the destination output file.
     * @throws IOException  Throws an IOException during file IO.
     */
    public static void WriteObject(Object obj, String name, String fileName, String rootOutputDir) throws IOException {
        Logger.wrl("AssemblerThumb: WriteObject: Name: " + name);
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();            
        String jsonString = gson.toJson(obj);
        FileUnloader.WriteStr(Paths.get(rootOutputDir, fileName).toString(), jsonString);
    }
    
    /**
     * A static method used to write the specified object, in JSON format, to standard output.
     * @param obj       The object to be converted and written in JSON format.
     * @param name      The name of the object that's being written in JSON format.
     */
    public static void PrintObject(Object obj, String name) {
        Logger.wrl("AssemblerThumb: PrintObject: Name: '" + name + "'");
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();            
        String jsonString = gson.toJson(obj);
        Logger.wr(jsonString);        
    }    
    
    /**
     * A static method used to parse a string of text and convert it to binary, decimal, or hex based on the string's prefix.
     * @param s     The string to convert to an Integer representation of the proper radix.
     * @return      An Integer representation of the provided text in the proper radix.
     */
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
    
    /**
     * A static method used to flip the provided binary number, in string format, from little to big endian and vice versa.
     * @param binStr    A string representation of a binary number.
     * @return          An endian flipped version of the originally provided binary number in string form.
     */
    public static String EndianFlipBin(String binStr) {
        binStr = CleanBinPrefix(binStr);
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
    
    /**
     * A static method used to flip the provided hex number, in string format, from little to big endian and vice versa.
     * @param hexStr    A string representation of a hex number.    
     * @return          An endian flipped version of the originally provided hex number in string form.
     */
    public static String EndianFlipHex(String hexStr) {
        hexStr = CleanHexPrefix(hexStr);
        int len = hexStr.length();
        String[] bytes = new String[len / 2];
        int currentByteIdx = 0;
        String currentByte = "";
        
        for(int i = 0; i < len; i++) {
            currentByte += hexStr.charAt(i);
            if(currentByte.length() == 2) {
                bytes[currentByteIdx] = currentByte;
                currentByteIdx++;
                currentByte = "";
            }
        }
        
        String res = "";
        for(int j = bytes.length - 1; j >= 0; j--) {
            res += bytes[j];
        }
        return "0x" + res;
    }    
    
    /**
     * A static method used to pad string representations of binary numbers with zeros on either the left or right by a specified amount.
     * @param binStr        A string representation of a binary number.
     * @param shiftCount    The number of positions to pad.
     * @param shiftRight    Apply the padding on the right or left.
     * @param padZeros      Apply the padding using zeros or ones.
     * @return              An adjusted string representation with the applied padding.
     */
    public static String ShiftBinStr(String binStr, int shiftCount, boolean shiftRight, boolean padZeros) {
        String binStr2 = binStr;
        String p1 = null;
        for(int i = 0; i < shiftCount; i++) {
            p1 = null;
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
    
    /**
     * A static method that is used to loop over the provided array of string values to find the specified target value.
     * @param source        An array of strings to search for a specified target value.
     * @param target        The target value to search the specified array of string for.
     * @return              A Boolean value indicating if the specified target string was found.
     */
    public static boolean ArrayContainsString(String[] source, String target) {
        for(int i = 0; i < source.length; i++) {
            if(source[i].equals(target)) {
                return true;
            }
        }
        return false;
    }

    /**
     * A static method that is used to loop over the provided array of string values to find the first array entry that is contained by the provided source string.
     * @param target        An array of string to search if any entries contain the sub-string provided, source.
     * @param source        A sub-string to search for in each of the entries provided by the array of target string, target.
     * @return              An array of integers with the first integer representing the array index of the match and the second integer representing the character position of the match.
     */
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
    
    /**
     * A static method used to find an occurrence of target in the specified integer array, source.
     * @param source        The array of integers to search for a match.
     * @param target        The int value to find a match for.
     * @return              A Boolean value indicating if the specified target integer was found in the specified array of integers.
     */
    public static boolean ArrayContainsInt(int[] source, int target) {
        for(int i = 0; i < source.length; i++) {
            if(source[i] == target) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * A static method used to clean supported hex prefixes from a string representation of a hex number.
     * @param hexStr    The string representation of a hex number to remove the prefix from.
     * @return          The provided hex number without the hex number prefix.
     */
    public static String CleanHexPrefix(String hexStr) {
        return hexStr.replace("#0x", "").replace("0x", "").replace("#0X", "").replace("0X", "").replace("&", "");
    }
    
    /**
     * A static method used to clean supported bin prefixes from a string representation of a binary number.
     * @param binStr    The string representation of a binary number to remove the prefix from.
     * @return          The provided binary number without the binary number prefix.
     */
    public static String CleanBinPrefix(String binStr) {
        return binStr.replace("#0b", "").replace("0b", "").replace("#0B", "").replace("0B", "");
    }    
    
    /**
     * A static method for formatting string representations of hex numbers to the specified length using left padding.
     * @param s     The string representation of a hex number to format.
     * @param len   The specified length to format the number to.
     * @return      The formatted hex string provided, prefixed with hex number code '0x'.
     */
    public static String FormatHexString(String s, int len) {
        return FormatHexString(s, len, true);
    }    
    
    /**
     * A static method for formatting string representations of hex numbers to the specified length using the specified padding.
     * @param s         The string representation of a hex number to format.
     * @param len       The specified length to format the number to.
     * @param padLeft   A Boolean argument indicating if padding should be applied to the left or right side of the hex string. 
     * @return          The formatted hex string provided, prefixed with hex number code '0x'. 
     */
    public static String FormatHexString(String s, int len, boolean padLeft) {
        String ret = "";
        if(!IsStringEmpty(s)) {
            s = CleanHexPrefix(s);
            ret = s;
            if(s.length() < len) {
                for(int i = s.length(); i < len; i++) {
                    if(padLeft == true) {
                        ret = "0" + ret;
                    } else {
                        ret += "0";
                    }
                }
            }
            ret = ret.toUpperCase();
            ret = "0x" + ret;            
        }
        return ret;
    }
    
    /**
     * A static method for formatting string representations of binary numbers to the specified length using left padding of zeros.
     * @param s     The string representation of a binary number to format.
     * @param len   The specified length to format the number to.
     * @return      The formatted binary string provided, prefixed with NO binary number code.
     */
    public static String FormatBinString(String s, int len) {    
        return FormatBinString(s, len, true);
    }
        
    /**
     * A static method for formatting string representations of binary numbers to the specified length using padding of zeros.
     * @param s         The string representation of a binary number to format.
     * @param len       The specified length to format the number to.
     * @param padLeft   A Boolean value indicating if the padding should be applied on the left or right.
     * @return          The formatted binary string provided, prefixed with NO binary number code. 
     */
    public static String FormatBinString(String s, int len, boolean padLeft) {
        return FormatBinString(s, len, true, "0");
    }
    
    /**
     * A static method for formatting string representations of binary numbers to the specified length using left/right padding of the specified character.
     * @param s         The string representation of a binary number to format.
     * @param len       The specified length to format the number to.
     * @param padLeft   A Boolean value indicating if the padding should be applied on the left or right.
     * @param padWith   The character used to pad the binary string.
     * @return          The formatted binary string provided, prefixed with NO binary number code.
     */
    public static String FormatBinString(String s, int len, boolean padLeft, String padWith) {
        String ret = null;
        if(!IsStringEmpty(s)) {
            s = CleanBinPrefix(s);
            ret = s;
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
    
    /**
     * A static method that converts the provided binary string to a hex string.
     * @param binStr    The binary string to convert to a hex string.
     * @return          A hex string representing the same value, in hex, as the provided binary string. No prefix code added.
     */
    public static String Bin2Hex(String binStr) {
        String s = binStr;
        s = CleanBinPrefix(s);
        s = "#0b" + s;
        Integer v = Utils.ParseNumberString(s);
        return Integer.toHexString(v);
    }
    
    /**
     * A static method that converts the provided hex string to a binary string.
     * @param hexStr    The hex string to convert to a binary string.
     * @return          A binary string representing the same value, in binary, as the provided binary string. No prefix code added.
     */    
    public static String Hex2Bin(String hexStr) {
        String s = hexStr;
        s = CleanHexPrefix(s);
        s = "#0x" + s;
        Integer v = Utils.ParseNumberString(s);
        return Integer.toBinaryString(v);
    }    
    
    /**
     * A static method used to format and clean up a string representation of a binary number.
     * @param binStr    A string representation of a binary number to format cleanly.
     * @param len       An integer representing the total length of the formatted binary string.
     * @param padLeft   A Boolean value indicating if the binary string should be padded on the left or right.
     * @return          A formatted, pretty, version of the specified binary string.
     */
    public static String PrettyBin(String binStr, int len, boolean padLeft) {
        binStr = CleanBinPrefix(binStr);
        String s = FormatBinString(binStr, len, true);
        String ret = "";
        int l = s.length();
        String tmp = "";
        for(int i = 0; i < l; i++) {
            tmp += s.charAt(i);
            if(i > 0 && (i + 1) % 8 == 0) {
                ret += tmp;                
                tmp = "";
                if(i < l - 1) {
                    ret += " ";
                }
            }
        }
        return ret;        
    }    
    
    /**
     * A static method used to format and clean up a string representation of a hex number.
     * @param hexStr    A string representation of a hex number to format cleanly.
     * @param len       An integer representing the total length of the formatted hex string.
     * @param padLeft   A Boolean value indicating if the binary string should be padded on the left or right.
     * @return          A formatted, pretty, version of the specified hex string.
     */
    public static String PrettyHex(String hexStr, int len, boolean padLeft) {
        hexStr = CleanHexPrefix(hexStr);
        String s = FormatHexString(hexStr, len, padLeft);
        s = CleanHexPrefix(s);
        String ret = "";
        int l = s.length();
        String tmp = "";
        for(int i = 0; i < l; i++) {
            tmp += s.charAt(i);
            if(i > 0 && i % 2 == 1) {
                ret += tmp;
                tmp = "";
                if(i < l - 1) {
                    ret += " ";
                }                
            }
        }
        return "0x" + ret;
    }
    
    /**
     * A static method used to size and space a provided string.
     * @param s         A string provided for formatting.
     * @param len       The desired length of the formatted string.
     * @param padLeft   A Boolean value indicating if the provided string should be padded on the left or the right.
     * @return          A string representing the final formatted version of the provided string, s.
     */
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
    
    /**
     * A static method used to check if a string is empty or not.
     * @param s     The string to check.
     * @return      A Boolean value indicating if the provided string is empty or not.
     */
    public static boolean IsStringEmpty(String s) {
        if(s == null || s.equals("")) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * A static method used to check if a List if empty or not.
     * @param l     The List to check.
     * @return      A Boolean value indicating if the provided string is empty or not.
     */
    public static boolean IsListEmpty(List l) {
        if(l == null) {
            return true;
        } else if(l != null && l.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * A static method used to extract min and max integer values from a string representation of the range, ie 0-9, using the default deliminator. 
     * @param range                     A string representation of the numeric range delineated by the default range delineation string.
     * @return                          An array of integers ordered range min, range max.
     * @throws ExceptionMalformedRange  An exception thrown during the separation of the specified integer range. 
     */
    public static int[] GetIntsFromRange(String range) throws ExceptionMalformedRange {
        return GetIntsFromRange(range, JsonObjTxtMatch.special_range);
    }
    
    /**
     * A static method used to extract min and max integer values from a string representation of the range, ie 0-9;
     * @param range                     A string representation of the numeric range delineated by the rangeDelim string.
     * @param rangeDelim                A string that is used to separate the two integer values represented in the range string.
     * @return                          An array of integers ordered range min, range max.
     * @throws ExceptionMalformedRange  An exception thrown during the separation of the specified integer range.
     */
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
    
    /**
     * A static method used to convert the provided char to an int.
     * @param c                         The char to convert to an int.
     * @return                          An integer representation of the provided char.
     * @throws ExceptionMalformedRange  An exception thrown during the char to int conversion process.
     */
    public static int GetIntFromChar(char c) throws ExceptionMalformedRange {
        int ret = 0;
        try {
            ret = Integer.parseInt(c + "");
        } catch(NumberFormatException e) {
            throw new ExceptionMalformedRange("The character provided could not be converted to a digit, " + c);
        }
        return ret;
    }

    /**
     * A static method used to convert the provided string to an int.
     * @param c                         The string to convert to an int.
     * @return                          An integer representation of the provided string.
     * @throws ExceptionMalformedRange  An exception thrown during the string to int conversion process.
     */
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
