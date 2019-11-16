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
public class PartnerNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>PartnerNotFoundException</code> without
     * detail message.
     */
    public PartnerNotFoundException() {
    }

    /**
     * Constructs an instance of <code>PartnerNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PartnerNotFoundException(String msg) {
        super(msg);
    }
}
