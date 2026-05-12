package com.codewithdang.kltn_giaphaonline.service.tree.person;

import com.codewithdang.kltn_giaphaonline.dto.request.PersonReq;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyTreeNodeRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PersonRes;
import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.FamilyCategory;
import com.codewithdang.kltn_giaphaonline.entity.Person;
import com.codewithdang.kltn_giaphaonline.entity.PersonRelationship;
import com.codewithdang.kltn_giaphaonline.enums.Gender;
import com.codewithdang.kltn_giaphaonline.enums.RelationType;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.mapper.PersonMapper;
import com.codewithdang.kltn_giaphaonline.repo.AccountRepo;
import com.codewithdang.kltn_giaphaonline.repo.FamilyCategoryRepo;
import com.codewithdang.kltn_giaphaonline.repo.PersonRelationshipRepo;
import com.codewithdang.kltn_giaphaonline.repo.PersonRepo;
import com.codewithdang.kltn_giaphaonline.service.minio_media.MinioService;
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


    /***
     * Create first person in family category
     * @param categoryId
     * @param req
     * @return
     */
    @Override
    @Transactional
    public PersonRes createPerson(Long categoryId, PersonReq req) {
        FamilyCategory category = familyCategoryRepo.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_CATEGORY_NOT_EXISTED));

        Account account = getCurrentAccount();
        Person newPerson = personMapper.toEntity(req);
        newPerson.setFamilyCategory(category);
        newPerson.setCreatedByAccount(account);
        newPerson.setGeneration(0L);

        return personMapper.toRes(personRepo.save(newPerson));
    }

    @Override
    @Transactional
    public PersonRes updatePerson(Long personId, PersonReq req) {
        Person person = personRepo.findById(personId)
                .orElseThrow(() -> new AppException(ErrorCode.PERSON_NOT_FOUND));

        personMapper.updateEntityFromRequest(req, person);

        return personMapper.toRes(personRepo.save(person));
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

    /**
     * Tạo thủy tổ (cha) cho node hiện tại.
     * Điều kiện: node phải là nam và chưa có cha.
     * Thủy tổ mới sẽ thuộc cùng familyCategory, không có cha/mẹ.
     * Node hiện tại sẽ được set father = thủy tổ mới.
     */
    @Override
    @Transactional
    public FamilyTreeNodeRes addRoot(Long personId, PersonReq req) {
        Person currentPerson = personRepo.findById(personId)
                .orElseThrow(() -> new AppException(ErrorCode.PERSON_NOT_FOUND));

        if (currentPerson.getGender() != Gender.MALE) {
            throw new AppException(ErrorCode.PERSON_MUST_BE_MALE_TO_ADD_ROOT);
        }
        if (currentPerson.getFather() != null) {
            throw new AppException(ErrorCode.PERSON_ALREADY_HAS_FATHER);
        }

        FamilyCategory category = currentPerson.getFamilyCategory();
        Account currentAccount = getCurrentAccount();

        // tìm generation nhỏ nhất hiện tại trong category
        Long minGen = personRepo.findAllByFamilyCategory_FamilyCategoryId(category.getFamilyCategoryId())
                .stream()
                .map(Person::getGeneration)
                .filter(g -> g != null)
                .min(Long::compareTo)
                .orElse(1L);

        // tạo thủy tổ mới với gen = minGen - 1
        Person newRoot = personMapper.toEntity(req);
        newRoot.setFamilyCategory(category);
        newRoot.setCreatedByAccount(currentAccount);
        newRoot.setFather(null);
        newRoot.setMother(null);
        newRoot.setGeneration(minGen - 1);
        newRoot = personRepo.save(newRoot);

        // gán thủy tổ làm cha của node hiện tại
        currentPerson.setFather(newRoot);
        personRepo.save(currentPerson);

        // tăng offset để generation hiển thị luôn dương
        category.setGenerationOffset(category.getGenerationOffset() + 1);
        familyCategoryRepo.save(category);

        return toNode(newRoot, Collections.emptyList(), true, category.getGenerationOffset());
    }

    /**
     * Thêm vợ/chồng cho personId.
     * Partner mới không thuộc dòng họ (familyCategory = null).
     * Tạo PersonRelationship SPOUSE giữa 2 người.
     */
    @Override
    @Transactional
    public FamilyTreeNodeRes addPartner(Long personId, PersonReq req) {
        Person person = personRepo.findById(personId)
                .orElseThrow(() -> new AppException(ErrorCode.PERSON_NOT_FOUND));

        Account account = getCurrentAccount();

        Person partner = personMapper.toEntity(req);
        partner.setFamilyCategory(null);
        partner.setCreatedByAccount(account);
        partner = personRepo.save(partner);

        PersonRelationship relationship = PersonRelationship.builder()
                .person(person)
                .partner(partner)
                .relationType(RelationType.SPOUSE)
                .isPrimary(true)
                .build();
        relationshipRepo.save(relationship);

        Long offset = getOffset(person);
        List<String> pids = List.of(personId.toString());
        return toNode(partner, pids, false, offset);
    }

    /**
     * Thêm con cho personId.
     * Tự xác định fid/mid dựa vào gender của personId.
     * Tìm partner qua PersonRelationship để set fid+mid đầy đủ cho con.
     * Con thuộc cùng familyCategory với cha/mẹ trong dòng họ.
     */
    @Override
    @Transactional
    public FamilyTreeNodeRes addChild(Long personId, PersonReq req) {
        Person person = personRepo.findById(personId)
                .orElseThrow(() -> new AppException(ErrorCode.PERSON_NOT_FOUND));

        Account account = getCurrentAccount();

        // tìm partner qua relationship
        List<PersonRelationship> relationships = relationshipRepo.findAllByPersonId(personId);
        Person partner = relationships.stream()
                .map(rel -> rel.getPerson().getPersonId().equals(personId)
                        ? rel.getPartner()
                        : rel.getPerson())
                .findFirst()
                .orElse(null);

        Person child = personMapper.toEntity(req);
        child.setFamilyCategory(person.getFamilyCategory());
        child.setCreatedByAccount(account);

        // set generation cho con
        if (person.getGeneration() != null) {
            child.setGeneration(person.getGeneration() + 1);
        }

        // xác định fid/mid theo gender
        if (person.getGender() == Gender.MALE) {
            child.setFather(person);
            if (partner != null) child.setMother(partner);
        } else {
            child.setMother(person);
            if (partner != null) child.setFather(partner);
        }

        child = personRepo.save(child);

        Long offset = getOffset(person);
        return toNode(child, Collections.emptyList(), true, offset);
    }

    /**
     * Xóa person.
     * Chỉ được xóa nếu không có con (không ai có father/mother = personId).
     * Soft delete: set deletedAt = now().
     * Xóa luôn các PersonRelationship liên quan.
     */
    @Override
    @Transactional
    public void deletePerson(Long personId) {
        Person person = personRepo.findById(personId)
                .orElseThrow(() -> new AppException(ErrorCode.PERSON_NOT_FOUND));

        if (personRepo.existsByFather_PersonId(personId) || personRepo.existsByMother_PersonId(personId)) {
            throw new AppException(ErrorCode.PERSON_HAS_CHILDREN_CANNOT_DELETE);
        }

        relationshipRepo.deleteAllByPerson_PersonIdOrPartner_PersonId(personId, personId);
        personRepo.delete(person);
    }

    // ==================== helpers ====================

    private Long getOffset(Person person) {
        if (person.getFamilyCategory() == null) return 1L;
        Long offset = person.getFamilyCategory().getGenerationOffset();
        return offset != null ? offset : 1L;
    }

    private FamilyTreeNodeRes toNode(Person p, List<String> pids, boolean isInFamily, Long offset) {
        String avatarUrl = p.getAvatarPath() != null ? minioService.getPresignedUrl(p.getAvatarPath()) : null;
        Long displayGen = p.getGeneration() != null ? p.getGeneration() + offset : null;

        return FamilyTreeNodeRes.builder()
                .id(p.getPersonId().toString())
                .fid(p.getFather() != null ? p.getFather().getPersonId().toString() : null)
                .mid(p.getMother() != null ? p.getMother().getPersonId().toString() : null)
                .pids(pids)
                .generation(displayGen != null ? displayGen.toString() : null)
                .personName(p.getFullName())
                .gender(p.getGender() != null ? p.getGender().name().toLowerCase() : null)
                .birthDate(p.getBirthDate() != null ? p.getBirthDate().toString() : null)
                .deathDate(p.getDeathDate() != null ? p.getDeathDate().toString() : null)
                .lifeStatus(p.getLifeStatus() != null ? p.getLifeStatus().name() : null)
                .originPlace(p.getOriginPlace())
                .placeOfResidence(p.getPlaceOfResidence())
                .avatarPath(p.getAvatarPath())
                .avatarUrl(avatarUrl)
                .isInFamily(isInFamily)
                .build();
    }

    private Account getCurrentAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return accountRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
    }
}
