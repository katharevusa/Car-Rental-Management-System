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
public class ReservationRecordNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>ReservationRecordNotFoundException</code>
     * without detail message.
     */
    public ReservationRecordNotFoundException() {
    }

    /**
     * Constructs an instance of <code>ReservationRecordNotFoundException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public ReservationRecordNotFoundException(String msg) {
        super(msg);
    }
}
