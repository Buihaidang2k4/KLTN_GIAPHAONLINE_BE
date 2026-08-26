package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.config.annotation.OperatorAction;
import com.codewithdang.kltn_giaphaonline.constants.ApiPath;
import com.codewithdang.kltn_giaphaonline.constants.MessageConstantsVi;
import com.codewithdang.kltn_giaphaonline.dto.request.PersonReq;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyTreeNodeRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PersonRes;
import com.codewithdang.kltn_giaphaonline.enums.CommonEnums;
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
@RequestMapping(ApiPath.FamilyTree.BASE)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Family Tree Management")
public class FamilyTreeController {

    PersonService personService;
    PersonRelationshipService relationshipService;
    FamilyTreeService familyTreeService;

    // ==================== Tree ====================

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.FamilyTree.CATEGORY_TREE)
    public ResponseEntity<ApiResponse<List<FamilyTreeNodeRes>>> getTree(
            @PathVariable Long categoryId) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.FamilyTree.GET_FAMILY_TREE_SUCCESS,
                familyTreeService.getTree(categoryId)));
    }

    // ==================== Person CRUD ====================

    @OperatorAction(CommonEnums.Operator.CREATE)
    @PostMapping(value = ApiPath.FamilyTree.CATEGORY_PERSONS, consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<PersonRes>> createPerson(
            @PathVariable Long categoryId,
            @ModelAttribute PersonReq req) {
        return ResponseEntity.ok(ApiResponse.success(201, MessageConstantsVi.FamilyTree.CREATE_PERSON_SUCCESS,
                personService.createPerson(categoryId, req)));
    }

    @OperatorAction(CommonEnums.Operator.UPDATE)
    @PutMapping(value = ApiPath.FamilyTree.PERSON_BY_ID, consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<PersonRes>> updatePerson(
            @PathVariable Long personId,
            @ModelAttribute PersonReq req) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.FamilyTree.UPDATE_PERSON_SUCCESS,
                personService.updatePerson(personId, req)));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.FamilyTree.PERSON_BY_ID)
    public ResponseEntity<ApiResponse<PersonRes>> getPersonById(
            @PathVariable Long personId) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.FamilyTree.GET_PERSON_SUCCESS,
                personService.getPersonById(personId)));
    }

    @OperatorAction(CommonEnums.Operator.DELETE)
    @DeleteMapping(ApiPath.FamilyTree.PERSON_BY_ID)
    public ResponseEntity<ApiResponse<Void>> deletePerson(@PathVariable Long personId) {
        personService.deletePerson(personId);
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.FamilyTree.DELETE_PERSON_SUCCESS, null));
    }

    // ==================== Person Query ====================

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.FamilyTree.PERSON_PARTNERS)
    public ResponseEntity<ApiResponse<List<PersonRes>>> getPartners(
            @PathVariable Long personId) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.FamilyTree.GET_PARTNERS_SUCCESS,
                personService.getPartners(personId)));
    }

    @OperatorAction(CommonEnums.Operator.READ)
    @GetMapping(ApiPath.FamilyTree.PERSON_MOTHERS)
    public ResponseEntity<ApiResponse<List<PersonRes>>> getMothersByFatherId(
            @PathVariable Long fatherId) {
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.FamilyTree.GET_MOTHERS_SUCCESS,
                personService.getMothersByFatherId(fatherId)));
    }

    // ==================== Person Tree Actions ====================

    @OperatorAction(CommonEnums.Operator.CREATE)
    @PostMapping(value = ApiPath.FamilyTree.PERSON_ROOT, consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<FamilyTreeNodeRes>> addRoot(
            @PathVariable Long personId,
            @ModelAttribute PersonReq req) {
        return ResponseEntity.ok(ApiResponse.success(201, MessageConstantsVi.FamilyTree.ADD_ROOT_SUCCESS,
                personService.addRoot(personId, req)));
    }

    @OperatorAction(CommonEnums.Operator.CREATE)
    @PostMapping(value = ApiPath.FamilyTree.PERSON_PARTNER, consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<FamilyTreeNodeRes>> addPartner(
            @PathVariable Long personId,
            @ModelAttribute PersonReq req) {
        return ResponseEntity.ok(ApiResponse.success(201, MessageConstantsVi.FamilyTree.ADD_PARTNER_SUCCESS,
                personService.addPartner(personId, req)));
    }

    @OperatorAction(CommonEnums.Operator.CREATE)
    @PostMapping(value = ApiPath.FamilyTree.PERSON_CHILD, consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<FamilyTreeNodeRes>> addChild(
            @PathVariable Long personId,
            @ModelAttribute PersonReq req) {
        return ResponseEntity.ok(ApiResponse.success(201, MessageConstantsVi.FamilyTree.ADD_CHILD_SUCCESS,
                personService.addChild(personId, req)));
    }

    // ==================== Relationship ====================

    @OperatorAction(CommonEnums.Operator.CREATE)
    @PostMapping(ApiPath.FamilyTree.PERSON_RELATIONSHIPS)
    public ResponseEntity<ApiResponse<Void>> addRelationship(
            @PathVariable Long personId,
            @RequestParam Long partnerId,
            @RequestParam String relationType) {
        relationshipService.addRelationship(personId, partnerId, relationType);
        return ResponseEntity.ok(ApiResponse.success(201, MessageConstantsVi.FamilyTree.ADD_RELATIONSHIP_SUCCESS, null));
    }

    @OperatorAction(CommonEnums.Operator.DELETE)
    @DeleteMapping(ApiPath.FamilyTree.RELATIONSHIP_BY_ID)
    public ResponseEntity<ApiResponse<Void>> removeRelationship(
            @PathVariable Long relationshipId) {
        relationshipService.removeRelationship(relationshipId);
        return ResponseEntity.ok(ApiResponse.success(200, MessageConstantsVi.FamilyTree.REMOVE_RELATIONSHIP_SUCCESS, null));
    }
}
