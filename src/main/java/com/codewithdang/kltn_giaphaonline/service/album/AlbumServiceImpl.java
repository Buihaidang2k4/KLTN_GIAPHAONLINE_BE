package com.codewithdang.kltn_giaphaonline.service.album;

import com.codewithdang.kltn_giaphaonline.dto.request.AlbumReq;
import com.codewithdang.kltn_giaphaonline.dto.request.CreateAuditLogReq;
import com.codewithdang.kltn_giaphaonline.dto.response.AlbumRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.Album;
import com.codewithdang.kltn_giaphaonline.entity.Family;
import com.codewithdang.kltn_giaphaonline.enums.AuditAction;
import com.codewithdang.kltn_giaphaonline.enums.AuditEntityType;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.mapper.AlbumMapper;
import com.codewithdang.kltn_giaphaonline.mapper.PageMapper;
import com.codewithdang.kltn_giaphaonline.repo.AccountRepo;
import com.codewithdang.kltn_giaphaonline.repo.AlbumRepo;
import com.codewithdang.kltn_giaphaonline.repo.FamilyRepo;
import com.codewithdang.kltn_giaphaonline.service.audit_log.AuditLogService;
import com.codewithdang.kltn_giaphaonline.service.minio_media.MinioService;
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

import java.util.Map;
import java.util.UUID;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class AlbumServiceImpl implements AlbumService {
    AlbumRepo albumRepo;
    AlbumMapper albumMapper;
    AccountRepo accountRepo;
    FamilyRepo familyRepo;
    PageMapper pageMapper;
    MinioService minioService;
    AuditLogService auditLogService;

    @Override
    @Transactional
    public AlbumRes createAlbum(Long familyId, AlbumReq albumReq) {
        Family family = familyRepo.findById(familyId).orElseThrow(() -> new AppException(ErrorCode.FAMILY_NOT_EXISTED));

        Account account = getCurrentAccount();
        Album album = albumMapper.toEntity(albumReq);
        album.setFamily(family);
        album.setCreatedByAccount(account);
        album.setSlug(generateSlug(albumReq.getTitle()));
        albumRepo.save(album);

        auditLogService.log(CreateAuditLogReq.builder()
                .familyId(familyId)
                .actorAccountId(account.getAccountId())
                .auditAction(AuditAction.ALBUM_CREATE.getLabel())
                .entityType(AuditEntityType.FAMILY.name())
                .newData(Map.of("Tên album", String.valueOf(album.getTitle())))
                .entityId(album.getAlbumId().toString())
                .build());

        return toResAlbum(album);
    }

    @Override
    @Transactional
    public AlbumRes updateAlbumById(Long albumId, AlbumReq albumReq) {
        Album album = albumRepo.findById(albumId)
                .orElseThrow(() -> new AppException(ErrorCode.ALBUM_NOT_FOUND));

        String oldTitle = album.getTitle();
        albumMapper.updateEntityFromRequest(albumReq, album);
        albumRepo.save(album);

        Account account = getCurrentAccount();
        auditLogService.log(CreateAuditLogReq.builder()
                .familyId(album.getFamily().getFamilyId())
                .actorAccountId(account.getAccountId())
                .auditAction(AuditAction.ALBUM_UPDATE.getLabel())
                .entityType(AuditEntityType.FAMILY.name())
                .oldData(Map.of("Tên album", String.valueOf(oldTitle)))
                .newData(Map.of("Tên album", String.valueOf(album.getTitle())))
                .entityId(albumId.toString())
                .build());

        return toResAlbum(album);
    }

    @Override
    @Transactional
    public void deleteAlbumById(Long albumId) {
        Album album = albumRepo.findById(albumId)
                .orElseThrow(() -> new AppException(ErrorCode.ALBUM_NOT_FOUND));

        Account account = getCurrentAccount();
        Long familyId = album.getFamily().getFamilyId();
        String title = album.getTitle();

        albumRepo.delete(album);

        if (album.getCoverPath() != null) {
            minioService.deleteFile(album.getCoverPath());
        }

        auditLogService.log(CreateAuditLogReq.builder()
                .familyId(familyId)
                .actorAccountId(account.getAccountId())
                .auditAction(AuditAction.ALBUM_DELETE.getLabel())
                .entityType(AuditEntityType.FAMILY.name())
                .oldData(Map.of("Tên album", String.valueOf(title)))
                .entityId(albumId.toString())
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AlbumRes> getAlbumByFamilyId(Long familyId, String keyword, Pageable pageable) {
        Page<Album> albumPage = albumRepo.findAlbumsByFamily_FamilyIdAndTitleContainingIgnoreCase(familyId, keyword, pageable);
        return pageMapper.toPageResponse(albumPage, this::toResAlbum);
    }

    @Override
    @Transactional(readOnly = true)
    public AlbumRes getAlbumById(Long albumId) {
        return albumRepo.findById(albumId).map(this::toResAlbum)
                .orElseThrow(() -> new AppException(ErrorCode.ALBUM_NOT_FOUND));
    }

    private Account getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return accountRepo.findByEmail(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
    }

    private String generateSlug(String title) {
        return title.toLowerCase().replaceAll("\\s+", "-") + "-" + UUID.randomUUID().toString().substring(0, 6);
    }

    private AlbumRes toResAlbum(Album album) {
        AlbumRes albumRes = albumMapper.toRes(album);

        if (album.getCoverPath() != null) {
            String coverUrl = minioService.getPresignedUrl(albumRes.getCoverPath());
            albumRes.setCoverUrl(coverUrl);
        }
        return albumRes;
    }
}
