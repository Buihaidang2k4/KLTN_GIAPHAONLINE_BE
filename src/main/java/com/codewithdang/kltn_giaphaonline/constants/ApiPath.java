package com.codewithdang.kltn_giaphaonline.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiPath {

    public static final String PREFIX = "${api.prefix}";

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Auth {
        public static final String BASE = PREFIX + "/auth";
        public static final String LOGIN = "/login";
        public static final String REGISTER = "/register";
        public static final String REGISTER_BY_INVITATION = "/register-by-invitation/{token}";
        public static final String REFRESH_TOKEN = "/refresh-token";
        public static final String INTROSPECT = "/introspect";
        public static final String LOGOUT = "/logout";
        public static final String VERIFY_ACCOUNT = "/verify-account/{token-verify}";
        public static final String RESEND_TOKEN_VERIFY = "/re-send-token-verify/{email}";
        public static final String FORGOT_PASSWORD_SEND_OTP = "/forgot-password-send-otp/{email}";
        public static final String FORGOT_PASSWORD_RESEND_OTP = "/forgot-password-resend-otp/{email}";
        public static final String VERIFY_FORGOT_PASSWORD_OTP = "/verify-forgot-password-otp/{otp}";
        public static final String RESET_PASSWORD = "/reset-password";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Account {
        public static final String BASE = PREFIX + "/accounts";
        public static final String BY_ID = "/{accountId}";
        public static final String MY_INFO = "/MyInfo";
        public static final String CHANGE_PASSWORD = "/change-pass/{accountId}";
        public static final String CHANGE_STATUS_LOCK = "/change-status-lock/{accountId}";
        public static final String CHANGE_AVATAR = "/change-avatar";
        public static final String ADD_ROLE = "/add-role-to-account";
        public static final String REMOVE_ROLE = "/remove-role-from-account";
        public static final String SOFT_DELETE = "/soft-delete/{accountId}";
        public static final String HARD_DELETE = "/hard-delete/{accountId}";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Album {
        public static final String BASE = PREFIX + "/albums";
        public static final String BY_ID = "/{albumId}";
        public static final String BY_FAMILY = "/family/{familyId}";
        public static final String MEDIA = "/{albumId}/media";
        public static final String MEDIA_MULTIPLE = "/{albumId}/media/multiple";
        public static final String MEDIA_LINK = "/{albumId}/media/link";
        public static final String MEDIA_DELETE = "/media/{mediaId}";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ArticleCategory {
        public static final String BASE = PREFIX + "/article-categories";
        public static final String BY_ID = "/{articleCategoryId}";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Article {
        public static final String BASE = PREFIX + "/articles";
        public static final String BY_ID = "/{articleId}";
        public static final String BY_SLUG = "/slug/{slug}";
        public static final String PUBLISH = "/{articleId}/publish";
        public static final String UNPUBLISH = "/{articleId}/unpublish";
        public static final String TOGGLE_FEATURED = "/{articleId}/toggle-featured";
        public static final String UPLOAD_IMAGE = "/upload-image";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class AuditLog {
        public static final String BASE = PREFIX + "/audit-logs";
        public static final String BY_FAMILY = "/family/{familyId}";
        public static final String BY_ENTITY = "/entity";
        public static final String BY_ACTOR = "/actor/{accountId}";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Ceremony {
        public static final String BASE = PREFIX + "/ceremonies";
        public static final String BY_ID = "/{ceremonyId}";
        public static final String BY_FAMILY = "/family/{familyId}";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class CeremonyTimeline {
        public static final String BASE = PREFIX + "/ceremony-timelines";
        public static final String BY_ID = "/{timelineId}";
        public static final String BY_CEREMONY = "/ceremony/{ceremonyId}";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class CeremonyTimelinePreparation {
        public static final String BASE = PREFIX + "/ceremony-timeline-preparations";
        public static final String BY_ID = "/{preparationId}";
        public static final String BY_TIMELINE = "/timeline/{timelineId}";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Dashboard {
        public static final String BASE = PREFIX + "/dashboard";
        public static final String FAMILY = "/family/{familyId}";
        public static final String SYSTEM = "/system";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Family {
        public static final String BASE = PREFIX + "/families";
        public static final String BY_ID = "/{id}";
        public static final String CURRENT_ACCOUNT = "/current-account";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class FamilyAchievement {
        public static final String BASE = PREFIX + "/families/{familyId}/achievements";
        public static final String BY_ID = "/{achievementId}";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class FamilyCategory {
        public static final String BASE = PREFIX + "/family-categories";
        public static final String BY_ID = "/{categoryId}";
        public static final String BY_FAMILY = "/family/{familyId}";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class FamilyEvent {
        public static final String BASE = PREFIX + "/family-events";
        public static final String BY_ID = "/{eventId}";
        public static final String BY_FAMILY = "/family/{familyId}";
        public static final String FAMILY_EVENT_BY_ID = "/family/{familyId}/event/{eventId}";
        public static final String SEND_REMINDERS = "/send-reminders";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class FamilyInvitation {
        public static final String BASE = PREFIX + "/family-invitations";
        public static final String SENT = "/sent";
        public static final String RECEIVED = "/received";
        public static final String INVITE = "/{familyId}/invite";
        public static final String ACCEPT = "/accept/{token}";
        public static final String REJECT = "/reject/{token}";
        public static final String CANCEL = "/{invitationId}/cancel";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class FamilyMember {
        public static final String BASE = PREFIX + "/families-members";
        public static final String BY_FAMILY = "/{familyId}";
        public static final String UPDATE_ROLE = "/{familyId}/members/{targetAccountId}/role";
        public static final String REMOVE_MEMBER = "/{familyId}/accounts/{targetAccountId}";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class FamilyPostCategory {
        public static final String BASE = PREFIX + "/family-post-categories";
        public static final String BY_ID = "/{categoryId}";
        public static final String ALL = "/all";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class FamilySubscription {
        public static final String BASE = PREFIX + "/family-subscriptions";
        public static final String BY_FAMILY = "/family/{familyId}";
        public static final String USAGE_STORAGE = "/family/{familyId}/usageStorage";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class FamilyTree {
        public static final String BASE = PREFIX + "/family-tree";
        public static final String CATEGORY_TREE = "/categories/{categoryId}/tree";
        public static final String CATEGORY_PERSONS = "/categories/{categoryId}/persons";
        public static final String PERSON_BY_ID = "/persons/{personId}";
        public static final String PERSON_PARTNERS = "/persons/{personId}/partners";
        public static final String PERSON_MOTHERS = "/persons/{fatherId}/mothers";
        public static final String PERSON_ROOT = "/persons/{personId}/root";
        public static final String PERSON_PARTNER = "/persons/{personId}/partner";
        public static final String PERSON_CHILD = "/persons/{personId}/child";
        public static final String PERSON_RELATIONSHIPS = "/persons/{personId}/relationships";
        public static final String RELATIONSHIP_BY_ID = "/relationships/{relationshipId}";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Feedback {
        public static final String BASE = PREFIX + "/feedbacks";
        public static final String BY_ID = "/{id}";
        public static final String BY_FEEDBACK_ID = "/{feedbackId}";
        public static final String HANDLE = "/{feedbackId}/handle";
        public static final String ALL = "/all";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Notification {
        public static final String BASE = PREFIX + "/notifications";
        public static final String BY_ID = "/{notificationId}";
        public static final String MARK_READ = "/{notificationId}/read";
        public static final String MARK_READ_ALL = "/read-all";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Payment {
        public static final String BASE = PREFIX + "/payments";
        public static final String BY_ID = "/{paymentId}";
        public static final String BY_FAMILY = "/family/{familyId}";
        public static final String TRANSACTION_BY_ID = "/transaction/{transactionId}";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class VNPay {
        public static final String BASE = PREFIX + "/payments/vnpay";
        public static final String CALLBACK = "/callback";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Permission {
        public static final String BASE = PREFIX + "/permissions";
        public static final String LIST = "/list";
        public static final String BY_NAME = "/{permissionName}";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Role {
        public static final String BASE = PREFIX + "/roles";
        public static final String BY_NAME = "/{roleName}";
        public static final String ADD_PERMISSION = "/add-permission/{roleName}";
        public static final String REMOVE_PERMISSION = "/remove-permission/{roleName}";
        public static final String ME = "/me";
        public static final String ME_FAMILY = "/me/family/{familyId}";
        public static final String ME_IS_SYSTEM = "/me/is-system";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class SubscriptionPlan {
        public static final String BASE = PREFIX + "/subscription-plans";
        public static final String BY_ID = "/{planId}";
        public static final String ACTIVE = "/active";
        public static final String TOGGLE_ACTIVE = "/{planId}/toggle-active";
    }
}
