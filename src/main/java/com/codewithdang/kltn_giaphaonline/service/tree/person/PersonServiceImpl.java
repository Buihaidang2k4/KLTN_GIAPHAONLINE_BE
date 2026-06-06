package com.codewithdang.kltn_giaphaonline.service.tree.person;

import com.codewithdang.kltn_giaphaonline.dto.request.CreateAuditLogReq;
import com.codewithdang.kltn_giaphaonline.dto.request.PersonReq;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyTreeNodeRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PersonRes;
import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.FamilyCategory;
import com.codewithdang.kltn_giaphaonline.entity.Person;
import com.codewithdang.kltn_giaphaonline.entity.PersonRelationship;
import com.codewithdang.kltn_giaphaonline.enums.AuditAction;
import com.codewithdang.kltn_giaphaonline.enums.AuditEntityType;
import com.codewithdang.kltn_giaphaonline.enums.Gender;
import com.codewithdang.kltn_giaphaonline.enums.RelationType;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.mapper.PersonMapper;
import com.codewithdang.kltn_giaphaonline.repo.AccountRepo;
import com.codewithdang.kltn_giaphaonline.repo.FamilyCategoryRepo;
import com.codewithdang.kltn_giaphaonline.repo.PersonRelationshipRepo;
import com.codewithdang.kltn_giaphaonline.repo.PersonRepo;
import com.codewithdang.kltn_giaphaonline.service.audit_log.AuditLogService;
import com.codewithdang.kltn_giaphaonline.service.minio_media.MinioService;
import com.codewithdang.kltn_giaphaonline.utils.ConstantUtils;
import com.codewithdang.kltn_giaphaonline.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PersonServiceImpl implements PersonService {

    PersonRepo personRepo;
    PersonRelationshipRepo relationshipRepo;
    FamilyCategoryRepo familyCategoryRepo;
    AccountRepo accountRepo;
    PersonMapper personMapper;
    MinioService minioService;
    AuditLogService auditLogService;
    SecurityUtils securityUtils;

    @Override
    @Transactional
    public PersonRes createPerson(Long categoryId, PersonReq req) {
        FamilyCategory category = familyCategoryRepo.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_CATEGORY_NOT_EXISTED));

        Account account = securityUtils.getCurrentAccount();
        Person newPerson = personMapper.toEntity(req);
        newPerson.setFamilyCategory(category);
        newPerson.setCreatedByAccount(account);
        newPerson.setGeneration(0L);

        if (req.getAvatar() != null && !req.getAvatar().isEmpty()) {
            newPerson.setAvatarPath(minioService.uploadImage(req.getAvatar(), ConstantUtils.Avatar));
        }

        Person saved = personRepo.save(newPerson);
        incrementTotalPerson(category);

        auditLogService.log(CreateAuditLogReq.builder()
                .auditAction(AuditAction.NODE_CREATE.getLabel())
                .actorAccountId(account.getAccountId())
                .familyId(category.getFamily().getFamilyId())
                .newData(Map.of("Tên thành viên: ", saved.getFullName()))
                .entityId(saved.getPersonId().toString())
                .build());
        return personMapper.toRes(saved);
    }

    @Override
    @Transactional
    public PersonRes updatePerson(Long personId, PersonReq req) {
        Person person = personRepo.findById(personId)
                .orElseThrow(() -> new AppException(ErrorCode.PERSON_NOT_FOUND));
        Account account = securityUtils.getCurrentAccount();

        if (req.getAvatar() != null && !req.getAvatar().isEmpty()) {
            if (person.getAvatarPath() != null) minioService.deleteFile(person.getAvatarPath());
            person.setAvatarPath(minioService.uploadImage(req.getAvatar(), ConstantUtils.Avatar));
        }

        if (req.getMotherId() != null) {
            Person mother = personRepo.findById(req.getMotherId())
                    .orElseThrow(() -> new AppException(ErrorCode.PERSON_NOT_FOUND));
            person.setMother(mother);
            if (mother.getGeneration() != null) person.setGeneration(mother.getGeneration() + 1);
        }

        if (req.getFullName() != null) person.setFullName(req.getFullName());
        if (req.getGender() != null) person.setGender(req.getGender());
        if (req.getPhoneNumber() != null) person.setPhoneNumber(req.getPhoneNumber());
        if (req.getBirthDate() != null) person.setBirthDate(req.getBirthDate());
        if (req.getDeathDate() != null) person.setDeathDate(req.getDeathDate());
        if (req.getOriginPlace() != null) person.setOriginPlace(req.getOriginPlace());
        if (req.getPlaceOfResidence() != null) person.setPlaceOfResidence(req.getPlaceOfResidence());
        if (req.getGraveLocation() != null) person.setGraveLocation(req.getGraveLocation());
        if (req.getLifeStatus() != null) person.setLifeStatus(req.getLifeStatus());
        if (req.getBiography() != null) person.setBiography(req.getBiography());
        if (req.getSlug() != null) person.setSlug(req.getSlug());

        person = personRepo.save(person);

        auditLogService.log(CreateAuditLogReq.builder()
                .auditAction(AuditAction.NODE_UPDATE.getLabel())
                .actorAccountId(account.getAccountId())
                .entityType(AuditEntityType.PERSON.name())
                .familyId(person.getFamilyCategory() != null ? person.getFamilyCategory().getFamily().getFamilyId() : null)
                .newData(Map.of("fullName", String.valueOf(person.getFullName())))
                .entityId(person.getPersonId().toString())
                .build());

        return personMapper.toRes(person);
    }

    @Override
    @Transactional(readOnly = true)
    public PersonRes getPersonById(Long personId) {
        return personRepo.findById(personId)
                .map(personMapper::toRes)
                .orElseThrow(() -> new AppException(ErrorCode.PERSON_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public PersonRes getPersonByFamilyCategoryId(Long familyCategoryId) {
        return personRepo.findAllByFamilyCategory_FamilyCategoryId(familyCategoryId)
                .stream()
                .filter(p -> p.getFather() == null && p.getMother() == null)
                .findFirst()
                .map(personMapper::toRes)
                .orElseThrow(() -> new AppException(ErrorCode.PERSON_NOT_FOUND));
    }

    @Override
    @Transactional
    public FamilyTreeNodeRes addRoot(Long personId, PersonReq req) {
        Person currentPerson = personRepo.findById(personId)
                .orElseThrow(() -> new AppException(ErrorCode.PERSON_NOT_FOUND));

        if (currentPerson.getFather() != null)
            throw new AppException(ErrorCode.PERSON_ALREADY_HAS_FATHER);

        FamilyCategory category = currentPerson.getFamilyCategory();
        Account currentAccount = securityUtils.getCurrentAccount();

        Long minGen = personRepo.findAllByFamilyCategory_FamilyCategoryId(category.getFamilyCategoryId())
                .stream()
                .map(Person::getGeneration)
                .filter(g -> g != null)
                .min(Long::compareTo)
                .orElse(1L);

        Person newRoot = personMapper.toEntity(req);
        newRoot.setFamilyCategory(category);
        newRoot.setCreatedByAccount(currentAccount);
        newRoot.setFather(null);
        newRoot.setMother(null);
        newRoot.setGeneration(minGen - 1);

        if (req.getAvatar() != null && !req.getAvatar().isEmpty()) {
            newRoot.setAvatarPath(minioService.uploadImage(req.getAvatar(), ConstantUtils.Avatar));
        }

        newRoot = personRepo.save(newRoot);
        incrementTotalPerson(category);

        currentPerson.setFather(newRoot);
        personRepo.save(currentPerson);

        category.setGenerationOffset(category.getGenerationOffset() + 1);
        familyCategoryRepo.save(category);

        auditLogService.log(CreateAuditLogReq.builder()
                .auditAction(AuditAction.NODE_CREATE.getLabel())
                .actorAccountId(currentAccount.getAccountId())
                .entityType(AuditEntityType.PERSON.name())
                .familyId(category.getFamily().getFamilyId())
                .newData(Map.of("fullName", String.valueOf(newRoot.getFullName()), "type", "root"))
                .entityId(newRoot.getPersonId().toString())
                .build());

        return toNode(newRoot, Collections.emptyList(), true, category.getGenerationOffset());
    }

    @Override
    @Transactional
    public FamilyTreeNodeRes addPartner(Long personId, PersonReq req) {
        Person currentPerson = personRepo.findById(personId)
                .orElseThrow(() -> new AppException(ErrorCode.PERSON_NOT_FOUND));

        Account account = securityUtils.getCurrentAccount();

        Person partner = personMapper.toEntity(req);
        if (currentPerson.getGender() == Gender.MALE) partner.setGender(Gender.FEMALE);
        else if (currentPerson.getGender() == Gender.FEMALE) partner.setGender(Gender.MALE);
        partner.setFamilyCategory(currentPerson.getFamilyCategory());
        partner.setCreatedByAccount(account);
        partner.setGeneration(currentPerson.getGeneration());

        if (req.getAvatar() != null && !req.getAvatar().isEmpty()) {
            partner.setAvatarPath(minioService.uploadImage(req.getAvatar(), ConstantUtils.Avatar));
        }

        partner = personRepo.save(partner);

        if (currentPerson.getFamilyCategory() != null) {
            incrementTotalPerson(currentPerson.getFamilyCategory());
        }

        relationshipRepo.save(PersonRelationship.builder()
                .person(currentPerson)
                .partner(partner)
                .relationType(RelationType.SPOUSE)
                .isPrimary(true)
                .build());

        auditLogService.log(CreateAuditLogReq.builder()
                .auditAction(AuditAction.NODE_RELATION_CHANGE.getLabel())
                .actorAccountId(account.getAccountId())
                .entityType(AuditEntityType.PERSON.name())
                .familyId(currentPerson.getFamilyCategory() != null ? currentPerson.getFamilyCategory().getFamily().getFamilyId() : null)
                .newData(Map.of("fullName", String.valueOf(partner.getFullName()), "type", "partner"))
                .entityId(partner.getPersonId().toString())
                .build());

        Long offset = getOffset(currentPerson);
        return toNode(partner, List.of(personId), false, offset);
    }

    @Override
    @Transactional
    public FamilyTreeNodeRes addChild(Long personId, PersonReq req) {
        Person currentPerson = personRepo.findById(personId)
                .orElseThrow(() -> new AppException(ErrorCode.PERSON_NOT_FOUND));

        Account account = securityUtils.getCurrentAccount();

        Person partner = null;
        if (req.getPartnerId() != null) {
            partner = personRepo.findById(req.getPartnerId())
                    .orElseThrow(() -> new AppException(ErrorCode.PERSON_NOT_FOUND));
        }

        FamilyCategory childCategory = currentPerson.getFamilyCategory() != null
                ? currentPerson.getFamilyCategory()
                : (partner != null ? partner.getFamilyCategory() : null);

        Person child = personMapper.toEntity(req);
        child.setCreatedByAccount(account);
        child.setFamilyCategory(childCategory);

        if (req.getAvatar() != null && !req.getAvatar().isEmpty()) {
            child.setAvatarPath(minioService.uploadImage(req.getAvatar(), ConstantUtils.Avatar));
        }

        if (currentPerson.getGeneration() != null) {
            child.setGeneration(currentPerson.getGeneration() + 1);
        }

        if (currentPerson.getGender() == Gender.MALE) {
            child.setFather(currentPerson);
            if (partner != null) child.setMother(partner);
        } else {
            child.setMother(currentPerson);
            if (partner != null) child.setFather(partner);
        }

        child = personRepo.save(child);

        if (childCategory != null) {
            incrementTotalPerson(childCategory);
        }

        auditLogService.log(CreateAuditLogReq.builder()
                .auditAction(AuditAction.NODE_CREATE.getLabel())
                .actorAccountId(account.getAccountId())
                .entityType(AuditEntityType.PERSON.name())
                .familyId(childCategory != null ? childCategory.getFamily().getFamilyId() : null)
                .newData(Map.of("fullName", String.valueOf(child.getFullName()), "type", "child"))
                .entityId(child.getPersonId().toString())
                .build());

        Long offset = getOffset(currentPerson);
        return toNode(child, Collections.emptyList(), true, offset);
    }

    @Override
    @Transactional
    public void deletePerson(Long personId) {
        Person person = personRepo.findById(personId)
                .orElseThrow(() -> new AppException(ErrorCode.PERSON_NOT_FOUND));

        if (personRepo.existsByFather_PersonId(personId) || personRepo.existsByMother_PersonId(personId)) {
            throw new AppException(ErrorCode.PERSON_HAS_CHILDREN_CANNOT_DELETE);
        }

        FamilyCategory category = person.getFamilyCategory();

        relationshipRepo.deleteAllByPerson_PersonIdOrPartner_PersonId(personId, personId);
        personRepo.delete(person);

        if (category != null) {
            decrementTotalPerson(category);
        }

        Account account = securityUtils.getCurrentAccount();
        auditLogService.log(CreateAuditLogReq.builder()
                .auditAction(AuditAction.NODE_DELETE.getLabel())
                .actorAccountId(account.getAccountId())
                .entityType(AuditEntityType.PERSON.name())
                .familyId(category != null ? category.getFamily().getFamilyId() : null)
                .newData(Map.of("fullName", String.valueOf(person.getFullName())))
                .entityId(personId.toString())
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonRes> getPartners(Long personId) {
        if (!personRepo.existsById(personId)) throw new AppException(ErrorCode.PERSON_NOT_FOUND);
        return relationshipRepo.findAllByPersonId(personId).stream()
                .map(rel -> rel.getPerson().getPersonId().equals(personId) ? rel.getPartner() : rel.getPerson())
                .map(personMapper::toRes)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonRes> getMothersByFatherId(Long fatherId) {
        if (!personRepo.existsById(fatherId)) throw new AppException(ErrorCode.PERSON_NOT_FOUND);
        return relationshipRepo.findAllByPersonId(fatherId).stream()
                .map(rel -> rel.getPerson().getPersonId().equals(fatherId) ? rel.getPartner() : rel.getPerson())
                .filter(p -> p.getGender() == Gender.FEMALE)
                .distinct()
                .map(personMapper::toRes)
                .toList();
    }

    // ==================== helpers ====================

    private void incrementTotalPerson(FamilyCategory category) {
        category.setTotalPerson(category.getTotalPerson() == null ? 1L : category.getTotalPerson() + 1);
        familyCategoryRepo.save(category);
    }

    private void decrementTotalPerson(FamilyCategory category) {
        long current = category.getTotalPerson() == null ? 0L : category.getTotalPerson();
        category.setTotalPerson(Math.max(0L, current - 1));
        familyCategoryRepo.save(category);
    }

    private Long getOffset(Person person) {
        if (person.getFamilyCategory() == null) return 1L;
        Long offset = person.getFamilyCategory().getGenerationOffset();
        return offset != null ? offset : 1L;
    }

    private FamilyTreeNodeRes toNode(Person p, List<Long> pids, boolean isInFamily, Long offset) {
        String avatarUrl = p.getAvatarPath() != null ? minioService.getPresignedUrl(p.getAvatarPath()) : null;
        Long displayGen = p.getGeneration() != null ? p.getGeneration() + offset : null;

        return FamilyTreeNodeRes.builder()
                .id(p.getPersonId())
                .fid(p.getFather() != null ? p.getFather().getPersonId() : null)
                .mid(p.getMother() != null ? p.getMother().getPersonId() : null)
                .pids(pids)
                .childs(Collections.emptyList())
                .fidName(p.getFather() != null ? p.getFather().getFullName() : null)
                .midName(p.getMother() != null ? p.getMother().getFullName() : null)
                .generation(displayGen)
                .personName(p.getFullName())
                .gender(p.getGender() != null ? p.getGender().name().toLowerCase() : null)
                .birthDate(p.getBirthDate() != null ? p.getBirthDate() : null)
                .deathDate(p.getDeathDate() != null ? p.getDeathDate() : null)
                .lifeStatus(p.getLifeStatus() != null ? p.getLifeStatus().name() : null)
                .originPlace(p.getOriginPlace())
                .placeOfResidence(p.getPlaceOfResidence())
                .avatarPath(p.getAvatarPath())
                .avatarUrl(avatarUrl)
                .isInFamily(isInFamily)
                .build();
    }

}
