/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Member;
import error.InputDataValidationException;
import error.InvalidLoginCredentialException;
import error.NoResultException;
import javax.ejb.Local;

/**
 *
 * @author kenne
 */
@Local
public interface MemberSessionLocal {
    
    public Long createMember(Member newMember) throws InputDataValidationException;
    
    public Member retrieveMemberByUsername(String username) throws NoResultException;
    
    public Long login(String username, String password) throws InvalidLoginCredentialException;
    
    public Member retrieveMemberByMemberId(Long memberId) throws NoResultException;
    
    public void updateMember(Member updatedMember) throws NoResultException;
    
}
