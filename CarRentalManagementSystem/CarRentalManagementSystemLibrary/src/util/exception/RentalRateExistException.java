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
public class RentalRateExistException extends Exception {

    /**
     * Creates a new instance of <code>RentalRateExistException</code> without
     * detail message.
     */
    public RentalRateExistException() {
    }

    /**
     * Constructs an instance of <code>RentalRateExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RentalRateExistException(String msg) {
        super(msg);
    }
}
