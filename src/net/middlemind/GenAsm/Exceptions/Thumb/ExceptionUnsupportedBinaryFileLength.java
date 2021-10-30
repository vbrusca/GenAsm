package net.middlemind.GenAsm.Exceptions.Thumb;

import net.middlemind.GenAsm.Exceptions.ExceptionBase;

/**
 * A specific implementation of the ExceptionBase class.
 * @author Victor G. Brusca, Middlemind Games 10/06/2021 2:16 PM EST
 */
public class ExceptionUnsupportedBinaryFileLength extends ExceptionBase {
    /**
     * A simple constructor.
     * @param errorMEssage The error message associated with the specified exception.
     */
    public ExceptionUnsupportedBinaryFileLength(String errorMEssage) {
        super(errorMEssage);
    }
}
