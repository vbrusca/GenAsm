package net.middlemind.GenAsm.Loaders;

import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionLoader;
import net.middlemind.GenAsm.JsonObjs.JsonObj;

/**
 * An interface used to define the basic loader class implementation.
 * @author Victor G. Brusca, Middlemind Games 07/27/2021 6:02 PM EST
 */
public interface Loader {
    /**
     * A method used to parse and load JSON data files.
     * @param json              The contents of the JSON file to load.
     * @param targetClass       A full java class representation of the Java class to load the JSON data into.
     * @param fileName          The full path to the JSON data file to load.
     * @return                  A JsonObj instance the represents the JSON data loaded.
     * @throws ExceptionLoader  An exception is thrown if there is an issue during the JSON data load.
     */
    public JsonObj ParseJson(String json, String targetClass, String fileName) throws ExceptionLoader;
}