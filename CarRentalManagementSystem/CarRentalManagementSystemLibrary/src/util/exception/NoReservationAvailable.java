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
public class NoReservationAvailable extends Exception {

    /**
     * Creates a new instance of <code>NoReservationAvailable</code> without
     * detail message.
     */
    public NoReservationAvailable() {
    }

    /**
     * Constructs an instance of <code>NoReservationAvailable</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoReservationAvailable(String msg) {
        super(msg);
    }
}
