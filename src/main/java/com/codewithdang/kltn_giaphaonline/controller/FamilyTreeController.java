package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.dto.request.PersonReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyTreeNodeRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PersonRes;
import com.codewithdang.kltn_giaphaonline.service.tree.node.FamilyTreeService;
import com.codewithdang.kltn_giaphaonline.service.tree.person.PersonRelationshipService;
import com.codewithdang.kltn_giaphaonline.service.tree.person.PersonService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/family-tree")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Family Tree Management")
public class FamilyTreeController {

    PersonService personService;
    PersonRelationshipService relationshipService;
    FamilyTreeService familyTreeService;

    // ==================== Tree ====================

    @GetMapping("/categories/{categoryId}/tree")
    public ResponseEntity<ApiResponse<List<FamilyTreeNodeRes>>> getTree(
            @PathVariable Long categoryId) {
        return ResponseEntity.ok(ApiResponse.success(200, "GET_FAMILY_TREE_SUCCESS",
                familyTreeService.getTree(categoryId)));
    }

    // ==================== Person CRUD ====================

    @PostMapping(value = "/categories/{categoryId}/persons", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<PersonRes>> createPerson(
            @PathVariable Long categoryId,
            @ModelAttribute PersonReq req) {
        return ResponseEntity.ok(ApiResponse.success(201, "CREATE_PERSON_SUCCESS",
                personService.createPerson(categoryId, req)));
    }

    @PutMapping(value = "/persons/{personId}", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<PersonRes>> updatePerson(
            @PathVariable Long personId,
            @ModelAttribute PersonReq req) {
        return ResponseEntity.ok(ApiResponse.success(200, "UPDATE_PERSON_SUCCESS",
                personService.updatePerson(personId, req)));
    }

    @GetMapping("/persons/{personId}")
    public ResponseEntity<ApiResponse<PersonRes>> getPersonById(
            @PathVariable Long personId) {
        return ResponseEntity.ok(ApiResponse.success(200, "GET_PERSON_SUCCESS",
                personService.getPersonById(personId)));
    }

    @DeleteMapping("/persons/{personId}")
    public ResponseEntity<ApiResponse<Void>> deletePerson(@PathVariable Long personId) {
        personService.deletePerson(personId);
        return ResponseEntity.ok(ApiResponse.success(200, "DELETE_PERSON_SUCCESS", null));
    }

    // ==================== Person Query ====================

    @GetMapping("/persons/{personId}/partners")
    public ResponseEntity<ApiResponse<List<PersonRes>>> getPartners(
            @PathVariable Long personId) {
        return ResponseEntity.ok(ApiResponse.success(200, "GET_PARTNERS_SUCCESS",
                personService.getPartners(personId)));
    }

    @GetMapping("/persons/{fatherId}/mothers")
    public ResponseEntity<ApiResponse<List<PersonRes>>> getMothersByFatherId(
            @PathVariable Long fatherId) {
        return ResponseEntity.ok(ApiResponse.success(200, "GET_MOTHERS_SUCCESS",
                personService.getMothersByFatherId(fatherId)));
    }

    // ==================== Person Tree Actions ====================

    @PostMapping(value = "/persons/{personId}/root", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<FamilyTreeNodeRes>> addRoot(
            @PathVariable Long personId,
            @ModelAttribute PersonReq req) {
        return ResponseEntity.ok(ApiResponse.success(201, "ADD_ROOT_SUCCESS",
                personService.addRoot(personId, req)));
    }

    @PostMapping(value = "/persons/{personId}/partner", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<FamilyTreeNodeRes>> addPartner(
            @PathVariable Long personId,
            @ModelAttribute PersonReq req) {
        return ResponseEntity.ok(ApiResponse.success(201, "ADD_PARTNER_SUCCESS",
                personService.addPartner(personId, req)));
    }

    @PostMapping(value = "/persons/{personId}/child", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<FamilyTreeNodeRes>> addChild(
            @PathVariable Long personId,
            @ModelAttribute PersonReq req) {
        return ResponseEntity.ok(ApiResponse.success(201, "ADD_CHILD_SUCCESS",
                personService.addChild(personId, req)));
    }

    // ==================== Relationship ====================

    @PostMapping("/persons/{personId}/relationships")
    public ResponseEntity<ApiResponse<Void>> addRelationship(
            @PathVariable Long personId,
            @RequestParam Long partnerId,
            @RequestParam String relationType) {
        relationshipService.addRelationship(personId, partnerId, relationType);
        return ResponseEntity.ok(ApiResponse.success(201, "ADD_RELATIONSHIP_SUCCESS", null));
    }

    @DeleteMapping("/relationships/{relationshipId}")
    public ResponseEntity<ApiResponse<Void>> removeRelationship(
            @PathVariable Long relationshipId) {
        relationshipService.removeRelationship(relationshipId);
        return ResponseEntity.ok(ApiResponse.success(200, "REMOVE_RELATIONSHIP_SUCCESS", null));
    }
}
