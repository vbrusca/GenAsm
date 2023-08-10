package net.middlemind.GenAsm.Exceptions.Thumb;

import net.middlemind.GenAsm.Exceptions.ExceptionBase;

/**
 * A specific implementation of the ExceptionBase class.
 * @author Victor G. Brusca, Middlemind Games 08/12/2021 6:48 PM EST
 */
public class ExceptionNoParentSymbolFound extends ExceptionBase {
    /**
     * A simple constructor.
     * @param errorMEssage The error message associated with the specified exception.
     */
    public ExceptionNoParentSymbolFound(String errorMEssage) {
        super(errorMEssage);
    }
}
