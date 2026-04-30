package com.codewithdang.kltn_giaphaonline.service.ceremony.ceremony_;

import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyReq;
import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyUpdateReq;
import com.codewithdang.kltn_giaphaonline.dto.response.CeremonyRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.Ceremony;
import com.codewithdang.kltn_giaphaonline.entity.Family;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.mapper.CeremonyMapper;
import com.codewithdang.kltn_giaphaonline.mapper.PageMapper;
import com.codewithdang.kltn_giaphaonline.repo.AccountRepo;
import com.codewithdang.kltn_giaphaonline.repo.CeremonyRepo;
import com.codewithdang.kltn_giaphaonline.repo.FamilyRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CeremonyServiceImpl implements CeremonyService {
    CeremonyRepo ceremonyRepo;
    CeremonyMapper ceremonyMapper;
    PageMapper pageMapper;
    FamilyRepo familyRepo;
    AccountRepo accountRepo;

    @Override
    @Transactional
    @PreAuthorize("hasRole('FAMILY_ADMIN') or hasAuthority('CEREMONY_MANAGE')")
    public CeremonyRes createCeremony(Long familyId, CeremonyReq req) {
        Family family = familyRepo.findById(familyId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_NOT_EXISTED));

        Ceremony ceremony = ceremonyMapper.toEntity(req);
        ceremony.setFamily(family);
        return ceremonyMapper.toRes(ceremonyRepo.save(ceremony));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CeremonyRes> getCeremonyList(Pageable pageable) {
        Page<Ceremony> ceremonyPage = ceremonyRepo.findAll(pageable);

        return pageMapper.toPageResponse(
                ceremonyPage,
                ceremonyMapper::toRes
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CeremonyRes> getCeremonyByFamilyId(
            Long familyId,
            String keyword,
            Pageable pageable
    ) {
        Family family = familyRepo.findById(familyId)
                .orElseThrow(() -> new AppException(ErrorCode.FAMILY_NOT_EXISTED));

        Page<Ceremony> ceremonyPage = ceremonyRepo.findAllByFamilyAndKeyword(family, keyword, pageable);

        return pageMapper.toPageResponse(
                ceremonyPage,
                ceremonyMapper::toRes
        );
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('FAMILY_ADMIN') or hasAuthority('CEREMONY_MANAGE')")
    public CeremonyRes updateCeremony(Long ceremonyId, CeremonyUpdateReq req) {
        Ceremony ceremony = ceremonyRepo.findById(ceremonyId)
                .orElseThrow(() -> new AppException(ErrorCode.CEREMONY_NOT_EXISTED));


        ceremonyMapper.updateCeremony(req, ceremony);
        Ceremony updatedCeremony = ceremonyRepo.save(ceremony);
        return ceremonyMapper.toRes(updatedCeremony);
    }

    @Override
    @Transactional(readOnly = true)
    public CeremonyRes getCeremonyById(Long ceremonyId) {
        Ceremony ceremony = ceremonyRepo.findById(ceremonyId).orElseThrow(
                () -> new AppException(ErrorCode.CEREMONY_NOT_EXISTED)
        );
        return ceremonyMapper.toRes(ceremony);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('FAMILY_ADMIN') or hasAuthority('CEREMONY_MANAGE')")
    public void deleteCeremonyById(Long ceremonyId) {
        Ceremony ceremony = ceremonyRepo.findById(ceremonyId).orElseThrow(
                () -> new AppException(ErrorCode.CEREMONY_NOT_EXISTED)
        );

        ceremonyRepo.delete(ceremony);
    }

    private Account getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return accountRepo.findByEmail(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
    }
}
