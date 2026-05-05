package com.codewithdang.kltn_giaphaonline.entity;


import com.codewithdang.kltn_giaphaonline.enums.MediaType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "album_media")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AlbumMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_media_id")
    Long albumMediaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false)
    Album album;

    @Column(name = "title", length = 255, nullable = false)
    String title;

    @Column(name = "description", length = 500)
    String description;

    @Column(name = "media_path", length = 500, nullable = false)
    String mediaPath;

    @Column(name = "thumbnail_path", length = 500)
    String thumbnailPath;

    @Column(name = "mime_type", length = 100)
    String mimeType;

    @Column(name = "file_size_bytes")
    Long fileSizeBytes;

    @Enumerated(EnumType.STRING)
    @Column(name = "media_type", length = 20, nullable = false)
    MediaType mediaType;

    @CreationTimestamp
    @Column(name = "created_at")
    Instant createdAt;
}