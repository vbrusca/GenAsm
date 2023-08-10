package net.middlemind.GenAsm.Exceptions.Thumb;

import net.middlemind.GenAsm.Exceptions.ExceptionBase;

/**
 * A specific implementation of the ExceptionBase class.
 * @author Victor G. Brusca, Middlemind Games 08/14/2021 2:21 PM EST
 */
public class ExceptionMalformedEntryEndDirectiveSet extends ExceptionBase {
    /**
     * A simple constructor.
     * @param errorMEssage The error message associated with the specified exception.
     */
    public ExceptionMalformedEntryEndDirectiveSet(String errorMEssage) {
        super(errorMEssage);
    }
}
