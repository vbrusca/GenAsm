package net.middlemind.GenAsm;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/07/2021 11:47 AM EST
 */
public class FileUnloader {    
    @SuppressWarnings("ConvertToTryWithResources")
    public static void WriteStr(String file, String jsonString) throws IOException {
        BufferedWriter bf = Files.newBufferedWriter(Paths.get(file), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        bf.write(jsonString);
        bf.flush();
        bf.close();
    }    
}
