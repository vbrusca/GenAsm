package net.middlemind.GenAsm.Exceptions.Thumb;

import net.middlemind.GenAsm.Exceptions.ExceptionBase;

/**
 * A specific implementation of the ExceptionBase class.
 * @author Victor G. Brusca, Middlemind Games 08/19/2021 12:35 PM EST
 */
public class ExceptionInvalidAssemblyLine extends ExceptionBase {
    /**
     * A simple constructor.
     * @param errorMEssage The error message associated with the specified exception.
     */
    public ExceptionInvalidAssemblyLine(String errorMEssage) {
        super(errorMEssage);
    }
}
 