package net.middlemind.GenAsm.JsonObjs;

import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionJsonObjLink;
import net.middlemind.GenAsm.Logger;

/**
 * A simple class that implements the JsonObj interface.
 * A class that represents a JSON object.
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 8:27 AM EST
 */
public class JsonObjBase implements JsonObj {
    /**
     * A string representing the name of this JSON object.
     */
    public String name;
    
    /**
     * A string representing the name of the file this JSON object was loaded from.
     */
    public String fileName;
    
    /**
     * A string representing the full class name of this JSON object's loader.
     */
    public String loader;
    
    /**
     * A method that returns this class' JSON object loader.
     * @return      A string representing the full class name of this JSON object's loader.
     */
    @Override
    public String GetLoader() {
        return loader;
    }
    
    /**
     * A method that sets the class' JSON object loader.
     * @param s     A string representing the full class name of this JSON object's loader.
     */
    @Override
    public void SetLoader(String s) {
        loader = s;
    }    
    
    /**
     * A method that returns the name of this JSON object class.
     * @return      A string representing the name of this JSON object. 
     */
    @Override
    public String GetName() {
        return name;
    }

    /**
     * A method that sets the name of the JSON object class.
     * @param s     A string representing the name of this JSON object. 
     */
    @Override
    public void SetName(String s) {
        name = s;
    }    
    
    /**
     * A method that returns the file name of the file this JSON object was loaded from.
     * @return      A string representing the name of the file this JSON object was loaded from.    
     */
    @Override
    public String GetFileName() {
        return fileName;
    }
    
    /**
     * A method that sets the file name of the file this JSON object was loaded from.
     * @param s     A string representing the name of the file this JSON object was loaded from.
     */
    @Override
    public void SetFileName(String s) {
        fileName = s;
    }    

    /**
     * A method that is used to print a string representation of this JSON object to standard output.
     */
    @Override
    public void Print() {
        Print("");
    }
    
    /**
     * A method that is used to print a string representation of this JSON object to standard output with a string prefix.
     * @param prefix    A string that is used as a prefix to the string representation of this JSON object.
     */
    @Override
    public void Print(String prefix) {
        Logger.wrl(prefix + "Name: " + name);
        Logger.wrl(prefix + "FileName: " + fileName);
        Logger.wrl(prefix + "Loader: " + loader);
    }    

    /**
     * A method used to link this JSON object with another loaded JSON object.
     * @param linkData              A JsonObj instance used as the link data to connect two JSON objects.
     * @throws ExceptionJsonObjLink An exception is thrown if there is an error finding the JSON object link.
     */
    @Override
    public void Link(JsonObj linkData) throws ExceptionJsonObjLink {
        Logger.wrl("JsonObjBase: Link: Do nothing...");
    }
}