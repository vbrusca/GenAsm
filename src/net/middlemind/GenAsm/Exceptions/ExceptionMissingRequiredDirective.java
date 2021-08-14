package net.middlemind.GenAsm.Exceptions;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/14/2021 2:37 PM EST
 */
public class ExceptionMissingRequiredDirective extends Exception {
    public ExceptionMissingRequiredDirective(String errorMEssage) {
        super(errorMEssage);
    }
}
