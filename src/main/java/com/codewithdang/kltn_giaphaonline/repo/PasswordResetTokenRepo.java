package com.codewithdang.kltn_giaphaonline.repo;

import com.codewithdang.kltn_giaphaonline.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepo extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByOtp(String otp);

    @Query("""
                SELECT COUNT(o) > 0
                FROM PasswordResetToken o
                WHERE o.otp = :otp
                AND o.isSuccess = true
            """)
    boolean isOtpVerified(@Param("otp") String otp);
}
