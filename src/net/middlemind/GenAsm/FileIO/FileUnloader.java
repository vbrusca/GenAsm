package net.middlemind.GenAsm.FileIO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * A class that is used to write data to a file.
 * @author Victor G. Brusca, Middlemind Games 08/07/2021 11:47 AM EST
 */
@SuppressWarnings("ConvertToTryWithResources")
public class FileUnloader {
    /**
     * A static method that writes the specified string to the target destination file.
     * @param file          The file to write the data to.
     * @param str           The string to write to the file.
     * @throws IOException  An IO exception is thrown if there is a file error.
     */
    public static void WriteStr(String file, String str) throws IOException {
        File directory = new File(file);
        directory = new File(directory.getParent());
        if (! directory.exists()){
            directory.mkdirs();
        }
        
        BufferedWriter bf = Files.newBufferedWriter(Paths.get(file), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        bf.write(str);
        bf.flush();
        bf.close();
    }

    /**
     * A static method that writes the specified list of strings to the target destination file.
     * @param file          The file to write the data to.
     * @param strs          The list of strings to write to the file.
     * @throws IOException  An IO exception is thrown if there is a file error.
     */
    public static void WriteList(String file, List<String> strs) throws IOException {
        File directory = new File(file);
        directory = new File(directory.getParent());
        if (! directory.exists()){
            directory.mkdirs();
        }
        
        BufferedWriter bf = Files.newBufferedWriter(Paths.get(file), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        for(String s : strs) {
            bf.write(s + System.lineSeparator());
        }
        bf.flush();
        bf.close();
    }
    
    /**
     * A static method that writes the specified byte array to the target destination file.
     * @param file          The file to write the data to.
     * @param buff          The byte array to write to the file.
     * @throws IOException  An IO exception is thrown if there is a file error.
     */
    public static void WriteBuffer(String file, byte[] buff) throws IOException {
        File directory = new File(file);
        directory = new File(directory.getParent());
        if (! directory.exists()){
            directory.mkdirs();
        }
        
        Files.write(Paths.get(file), buff);
    }
}
