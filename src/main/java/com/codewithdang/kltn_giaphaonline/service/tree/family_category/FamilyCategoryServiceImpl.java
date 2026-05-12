package com.codewithdang.kltn_giaphaonline.service.tree.family_category;

import com.codewithdang.kltn_giaphaonline.dto.request.FamilyCategoryReq;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyCategoryRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.Family;
import com.codewithdang.kltn_giaphaonline.entity.FamilyCategory;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.mapper.FamilyCategoryMapper;
import com.codewithdang.kltn_giaphaonline.mapper.PageMapper;
import com.codewithdang.kltn_giaphaonline.repo.AccountRepo;
import com.codewithdang.kltn_giaphaonline.repo.FamilyCategoryRepo;
import com.codewithdang.kltn_giaphaonline.repo.FamilyRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public FamilyCategoryRes createFamilyCategory(Long familyId, FamilyCategoryReq req) {
        Family family = familyRepo.findById(familyId).orElseThrow(() -> new AppException(ErrorCode.FAMILY_NOT_EXISTED));
        Account currentAccount = getCurrentAccount();
        FamilyCategory familyCategory = categoryMapper.toEntity(req);
        familyCategory.setFamily(family);
        familyCategory.setOwner(currentAccount);
        familyCategoryRepo.save(familyCategory);
        return categoryMapper.toRes(familyCategory);
    }

    @Override
    @Transactional
    public FamilyCategoryRes updateFamilyCategory(Long categoryId, FamilyCategoryReq req) {
        FamilyCategory category = familyCategoryRepo.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_CATEGORY_NOT_EXISTED));

        categoryMapper.updateEntityFromRequest(req, category);
        familyCategoryRepo.save(category);
        return categoryMapper.toRes(category);
    }

    @Override
    @Transactional
    public void deleteFamilyCategory(Long id) {
        if (!familyCategoryRepo.existsById(id)) {
            throw new AppException(ErrorCode.FAMILY_CATEGORY_NOT_EXISTED);
        }
        familyCategoryRepo.deleteById(id);
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
