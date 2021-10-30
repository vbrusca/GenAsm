package net.middlemind.GenAsm.PreProcessors;

import java.util.List;

/**
 * An interface used to define the basic pre-processor class implementation.
 * @author Victor G. Brusca, Middlemind Games 10-06-2021 9:18 AM EST
 */
public interface PreProcessor {
    /**
     * A method used to run the pre-processor on an assembly source file.
     * @param assemblySourceFile    The full path to the assembly source file to process.
     * @param outputDir             The output directory to use to write output files to.
     * @param other                 A generic Java class used to customize the pre-processor.
     * @return                      A list of string representing the pre-processed file.
     * @throws Exception            An exception is thrown if an error is encountered during the pre-processor run.
     */
    public List<String> RunPreProcessor(String assemblySourceFile, String outputDir, Object other) throws Exception;
}
