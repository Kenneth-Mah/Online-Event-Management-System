/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.Event;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import session.EventSessionLocal;
import session.RegistrationSessionLocal;

/**
 *
 * @author User
 */
@Named(value = "registerEventManagedBean")
@ViewScoped
public class RegisterEventManagedBean implements Serializable {
    
    @Inject
    private AuthenticationManagedBean authenticationManagedBean;
    
    @EJB
    private EventSessionLocal eventSessionLocal;
    @EJB
    private RegistrationSessionLocal registrationSessionLocal;
    
    private List<Event> searchedEvents;
    
    private List<Event> registeredEvents;

    /**
     * Creates a new instance of RegisterEventManagedBean
     */
    public RegisterEventManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        searchedEvents = eventSessionLocal.searchEventsByTitle(null);
        try {
            registeredEvents = registrationSessionLocal.retrieveRegisteredEventsByMemberId(authenticationManagedBean.getMemberId());
        } catch (Exception ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Member does not exist"));
        }
    }
    
    public void registerForEvent() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext()
                .getRequestParameterMap();
        String eventIdStr = params.get("eventId");
        Long eventId = Long.parseLong(eventIdStr);
        try {
            registrationSessionLocal.createRegistration(authenticationManagedBean.getMemberId(), eventId);
            registeredEvents = registrationSessionLocal.retrieveRegisteredEventsByMemberId(authenticationManagedBean.getMemberId());
        } catch (Exception e) {
            //show with an error icon
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to register for event"));
            return;
        }
        context.addMessage(null, new FacesMessage("Success", "Successfully registered for event"));
    }
    
    public void unregisterFromEvent() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext()
                .getRequestParameterMap();
        String eventIdStr = params.get("eventId");
        Long eventId = Long.parseLong(eventIdStr);
        try {
            registrationSessionLocal.deleteRegistration(authenticationManagedBean.getMemberId(), eventId);
            registeredEvents = registrationSessionLocal.retrieveRegisteredEventsByMemberId(authenticationManagedBean.getMemberId());
        } catch (Exception e) {
            //show with an error icon
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to unregister from event"));
            return;
        }
        context.addMessage(null, new FacesMessage("Success", "Successfully unregistered from event"));
    }

    public List<Event> getSearchedEvents() {
        return searchedEvents;
    }

    public void setSearchedEvents(List<Event> searchedEvents) {
        this.searchedEvents = searchedEvents;
    }

    public List<Event> getRegisteredEvents() {
        return registeredEvents;
    }

    public void setRegisteredEvents(List<Event> registeredEvents) {
        this.registeredEvents = registeredEvents;
    }
    
}
