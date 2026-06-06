package com.codewithdang.kltn_giaphaonline.service.family;


import com.codewithdang.kltn_giaphaonline.dto.request.FamilyReq;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.Family;
import com.codewithdang.kltn_giaphaonline.entity.FamilyMember;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.mapper.FamilyMapper;
import com.codewithdang.kltn_giaphaonline.mapper.PageMapper;
import com.codewithdang.kltn_giaphaonline.repo.AccountRepo;
import com.codewithdang.kltn_giaphaonline.repo.FamilyMemberRepo;
import com.codewithdang.kltn_giaphaonline.repo.FamilyRepo;
import com.codewithdang.kltn_giaphaonline.service.family_member.FamilyMemberService;
import com.codewithdang.kltn_giaphaonline.utils.SecurityUtils;
import com.codewithdang.kltn_giaphaonline.utils.SlugUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    FamilyMemberRepo familyMemberRepo;
    SecurityUtils securityUtils;

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
        familyMemberService.assignFamilyAdmin(
                family.getFamilyId(),
                account.getAccountId()
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
        Account account = securityUtils.getCurrentAccount();
        boolean isMember = familyMemberRepo.existsByFamilyAndAccount(family, account);
        if (!isMember) throw new AppException(ErrorCode.UNAUTHORIZED);
        return familyMapper.toRes(family);
    }

    @Override
    @Transactional
    public void deleteFamilyById(Long familyId) {
        Family family = familyRepo.findById(familyId).orElseThrow(() -> new AppException(ErrorCode.FAMILY_NOT_EXISTED));
        familyRepo.delete(family);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<FamilyRes> getFamiliesByCurrentAccount(Pageable pageable) {
        Account account = securityUtils.getCurrentAccount();
        log.info("account current: {}", account.getAccountId());
        Page<FamilyMember> familyMembers = familyMemberRepo.findAllByAccount(account, pageable);
        log.info("List family members found for account {}: {}", account.getEmail(), familyMembers.getContent());
        Page<Family> familyPage = familyMembers.map(FamilyMember::getFamily);
        log.info("Found {} families for account {}", familyPage.getTotalElements(), account.getEmail());
        return pageMapper.toPageResponse(
                familyPage,
                familyMapper::toRes);
    }


}
