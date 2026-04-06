package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.FamilyInvitation;
import com.codewithdang.kltn_giaphaonline.enums.FamilyInvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilyInvitationRepo extends JpaRepository<FamilyInvitation, Long> {
    boolean existsByFamily_FamilyIdAndInvitedEmailAndInvitationStatus(Long familyFamilyId, String invitedEmail, FamilyInvitationStatus invitationStatus);
}
