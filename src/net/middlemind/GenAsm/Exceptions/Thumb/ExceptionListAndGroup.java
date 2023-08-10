package net.middlemind.GenAsm.Exceptions.Thumb;

import net.middlemind.GenAsm.Exceptions.ExceptionBase;

/**
 *A specific implementation of the ExceptionBase class.
 * @author Victor G. Brusca, Middlemind Games 08/09/2021 5:24 PM EST
 */
public class ExceptionListAndGroup extends ExceptionBase {
    /**
     * A simple constructor.
     * @param errorMEssage The error message associated with the specified exception.
     */
    public ExceptionListAndGroup(String errorMEssage) {
        super(errorMEssage);
    }    
}
