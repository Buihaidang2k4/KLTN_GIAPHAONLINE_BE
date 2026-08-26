package com.codewithdang.kltn_giaphaonline.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageConstantsVi {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Auth {
        public static final String LOGIN_SUCCESS = "Đăng nhập thành công";
        public static final String REGISTER_SUCCESS = "Đăng ký tài khoản thành công";
        public static final String REGISTER_BY_INVITATION_SUCCESS = "Đăng ký tài khoản thành công. Vui lòng kiểm tra email để xác thực tài khoản.";
        public static final String REFRESH_TOKEN_SUCCESS = "Làm mới phiên đăng nhập thành công";
        public static final String INTROSPECT_SUCCESS = "Kiểm tra token thành công";
        public static final String LOGOUT_SUCCESS = "Đăng xuất thành công";
        public static final String LOGOUT_ALL_SUCCESS = "Đăng xuất khỏi tất cả các thiết bị thành công";
        public static final String GET_ACTIVE_SESSIONS_SUCCESS = "Lấy danh sách phiên đăng nhập thành công";
        public static final String VERIFY_ACCOUNT_SUCCESS = "Xác thực tài khoản thành công";
        public static final String RESEND_TOKEN_VERIFY_ACCOUNT_SUCCESS = "Gửi lại mã xác thực tài khoản thành công";
        public static final String FORGOT_PASSWORD_SEND_OTP_SUCCESS = "Gửi mã OTP quên mật khẩu thành công";
        public static final String FORGOT_PASSWORD_RESEND_OTP_SUCCESS = "Gửi lại mã OTP quên mật khẩu thành công";
        public static final String VERIFY_FORGOT_PASSWORD_OTP_HASH_SUCCESS = "Xác thực mã OTP quên mật khẩu thành công";
        public static final String RESET_PASSWORD_SUCCESS = "Đặt lại mật khẩu thành công";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Account {
        public static final String GET_ACCOUNTS_SUCCESS = "Lấy danh sách tài khoản thành công";
        public static final String GET_ACCOUNT_SUCCESS = "Lấy thông tin tài khoản thành công";
        public static final String GET_MY_INFO_SUCCESS = "Lấy thông tin cá nhân thành công";
        public static final String CREATE_ACCOUNT_SUCCESS = "Tạo tài khoản thành công";
        public static final String CHANGE_PASSWORD_SUCCESS = "Đổi mật khẩu thành công";
        public static final String UPDATE_ACCOUNT_SUCCESS = "Cập nhật thông tin tài khoản thành công";
        public static final String UPDATE_ACCOUNT_STATUS_SUCCESS = "Cập nhật trạng thái khóa tài khoản thành công";
        public static final String UPDATE_AVATAR_SUCCESS = "Cập nhật ảnh đại diện thành công";
        public static final String ADD_ROLE_SUCCESS = "Gán vai trò cho tài khoản thành công";
        public static final String REMOVE_ROLE_SUCCESS = "Gỡ vai trò khỏi tài khoản thành công";
        public static final String SOFT_DELETE_ACCOUNT_SUCCESS = "Xóa tạm thời tài khoản thành công";
        public static final String HARD_DELETE_ACCOUNT_SUCCESS = "Xóa vĩnh viễn tài khoản thành công";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Album {
        public static final String CREATE_ALBUM_SUCCESS = "Tạo album thành công";
        public static final String UPDATE_ALBUM_SUCCESS = "Cập nhật album thành công";
        public static final String DELETE_ALBUM_SUCCESS = "Xóa album thành công";
        public static final String GET_ALBUMS_BY_FAMILY_SUCCESS = "Lấy danh sách album gia tộc thành công";
        public static final String GET_ALBUM_SUCCESS = "Lấy thông tin album thành công";
        public static final String UPLOAD_MEDIA_SUCCESS = "Tải lên media thành công";
        public static final String UPLOAD_MULTIPLE_MEDIA_SUCCESS = "Tải lên nhiều media thành công";
        public static final String GET_ALBUM_MEDIA_SUCCESS = "Lấy danh sách media của album thành công";
        public static final String UPLOAD_LINK_SUCCESS = "Thêm liên kết media thành công";
        public static final String DELETE_MEDIA_SUCCESS = "Xóa media thành công";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ArticleCategory {
        public static final String CREATE_ARTICLE_CATEGORY_SUCCESS = "Tạo danh mục bài viết thành công";
        public static final String UPDATE_ARTICLE_CATEGORY_SUCCESS = "Cập nhật danh mục bài viết thành công";
        public static final String DELETE_ARTICLE_CATEGORY_SUCCESS = "Xóa danh mục bài viết thành công";
        public static final String GET_ARTICLE_CATEGORY_SUCCESS = "Lấy thông tin danh mục bài viết thành công";
        public static final String GET_ALL_ARTICLE_CATEGORY_SUCCESS = "Lấy danh sách danh mục bài viết thành công";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Article {
        public static final String CREATE_ARTICLE_SUCCESS = "Tạo bài viết thành công";
        public static final String UPDATE_ARTICLE_SUCCESS = "Cập nhật bài viết thành công";
        public static final String DELETE_ARTICLE_SUCCESS = "Xóa bài viết thành công";
        public static final String GET_ARTICLE_SUCCESS = "Lấy thông tin bài viết thành công";
        public static final String GET_ALL_ARTICLES_SUCCESS = "Lấy danh sách bài viết thành công";
        public static final String PUBLISH_ARTICLE_SUCCESS = "Xuất bản bài viết thành công";
        public static final String UNPUBLISH_ARTICLE_SUCCESS = "Hủy xuất bản bài viết thành công";
        public static final String TOGGLE_FEATURED_SUCCESS = "Thay đổi trạng thái bài viết nổi bật thành công";
        public static final String UPLOAD_IMAGE_SUCCESS = "Tải ảnh bài viết thành công";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class AuditLog {
        public static final String GET_LOG_BY_FAMILY_SUCCESS = "Lấy nhật ký hoạt động gia tộc thành công";
        public static final String GET_LOG_BY_ENTITY_SUCCESS = "Lấy nhật ký theo đối tượng thành công";
        public static final String GET_LOG_BY_ACTOR_SUCCESS = "Lấy nhật ký theo người thực hiện thành công";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Ceremony {
        public static final String GET_CEREMONY_SUCCESS = "Lấy thông tin nghi lễ thành công";
        public static final String GET_CEREMONY_LIST_SUCCESS = "Tải danh sách nghi lễ thành công";
        public static final String CREATE_CEREMONY_SUCCESS = "Tạo nghi lễ mới thành công";
        public static final String UPDATE_CEREMONY_SUCCESS = "Cập nhật nghi lễ thành công";
        public static final String DELETE_CEREMONY_SUCCESS = "Đã xóa nghi lễ thành công";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class CeremonyTimeline {
        public static final String CREATE_TIMELINE_SUCCESS = "Tạo tiến trình nghi lễ thành công";
        public static final String GET_TIMELINE_BY_ID_SUCCESS = "Lấy tiến trình nghi lễ thành công";
        public static final String GET_TIMELINE_BY_CEREMONY_SUCCESS = "Lấy danh sách tiến trình theo nghi lễ thành công";
        public static final String GET_ALL_TIMELINE_SUCCESS = "Lấy tất cả tiến trình nghi lễ thành công";
        public static final String UPDATE_TIMELINE_SUCCESS = "Cập nhật tiến trình nghi lễ thành công";
        public static final String DELETE_TIMELINE_SUCCESS = "Xóa tiến trình nghi lễ thành công";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class CeremonyTimelinePreparation {
        public static final String CREATE_PREPARATION_SUCCESS = "Tạo bước chuẩn bị nghi lễ thành công";
        public static final String GET_PREPARATION_BY_ID_SUCCESS = "Lấy bước chuẩn bị nghi lễ thành công";
        public static final String GET_PREPARATION_BY_TIMELINE_SUCCESS = "Lấy danh sách chuẩn bị theo tiến trình thành công";
        public static final String GET_ALL_PREPARATION_SUCCESS = "Lấy tất cả danh sách chuẩn bị thành công";
        public static final String UPDATE_PREPARATION_SUCCESS = "Cập nhật bước chuẩn bị thành công";
        public static final String DELETE_PREPARATION_SUCCESS = "Xóa bước chuẩn bị thành công";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Dashboard {
        public static final String GET_DASHBOARD_SUCCESS = "Lấy dữ liệu bảng điều khiển gia tộc thành công";
        public static final String GET_DASHBOARD_SYSTEM_SUCCESS = "Lấy dữ liệu bảng điều khiển hệ thống thành công";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Family {
        public static final String CREATE_FAMILY_SUCCESS = "Tạo gia tộc thành công";
        public static final String GET_FAMILY_SUCCESS = "Lấy danh sách gia tộc thành công";
        public static final String DELETE_FAMILY_SUCCESS = "Xóa gia tộc thành công";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class FamilyAchievement {
        public static final String CREATE_ACHIEVEMENT_SUCCESS = "Tạo thành tựu gia tộc thành công";
        public static final String UPDATE_ACHIEVEMENT_SUCCESS = "Cập nhật thành tựu gia tộc thành công";
        public static final String DELETE_ACHIEVEMENT_SUCCESS = "Xóa thành tựu gia tộc thành công";
        public static final String GET_ACHIEVEMENT_SUCCESS = "Lấy thông tin thành tựu thành công";
        public static final String GET_ACHIEVEMENTS_SUCCESS = "Lấy danh sách thành tựu gia tộc thành công";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class FamilyCategory {
        public static final String CREATE_FAMILY_CATEGORY_SUCCESS = "Tạo danh mục chi/nhánh thành công";
        public static final String UPDATE_FAMILY_CATEGORY_SUCCESS = "Cập nhật danh mục chi/nhánh thành công";
        public static final String DELETE_FAMILY_CATEGORY_SUCCESS = "Xóa danh mục chi/nhánh thành công";
        public static final String GET_FAMILY_CATEGORY_BY_ID_SUCCESS = "Lấy thông tin danh mục chi/nhánh thành công";
        public static final String GET_ALL_CATEGORY_BY_FAMILY_ID_SUCCESS = "Lấy danh sách chi/nhánh theo gia tộc thành công";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class FamilyEvent {
        public static final String CREATE_EVENT_SUCCESS = "Tạo sự kiện gia tộc thành công";
        public static final String UPDATE_EVENT_SUCCESS = "Cập nhật sự kiện gia tộc thành công";
        public static final String DELETE_EVENT_SUCCESS = "Xóa sự kiện gia tộc thành công";
        public static final String GET_EVENTS_BY_FAMILY_SUCCESS = "Lấy danh sách sự kiện gia tộc thành công";
        public static final String GET_EVENT_SUCCESS = "Lấy thông tin sự kiện thành công";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class FamilyInvitation {
        public static final String GET_MY_INVITATION_SENT_SUCCESS = "Lấy danh sách lời mời đã gửi thành công";
        public static final String GET_MY_INVITATION_RECEIVED_SUCCESS = "Lấy danh sách lời mời đã nhận thành công";
        public static final String INVITE_MEMBER_SUCCESS = "Gửi lời mời tham gia gia tộc thành công";
        public static final String ACCEPT_INVITATION_SUCCESS = "Chấp nhận lời mời gia tộc thành công";
        public static final String REJECT_INVITATION_SUCCESS = "Từ chối lời mời gia tộc thành công";
        public static final String CANCEL_INVITATION_SUCCESS = "Hủy lời mời gia tộc thành công";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class FamilyMember {
        public static final String GET_FAMILY_MEMBER_SUCCESS = "Lấy danh sách thành viên gia tộc thành công";
        public static final String UPDATE_MEMBER_ROLE_SUCCESS = "Cập nhật vai trò thành viên thành công";
        public static final String REMOVE_MEMBER_SUCCESS = "Xóa thành viên khỏi gia tộc thành công";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class FamilyPostCategory {
        public static final String CREATE_POST_CATEGORY_SUCCESS = "Tạo danh mục bài đăng thành công";
        public static final String UPDATE_POST_CATEGORY_SUCCESS = "Cập nhật danh mục bài đăng thành công";
        public static final String DELETE_POST_CATEGORY_SUCCESS = "Xóa danh mục bài đăng thành công";
        public static final String GET_POST_CATEGORY_SUCCESS = "Lấy thông tin danh mục bài đăng thành công";
        public static final String GET_POST_CATEGORIES_BY_FAMILY_SUCCESS = "Lấy danh mục bài đăng theo gia tộc thành công";
        public static final String GET_ALL_POST_CATEGORIES_SUCCESS = "Lấy tất cả danh mục bài đăng thành công";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class FamilySubscription {
        public static final String GET_FAMILY_SUBSCRIPTION_SUCCESS = "Lấy thông tin gói đăng ký gia tộc thành công";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class FamilyTree {
        public static final String GET_FAMILY_TREE_SUCCESS = "Lấy sơ đồ cây gia phả thành công";
        public static final String CREATE_PERSON_SUCCESS = "Thêm thành viên phả hệ thành công";
        public static final String UPDATE_PERSON_SUCCESS = "Cập nhật thành viên phả hệ thành công";
        public static final String GET_PERSON_SUCCESS = "Lấy thông tin thành viên phả hệ thành công";
        public static final String DELETE_PERSON_SUCCESS = "Xóa thành viên phả hệ thành công";
        public static final String GET_PARTNERS_SUCCESS = "Lấy danh sách bạn đời thành công";
        public static final String GET_MOTHERS_SUCCESS = "Lấy danh sách mẹ thành công";
        public static final String ADD_ROOT_SUCCESS = "Thêm cụ tổ thành công";
        public static final String ADD_PARTNER_SUCCESS = "Thêm bạn đời thành công";
        public static final String ADD_CHILD_SUCCESS = "Thêm con thành công";
        public static final String ADD_RELATIONSHIP_SUCCESS = "Thêm mối quan hệ thành công";
        public static final String REMOVE_RELATIONSHIP_SUCCESS = "Xóa mối quan hệ thành công";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Feedback {
        public static final String CREATE_FEEDBACK_SUCCESS = "Gửi phản hồi thành công";
        public static final String HANDLE_FEEDBACK_SUCCESS = "Xử lý phản hồi thành công";
        public static final String GET_FEEDBACK_SUCCESS = "Lấy thông tin phản hồi thành công";
        public static final String GET_FEEDBACK_LIST_SUCCESS = "Lấy danh sách phản hồi thành công";
        public static final String DELETE_FEEDBACK_SUCCESS = "Xóa phản hồi thành công";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Notification {
        public static final String GET_NOTIFICATIONS_SUCCESS = "Lấy danh sách thông báo thành công";
        public static final String MARK_AS_READ_SUCCESS = "Đã đánh dấu thông báo là đã đọc";
        public static final String MARK_ALL_AS_READ_SUCCESS = "Đã đánh dấu tất cả thông báo là đã đọc";
        public static final String DELETE_NOTIFICATION_SUCCESS = "Xóa thông báo thành công";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Payment {
        public static final String GET_ALL_PAYMENTS_SUCCESS = "Lấy danh sách giao dịch thanh toán thành công";
        public static final String GET_ALL_PAYMENTS_BY_FAMILY_ID_SUCCESS = "Lấy danh sách thanh toán của gia tộc thành công";
        public static final String DELETE_PAYMENT_SUCCESS = "Xóa giao dịch thanh toán thành công";
        public static final String GET_PAYMENT_BY_TRANSACTION_ID_SUCCESS = "Lấy thông tin giao dịch thanh toán thành công";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class VNPay {
        public static final String CREATE_PAYMENT_SUCCESS = "Tạo giao dịch thanh toán VNPay thành công";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Permission {
        public static final String GET_ALL_PERMISSIONS_SUCCESS = "Lấy danh sách quyền thành công";
        public static final String GET_ALL_PERMISSIONS_LIST_SUCCESS = "Lấy tất cả danh sách quyền thành công";
        public static final String GET_PERMISSION_SUCCESS = "Lấy thông tin quyền thành công";
        public static final String CREATE_PERMISSION_SUCCESS = "Tạo quyền mới thành công";
        public static final String UPDATE_PERMISSION_SUCCESS = "Cập nhật quyền thành công";
        public static final String DELETE_PERMISSION_SUCCESS = "Xóa quyền thành công";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Role {
        public static final String GET_ROLES_SUCCESS = "Lấy danh sách vai trò thành công";
        public static final String CREATE_ROLE_SUCCESS = "Tạo vai trò thành công";
        public static final String ADD_PERMISSION_TO_ROLE_SUCCESS = "Thêm quyền vào vai trò thành công";
        public static final String REMOVE_PERMISSION_FROM_ROLE_SUCCESS = "Gỡ quyền khỏi vai trò thành công";
        public static final String DELETE_ROLE_SUCCESS = "Xóa vai trò thành công";
        public static final String GET_ROLE_BY_CURRENT_ACCOUNT_SUCCESS = "Lấy vai trò của tài khoản hiện tại thành công";
        public static final String GET_ROLE_BY_FAMILY_SUCCESS = "Lấy vai trò theo gia tộc thành công";
        public static final String CHECK_SYSTEM_ACCOUNT_SUCCESS = "Kiểm tra tài khoản quản trị hệ thống thành công";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class SubscriptionPlan {
        public static final String CREATE_SUBSCRIPTION_PLAN_SUCCESS = "Tạo gói dịch vụ thành công";
        public static final String UPDATE_SUBSCRIPTION_PLAN_SUCCESS = "Cập nhật gói dịch vụ thành công";
        public static final String GET_SUBSCRIPTION_PLAN_SUCCESS = "Lấy thông tin gói dịch vụ thành công";
        public static final String GET_ALL_SUBSCRIPTION_PLANS_SUCCESS = "Lấy danh sách gói dịch vụ thành công";
        public static final String GET_ACTIVE_SUBSCRIPTION_PLANS_SUCCESS = "Lấy danh sách gói dịch vụ đang hoạt động thành công";
        public static final String TOGGLE_SUBSCRIPTION_PLAN_SUCCESS = "Thay đổi trạng thái kích hoạt gói dịch vụ thành công";
        public static final String DELETE_SUBSCRIPTION_PLAN_SUCCESS = "Xóa gói dịch vụ thành công";
    }
}
