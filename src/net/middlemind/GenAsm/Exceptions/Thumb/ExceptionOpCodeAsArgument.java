package net.middlemind.GenAsm.Exceptions.Thumb;

import net.middlemind.GenAsm.Exceptions.ExceptionBase;

/**
 * A specific implementation of the ExceptionBase class.
 * @author Victor G. Brusca, Middlemind Games 08/18/2021 11:20 AM EST
 */
public class ExceptionOpCodeAsArgument extends ExceptionBase {
    /**
     * A simple constructor.
     * @param errorMEssage The error message associated with the specified exception.
     */
    public ExceptionOpCodeAsArgument(String errorMEssage) {
        super(errorMEssage);
    }
}
