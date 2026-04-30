package com.codewithdang.kltn_giaphaonline.service.ceremony.ceremony_;

import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyReq;
import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyUpdateReq;
import com.codewithdang.kltn_giaphaonline.dto.response.CeremonyRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface CeremonyService {
    CeremonyRes createCeremony(Long familyId, CeremonyReq req);

    PageResponse<CeremonyRes> getCeremonyList(Pageable pageable);

    PageResponse<CeremonyRes> getCeremonyByFamilyId(Long familyId, String keyword, Pageable pageable);

    CeremonyRes updateCeremony(Long ceremonyId, CeremonyUpdateReq req);

    CeremonyRes getCeremonyById(Long ceremonyId);

    void deleteCeremonyById(Long ceremonyId);
}
