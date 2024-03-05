/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Event;
import error.NoResultException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author kenne
 */
@Local
public interface RegistrationSessionLocal {
    
    public void createRegistration(Long memberId, Long eventId) throws NoResultException;
    
    public List<Event> retrieveRegisteredEventsByMemberId(Long memberId) throws NoResultException;
    
}
