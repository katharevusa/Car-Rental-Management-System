/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author admin
 */
public class NoOutletIsOpeningException extends Exception {

    /**
     * Creates a new instance of <code>NoOutletIsOpeningException</code> without
     * detail message.
     */
    public NoOutletIsOpeningException() {
    }

    /**
     * Constructs an instance of <code>NoOutletIsOpeningException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public NoOutletIsOpeningException(String msg) {
        super(msg);
    }
}
