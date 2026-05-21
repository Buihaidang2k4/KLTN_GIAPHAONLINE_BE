package com.codewithdang.kltn_giaphaonline.service.tree.family_category;

import com.codewithdang.kltn_giaphaonline.dto.request.CreateAuditLogReq;
import com.codewithdang.kltn_giaphaonline.dto.request.FamilyCategoryReq;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyCategoryRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.Family;
import com.codewithdang.kltn_giaphaonline.entity.FamilyCategory;
import com.codewithdang.kltn_giaphaonline.entity.Person;
import com.codewithdang.kltn_giaphaonline.enums.AuditAction;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.mapper.FamilyCategoryMapper;
import com.codewithdang.kltn_giaphaonline.mapper.PageMapper;
import com.codewithdang.kltn_giaphaonline.repo.AccountRepo;
import com.codewithdang.kltn_giaphaonline.repo.FamilyCategoryRepo;
import com.codewithdang.kltn_giaphaonline.repo.FamilyRepo;
import com.codewithdang.kltn_giaphaonline.repo.PersonRelationshipRepo;
import com.codewithdang.kltn_giaphaonline.repo.PersonRepo;
import com.codewithdang.kltn_giaphaonline.service.audit_log.AuditLogService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FamilyCategoryServiceImpl implements FamilyCategoryService {
    FamilyCategoryRepo familyCategoryRepo;
    FamilyRepo familyRepo;
    FamilyCategoryMapper categoryMapper;
    AccountRepo accountRepo;
    PageMapper pageMapper;
    PersonRepo personRepo;
    PersonRelationshipRepo relationshipRepo;
    AuditLogService auditLogService;

    @Override
    @Transactional
    public FamilyCategoryRes createFamilyCategory(Long familyId, FamilyCategoryReq req) {
        Family family = familyRepo.findById(familyId).orElseThrow(() -> new AppException(ErrorCode.FAMILY_NOT_EXISTED));
        Account currentAccount = getCurrentAccount();
        FamilyCategory familyCategory = categoryMapper.toEntity(req);
        familyCategory.setFamily(family);
        familyCategory.setOwner(currentAccount);
        familyCategoryRepo.save(familyCategory);


        auditLogService.log(CreateAuditLogReq.builder()
                .familyId(familyId)
                .actorAccountId(currentAccount.getAccountId())
                .auditAction(AuditAction.FAMILY_CREATE.getLabel())
                .newData(Map.of("Danh mục", familyCategory.getFamilyName()))
                .entityId(familyCategory.getFamilyCategoryId().toString())
                .build());
        return categoryMapper.toRes(familyCategory);
    }

    @Override
    @Transactional
    public FamilyCategoryRes updateFamilyCategory(Long categoryId, FamilyCategoryReq req) {
        FamilyCategory category = familyCategoryRepo.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_CATEGORY_NOT_EXISTED));
        Account currentAccount = getCurrentAccount();
        String oldCategoryName = category.getFamilyName();

        categoryMapper.updateEntityFromRequest(req, category);
        familyCategoryRepo.save(category);


        auditLogService.log(CreateAuditLogReq.builder()
                .familyId(category.getFamily().getFamilyId())
                .actorAccountId(currentAccount.getAccountId())
                .auditAction(AuditAction.FAMILY_UPDATE.getLabel())
                .newData(Map.of("Danh mục", category.getFamilyName()))
                .oldData(Map.of("Danh mục", oldCategoryName))
                .entityId(category.getFamilyCategoryId().toString())
                .build());

        return categoryMapper.toRes(category);
    }

    @Override
    @Transactional
    public void deleteFamilyCategory(Long id) {

        FamilyCategory category = familyCategoryRepo.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.FAMILY_CATEGORY_NOT_EXISTED));
        Account currentAccount = getCurrentAccount();

        // 1. lấy tất cả person trong category
        List<Person> persons = personRepo.findAllByFamilyCategory_FamilyCategoryId(id);

        if (!persons.isEmpty()) {
            List<Long> personIds = persons.stream().map(Person::getPersonId).toList();

            // 2. xóa tất cả relationship liên quan
            relationshipRepo.deleteAllByPersonIds(personIds);

            // 3. gỡ bỏ các FK tự tham chiếu (father, mother, rootPerson) trước khi xóa
            persons.forEach(p -> {
                p.setFather(null);
                p.setMother(null);
                p.setRootPerson(null);
            });
            personRepo.saveAll(persons);

            // 4. xóa tất cả person
            personRepo.deleteAll(persons);
        }

        // 5. xóa category
        familyCategoryRepo.deleteById(id);


        auditLogService.log(CreateAuditLogReq.builder()
                .familyId(category.getFamily().getFamilyId())
                .actorAccountId(currentAccount.getAccountId())
                .auditAction(AuditAction.FAMILY_DELETE.getLabel())
                .newData(Map.of("Danh mục", category.getFamilyName()))
                .entityId(category.getFamilyCategoryId().toString())
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public FamilyCategoryRes getFamilyCategoryById(Long categoryId) {
        return familyCategoryRepo.findById(categoryId)
                .map(categoryMapper::toRes)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_CATEGORY_NOT_EXISTED));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<FamilyCategoryRes> getAllCategoryByFamilyId(Long familyId, Pageable pageable) {
        return pageMapper.toPageResponse(
                familyCategoryRepo.findAllByFamily_FamilyId(familyId, pageable),
                categoryMapper::toRes
        );
    }


    private Account getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return accountRepo.findByEmail(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
    }
}
