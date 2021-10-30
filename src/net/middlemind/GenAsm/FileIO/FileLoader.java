package net.middlemind.GenAsm.FileIO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * A class that is used to load a file's contents into different data structures.
 * @author Victor G. Brusca, Middlemind Games 07/19/2021 6:44 PM EST
 */
public class FileLoader {
    /**
     * A static method that is used to load a file's contents into a list of strings.
     * @param file          The target file to load.
     * @return              A list of strings representing the contents of the file.
     * @throws IOException  An IO exception is thrown if there is a file error.
     */
    public static List<String> Load(String file) throws IOException {
        return Files.readAllLines(Paths.get(file));    
    }
    
    /**
     * A static method that is used to load a file's contents into a string.
     * @param file          The target file to load.
     * @return              A string representing the contents of the file.
     * @throws IOException  An IO exception is thrown if there is a file error.
     */
    public static String LoadStr(String file) throws IOException {
        return Files.readString(Paths.get(file));
    }
    
    /**
     * A static method that is used to load a file's contents into an array of bytes.
     * @param file          The target file to load.
     * @return              A byte array representing the contents of the file.
     * @throws IOException  An IO exception is thrown if there is a file error.
     */
    public static byte[] LoadBin(String file) throws IOException {
        return Files.readAllBytes(Paths.get(file));
    }
}
