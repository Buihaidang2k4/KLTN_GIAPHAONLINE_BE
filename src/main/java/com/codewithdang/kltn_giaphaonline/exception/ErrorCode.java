package com.codewithdang.kltn_giaphaonline.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import static org.springframework.http.HttpStatus.*;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    // --- SYSTEM & AUTHENTICATION (9000 - 9999) ---
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", INTERNAL_SERVER_ERROR),
    INVALID_KEY(9000, "Invalid message key", BAD_REQUEST),
    UNAUTHENTICATED(9001, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(9002, "You do not have permission to access this resource", FORBIDDEN),
    TOKEN_EXPIRED(9003, "Token has expired", UNAUTHORIZED.getStatusCode()),
    TOKEN_REVOKED(9004, "Token has been revoked or is no longer valid", HttpStatus.UNAUTHORIZED),
    ROLE_NOT_ALLOWED(9005, "Your role is not allowed to perform this action", FORBIDDEN),
    DATABASE_ERROR(9006, "Database failed toEmail check backlist for token ", INTERNAL_SERVER_ERROR),
    REFRESH_TOKEN_NOT_EXIST_IN_COOKIES(9007, "Refresh token is null when get from cookies ", BAD_REQUEST),
    REFRESH_TOKEN_NOT_FOUND(9007, "Refresh token is missing in cookies", BAD_REQUEST),

    // account
    ACCOUNT_EXISTED(1001, "Account already exists", BAD_REQUEST),
    ACCOUNT_NOT_EXISTED(1002, "Account not existed", HttpStatus.NOT_FOUND),
    PASSWORD_NOT_MATCH(1003, "Password does not match", HttpStatus.BAD_REQUEST),
    ACCOUNT_CANNOT_UPDATE_STATUS(1004, "Invalid account status update.", HttpStatus.BAD_REQUEST),
    ACCOUNT_NOT_ACTIVE(1005, "Account is currently not active.", HttpStatus.BAD_REQUEST),
    ACCOUNT_VERIFICATION_TOKEN_NOT_FOUND(1006, "Verification token not found.", HttpStatus.NOT_FOUND),
    ACCOUNT_VERIFICATION_TOKEN_ALREADY_USED(1007, "This token has already been used.", HttpStatus.CONFLICT),
    ACCOUNT_VERIFICATION_TOKEN_EXPIRED(1008, "Verification token has expired.", HttpStatus.GONE),
    ACCOUNT_ALREADY_VERIFIED(1009, "Account is already verified. Please login.", HttpStatus.BAD_REQUEST),
    ACCOUNT_STATUS_IS_NOT_DELETE(1010, "The account status is not DELETED.", HttpStatus.BAD_REQUEST),
    ACCOUNT_STATUS_IS_NOT_ACTIVE(1011, "The account status is not ACTIVE.", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_ALREADY_EXISTS(1012, "Phone number is already registered", HttpStatus.BAD_REQUEST),

    // role
    ROLE_EXISTED(1100, "Role existed", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTED(1101, "Role is not existed", HttpStatus.NOT_FOUND),
    ROLE_IS_ALREADY_USED(1102, "Role is used by account", HttpStatus.CONFLICT),
    ROLE_IS_ALREADY_IN_ACCOUNT(1103, "Role is already assigned toEmail the account", HttpStatus.BAD_REQUEST),
    ROLE_NOT_ASSIGNED_TO_ACCOUNT(1104, "Role is not assigned toEmail the account", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_ASSIGNED_TO_ROLE(1105, "The specified permission is not assigned toEmail this role", HttpStatus.BAD_REQUEST),
    SCOPE_TYPE_IS_NULL(1106, "Scope Type is null", HttpStatus.BAD_REQUEST),
    ROLE_PERMISSION_SCOPE_MISMATCH(1107, "ROLE mis match with PERMISSION by SCOPE_TYPE", HttpStatus.BAD_REQUEST),
    ROLE_IS_NOT_WITHIN_THE_SCOPE_OF_THE_GENEALOGY(1108, "Role is not within THE_SCOPE", HttpStatus.BAD_REQUEST),
    CANNOT_UPDATE_YOUR_OWN_ROLE(1109, "Cannot update your own role", HttpStatus.BAD_REQUEST),

    // permission
    PERMISSION_EXISTED(1200, "Permission existed", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_EXISTED(1201, "Permission not  existed", HttpStatus.NOT_FOUND),
    PERMISSION_IS_ALREADY_USED(1202, "Permission is used by role", HttpStatus.CONFLICT),

    // minio
    INVALID_FILE_TYPE(1306, "Only image files are allowed (jpg, png, jpeg)", HttpStatus.BAD_REQUEST),
    FILE_TOO_LARGE(1307, "File size must not exceed 5MB", HttpStatus.BAD_REQUEST),
    INVALID_VIDEO_TYPE(1308, "Only video files are allowed (mp4, mkv, avi)", HttpStatus.BAD_REQUEST),
    VIDEO_TOO_LARGE(1309, "Video size must not exceed 100MB", HttpStatus.BAD_REQUEST),

    // token
    TOKEN_INVALID(1400, "Token invalid or expired", HttpStatus.BAD_REQUEST),

    // audit
    ACTION_IS_EMPTY(1500, "Audit action must not be blank", HttpStatus.BAD_REQUEST),
    ENTITY_TYPE_IS_EMPTY(1501, "Audit entityType must not be blank", HttpStatus.BAD_REQUEST),

    // article
    ARTICLE_NOT_EXISTED(1600, "Article not found", HttpStatus.NOT_FOUND),
    ARTICLE_CATEGORY_EXISTED(1601, "Article existed", BAD_REQUEST),
    ARTICLE_CATEGORY_SLUG_EXISTED(1602, "Article slug existed", BAD_REQUEST),
    ARTICLE_CATEGORY_ALREADY_HAS_ARTICLES(1603, "Article category already has articles cannot deleted", BAD_REQUEST),


    // family
    FAMILY_NOT_EXISTED(1700, "Family not found", HttpStatus.NOT_FOUND),
    ACCOUNT_ACCESS_DENIED(1701, "Account is not owner family", BAD_REQUEST),
    YOU_ARE_NOT_A_MEMBER_OF_THE_FAMILY(1702, "You are not a member of this Family", HttpStatus.BAD_REQUEST),
    FAMILY_ROLE_IS_NOT_AUTHORITY(1703, "Family role is not authority", HttpStatus.BAD_REQUEST),
    ROLE_ALREADY_ASSIGNED_TO_MEMBER(1704, "Role is already assigned to member", HttpStatus.BAD_REQUEST),
    ACCOUNT_FAMILY_NO_PERMISSION(1705, "Acount", BAD_REQUEST),
    // ceremony
    CEREMONY_NOT_EXISTED(1800, "Ceremony is not found", HttpStatus.NOT_FOUND),

    // family_invitation
    THIS_MEMBER_HAS_ALREADY_RECEIVED_AN_INVITATION(1900, "This member has already received an invitation", BAD_REQUEST),
    CANNOT_INVITE_YOURSELF(1901, "You cannot invite yourself", BAD_REQUEST),
    FAMILY_INVITATION_NOT_EXISTED(1902, "", NOT_FOUND),
    INVITATION_ALREADY_HANDLED(1903, "Invitation already handled and status is not PENDING", BAD_REQUEST),
    INVITATION_EXPIRED(1904, "Invitation expired", HttpStatus.BAD_REQUEST),
    INVALID_INVITATION_RECIPIENT(1905, "The current account's email does not match the invitation's recipient email.", HttpStatus.BAD_REQUEST),

    // family_member
    THIS_ACCOUNT_IS_ALREADY_A_MEMBER_OF_THE_FAMILY(2000, "This account is already a member of the family", BAD_REQUEST),
    FAMILY_MEMBER_STATUS_NOT_ACTIVE(2001, "Family member status is not active", HttpStatus.BAD_REQUEST),
    FAMILY_MEMBER_NOT_EXISTED(2002, "Family member is not existed", HttpStatus.NOT_FOUND),
    CANNOT_REMOVE_YOURSELF(2003, "You cannot remove yourself", HttpStatus.BAD_REQUEST),

    // notification
    NOTIFICATION_NOT_EXISTED(2100, "Notification not found", HttpStatus.NOT_FOUND),
    NOTIFICATION_IS_EXISTED(2101, "Notification is existed", BAD_REQUEST),


    // CEREMONY_TIMELINE
    CEREMONY_TIMELINE_NOT_EXISTED(2200, "The ceremonial milestone does not exist.", NOT_FOUND),
    CEREMONY_TIMELINE_PREPARATION_NOT_EXISTED(2201, "The prepared item does not exist.", NOT_FOUND),
    ;

    final int code;
    final String message;
    final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

}
