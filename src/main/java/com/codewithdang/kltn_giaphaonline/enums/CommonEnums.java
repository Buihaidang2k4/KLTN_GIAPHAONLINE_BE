package com.codewithdang.kltn_giaphaonline.enums;

import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@Getter
public class CommonEnums {

    public interface BaseEnum {
        int getCode();

        static <E extends Enum<E> & BaseEnum> E fromCode(Class<E> enumClass, Integer code) {
            return Arrays.stream(enumClass.getEnumConstants())
                    .filter(e -> Objects.equals(e.getCode(), code))
                    .findFirst()
                    .orElseThrow(() ->
                            new AppException(ErrorCode.RESOURCE_NOT_FOUND)
                    );
        }
    }

    @Getter
    public enum Operator {
        CREATE("Thêm mới"),
        READ("Xem"),
        UPDATE("Cập nhật"),
        DELETE("Xoá"),
        CANCEL("Huỷ"),
        IMPORT("Tải lên file"),
        VOID("Doing nothing"),
        DOWNLOAD("Tải xuống file"),
        SYNC("Đồng bộ dữ liệu"),
        EXPORT("Xuất file");

        private final String description;

        Operator(String description) {
            this.description = description;
        }
    }

    @Getter
    @RequiredArgsConstructor
    public enum ActiveStatus implements BaseEnum {
        IN_ACTIVE(0, "Ngừng hoạt động"),
        ACTIVE(1, "Đang hoạt động");

        private final int code;
        private final String description;
    }

  
}
