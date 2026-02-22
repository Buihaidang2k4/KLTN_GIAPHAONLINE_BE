package com.codewithdang.kltn_giaphaonline.service.auth;

import com.codewithdang.kltn_giaphaonline.dto.request.AuthReq;
import com.codewithdang.kltn_giaphaonline.dto.response.AuthRes;
import com.codewithdang.kltn_giaphaonline.dto.response.IntrospectRes;
import com.codewithdang.kltn_giaphaonline.entity.Account;
import com.codewithdang.kltn_giaphaonline.entity.AccountRole;
import com.codewithdang.kltn_giaphaonline.entity.RevokedToken;
import com.codewithdang.kltn_giaphaonline.enums.AccountStatus;
import com.codewithdang.kltn_giaphaonline.exception.AppException;
import com.codewithdang.kltn_giaphaonline.exception.ErrorCode;
import com.codewithdang.kltn_giaphaonline.repo.AccountRepo;
import com.codewithdang.kltn_giaphaonline.repo.AccountRoleRepo;
import com.codewithdang.kltn_giaphaonline.repo.RevokedTokenRepo;
import com.codewithdang.kltn_giaphaonline.service.revoked_token.RevokedTokenService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
    AccountRepo accountRepo;
    PasswordEncoder passwordEncoder;
    RevokedTokenService revokedTokenService;

    @NonFinal
    @Value("${jwt.secret}")
    protected String TOKEN_KEY;
    @NonFinal
    @Value("${jwt.expiration}")
    protected Long ACCESS_TOKEN_DURATION;
    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected Long REFRESH_TOKEN_DURATION;


    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final Duration REFRESH_COOKIES_EXPIRATION = Duration.ofDays(7);

    @Override
    public AuthRes authenticate(AuthReq authReq, HttpServletResponse httpRes) throws ParseException {
        Account account = accountRepo.findByEmail(authReq.email())
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        if (!account.getAccountStatus().equals(AccountStatus.ACTIVE))
            throw new AppException(ErrorCode.ACCOUNT_NOT_ACTIVE);

        if (!passwordEncoder.matches(authReq.password(), account.getPasswordHash()))
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);

        String accessToken = buildToken(account, Duration.ofMillis(ACCESS_TOKEN_DURATION), ACCESS_TOKEN);
        String refreshToken = buildToken(account, Duration.ofMillis(REFRESH_TOKEN_DURATION), REFRESH_TOKEN);

        ResponseCookie cookieAccess = buildCookie(accessToken, ACCESS_TOKEN);
        ResponseCookie cookieRefresh = buildCookie(refreshToken, REFRESH_TOKEN);

        // set cookies
        httpRes.addHeader(HttpHeaders.SET_COOKIE, cookieAccess.toString());
        httpRes.addHeader(HttpHeaders.SET_COOKIE, cookieRefresh.toString());

        Instant expiresAt = Instant.now().plusMillis(ACCESS_TOKEN_DURATION);

        return new AuthRes(expiresAt);
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws ParseException, JOSEException {
        String refreshTokenFromCookieOld = getTokenFromCookie(request, REFRESH_TOKEN);
        if (refreshTokenFromCookieOld == null)
            throw new AppException(ErrorCode.REFRESH_TOKEN_NOT_EXIST_IN_COOKIES);

        // check revoked
        if (revokedTokenService.isTokenRevoked(refreshTokenFromCookieOld))
            throw new AppException(ErrorCode.TOKEN_REVOKED);

        SignedJWT signedJWT = verifyToken(refreshTokenFromCookieOld, true);
        JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
        String email = jwtClaimsSet.getSubject();

        // Ngay khi sử dụng thành công, đưa RT cũ vào danh sách đen
        Instant expiryTime = jwtClaimsSet.getExpirationTime().toInstant();
        revokedTokenService.revokedToken(refreshTokenFromCookieOld, expiryTime);

        Account account = accountRepo.
                findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        String newAccessToken = buildToken(account, Duration.ofMillis(ACCESS_TOKEN_DURATION), ACCESS_TOKEN);
        String newRefreshToken = buildToken(account, Duration.ofMillis(REFRESH_TOKEN_DURATION), REFRESH_TOKEN);

        ResponseCookie cookieAccess = buildCookie(newAccessToken, ACCESS_TOKEN);
        ResponseCookie cookieRefresh = buildCookie(newRefreshToken, REFRESH_TOKEN);

        response.addHeader(HttpHeaders.SET_COOKIE, cookieAccess.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, cookieRefresh.toString());
    }

    @Override
    public IntrospectRes introspect(HttpServletRequest request) throws ParseException, JOSEException {
        String token = getTokenFromCookie(request, ACCESS_TOKEN);
        if (token == null) return new IntrospectRes(false, null);

        boolean isValid = true;

        JWTClaimsSet claimsSet = decodeToken(token);
        Date expiration = claimsSet.getExpirationTime();
        Instant exp = expiration.toInstant();

        try {
            if (revokedTokenService.isTokenRevoked(token)) {
                throw new AppException(ErrorCode.TOKEN_REVOKED);
            }
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
            log.warn("Token introspection failed: {}", e.getMessage());
        }

        return IntrospectRes.builder()
                .valid(isValid)
                .exp(exp)
                .build();
    }


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) throws ParseException {
        String refreshToken = getTokenFromCookie(request, REFRESH_TOKEN);
        String accessToken = getTokenFromCookie(request, ACCESS_TOKEN);

        if (accessToken != null)
            revokedTokenService.revokedToken(
                    accessToken,
                    decodeToken(accessToken).getExpirationTime().toInstant()
            );

        // Thu hồi Refresh Token
        if (refreshToken != null)
            revokedTokenService.revokedToken(
                    refreshToken,
                    decodeToken(refreshToken).getExpirationTime().toInstant()
            );

        // Clear cookies
        response.addHeader(HttpHeaders.SET_COOKIE, clearCookie(ACCESS_TOKEN).toString());
        response.addHeader(HttpHeaders.SET_COOKIE, clearCookie(REFRESH_TOKEN).toString());
    }


    private ResponseCookie clearCookie(String cookieName) {
        return ResponseCookie.from(cookieName, "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();
    }

    private String getTokenFromCookie(HttpServletRequest request, String cookieName) {
        return Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(c -> cookieName.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);

        // verify
        JWSVerifier jwsVerifier = new MACVerifier(TOKEN_KEY.getBytes());
        boolean verified = signedJWT.verify(jwsVerifier);

        if (!verified)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

        // check exp
        Date expiryTime = claimsSet.getExpirationTime();
        if (expiryTime == null || expiryTime.before(new Date()))
            throw new AppException(ErrorCode.TOKEN_EXPIRED);

        // check type
        var typeClaim = claimsSet.getClaim("type");
        if (typeClaim == null)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        String type = typeClaim.toString();

        if (isRefresh && !REFRESH_TOKEN.equals(type))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (!isRefresh && !ACCESS_TOKEN.equals(type))
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        return signedJWT;
    }

    private String buildToken(Account account, Duration duration, String type) {
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(account.getEmail())
                .issuer(account.getFullName())
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(duration).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(account))
                .claim("type", type)
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

        try {
            signedJWT.sign(new MACSigner(TOKEN_KEY.getBytes(StandardCharsets.UTF_8)));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return signedJWT.serialize();
    }


    // Lấy quyền trong từng dòng họ (Dùng cho logic nghiệp vụ gia phả)
//    private List<Map<String, Object>> buildFamilyClaims(Account account) {
//        return familyMemberRepo.findAllByAccount_AccountId(account.getAccountId()).stream()
//                .map(fm -> {
//                    Map<String, Object> map = new HashMap<>();
//                    map.put("fid", fm.getFamily().getFamilyId());
//                    map.put("role", fm.getRole().name());
//                    return map;
//                }).toList();
//    }

    private String buildScope(Account account) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        Set<AccountRole> accountRoles = account.getAccountRoles();

        if (!CollectionUtils.isEmpty(accountRoles)) {
            accountRoles.forEach(ar -> {
                stringJoiner.add("ROLE_" + ar.getRole().getName());
            });
        }

        return stringJoiner.toString();
    }


    public ResponseCookie buildCookie(String refreshToken, String cookieName) {
        return ResponseCookie.from(cookieName, refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .maxAge(REFRESH_COOKIES_EXPIRATION)
                .path("/")
                .build();
    }

    public JWTClaimsSet decodeToken(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT.getJWTClaimsSet();
    }
}
