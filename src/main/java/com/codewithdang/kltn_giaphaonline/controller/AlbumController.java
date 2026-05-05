package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.dto.request.AlbumReq;
import com.codewithdang.kltn_giaphaonline.dto.response.AlbumMediaRes;
import com.codewithdang.kltn_giaphaonline.dto.response.AlbumRes;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.enums.MediaType;
import com.codewithdang.kltn_giaphaonline.service.album.AlbumMediaService;
import com.codewithdang.kltn_giaphaonline.service.album.AlbumService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/albums")
@RequiredArgsConstructor
@Tag(name = "Album Management")
public class AlbumController {

    private final AlbumService albumService;
    private final AlbumMediaService albumMediaService;

    @PostMapping
    public ResponseEntity<ApiResponse<AlbumRes>> createAlbum(
            @RequestParam Long familyId,
            @Valid @RequestBody AlbumReq request) {
        return ResponseEntity.ok(ApiResponse.success(201, "CREATE_ALBUM_SUCCESS",
                albumService.createAlbum(familyId, request)));
    }

    @PutMapping("/{albumId}")
    public ResponseEntity<ApiResponse<AlbumRes>> updateAlbum(
            @PathVariable Long albumId,
            @Valid @RequestBody AlbumReq request) {
        return ResponseEntity.ok(ApiResponse.success(200, "UPDATE_ALBUM_SUCCESS",
                albumService.updateAlbumById(albumId, request)));
    }

    @DeleteMapping("/{albumId}")
    public ResponseEntity<ApiResponse<Void>> deleteAlbum(@PathVariable Long albumId) {
        albumService.deleteAlbumById(albumId);
        return ResponseEntity.ok(ApiResponse.success(200, "DELETE_ALBUM_SUCCESS", null));
    }

    @GetMapping("/family/{familyId}")
    public ResponseEntity<ApiResponse<PageResponse<AlbumRes>>> getAlbumsByFamily(
            @PathVariable Long familyId,
            @RequestParam(required = false, defaultValue = "") java.lang.String keyword,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(200, "GET_ALBUMS_BY_FAMILY_SUCCESS",
                albumService.getAlbumByFamilyId(familyId, keyword, pageable)));
    }

    @GetMapping("/{albumId}")
    public ResponseEntity<ApiResponse<AlbumRes>> getAlbumById(@PathVariable Long albumId) {
        return ResponseEntity.ok(ApiResponse.success(200, "GET_ALBUM_SUCCESS",
                albumService.getAlbumById(albumId)));
    }

    @PostMapping("/{albumId}/media")
    public ResponseEntity<ApiResponse<AlbumMediaRes>> uploadMedia(
            @PathVariable Long albumId,
            @RequestParam MultipartFile file) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(201, "UPLOAD_MEDIA_SUCCESS",
                albumMediaService.uploadMedia(albumId, file)));
    }

    @PostMapping("/{albumId}/media/multiple")
    public ResponseEntity<ApiResponse<List<AlbumMediaRes>>> uploadMultiple(
            @PathVariable Long albumId,
            @RequestParam List<MultipartFile> files) {
        return ResponseEntity.ok(ApiResponse.success(201, "UPLOAD_MULTIPLE_MEDIA_SUCCESS",
                albumMediaService.uploadMultiple(albumId, files)));
    }

    @GetMapping("/{albumId}/media")
    public ResponseEntity<ApiResponse<PageResponse<AlbumMediaRes>>> getMediaByAlbum(
            @PathVariable Long albumId,
            @RequestParam(required = false, defaultValue = "") MediaType mediaType,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(200, "GET_ALBUM_MEDIA_SUCCESS",
                albumMediaService.getAlbumMediaByAlbumId(albumId, mediaType, pageable)));
    }

    @DeleteMapping("/media/{mediaId}")
    public ResponseEntity<ApiResponse<Void>> deleteMedia(@PathVariable Long mediaId) {
        albumMediaService.deleteMedia(mediaId);
        return ResponseEntity.ok(ApiResponse.success(200, "DELETE_MEDIA_SUCCESS", null));
    }
}
