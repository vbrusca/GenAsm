package net.middlemind.GenAsm;

/**
 * A centralized logging class used to write text to standard output and error.
 * @author Victor G. Brusca, Middlemind Games 07-19-2021 6:56 PM EST
 */
public class Logger {
    /**
     * A static flag that controls this class' logging behavior.
     */
    public static boolean LOGGING_ON = true;
    
    /**
     * A static logging method that writes the provided text, followed by a new line, to standard output.
     * @param s     The specified text to write. 
     */
    public static void wrl(String s) {
        if(LOGGING_ON) {
            System.out.println(s);
        }
    }
    
    /**
     * A static logging method that writes the provided text to standard output.
     * @param s     The specified text to write.
     */
    public static void wr(String s) {
        if(LOGGING_ON) {
            System.out.print(s);
        }
    }    
    
    /**
     * A static logging method that writes the provided text, followed by a new line, to standard error.
     * @param s     The specified text to write.
     */
    public static void wrlErr(String s) {
        if(LOGGING_ON) {
            System.err.println(s);
        }
    }    
}
