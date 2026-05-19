package com.codewithdang.kltn_giaphaonline.controller;

import com.codewithdang.kltn_giaphaonline.dto.request.ChangePasswordAccountReq;
import com.codewithdang.kltn_giaphaonline.dto.request.ChangeStatusLockReq;
import com.codewithdang.kltn_giaphaonline.dto.request.CreateAccountReq;
import com.codewithdang.kltn_giaphaonline.dto.request.UpdateAccountReq;
import com.codewithdang.kltn_giaphaonline.dto.response.AccountDetailsRes;
import com.codewithdang.kltn_giaphaonline.dto.response.AccountRes;
import com.codewithdang.kltn_giaphaonline.dto.response.ApiResponse;
import com.codewithdang.kltn_giaphaonline.dto.response.PageResponse;
import com.codewithdang.kltn_giaphaonline.service.account.AccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("${api.prefix}/accounts")
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Account Management")
public class AccountController {
    AccountService accountService;

    @GetMapping
    ResponseEntity<ApiResponse<PageResponse<AccountRes>>> getAccounts(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false) String status,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.success(200, "GET_ACCOUNTS_SUCCESS",
                        accountService.getAccounts(keyword, status, pageable))
        );
    }

    @GetMapping("/{accountId}")
    ResponseEntity<ApiResponse<AccountRes>> getAccountById(@PathVariable Long accountId) {
        return ResponseEntity.ok(
                ApiResponse.success(200, "GET_ACCOUNT_SUCCESS", accountService.getAccountById(accountId))
        );
    }

    @GetMapping("/MyInfo")
    ResponseEntity<ApiResponse<AccountDetailsRes>> getMyInfo() {
        return ResponseEntity.ok(
                ApiResponse.success(200, "GET_MY_INFO_SUCCESS", accountService.getMyInfo())
        );
    }

    @PostMapping
    ResponseEntity<ApiResponse<AccountRes>> createAccount(@Valid @RequestBody CreateAccountReq req) {
        return ResponseEntity.status(201).body(
                ApiResponse.success(201, "CREATE_ACCOUNT_SUCCESS", accountService.createAccount(req))
        );
    }

    @PutMapping("/change-pass/{accountId}")
    ResponseEntity<ApiResponse<Void>> changePass(@PathVariable Long accountId, @Valid @RequestBody ChangePasswordAccountReq req) {
        accountService.changePassword(accountId, req);
        return ResponseEntity.ok(
                ApiResponse.success(200, "CHANGE_PASSWORD_SUCCESS", null)
        );
    }

    @PutMapping("/{accountId}")
    ResponseEntity<ApiResponse<Void>> updateAccount(@PathVariable Long accountId, @Valid @RequestBody UpdateAccountReq req) {
        accountService.updateAccount(accountId, req);
        return ResponseEntity.ok(
                ApiResponse.success(200, "UPDATE_ACCOUNT_SUCCESS", null)
        );
    }

    @PutMapping("/change-status-lock/{accountId}")
    ResponseEntity<ApiResponse<AccountRes>> changeStatusLock(@PathVariable Long accountId, @Valid @RequestBody ChangeStatusLockReq req) {
        return ResponseEntity.ok(
                ApiResponse.success(200, "UPDATE_ACCOUNT_STATUS_SUCCESS", accountService.updateAccountStatus(accountId, req))
        );
    }

    @PutMapping("/change-avatar")
    ResponseEntity<ApiResponse<Void>> changeAvatar(@RequestParam Long accountId,
                                                   @RequestPart("file") MultipartFile file) {
        accountService.changeAvatar(accountId, file);
        return ResponseEntity.ok(
                ApiResponse.success(200, "UPDATE_AVATAR_SUCCESS", null)
        );
    }

    @PostMapping("/add-role-to-account")
    ResponseEntity<ApiResponse<Void>> addRole(@RequestParam Long accountId, @RequestParam String roleName) {
        accountService.addRole(accountId, roleName);
        return ResponseEntity.ok(
                ApiResponse.success(200, "ADD_ROLE_SUCCESS", null)
        );
    }

    @DeleteMapping("/remove-role-from-account")
    ResponseEntity<ApiResponse<Void>> removeRoleFromAccount(@RequestParam Long accountId, @RequestParam String roleName) {
        accountService.removeRole(accountId, roleName);
        return ResponseEntity.ok(
                ApiResponse.success(200, "REMOVE_ROLE_SUCCESS", null)
        );
    }

    @DeleteMapping("/soft-delete/{accountId}")
    ResponseEntity<ApiResponse<Void>> softDeleteAccount(@PathVariable Long accountId) {
        accountService.softDeleteAccount(accountId);
        return ResponseEntity.ok(
                ApiResponse.success(200, "SOFT_DELETE_ACCOUNT_SUCCESS", null)
        );
    }

    @DeleteMapping("/hard-delete/{accountId}")
    ResponseEntity<ApiResponse<Void>> hardDeleteAccount(@PathVariable Long accountId) {
        accountService.hardDeleteAccount(accountId);
        return ResponseEntity.ok(
                ApiResponse.success(200, "HARD_DELETE_ACCOUNT_SUCCESS", null)
        );
    }
}