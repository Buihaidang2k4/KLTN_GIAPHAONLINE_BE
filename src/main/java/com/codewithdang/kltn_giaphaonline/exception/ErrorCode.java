package com.codewithdang.kltn_giaphaonline.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(9000, "Uncategorized error", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(9001, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(9002, "You do not have permission", HttpStatus.FORBIDDEN),              // 403
    TOKEN_EXPIRED(9003, "Token expired", HttpStatus.UNAUTHORIZED),
    TOKEN_REVOKED(9004, "Token has been revoked or is no longer valid", HttpStatus.UNAUTHORIZED),
    ROLE_NOT_ALLOWED(9005, "Role is not allowed for this action", HttpStatus.FORBIDDEN),
    DATABASE_ERROR(9006, "Database failed toEmail check backlist for token ", INTERNAL_SERVER_ERROR),
    REFRESH_TOKEN_NOT_EXIST_IN_COOKIES(9007, "Refresh token is null when get from cookies ", BAD_REQUEST),


    // account
    ACCOUNT_EXISTED(1001, "Account existed", HttpStatus.BAD_REQUEST),
    ACCOUNT_NOT_EXISTED(1002, "Account not existed", HttpStatus.NOT_FOUND),
    PASSWORD_NOT_MATCH(1003, "Password not match", HttpStatus.BAD_REQUEST),
    ACCOUNT_CANNOT_UPDATE_STATUS(1004, "Invalid status update.", HttpStatus.BAD_REQUEST),
    ACCOUNT_NOT_ACTIVE(1005, "Account is not active.", HttpStatus.BAD_REQUEST),

    // role
    ROLE_EXISTED(1100, "Role existed", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTED(1101, "Role is not existed", HttpStatus.NOT_FOUND),
    ROLE_IS_ALREADY_USED(1102, "Role is used by account", HttpStatus.CONFLICT),
    ROLE_IS_ALREADY_IN_ACCOUNT(1103, "Role is already assigned toEmail the account", HttpStatus.BAD_REQUEST),
    ROLE_NOT_ASSIGNED_TO_ACCOUNT(1104, "Role is not assigned toEmail the account", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_ASSIGNED_TO_ROLE(1105, "The specified permission is not assigned toEmail this role", HttpStatus.BAD_REQUEST),


    // permission
    PERMISSION_EXISTED(1200, "Permission existed", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_EXISTED(1201, "Permission not  existed", HttpStatus.BAD_REQUEST),
    PERMISSION_IS_ALREADY_USED(1202, "Permission is used by role", HttpStatus.CONFLICT),

    // minio
    INVALID_FILE_TYPE(1306, "Only image files are allowed (jpg, png, jpeg)", HttpStatus.BAD_REQUEST),
    FILE_TOO_LARGE(1307, "File size must not exceed 5MB", HttpStatus.BAD_REQUEST),
    INVALID_VIDEO_TYPE(1308, "Only video files are allowed (mp4, mkv, avi)", HttpStatus.BAD_REQUEST),
    VIDEO_TOO_LARGE(1309, "Video size must not exceed 100MB", HttpStatus.BAD_REQUEST),

    ;


    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
