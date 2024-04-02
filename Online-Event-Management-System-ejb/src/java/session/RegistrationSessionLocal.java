/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Event;
import entity.Registration;
import error.ResourceNotFoundException;
import error.RegistrationDeletionNotAllowedException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author kenne
 */
@Local
public interface RegistrationSessionLocal {
    
    public void createRegistration(Long memberId, Long eventId) throws ResourceNotFoundException;
    
    public List<Event> retrieveRegisteredEventsByMemberId(Long memberId) throws ResourceNotFoundException;
    
    public void deleteRegistration(Long memberId, Long eventId) throws ResourceNotFoundException, RegistrationDeletionNotAllowedException;
    
    public Registration retrieveRegistrationByRegistrationId(Long registrationId) throws ResourceNotFoundException;
    
    public void toggleAttendence(Long registrationId) throws ResourceNotFoundException;
    
}
