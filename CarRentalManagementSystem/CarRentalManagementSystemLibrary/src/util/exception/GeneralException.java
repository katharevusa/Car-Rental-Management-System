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
public class GeneralException extends Exception {

    /**
     * Creates a new instance of <code>GeneralException</code> without detail
     * message.
     */
    public GeneralException() {
    }

    /**
     * Constructs an instance of <code>GeneralException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public GeneralException(String msg) {
        super(msg);
    }
}
