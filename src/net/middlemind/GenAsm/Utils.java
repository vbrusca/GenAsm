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
}
