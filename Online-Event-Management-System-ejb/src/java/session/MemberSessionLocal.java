/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Member;
import error.InputDataValidationException;
import javax.ejb.Local;

/**
 *
 * @author kenne
 */
@Local
public interface MemberSessionLocal {
    
    public Long createMember(Member m) throws InputDataValidationException;
    
}
