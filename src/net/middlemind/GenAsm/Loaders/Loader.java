package net.middlemind.GenAsm.Loaders;

import net.middlemind.GenAsm.Exceptions.ExceptionLoader;
import net.middlemind.GenAsm.JsonObjs.JsonObj;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 07/27/2021 6:02 PM EST
 */
public interface Loader {
    public JsonObj ParseJson(String json, String targetClass, String fileName) throws ExceptionLoader;
}
