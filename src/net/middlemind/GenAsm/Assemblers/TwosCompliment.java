package net.middlemind.GenAsm.Assemblers;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 09/20/2021 11:50AM EST
 */
public class TwosCompliment {
    //Solution by Sarbjit Singh on forum, https://stackoverflow.com/questions/42595963/how-to-get-2s-complement-of-a-binary-number-in-java-programmatically
    public static String GetTwosCompliment(String bin) {
        String twos = "", ones = "";

        for (int i = 0; i < bin.length(); i++) {
            ones += flip(bin.charAt(i));
        }
        
        int number0 = Integer.parseInt(ones, 2);
        StringBuilder builder = new StringBuilder(ones);
        boolean b = false;
        
        for (int i = ones.length() - 1; i > 0; i--) {
            if (ones.charAt(i) == '1') {
                builder.setCharAt(i, '0');
            } else {
                builder.setCharAt(i, '1');
                b = true;
                break;
            }
        }
        
        if (!b) {
            builder.append("1", 0, 7);
        }
        
        twos = builder.toString();
        return twos;
    }

    private static char flip(char c) {
        return (c == '0') ? '1' : '0';
    }    
}
