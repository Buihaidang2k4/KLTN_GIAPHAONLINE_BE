package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.PersonRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRelationshipRepo extends JpaRepository<PersonRelationship, Long> {

    @Query("SELECT pr FROM PersonRelationship pr WHERE pr.person.personId IN :personIds OR pr.partner.personId IN :personIds")
    List<PersonRelationship> findAllByPersonIds(@Param("personIds") List<Long> personIds);

    @Modifying
    @Query("DELETE FROM PersonRelationship pr WHERE pr.person.personId IN :personIds OR pr.partner.personId IN :personIds")
    void deleteAllByPersonIds(@Param("personIds") List<Long> personIds);

    @Query("SELECT pr FROM PersonRelationship pr WHERE pr.person.personId = :personId OR pr.partner.personId = :personId")
    List<PersonRelationship> findAllByPersonId(@Param("personId") Long personId);

    void deleteAllByPerson_PersonIdOrPartner_PersonId(Long personId, Long partnerId);
}
