package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForgotPasswordRepo extends JpaRepository<PasswordResetToken, Long> {
}
