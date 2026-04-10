package com.codewithdang.kltn_giaphaonline.service.family;


import com.codewithdang.kltn_giaphaonline.dto.request.FamilyReq;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.Family;
import com.codewithdang.kltn_giaphaonline.enums.RoleEnums;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.mapper.FamilyMapper;
import com.codewithdang.kltn_giaphaonline.mapper.PageMapper;
import com.codewithdang.kltn_giaphaonline.repo.AccountRepo;
import com.codewithdang.kltn_giaphaonline.repo.FamilyRepo;
import com.codewithdang.kltn_giaphaonline.service.family_member.FamilyMemberService;
import com.codewithdang.kltn_giaphaonline.utils.SlugUtil;
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
public class FamilyServiceImpl implements FamilyService {
    FamilyRepo familyRepo;
    FamilyMapper familyMapper;
    PageMapper pageMapper;
    FamilyMemberService familyMemberService;
    AccountRepo accountRepo;

    @Override
    @Transactional
    public FamilyRes createFamily(FamilyReq req) {
        Account account = accountRepo.findById(req.getOwnerAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        Family family = familyMapper.toEntity(req);
        String slug = SlugUtil.toSlugFamily(req.getFamilyName());
        family.setSlug(slug);
        family.setOwner(account);
        
        family = familyRepo.save(family);
        // create role member admin
        familyMemberService.addMember(
                family.getFamilyId(),
                account.getAccountId(),
                RoleEnums.FAMILY_ADMIN.name()
        );

        return familyMapper.toRes(family);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<FamilyRes> getFamilies(Pageable pageable) {
        Page<Family> familyPage = familyRepo.findAll(pageable);
        return pageMapper.toPageResponse(
                familyPage,
                familyMapper::toRes);
    }

    @Transactional(readOnly = true)
    @Override
    public FamilyRes getFamilyById(Long familyId) {
        Family family = familyRepo.findById(familyId).orElseThrow(() -> new AppException(ErrorCode.FAMILY_NOT_EXISTED));
        return familyMapper.toRes(family);
    }

    @Override
    @Transactional
    public void deleteFamilyById(Long familyId) {
        Family family = familyRepo.findById(familyId).orElseThrow(() -> new AppException(ErrorCode.FAMILY_NOT_EXISTED));
        familyRepo.delete(family);
    }


}
