package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.Family;
import com.codewithdang.kltn_giaphaonline.entity.FamilyMember;
import com.codewithdang.kltn_giaphaonline.enums.FamilyMemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyMemberRepo extends JpaRepository<FamilyMember, Long> {

    boolean existsByFamilyAndAccount_Email(Family family, String email);

    Optional<FamilyMember> findByFamily_FamilyIdAndAccount_AccountId(Long familyFamilyId, Long accountAccountId);

    boolean existsByFamilyAndAccount_AccountId(Family family, Long accountAccountId);

    boolean existsByFamily_FamilyIdAndAccount_AccountIdAndStatus(Long familyFamilyId, Long accountAccountId, FamilyMemberStatus status);

    void deleteByAccount(Account account);

    List<FamilyMember> findByFamily_FamilyId(Long familyFamilyId);

    Page<FamilyMember> findAllByAccount(Account account, Pageable pageable);

    boolean existsByFamilyAndAccount(Family family, Account account);

    List<FamilyMember> findAllByAccount_AccountId(Long accountId);

    List<FamilyMember> findByAccount_AccountIdAndFamily_FamilyId(Long accountAccountId, Long familyFamilyId);

    long countByFamily_FamilyIdAndRole_NameAndStatus(Long familyId, String roleName, FamilyMemberStatus status);
}
