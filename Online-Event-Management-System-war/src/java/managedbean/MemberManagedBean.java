/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.Member;
import error.ResourceNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.Serializable;
import java.nio.file.Files;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.ServletContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.Part;
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
    
    private Part uploadedfile;
    private String filename = "";

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
            password = member.getPassword();
            name = member.getName();
            phone = member.getPhone();
            email = member.getEmail();
        } catch (ResourceNotFoundException ex) {
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
        } catch (ResourceNotFoundException ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to update member"));
            return null;
        }
    }
    
    public String upload() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

        //get the deployment path
        String UPLOAD_DIRECTORY = ctx.getRealPath("/") + "upload/";
        System.out.println("#UPLOAD_DIRECTORY : " + UPLOAD_DIRECTORY);

        //debug purposes
        setFilename(Paths.get(uploadedfile.getSubmittedFileName()).getFileName().toString());
        System.out.println("filename: " + getFilename());
        //---------------------
        
        //replace existing file
        Path path = Paths.get(UPLOAD_DIRECTORY + getFilename());
        InputStream bytes = uploadedfile.getInputStream();
        Files.copy(bytes, path, StandardCopyOption.REPLACE_EXISTING);
        
        //Setting Member new photo
        String photo = "/Online-Event-Management-System-war/upload/" + getFilename();
        member.setPhoto(photo);
        try {
            memberSessionLocal.updateMember(member);
            return "/secret/viewProfile.xhtml?faces-redirect=true";
        } catch (ResourceNotFoundException ex) {
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
    
    public Part getUploadedfile() {
        return uploadedfile;
    }

    public void setUploadedfile(Part uploadedfile) {
        this.uploadedfile = uploadedfile;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
    
}
