package net.middlemind.GenAsm.PreProcessors;

import java.util.List;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 10-06-2021 9:18 AM EST
 */
public interface PreProcessor {
    public List<String> RunPreProcessor(String assemblySourceFile, String outputDir, Object other) throws Exception;
}
