package com.codewithdang.kltn_giaphaonline.service.album;

import com.codewithdang.kltn_giaphaonline.dto.response.AlbumMediaRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.enums.MediaType;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AlbumMediaService {
    AlbumMediaRes uploadMedia(Long albumId, MultipartFile file) throws IOException;

    AlbumMediaRes uploadLink(Long albumId, String url, String title);


    List<AlbumMediaRes> uploadMultiple(Long albumId, List<MultipartFile> files);

    PageResponse<AlbumMediaRes> getAlbumMediaByAlbumId(Long albumId, MediaType mediaType, Pageable pageable);

    void deleteMedia(Long mediaId);
}
