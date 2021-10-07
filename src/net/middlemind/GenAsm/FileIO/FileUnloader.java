package net.middlemind.GenAsm.FileIO;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/07/2021 11:47 AM EST
 */
@SuppressWarnings("ConvertToTryWithResources")
public class FileUnloader {        
    public static void WriteStr(String file, String str) throws IOException {
        BufferedWriter bf = Files.newBufferedWriter(Paths.get(file), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        bf.write(str);
        bf.flush();
        bf.close();
    }

    public static void WriteList(String file, List<String> strs) throws IOException {
        BufferedWriter bf = Files.newBufferedWriter(Paths.get(file), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        for(String s : strs) {
            bf.write(s + System.lineSeparator());
        }
        bf.flush();
        bf.close();
    }    
}
