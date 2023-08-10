package net.middlemind.GenAsm.Exceptions.Thumb;

import net.middlemind.GenAsm.Exceptions.ExceptionBase;

/**
 * A specific implementation of the ExceptionBase class.
 * @author Victor G. Brusca, Middlemind Games 10/08/2021 09:02 AM EST
 */
public class ExceptionNoSymbolValueFound extends ExceptionBase {
    /**
     * A simple constructor.
     * @param errorMEssage The error message associated with the specified exception.
     */
    public ExceptionNoSymbolValueFound(String errorMEssage) {
        super(errorMEssage);
    }
}
