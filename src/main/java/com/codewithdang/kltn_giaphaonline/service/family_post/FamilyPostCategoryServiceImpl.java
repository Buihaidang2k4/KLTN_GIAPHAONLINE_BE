package com.codewithdang.kltn_giaphaonline.service.family_post;

import com.codewithdang.kltn_giaphaonline.dto.request.PostCategoryReq;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyPostCategoryRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.entity.Family;
import com.codewithdang.kltn_giaphaonline.entity.FamilyPostCategory;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.mapper.FamilyPostCategoryMapper;
import com.codewithdang.kltn_giaphaonline.mapper.PageMapper;
import com.codewithdang.kltn_giaphaonline.repo.FamilyPostCategoryRepo;
import com.codewithdang.kltn_giaphaonline.repo.FamilyRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FamilyPostCategoryServiceImpl implements FamilyPostCategoryService {
    FamilyPostCategoryRepo postCategoryRepo;
    FamilyPostCategoryMapper postCategoryMapper;
    PageMapper pageMapper;
    FamilyRepo familyRepo;

    @Override
    @Transactional
    public FamilyPostCategoryRes createPostCategory(Long familyId, PostCategoryReq req) {
        Family family = familyRepo.findById(familyId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_NOT_EXISTED));

        if (postCategoryRepo.existsByFamily_FamilyIdAndName(familyId, req.getName()))
            throw new AppException(ErrorCode.FAMILY_POST_CATEGORY_IS_EXISTED);
        FamilyPostCategory familyPostCategory = postCategoryMapper.toEntity(req);
        familyPostCategory.setFamily(family);

        familyPostCategory = postCategoryRepo.save(familyPostCategory);
        return postCategoryMapper.toDto(familyPostCategory);
    }

    @Override
    @Transactional
    public FamilyPostCategoryRes updatePostCategory(Long familyId, Long postCategoryId, PostCategoryReq req) {
        FamilyPostCategory familyPostCategory =
                postCategoryRepo.findByFamily_FamilyIdAndCategoryId(familyId, postCategoryId)
                        .orElseThrow(() -> new AppException(ErrorCode.FAMILY_POST_CATEGORY_NOT_EXISTED));

        postCategoryMapper.updatePostCategory(req, familyPostCategory);
        familyPostCategory = postCategoryRepo.save(familyPostCategory);
        return postCategoryMapper.toDto(familyPostCategory);
    }

    @Override
    @Transactional
    public void deletePostCategory(Long familyId, Long postCategoryId) {
        FamilyPostCategory postCategory = postCategoryRepo
                .findByFamily_FamilyIdAndCategoryId(familyId, postCategoryId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_POST_CATEGORY_NOT_EXISTED));

        postCategoryRepo.delete(postCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<FamilyPostCategoryRes> getPostCategoriesByFamily(Long familyId, String keyword, Pageable pageable) {
        Page<FamilyPostCategory> postCategories = postCategoryRepo.findAllByFamily_FamilyIdAndKeyword(familyId, keyword, pageable);
        return pageMapper.toPageResponse(postCategories, postCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<FamilyPostCategoryRes> getPostCategories(Pageable pageable) {
        Page<FamilyPostCategory> postCategories = postCategoryRepo.findAll(pageable);
        return pageMapper.toPageResponse(postCategories, postCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public FamilyPostCategoryRes getPostCategoryById(Long postCategoryId) {
        FamilyPostCategory postCategory = postCategoryRepo.findById(postCategoryId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_POST_CATEGORY_NOT_EXISTED));

        return postCategoryMapper.toDto(postCategory);
    }
}
