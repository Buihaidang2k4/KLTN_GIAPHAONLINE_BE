package com.codewithdang.kltn_giaphaonline.service.family;

import com.codewithdang.kltn_giaphaonline.dto.request.FamilyReq;
import com.codewithdang.kltn_giaphaonline.dto.response.FamilyRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface FamilyService {
    FamilyRes createFamily(FamilyReq req);

    PageResponse<FamilyRes> getFamilies(Pageable pageable);

    FamilyRes getFamilyById(Long familyId);

    void deleteFamilyById(Long familyId);

    PageResponse<FamilyRes> getFamiliesByCurrentAccount(Pageable pageable);
}
