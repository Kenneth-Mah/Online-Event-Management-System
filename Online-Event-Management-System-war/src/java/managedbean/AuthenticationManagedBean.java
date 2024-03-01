/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.Member;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
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
    
    private String username = null;
    private String password = null;
    private Long memberId = -1L;

    /**
     * Creates a new instance of AuthenticationManagedBean
     */
    public AuthenticationManagedBean() {
    }
    
    public String signup(ActionEvent evt) {
        Member m = new Member();
        m.setUsername(username);
        m.setPassword(password);
        
        try {
            memberId = memberSessionLocal.createMember(m);
            
            return "index.xhtml";
        } catch (Exception e) {
            //signup failed
            username = null;
            password = null;
            memberId = -1L;
            return "signup.xhtml";
        }
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
    
}
