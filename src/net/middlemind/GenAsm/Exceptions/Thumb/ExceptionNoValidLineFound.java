package net.middlemind.GenAsm.Exceptions.Thumb;

import net.middlemind.GenAsm.Exceptions.ExceptionBase;

/**
 * A specific implementation of the ExceptionBase class.
 * @author Victor G. Brusca, Middlemind Games 08/13/2021 6:52 PM EST
 */
public class ExceptionNoValidLineFound extends ExceptionBase {
    /**
     * A simple constructor.
     * @param errorMEssage The error message associated with the specified exception.
     */
    public ExceptionNoValidLineFound(String errorMEssage) {
        super(errorMEssage);
    }
}
