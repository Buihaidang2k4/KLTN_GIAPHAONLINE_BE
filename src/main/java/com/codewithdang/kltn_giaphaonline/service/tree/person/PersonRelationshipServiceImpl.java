package com.codewithdang.kltn_giaphaonline.service.tree.person;

import com.codewithdang.kltn_giaphaonline.entity.Person;
import com.codewithdang.kltn_giaphaonline.entity.PersonRelationship;
import com.codewithdang.kltn_giaphaonline.enums.RelationType;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.repo.PersonRelationshipRepo;
import com.codewithdang.kltn_giaphaonline.repo.PersonRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PersonRelationshipServiceImpl implements PersonRelationshipService {

    PersonRepo personRepo;
    PersonRelationshipRepo relationshipRepo;

    @Override
    @Transactional
    public void addRelationship(Long personId, Long partnerId, String relationType) {
        Person person = personRepo.findById(personId)
                .orElseThrow(() -> new AppException(ErrorCode.PERSON_NOT_FOUND));
        Person partner = personRepo.findById(partnerId)
                .orElseThrow(() -> new AppException(ErrorCode.PERSON_NOT_FOUND));

        PersonRelationship relationship = PersonRelationship.builder()
                .person(person)
                .partner(partner)
                .relationType(RelationType.valueOf(relationType))
                .isPrimary(true)
                .build();

        relationshipRepo.save(relationship);
    }

    @Override
    @Transactional
    public void removeRelationship(Long relationshipId) {
        if (!relationshipRepo.existsById(relationshipId)) {
            throw new AppException(ErrorCode.PERSON_RELATIONSHIP_NOT_FOUND);
        }
        relationshipRepo.deleteById(relationshipId);
    }
}
