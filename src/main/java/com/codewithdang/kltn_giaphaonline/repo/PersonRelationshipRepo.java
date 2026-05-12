package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.PersonRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRelationshipRepo extends JpaRepository<PersonRelationship, Long> {

    @Query("SELECT pr FROM PersonRelationship pr WHERE pr.person.personId IN :personIds OR pr.partner.personId IN :personIds")
    List<PersonRelationship> findAllByPersonIds(@Param("personIds") List<Long> personIds);
}
