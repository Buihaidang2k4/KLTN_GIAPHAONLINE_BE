package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.FamilyInvitation;
import com.codewithdang.kltn_giaphaonline.enums.FamilyInvitationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FamilyInvitationRepo extends JpaRepository<FamilyInvitation, Long> {
    boolean existsByFamily_FamilyIdAndInvitedEmailAndInvitationStatus(Long familyFamilyId, String invitedEmail, FamilyInvitationStatus invitationStatus);

    Optional<FamilyInvitation> findByInviteToken(String inviteToken);

    Page<FamilyInvitation> findByInvitedByAccount_AccountId(Long invitedByAccountAccountId, Pageable pageable);

    Page<FamilyInvitation> findByInvitedEmailIgnoreCase(String invitedEmail, Pageable pageable);

    void deleteByInvitedAccount(Account invitedAccount);

    void deleteByInvitedByAccount(Account invitedByAccount);
}
