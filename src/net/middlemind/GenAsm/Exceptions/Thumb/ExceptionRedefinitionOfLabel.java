package net.middlemind.GenAsm.Exceptions.Thumb;

import net.middlemind.GenAsm.Exceptions.ExceptionBase;

/**
 * A specific implementation of the ExceptionBase class.
 * @author Victor G. Brusca, Middlemind Games 08/14/2021 2:41 PM EST
 */
public class ExceptionRedefinitionOfLabel extends ExceptionBase {
    /**
     * A simple constructor.
     * @param errorMEssage The error message associated with the specified exception.
     */
    public ExceptionRedefinitionOfLabel(String errorMEssage) {
        super(errorMEssage);
    }
}
