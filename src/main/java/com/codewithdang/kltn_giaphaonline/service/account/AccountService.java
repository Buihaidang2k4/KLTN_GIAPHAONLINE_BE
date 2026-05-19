package com.codewithdang.kltn_giaphaonline.service.account;

import com.codewithdang.kltn_giaphaonline.dto.request.ChangeStatusLockReq;
import com.codewithdang.kltn_giaphaonline.dto.request.CreateAccountReq;
import com.codewithdang.kltn_giaphaonline.dto.request.ChangePasswordAccountReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateAccountReq;
import com.codewithdang.kltn_giaphaonline.dto.response.AccountDetailsRes;
import com.codewithdang.kltn_giaphaonline.dto.response.AccountRes;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.entity.Account;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;


public interface AccountService {
    Account getCurrentAccount();

    AccountRes createAccount(CreateAccountReq req);

    void changePassword(Long accountId, ChangePasswordAccountReq req);

    void updateAccount(Long accountId, UpdateAccountReq accountReq);

    // lock / unlock
    AccountRes updateAccountStatus(Long accountId, ChangeStatusLockReq req);

    void softDeleteAccount(Long accountId); // soft delete

    void hardDeleteAccount(Long accountId);

    void addRole(Long accountId, String roleName);

    void removeRole(Long accountId, String roleName);

    void changeAvatar(Long accountId, MultipartFile avatarFile);

    PageResponse<AccountRes> getAccounts(String keyword, String status, Pageable pageable); // admin

    AccountRes getAccountById(Long accountId);

    AccountDetailsRes getMyInfo();
}
