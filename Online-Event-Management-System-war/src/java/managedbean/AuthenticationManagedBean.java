/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.Member;
import error.InputDataValidationException;
import error.InvalidLoginCredentialException;
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
    private String photo;
    private String phone;
    private String email;

    /**
     * Creates a new instance of AuthenticationManagedBean
     */
    public AuthenticationManagedBean() {
    }

    public String signup(ActionEvent evt) {
        FacesContext context = FacesContext.getCurrentInstance();
               
        //Setting default photo
        photo = "/Online-Event-Management-System-war/upload/default-profile-icon.png";

        Member newMember = new Member();
        newMember.setUsername(username);
        newMember.setPassword(password);
        newMember.setName(name);
        newMember.setPhoto(photo);
        newMember.setPhone(phone);
        newMember.setEmail(email);

        try {
            memberId = memberSessionLocal.createMember(newMember);
            username = null;
            password = null;
            name = null;
            phone = null;
            email = null;

            return "/secret/organisingEvents2.xhtml?faces-redirect=true";
        } catch (InputDataValidationException ex) {
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
            username = null;
            password = null;

            //do redirect
            return "/secret/organisingEvents2.xhtml?faces-redirect=true";
        } catch (InvalidLoginCredentialException ex) {
            //login failed
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "Username or password incorrect"));
            memberId = -1L;
            username = null;
            password = null;
            return null;
        }
    }

    public String logout(ActionEvent evt) {
        memberId = -1L;
        username = null;
        password = null;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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
