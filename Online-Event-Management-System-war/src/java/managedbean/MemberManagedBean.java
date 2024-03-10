/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.Member;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import session.MemberSessionLocal;

/**
 *
 * @author User
 */
@Named(value = "memberManagedBean")
@ViewScoped
public class MemberManagedBean implements Serializable {
    
    @Inject
    private AuthenticationManagedBean authenticationManagedBean;
    
    @EJB
    private MemberSessionLocal memberSessionLocal;
    
    private Member member;

    private String password;
    private String name;
    private String phone;
    private String email;

    /**
     * Creates a new instance of MemberManagedBean
     */
    public MemberManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            member = memberSessionLocal.retrieveMemberByMemberId(authenticationManagedBean.getMemberId());
        } catch (Exception ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Member does not exist"));
        }
    }
    
    public String updateMember(ActionEvent evt) {
        FacesContext context = FacesContext.getCurrentInstance();
        member.setPassword(password);
        member.setName(name);
        member.setPhone(phone);
        member.setEmail(email);
        try {
            memberSessionLocal.updateMember(member);
            return "/secret/viewProfile.xhtml?faces-redirect=true";
        } catch (Exception ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to update member"));
            return null;
        }
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
