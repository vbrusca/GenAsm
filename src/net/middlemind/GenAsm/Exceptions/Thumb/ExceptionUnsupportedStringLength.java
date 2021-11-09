package net.middlemind.GenAsm.Exceptions.Thumb;

import net.middlemind.GenAsm.Exceptions.ExceptionBase;

/**
 * A specific implementation of the ExceptionBase class.
 * @author Victor G. Brusca, Middlemind Games 11/09/2021 8:56 PM EST
 */
public class ExceptionUnsupportedStringLength extends ExceptionBase {
    /**
     * A simple constructor.
     * @param errorMEssage The error message associated with the specified exception.
     */
    public ExceptionUnsupportedStringLength(String errorMEssage) {
        super(errorMEssage);
    }
}
