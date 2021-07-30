package net.middlemind.GenAsm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07-19-2021 6:44 PM EST
 */
public class FileLoader {
    public static List<String> Load(String file) throws IOException {
        return Files.readAllLines(Paths.get(file));    
    }
    
    public static String LoadStr(String file) throws IOException {
        return Files.readString(Paths.get(file));
    }
}
