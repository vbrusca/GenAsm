package net.middlemind.GenAsm;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07-19-2021 6:56 PM EST
 */
public class Logger {
    public static boolean LOGGING_ON = true;
    
    public static void wrl(String s) {
        if(LOGGING_ON) {
            System.out.println(s);
        }
    }
    
    public static void wr(String s) {
        if(LOGGING_ON) {
            System.out.print(s);
        }
    }    
    
    public static void wrlErr(String s) {
        if(LOGGING_ON) {
            System.err.println(s);
        }
    }    
}
