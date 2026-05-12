package com.codewithdang.kltn_giaphaonline.service.tree.person;

public interface PersonRelationshipService {
    void addRelationship(Long personId, Long partnerId, String relationType);
    void removeRelationship(Long relationshipId);
}
