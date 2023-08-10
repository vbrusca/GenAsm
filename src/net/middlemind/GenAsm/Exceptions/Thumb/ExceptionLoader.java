package net.middlemind.GenAsm.Exceptions.Thumb;

import net.middlemind.GenAsm.Exceptions.ExceptionBase;

/**
 * A specific implementation of the ExceptionBase class.
 * @author Victor G. Brusca, Middlemind Games 07/30/2021 6:52 AM EST
 */
public class ExceptionLoader extends ExceptionBase {
    /**
     * A simple constructor.
     * @param errorMEssage The error message associated with the specified exception.
     */    
    public ExceptionLoader(String errorMEssage) {
        super(errorMEssage);
    }
}
