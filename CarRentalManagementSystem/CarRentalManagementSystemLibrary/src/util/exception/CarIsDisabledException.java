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
public class CarIsDisabledException extends Exception {

    /**
     * Creates a new instance of <code>CarIsDisabledException</code> without
     * detail message.
     */
    public CarIsDisabledException() {
    }

    /**
     * Constructs an instance of <code>CarIsDisabledException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CarIsDisabledException(String msg) {
        super(msg);
    }
}
