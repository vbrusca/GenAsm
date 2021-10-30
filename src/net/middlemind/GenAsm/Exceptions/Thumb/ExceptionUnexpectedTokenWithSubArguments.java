package net.middlemind.GenAsm.Exceptions.Thumb;

import net.middlemind.GenAsm.Exceptions.ExceptionBase;

/**
 * A specific implementation of the ExceptionBase class.
 * @author Victor G. Brusca, Middlemind Games 08/18/2021 1:34 PM EST
 */
public class ExceptionUnexpectedTokenWithSubArguments extends ExceptionBase {
    /**
     * A simple constructor.
     * @param errorMEssage The error message associated with the specified exception.
     */
    public ExceptionUnexpectedTokenWithSubArguments(String errorMEssage) {
        super(errorMEssage);
    }
}
