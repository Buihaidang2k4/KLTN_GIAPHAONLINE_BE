package com.codewithdang.kltn_giaphaonline.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageConstantsEn {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Auth {
        public static final String LOGIN_SUCCESS = "LOGIN_SUCCESS";
        public static final String REGISTER_SUCCESS = "REGISTER_SUCCESS";
        public static final String REGISTER_BY_INVITATION_SUCCESS = "REGISTER_BY_INVITATION_SUCCESS";
        public static final String REFRESH_TOKEN_SUCCESS = "REFRESH_TOKEN_SUCCESS";
        public static final String INTROSPECT_SUCCESS = "INTROSPECT_SUCCESS";
        public static final String LOGOUT_SUCCESS = "LOGOUT_SUCCESS";
        public static final String VERIFY_ACCOUNT_SUCCESS = "VERIFY_ACCOUNT_SUCCESS";
        public static final String RESEND_TOKEN_VERIFY_ACCOUNT_SUCCESS = "RESEND_TOKEN_VERIFY_ACCOUNT_SUCCESS";
        public static final String FORGOT_PASSWORD_SEND_OTP_SUCCESS = "FORGOT_PASSWORD_SEND_OTP_SUCCESS";
        public static final String FORGOT_PASSWORD_RESEND_OTP_SUCCESS = "FORGOT_PASSWORD_RESEND_OTP_SUCCESS";
        public static final String VERIFY_FORGOT_PASSWORD_OTP_HASH_SUCCESS = "VERIFY_FORGOT_PASSWORD_OTP_HASH_SUCCESS";
        public static final String RESET_PASSWORD_SUCCESS = "RESET_PASSWORD_SUCCESS";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Account {
        public static final String GET_ACCOUNTS_SUCCESS = "GET_ACCOUNTS_SUCCESS";
        public static final String GET_ACCOUNT_SUCCESS = "GET_ACCOUNT_SUCCESS";
        public static final String GET_MY_INFO_SUCCESS = "GET_MY_INFO_SUCCESS";
        public static final String CREATE_ACCOUNT_SUCCESS = "CREATE_ACCOUNT_SUCCESS";
        public static final String CHANGE_PASSWORD_SUCCESS = "CHANGE_PASSWORD_SUCCESS";
        public static final String UPDATE_ACCOUNT_SUCCESS = "UPDATE_ACCOUNT_SUCCESS";
        public static final String UPDATE_ACCOUNT_STATUS_SUCCESS = "UPDATE_ACCOUNT_STATUS_SUCCESS";
        public static final String UPDATE_AVATAR_SUCCESS = "UPDATE_AVATAR_SUCCESS";
        public static final String ADD_ROLE_SUCCESS = "ADD_ROLE_SUCCESS";
        public static final String REMOVE_ROLE_SUCCESS = "REMOVE_ROLE_SUCCESS";
        public static final String SOFT_DELETE_ACCOUNT_SUCCESS = "SOFT_DELETE_ACCOUNT_SUCCESS";
        public static final String HARD_DELETE_ACCOUNT_SUCCESS = "HARD_DELETE_ACCOUNT_SUCCESS";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Album {
        public static final String CREATE_ALBUM_SUCCESS = "CREATE_ALBUM_SUCCESS";
        public static final String UPDATE_ALBUM_SUCCESS = "UPDATE_ALBUM_SUCCESS";
        public static final String DELETE_ALBUM_SUCCESS = "DELETE_ALBUM_SUCCESS";
        public static final String GET_ALBUMS_BY_FAMILY_SUCCESS = "GET_ALBUMS_BY_FAMILY_SUCCESS";
        public static final String GET_ALBUM_SUCCESS = "GET_ALBUM_SUCCESS";
        public static final String UPLOAD_MEDIA_SUCCESS = "UPLOAD_MEDIA_SUCCESS";
        public static final String UPLOAD_MULTIPLE_MEDIA_SUCCESS = "UPLOAD_MULTIPLE_MEDIA_SUCCESS";
        public static final String GET_ALBUM_MEDIA_SUCCESS = "GET_ALBUM_MEDIA_SUCCESS";
        public static final String UPLOAD_LINK_SUCCESS = "UPLOAD_LINK_SUCCESS";
        public static final String DELETE_MEDIA_SUCCESS = "DELETE_MEDIA_SUCCESS";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ArticleCategory {
        public static final String CREATE_ARTICLE_CATEGORY_SUCCESS = "CREATE_ARTICLE_CATEGORY_SUCCESS";
        public static final String UPDATE_ARTICLE_CATEGORY_SUCCESS = "UPDATE_ARTICLE_CATEGORY_SUCCESS";
        public static final String DELETE_ARTICLE_CATEGORY_SUCCESS = "DELETE_ARTICLE_CATEGORY_SUCCESS";
        public static final String GET_ARTICLE_CATEGORY_SUCCESS = "GET_ARTICLE_CATEGORY_SUCCESS";
        public static final String GET_ALL_ARTICLE_CATEGORY_SUCCESS = "GET_ALL_ARTICLE_CATEGORY_SUCCESS";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Article {
        public static final String CREATE_ARTICLE_SUCCESS = "CREATE_ARTICLE_SUCCESS";
        public static final String UPDATE_ARTICLE_SUCCESS = "UPDATE_ARTICLE_SUCCESS";
        public static final String DELETE_ARTICLE_SUCCESS = "DELETE_ARTICLE_SUCCESS";
        public static final String GET_ARTICLE_SUCCESS = "GET_ARTICLE_SUCCESS";
        public static final String GET_ALL_ARTICLES_SUCCESS = "GET_ALL_ARTICLES_SUCCESS";
        public static final String PUBLISH_ARTICLE_SUCCESS = "PUBLISH_ARTICLE_SUCCESS";
        public static final String UNPUBLISH_ARTICLE_SUCCESS = "UNPUBLISH_ARTICLE_SUCCESS";
        public static final String TOGGLE_FEATURED_SUCCESS = "TOGGLE_FEATURED_SUCCESS";
        public static final String UPLOAD_IMAGE_SUCCESS = "UPLOAD_IMAGE_SUCCESS";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class AuditLog {
        public static final String GET_LOG_BY_FAMILY_SUCCESS = "GET_LOG_BY_FAMILY_SUCCESS";
        public static final String GET_LOG_BY_ENTITY_SUCCESS = "GET_LOG_BY_ENTITY_SUCCESS";
        public static final String GET_LOG_BY_ACTOR_SUCCESS = "GET_LOG_BY_ACTOR_SUCCESS";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Ceremony {
        public static final String GET_CEREMONY_SUCCESS = "GET_CEREMONY_SUCCESS";
        public static final String GET_CEREMONY_LIST_SUCCESS = "GET_CEREMONY_LIST_SUCCESS";
        public static final String CREATE_CEREMONY_SUCCESS = "CREATE_CEREMONY_SUCCESS";
        public static final String UPDATE_CEREMONY_SUCCESS = "UPDATE_CEREMONY_SUCCESS";
        public static final String DELETE_CEREMONY_SUCCESS = "DELETE_CEREMONY_SUCCESS";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class CeremonyTimeline {
        public static final String CREATE_TIMELINE_SUCCESS = "CREATE_TIMELINE_SUCCESS";
        public static final String GET_TIMELINE_BY_ID_SUCCESS = "GET_TIMELINE_BY_ID_SUCCESS";
        public static final String GET_TIMELINE_BY_CEREMONY_SUCCESS = "GET_TIMELINE_BY_CEREMONY_SUCCESS";
        public static final String GET_ALL_TIMELINE_SUCCESS = "GET_ALL_TIMELINE_SUCCESS";
        public static final String UPDATE_TIMELINE_SUCCESS = "UPDATE_TIMELINE_SUCCESS";
        public static final String DELETE_TIMELINE_SUCCESS = "DELETE_TIMELINE_SUCCESS";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class CeremonyTimelinePreparation {
        public static final String CREATE_PREPARATION_SUCCESS = "CREATE_PREPARATION_SUCCESS";
        public static final String GET_PREPARATION_BY_ID_SUCCESS = "GET_PREPARATION_BY_ID_SUCCESS";
        public static final String GET_PREPARATION_BY_TIMELINE_SUCCESS = "GET_PREPARATION_BY_TIMELINE_SUCCESS";
        public static final String GET_ALL_PREPARATION_SUCCESS = "GET_ALL_PREPARATION_SUCCESS";
        public static final String UPDATE_PREPARATION_SUCCESS = "UPDATE_PREPARATION_SUCCESS";
        public static final String DELETE_PREPARATION_SUCCESS = "DELETE_PREPARATION_SUCCESS";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Dashboard {
        public static final String GET_DASHBOARD_SUCCESS = "GET_DASHBOARD_SUCCESS";
        public static final String GET_DASHBOARD_SYSTEM_SUCCESS = "GET_DASHBOARD_SYSTEM_SUCCESS";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Family {
        public static final String CREATE_FAMILY_SUCCESS = "CREATE_FAMILY_SUCCESS";
        public static final String GET_FAMILY_SUCCESS = "GET_FAMILY_SUCCESS";
        public static final String DELETE_FAMILY_SUCCESS = "DELETE_FAMILY_SUCCESS";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class FamilyAchievement {
        public static final String CREATE_ACHIEVEMENT_SUCCESS = "CREATE_ACHIEVEMENT_SUCCESS";
        public static final String UPDATE_ACHIEVEMENT_SUCCESS = "UPDATE_ACHIEVEMENT_SUCCESS";
        public static final String DELETE_ACHIEVEMENT_SUCCESS = "DELETE_ACHIEVEMENT_SUCCESS";
        public static final String GET_ACHIEVEMENT_SUCCESS = "GET_ACHIEVEMENT_SUCCESS";
        public static final String GET_ACHIEVEMENTS_SUCCESS = "GET_ACHIEVEMENTS_SUCCESS";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class FamilyCategory {
        public static final String CREATE_FAMILY_CATEGORY_SUCCESS = "CREATE_FAMILY_CATEGORY_SUCCESS";
        public static final String UPDATE_FAMILY_CATEGORY_SUCCESS = "UPDATE_FAMILY_CATEGORY_SUCCESS";
        public static final String DELETE_FAMILY_CATEGORY_SUCCESS = "DELETE_FAMILY_CATEGORY_SUCCESS";
        public static final String GET_FAMILY_CATEGORY_BY_ID_SUCCESS = "GET_FAMILY_CATEGORY_BY_ID_SUCCESS";
        public static final String GET_ALL_CATEGORY_BY_FAMILY_ID_SUCCESS = "GET_ALL_CATEGORY_BY_FAMILY_ID_SUCCESS";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class FamilyEvent {
        public static final String CREATE_EVENT_SUCCESS = "CREATE_EVENT_SUCCESS";
        public static final String UPDATE_EVENT_SUCCESS = "UPDATE_EVENT_SUCCESS";
        public static final String DELETE_EVENT_SUCCESS = "DELETE_EVENT_SUCCESS";
        public static final String GET_EVENTS_BY_FAMILY_SUCCESS = "GET_EVENTS_BY_FAMILY_SUCCESS";
        public static final String GET_EVENT_SUCCESS = "GET_EVENT_SUCCESS";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class FamilyInvitation {
        public static final String GET_MY_INVITATION_SENT_SUCCESS = "GET_MY_INVITATION_SENT_SUCCESS";
        public static final String GET_MY_INVITATION_RECEIVED_SUCCESS = "GET_MY_INVITATION_RECEIVED_SUCCESS";
        public static final String INVITE_MEMBER_SUCCESS = "INVITE_MEMBER_SUCCESS";
        public static final String ACCEPT_INVITATION_SUCCESS = "ACCEPT_INVITATION_SUCCESS";
        public static final String REJECT_INVITATION_SUCCESS = "REJECT_INVITATION_SUCCESS";
        public static final String CANCEL_INVITATION_SUCCESS = "CANCEL_INVITATION_SUCCESS";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class FamilyMember {
        public static final String GET_FAMILY_MEMBER_SUCCESS = "GET_FAMILY_MEMBER_SUCCESS";
        public static final String UPDATE_MEMBER_ROLE_SUCCESS = "UPDATE_MEMBER_ROLE_SUCCESS";
        public static final String REMOVE_MEMBER_SUCCESS = "REMOVE_MEMBER_SUCCESS";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class FamilyPostCategory {
        public static final String CREATE_POST_CATEGORY_SUCCESS = "CREATE_POST_CATEGORY_SUCCESS";
        public static final String UPDATE_POST_CATEGORY_SUCCESS = "UPDATE_POST_CATEGORY_SUCCESS";
        public static final String DELETE_POST_CATEGORY_SUCCESS = "DELETE_POST_CATEGORY_SUCCESS";
        public static final String GET_POST_CATEGORY_SUCCESS = "GET_POST_CATEGORY_SUCCESS";
        public static final String GET_POST_CATEGORIES_BY_FAMILY_SUCCESS = "GET_POST_CATEGORIES_BY_FAMILY_SUCCESS";
        public static final String GET_ALL_POST_CATEGORIES_SUCCESS = "GET_ALL_POST_CATEGORIES_SUCCESS";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class FamilySubscription {
        public static final String GET_FAMILY_SUBSCRIPTION_SUCCESS = "GET_FAMILY_SUBSCRIPTION_SUCCESS";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class FamilyTree {
        public static final String GET_FAMILY_TREE_SUCCESS = "GET_FAMILY_TREE_SUCCESS";
        public static final String CREATE_PERSON_SUCCESS = "CREATE_PERSON_SUCCESS";
        public static final String UPDATE_PERSON_SUCCESS = "UPDATE_PERSON_SUCCESS";
        public static final String GET_PERSON_SUCCESS = "GET_PERSON_SUCCESS";
        public static final String DELETE_PERSON_SUCCESS = "DELETE_PERSON_SUCCESS";
        public static final String GET_PARTNERS_SUCCESS = "GET_PARTNERS_SUCCESS";
        public static final String GET_MOTHERS_SUCCESS = "GET_MOTHERS_SUCCESS";
        public static final String ADD_ROOT_SUCCESS = "ADD_ROOT_SUCCESS";
        public static final String ADD_PARTNER_SUCCESS = "ADD_PARTNER_SUCCESS";
        public static final String ADD_CHILD_SUCCESS = "ADD_CHILD_SUCCESS";
        public static final String ADD_RELATIONSHIP_SUCCESS = "ADD_RELATIONSHIP_SUCCESS";
        public static final String REMOVE_RELATIONSHIP_SUCCESS = "REMOVE_RELATIONSHIP_SUCCESS";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Feedback {
        public static final String CREATE_FEEDBACK_SUCCESS = "CREATE_FEEDBACK_SUCCESS";
        public static final String HANDLE_FEEDBACK_SUCCESS = "HANDLE_FEEDBACK_SUCCESS";
        public static final String GET_FEEDBACK_SUCCESS = "GET_FEEDBACK_SUCCESS";
        public static final String GET_FEEDBACK_LIST_SUCCESS = "GET_FEEDBACK_LIST_SUCCESS";
        public static final String DELETE_FEEDBACK_SUCCESS = "DELETE_FEEDBACK_SUCCESS";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Notification {
        public static final String GET_NOTIFICATIONS_SUCCESS = "GET_NOTIFICATIONS_SUCCESS";
        public static final String MARK_AS_READ_SUCCESS = "MARK_AS_READ_SUCCESS";
        public static final String MARK_ALL_AS_READ_SUCCESS = "MARK_ALL_AS_READ_SUCCESS";
        public static final String DELETE_NOTIFICATION_SUCCESS = "DELETE_NOTIFICATION_SUCCESS";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Payment {
        public static final String GET_ALL_PAYMENTS_SUCCESS = "GET_ALL_PAYMENTS_SUCCESS";
        public static final String GET_ALL_PAYMENTS_BY_FAMILY_ID_SUCCESS = "GET_ALL_PAYMENTS_BY_FAMILY_ID_SUCCESS";
        public static final String DELETE_PAYMENT_SUCCESS = "DELETE_PAYMENT_SUCCESS";
        public static final String GET_PAYMENT_BY_TRANSACTION_ID_SUCCESS = "GET_PAYMENT_BY_TRANSACTION_ID_SUCCESS";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class VNPay {
        public static final String CREATE_PAYMENT_SUCCESS = "CREATE_PAYMENT_SUCCESS";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Permission {
        public static final String GET_ALL_PERMISSIONS_SUCCESS = "GET_ALL_PERMISSIONS_SUCCESS";
        public static final String GET_ALL_PERMISSIONS_LIST_SUCCESS = "GET_ALL_PERMISSIONS_LIST_SUCCESS";
        public static final String GET_PERMISSION_SUCCESS = "GET_PERMISSION_SUCCESS";
        public static final String CREATE_PERMISSION_SUCCESS = "CREATE_PERMISSION_SUCCESS";
        public static final String UPDATE_PERMISSION_SUCCESS = "UPDATE_PERMISSION_SUCCESS";
        public static final String DELETE_PERMISSION_SUCCESS = "DELETE_PERMISSION_SUCCESS";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Role {
        public static final String GET_ROLES_SUCCESS = "GET_ROLES_SUCCESS";
        public static final String CREATE_ROLE_SUCCESS = "CREATE_ROLE_SUCCESS";
        public static final String ADD_PERMISSION_TO_ROLE_SUCCESS = "ADD_PERMISSION_TO_ROLE_SUCCESS";
        public static final String REMOVE_PERMISSION_FROM_ROLE_SUCCESS = "REMOVE_PERMISSION_FROM_ROLE_SUCCESS";
        public static final String DELETE_ROLE_SUCCESS = "DELETE_ROLE_SUCCESS";
        public static final String GET_ROLE_BY_CURRENT_ACCOUNT_SUCCESS = "GET_ROLE_BY_CURRENT_ACCOUNT_SUCCESS";
        public static final String GET_ROLE_BY_FAMILY_SUCCESS = "GET_ROLE_BY_FAMILY_SUCCESS";
        public static final String CHECK_SYSTEM_ACCOUNT_SUCCESS = "CHECK_SYSTEM_ACCOUNT_SUCCESS";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class SubscriptionPlan {
        public static final String CREATE_SUBSCRIPTION_PLAN_SUCCESS = "CREATE_SUBSCRIPTION_PLAN_SUCCESS";
        public static final String UPDATE_SUBSCRIPTION_PLAN_SUCCESS = "UPDATE_SUBSCRIPTION_PLAN_SUCCESS";
        public static final String GET_SUBSCRIPTION_PLAN_SUCCESS = "GET_SUBSCRIPTION_PLAN_SUCCESS";
        public static final String GET_ALL_SUBSCRIPTION_PLANS_SUCCESS = "GET_ALL_SUBSCRIPTION_PLANS_SUCCESS";
        public static final String GET_ACTIVE_SUBSCRIPTION_PLANS_SUCCESS = "GET_ACTIVE_SUBSCRIPTION_PLANS_SUCCESS";
        public static final String TOGGLE_SUBSCRIPTION_PLAN_SUCCESS = "TOGGLE_SUBSCRIPTION_PLAN_SUCCESS";
        public static final String DELETE_SUBSCRIPTION_PLAN_SUCCESS = "DELETE_SUBSCRIPTION_PLAN_SUCCESS";
    }
}
