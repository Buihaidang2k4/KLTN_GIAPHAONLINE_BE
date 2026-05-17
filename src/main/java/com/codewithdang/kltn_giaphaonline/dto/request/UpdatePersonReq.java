package com.codewithdang.kltn_giaphaonline.dto.request;

import com.codewithdang.kltn_giaphaonline.enums.Gender;
import com.codewithdang.kltn_giaphaonline.enums.LifeStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdatePersonReq {
    String fullName;
    Gender gender;
    String phoneNumber;
    LocalDate birthDate;
    LocalDate deathDate;
    String originPlace;
    String placeOfResidence;
    String graveLocation;
    LifeStatus lifeStatus;
    String biography;
    String slug;
    MultipartFile avatar; // file ảnh đại diện, null nếu không cập nhật
}
