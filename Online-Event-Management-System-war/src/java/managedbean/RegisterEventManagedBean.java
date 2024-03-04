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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import session.EventSessionLocal;

/**
 *
 * @author User
 */
@Named(value = "registerEventManagedBean")
@ViewScoped
public class RegisterEventManagedBean implements Serializable {
    
    @EJB
    private EventSessionLocal eventSessionLocal;
    
    private List<Event> events;

    /**
     * Creates a new instance of RegisterEventManagedBean
     */
    public RegisterEventManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            events = eventSessionLocal.searchEventsByTitle(null);
        } catch (Exception ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unknown Error"));
        }
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
    
}
