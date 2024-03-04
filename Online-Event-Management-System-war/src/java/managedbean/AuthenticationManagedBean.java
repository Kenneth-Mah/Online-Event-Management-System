/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.Member;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import session.MemberSessionLocal;

/**
 *
 * @author kenne
 */
@Named(value = "authenticationManagedBean")
@SessionScoped
public class AuthenticationManagedBean implements Serializable {

    @EJB
    private MemberSessionLocal memberSessionLocal;

    private Long memberId = -1L;

    private String username = null;
    private String password = null;
    private String name;
    private String phone;
    private String email;

    /**
     * Creates a new instance of AuthenticationManagedBean
     */
    public AuthenticationManagedBean() {
    }

    public String signup(ActionEvent evt) throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        Member newMember = new Member();
        newMember.setUsername(username);
        newMember.setPassword(password);
        newMember.setName(name);
        newMember.setPhone(phone);
        newMember.setEmail(email);

        try {
            memberId = memberSessionLocal.createMember(newMember);

            return "/secret/organisingEvents.xhtml?faces-redirect=true";
        } catch (Exception ex) {
            //signup failed
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "Username already exists"));
            return null;
        }
    }
    
    public String login(ActionEvent evt) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            //store the logged in user id
            memberId = memberSessionLocal.login(username, password);
            
            //do redirect
            return "/secret/organisingEvents.xhtml?faces-redirect=true";
        } catch (Exception ex) {
            //login failed
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "Username or password incorrect"));
            username = null;
            password = null;
            memberId = -1L;
            return null;
        }
    }
    
     public String logout(ActionEvent evt) {
        username = null;
        password = null;
        memberId = -1L;
        
        name = null;
        phone = null;
        email = null;
        
        return "/index.xhtml?faces-redirect=true";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
