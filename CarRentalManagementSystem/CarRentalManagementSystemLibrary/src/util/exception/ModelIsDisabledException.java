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
public class ModelIsDisabledException extends Exception {

    /**
     * Creates a new instance of <code>ModelIsDisabledException</code> without
     * detail message.
     */
    public ModelIsDisabledException() {
    }

    /**
     * Constructs an instance of <code>ModelIsDisabledException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ModelIsDisabledException(String msg) {
        super(msg);
    }
}
