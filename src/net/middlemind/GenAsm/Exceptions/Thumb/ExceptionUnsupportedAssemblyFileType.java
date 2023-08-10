package net.middlemind.GenAsm.Exceptions.Thumb;

import net.middlemind.GenAsm.Exceptions.ExceptionBase;

/**
 * A specific implementation of the ExceptionBase class.
 * @author Victor G. Brusca, Middlemind Games 10/06/2021 11:21 AM EST
 */
public class ExceptionUnsupportedAssemblyFileType extends ExceptionBase {
    /**
     * A simple constructor.
     * @param errorMEssage The error message associated with the specified exception.
     */
    public ExceptionUnsupportedAssemblyFileType(String errorMEssage) {
        super(errorMEssage);
    }
}
