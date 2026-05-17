package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.dto.response.PersonRes;
import com.codewithdang.kltn_giaphaonline.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepo extends JpaRepository<Person, Long> {

    List<Person> findAllByFamilyCategory_FamilyCategoryId(Long familyCategoryId);

    boolean existsByFather_PersonId(Long fatherPersonId);

    boolean existsByMother_PersonId(Long motherPersonId);

    List<Person> findByFather_PersonId(Long fatherPersonId);

    @Query("SELECT p FROM Person p WHERE p.familyCategory.familyCategoryId = :categoryId OR " +
            "p.personId IN (SELECT pr.partner.personId FROM PersonRelationship pr WHERE pr.person.familyCategory.familyCategoryId = :categoryId) OR " +
            "p.personId IN (SELECT pr.person.personId FROM PersonRelationship pr WHERE pr.partner.familyCategory.familyCategoryId = :categoryId)")
    List<Person> findAllPersonsInTree(@Param("categoryId") Long categoryId);

    List<PersonRes> getByFather_PersonId(Long fatherPersonId);
}
