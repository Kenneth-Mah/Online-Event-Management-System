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
public class InputDataValidationException extends Exception {

    /**
     * Creates a new instance of <code>UsernameAlreadyExistsException</code>
     * without detail message.
     */
    public InputDataValidationException() {
    }

    /**
     * Constructs an instance of <code>UsernameAlreadyExistsException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public InputDataValidationException(String msg) {
        super(msg);
    }
}
