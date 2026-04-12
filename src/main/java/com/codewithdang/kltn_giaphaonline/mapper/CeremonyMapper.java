package com.codewithdang.kltn_giaphaonline.mapper;

import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyReq;
import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyUpdateReq;
import com.codewithdang.kltn_giaphaonline.dto.response.CeremonyRes;
import com.codewithdang.kltn_giaphaonline.entity.Ceremony;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CeremonyMapper {
    Ceremony toEntity(CeremonyReq req);

    @Mapping(target = "familyId", source = "family.familyId")
    CeremonyRes toRes(Ceremony ceremony);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCeremony(CeremonyUpdateReq ceremonyRes, @MappingTarget Ceremony ceremony);
}
