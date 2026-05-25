package com.codewithdang.kltn_giaphaonline.service.album;


import com.codewithdang.kltn_giaphaonline.dto.request.CreateAuditLogReq;
import com.codewithdang.kltn_giaphaonline.dto.response.AlbumMediaRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.Album;
import com.codewithdang.kltn_giaphaonline.entity.AlbumMedia;
import com.codewithdang.kltn_giaphaonline.enums.AuditAction;
import com.codewithdang.kltn_giaphaonline.enums.AuditEntityType;
import com.codewithdang.kltn_giaphaonline.enums.MediaType;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.mapper.PageMapper;
import com.codewithdang.kltn_giaphaonline.repo.AccountRepo;
import com.codewithdang.kltn_giaphaonline.repo.AlbumMediaRepo;
import com.codewithdang.kltn_giaphaonline.repo.AlbumRepo;
import com.codewithdang.kltn_giaphaonline.service.audit_log.AuditLogService;
import com.codewithdang.kltn_giaphaonline.service.minio_media.MinioService;
import com.codewithdang.kltn_giaphaonline.service.minio_media.MinioServiceImpl;
import com.codewithdang.kltn_giaphaonline.utils.ConstantUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class AlbumMediaServiceImpl implements AlbumMediaService {
    AlbumRepo albumRepo;
    AlbumMediaRepo albumMediaRepo;
    MinioService minioService;
    PageMapper pageMapper;
    AuditLogService auditLogService;
    AccountRepo accountRepo;

    @Override
    @Transactional
    public AlbumMediaRes uploadMedia(Long albumId, MultipartFile file) throws IOException {
        Album album = albumRepo.findById(albumId)
                .orElseThrow(() -> new AppException(ErrorCode.ALBUM_NOT_FOUND));

        Account currentAccount = getCurrentAccount();

        MediaType mediaType = detectMediaType(file);
        String objectName;
        String thumbnailPath = null;

        switch (mediaType) {
            case IMAGE -> objectName = minioService.uploadImage(file, ConstantUtils.Image);
            case VIDEO -> {
                objectName = minioService.uploadVideo(file, ConstantUtils.Video);
                File tempVideo = File.createTempFile("temp-video-", ".tmp");
                try {
                    file.transferTo(tempVideo);
                    File thumbnail = generateThumbnail(tempVideo);
                    try {
                        try (FileInputStream fis = new FileInputStream(thumbnail)) {
                            MultipartFile thumbMultipart = new MockMultipartFile(
                                    "thumbnail",
                                    thumbnail.getName(),
                                    "image/jpeg",
                                    fis
                            );
                            thumbnailPath = minioService.uploadImage(thumbMultipart, ConstantUtils.VideoThumb);
                        }
                    } finally {
                        // del temp
                        thumbnail.delete();
                    }
                } finally {
                    tempVideo.delete();
                }
            }
            case DOCUMENT -> objectName = minioService.uploadFile(file, ConstantUtils.Docs);
            default -> objectName = minioService.uploadImage(file, ConstantUtils.Others);
        }

        java.lang.String fileName = Optional.ofNullable(file.getOriginalFilename()).orElse("media");

        AlbumMedia albumMedia = AlbumMedia.builder()
                .album(album)
                .title(fileName)
                .mediaPath(objectName)
                .thumbnailPath(thumbnailPath)
                .mimeType(file.getContentType())
                .fileSizeBytes(file.getSize())
                .mediaType(mediaType)
                .build();

        albumMediaRepo.save(albumMedia);

        // update album
        int currentCount = Optional.ofNullable(album.getMediaCount()).orElse(0);
        long currentSize = Optional.ofNullable(album.getTotalSize()).orElse(0L);
        album.setMediaCount(currentCount + 1);
        album.setTotalSize(currentSize + file.getSize());

        // set cover nếu chưa có
        if (album.getCoverPath() == null && mediaType == MediaType.IMAGE) {
            album.setCoverPath(objectName);
        }
        albumRepo.save(album);

        auditLogService.log(CreateAuditLogReq.builder()
                .familyId(album.getFamily().getFamilyId())
                .actorAccountId(currentAccount.getAccountId())
                .auditAction(AuditAction.ALBUM_IMAGE_UPLOAD.getLabel())
                .entityType(AuditEntityType.FAMILY.name())
                .newData(buildMediaDataMap(albumMedia))
                .entityId(albumMedia.getAlbumMediaId().toString())
                .build());

        return toRes(albumMedia);
    }

    @Override
    @Transactional
    public AlbumMediaRes uploadLink(Long albumId, String url, String title) {
        if (url == null || url.isBlank()) throw new AppException(ErrorCode.INVALID_LINK_URL);

        // validate URL format
        try {
            new java.net.URI(url).toURL();
        } catch (Exception e) {
            throw new AppException(ErrorCode.INVALID_URL_FORMAT);
        }

        Album album = albumRepo.findById(albumId)
                .orElseThrow(() -> new AppException(ErrorCode.ALBUM_NOT_FOUND));

        Long fileSize = fetchContentLength(url);

        AlbumMedia albumMedia = AlbumMedia.builder()
                .title(title != null && !title.isBlank() ? title : url)
                .album(album)
                .mediaPath(url)
                .mediaType(MediaType.LINK)
                .fileSizeBytes(fileSize)
                .build();

        albumMediaRepo.save(albumMedia);

        // cập nhật album
        int currentCount = Optional.ofNullable(album.getMediaCount()).orElse(0);
        long currentSize = Optional.ofNullable(album.getTotalSize()).orElse(0L);
        album.setMediaCount(currentCount + 1);
        album.setTotalSize(currentSize + (fileSize != null ? fileSize : 0L));
        albumRepo.save(album);

        return AlbumMediaRes.builder()
                .albumMediaId(albumMedia.getAlbumMediaId())
                .albumId(album.getAlbumId())
                .title(albumMedia.getTitle())
                .mediaPath(url)
                .mediaUrl(url)
                .mediaType(MediaType.LINK)
                .fileSizeBytes(fileSize)
                .build();
    }

    private Long fetchContentLength(String url) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new java.net.URI(url).toURL().openConnection();
            conn.setRequestMethod("HEAD");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.connect();
            long length = conn.getContentLengthLong();
            conn.disconnect();
            return length > 0 ? length : null;
        } catch (Exception e) {
            log.warn("Cannot fetch content length for url: {}", url);
            return null;
        }
    }

    @Override
    @Transactional
    public List<AlbumMediaRes> uploadMultiple(Long albumId, List<MultipartFile> files) {
        return files.stream()
                .map(file -> {
                    try {
                        return uploadMedia(albumId, file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AlbumMediaRes> getAlbumMediaByAlbumId(Long albumId, MediaType mediaType, Pageable pageable) {
        MediaType type = mediaType != null
                ? MediaType.valueOf(mediaType.name())
                : null;

        Page<AlbumMedia> albumMediaPage = albumMediaRepo.searchMedia(albumId, type, pageable);
        return pageMapper.toPageResponse(albumMediaPage, this::toRes);
    }

    @Override
    @Transactional
    public void deleteMedia(Long mediaId) {
        AlbumMedia albumMedia = albumMediaRepo.findById(mediaId)
                .orElseThrow(() -> new AppException(ErrorCode.ALBUM_MEDIA_NOT_FOUND));

        Album album = albumMedia.getAlbum();
        Account currentAccount = getCurrentAccount();
        // xoa minio
        minioService.deleteFile(albumMedia.getMediaPath());
        if (albumMedia.getMediaPath().equals(album.getCoverPath())) {
            album.setCoverPath(null);
        }

        albumMediaRepo.delete(albumMedia);

        int currentCount = Optional.ofNullable(album.getMediaCount()).orElse(0);
        long currentSize = Optional.ofNullable(album.getTotalSize()).orElse(0L);
        long mediaSize = Optional.ofNullable(albumMedia.getFileSizeBytes()).orElse(0L);

        album.setMediaCount(Math.max(0, currentCount - 1));
        album.setTotalSize(Math.max(0L, currentSize - mediaSize));

        albumRepo.save(album);

        auditLogService.log(CreateAuditLogReq.builder()
                .familyId(album.getFamily().getFamilyId())
                .actorAccountId(currentAccount.getAccountId())
                .auditAction(AuditAction.ALBUM_IMAGE_DELETE.getLabel())
                .entityType(AuditEntityType.FAMILY.name())
                .oldData(buildMediaDataMap(albumMedia))
                .entityId(mediaId.toString())
                .build());
    }


    private AlbumMediaRes toRes(AlbumMedia media) {
        String mediaUrl = media.getMediaType() == MediaType.LINK
                ? media.getMediaPath()
                : minioService.getPresignedUrl(media.getMediaPath());

        String thumbnailUrl = media.getThumbnailPath() != null
                ? minioService.getPresignedUrl(media.getThumbnailPath())
                : null;

        return AlbumMediaRes.builder()
                .albumMediaId(media.getAlbumMediaId())
                .albumId(media.getAlbum().getAlbumId())
                .title(media.getTitle())
                .description(media.getDescription())
                .mediaPath(media.getMediaPath())
                .mediaUrl(mediaUrl)
                .thumbnailPath(media.getThumbnailPath())
                .thumbnailUrl(thumbnailUrl)
                .mimeType(media.getMimeType())
                .fileSizeBytes(media.getFileSizeBytes())
                .mediaType(media.getMediaType())
                .build();
    }

    private Map<String, Object> buildMediaDataMap(AlbumMedia media) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("Tiêu đề", String.valueOf(media.getTitle()));
        data.put("Loại", media.getMediaType() != null ? media.getMediaType().name() : null);
        data.put("Kích thước", media.getFileSizeBytes() != null ? media.getFileSizeBytes() + " bytes" : null);
        data.put("Album", media.getAlbum().getTitle());
        return data;
    }

    private MediaType detectMediaType(MultipartFile file) {
        String type = file.getContentType();

        if (type == null) return MediaType.OTHER;

        if (type.startsWith("image/")) return MediaType.IMAGE;
        if (type.startsWith("video/")) return MediaType.VIDEO;
        if (MinioServiceImpl.DOCUMENT.contains(type)) return MediaType.DOCUMENT;

        return MediaType.OTHER;
    }

    public File generateThumbnail(File videoFile) {
        try {
            File thumbnail = File.createTempFile("thumb-", ".jpg");

            try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoFile)) {
                grabber.start();
                // seek đến giây thứ 1
                long durationMicros = grabber.getLengthInTime();
                long seekMicros = Math.min(1_000_000L, durationMicros > 0 ? durationMicros / 2 : 0);
                grabber.setTimestamp(seekMicros);

                Frame frame = null;
                Frame grabbed;
                while ((grabbed = grabber.grabImage()) != null) {
                    frame = grabbed;
                    break;
                }

                if (frame == null) throw new RuntimeException("No frame captured from video");

                try (Java2DFrameConverter converter = new Java2DFrameConverter()) {
                    BufferedImage image = converter.convert(frame);
                    ImageIO.write(image, "jpg", thumbnail);
                }
            }

            return thumbnail;
        } catch (Exception e) {
            throw new RuntimeException("Cannot generate thumbnail", e);
        }
    }

    private Account getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return accountRepo.findByEmail(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
    }
}
