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
public class PastReservationException extends Exception {

    /**
     * Creates a new instance of <code>PastReservationException</code> without
     * detail message.
     */
    public PastReservationException() {
    }

    /**
     * Constructs an instance of <code>PastReservationException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PastReservationException(String msg) {
        super(msg);
    }
}
