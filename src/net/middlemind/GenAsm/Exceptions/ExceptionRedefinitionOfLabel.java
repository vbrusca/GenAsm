package net.middlemind.GenAsm.Exceptions;

/**
 *
 * @author Victor G. Brusca, Middlemind Games 08/14/2021 2:41 PM EST
 */
public class ExceptionRedefinitionOfLabel extends Exception {
    public ExceptionRedefinitionOfLabel(String errorMEssage) {
        super(errorMEssage);
    }
}
