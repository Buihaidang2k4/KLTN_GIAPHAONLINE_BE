package com.codewithdang.kltn_giaphaonline.service.ceremony.ceremony_;

import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyReq;
import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyUpdateReq;
import com.codewithdang.kltn_giaphaonline.dto.response.CeremonyRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.entity.Ceremony;
import com.codewithdang.kltn_giaphaonline.entity.Family;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.mapper.CeremonyMapper;
import com.codewithdang.kltn_giaphaonline.mapper.PageMapper;
import com.codewithdang.kltn_giaphaonline.repo.CeremonyRepo;
import com.codewithdang.kltn_giaphaonline.repo.FamilyRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    @Transactional
    public CeremonyRes createCeremony(CeremonyReq req) {
        Family family = familyRepo.findById(req.getFamilyId())
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
    @Transactional
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
    public void deleteCeremonyById(Long ceremonyId) {
        Ceremony ceremony = ceremonyRepo.findById(ceremonyId).orElseThrow(
                () -> new AppException(ErrorCode.CEREMONY_NOT_EXISTED)
        );

        ceremonyRepo.delete(ceremony);
    }
}
