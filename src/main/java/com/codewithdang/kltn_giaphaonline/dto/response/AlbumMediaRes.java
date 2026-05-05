package com.codewithdang.kltn_giaphaonline.dto.response;

import com.codewithdang.kltn_giaphaonline.enums.MediaType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumMediaRes {
    Long albumMediaId;
    Long albumId;
    java.lang.String title;
    java.lang.String description;
    java.lang.String mediaPath;
    java.lang.String mediaUrl;
    java.lang.String thumbnailPath;
    java.lang.String thumbnailUrl;
    java.lang.String mimeType;
    Long fileSizeBytes;
    MediaType mediaType;
}
