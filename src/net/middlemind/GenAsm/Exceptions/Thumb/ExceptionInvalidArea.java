package net.middlemind.GenAsm.Exceptions.Thumb;

import net.middlemind.GenAsm.Exceptions.ExceptionBase;

/**
 * A specific implementation of the ExceptionBase class.
 * @author Victor G. Brusca, Middlemind Games 08/19/2021 10:12 AM EST
 */
public class ExceptionInvalidArea extends ExceptionBase {
    /**
     * A simple constructor.
     * @param errorMEssage The error message associated with the specified exception.
     */    
    public ExceptionInvalidArea(String errorMEssage) {
        super(errorMEssage);
    }
}