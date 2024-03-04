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
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import session.EventSessionLocal;

/**
 *
 * @author User
 */
@Named(value = "organiseEventManagedBean")
@ViewScoped
public class OrganiseEventManagedBean implements Serializable {
    
    @Inject
    private AuthenticationManagedBean authenticationManagedBean;
    
    @EJB
    private EventSessionLocal eventSessionLocal;
    
    private String title;
    private String location;
    private String description;
    private Date date;
    private Date deadline;
    
    private List<Event> events;

    /**
     * Creates a new instance of OrganiseEventManagedBean
     */
    public OrganiseEventManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            events = eventSessionLocal.retrieveEventsByMemberId(authenticationManagedBean.getMemberId());
        } catch (Exception ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Member does not exist"));
        }
    }
    
    public String createEvent(ActionEvent evt) {
        FacesContext context = FacesContext.getCurrentInstance();
        
        Event newEvent = new Event();
        newEvent.setTitle(title);
        newEvent.setLocation(location);
        newEvent.setDescription(description);
        newEvent.setDate(date);
        newEvent.setDeadline(deadline);
        
        try {
            eventSessionLocal.createEvent(authenticationManagedBean.getMemberId(), newEvent);
            
            return "/secret/organisingEvents.xhtml?faces-redirect=true";
        } catch (Exception ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "Member does not exist"));
            return null;
        }
    }
    
    public void deleteEvent() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext()
                .getRequestParameterMap();
        String eventIdStr = params.get("eventId");
        Long eventId = Long.parseLong(eventIdStr);
        try {
            eventSessionLocal.deleteEvent(eventId);
        } catch (Exception e) {
            //show with an error icon
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to delete event"));
            return;
        }
        context.addMessage(null, new FacesMessage("Success", "Successfully deleted event"));
        init();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
    
}
