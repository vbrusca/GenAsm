package net.middlemind.GenAsm;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/27/2021 6:02 PM EST
 */
public interface Loader {
    public JsonObj ParseJson(String json, String targetClass, String fileName) throws LoaderException;
}
