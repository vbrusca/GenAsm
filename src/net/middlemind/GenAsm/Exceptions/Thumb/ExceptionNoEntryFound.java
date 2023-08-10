package net.middlemind.GenAsm.Exceptions.Thumb;

import net.middlemind.GenAsm.Exceptions.ExceptionBase;

/**
 * A specific implementation of the ExceptionBase class.
 * @author Victor G. Brusca, Middlemind Games 08/09/2021 5:54 AM EST
 */
public class ExceptionNoEntryFound extends ExceptionBase {
    /**
     * A simple constructor.
     * @param errorMEssage The error message associated with the specified exception.
     */
    public ExceptionNoEntryFound(String errorMEssage) {
        super(errorMEssage);
    }
}
