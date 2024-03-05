/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package error;

/**
 *
 * @author kenne
 */
public class RegistrationDeletionNotAllowedException extends Exception {

    /**
     * Creates a new instance of
     * <code>RegistrationDeletionNotAllowedException</code> without detail
     * message.
     */
    public RegistrationDeletionNotAllowedException() {
    }

    /**
     * Constructs an instance of
     * <code>RegistrationDeletionNotAllowedException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public RegistrationDeletionNotAllowedException(String msg) {
        super(msg);
    }
}
