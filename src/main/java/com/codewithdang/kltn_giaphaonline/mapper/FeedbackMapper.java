package com.codewithdang.kltn_giaphaonline.mapper;

import com.codewithdang.kltn_giaphaonline.dto.request.AlbumReq;
import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyReq;
import com.codewithdang.kltn_giaphaonline.dto.request.CeremonyUpdateReq;
import com.codewithdang.kltn_giaphaonline.dto.response.AlbumRes;
import com.codewithdang.kltn_giaphaonline.dto.response.CeremonyRes;
import com.codewithdang.kltn_giaphaonline.entity.Album;
import com.codewithdang.kltn_giaphaonline.entity.Ceremony;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {CeremonyTimelineMapper.class})
public interface AlbumMapper {
    Album toEntity(AlbumReq req);

    @Mapping(target = "familyId", source = "family.familyId")
    @Mapping(target = "createdByAccountId", source = "createdByAccount.accountId")
    AlbumRes toRes(Album album);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(AlbumReq req, @MappingTarget Album entity);
}
