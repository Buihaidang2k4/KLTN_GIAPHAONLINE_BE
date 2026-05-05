package com.codewithdang.kltn_giaphaonline.service.album;

import com.codewithdang.kltn_giaphaonline.dto.request.AlbumReq;
import com.codewithdang.kltn_giaphaonline.dto.response.AlbumRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;


public interface AlbumService {
    AlbumRes createAlbum(Long familyId, AlbumReq albumReq);

    AlbumRes updateAlbumById(Long albumId, AlbumReq albumReq);

    void deleteAlbumById(Long albumId);

    PageResponse<AlbumRes> getAlbumByFamilyId(Long familyId, String keyword, Pageable pageable);

    AlbumRes getAlbumById(Long albumId);
}
