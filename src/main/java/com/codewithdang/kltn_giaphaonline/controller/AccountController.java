package com.codewithdang.kltn_giaphaonline.controller;


import com.codewithdang.kltn_giaphaonline.dto.request.ChangePasswordAccountReq;
import com.codewithdang.kltn_giaphaonline.dto.request.ChangeStatusLockReq;
import com.codewithdang.kltn_giaphaonline.dto.request.CreateAccountReq;
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
    ResponseEntity<ApiResponse<PageResponse<AccountRes>>> getAccounts(Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>(200, "ACCOUNTS", accountService.getAccounts(pageable)));
    }

    @GetMapping("/{accountId}")
    ResponseEntity<ApiResponse<AccountRes>> getAccountById(@PathVariable Long accountId) {
        return ResponseEntity.ok(new ApiResponse<>(200, "ACCOUNT", accountService.getAccountById(accountId)));
    }

    @PostMapping
    ResponseEntity<ApiResponse<AccountRes>> createAccount(@Valid @RequestBody CreateAccountReq req) {
        return ResponseEntity.ok(new ApiResponse<>(201, "ACCOUNT", accountService.createAccount(req)));
    }

    @PutMapping("/change-pass/{accountId}")
    ResponseEntity<ApiResponse<?>> changePass(@PathVariable Long accountId, @Valid @RequestBody ChangePasswordAccountReq req) {
        accountService.changePassword(accountId, req);
        return ResponseEntity.ok(new ApiResponse<>(200, "CHANGE PASS ACCOUNT SUCCESS", null));
    }

    @PutMapping("/change-status-lock/{accountId}")
    ResponseEntity<ApiResponse<?>> changeStatusLock(@PathVariable Long accountId, @Valid @RequestBody ChangeStatusLockReq req) {
        return ResponseEntity.ok(new ApiResponse<>(200, "CHANGE STATUS LOCK ACCOUNT SUCCESS", accountService.updateAccountStatus(accountId, req)));
    }

    @PutMapping("/change-avatar")
    ResponseEntity<ApiResponse<?>> changeStatusLock(@RequestParam Long accountId,
                                                    @RequestPart("file") MultipartFile file) {
        accountService.changeAvatar(accountId, file);
        return ResponseEntity.ok(new ApiResponse<>(200, "CHANGE AVATAR ACCOUNT SUCCESS", null));
    }

    @PostMapping("/add-role-to-account")
    ResponseEntity<ApiResponse<?>> addRole(@RequestParam Long accountId, @RequestParam String roleName) {
        accountService.addRole(accountId, roleName);
        return ResponseEntity.ok(new ApiResponse(200, "ADD ROLE SUCCESS", null));
    }

    @DeleteMapping("/remove-role-from-account")
    ResponseEntity<ApiResponse<?>> removeRoleFromAccount(@RequestParam Long accountId, @RequestParam String roleName) {
        accountService.removeRole(accountId, roleName);
        return ResponseEntity.ok(new ApiResponse(200, "REMOVE ROLE SUCCESS", null));
    }

    @DeleteMapping("/{accountId}")
    ResponseEntity<ApiResponse<?>> deleteAccount(@PathVariable Long accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.ok(new ApiResponse(200, "DELETE ACCOUNT SUCCESS", null));
    }
}
