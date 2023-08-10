package net.middlemind.GenAsm.JsonObjs;

import net.middlemind.GenAsm.Exceptions.Thumb.ExceptionJsonObjLink;

/**
 * An interface that represents a generic JSON object.
 * This class is used as the base of all Java classes that are used to represent JSON objects.
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 6:46 AM EST
 */
public interface JsonObj {
    /**
     * A method that returns the full class name of the loading class for this JSON object.
     * @return      The full class name of the class used to load this JSON object.
     */
    public String GetLoader();
    
    /**
     * A method that sets the full class name of the loading class for this JSON object.
     * @param s     The full class name of the class used to load this JSON object.
     */
    public void SetLoader(String s);
    
    /**
     * A method that returns the name of this JSON object.
     * @return      A string representing the name of this JSON object.
     */
    public String GetName();
    
    /**
     * A method that sets the name of this JSON object.
     * @param s     A string representing the name of this JSON object.
     */
    public void SetName(String s);
    
    /**
     * A method that returns the file name of the JSON file that contains the JSON object.
     * @return      A string representing the file name of that contains this JSON object.
     */
    public String GetFileName();
    
    /**
     * A method that sets the file name of the JSON file that contains the JSON object.
     * @param s     A string representing the file name of that contains this JSON object.
     */
    public void SetFileName(String s);
    
    /**
     * A method used to print a representation of this JSON object to standard output.
     */
    public void Print();
    
    /**
     * A method used to print a string representation of this JSON object to standard output with a prefix.
     * @param prefix    A prefix to print before the string representation of this JSON object.
     */
    public void Print(String prefix);
    
    /**
     * An optional method used to link this JSON object to other loaded JSON objects.
     * @param linkData              The JsonObj to use as the link data between this JSON object and another.
     * @throws ExceptionJsonObjLink An exception is thrown if there is an error finding a JSON object match.
     */
    public void Link(JsonObj linkData) throws ExceptionJsonObjLink;    
}
